package com.example.firebasegogo;
//import android.support.v7.app.ActionBarActivity;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class DetailMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //navbar
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    //fetch data based on button clicked
    ArrayList<Item> item = new ArrayList<>();
    ArrayListAdapter listviewAdapter;
    StorageReference folder;
    DatabaseReference databaseReference;
    String MenuType;

    FloatingActionButton fab;

    //var for uploading image
    int flag = 0;
    final int PICK_IMAGE_REQUEST = 71;
    Uri imagefilePath;
    ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);
        //navbar hide and see
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(this);
        //map fab button
        fab = (FloatingActionButton) findViewById(R.id.floating_action_button);
        final Item myitem = null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(myitem, "Add Item");
            }
        });

        //fetch data to listview based on button label
        ListView myListView = (ListView) findViewById(R.id.menu);
        Bundle extras = getIntent().getExtras();
        MenuType = extras.getString("name");
        //set action bar title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(MenuType + " Tab");
        folder = FirebaseStorage.getInstance().getReference().child(MenuType);
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-testing-d686c.firebaseio.com/"+ MenuType);
        fetchListview(myListView);

        //make context menu for every listview item
        registerForContextMenu(myListView);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
            }
        };

    }

    //search bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listviewAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    //context menu setting
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.listviewitem_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Item myitem = this.item.get(info.position);
        String name = myitem.getName();
        switch (item.getItemId()){
            case R.id.editbtn:
                showDialog(myitem, "Edit Item");
                return true;
            case  R.id.delbtn:
                //delete item from database and image from storage
                deleteItem(myitem);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    public void deleteItem(final Item temp){
        Query applesQuery = databaseReference.orderByChild("Name").equalTo(temp.getName());
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
                deleteFSimg(temp);
                refresh();
                Toast.makeText(DetailMenu.this, "Delete successfully", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "onCancelled", databaseError.toException());
            }
        });
    }
    public void deleteFSimg(Item temp){
        StorageReference photoRef = folder.getStorage().getReferenceFromUrl(temp.getImage());
        try{
            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    Log.d("TAG", "onSuccess: deleted file");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                    Log.d("TAG", "onFailure: did not delete file");
                }
            });
        }catch (Exception e){}
    }
    public void fetchListview(final ListView myListView){
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(final com.google.firebase.database.DataSnapshot dataSnapshot) {
                item.clear();
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
    public void refresh(){
        item.clear();
        listviewAdapter.notifyDataSetChanged();
    }
    public void showDialog(final Item item, String actvty){
        final Item myitem = item;
        final Dialog dialog = new Dialog(DetailMenu.this);
        dialog.setContentView(R.layout.dialog);
        TextView textView = (TextView) dialog.findViewById(R.id.txtmessage);
        textView.setText(actvty);
        //get layout
        final EditText nameText = (EditText) dialog.findViewById(R.id.txtName);
        final EditText priceText = (EditText) dialog.findViewById(R.id.txtPrice);
        Button btn = (Button) dialog.findViewById(R.id.btdone);
        imgView = (ImageView) dialog.findViewById(R.id.txtImg);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        //prepare data for editting
        //set view
        if (actvty.equals("Edit Item")) {
            final String image = myitem.getImage();
            final String queryname = myitem.getName();
            nameText.setText(queryname);
            priceText.setText(myitem.getPrice());
            Picasso.get().load(myitem.getImage()).into(imgView);
            //edit firebase on button click
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        String[] key = new String[1];
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Item newItem = ds.getValue(Item.class);
                                if (newItem.getName().equals(queryname)) {
                                    key[0] = ds.getKey();
                                }
                            }
                            Item newitem = new Item(nameText.getText().toString(), priceText.getText().toString(), image);
                            if (flag == 0) {
                                hashingtoDB(newitem, key[0]);
                            }
                            if (flag == 1) {
                                deleteFSimg(newitem);
                                uploadImage(newitem, key[0]);
                            }
                            refresh();
                            dialog.dismiss();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("TAG", "onCancelled", databaseError.toException());
                        }
                    });
                }
            });
        }
        if(actvty.equals("Add Item")){
            btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String image = "https://firebasestorage.googleapis.com/v0/b/fir-testing-d686c.appspot.com/o/brb05.19.plus_.jpg?alt=media&token=091d750d-34f1-4e0a-928f-df2f5429e50e";
                    String mykey = databaseReference.push().getKey();
                    Item newitem = new Item(nameText.getText().toString(), priceText.getText().toString(), image);
                    if (flag == 0) {
                        hashingtoDB(newitem, mykey);
                    }
                    if (flag == 1) {
                        uploadImage(newitem, mykey);
                    }
                    refresh();
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            imagefilePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagefilePath);
                imgView.setImageBitmap(bitmap);
                flag = 1;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void uploadImage(final Item temp, final String key) {

        if(imagefilePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = folder.child("/"+ temp.getName());
            ref.putFile(imagefilePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressDialog.dismiss();
                                    Toast.makeText(DetailMenu.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                    temp.setImage(String.valueOf(uri));
                                    hashingtoDB(temp, key);
                                }
                            });
                            flag = 0;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(DetailMenu.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
    private void hashingtoDB(Item temp, String key){
        Map<String, Object> postValues = temp.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + key, postValues);
        databaseReference.updateChildren(childUpdates);
    }

     @Override
     public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
         return super.onOptionsItemSelected(item);
     }
     @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeview:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.foodview:
                Intent FoodIntent = new Intent(this, DetailMenu.class);
                FoodIntent.putExtra("name", "Food");
                startActivity(FoodIntent);
                return true;
            case R.id.drinkview:
                Intent DrinkIntent = new Intent(this, DetailMenu.class);
                DrinkIntent.putExtra("name", "Drinks");
                startActivity(DrinkIntent);
                return true;
            case R.id.logout:
                Intent LogoutIntent = new Intent(this, SplashScreenActivity.class);
                startActivity(LogoutIntent);
                return true;
            default:
                return true;
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

