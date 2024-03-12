package com.wdj.chatbot.api.domain.ai.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wdj.chatbot.api.domain.ai.IOpenAI;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class OpenAI implements IOpenAI
{

    private Logger logger = LoggerFactory.getLogger(OpenAI.class);
    @Value("${chatbot-api.openAikey}")
    private String openAiKey;

    @Override
    public String doChatGpt(String question) throws IOException {

        String pro = "127.0.0.1";//本机地址
        int pro1 = 7890; //代理端口号
        //创建一个 HttpHost 实例，这样就设置了代理服务器的主机和端口。
        HttpHost httpHost = new HttpHost(pro, pro1);
        //创建一个 RequestConfig 对象，然后使用 setProxy() 方法将代理 httpHost 设置进去。
        RequestConfig build = RequestConfig.custom().setProxy(httpHost).build();

        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://api.openai.com/v1/chat/completions");

        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer " + openAiKey);

        post.setConfig(build);

        String paramJson = "{\n" +
                "     \"model\": \"gpt-3.5-turbo\",\n" +
                "     \"messages\": [{\"role\": \"user\", \"content\": \""+ question + "\"}],\n" +
                "     \"temperature\": 0.7\n" +
                "   }";

        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);

        CloseableHttpResponse response = httpclient.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String jsonStr = EntityUtils.toString(response.getEntity());
            // 使用Fastjson解析JSON字符串
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            // 提取content字段的内容
            String content = jsonObject.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
            // 打印content的内容
            return content;
        } else {
            throw new RuntimeException("api.openai.com Err Code is " + response.getStatusLine().getStatusCode());
        }

    }
}
