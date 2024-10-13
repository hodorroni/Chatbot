package com.handson.chatbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class JokesService {
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    @Autowired
    ObjectMapper om;

    public String searchJokes(String keyword) throws IOException {
        return getJokesByQuery(keyword);
    }

    private String getJokesByQuery(String keyword) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.chucknorris.io/jokes/search?query="+keyword)
                .method("GET", null)
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .addHeader("accept-language", "en-US,en;q=0.9,he-IL;q=0.8,he;q=0.7")
                .addHeader("cache-control", "max-age=0")
                .addHeader("priority", "u=0, i")
                .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"129\", \"Not=A?Brand\";v=\"8\", \"Chromium\";v=\"129\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("sec-fetch-dest", "document")
                .addHeader("sec-fetch-mode", "navigate")
                .addHeader("sec-fetch-site", "none")
                .addHeader("sec-fetch-user", "?1")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36")
                .build();
        Response response = client.newCall(request).execute();
        String res = response.body().string();
        JokesResponse jokes = om.readValue(res,JokesResponse.class);
        StringBuilder responseReturned = new StringBuilder();
        try{
            if(jokes.getResult()!=null && !jokes.getResult().isEmpty()) {
                for(int i=0;i<jokes.getResult().size();i++){
                    JokesResponseObject joke = jokes.getResult().get(i);
                    responseReturned.append(i+1).append(".").append("\n").append("Joke: ").append(joke.getValue()).append("\n").append(" image: ").append(joke.getUrl()).append("\n");
                }
            }
            return responseReturned.toString();
        }
        catch (Exception e){
            e.printStackTrace();
            return "Not Found";
        }
    }

    static class JokesResponse {
        List<JokesResponseObject> result;

        public List<JokesResponseObject> getResult() {
            return result;
        }
    }

    static class JokesResponseObject {
        List<String> categories;
        String url;
        String value;
        public List<String> getCategories() {
            return categories;
        }

        public String getUrl() {
            return url;
        }

        public String getValue() {
            return value;
        }
    }

}
