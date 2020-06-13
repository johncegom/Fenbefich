package com.example.firebasegogo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        constraintLayout = (ConstraintLayout) findViewById(R.id.cons_layout);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this,
                R.anim.hyperspace_jump);
        constraintLayout.startAnimation(hyperspaceJumpAnimation);
        handler.postDelayed(runnable, 2000);
    }
}