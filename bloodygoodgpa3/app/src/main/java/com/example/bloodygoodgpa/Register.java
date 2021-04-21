package com.example.bloodygoodgpa;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class Register extends AppCompatActivity {

    private ImageButton back, submit;
    private EditText NetID;
    private Container container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        back = (ImageButton) findViewById(R.id. imageButton);
        NetID = (EditText) findViewById(R.id. NetID);
        submit = (ImageButton) findViewById(R.id. send_NetID);
        container = ((BaseApp)getApplication()).container;
    }
    public void goBack(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
    public void register(View view) {
        container.user = NetID.getText().toString().toLowerCase().trim();
        startActivity(new Intent(this, RegisterCode.class));
    }
}