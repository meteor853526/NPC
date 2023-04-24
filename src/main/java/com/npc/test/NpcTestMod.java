package com.npc.test;

import com.mojang.brigadier.CommandDispatcher;
import com.npc.test.client.ClientBubblesUtil;
import com.npc.test.entity.ModEntityTypes;
import com.npc.test.entity.chatbubble.ChatBubbleManger;
import com.npc.test.entity.render.DeliveryRenderer;
import com.npc.test.entity.render.NpcRenderer;
import com.npc.test.init.InitEntities;
import com.npc.test.init.InitTrigger;
import com.npc.test.item.ModItems;
import com.npc.test.packet.SCSendModPresent;
import com.npc.test.packet.SCSyncBubbleMessage;
import com.npc.test.passive.NpcEntity;

import com.npc.test.server.BubblesCustomCommand;
import com.npc.test.util.ResetUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NpcTestMod.MOD_ID)
@Mod.EventBusSubscriber(modid="npctestmod", bus=Mod.EventBusSubscriber.Bus.MOD)
public class NpcTestMod
{
    // Directly reference a log4j logger.

    public static final String MOD_ID = "npctestmod";
    public static final Logger LOGGER = LogManager.getLogger();
    public static SimpleChannel channel = NetworkRegistry.newSimpleChannel((ResourceLocation)new ResourceLocation("npctestmod", "npctestmod"), () -> "1", "1"::equals, "1"::equals);
    CommandDispatcher<CommandSource> commandDispatcher;
    public NpcTestMod() {
        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntityTypes.register(eventBus);
        ModItems.register(eventBus);

        eventBus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        eventBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        eventBus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        eventBus.addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new PlayerChatEvent()); // register event
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommand);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarted);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BubblesConfig.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, BubblesConfig.SERVER_SPEC);
        ChatBubbleManger.initDefaultChat();
        InitEntities.DATA_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        InitTrigger.init();


    }
    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

        event.enqueueWork(() ->
        {
            GlobalEntityTypeAttributes.put(ModEntityTypes.NPC.get(), NpcEntity.setCustomAttributes().build());
            GlobalEntityTypeAttributes.put(ModEntityTypes.Delivery_NPC.get(), NpcEntity.setCustomAttributes().build());
        });
        int index = 0;
        channel.registerMessage(index++, SCSendModPresent.class, SCSendModPresent::encode, SCSendModPresent::decode, SCSendModPresent::handle);
        channel.registerMessage(index, SCSyncBubbleMessage.class, SCSyncBubbleMessage::encode, SCSyncBubbleMessage::decode, SCSyncBubbleMessage::handle);
    }

    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent event) {
        ClientBubblesUtil.registerBindings();
        if (((List)BubblesConfig.CLIENT.colorOutline.get()).isEmpty() || ((List)BubblesConfig.CLIENT.colorInside.get()).isEmpty() || ((List)BubblesConfig.CLIENT.colorText.get()).isEmpty()) {
            ResetUtil.resetColors();
        }
    }
    private void onRegisterCommand(RegisterCommandsEvent event) {
        this.commandDispatcher = event.getDispatcher();
    }

    private void onServerStarted(FMLServerStartedEvent event) {
        LOGGER.info("---------------------");
        if (!((String)BubblesConfig.SERVER.commandName.get()).isEmpty()) {
            BubblesCustomCommand.register(this.commandDispatcher);
            LOGGER.info("Server registered /" + (String)BubblesConfig.SERVER.commandName.get() + " as command for comics bubbles chat");
        }
        LOGGER.info("Server " + ((Boolean)BubblesConfig.SERVER.chatListener.get() != false ? "enable" : "disable") + " chat listener for comics bubbles chat");
        LOGGER.info("Server " + ((Boolean)BubblesConfig.SERVER.canThroughBlocks.get() != false ? "enable" : "disable") + " bubbles through blocks for comics bubbles chat");
        LOGGER.info("Server set range for comics bubbles chat packet on " + BubblesConfig.SERVER.bubbleRange.get());
        LOGGER.info("You'r server config file is on your directory of your world !");
        LOGGER.info("---------------------");
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.NPC.get(), NpcRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.Delivery_NPC.get(), DeliveryRenderer::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
