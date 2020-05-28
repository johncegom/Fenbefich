package com.example.firebasegogo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DrinkDetailMenu extends AppCompatActivity {
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
        myListView = (ListView) findViewById(R.id.menu);
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
                fromListtoArray();
                //fetching using array listview adapter
                ListviewAdapter listviewAdapter = new ListviewAdapter(DrinkDetailMenu.this,name,price,img);
                myListView.setAdapter(listviewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "loadDrink:onCancelled", databaseError.toException());
            }
        });
        registerForContextMenu(myListView);
    }
    public void fromListtoArray() {
        name = Names.toArray(new String[Names.size()]);
        price = Prices.toArray(new String[Prices.size()]);
        img = Images.toArray(new String[Images.size()]);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.listviewitem_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.editbtn:
                Toast.makeText(this,"u edit",Toast.LENGTH_SHORT).show();
                return true;
            case  R.id.delbtn:
                Toast.makeText(this,"u delete",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
