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

        setSongData(path,position);
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        play.setImageResource(R.drawable.pause);

        seekBar.setMax(mediaPlayer.getDuration());
        //seekBarUpdater = new SeekBarUpdater(true);
        //seekBarUpdater.start();
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

    /**
     * If running is true then seekbar is updating else no
     * A thread is opened where the seekbar is updated every 5 milisecond
     * if current is greater than total duration then we break out of the loop
     */
    class SeekBarUpdater extends Thread {
        private boolean running;

        public SeekBarUpdater(boolean status) {
            running = status;
        }

        public void stopThread() {
            running = false;
        }

        @Override
        public void run() {
            try {
                while (running) {
                    int duration = mediaPlayer.getDuration();
                    int current = mediaPlayer.getCurrentPosition();

                    while (current < duration) {
                        sleep(500);
                        current = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(current);
                    }
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                running = false;
            }
        }
    }

    public void setSongData(String songPath, int pos) {

    }

}
