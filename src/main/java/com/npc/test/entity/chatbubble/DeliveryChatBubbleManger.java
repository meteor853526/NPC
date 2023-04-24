package com.npc.test.entity.chatbubble;


import com.google.common.collect.Lists;
import com.npc.test.NpcTestMod;
import com.npc.test.client.resource.pojo.ChatBubbleInfo;
import com.npc.test.client.resource.pojo.MaidModelInfo;
import com.npc.test.entity.info.ServerCustomPackLoader;
import com.npc.test.passive.DeliveryEntity;
import com.npc.test.util.GetJarResources;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

import static com.npc.test.entity.chatbubble.MaidChatBubbles.EMPTY;
import static com.npc.test.entity.info.ServerCustomPackLoader.GSON;


@Mod.EventBusSubscriber
public class DeliveryChatBubbleManger {

    public static ChatBubbleInfo DEFAULT_CHAT_BUBBLE = null;
    private static final String DEFAULT_CHAT_BUBBLE_PATH = String.format("/assets/%s/tlm_custom_pack/default_chat_bubble.json", NpcTestMod.MOD_ID);
    private static final Random RANDOM = new Random();

    private static final String IDLE_CHAT_TEXT_ID = "idle";
    private static final String SLEEP_CHAT_TEXT_ID = "sleep";
    private static final String WORK_CHAT_TEXT_ID = "work";
    private static final String MORNING_CHAT_TEXT_ID = "morning";
    private static final String NIGHT_CHAT_TEXT_ID = "night";
    private static final String RAIN_CHAT_TEXT_ID = "rain";
    private static final String SNOW_CHAT_TEXT_ID = "snow";
    private static final String COLD_CHAT_TEXT_ID = "cold";
    private static final String HOT_CHAT_TEXT_ID = "hot";
    private static final String HURT_CHAT_TEXT_ID = "hurt";
    private static final String BEG_CHAT_TEXT_ID = "beg";

    private static final long MORNING_START = 0;
    private static final long MORNING_END = 3000;
    private static final long EVENING_START = 12000;
    private static final long EVENING_END = 15000;
    private static final float LOW_TEMPERATURE = 0.15F;
    private static final float HIGH_TEMPERATURE = 0.95F;

    public static String bubble_msg = "";

    /**
     * 显示时长，15 秒
     */
    private static final long DURATION = 3 * 1000;
    /**
     * 检测间隔，60 秒
     */
    private static final int CHECK_RATE = 60 * 20;

    public static void addChatBubble(long endTime, ChatText text, DeliveryEntity maid) {
        if (System.currentTimeMillis() > endTime) {
            return;
        }
        if (text == ChatText.EMPTY_CHAT_TEXT) {
            return;
        }

        Pair<Long, ChatText> bubbleItem = Pair.of(endTime, text);
        MaidChatBubbles chatBubble = maid.getChatBubble();
        long minEndTime = -1;
        int index = 1;
        Pair<Long, ChatText> bubble1 = chatBubble.getBubble1();
        Pair<Long, ChatText> bubble2 = chatBubble.getBubble2();
        Pair<Long, ChatText> bubble3 = chatBubble.getBubble3();

        if (bubble1.getLeft() <= minEndTime) {
            minEndTime = bubble1.getLeft();
        }
        if (bubble2.getLeft() <= minEndTime) {
            minEndTime = bubble2.getLeft();
            index = 2;
        }
        if (bubble3.getLeft() <= minEndTime) {
            index = 3;
        }

        MaidChatBubbles newChatBubble;
//        switch (index) {
//            default:
//            case 1:
//                newChatBubble = new MaidChatBubbles(bubbleItem, bubble2, bubble3);
//                break;
//            case 2:
//                newChatBubble = new MaidChatBubbles(bubble1, bubbleItem, bubble3);
//                break;
//            case 3:
//                newChatBubble = new MaidChatBubbles(bubble1, bubble2, bubbleItem);
//                break;
//        }
        newChatBubble = new MaidChatBubbles(bubbleItem, bubble2, bubble3);
        maid.setChatBubble(newChatBubble);
    }

