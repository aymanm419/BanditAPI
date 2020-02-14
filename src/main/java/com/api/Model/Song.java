package com.api.Model;

import lombok.Data;

@Data
public class Song {
    private String songName, bandName;
    private String songFileDir;

    public Song(String songName, String bandName, String songFileDir) {
        this.songName = songName;
        this.bandName = bandName;
        this.songFileDir = songFileDir;
    }
}
