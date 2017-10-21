package com.chichkanov.service;

import com.chichkanov.models.Product;
import com.fasterxml.jackson.core.JsonParser;
import com.google.gson.Gson;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.IOException;

public class OscarService {

    private static final String LOGTAG = "OSCARSERVICE";

    private static final String BASE_URL = "http://ec2-user@ec2-54-190-58-218.us-west-2.compute.amazonaws.com/api/";
    private static volatile OscarService instance;

    private OscarService() {
    }

    public static OscarService getInstance() {
        OscarService currentInstance;
        if (instance == null) {
            synchronized (OscarService.class) {
                if (instance == null) {
                    instance = new OscarService();
                }
                currentInstance = instance;
            }
        } else {
            currentInstance = instance;
        }
        return currentInstance;
    }

    public String fetchDiscount(long chatId) {
        String requestUrl = BASE_URL + "?" + "id=" + chatId;
        System.out.println(requestUrl);
        CloseableHttpClient client = HttpClientBuilder.create()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();

        String recomendation = null;
        try {
            HttpGet request = new HttpGet(requestUrl);
            CloseableHttpResponse response = client.execute(request);
            HttpEntity ht = response.getEntity();
            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            recomendation = EntityUtils.toString(buf, "UTF-8");

            Gson gson = new Gson();
            Product product = gson.fromJson(recomendation, Product.class);
            recomendation = product.getProductName() + "\nЦена: " + product.getPrice() + " руб.";

            BotLogger.info(LOGTAG, recomendation);
        } catch (IOException e) {
            BotLogger.error(LOGTAG, e);
        }



        return recomendation;
    }

    public void buyPack(long chatId, String packJson) {
        HttpClient client = HttpClientBuilder.create().build(); //Use this instead

        System.out.println("Pack bought");

        HttpPost request = new HttpPost(BASE_URL + "transaction/" + "?" + "id=" + chatId);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        try {
            StringEntity params =new StringEntity(packJson, "UTF-8");
            request.setEntity(params);
            client.execute(request);
            BotLogger.info(LOGTAG, "Pack BOUGHT");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
