package com.entity;

import com.google.gson.Gson;

public class Msg {
    private String msgType;
    private String msgBody;

    public Msg(String msgType, String msgBody) {
        this.msgType = msgType;
        this.msgBody = msgBody;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static Msg decode(String msg){
        return new Gson().fromJson(msg, Msg.class);
    }
}
