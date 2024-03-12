package com.wdj.chatbot.api.test;


import com.wdj.chatbot.api.domain.ai.IOpenAI;
import com.wdj.chatbot.api.domain.zsxq.IZsxqApi;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRunTest {

    private Logger logger = LoggerFactory.getLogger(SpringBootRunTest.class);

    @Value("${chatbot-api.groupId}")
    private String groupId;
    @Value("${chatbot-api.cookie}")
    private String cookie;
    @Resource
    private IZsxqApi zsxqApi;

    @Resource
    private IOpenAI openAI;



    @Test
    public void test_zsxqApi() throws IOException {
        String text = null;
        String topicId = null;

        try {
            String jsonStr = zsxqApi.queryUnAnsweredQuestionsTopicId(groupId, cookie);
            logger.info("拉取提问数据,jsonStr：{}", jsonStr);

            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(jsonStr);
                org.json.JSONObject respData = jsonObject.getJSONObject("resp_data");
                org.json.JSONArray topics = respData.getJSONArray("topics");

                // Parse current date and time
                Date currentDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                // 遍历主题数组，找到第一个零评论的主题并满足时间要求
                for (int i = 0; i < topics.length(); i++) {
                    org.json.JSONObject topic = topics.getJSONObject(i);

                    // 检查评论数是否为零
                    int commentsCount = topic.getInt("comments_count");

                    // Parse topic creation time
                    String createTimeString = topic.getString("create_time");
                    Date createTime = sdf.parse(createTimeString);

                    // Check if comments are zero and topic created within the last three days
                    if (commentsCount == 0 && isWithinThreeDays(createTime, currentDate)) {
                        // 提取主题 ID 和文本
                        topicId = String.valueOf(topic.getLong("topic_id"));
                        text = topic.getJSONObject("talk").getString("text");

                        // 输出主题 ID 和文本
                        System.out.println("三天内零评论Topic ID: " + topicId);
                        System.out.println("三天内零评论Topic Text: " + text);

                        // 找到第一个满足条件的主题后，退出循环
                        break;
                    }
                }
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            logger.error("拉取提问数据异常", e);
        }
    }

    private boolean isWithinThreeDays(Date topicDate, Date currentDate) {
        // Calculate the difference in milliseconds
        long differenceInMillis = currentDate.getTime() - topicDate.getTime();

        // Convert milliseconds to days
        long differenceInDays = differenceInMillis / (24 * 60 * 60 * 1000);

        return differenceInDays <= 1;
    }






    @Test
    public void test_openAi() throws IOException{
        String response = openAI.doChatGpt("帮我写一个java的冒泡排序");
        logger.info("测试结果：{}", response);
    }








}
