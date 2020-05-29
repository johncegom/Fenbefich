package com.example.firebasegogo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailMenu extends AppCompatActivity {
    private ListView myListView;
    ArrayList<Item> item = new ArrayList<>();
    ArrayListAdapter listviewAdapter;
    DatabaseReference databaseReference;
    String MenuType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);
        myListView = (ListView) findViewById(R.id.menu);
        Bundle extras = getIntent().getExtras();
        MenuType = extras.getString("name");
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-testing-d686c.firebaseio.com/"+ MenuType +"");
        refreshListview();
        registerForContextMenu(myListView);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.listviewitem_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String name = this.item.get(info.position).getName();
        switch (item.getItemId()){
            case R.id.editbtn:
                Toast.makeText(this,"u edit",Toast.LENGTH_SHORT).show();
                return true;
            case  R.id.delbtn:
                deleteItem(name);
                this.item.clear();
                listviewAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    public void deleteItem(String ItemName){
        Query applesQuery = databaseReference.orderByChild("Name").equalTo(ItemName);
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "onCancelled", databaseError.toException());
            }
        });
    }
    public void refreshListview(){
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(final com.google.firebase.database.DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Item newItem = ds.getValue(Item.class);
                    item.add(newItem);
                }
                listviewAdapter = new ArrayListAdapter(DetailMenu.this, item);
                myListView.setAdapter(listviewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "loadFood:onCancelled", databaseError.toException());
            }
        });
    }
}

