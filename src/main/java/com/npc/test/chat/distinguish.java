package com.npc.test.chat;

import com.npc.test.PlayerChatEvent;
import com.npc.test.TurboRequestHandler;
import com.npc.test.passive.DeliveryEntity;
import com.npc.test.passive.NpcEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.ServerChatEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class distinguish implements Runnable {

    private String response;

    private BlockPos pos;

    public static String followmsg = null;
    public ServerChatEvent event;
//    private static final String REQUEST_BODY_GPT_TURBO = "{\n" +
//            "     \"model\":\"gpt-3.5-turbo\",\n" +
//            "     \"messages\": [{\"role\": \"system\", \"content\": \"Determine whether the following sentence needs a delivery person. If it need ,reply yes,otherwise no\"}," +
//            "                    {\"role\": \"user\", \"content\": \"%s\"}],\n" +
//            "     \"temperature\":0.7\n" +
//            "}";
    public distinguish(String response, ServerChatEvent event,BlockPos pos) {
        this.response = response;
        this.event = event;
        this.pos = pos;
    }
    public ServerChatEvent getEvent() {
        return event;
    }

    public void setEvent(ServerChatEvent event) {
        this.event = event;
    }

    public void run() {
        System.out.println("Task " + " is running on thread " + Thread.currentThread().getName());

            try {

                    String format  =
                            "{\"role\": \"system\", \"content\": \"Determine whether the following sentence needs a delivery person. If it need ,reply yes,otherwise no\"}," +
                            "{\"role\": \"user\", \"content\": \"%s\"}\n"
                            ;
                    //System.out.println(format);

                    String cruuent = String.format(format,response.replace("~",""));
                    //System.out.println(cruuent);
                    String response = TurboRequestHandler.getAIResponse(cruuent);
                    if(response.contains("yes") || response.contains("Yes") || response.contains("YES")){
                        DeliveryEntity.pos = pos;

                        DeliveryEntity.taskID = 3; // (7,4,12)
                        NpcEntity.taskID = 2;
                        //System.out.println("???????????????");
                    }
                    System.out.println("check" + response);


                    //System.out.println("check" + DeliveryEntity.taskID);



                Thread.sleep(1000);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }


    }
}
