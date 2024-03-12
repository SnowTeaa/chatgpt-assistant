package com.wdj.chatbot.api.domain.zsxq.model.req;


public class AnswerReq {

    private com.wdj.chatbot.api.domain.zsxq.model.req.ReqData req_data;

    public AnswerReq(com.wdj.chatbot.api.domain.zsxq.model.req.ReqData req_data) {
        this.req_data = req_data;
    }

    public com.wdj.chatbot.api.domain.zsxq.model.req.ReqData getReq_data() {
        return req_data;
    }

    public void setReq_data(com.wdj.chatbot.api.domain.zsxq.model.req.ReqData req_data) {
        this.req_data = req_data;
    }

}
