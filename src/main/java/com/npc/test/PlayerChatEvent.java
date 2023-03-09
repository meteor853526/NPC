package com.npc.test;


import com.google.gson.JsonObject;
import com.npc.test.commands.NpcMoveCommand;
import com.npc.test.commands.ReturnHomeCommand;
import com.npc.test.commands.SetHomeCommand;
import com.npc.test.passive.NpcEntity;
import net.minecraft.client.util.JSONException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;


import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber(modid = NpcTestMod.MOD_ID)
public class PlayerChatEvent {


    public static String msg = "";
    public static long lastRequest = 0;
    @SubscribeEvent
    public String getMsg(PlayerEvent event) {
        ItemStack item = event.getPlayer().getMainHandItem();
        //System.out.println(new PlayerInteractEvent.LeftClickBlock(player,player.blockPosition(),player.getDirection()).getUseBlock());

        String tool = String.valueOf(item);

        if(tool == "diamond_pickaxe" ){

        }
        //String tool = String.valueOf(event.getPlayer().getMainHandItem());
        //event.getPlayer().sendMessage(new StringTextComponent( tool),null);
//        NpcEntity.msg = event.getGuiContainer().getNarrationMessage();
//        return event.getGuiContainer().getNarrationMessage();
        return "";
    }
    @SubscribeEvent
    public static void BlockLeftClickEvent(PlayerInteractEvent.LeftClickBlock event) {
        String tool = String.valueOf(event.getPlayer().getMainHandItem());
        System.out.println("?" + tool + "?");
        if(tool.equals("1 diamond_sword")){
            System.out.println(event.getPos());
            NpcEntity.pos = event.getPos();
        }

    }




    @SubscribeEvent
    public String getd(ClientChatEvent event) {
        System.out.println(event);
        String msg = event.getMessage();

//        String str = msg ;
//        String regex ="[-]?\\d*";
//        Pattern p = Pattern.compile(regex);
//
//        Matcher m = p.matcher(str);
//        int[] arr = new int[3];
//        //arr[0] = arr[1] = arr[2] = 0;
//        int count = 0;
//        while (m.find()) {
//            if (!"".equals(m.group())){
//                System.out.println("come here:"+ m.group());
//                arr[count++] = Integer.valueOf(m.group());
//            }
//        }
//        //System.out.println(arr);
//        //if(arr[0] != 0 && arr[1] != 0 &&arr[2] != 0)
//        NpcEntity.pos = new BlockPos(arr[0],arr[1],arr[2]);


        NpcEntity.msg = msg;

        if(NpcEntity.msg != "") {
            lastRequest = System.currentTimeMillis();
            Thread t = new Thread(() -> {
                try {
                    String response = RequestHandler.getAIResponse(msg);
                    NpcEntity.msg = "";
                    System.out.println(response);
                    NpcEntity.replay = response;

                    String str = msg ;
                    String regex ="[-]?\\d*";
                    Pattern p = Pattern.compile(regex);

                    Matcher m = p.matcher(str);
                    int[] arr = new int[3];
                    //arr[0] = arr[1] = arr[2] = 0;
                    int count = 0;
                    while (m.find()) {
                        if (!"".equals(m.group())){
                            System.out.println("come here:"+ m.group());
                            arr[count++] = Integer.valueOf(m.group());
                        }
                    }
                    //System.out.println(arr);
                    //if(arr[0] != 0 && arr[1] != 0 &&arr[2] != 0)
                    NpcEntity.pos = new BlockPos(arr[0],arr[1],arr[2]);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            t.start();
        }
//        String path = "/chat.json";
//
//        JsonObject json = new JsonObject();
//        json.addProperty("1. ", event.getMessage());
//
//        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
//            out.write(json.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return null;

    }

    @SubscribeEvent
    public String getdsd(PlayerEvent event) {
        //event.getPlayer().getMainHandItem();

        if(NpcEntity.replay != ""){
            //event.getPlayer().displayClientMessage(new StringTextComponent(NpcEntity.replay),true);
            event.getPlayer().sendMessage(new StringTextComponent("ChatGPT: "+ NpcEntity.replay),null);
            NpcEntity.replay = "";
        }

        return "";
    }




//    @SubscribeEvent
//    public String getdsd(PlayerEvent event) {
//        //event.getEntity().getHandSlots();
//        System.out.println(event.getEntity().getHandSlots());
//        return "";
//    }




//    @SubscribeEvent
//    public static void onCommandsRegister(RegisterCommandsEvent event) {
//        new SetHomeCommand(event.getDispatcher());
//        new ReturnHomeCommand(event.getDispatcher());
//        new NpcMoveCommand(event.getDispatcher());
//        ConfigCommand.register(event.getDispatcher());
//    }
//
//    @SubscribeEvent
//    public static void onPlayerCloneEvent(PlayerEvent.Clone event) {
//        if(!event.getOriginal().getCommandSenderWorld().isClientSide()) {
//            event.getPlayer().getPersistentData().putIntArray(NpcTestMod.MOD_ID + "homepos",
//                    event.getOriginal().getPersistentData().getIntArray(NpcTestMod.MOD_ID + "homepos"));
//        }
//    }

}

