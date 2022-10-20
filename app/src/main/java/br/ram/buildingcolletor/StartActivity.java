package br.ram.buildingcolletor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

import br.ram.buildingcolletor.asset.AssetsActivity;

public class StartActivity extends AppCompatActivity {

    //Variables
    protected ProgressBar progressBar;
    private TimerTask myTimerTask;
    private Intent nextActivity;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Reference
        progressBar = findViewById(R.id.progressBar);

        // show the progress bar
        progressBar.getProgress();

        //Before 4s, change activity
        myTimerTask = new TimerTask() {
            @Override
            public void run() {
                //Codigo pasar pantalla
                nextActivity = new Intent(StartActivity.this, AssetsActivity.class);
                finish();
                startActivity(nextActivity);
            }
        };

        timer = new Timer();
        timer.schedule(myTimerTask, 4000);
    }
}