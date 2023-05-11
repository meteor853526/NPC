package com.npc.test.chat;

import com.npc.test.PlayerChatEvent;
import com.npc.test.RequestHandler;
import com.npc.test.TurboRequestHandler;
import com.npc.test.passive.DeliveryEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.ServerChatEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GPdelivery_chat implements Runnable {
    private int taskId;

    private String setting;
    private String chatRecord;
    public static String followmsg = null;
    public ServerChatEvent event;
    public GPdelivery_chat(int taskId, ServerChatEvent event, String setting, String chatRecord) {
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

                    String format = "{\"role\": \"user\", \"content\": \"%s\"}" ;

                    String cruuent = chatRecord + String.format(format,event.getMessage().replace("~",""));

                    if (!Objects.equals(setting, "")) {

                        String response = TurboRequestHandler.getAIResponse(cruuent);
                        try {
                            FileWriter fileWriter = new FileWriter("C:\\Users\\Dingo\\Documents\\GitHub\\NPC\\src\\main\\java\\com\\npc\\test\\chat\\GroupChatRecord.txt");         // writing back to the file
                            fileWriter.write(cruuent+",\n");
                            String RespondFormat =  "{\"role\": \"assistant\", \"content\": \"%s\"},\n" ;
                            RespondFormat = String.format(RespondFormat,response.replace("\n",""));
                            //System.out.println(RespondFormat);
                            fileWriter.write(RespondFormat);

                            fileWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        PlayerChatEvent.ChatReply = response;
                        event.getPlayer().sendMessage(new StringTextComponent("~"+response), event.getPlayer().getUUID());

                        if(!Objects.equals(PlayerChatEvent.ChatMsg, "")){
                            String regex ="[-]?\\d*";
                            Pattern p = Pattern.compile(regex);
                            Matcher m = p.matcher(PlayerChatEvent.ChatMsg);
                            int[] arr = new int[3];
                            System.out.println();
                            //arr[0] = arr[1] = arr[2] = 0;
                            int count = 0;
                            while (m.find()) {
                                if (!"".equals(m.group())){
                                    System.out.println("come here:"+ m.group());
                                    arr[count++] = Integer.parseInt(m.group());
                                }
                            }
                            DeliveryEntity.pos = new BlockPos(arr[0],arr[1],arr[2]);
                            DeliveryEntity.taskID = 1;
                            System.out.println(DeliveryEntity.taskID);
                        }

                }
                Thread.sleep(1000);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }


    }
}
