package com.npc;

import com.npc.test.PlayerChatEvent;
import com.npc.test.RequestHandler;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.ServerChatEvent;

import java.io.IOException;
import java.util.Objects;

public class MyTask implements Runnable {
    private int taskId;
    private String message;

    public static String followmsg = null;
    public ServerChatEvent event;
    public MyTask(int taskId, String message,ServerChatEvent event) {
        this.taskId = taskId;
        this.message = message;
        this.event = event;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
    }
}
