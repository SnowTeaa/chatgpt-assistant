package com.wdj.chatbot.api.domain.zsxq;

import java.io.IOException;

public interface IZsxqApi {

   // UnAnsweredQuestionsAggregates queryUnAnsweredQuestionsTopicId(String groupId, String cookie) throws IOException;
    String queryUnAnsweredQuestionsTopicId(String groupId, String cookie) throws IOException;
    boolean answer(String groupId, String cookie, String topicId, String text, boolean silenced) throws IOException;

}