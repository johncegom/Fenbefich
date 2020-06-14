package com.example.firebasegogo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Constructor;

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
    Button login_button;
    Animation topAnimation, bottomAnimation;
    ImageView logoSplashScreen;
    TextView appName;
    EditText login_id;
    EditText password;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        //preload part of data
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-testing-d686c.firebaseio.com");
        databaseReference.keepSynced(true);
        //animate
        constraintLayout = (ConstraintLayout) findViewById(R.id.cons_layout);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        handler.postDelayed(runnable, 4000);
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Map
        logoSplashScreen = (ImageView) findViewById(R.id.imageViewSplashScreen);
        appName = (TextView) findViewById(R.id.textViewSplashScreen);
        login_id = (EditText) findViewById(R.id.user_id);
        password = (EditText) findViewById(R.id.user_pwd);
        login_button = (Button) findViewById(R.id.btn_login);

        //Set animation for logo and text
        logoSplashScreen.setAnimation(topAnimation);
        appName.setAnimation(bottomAnimation);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                constraintLayout.animate().translationYBy(-600f).setDuration(2000);
            };
        };
        handler.postDelayed(r, 2000);
        //Validate login
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(login_id.getText().toString(), password.getText().toString());
            }
        });
    }
    public boolean validate(final String id, final String password){
        databaseReference.child("Users").addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            int flag = 0;
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            @Override
            public void onDataChange(final com.google.firebase.database.DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User newuser = ds.getValue(User.class);
                    if (newuser.getID().equals(id) && newuser.getPassword().equals(password)){
                        flag = 1;
                        editor.putString("session ID", login_id.getText().toString());
                        editor.putString("session Name", newuser.getName());
                        editor.putString("session Ava", newuser.getImage());
                        editor.commit();
                    }
                }
                if (flag == 1){
                    login_id.setText("");
                    //store session id
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(SplashScreenActivity.this, "Invalid attempt to access", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "loadFood:onCancelled", databaseError.toException());
            }
        });
        return false;
    }
}