package com.example.firebasegogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

public class RevenueActivity extends AppCompatActivity {

    TextView textViewTotalFood;
    TextView textViewTotalDrink;
    TextView textViewAmountFood;
    TextView textViewAmountDrink;
    TextView textViewTotal;
    TextView textViewAmounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);

        textViewAmounts = (TextView) findViewById(R.id.textViewAmounts);
        textViewTotalDrink = (TextView) findViewById(R.id.textViewTotalDrink);
        textViewTotalFood = (TextView) findViewById(R.id.textViewTotalFood);
        textViewAmountFood = (TextView) findViewById(R.id.textViewAmountFood);
        textViewAmountDrink = (TextView) findViewById(R.id.textViewAmountDrink);
        textViewTotal = (TextView) findViewById(R.id.textViewTotal);

        SharedPreferences pref = getSharedPreferences("MyPref", 0);
        Integer totalD = pref.getInt("Total Drinks", 0);
        Integer totalF = pref.getInt("Total Food", 0);
        Integer amountD = pref.getInt("Total DA", 0);
        Integer amountF = pref.getInt("Total FA", 0);
        Integer total = totalD + totalF;
        Integer amounts = amountD + amountF;

        textViewTotalDrink.setText(totalD.toString());
        textViewTotalFood.setText(totalF.toString());
        textViewAmountFood.setText(amountF.toString());
        textViewAmountDrink.setText(amountD.toString());
        textViewTotal.setText(total.toString());
        textViewAmounts.setText(amounts.toString());


    }
}