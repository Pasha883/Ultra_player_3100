package com.example.lesson;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    String[] songNames = {"Лесник", "Кукла кодуна", "Ночь (2024)",
            "Седая ночь", "Мама удалила роблокс", "А где прошла ты..."};
    int[] songs = {R.raw.lesnik, R.raw.kukla, R.raw.noch, R.raw.sednoch, R.raw.roblox, R.raw.proshla};
    int currentSong = 0;
    int playingSong = -1;
    MediaPlayer player;
    public ProgressBar progress;

    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView  = findViewById(R.id.name);
        textView.setText(songNames[currentSong]);

        progress = findViewById(R.id.progressBar);
    }

    public void playPause(View v){
        if(playingSong == -1 || playingSong != currentSong){
            if(player != null) {
                player.stop();
                Log.println(Log.DEBUG,"Player", "Stop, new song");
            }


            player = MediaPlayer.create(this, songs[currentSong]);
            player.start();

            playingSong = currentSong;

            Button button = findViewById(R.id.play_pause);
            button.setText("PAUSE");

            player.setOnPreparedListener(mp -> {
                progress.setMax(player.getDuration());
                player.start();
                updateProgressBar();
            });

            handler = new Handler(Looper.getMainLooper());
            runnable = this::updateProgressBar;
        }

        else if (player.isPlaying()){
            player.pause();

            Button button = findViewById(R.id.play_pause);
            button.setText("PLAY");
        }

        else{
            player.start();

            Button button = findViewById(R.id.play_pause);
            button.setText("PAUSE");
        }

        progress.setProgress((int) (player.getCurrentPosition() / (double) player.getDuration()) * 1000);
        Log.println(Log.DEBUG, "Progress bar", String.valueOf(progress.getProgress()) +
                " " + String.valueOf((int) (player.getCurrentPosition()
                / (double) player.getDuration()) * 1000));
        Log.println(Log.DEBUG, "Progress bar", String.valueOf(player.getCurrentPosition()
                / (double) player.getDuration()));
    }

    public void stop(View v){
        if(player != null){
            player.seekTo(0);
            player.stop();

            Button button = findViewById(R.id.play_pause);
            button.setText("PLAY");

            playingSong = -1;
        }
    }

    public void nextSong(View v) {
        if(currentSong < songNames.length - 1){
            currentSong++;
            TextView textView  = findViewById(R.id.name);
            textView.setText(songNames[currentSong]);
        }
        else{
            currentSong = 0;
            TextView textView  = findViewById(R.id.name);
            textView.setText(songNames[currentSong]);
        }

        Button button = findViewById(R.id.play_pause);

        if(playingSong == currentSong && player.isPlaying()) {
            button.setText("PAUSE");
        }
        else{
            button.setText("PLAY");
        }

    }

    public void prevSong(View v){
        if(currentSong > 0){
            currentSong--;
            TextView textView  = findViewById(R.id.name);
            textView.setText(songNames[currentSong]);
        }
        else{
            currentSong = songNames.length - 1;
            TextView textView  = findViewById(R.id.name);
            textView.setText(songNames[currentSong]);
        }

        Button button = findViewById(R.id.play_pause);

        if(playingSong == currentSong && player.isPlaying()) {
            button.setText("PAUSE");
        }
        else{
            button.setText("PLAY");
        }

    }

    private void updateProgressBar() {
        progress.setProgress(player.getCurrentPosition());
        handler.postDelayed(runnable, 100); // Update every 100 milliseconds
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
        handler.removeCallbacks(runnable);
    }




}