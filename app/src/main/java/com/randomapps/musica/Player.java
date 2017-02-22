package com.randomapps.musica;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


public class Player extends AppCompatActivity {

    //song position in ListView
    int position;
    SongManager songManager;

    //Playlist
    ArrayList<File> songList;
    //Song name
    String path;

    ImageView albumArt;
    TextView name, artist;
    ImageButton play, prev, next, playlist;
    SeekBar seekBar;
    String artistName, songName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


    }

}
