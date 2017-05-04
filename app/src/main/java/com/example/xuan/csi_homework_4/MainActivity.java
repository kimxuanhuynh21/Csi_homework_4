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
import java.util.StringTokenizer;
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
    TextView timePassedTextView;
    TextView firstMonsterEatenCookiesTextView;
    TextView firstMonsterEatTimeLeftTextView;
    TextView secondMonsterEatenCookiesTextView;
    TextView secondMonsterEatTimeLeftTextView;

    int firstMonsterEatenCookies;
    int secondMonsterEatenCookies;
    int totalCookiesInJar;
    int countDownClock;
    int grandmaMonsterCookingTime;
    int gameDuration;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.activity_main);

        initVariables();
        setControls();
    }

    private void setControls(){
        firstMonsterEatenProgressBar = (ProgressBar) findViewById(R.id.first_monster_eaten_progress_bar);
        secondMonsterEatenProgressBar = (ProgressBar) findViewById(R.id.second_monster_eaten_progress_bar);
        countDownProgressBar = (ProgressBar) findViewById(R.id.count_down_progress_bar);
        totalCookiesInJarTextView = (TextView) findViewById(R.id.total_cookies_in_jar);
        timePassedTextView = (TextView) findViewById(R.id.time_passed);
        firstMonsterEatenCookiesTextView = (TextView) findViewById(R.id.first_monster_eaten_cookies);
        firstMonsterEatTimeLeftTextView = (TextView) findViewById(R.id.first_monster_eat_time_left);
        secondMonsterEatenCookiesTextView = (TextView) findViewById(R.id.second_monster_eaten_cookies);
        secondMonsterEatTimeLeftTextView = (TextView) findViewById(R.id.second_monster_eat_time_left);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseGame();
            }
        });
        startOverButton = (Button) findViewById(R.id.start_over_button);
        startOverButton.setOnClickListener(new View.OnClickListener() {
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
        handler.post(countDown);
    }

    private void pauseGame(){
        Toast.makeText(MainActivity.this, "Pause game", Toast.LENGTH_SHORT).show();
        handler.removeCallbacks(firstMonsterEating);
        handler.removeCallbacks(secondMonsterEating);
        handler.removeCallbacks(grandmaMonsterBaking);
    }

    private void resetGame(){
        initVariables();
        startGame();
    }

    private void initVariables(){
        firstMonsterEatenCookies = 0;
        secondMonsterEatenCookies = 0;
        totalCookiesInJar = 20;
        countDownClock = 0;
        grandmaMonsterCookingTime = 5000;
        gameDuration = 120;
    }

    private boolean isGameStopped(){
        return firstMonsterEatenCookies < 100 && secondMonsterEatenCookies < 100 && countDownClock < 120;
    }

    private void resumeGame(){
        startGame();
    }

    Runnable firstMonsterEating = new Runnable() {
        @Override
        public void run() {
            if(isGameStopped()){
                int randomEatenCookies = random.nextInt(totalCookiesInJar);
                int randomTime = random.nextInt(4)+1;
                firstMonsterEatenCookies += randomEatenCookies;
                totalCookiesInJar -= randomEatenCookies;

                firstMonsterEatenCookiesTextView.setText(String.valueOf(firstMonsterEatenCookies));
                firstMonsterEatTimeLeftTextView.setText(String.valueOf(randomTime));
                totalCookiesInJarTextView.setText(String.valueOf(totalCookiesInJar));
                firstMonsterEatenProgressBar.setProgress(firstMonsterEatenCookies);

                handler.postDelayed(this, randomTime*1000);
                Toast.makeText(MainActivity.this, "First monster consumes " + String.valueOf(randomEatenCookies) + " in " + String.valueOf(randomTime), Toast.LENGTH_SHORT).show();
            }
        }
    };
    Runnable secondMonsterEating = new Runnable() {
        @Override
        public void run() {
            if(isGameStopped()){
                int randomEatenCookies = random.nextInt(totalCookiesInJar);
                int randomTime = random.nextInt(4)+1;
                secondMonsterEatenCookies += randomEatenCookies;
                totalCookiesInJar -= randomEatenCookies;

                secondMonsterEatenCookiesTextView.setText(String.valueOf(secondMonsterEatenCookies));
                secondMonsterEatTimeLeftTextView.setText(String.valueOf(randomTime));
                totalCookiesInJarTextView.setText(String.valueOf(totalCookiesInJar));
                secondMonsterEatenProgressBar.setProgress(secondMonsterEatenCookies);

                handler.postDelayed(this, randomTime*1000);
                Toast.makeText(MainActivity.this, "Second monster consumes " + String.valueOf(randomEatenCookies) + " in " + String.valueOf(randomTime), Toast.LENGTH_SHORT).show();
            }
        }
    };
    Runnable grandmaMonsterBaking = new Runnable() {
        @Override
        public void run() {
            int randomCookies = random.nextInt(9) + 1;
            totalCookiesInJar += randomCookies;
            totalCookiesInJarTextView.setText(String.valueOf(totalCookiesInJar));
            handler.postDelayed(this, grandmaMonsterCookingTime);
            Toast.makeText(MainActivity.this, "Grandma monster cooks " + String.valueOf(randomCookies), Toast.LENGTH_SHORT).show();
        }
    };
    Runnable countDown = new Runnable() {
        @Override
        public void run() {
            if(isGameStopped()){
                countDownClock++;
                timePassedTextView.setText(String.valueOf(countDownClock));
                countDownProgressBar.setProgress(countDownClock);
                handler.postDelayed(this, 1000);
            }
        }
    };
}
