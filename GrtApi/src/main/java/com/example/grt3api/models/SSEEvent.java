package com.example.grt3api.models;

public class SSEEvent {
    private final String type;
    private final Object data;

    public SSEEvent(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
}