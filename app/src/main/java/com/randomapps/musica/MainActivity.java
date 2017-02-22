package com.randomapps.musica;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ListView listView;
    String baseDirectory;
    String[] listItems;
    ArrayList<File> songList;
    SongManager songManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        //songList updated again for assurance
        songList = songManager.getSongsList();
        listItems = new String[songList.size()];

        String[] debug = {"hello", "world", "my", "name", "is", "tahsin", "rashad"};

        //Remove the file extension '.mp3' from the name
        for(int i = 0; i < songList.size(); i++) {
            listItems[i] = songList.get(i).getName().replace(".mp3", "");
        }

        //ArrayAdapter for setting up the ListView
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.row_layout, R.id.text_view, listItems);
        listView.setAdapter(arrayAdapter);
    }

    /**
     * Used to initialize all the components
     * set the dialog and fetch the songs
     */
    public void init() {

        listView = (ListView)findViewById(R.id.list_view);
        songManager = new SongManager();
        //Path to external storage directory, in this case SD card
        baseDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait, while songs are loading...");
        //The dialog is cancelable with BACK key
        progressDialog.setCancelable(true);

        //Display the progress dialog, until all songs have been fetched
        while(!songManager.getFetchStatus()) {
            songList = songManager.findSongList(new File(baseDirectory));
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
