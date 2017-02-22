package com.randomapps.musica;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;


import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<File> songList;
    SongManager songManager;
    ProgressDialog progressDialog;
    File root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    /**
     * Used to initialize all the components
     * set the dialog and fetch the songs
     */
    public void init() {

        listView = (ListView)findViewById(R.id.listView);
        songManager = new SongManager();
        //Path to external storage directory, in this case SD card
        root = Environment.getExternalStorageDirectory();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait, while songs are loading...");
        //The dialog is cancelable with BACK key
        progressDialog.setCancelable(true);

        //Display the progress dialog, until all songs have been fetched
        while(!songManager.getFetchStatus()) {
            songList = songManager.findSongList(root);
        }
        if(songList != null){
            progressDialog.dismiss();
        }

    }

    /**
     * Close the app if user presses BACK key
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
