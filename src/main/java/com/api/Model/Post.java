package com.api.Model;


import lombok.Data;
@Data
public class Post {
    private Song song;
    private String pictureDir;
    private int postID;

    public Post(Integer postID,Song song, String pictureDir) {
        this.postID = postID;
        this.song = song;
        this.pictureDir = pictureDir;
    }
}