package com.npc.test;


import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.IRenderCall;
import com.npc.MyTask;
import com.npc.ThreadManager;
import com.npc.ThreadManagerExample;
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
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber(modid = NpcTestMod.MOD_ID)
public class PlayerChatEvent {


    public static String msg = "";
    public static String NpcData = null;
    public static long lastRequest = 0;
    public static  int count = 0;
    public static String setting = "";
    public static ThreadManager threadManager = new ThreadManager(new ThreadPoolExecutor(10, 10, 10L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()));
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
    public String getClientmsg(ServerChatEvent event) throws SQLException, ClassNotFoundException, ExecutionException, InterruptedException, IOException {
        String msg = event.getMessage();
        System.out.println(event);

        NpcEntity.msg = msg;

        if(msg.charAt(0) == '#') {
            lastRequest = System.currentTimeMillis();
//            File file = new File("C:\\Users\\Dingo\\Documents\\GitHub\\NPC\\src\\main\\java\\com\\npc\\test\\NpcSetting.json");
            File file = new File("C:\\Users\\lili\\Desktop\\NPC\\src\\main\\java\\com\\npc\\test\\NpcSetting.json");
            String settingContent = FileUtils.readFileToString(file,"UTF-8");
            setting = settingContent;

//            String text = new String(Files.readAllBytes(Paths.get("C:\\Users\\Dingo\\Documents\\GitHub\\NPC\\src\\main\\java\\com\\npc\\test\\ChatRecord.txt")), StandardCharsets.UTF_8);
            String text = new String(Files.readAllBytes(Paths.get("C:\\Users\\lili\\Desktop\\NPC\\src\\main\\java\\com\\npc\\test\\ChatRecord.txt")), StandardCharsets.UTF_8);

            //event.getPlayer().sendMessage(new StringTextComponent("ChatGPT: " + NpcEntity.replay), event.getPlayer().getUUID());




 //            if(threadManager.getActCount() < 1 ){
//                threadManager.execute(task);
//            }else{
//                MyTask.followmsg = msg;
//            }





//            CompletableFuture<Boolean> future1 = CompletableFuture.supplyAsync(() -> {
//                while(true){
//                    if(!Objects.equals(PlayerChatEvent.msg, ""))break;
//                }
//               return true;
//            });
//            System.out.println(PlayerChatEvent.msg);
//            if(Objects.equals(PlayerChatEvent.msg, ""))System.out.println("??????????????");
            //event.getPlayer().sendMessage(new StringTextComponent("ChatGPT: "+ PlayerChatEvent.msg),event.getPlayer().getUUID());

//            String regex ="[-]?\\d*";
//            Pattern p = Pattern.compile(regex);
//            Matcher m = p.matcher(PlayerChatEvent.msg);
//            int[] arr = new int[3];
//            //arr[0] = arr[1] = arr[2] = 0;
//            int count = 0;
//            while (m.find()) {
//                if (!"".equals(m.group())){
//                    arr[count++] = Integer.valueOf(m.group());
//                }
//            }
//            NpcEntity.pos = new BlockPos(arr[0],arr[1],arr[2]);


        }
        return null;

    }




//    @SubscribeEvent
//    public String getd(ClientChatEvent event) throws SQLException {
//        System.out.println(event);
//        String msg = event.getMessage();
//        System.out.println(event.getOriginalMessage()+ "????????");
//        msg = event.getMessage();
//
//        NpcEntity.msg = msg;
//
//        if(NpcEntity.msg != "") {
//            lastRequest = System.currentTimeMillis();
//            String finalMsg = msg;
//            Thread t = new Thread(() -> {
//                try {
//                    MySQLExample sqlExample = new MySQLExample();
//                    NpcEntity.msg = "";
//                    System.out.println(response);
//                    NpcEntity.replay = response;
//
//                    String str = finalMsg;
//                    String regex ="[-]?\\d*";
//                    Pattern p = Pattern.compile(regex);
//
//                    Matcher m = p.matcher(str);
//                    int[] arr = new int[3];
//                    //arr[0] = arr[1] = arr[2] = 0;
//                    int count = 0;
//                    while (m.find()) {
//                        if (!"".equals(m.group())){
//                            System.out.println("come here:"+ m.group());
//                            arr[count++] = Integer.valueOf(m.group());
//                        }
//                    }
//                    //System.out.println(arr);
//                    //if(arr[0] != 0 && arr[1] != 0 &&arr[2] != 0)
//                    NpcEntity.pos = new BlockPos(arr[0],arr[1],arr[2]);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//            t.start();
//        }
//        return null;
//
//    }

//    @SubscribeEvent
//    public String getdsd(PlayerEvent event) {
//        //event.getPlayer().getMainHandItem();
//
//        if(NpcEntity.replay != ""){
//            //event.getPlayer().displayClientMessage(new StringTextComponent(NpcEntity.replay),true);
//            event.getPlayer().sendMessage(new StringTextComponent("ChatGPT: "+ NpcEntity.replay),null);
//            NpcEntity.replay = "";
//        }
//
//        return "";
//    }


}

