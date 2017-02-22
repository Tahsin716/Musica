package com.randomapps.musica;

import java.io.File;
import java.util.ArrayList;


public class SongManager {

    //Boolean value which determines whether all the songs have been fetched, or the process is ongoing.
    boolean fetchStatus = false;

    //ArrayList for holding all the songs.
    ArrayList<File> songsList = new ArrayList<>();


    /**
     * Required public constructor
     */
    public SongManager(){

    }

    /**
     * Fetches all the songs in the sd card
     * Inner folders are recursively checked for mp3 files
     * Hidden folders are ignored as it may result in a stack-overflow
     * The ArrayList is then returned
     */
    public ArrayList<File> findSongList(File root) {
        ArrayList<File> songs = new ArrayList<>();
        File[] files = root.listFiles();

        for(File singleFile: files) {

            //Recursively check inner folders, ignoring hidden folders; else add the mp3 file
            if(singleFile.isDirectory() && !singleFile.isHidden()) {
                songs.addAll(findSongList(singleFile));
            }
            else {
                if(singleFile.getName().endsWith(".mp3")) {
                    songs.add(singleFile);
                }
            }

            //All songs have been fetched
            fetchStatus = true;
            songsList = songs;

            return songs;
        }
        return songs;
    }



}
