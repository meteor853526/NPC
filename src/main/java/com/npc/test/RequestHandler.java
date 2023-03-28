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
import java.util.Objects;

public class RequestHandler {


    public boolean enabled = true;
    public String apiKey = "";
    public String model = "text-davinci-003";
    public float temperature = 0.6f;
    private class OpenAIRequest {
        String model = "text-davinci-002";
        String stop = "\"";
        String prompt = "hi";
        float temperature = 0.6f;
        int max_tokens = 64;

        OpenAIRequest(String prompt) {
            this.prompt = prompt;
        }
        OpenAIRequest(String prompt, String model, float temperature) {
            this.prompt = prompt;
            this.model = model;
            this.temperature = temperature;
        }
    }

    private class OpenAIResponse {
        class Choice {
            String text;
        }
        Choice[] choices;
    }

    public String getAIResponse(String prompt) throws IOException {
        if (prompt.length() > 4096) prompt = prompt.substring(prompt.length() - 4096);
        //AIMobsMod.LOGGER.info("Prompt: " + prompt);

        OpenAIRequest openAIRequest = new OpenAIRequest(prompt, "text-davinci-003", 0.6f);
        String data = new Gson().toJson(openAIRequest);

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost request = new HttpPost("https://api.openai.com/v1/completions");
            StringEntity params = new StringEntity(data, "UTF-8");
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Authorization", "Bearer " + "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");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            return new Gson().fromJson(responseString, OpenAIResponse.class).choices[0].text.replace("\n", " ");
        }
    }
}
