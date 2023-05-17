package com.example.onlinemusicplayer.UserCode.ClassJavaUser;

public class UserUpload {

    String name;
    String url ;
    String songsCategory;

    public UserUpload(String name, String url, String songsCategory) {
        this.name = name;
        this.url = url;
        this.songsCategory = songsCategory;
    }

    public UserUpload() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSongsCategory() {
        return songsCategory;
    }

    public void setSongsCategory(String songsCategory) {
        this.songsCategory = songsCategory;
    }
}
