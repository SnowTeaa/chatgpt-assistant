package com.wdj.chatbot.api.domain.ai;

import java.io.IOException;

public interface IOpenAI {

    String doChatGpt(String question) throws IOException;

}
