package com.example.firebasegogo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private ArrayList<Cart> cartItems;
    private CartAdapter CartAdapter;
    DatabaseReference databaseReference;
    private ListView listViewCart;
    Button btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-testing-d686c.firebaseio.com/Orders");
        listViewCart = (ListView) findViewById(R.id.listViewCart);
        btnOrder = (Button) findViewById(R.id.buttonorder);
        SharedPreferences pref = getSharedPreferences("MyPref", 0);
        Gson gson =new Gson();
        String json = pref.getString("Cart Items", "");

        cartItems = new ArrayList<Cart>();


        listViewCart.setAdapter(CartAdapter);
        fetchData();
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    Integer tempD = 0;
                    Integer tempDA = 0;
                    Integer tempF = 0;
                    Integer tempFA = 0;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Cart cart = ds.getValue(Cart.class);
                            if(cart.getType().equals("Drinks")){
                                tempD += Integer.parseInt(cart.getPrice())*cart.getQuantity();
                                tempDA += cart.getQuantity();
                            }
                            else{
                                tempF += Integer.parseInt(cart.getPrice())*cart.getQuantity();
                                tempFA += cart.getQuantity();
                            }
                        }
                        SharedPreferences settings = getSharedPreferences("MyPref",0);
                        SharedPreferences.Editor editor = settings.edit();;
                        editor.putInt("Total Drinks", tempD);
                        editor.putInt("Total DA", tempDA);
                        editor.putInt("Total Food", tempF);
                        editor.putInt("Total FA", tempFA);
                        editor.commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                startActivity(new Intent(CartActivity.this, MainActivity.class));
            }
        });
    }
    public void fetchData(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Cart cart = ds.getValue(Cart.class);
                    cartItems.add(cart);
                }
                CartAdapter = new CartAdapter(CartActivity.this, R.layout.cart_item, cartItems);
                listViewCart.setAdapter(CartAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}