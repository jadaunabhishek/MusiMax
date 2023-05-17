package com.example.onlinemusicplayer.AdminCode.ClassJavaAdmin;

public class AdminUpload {

    public  String name;
    public  String url ;
    public  String songsCategory;

    public AdminUpload(String name, String url, String songsCategory) {
        this.name = name;
        this.url = url;
        this.songsCategory = songsCategory;
    }

    public AdminUpload() {
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
