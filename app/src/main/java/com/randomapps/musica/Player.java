package com.randomapps.musica;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    //Filter out all songs
    SongManager songManager;

    ImageView albumArt;
    TextView name, artist;
    ImageButton play, prev, next, playlist;
    SeekBar seekBar;
    String artistName, songName;

    //Uniform Resource Identifier, used for media player to pass song name
    Uri uri;
    //Seekbar updated in Thread
    static SeekBarUpdater seekBarUpdater;

    byte[] art;
    Bitmap songArt;

    //Used for retrieving track information, such as artist-name, album-art etc.
    MediaMetadataRetriever mediaMetadataRetriever;
    static MediaPlayer mediaPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        init();

        //If a song is already playing stop and release media player
        //Also stop the seekbar thread if, the song was not paused or completed
        if(mediaPlayer != null) {
            if(seekBarUpdater != null) {
                seekBarUpdater.stopThread();
                seekBarUpdater.interrupt();
            }
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        //Fetch all the songs
        songManager = new SongManager();
        if(!songManager.getFetchStatus()) {
            songList = songManager.findSongList(Environment.getExternalStorageDirectory());
        }
        else {
            songList = songManager.getSongsList();
        }

        position = getIntent().getIntExtra("position",0);

        //Path has to be toString() not getName()
        //Else it gives error
        path = songList.get(position).toString();
        uri = Uri.parse(path);

        setSongData(path, position);
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        play.setImageResource(R.drawable.pause);

        seekBar.setMax(mediaPlayer.getDuration());
        seekBarUpdater = new SeekBarUpdater(true);
        seekBarUpdater.start();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //For updating the seekbar by dragging it
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    play.setImageResource(R.drawable.play);
                } else {
                    mediaPlayer.start();
                    play.setImageResource(R.drawable.pause);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playbackUtils("next");
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playbackUtils("prev");
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playbackUtils("complete");
            }
        });
    }

    /**
     * Initializes the UI components
     */
    public void init() {
        albumArt = (ImageView)findViewById(R.id.albumArt);
        name = (TextView)findViewById(R.id.songName);
        artist = (TextView)findViewById(R.id.artistName);
        play = (ImageButton)findViewById(R.id.playButton);
        prev = (ImageButton)findViewById(R.id.previousButton);
        next = (ImageButton)findViewById(R.id.nextButton);
        playlist = (ImageButton)findViewById(R.id.playlistButton);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
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
                    running = false;
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                running = false;
            }
        }
    }


    /**
     * Sets the embedded song data, such as artist-name, album-art etc
     * if data is not provided then default info is set
     */
    public void setSongData(String songPath, int pos) {
        mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(songPath);

        try {
            songName = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            art = mediaMetadataRetriever.getEmbeddedPicture();
            songArt = BitmapFactory.decodeByteArray(art, 0, art.length);
            artistName = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

            artist.setText(artistName);
            name.setText(songName);
            albumArt.setImageBitmap(songArt);
        }
        catch (Exception e) {
            albumArt.setImageResource(R.drawable.albumart);
            artist.setText("Unknown Artist");
            name.setText(songList.get(pos).getName().replace(".mp3",""));
        }
    }


    /**
     * Reduces repetition of fairly common of
     * next, prev and on-completion
     */
    public void playbackUtils(String callback) {
        if(seekBarUpdater != null) {
            seekBarUpdater.stopThread();
            seekBarUpdater.interrupt();
            seekBarUpdater = null;
        }

        mediaPlayer.stop();
        mediaPlayer.release();

        //If callback is next or complete we will go to the next song
        //but modulus is taken to not go array index out of bounds
        //similarly for previous if first song is playing, we go to the last one
        if(callback.equals("next") || callback.equals("complete")) {
            position =  (position + 1) % songList.size();
        }
        else {
            position = (position - 1 < 0) ? songList.size() - 1 : position - 1;
        }

        path = songList.get(position).toString();
        setSongData(path, position);
        uri = Uri.parse(path);
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playbackUtils("complete");
            }
        });

        play.setImageResource(R.drawable.pause);

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        seekBarUpdater = new SeekBarUpdater(true);
        seekBarUpdater.start();
    }

}
