package com.login.service;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.StringTokenizer;

@Service
@Slf4j
public class ChatGPTService {
    public String sendRequest(String request,String secretKey) {
        log.info("Sending Request to openai.com with request: "+request);
        try {
            StringTokenizer tokenCount = new StringTokenizer(request);
            log.info("Token Count For Requesting openai.com: "+tokenCount.countTokens());

            URL url = new URL("https://api.openai.com/v1/completions");
            HttpURLConnection httpCon = (HttpURLConnection)url.openConnection();
            httpCon.setRequestMethod("POST");
            httpCon.setRequestProperty("Content-Type","application/json");
            httpCon.setRequestProperty("Authorization","Bearer "+secretKey);
            httpCon.setDoOutput(true);

            JsonObject json = new JsonObject();
            json.addProperty("model","text-curie-001");
            json.addProperty("prompt",request);
            json.addProperty("temperature",0.5);
            json.addProperty("max_tokens",2000);
            json.addProperty("top_p",1);
            json.addProperty("frequency_penalty",0);
            json.addProperty("presence_penalty",0);
            log.info("Json is: "+json);

            OutputStreamWriter writer = new OutputStreamWriter(httpCon.getOutputStream());
            writer.write(json.toString());
            writer.flush();
            writer.close();
            httpCon.getOutputStream().flush();
            httpCon.getOutputStream().close();

            InputStream responseStream = httpCon.getResponseCode() / 100 == 2 ? httpCon.getInputStream() : httpCon.getErrorStream();
            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
            String response = s.hasNext() ? s.next() : "";

            log.info("Response is: "+response);
            return response;
        } catch (Exception e){
            log.info("Exception Occurred While Sending Request to openai.com for request: "+request);
            return "Exception";
        }
    }
}
