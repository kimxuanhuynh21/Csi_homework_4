package com.example.xuan.csi_homework_4;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity {
    final Handler handler = new Handler();
    Random random = new Random();

    ProgressBar firstMonsterEatenProgressBar;
    ProgressBar secondMonsterEatenProgressBar;
    ProgressBar countDownProgressBar;

    Button cancelButton;
    Button startOverButton;

    TextView totalCookiesInJarTextView;

    int firstMonsterEatenCookies = 0;
    int secondMonsterEatenCookies = 0;
    int totalCookiesInJar = 20;
    int countDown = 0;
    int grandmaMonsterCookingTime = 5000;

    int gameDuration = 120;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.activity_main);

        setControls();
        startGame();
    }

    private void setControls(){
        firstMonsterEatenProgressBar = (ProgressBar) findViewById(R.id.first_monster_eaten_progress_bar);
        secondMonsterEatenProgressBar = (ProgressBar) findViewById(R.id.second_monster_eaten_progress_bar);
        countDownProgressBar = (ProgressBar) findViewById(R.id.count_down_progress_bar);
        totalCookiesInJarTextView = (TextView) findViewById(R.id.total_cookies_in_jar);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseGame();
            }
        });
        startOverButton = (Button) findViewById(R.id.start_over_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeGame();
            }
        });
    }

    public void startGame(){
        handler.post(firstMonsterEating);
        handler.post(secondMonsterEating);
        handler.post(grandmaMonsterBaking);
    }

    private void pauseGame(){
        Toast.makeText(MainActivity.this, "Pause game", Toast.LENGTH_SHORT).show();
        handler.removeCallbacks(firstMonsterEating);
        handler.removeCallbacks(secondMonsterEating);
        handler.removeCallbacks(grandmaMonsterBaking);
    }

    private void resumeGame(){
        startGame();
    }

    Runnable firstMonsterEating = new Runnable() {
        @Override
        public void run() {
            int randomEatenCookies = random.nextInt(totalCookiesInJar);
            int randomTime = random.nextInt(5000) + 5;
            if(firstMonsterEatenCookies < 100){
                firstMonsterEatenCookies += randomEatenCookies;
                totalCookiesInJar -= randomEatenCookies;
                firstMonsterEatenProgressBar.setProgress(firstMonsterEatenCookies);
                handler.postDelayed(this, randomTime);
                Toast.makeText(MainActivity.this, "First monster consumes " + String.valueOf(randomEatenCookies) + " in " + String.valueOf(randomTime), Toast.LENGTH_SHORT).show();
            }
        }
    };
    Runnable secondMonsterEating = new Runnable() {
        @Override
        public void run() {
            int randomEatenCookies = random.nextInt(totalCookiesInJar);
            int randomTime = random.nextInt(5000) + 5;
            if(secondMonsterEatenCookies < 100){
                secondMonsterEatenCookies += randomEatenCookies;
                totalCookiesInJar -= randomEatenCookies;
                secondMonsterEatenProgressBar.setProgress(secondMonsterEatenCookies);
                handler.postDelayed(this, randomTime);
                Toast.makeText(MainActivity.this, "Second monster consumes " + String.valueOf(randomEatenCookies) + " in " + String.valueOf(randomTime), Toast.LENGTH_SHORT).show();
            }
        }
    };
    Runnable grandmaMonsterBaking = new Runnable() {
        @Override
        public void run() {
            int randomCookies = random.nextInt(9) + 1;
            totalCookiesInJar += randomCookies;
            totalCookiesInJarTextView.setText(String.valueOf(randomCookies));
            handler.postDelayed(this, grandmaMonsterCookingTime);
            Toast.makeText(MainActivity.this, "Grandma monster cooks " + String.valueOf(randomCookies), Toast.LENGTH_SHORT).show();
        }
    };
}
