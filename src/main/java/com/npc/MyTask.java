package com.npc;

import com.npc.test.PlayerChatEvent;
import com.npc.test.RequestHandler;
import com.npc.test.passive.NpcEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.ServerChatEvent;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class MyTask implements Runnable {
    private int taskId;
    private String setting;
    private  String chatRecord;


    public static String followmsg = null;
    public ServerChatEvent event;
    public MyTask(int taskId, ServerChatEvent event,String setting,String chatRecord) {
        this.taskId = taskId;
        this.setting = setting;
        this.event = event;
        this.chatRecord = chatRecord;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }


    public ServerChatEvent getEvent() {
        return event;
    }

    public void setEvent(ServerChatEvent event) {
        this.event = event;
    }

    public void run() {
        System.out.println("Task " + taskId + " is running on thread " + Thread.currentThread().getName());

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        while (true) {
//            try {
//                if(!Objects.equals(message, "")) {
//                    String response = RequestHandler.getAIResponse(message);
//                    PlayerChatEvent.msg = response;
//                    System.out.println(response);
//                    this.event.getPlayer().sendMessage(new StringTextComponent("ChatGPT: " + PlayerChatEvent.msg), event.getPlayer().getUUID());
//
//                    message = "";
//                }else if(!Objects.equals(followmsg, "")){
//                    String response = RequestHandler.getAIResponse(followmsg);
//                    PlayerChatEvent.msg = response;
//                    System.out.println(response);
//                    this.event.getPlayer().sendMessage(new StringTextComponent("ChatGPT: " + response), event.getPlayer().getUUID());
//                    followmsg = "";
//
//                }
//                Thread.sleep(10000);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
        //System.out.println("Task " + taskId + " has completed");

            try {
                while (true) {

                    if (!Objects.equals(setting, "")) {

                        System.out.println("?????????????????????????????????????");
                        String response = RequestHandler.getAIResponse("You are a Non-Player Character(NPC) and your name is diedie and your duty is a farmer and sell some product to player in minecraft !!! . Then is your character setting " + setting + "And this the record we talked before " + chatRecord + "If record show nothing which mean this is the first time we met .There is the current message: " + event.getMessage());
                        System.out.println("You are a Non-Player Character(NPC) and your name is diedie and your duty is a farmer and sell some product to player in minecraft !!! . " +
                                "Then is your character setting " + setting + "And this the record we talked before "
                                + chatRecord + "If record show nothing which mean this is the first time we met ." +
                                "There is the current message: " + event.getMessage());
                        JSONObject chatjsonObject = new JSONObject(PlayerChatEvent.chatRecord);

                        JSONObject temp = new JSONObject();

                        temp.put("Human:", event.getMessage());
                        temp.put("AI", response);


                        chatjsonObject.put("chat flow(" + taskId + ")", temp);

                        try {
//                            FileWriter fileWriter = new FileWriter("C:\\Users\\Dingo\\Documents\\GitHub\\NPC\\src\\main\\java\\com\\npc\\test\\ChatRecord.txt",true);         // writing back to the file
                            FileWriter fileWriter = new FileWriter("C:\\Users\\lili\\Desktop\\NPC\\src\\main\\java\\com\\npc\\test\\ChatRecord.txt",true);
                            fileWriter.write("Human:"+ event.getMessage()+"\\n");
                            fileWriter.write("AI:"+ response+"\\n");
                            fileWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        NpcEntity.msg = "";
                        System.out.println(response);
                        NpcEntity.replay = response;
                        event.getPlayer().sendMessage(new StringTextComponent( response), event.getPlayer().getUUID());

                        break;
                    }
                }
                Thread.sleep(1000);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }


    }
}
