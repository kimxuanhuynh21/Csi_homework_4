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
    private static final int PROGRESS = 0x1;
    private static final String TAG = "MyActivity";
    private static int MaxBakingTime = 5;
    private static int GameDuration = 120;
    private static int MaxNewCookies = 10;
    private static int MaxEatingSpeed = 5000;

    private ProgressBar firstMonsterProgress;
    private ProgressBar secondMonsterProgress;
    private ProgressBar totalTimeLeftProgress;
    private DonutProgress bakingTimeProgress;

    private Thread eatingThread;
    private Thread bakingThread;
    private Thread countDownThread;
    private Timer newTimer = new Timer();

    private TextView timePassed;
    private TextView totalBakedCookiesTV;
    private TextView bakedCookiesLeftTV;
    private TextView firstMonsterEatenCookiesTV;
    private TextView secondMonsterEatenCookiesTV;
    private Button cancelButton;
    private Button startOverlButton;

    private int circularProgressStatus = 0;
    private int timePassedValue = 0;

    private int mInterval = 1000;
    private long eatingSpeed = 7000;

    private boolean turn = false;

    private int totalBakedCookies = 20;
    private int bakedCookiesLeft = 20;

    private int firstMonsterEatenCookies = 0;
    private int secondMonsterEatenCookies = 0;

    private Random rand = new Random();

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.activity_main);

        bakingTimeProgress = (DonutProgress) findViewById(R.id.baking_time);

        firstMonsterProgress = (ProgressBar) findViewById(R.id.first_monster_eaten_progress_bar);
        secondMonsterProgress = (ProgressBar) findViewById(R.id.second_monster_eaten_progress_bar);
        totalTimeLeftProgress = (ProgressBar) findViewById(R.id.game_time);
        totalBakedCookiesTV = (TextView) findViewById(R.id.totalBakedCookies);
        bakedCookiesLeftTV = (TextView) findViewById(R.id.bakedCookiesLeft);
        timePassed = (TextView)findViewById(R.id.timePassed);
        firstMonsterEatenCookiesTV = (TextView)findViewById(R.id.first_monster_eaten_cookies);
        secondMonsterEatenCookiesTV = (TextView)findViewById(R.id.second_monster_eaten_cookies);
        cancelButton = (Button)findViewById(R.id.cancel_button);
        startOverlButton = (Button)findViewById(R.id.start_over_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Stop all", Toast.LENGTH_SHORT).show();
                stopAll();
            }
        });
        startOverlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Start over", Toast.LENGTH_SHORT).show();
                startOver();
            }
        });

        totalTimeLeftProgress.setMax(GameDuration);

        eatingThread = new Thread(eatingProgress);
        bakingThread = new Thread(bakingProgress);
        countDownThread = new Thread(countDownProgress);

    }

    Runnable eatingProgress = new Runnable() {
        @Override
        public void run() {
            try {
                int eatenCookies = eatCookies(turn); //this function can change value of mInterval.
                if(eatenCookies > 100)
                {
                    eatenCookies = 100;
                }
                if(turn)
                {
                    firstMonsterProgress.setProgress(eatenCookies);
                    firstMonsterEatenCookiesTV.setText(String.valueOf(eatenCookies));
                }
                else
                {
                    secondMonsterProgress.setProgress(eatenCookies);
                    secondMonsterEatenCookiesTV.setText(String.valueOf(eatenCookies));
                }
                turn = !turn;
            } catch (Exception ex) {
                Log.v(TAG, String.valueOf(ex));
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
//                if(timePassedValue < GameDuration && firstMonsterEatenCookies != 100 && secondMonsterEatenCookies != 100)
//                {
//                    mHandler.postDelayed(eatingProgress, eatingSpeed);
//                }
            }
        }
    };

    Runnable bakingProgress = new Runnable() {
        @Override
        public void run() {
            try {
                int value = bakeCookies();
                bakingTimeProgress.setProgress(value);
            } catch (Exception ex) {
                Log.v(TAG, String.valueOf(ex));
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                if(timePassedValue < GameDuration && firstMonsterEatenCookies != 100 && secondMonsterEatenCookies != 100)
                {
                    mHandler.postDelayed(bakingProgress, mInterval);
                }
            }
        }
    };

    Runnable countDownProgress = new Runnable() {
        @Override
        public void run() {
            try {
                passTime();
                timePassed.setText(timePassedValue + "/");
                totalTimeLeftProgress.setProgress(timePassedValue);
            } catch (Exception ex) {
                Log.v(TAG, String.valueOf(ex));
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                if(timePassedValue < GameDuration && firstMonsterEatenCookies != 100 && secondMonsterEatenCookies != 100)
                {
                    mHandler.postDelayed(countDownProgress, mInterval);
                }
            }
        }
    };

    private void passTime() {
        this.timePassedValue++;
    }

    private int eatCookies(boolean turn) {
        int randomCookies = rand.nextInt(this.bakedCookiesLeft) + 1;
        bakedCookiesLeft -= randomCookies;
        bakedCookiesLeftTV.setText(String.valueOf(bakedCookiesLeft));
        int value;
        if(turn)
        {
            this.firstMonsterEatenCookies += randomCookies;
            value = this.firstMonsterEatenCookies;
            Toast.makeText(getApplicationContext(), "First monster ate " + randomCookies, Toast.LENGTH_SHORT).show();
        }
        else
        {
            this.secondMonsterEatenCookies += randomCookies;
            value = this.secondMonsterEatenCookies;
            Toast.makeText(getApplicationContext(), "Second monster ate " + randomCookies, Toast.LENGTH_SHORT).show();
        }
        return value;
    }

    private int bakeCookies() {
        circularProgressStatus++;
        if(circularProgressStatus > MaxBakingTime)
        {
            circularProgressStatus = 1;
            int randomCookies = rand.nextInt(MaxNewCookies) + 1;
            totalBakedCookies += randomCookies;
            bakedCookiesLeft += randomCookies;
            totalBakedCookiesTV.setText(String.valueOf(totalBakedCookies));
            bakedCookiesLeftTV.setText(String.valueOf(bakedCookiesLeft));
            Toast.makeText(getApplicationContext(), "Grandma monster cooks " + randomCookies + " cookies", Toast.LENGTH_SHORT).show();
        }
        int value = circularProgressStatus;
        return value;
    }

    private void startOver() {
//        eatingThread.start();
        newTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
//                if(timePassedValue < GameDuration && firstMonsterEatenCookies != 100 && secondMonsterEatenCookies != 100)
//                {
                    eatingProgress.run();
//                }
            }
        }, 0, eatingSpeed);
        bakingThread.start();
        countDownThread.start();
    }

    private void stopAll() {
        newTimer.cancel();
        eatingThread.interrupt();
        bakingThread.interrupt();
        countDownThread.interrupt();
    }

    private void reset() {

    }
}
