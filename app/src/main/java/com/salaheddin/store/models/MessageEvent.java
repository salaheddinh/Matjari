package com.salaheddin.store.models;


public class MessageEvent{
    private String Message;

    public MessageEvent(String message) {
        Message = message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMessage() {
        return Message;
    }
}
