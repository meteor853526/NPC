package com.npc.test.chat;

import com.npc.test.TurboRequestHandler;
import com.npc.test.passive.NpcEntity;
import net.minecraftforge.event.ServerChatEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.IOException;

public class ConnectGUI implements Runnable {

    private String response;
    private String chat_Record;

    public ServerChatEvent event;
    public ConnectGUI(String response, ServerChatEvent event, String chatRecord) {
        this.response = response;
        this.event = event;
        this.chat_Record = chatRecord;

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
                  //  "{\"role\": \"system\", \"content\": \"When someone want to buy wheat or carrot or beetroot from you, please parse into following format with delivery list:Item:   Amount:   Price: (DONT add gold coins after price).\"}," +
                    "{\"role\": \"user\", \"content\": \"%s\"}\n"
                    ;

            String current = chat_Record + String.format(format,response.replace("~",""));
            //String response = TurboRequestHandler.getAIResponse(current);
            //String pattern = "\\s*1\\.\\s*Item:[\\s\\S]*?2\\.\\s*Amount:\\s*(.*?)\\s*3\\.\\s*Price:\\s*([\\d.]+)";
            String pattern = "Item:\\s*(.*?),\\s*Amount:\\s*(\\d+),\\s*Price:\\s*(\\d+)\\s*.*";


            System.out.println("This is " + current);

            Pattern regex = Pattern.compile(pattern);
            Matcher matcher = regex.matcher(current);

            String item = "";
            int amount = 0;
            float price = 0.0f;

            /*while (matcher.find()) {
                if (item.isEmpty()) {
                    item = matcher.group();
                } else if (amount == 0) {
                    amount = Integer.parseInt(matcher.group());
                } else if (price == 0.0f) {
                    price = Float.parseFloat(matcher.group());
                }
            }*/

            while (matcher.find()) {
                item = matcher.group(1).trim();
                amount = Integer.parseInt(matcher.group(2).trim());
                price = Integer.parseInt(matcher.group(3).trim());
            }

            /*if (matcher.find()) {
                String item = matcher.group(1).trim();
                int amount = Integer.parseInt(matcher.group(2).trim());
                int price = Integer.parseInt(matcher.group(3).trim());

                System.out.println(item);
                System.out.println(amount);
                System.out.println(price);
            }*/

            /*if (matcher.find()) {
                item = matcher.group(1);
                amount = Integer.parseInt(matcher.group(2));
                String priceString = matcher.group(3).replaceAll("[^\\d.]", "");
                price = Float.parseFloat(priceString);
            }*/

            System.out.println(item);
            System.out.println(amount);
            System.out.println(price);

            //if(response.contains("")){

             //   NpcEntity.taskID = 2;
            //}
            //System.out.println("check" + response);


            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
