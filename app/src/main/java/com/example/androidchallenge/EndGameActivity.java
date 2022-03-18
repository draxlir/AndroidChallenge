package com.example.androidchallenge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class EndGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        SharedPreferences sharedPrefs = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        long score = sharedPrefs.getLong("score", 0);
        boolean win = sharedPrefs.getBoolean("result", false);
        setContentView(R.layout.end_game);
        if(win){
            TextView tw_endGame;
            TextView tw_score;
            tw_score = (TextView) findViewById(R.id.tw_score);
            tw_endGame = (TextView) findViewById(R.id.end_game);
            tw_endGame.setText("Victoire !");
            tw_score.setText(String.format("%d s, %d millis",
                    TimeUnit.MILLISECONDS.toSeconds(score),
                    TimeUnit.MILLISECONDS.toMillis(score) -
                            TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(score))
            ));
        }else{
            TextView tw_endGame;
            TextView tw_score;
            tw_score = (TextView) findViewById(R.id.tw_score);
            tw_endGame = (TextView) findViewById(R.id.end_game);
            tw_endGame.setText("Defaite ...");
            tw_score.setText(String.format("Vous avez perdu"));
        }
    }

    public void clickPlayAgain(View v){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void clickMenu(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
