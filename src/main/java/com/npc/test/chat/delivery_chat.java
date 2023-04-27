package com.npc.test.chat;

import com.npc.test.PlayerChatEvent;
import com.npc.test.RequestHandler;
import com.npc.test.passive.NpcEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.ServerChatEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class delivery_chat implements Runnable {
    private int taskId;

    private String setting;
    private String chatRecord;
    public static String followmsg = null;
    public ServerChatEvent event;
    public delivery_chat(int taskId, ServerChatEvent event, String setting, String chatRecord) {
        this.taskId = taskId;

        this.event = event;
        this.setting = setting;
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

            try {
                while (true) {

                    if (!Objects.equals(setting, "")) {
                        String response = RequestHandler.getAIResponse("Your reply need to below 50 words !!!!. You are a Non-Player Character(NPC) and your name is diedie and your duty is a farmer and sell some product to player in minecraft !!! .  Then is your character setting "
                                + setting + "And this the record we talked before " + chatRecord
                                + "If record show something which mean dont say hi again and DONT introduce yourself again and DONT say the setting! If record show nothing which mean this is the first time we met .There is the current message: "
                                + event.getMessage().replace("#"," "));



                        System.out.println("You are a Non-Player Character(NPC) and your name is diedie and your duty is a farmer and sell some product to player in minecraft !!! . " +
                                "Then is your character setting " + setting + "And this the record we talked before "
                                + chatRecord + "If record show nothing which mean this is the first time we met ." +
                                "There is the current message: " + event.getMessage());

                        try {
                            FileWriter fileWriter = new FileWriter("C:\\Users\\User\\IdeaProjects\\NPC\\src\\main\\java\\com\\npc\\test\\chat\\ChatRecord.txt",true);         // writing back to the file
                            fileWriter.write("Human:"+ event.getMessage().replace("Hi there! I'm Diedie, the farmer chief. It looks like we haven't talked before. For 1 carrot it costs 3 gold coins, 1 wheat costs 2 gold coins and 1 beetroot is 5 gold coins.", " ") +"\\n");
                            fileWriter.write( response +"\\n");

                            fileWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println(response);
                        PlayerChatEvent.ChatReply = response;
                        event.getPlayer().sendMessage(new StringTextComponent("@:"+response), event.getPlayer().getUUID());
                        break;
                    }
                }
                Thread.sleep(1000);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }


    }
}
