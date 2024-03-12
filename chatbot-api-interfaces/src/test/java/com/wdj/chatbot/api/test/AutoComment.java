package com.wdj.chatbot.api.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;



public class AutoComment {

    private static final String ACCESS_TOKEN = "";
    private static final String SESSION_ID = "";

    public static void main(String[] args) {
        try {
            AutoComment autoComment = new AutoComment();
            autoComment.commentOnTopic("4844855851424448", "士大夫士大夫但是");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void commentOnTopic(String topicId, String commentText) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 构建评论的请求体
        String requestBody = "{\n" +
                "  \"req_data\": {\n" +
                "    \"text\": \"" + commentText + "\",\n" +
                "    \"image_ids\": [],\n" +
                "    \"mentioned_user_ids\": []\n" +
                "  }\n" +
                "}";

        // 构建 POST 请求
        HttpPost postRequest = new HttpPost("https://api.zsxq.com/v2/topics/" + topicId + "/comments");
        postRequest.addHeader("cookie", "zsxq_access_token=" + ACCESS_TOKEN + "; zsxqsessionid=" + SESSION_ID);
        postRequest.addHeader("content-type", "application/json; charset=UTF-8");

        postRequest.addHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");

        postRequest.setEntity(new StringEntity(requestBody, ContentType.create("application/json", Consts.UTF_8)));

        // 发送 POST 请求
        HttpResponse response = httpClient.execute(postRequest);

        // 处理响应
        if (response.getStatusLine().getStatusCode() == 200) {
            System.out.println("评论成功！");
            // 可以根据需要处理成功后的逻辑

            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);

        } else {
            System.out.println("评论失败，HTTP状态码: " + response.getStatusLine().getStatusCode());
            // 可以根据需要处理失败后的逻辑
        }

    }


    private Logger logger = LoggerFactory.getLogger(SpringBootRunTest.class);


    @Test
    public void test_chatGpt() throws IOException {


        String pro = "127.0.0.1";//本机地址
        int pro1 = 7890; //代理端口号
        //创建一个 HttpHost 实例，这样就设置了代理服务器的主机和端口。
        HttpHost httpHost = new HttpHost(pro, pro1);
        //创建一个 RequestConfig 对象，然后使用 setProxy() 方法将代理 httpHost 设置进去。
        RequestConfig build = RequestConfig.custom().setProxy(httpHost).build();

        String apikey = "";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://api.openai.com/v1/chat/completions");
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer " + apikey);

        post.setConfig(build);

        String paramJson = "{\n" +
                "     \"model\": \"gpt-3.5-turbo\",\n" +
                "     \"messages\": [{\"role\": \"user\", \"content\": \"帮我写一个java冒泡排序\"}],\n" +
                "     \"temperature\": 0.7\n" +
                "   }";

        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);


        CloseableHttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String jsonStr = EntityUtils.toString(response.getEntity());
            // 使用Fastjson解析JSON字符串
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            // 提取content字段的内容
            String content = jsonObject.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
            // 打印content的内容
            System.out.println("提取的content内容：" + content);

        } else {
            throw new RuntimeException("api.openai.com Err Code is " + response.getStatusLine().getStatusCode());
        }


    }

}
