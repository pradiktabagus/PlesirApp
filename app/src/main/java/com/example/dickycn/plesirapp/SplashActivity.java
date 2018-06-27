package com.example.dickycn.plesirapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class SplashActivity extends AppCompatActivity {
    private Typeface slim_joe = null;
    private Typeface big_john = null;
    private TextView textView1, textView2;
    private ImageView imageView;
    //RingProgressBar ringProgressBar;
    int progress = 0;

    /*Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0){
                if (progress<100){
                    progress++;
                    ringProgressBar.setProgress(progress);
                }
            }
        }
    };*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /*slim_joe = Typeface.createFromAsset(getAssets(), "Slim_Joe.otf");
        big_john = Typeface.createFromAsset(getAssets(), "BIG_JOHN.otf");
        textView1 = (TextView)findViewById(R.id.plesir);
        textView2 = (TextView)findViewById(R.id.blora);*/
        /*textView1.setTypeface(slim_joe);
        textView2.setTypeface(big_john);*/
        //ringProgressBar = (RingProgressBar)findViewById(R.id.progressbar);
        imageView = (ImageView)findViewById(R.id.pin);

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.transition);
        imageView.startAnimation(myanim);

        final Intent intent = new Intent(this, MainActivity.class);
        Thread timer = new Thread(){
            public void run (){
                try {
                    sleep(2000);
                }catch (InterruptedException e ){
                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
        /*ringProgressBar.setOnProgressListener(new RingProgressBar.OnProgressListener(){
            @Override
            public void progressToComplete() {
                Toast.makeText(SplashActivity.this,"Sugeng Rawuh", Toast.LENGTH_SHORT).show();
            }
        });*/
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i<60 ; i++){
                    try {
                        Thread.sleep(60);
                        myHandler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/
    }
}
