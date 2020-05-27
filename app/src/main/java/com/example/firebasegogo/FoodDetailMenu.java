package com.example.firebasegogo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FoodDetailMenu extends AppCompatActivity {
    private ListView myListView;
    ArrayList<String> Names = new ArrayList<>();
    ArrayList<String> Prices = new ArrayList<>();
    ArrayList<String> Images = new ArrayList<>();
    String[] name;
    String[] price;
    String[] img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail_menu);
        myListView = (ListView) findViewById(R.id.food_menu);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-testing-d686c.firebaseio.com/Food");
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(final com.google.firebase.database.DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Food newFood = ds.getValue(Food.class);
                    Names.add(newFood.getName());
                    Prices.add(newFood.getPrice());
                    Images.add(newFood.getImage());
                }
                name = Names.toArray(new String[Names.size()]);
                price = Prices.toArray(new String[Prices.size()]);
                img = Images.toArray(new String[Images.size()]);
                ListviewAdapter listviewAdapter = new ListviewAdapter(FoodDetailMenu.this,name,price,img);
                myListView.setAdapter(listviewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

