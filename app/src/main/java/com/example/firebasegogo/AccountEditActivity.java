package com.example.firebasegogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class AccountEditActivity extends AppCompatActivity {
    EditText id;
    EditText name;
    ImageView ava;
    Button pwd;
    Button conf;
    String session_id;
    DatabaseReference databaseReference;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);
        img = (ImageView) findViewById(R.id.navava);
        id = (EditText) findViewById(R.id.user_id);
        name = (EditText) findViewById(R.id.username);
        ava = (ImageView) findViewById(R.id.avatar);
        pwd = (Button) findViewById(R.id.pwd_change);
        conf = (Button) findViewById(R.id.confirm);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        session_id = pref.getString("session ID", null);
        Toast.makeText(this, session_id, Toast.LENGTH_SHORT).show();
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-testing-d686c.firebaseio.com/Users");
        fetchData(session_id);
    }
    public void fetchData(String session_id){
        Query itemQuery = databaseReference.orderByChild("ID").equalTo(session_id);
        itemQuery.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(final com.google.firebase.database.DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User newUser = ds.getValue(User.class);
                    id.setText(newUser.getID());
                    name.setText(newUser.getName());
                    Picasso.get().load(newUser.getImage()).fit().into(ava);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "loadFood:onCancelled", databaseError.toException());
            }
        });
    }
}