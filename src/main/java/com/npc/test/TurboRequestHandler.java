package com.npc.test;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class TurboRequestHandler {
    private static final String URL_GPT_TURBO = "https://api.openai.com/v1/chat/completions";
    private static final String REQUEST_BODY_GPT_TURBO = "{\n" +
            "     \"model\":\"gpt-3.5-turbo\",\n" +

            "     \"messages\": [%s],\n" +

            "     \"temperature\":0.7\n" +
            "}";

    private static class TurboRequest {
        static class Choice {
            message message;
            String finish_reason;
            int index;
        }
        static class message {
            String role;
            String content;
        }
        static class usage {
            String prompt_tokens;
            String completion_tokens;
            String total_tokens;
        }

        usage usage;
        Choice[] choices;
    }



    public static String getAIResponse(String prompt) throws IOException {
        if (prompt.length() > 4096) prompt = prompt.substring(prompt.length() - 4096);
        //AIMobsMod.LOGGER.info("Prompt: " + prompt);

        String postBody = String.format(REQUEST_BODY_GPT_TURBO,prompt);
        System.out.println(postBody);
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost request = new HttpPost(URL_GPT_TURBO);
            StringEntity params = new StringEntity(postBody, "UTF-8");
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Authorization", "Bearer " + "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");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            System.out.println(responseString);

            return new Gson().fromJson(responseString, TurboRequest.class).choices[0].message.content.replace("\r", " ");
        }
    }

}
