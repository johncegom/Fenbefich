package com.example.firebasegogo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private CardView btnDrinks;
    private CardView btnFood;
    private CardView btnAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDrinks = (CardView) findViewById(R.id.btnDrinks);
        btnFood = (CardView) findViewById(R.id.btnFood);
        btnAccount = (CardView) findViewById(R.id.btnAccount);
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
