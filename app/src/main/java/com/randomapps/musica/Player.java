package com.randomapps.musica;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
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
    //Playlist
    ArrayList<File> songList;
    //Song name
    String path;

    ImageView albumArt;
    TextView name, artist;
    ImageButton play, prev, next, playlist;
    SeekBar seekBar;
    String artistName, songName;

    //Uniform Resource Identifier, used for media player to pass song name
    Uri uri;
    //Seebar updated in Thread
    static SeekBarUpdater seekBarUpdater;

    byte[] art;
    Bitmap songArt;

    //Used for retrieving song informations, such as artist-name, album-art etc.
    MediaMetadataRetriever mediaMetadataRetriever;
    static MediaPlayer mediaPlayer;
    boolean notificationFlag = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        init();

        if(mediaPlayer != null) {
            if(seekBarUpdater != null) {
                //code it
            }
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    public void init() {
        albumArt = (ImageView)findViewById(R.id.albumArt);
        name = (TextView)findViewById(R.id.songName);
        artist = (TextView)findViewById(R.id.artistName);
        play = (ImageButton)findViewById(R.id.playButton);
        prev = (ImageButton)findViewById(R.id.previousButton);
        next = (ImageButton)findViewById(R.id.nextButton);
        playlist = (ImageButton)findViewById(R.id.playlistButton);
        seekBar = (SeekBar)findViewById(R.id.seekBar);

        Bundle extras = getIntent().getExtras();
        position = extras.getInt("position");
        songList = extras.getParcelable("song_list");
        path = songList.get(position).getName();
        uri = Uri.parse(path);
    }

    class SeekBarUpdater extends Thread {

    }

}
