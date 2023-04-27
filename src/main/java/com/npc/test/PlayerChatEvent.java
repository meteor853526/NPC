package com.npc.test;


import com.npc.test.chat.ThreadManager;
import com.npc.test.chat.delivery_chat;
import com.npc.test.chat.farmer_chat;
import com.npc.test.passive.NpcEntity;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.*;

import org.apache.commons.io.FileUtils;


@Mod.EventBusSubscriber(modid = NpcTestMod.MOD_ID)
public class PlayerChatEvent {


    public static String ChatMsg = "";
    public static String NpcData = null;
    public static long lastRequest = 0;
    public static int count = 0;
    public static String setting = "";

    public static String ChatReply = "";
    public static String chatRecord ="";
    public static ThreadManager threadManager = new ThreadManager(new ThreadPoolExecutor(10, 10, 10L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()));
//    @SubscribeEvent
//    public String getMsg(PlayerEvent event) {
//        ItemStack item = event.getPlayer().getMainHandItem();
//        //System.out.println(new PlayerInteractEvent.LeftClickBlock(player,player.blockPosition(),player.getDirection()).getUseBlock());
//
//        String tool = String.valueOf(item);
//
//        if(tool == "diamond_pickaxe" ){
//
//        }
//        //String tool = String.valueOf(event.getPlayer().getMainHandItem());
//        //event.getPlayer().sendMessage(new StringTextComponent( tool),null);
////        NpcEntity.msg = event.getGuiContainer().getNarrationMessage();
////        return event.getGuiContainer().getNarrationMessage();
//        return "";
//    }
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
    public String getClientmsg(ServerChatEvent event) throws IOException {
        ChatMsg = event.getMessage();
        System.out.println(event);


        Path CharRecordPath = Paths.get("C:\\Users\\Dingo\\Documents\\GitHub\\NPC\\src\\main\\java\\com\\npc\\test\\chat\\ChatRecord.txt");
        String NpcSettingPath = "C:\\Users\\Dingo\\Documents\\GitHub\\NPC\\src\\main\\java\\com\\npc\\test\\chat\\NpcSetting.json";
        if(ChatMsg.charAt(0) == '#') {
            lastRequest = System.currentTimeMillis();
            File file = new File(NpcSettingPath);
            String settingContent = FileUtils.readFileToString(file,"UTF-8");
            setting = settingContent;
            String text = new String(Files.readAllBytes(CharRecordPath), StandardCharsets.UTF_8);
            chatRecord = text;
//            Thread t = new Thread(() -> {
//                try {
//
//                    String response = RequestHandler.getAIResponse("You play as an npc in minecraft. Then is your setting " +chatContent+"And you don't need to repeat the setting.And this the record we chat before "+chatcontent +"My question: "+event.getMessage());
//                    JSONObject chatjsonObject=new JSONObject(chatContent);
//                    DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss z");
//                    JSONObject temp=new JSONObject();
//
//                    temp.put("PlayerMessage",event.getMessage());
//                    temp.put("GPT reply",response);
//
//                    String date = dateFormat.format(new Date());
//                    chatjsonObject.put("Time("+date+")",temp);
//
//                    try {
//                        FileWriter fileWriter = new FileWriter("C:\\Users\\Dingo\\Documents\\GitHub\\NPC\\src\\main\\java\\com\\npc\\test\\ChatRecord.json");         // writing back to the file
//                        fileWriter.write(chatjsonObject.toString());
//                        fileWriter.flush();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    NpcEntity.msg = "";
//                    System.out.println(response);
//                    NpcEntity.replay = response;
//                    event.getPlayer().sendMessage(new StringTextComponent("ChatGPT: " + response), event.getPlayer().getUUID());
//                    String regex ="[-]?\\d*";
//                    Pattern p = Pattern.compile(regex);
//                    Matcher m = p.matcher(msg);
//                    int[] arr = new int[3];
//                    //arr[0] = arr[1] = arr[2] = 0;
//                    int count = 0;
//                    while (m.find()) {
//                        if (!"".equals(m.group())){
//                            System.out.println("come here:"+ m.group());
//                            arr[count++] = Integer.parseInt(m.group());
//                        }
//                    }
//                    NpcEntity.pos = new BlockPos(arr[0],arr[1],arr[2]);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//            t.start();
            threadManager.execute(new farmer_chat(count++,event,settingContent,chatRecord));
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

        if(ChatMsg.charAt(0) == '@') {
            lastRequest = System.currentTimeMillis();
            File file = new File(NpcSettingPath);
            String settingContent = FileUtils.readFileToString(file,"UTF-8");
            setting = settingContent;
            String text = new String(Files.readAllBytes(CharRecordPath), StandardCharsets.UTF_8);
            chatRecord = text;
//            Thread t = new Thread(() -> {
//                try {
//
//                    String response = RequestHandler.getAIResponse("You play as an npc in minecraft. Then is your setting " +chatContent+"And you don't need to repeat the setting.And this the record we chat before "+chatcontent +"My question: "+event.getMessage());
//                    JSONObject chatjsonObject=new JSONObject(chatContent);
//                    DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss z");
//                    JSONObject temp=new JSONObject();
//
//                    temp.put("PlayerMessage",event.getMessage());
//                    temp.put("GPT reply",response);
//
//                    String date = dateFormat.format(new Date());
//                    chatjsonObject.put("Time("+date+")",temp);
//
//                    try {
//                        FileWriter fileWriter = new FileWriter("C:\\Users\\Dingo\\Documents\\GitHub\\NPC\\src\\main\\java\\com\\npc\\test\\ChatRecord.json");         // writing back to the file
//                        fileWriter.write(chatjsonObject.toString());
//                        fileWriter.flush();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    NpcEntity.msg = "";
//                    System.out.println(response);
//                    NpcEntity.replay = response;
//                    event.getPlayer().sendMessage(new StringTextComponent("ChatGPT: " + response), event.getPlayer().getUUID());
//                    String regex ="[-]?\\d*";
//                    Pattern p = Pattern.compile(regex);
//                    Matcher m = p.matcher(msg);
//                    int[] arr = new int[3];
//                    //arr[0] = arr[1] = arr[2] = 0;
//                    int count = 0;
//                    while (m.find()) {
//                        if (!"".equals(m.group())){
//                            System.out.println("come here:"+ m.group());
//                            arr[count++] = Integer.parseInt(m.group());
//                        }
//                    }
//                    NpcEntity.pos = new BlockPos(arr[0],arr[1],arr[2]);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//            t.start();
            threadManager.execute(new delivery_chat(count++,event,settingContent,chatRecord));
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
//

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

