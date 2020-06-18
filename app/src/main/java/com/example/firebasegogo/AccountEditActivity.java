package com.example.firebasegogo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.HashMap;
import java.util.Map;

public class AccountEditActivity extends AppCompatActivity {
    EditText id;
    EditText name;
    ImageView ava;
    String avalink;
    EditText mail;
    TextView pwd;
    Button bac;
    Button conf;
    String session_id;
    DatabaseReference databaseReference;
    StorageReference folder;
    ImageView img;
    Dialog dialog;
    User user = new User();
    int flag = 0;
    int flag1 = 0;
    final int PICK_IMAGE_REQUEST = 71;
    Uri imagefilePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);
        img = (ImageView) findViewById(R.id.navava);
        id = (EditText) findViewById(R.id.user_id);
        name = (EditText) findViewById(R.id.username);
        ava = (ImageView) findViewById(R.id.avatar);
        pwd = (TextView) findViewById(R.id.pwd_change);
        mail = (EditText) findViewById(R.id.user_mail);
        conf = (Button) findViewById(R.id.confir);
        bac = (Button) findViewById(R.id.bacc);
        SharedPreferences settings = getSharedPreferences("MyPref",0);
        session_id = settings.getString("session ID", null);
//        Toast.makeText(this, session_id, Toast.LENGTH_SHORT).show();
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-testing-d686c.firebaseio.com/Users");
        folder = FirebaseStorage.getInstance().getReference().child("Users");
        fetchData(session_id);

        bac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountEditActivity.this, MainActivity.class));
            }
        });
        conf.setOnClickListener(new View.OnClickListener() {
            String[] key = new String[1];
            String passs;
            @Override
            public void onClick(View v) {
                //Toast.makeText(AccountEditActivity.this, avalink, Toast.LENGTH_SHORT).show();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            User newUser = ds.getValue(User.class);
                            if (newUser.getID().equals(id.getText().toString())) {
                                key[0] = ds.getKey();
                                avalink = newUser.getImage();
                            }
                        }
                        try{
                            if (flag == 1){
                                SharedPreferences settings = getSharedPreferences("MyPref",0);
                                passs = settings.getString("session NewPassword", null);
                            }
                            else {
                                SharedPreferences settings = getSharedPreferences("MyPref",0);
                                passs = settings.getString("session Password", null);
                            }
//                            SharedPreferences settings = getSharedPreferences("MyPref",0);
//                            passs = settings.getString("session Password", null);
                            User newuser = new User(id.getText().toString(), name.getText().toString(), passs, avalink, mail.getText().toString());
                            //  hashingtoDB(newuser, key[0]);
                            if (flag1 == 0) {
                                hashingtoDB(newuser, key[0]);
                            }
                            else {
                                deleteFSimg(newuser);
                                uploadImage(newuser, key[0]);
                            }
                            SharedPreferences settings = getSharedPreferences("MyPref",0);
                            SharedPreferences.Editor editor = settings.edit();;
                            editor.putString("session Password", passs);
                            editor.commit();
                        } catch (Exception e){}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpwdDialog();
            }
        });
        ava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }
    public void deleteFSimg(User temp){
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
    private void uploadImage(final User temp, final String key) {

        if(imagefilePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = folder.child("/"+ temp.getID());
            ref.putFile(imagefilePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AccountEditActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                    temp.setImage(String.valueOf(uri));
                                    hashingtoDB(temp, key);
                                }
                            });
                            flag1 = 0;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AccountEditActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                ava.setImageBitmap(bitmap);
                flag1 = 1;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public void fetchData(final String session_id){
        //Query itemQuery = databaseReference.orderByChild("ID").equalTo(session_id);
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(final com.google.firebase.database.DataSnapshot dataSnapshot) {
                //clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User newUser = ds.getValue(User.class);
                    if(newUser.getID().equals(session_id)){
                        user = newUser;
                    }
                }
                assignData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "loadUser:onCancelled", databaseError.toException());
            }
        });
    }
    public void assignData(){
        id.setText(user.getID());
        name.setText(user.getName());
        Picasso.get().load(user.getImage()).into(ava);
        avalink = user.getImage();
        mail.setText(user.getEmail());
    }
    private void hashingtoDB(User temp, String key){
        Map<String, Object> postValues = temp.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + key, postValues);
        databaseReference.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("", "task is: " + task);
                if(task.isSuccessful()){
                    //finish();
                    //startActivity(new Intent(AccountEditActivity.this, AccountEditActivity.class));
                    //Toast.makeText(AccountEditActivity.this, "ok", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AccountEditActivity.this, "ko", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void showpwdDialog(){
        dialog = new Dialog(AccountEditActivity.this);
        dialog.setContentView(R.layout.pwd_dialog);
        TextView textView = (TextView) dialog.findViewById(R.id.txtmessage);
        textView.setText("Change password");
        //get layout
        final EditText OldPass = (EditText) dialog.findViewById(R.id.txtOP);
        final EditText NewPass = (EditText) dialog.findViewById(R.id.txtNP);
        Button btn = (Button) dialog.findViewById(R.id.btdone);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpass = OldPass.getText().toString();
                String newpass = NewPass.getText().toString();
                comparethenChangePassword(oldpass, newpass);
            }
        });
        dialog.show();
    }
    private void comparethenChangePassword(final String oldpass, final String newpass){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            SharedPreferences settings = getSharedPreferences("MyPref",0);
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User newUser = ds.getValue(User.class);
                    assert newUser != null;
                    if (newUser.getID().equals(id.getText().toString())) {
                        if(oldpass.equals(newUser.getPassword())){
                            SharedPreferences.Editor editor = settings.edit();;
                            editor.putString("session NewPassword", newpass);
                            editor.commit();
                            flag = 1;
                            dialog.dismiss();
                            break;
                        }
                        else{
                            Toast.makeText(AccountEditActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
//    public void clear(){
//        id.getText().clear();
//        name.getText().clear();
//        avalink = "";
//        mail.getText().clear();
//    }

}