package com.example.mylastpassphone;

public class Data {

    private String url;
    private String username;
    private String password;
    private int id;

    public Data() {
    }

    public Data(String url, String username, String password, int id) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }
}