    public static int getChatBubbleCount(DeliveryEntity maid) {
        int count = 0;
        MaidChatBubbles chatBubble = maid.getChatBubble();
        Pair<Long, ChatText> bubble1 = chatBubble.getBubble1();
        Pair<Long, ChatText> bubble2 = chatBubble.getBubble2();
        Pair<Long, ChatText> bubble3 = chatBubble.getBubble3();
        if (bubble1 != EMPTY) {
            count = count + 1;
        }
        if (bubble2 != EMPTY) {
            count = count + 1;
        }
        if (bubble3 != EMPTY) {
            count = count + 1;
        }
        return count;
    }

    public static void tick(DeliveryEntity npc) {
        if(!Objects.equals(DeliveryEntity.replay, "")){
            long offset1 = npc.getUUID().getLeastSignificantBits() % CHECK_RATE;
            checkTimeoutChatBubble(npc);
//            if ((npc.tickCount + offset1) % CHECK_RATE == 0) {
//                addMainChatText(npc,DeliveryEntity.replay);
//            }
            addMainChatText(npc,DeliveryEntity.replay);
        }








    }

    public static void initDefaultChat() {
        InputStream stream = GetJarResources.readTouhouLittleMaidFile(DEFAULT_CHAT_BUBBLE_PATH);
        try {
            if (stream != null) {
                DEFAULT_CHAT_BUBBLE = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), ChatBubbleInfo.class);
            }
        } finally {
            IOUtils.closeQuietly(stream);
        }

    }

    private static void checkTimeoutChatBubble(DeliveryEntity maid) {
        MaidChatBubbles chatBubble = maid.getChatBubble();
        Pair<Long, ChatText> bubble1 = chatBubble.getBubble1();
        Pair<Long, ChatText> bubble2 = chatBubble.getBubble2();
        Pair<Long, ChatText> bubble3 = chatBubble.getBubble3();
        long currentTimeMillis = System.currentTimeMillis();
        if (bubble1 != EMPTY && currentTimeMillis > bubble1.getLeft()) {
            bubble1 = EMPTY;
            DeliveryEntity.replay = "";
        }
        if (bubble2 != EMPTY && currentTimeMillis > bubble2.getLeft()) {
            bubble2 = EMPTY;
            DeliveryEntity.replay = "";
        }
        if (bubble3 != EMPTY && currentTimeMillis > bubble3.getLeft()) {
            bubble3 = EMPTY;
            DeliveryEntity.replay = "";
        }
        maid.setChatBubble(new MaidChatBubbles(bubble1, bubble2, bubble3));
    }

//    @SubscribeEvent(priority = EventPriority.LOWEST)
//    public static void addHurtChatText(MaidDamageEvent event) {
//        addSpecialChatText(event.getMaid(), HURT_CHAT_TEXT_ID);
//    }

    private static void addMainChatText(DeliveryEntity maid) {
//        MaidSchedule schedule = maid.getSchedule();
//        int time = (int) (maid.level.getDayTime() % 24000L);
//        Activity activity;
//        switch (schedule) {
//            default:
//            case DAY:
//                activity = InitEntities.MAID_DAY_SHIFT_SCHEDULES.get().getActivityAt(time);
//                break;
//            case NIGHT:
//                activity = InitEntities.MAID_NIGHT_SHIFT_SCHEDULES.get().getActivityAt(time);
//                break;
//            case ALL:
//                activity = Activity.WORK;
//                break;
//        }
//      if (activity == Activity.IDLE) {
            //addMainChatText(maid, IDLE_CHAT_TEXT_ID);
//            return;
//        }
//        if (activity == Activity.REST) {
//            addMainChatText(maid, SLEEP_CHAT_TEXT_ID);
//            return;
//        }
//        if (activity == Activity.WORK) {
//            addWorkChatText(maid, maid.getTask().getUid().getPath());
//        }
    }

    private static void addMainChatText(DeliveryEntity maid, String chatText) {
        //List<ChatText> chatTexts = getChatTexts(maid, text -> text.getMain().get(chatTextId));

        ChatText randomChat = new ChatText(ChatTextType.TEXT, DEFAULT_CHAT_BUBBLE.getBg(), chatText);
        maid.addChatBubble(getEndTime(), randomChat);
    }

