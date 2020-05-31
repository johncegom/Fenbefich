package com.example.firebasegogo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
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
import java.util.UUID;

public class DetailMenu extends AppCompatActivity {
    ListView myListView;
    ArrayList<Item> item = new ArrayList<>();
    ArrayListAdapter listviewAdapter;
    StorageReference folder;
    DatabaseReference databaseReference;
    String MenuType;
    String[] key = new String[1];
    //var for uploading image
    Item myitem1;
    int flag = 0;
    final int PICK_IMAGE_REQUEST = 71;
    Uri filePath;
    ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);
        myListView = (ListView) findViewById(R.id.menu);
        Bundle extras = getIntent().getExtras();
        MenuType = extras.getString("name");
        folder = FirebaseStorage.getInstance().getReference().child("FoodImages");
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-testing-d686c.firebaseio.com/"+ MenuType +"");
        fetchListview();
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
        Item myitem = this.item.get(info.position);
        String name = myitem.getName();
        switch (item.getItemId()){
            case R.id.editbtn:
                showDialog(myitem);
                return true;
            case  R.id.delbtn:
                deleteItem(name);
                refresh();
                return true;
            case R.id.newbtn:
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
    public void fetchListview(){
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
    public void refresh(){
        item.clear();
        listviewAdapter.notifyDataSetChanged();
    }
    public void showDialog(final Item myitem){
        final Dialog dialog = new Dialog(DetailMenu.this);
        dialog.setTitle("Edit Box");
        dialog.setContentView(R.layout.dialog);
        TextView textView = (TextView) dialog.findViewById(R.id.txtmessage);
        textView.setText("Update item");
        //get layout
        final EditText nameText = (EditText) dialog.findViewById(R.id.txtName);
        final EditText priceText = (EditText) dialog.findViewById(R.id.txtPrice);
        imgView = (ImageView) dialog.findViewById(R.id.txtImg);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                chooseImage();
            }
        });
        //prepare data for editting
        final String image = myitem.getImage();
        final String queryname = myitem.getName();
        //set view
        nameText.setText(queryname);
        priceText.setText(myitem.getPrice());
        Picasso.get().load(myitem.getImage()).into(imgView);

        //edit firebase on button click
        Button btn = (Button) dialog.findViewById(R.id.btdone);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren()) {
                            Item newItem = ds.getValue(Item.class);
                            if (newItem.getName().equals(queryname)){
                                key[0] = ds.getKey();
                            }
                        }
                        myitem1 = new Item(nameText.getText().toString(), priceText.getText().toString(), image);
                        if (flag == 0){
                            Map<String, Object> postValues = myitem1.toMap();
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/" + key[0], postValues);
                            databaseReference.updateChildren(childUpdates);
                        }
                        if (flag == 1){
                            uploadImage();
                        }
                        refresh();
                        dialog.dismiss();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TAG", "onCancelled", databaseError.toException());
                    }
                });
                //Toast.makeText(DetailMenu.this, queryname, Toast.LENGTH_LONG).show();

            }
        });
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
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = folder.child("/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressDialog.dismiss();
                                    Toast.makeText(DetailMenu.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                    myitem1.setImage(String.valueOf(uri));
                                    Map<String, Object> postValues = myitem1.toMap();
                                    Map<String, Object> childUpdates = new HashMap<>();
                                    childUpdates.put("/" + key[0], postValues);
                                    databaseReference.updateChildren(childUpdates);
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
}

