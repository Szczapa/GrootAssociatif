package com.example.grt3api.models;

public class Streamer {
    private String user_login;
    private String title;
    private int viewerCount;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    private String url;


    public Streamer(String user_login, String title, int viewerCount, String url) {
        this.user_login = user_login;
        this.title = title;
        this.viewerCount = viewerCount;
        this.url =  url.replace("{width}x{height}", "250x143");;
    }

    // Getters et Setters
    public String getuser_login() {
        return user_login;
    }

    public void setuser_login(String user_login) {
        this.user_login = user_login;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getViewerCount() {
        return viewerCount;
    }

    public void setViewerCount(int viewerCount) {
        this.viewerCount = viewerCount;
    }

    @Override
    public String toString() {
        return "Streamer{" +
                "user_login='" + user_login + '\'' +
                ", url=" + url +
                '}';
    }
}
