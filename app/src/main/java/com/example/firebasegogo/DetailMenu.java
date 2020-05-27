package com.example.firebasegogo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailMenu extends AppCompatActivity {
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
        setContentView(R.layout.activity_detail_menu);
        myListView = (ListView) findViewById(R.id.drink_menu);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-testing-d686c.firebaseio.com/Drinks");
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(final com.google.firebase.database.DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Drink newDrink = ds.getValue(Drink.class);
                    Names.add(newDrink.getName());
                    Prices.add(newDrink.getPrice());
                    Images.add(newDrink.getImage());
                }
                name = Names.toArray(new String[Names.size()]);
                price = Prices.toArray(new String[Prices.size()]);
                img = Images.toArray(new String[Images.size()]);
                ListviewAdapter listviewAdapter = new ListviewAdapter(DetailMenu.this,name,price,img);
                myListView.setAdapter(listviewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
