package com.example.firebasegogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    private Button btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent DrinkIntent = new Intent(MainActivity.this, DetailMenu.class);
                DrinkIntent.putExtra("name", "Drinks");
                startActivity(DrinkIntent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent FoodIntent = new Intent(MainActivity.this, DetailMenu.class);
                FoodIntent.putExtra("name", "Food");
                startActivity(FoodIntent);
            }
        });
    }
}