//    private static void addWorkChatText(DeliveryEntity maid, String taskId) {
//        List<ChatText> workChatTexts = getChatTexts(maid, text -> text.getMain().get(WORK_CHAT_TEXT_ID));
//        List<ChatText> taskChatTexts = getChatTexts(maid, text -> text.getMain().get(taskId));
//        workChatTexts.addAll(taskChatTexts);
//        ChatText randomChat = getRandomChatText(workChatTexts);
//        maid.addChatBubble(getEndTime(), randomChat);
//    }
//
//    private static void addSpecialChatText(EntityMaid maid) {
//        World world = maid.level;
//        BlockPos pos = maid.blockPosition();
//        long dayTime = world.getDayTime();
//        Biome biome = world.getBiome(pos);
//        if (maid.isBegging()) {
//            addSpecialChatText(maid, BEG_CHAT_TEXT_ID);
//            return;
//        }
//        // 差不多早上 6:00 - 9:00
//        if (MORNING_START < dayTime && dayTime < MORNING_END) {
//            addSpecialChatText(maid, MORNING_CHAT_TEXT_ID);
//            return;
//        }
//        // 差不多下午 6:00 - 9:00
//        if (EVENING_START < dayTime && dayTime < EVENING_END) {
//            addSpecialChatText(maid, NIGHT_CHAT_TEXT_ID);
//            return;
//        }
//        if (world.isRaining() && isRainBiome(biome, pos)) {
//            addSpecialChatText(maid, RAIN_CHAT_TEXT_ID);
//            return;
//        }
//        if (world.isRaining() && isSnowyBiome(biome, pos)) {
//            addSpecialChatText(maid, SNOW_CHAT_TEXT_ID);
//            return;
//        }
//        if (biome.getTemperature(pos) < LOW_TEMPERATURE) {
//            addSpecialChatText(maid, COLD_CHAT_TEXT_ID);
//            return;
//        }
//        if (biome.getTemperature(pos) > HIGH_TEMPERATURE) {
//            addSpecialChatText(maid, HOT_CHAT_TEXT_ID);
//        }
//    }
//
//    private static void addSpecialChatText(EntityMaid maid, String chatTextId) {
//        List<ChatText> chatTexts = getChatTexts(maid, text -> text.getSpecial().get(chatTextId));
//        ChatText randomChat = getRandomChatText(chatTexts);
//        maid.addChatBubble(getEndTime(), randomChat);
//    }
//
//    private static void addOtherChatText(EntityMaid maid) {
//        maid.getMaidBauble().fireEvent((b, s) -> {
//            String chatBubbleId = b.getChatBubbleId();
//            if (!chatBubbleId.isEmpty()) {
//                List<ChatText> chatTexts = getChatTexts(maid, text -> text.getOther().get(chatBubbleId));
//                maid.addChatBubble(getEndTime(), getRandomChatText(chatTexts));
//                return true;
//            }
//            return false;
//        });
//    }

    private static ChatText getRandomChatText(List<ChatText> chatTexts) {
        int length = chatTexts.size();
        if (length <= 0) {
            return ChatText.EMPTY_CHAT_TEXT;
        }
        int randomIndex = RANDOM.nextInt(length);
        return chatTexts.stream().skip(randomIndex)
                .findFirst().orElse(ChatText.EMPTY_CHAT_TEXT);
    }

    private static List<ChatText> getChatTexts(DeliveryEntity maid, Function<ChatBubbleInfo.Text, List<ChatText>> function) {
        Optional<MaidModelInfo> info = ServerCustomPackLoader.SERVER_MAID_MODELS.getInfo(maid.getModelId());
        if (info.isPresent()) {
            ChatBubbleInfo chatBubble = info.get().getChatBubble();
            List<ChatText> result = function.apply(chatBubble.getText());
            if (result != null) {
                return result;
            }
        }
        return Lists.newArrayList();
    }

    private static long getEndTime() {
        return System.currentTimeMillis() + DURATION;
    }
}
