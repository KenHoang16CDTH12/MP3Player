package app.it.hueic.mp3player.view;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import app.it.hueic.mp3player.R;
import app.it.hueic.mp3player.controller.SongManager;
import app.it.hueic.mp3player.model.SongModel;
import app.it.hueic.mp3player.util.Util;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {
    public static final int SELECT_SONG_REQUEST = 0;
    private Toolbar toolbar;

    public static ArrayList<SongModel> arrSongs = new ArrayList<>();
    private int seekForwardTime = 5000;
    private int seekBackwardTime = 5000;
    private int currentSongIndex = 0;
    //MediaPlayer
    private MediaPlayer mp;
    //Handler to update UI timer, progress bar etc,...
    private Handler mHandler = new Handler();
    private ImageView btnPlay;
    private ImageView btnNext;
    private ImageView btnPrevious;
    private ImageView btnForward;
    private ImageView btnBackward;
    private SeekBar seekBar;
    private TextView tvCurrentDuration;
    private TextView tvTotalDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        //get actionbar
        toolbar = (Toolbar) findViewById(R.id.tbTop);
        toolbar.setTitle("MP3 Player");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationIcon(R.drawable.music_player32);
        setSupportActionBar(toolbar);
        //get all song from sdcard
        SongManager songManager = new SongManager();
        arrSongs = songManager.getPlayList();
        //Media Player
        mp = new MediaPlayer();
        //findviewById
        //Button
        btnPlay = (ImageView) findViewById(R.id.btnPlay);
        btnForward = (ImageView) findViewById(R.id.btnForward);
        btnBackward = (ImageView) findViewById(R.id.btnRewind);
        btnNext = (ImageView) findViewById(R.id.btnNext);
        btnPrevious = (ImageView) findViewById(R.id.btnPrevious);
        //SeekBar
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        //TextView
        tvCurrentDuration = (TextView) findViewById(R.id.tvStart);
        tvTotalDuration = (TextView) findViewById(R.id.tvEnd);
        //handle Events
        btnPlay.setOnClickListener(this);
        btnForward.setOnClickListener(this);
        btnBackward.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        //listener
        seekBar.setOnSeekBarChangeListener(this);
        mp.setOnCompletionListener(this);
        playSong(currentSongIndex);
    }

    private void playSong(int currentSongIndex) {
        //Play song
        try{
            mp.reset();
            mp.setDataSource(arrSongs.get(currentSongIndex).path);
            mp.prepare();
            mp.start();
            //Update title of toolbar
            toolbar.setTitle(arrSongs.get(currentSongIndex).title);
            //Changing Button Image to pause image
            btnPlay.setImageResource(R.drawable.pause64);
            //set Progressbar values
            seekBar.setProgress(0);
            seekBar.setMax(100);
            //Updating progress bar
            updateProgressBar();
            //notification
            buildNotification();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildNotification() {
        Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
        PendingIntent pendingIntent =  PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        android.support.v4.app.NotificationCompat.Builder  builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.compact_disc32)
                .setContentTitle("Media Artist")
                .setContentText(arrSongs.get(currentSongIndex).title)
                .setDeleteIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    /**
     * Update timer on seekbar
     */
    private void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runable thread
     * @param mUpdateTimeTask
     */
    private Runnable mUpdateTimeTask =  new Runnable() {
        @Override
        public void run() {
            try {
                long totalDuration = mp.getDuration();
                long currentDuration = mp.getCurrentPosition();
                //Displaying Total Duration time
                tvTotalDuration.setText("" + Util.milisecondsToTimer(totalDuration));
                tvCurrentDuration.setText("" + Util.milisecondsToTimer(currentDuration));
                //Updating progress bar
                int progress = Util.getProgressPercentage(currentDuration, totalDuration);
                seekBar.setProgress(progress);
                //Running this thread after 100 milliseconds
                mHandler.postDelayed(this, 100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnPlay:
                /**
                 * Play button click event
                 * plays a song and changes button to pause image
                 * pauses a song and changes button to play image
                 */
                if (mp.isPlaying()) {
                    if (mp != null) {
                        mp.pause();
                        //Changing button image to play Button
                        btnPlay.setImageResource(R.drawable.play_button64);
                    }
                } else {
                    //Resume song
                    if (mp != null) {
                        mp.start();
                        btnPlay.setImageResource(R.drawable.pause64);
                    }
                }
                break;
            case R.id.btnForward :
                //get current song position
                int currentPosition = mp.getCurrentPosition();
                //check if seekForward time is lesser than song duration
                if (currentPosition + seekForwardTime <= mp.getDuration()) {
                    mp.seekTo(currentPosition + seekForwardTime);
                } else {
                    //forward to end position
                    mp.seekTo(mp.getDuration());
                }

                break;
            case R.id.btnRewind :
                //get current song position
                int currentPositionRewind = mp.getCurrentPosition();
                if (currentPositionRewind - seekBackwardTime >= 0) {
                    mp.seekTo(currentPositionRewind - seekBackwardTime);
                } else {
                    mp.seekTo(0);
                }
                break;
            case R.id.btnNext :
                //checck if next song is there or not
                if (currentSongIndex < (arrSongs.size() - 1)) {
                    playSong(currentSongIndex + 1);
                    currentSongIndex = currentSongIndex + 1;
                } else {
                    playSong(0);
                    currentSongIndex = 0;
                }
                buildNotification();
                break;
            case R.id.btnPrevious :
                if (currentSongIndex > 0) {
                    playSong(currentSongIndex - 1);
                    currentSongIndex = currentSongIndex - 1;
                } else {
                    playSong(arrSongs.size() - 1);
                    currentSongIndex = arrSongs.size() - 1;
                }
                buildNotification();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_songs:
                Intent intent = new Intent(this, ListSongActivity.class);
                startActivityForResult(intent, SELECT_SONG_REQUEST);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_SONG_REQUEST && resultCode == RESULT_OK) {
            currentSongIndex = data.getExtras().getInt("id");
            playSong(currentSongIndex);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (currentSongIndex < (arrSongs.size() - 1)) {
            playSong(currentSongIndex + 1);
            currentSongIndex = currentSongIndex + 1;
        } else {
            playSong(0);
            currentSongIndex = 0;
        }
        buildNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.release();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }
}
