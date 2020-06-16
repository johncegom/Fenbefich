package com.example.firebasegogo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private CardView btnDrinks;
    private CardView btnFood;
    private CardView btnAccount;
    private TextView textViewWelcome;
    private Button btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDrinks = (CardView) findViewById(R.id.btnDrinks);
        btnFood = (CardView) findViewById(R.id.btnFood);
        btnAccount = (CardView) findViewById(R.id.btnAccount);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        textViewWelcome = (TextView) findViewById(R.id.textViewWelcome);

        //Set welcome text
        SharedPreferences pref = getSharedPreferences("MyPref", 0);
        textViewWelcome.setText("Hello, " + pref.getString("session Name", null));

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LogoutIntent = new Intent(MainActivity.this, SplashScreenActivity.class);
                startActivity(LogoutIntent);
            }
        });

        btnDrinks.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent DrinkIntent = new Intent(MainActivity.this, DetailMenuActivity.class);
                DrinkIntent.putExtra("name", "Drinks");
                startActivity(DrinkIntent);
            }
        });
        btnFood.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent FoodIntent = new Intent(MainActivity.this, DetailMenuActivity.class);
                FoodIntent.putExtra("name", "Food");
                startActivity(FoodIntent);
            }
        });
        btnAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent AccountEditIntent = new Intent(MainActivity.this, AccountEditActivity.class);
                startActivity(AccountEditIntent);
            }
        });

    }
}
