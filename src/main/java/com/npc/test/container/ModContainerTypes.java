package com.npc.test.container;

import com.npc.test.NpcTestMod;
import com.npc.test.trade.NpcContainer;
import com.npc.test.trade.NpcGui;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ModContainerTypes {
		public static final DeferredRegister<ContainerType<?>> CONTAINERS = create(ForgeRegistries.CONTAINERS);
	 	public static final RegistryObject<ContainerType<NpcContainer>> NPC = register("npc_container", NpcContainer::new);
	    

	    private ModContainerTypes() {}

	    public static void register(IEventBus eventBus) {
	    	CONTAINERS.register(eventBus);
	    }

	    public static void registerScreens(FMLClientSetupEvent event) {
	        ScreenManager.register(NPC.get(), NpcGui::new);
	    }

	    private static <T extends Container> RegistryObject<ContainerType<T>> register(String name, IContainerFactory<T> factory) {
	        return CONTAINERS.register(name, () -> IForgeContainerType.create(factory));
        }

	    private static <T extends IForgeRegistryEntry<T>> DeferredRegister<T> create(IForgeRegistry<T> registry) {
	        return DeferredRegister.create(registry, NpcTestMod.MOD_ID);
	    }
}
