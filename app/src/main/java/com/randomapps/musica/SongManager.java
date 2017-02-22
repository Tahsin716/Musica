package com.randomapps.musica;


import android.os.Environment;

import java.io.File;
import java.util.ArrayList;


public class SongManager {

    //Boolean value which determines whether all the songs have been fetched, or the process is ongoing.
    boolean fetchStatus = false;

    //ArrayList for holding all the songs.
    ArrayList<File> songsList = new ArrayList<>();

    //This gives directory of the external memory (SD card)
    File root = Environment.getExternalStorageDirectory();
    /**
     * Required public constructor
     */
    public SongManager(){

    }





}
