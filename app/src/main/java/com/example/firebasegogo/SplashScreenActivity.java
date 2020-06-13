package com.example.firebasegogo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    LinearLayout linearLayout;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            linearLayout.setVisibility(View.VISIBLE);
        }
    };
    Runnable r = new Runnable() {
        @Override
        public void run() {
            constraintLayout.animate().translationYBy(-600f).setDuration(2000);
        };
    };
    Button login_button;
    Animation topAnimation, bottomAnimation;
    ImageView logoSplashScreen;
    TextView appName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        //animate
        constraintLayout = (ConstraintLayout) findViewById(R.id.cons_layout);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        handler.postDelayed(runnable, 4000);
        //Login
        login_button = (Button) findViewById(R.id.btn_login);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Map
        logoSplashScreen = (ImageView) findViewById(R.id.imageViewSplashScreen);
        appName = (TextView) findViewById(R.id.textViewSplashScreen);

        //Set animation for logo and text
        logoSplashScreen.setAnimation(topAnimation);
        appName.setAnimation(bottomAnimation);
        handler.postDelayed(r, 2000);
    }
}