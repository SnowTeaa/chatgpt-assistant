package com.wdj.chatbot.api.domain.zsxq.service;

import com.alibaba.fastjson.JSON;
import com.wdj.chatbot.api.domain.zsxq.IZsxqApi;
import com.wdj.chatbot.api.domain.zsxq.model.req.AnswerReq;
import com.wdj.chatbot.api.domain.zsxq.model.req.ReqData;
import com.wdj.chatbot.api.domain.zsxq.model.res.AnswerRes;
import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;



@Service
public class ZsxqApi implements IZsxqApi {

    private static final String ACCESS_TOKEN = "";
    private static final String SESSION_ID = "";

    private static final Logger logger = LoggerFactory.getLogger(ZsxqApi.class);

    @Override
    public String queryUnAnsweredQuestionsTopicId(String groupId, String cookie) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpGet get = new HttpGet("https://api.zsxq.com/v2/groups/" + groupId + "/topics?scope=all&count=20");
        get.addHeader("cookie", cookie);
        get.addHeader("Content-type", "application/json; charset=UTF-8");

        try (CloseableHttpResponse response = httpClient.execute(get)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String jsonStr = EntityUtils.toString(response.getEntity());
                logger.info("拉取提问数据,groupid：{}  jsonStr：{}", groupId,jsonStr);
                return jsonStr;
            } else {
                throw new RuntimeException("queryUnAnsweredQuestionsTopicId Err Code is " + response.getStatusLine().getStatusCode());
            }
        }
    }

    @Override
    public boolean answer(String groupId, String cookie, String topicId, String text, boolean silenced) throws IOException {
        commentOnTopic(topicId, text, groupId);
        return true;
    }



    private boolean commentOnTopic(String topicId, String commentText,String groupid) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            boolean silenced = false;
            AnswerReq answerReq = new AnswerReq(new ReqData(commentText,silenced));

            String paramJson = JSON.toJSONString(answerReq);

            HttpPost postRequest = new HttpPost("https://api.zsxq.com/v2/topics/" + topicId + "/comments");
            postRequest.addHeader("cookie", "zsxq_access_token=" + ACCESS_TOKEN + "; zsxqsessionid=" + SESSION_ID);
            postRequest.addHeader("content-type", "application/json; charset=UTF-8");
            postRequest.setEntity(new StringEntity(paramJson, ContentType.create("application/json", Consts.UTF_8)));

            try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    logger.info("评论成功！");
                    String jsonStr = EntityUtils.toString(response.getEntity());
                    logger.info(jsonStr);
                    logger.info("回答星球问题结果。groupid：{} topicid：{} jsonStr：{}", groupid, topicId,jsonStr);
                    AnswerRes answerRes = JSON.parseObject(jsonStr, AnswerRes.class);
                    return answerRes.isSucceeded();
                } else {
                    throw new RuntimeException("answer Err Code is " + response.getStatusLine().getStatusCode());
                }
            }
        }
    }
}
