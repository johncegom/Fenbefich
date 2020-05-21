package com.example.firebasegogo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity {
    private Firebase mRef;
    private Button btn;
    private EditText et;
    private EditText testvar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://fir-testing-d686c.firebaseio.com/Users");
        btn = (Button) findViewById(R.id.button);
        et = (EditText) findViewById(R.id.editText);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String value = et.getText().toString();
//                Firebase mRefchild = mRef.child("Name");
//                mRefchild.setValue(value);
                mRef.push().setValue(value);
            }
        });
    }
}
