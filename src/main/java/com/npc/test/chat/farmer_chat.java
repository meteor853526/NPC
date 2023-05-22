package com.npc.test.chat;

import com.npc.test.PlayerChatEvent;
import com.npc.test.RequestHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class farmer_chat implements Runnable {
    private int taskId;
    private String setting;
    private String chatRecord;
    public static Minecraft mc;
    public static World world;
    public static Long tick;
    public static String currentTime;
    public ServerChatEvent event;
    public farmer_chat(int taskId, ServerChatEvent event, String setting, String chatRecord) {
        this.taskId = taskId;

        this.event = event;
        this.setting = setting;
        this.chatRecord = chatRecord;
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
//                    PlayerEntity player = Minecraft.getInstance().player;
                    tick = Minecraft.getInstance().level.getDayTime();
                    System.out.println("TICK:"+tick);
                    if (!Objects.equals(setting, "")) {
                        if(tick >= 0 && tick <1000){
                            currentTime = "06:00";
                        }
                        else if(tick >= 1000 && tick <2000){
                            currentTime = "07:00";
                        }
                        else if(tick >= 2000&& tick <3000){
                            currentTime = "08:00";
                        }
                        else if(tick >= 3000&& tick <4000){
                            currentTime = "09:00";
                        }
                        else if(tick >= 4000&& tick <5000){
                            currentTime = "10:00";
                        }
                        else if(tick >= 5000&& tick <6000){
                            currentTime = "11:00";
                        }
                        else if(tick >= 6000&& tick <7000){
                            currentTime = "12:00";
                        }
                        else if(tick >= 7000&& tick <8000){
                            currentTime = "13:00";
                        }
                        else if(tick >= 8000&& tick <9000){
                            currentTime = "14:00";
                        }
                        else if(tick >= 9000&& tick <10000){
                            currentTime = "15:00";
                        }
                        else if(tick >= 10000 && tick <11000){
                            currentTime = "16:00";
                        }
                        else if(tick >= 11000 && tick <12000){
                            currentTime = "17:00";
                        }
                        else if(tick >= 12000&& tick <13000){
                            currentTime = "18:00";
                        }
                        else if(tick >= 13000&& tick <14000){
                            currentTime = "19:00";
                        }
                        else if(tick >= 14000&& tick <15000){
                            currentTime = "20:00";
                        }
                        else if(tick >= 15000&& tick <16000){
                            currentTime = "21:00";
                        }
                        else if(tick >= 17000&& tick <18000){
                            currentTime = "22:00";
                        }
                        else if(tick >= 18000&& tick <19000){
                            currentTime = "23:00";
                        }
                        else if(tick >= 18000&& tick <19000){
                            currentTime = "00:00";
                        }
                        else if(tick >= 19000&& tick <20000){
                            currentTime = "01:00";
                        }
                        else if(tick >= 20000&& tick <21000){
                            currentTime = "02:00";
                        }
                        else if(tick >= 21000&& tick <22000){
                            currentTime = "03:00";
                        }
                        else if(tick >= 22000&& tick <23000){
                            currentTime = "04:00";
                        }
                        else if(tick >= 23000&& tick <24000){
                            currentTime = "05:00";
                        }
                        String response = RequestHandler.getAIResponse("Your reply need to below 50 words !!!!. You are a Non-Player Character(NPC) and your name is diedie and your duty is a farmer and sell some product to player in minecraft !!! .  Then is your character setting "
                                + setting +"\n" + chatRecord
                                + "If record show something which mean dont say hi again and DONT introduce yourself again and DONT say the setting! If record show nothing which mean this is the first time we met ."+ "(it is "+currentTime+" now.)"+"There is the current message: "
                                + event.getMessage().replace("#","")
                                + "if player say they want to buy something in the current message, please parse according to the following format: item: \nquantity: \nprice: \n");



                        System.out.println("Your reply need to below 50 words !!!!. You are a Non-Player Character(NPC) and your name is diedie and your duty is a farmer and sell some product to player in minecraft !!! .  Then is your character setting "
                                + setting +"And this the record we talked before " + chatRecord
                                + "If record show something which mean dont say hi again and DONT introduce yourself again and DONT say the setting! If record show nothing which mean this is the first time we met ."+ "(it is "+currentTime+" now.)"+"There is the current message: "
                                + event.getMessage().replace("#"," ").replace("'",""));

                        try {
//                            FileWriter fileWriter = new FileWriter("C:\\Users\\Dingo\\Documents\\GitHub\\NPC\\src\\main\\java\\com\\npc\\test\\chat\\ChatRecord.txt",true);         // writing back to the file
                            FileWriter fileWriter = new FileWriter("C:\\Users\\User\\IdeaProjects\\NPC\\src\\main\\java\\com\\npc\\test\\chat\\ChatRecord.txt",true);
                            fileWriter.write("Human:"+ event.getMessage().replace("Hi there! I'm Diedie, the farmer chief. It looks like we haven't talked before. For 1 carrot it costs 3 gold coins, 1 wheat costs 2 gold coins and 1 beetroot is 5 gold coins.", " ") +"\\n");
//                            fileWriter.write("Human:"+ event.getMessage()+"\\n");
                            fileWriter.write("Diedie:"+ response+"\\n");
//                            fileWriter.write( response +"\\n");


                            fileWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        System.out.println(response);
                        PlayerChatEvent.ChatReply = response;
                        event.getPlayer().sendMessage(new StringTextComponent("#:"+response), event.getPlayer().getUUID());
                        break;
                    }
                }
                Thread.sleep(1000);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }


    }
}
