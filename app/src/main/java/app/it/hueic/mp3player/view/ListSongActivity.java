package app.it.hueic.mp3player.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import app.it.hueic.mp3player.R;
import app.it.hueic.mp3player.adapter.ListSongAdapter;
import app.it.hueic.mp3player.model.SongModel;

public class ListSongActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Toolbar toolbar;
    private ListView listsong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song);
        //findview
        listsong = (ListView) findViewById(R.id.lvSong);
        //get Action bar
        toolbar = (Toolbar) findViewById(R.id.tbList);
        toolbar.setTitle("List songs");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationIcon(R.drawable.back32);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //create adapter
        ListSongAdapter adapter = new ListSongAdapter(this, R.layout.item_list_song, PlayActivity.arrSongs);
        listsong.setAdapter(adapter);
        listsong.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SongModel song = PlayActivity.arrSongs.get(position);
        //Create intent
        Intent intent = new Intent();
        intent.putExtra("id", position);
        setResult(RESULT_OK, intent);
        finish();
    }

}
