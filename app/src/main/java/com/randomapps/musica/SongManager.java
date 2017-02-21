package com.randomapps.musica;


import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class SongManager {

    final String MEDIA_PATH = new String("/sdcard/"); //sdcard path
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    // Required Public Constructor
    public SongManager(){

    }

    //Read all mp3 files from sdcard and store it in ArrayList
    public ArrayList<HashMap<String, String>> getPlayList(){
        File home = new File(MEDIA_PATH);

        //if there are files with .mp3 extension
        if (home.listFiles(new FileExtensionFilter()).length > 0) {

            //
            for (File file : home.listFiles(new FileExtensionFilter())) {
                HashMap<String, String> song = new HashMap<String, String>();
                song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4))); //TODO: -4
                song.put("songPath", file.getPath());

                // Adding each song to SongList
                songsList.add(song);
            }
        }
        // return songs list array
        return songsList;
    }

    //Class FileExtensionFilter implements FilenameFilter interface, to filter files with .mp3 extension
    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }

}
