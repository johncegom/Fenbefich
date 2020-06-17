package com.example.firebasegogo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashScreenActivity extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    LinearLayout linearLayout;
    Handler handler;
    Runnable runnable;
    Button login_button;
    Animation topAnimation, bottomAnimation;
    ImageView logoSplashScreen;
    TextView appName;
    EditText login_id;
    EditText password;
    CheckBox cb;
    private int flag = 0;
    DatabaseReference databaseReference;
    static boolean calledAlready = false;
    static boolean calledAlready1 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        handler =  new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                linearLayout.setVisibility(View.VISIBLE);
            }
        };

        //preload part of data
        if (!calledAlready)
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-testing-d686c.firebaseio.com");
        databaseReference.keepSynced(true);
        //Map animation
        constraintLayout = (ConstraintLayout) findViewById(R.id.cons_layout);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        //Map view
        logoSplashScreen = (ImageView) findViewById(R.id.imageViewSplashScreen);
        appName = (TextView) findViewById(R.id.textViewSplashScreen);
        login_id = (EditText) findViewById(R.id.user_id);
        password = (EditText) findViewById(R.id.user_mail);
        login_button = (Button) findViewById(R.id.btn_login);
        cb = (CheckBox) findViewById(R.id.rem_cb);
        SharedPreferences pref = getSharedPreferences("MyPref", 0);
        login_id.setText(pref.getString("session ID", null));
        if (pref.getBoolean("remember me", false)){
            password.setText(pref.getString("session Password", null));
            cb.setChecked(true);
        }
        else{
            password.setText(null);
        }
        //Set animation for logo and text
        if (!calledAlready1)
        {
            logoSplashScreen.setAnimation(topAnimation);
            appName.setAnimation(bottomAnimation);
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    constraintLayout.animate().translationYBy(-650f).setDuration(2000);
                };
            };
            handler.postDelayed(r, 2000);
            handler.postDelayed(runnable, 4000);
            calledAlready1 = true;
        }
        else {
            constraintLayout.animate().translationYBy(-650f);
            linearLayout.setVisibility(View.VISIBLE);
        }
        //Validate login
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(login_id.getText().toString(), password.getText().toString());
            }
        });
        //Remember me
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    SharedPreferences settings = getSharedPreferences("MyPref",0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("session Password", password.getText().toString());
                    editor.putBoolean("remember me", true);
                    editor.commit();
                }
                else{
                    SharedPreferences settings = getSharedPreferences("MyPref",0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("remember me", false);
                    editor.commit();
                }
            }
        });
    }
    public boolean validate(final String id, final String passwordd){
        databaseReference.child("Users").addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            SharedPreferences settings = getSharedPreferences("MyPref",0);
            SharedPreferences.Editor editor = settings.edit();
            @Override
            public void onDataChange(final com.google.firebase.database.DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User newuser = ds.getValue(User.class);
                    if (newuser.getID().equals(id) && newuser.getPassword().equals(passwordd)){
                        flag = 1;
                        //store session id
                        SharedPreferences.Editor editor = settings.edit();;
                        editor.putString("session ID", newuser.getID());
                        editor.putString("session Name", newuser.getName());
                        editor.putString("session Ava", newuser.getImage());
                        editor.putString("session Password", newuser.getPassword());
                        editor.putString("session Email", newuser.getEmail());
                        editor.commit();
                    }
                }
                if (flag == 1){
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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