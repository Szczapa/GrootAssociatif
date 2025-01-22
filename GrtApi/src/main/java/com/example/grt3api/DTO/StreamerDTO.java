package com.example.grt3api.DTO;

import java.util.Objects;

public class StreamerDTO {
    private String name;  // Pseudo du streamer
    private boolean online;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;


    public StreamerDTO(String name, boolean online, String url) {
        this.name = name;
        this.online = online;
        this.url = url;
    }

    // Getters et Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreamerDTO that = (StreamerDTO) o;
        return online == that.online && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, online);
    }


    @Override
    public String toString() {
        return "StreamerDTO{" +
                "name='" + name + '\'' +
                ", online=" + online +
                '}';
    }
}
