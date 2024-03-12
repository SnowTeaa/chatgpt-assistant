package com.wdj.chatbot.api.test;


import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ApiTest {


    private static final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void query_unanswered_questions() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpGet get = new HttpGet("https://api.zsxq.com/v2/groups/28885518425541/topics?scope=all&count=20");


        get.addHeader("cookie", "");
        get.addHeader("Content-type","application/json; charset=UTF-8");

        CloseableHttpResponse response = httpClient.execute(get);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){

//            String res = EntityUtils.toString(response.getEntity());
//            System.out.println(res);
            String jsonStr = EntityUtils.toString(response.getEntity());
//            logger.info(jsonStr);
            logger.info("拉取提问数据,jsonStr：{}",jsonStr);

        } else {
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }

}

