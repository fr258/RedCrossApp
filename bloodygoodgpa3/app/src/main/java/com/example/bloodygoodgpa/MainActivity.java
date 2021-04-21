package com.example.bloodygoodgpa;

import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Handler;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button login, register;
    EditText Username, Password;
    Container container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = ((BaseApp)getApplication()).container;
        Username = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.creating_new_user);

        login.setOnClickListener(this);
        register.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        Container container =  ((BaseApp)getApplication()).container;
        if (v.getId() == R.id.login) {
            Boolean success = container.client.login(Username.getText().toString().trim().toLowerCase(), Password.getText().toString());
            if (success==null) {
                Toast.makeText(getApplicationContext(), "Failed to connect to network. Please check your connection and try again.", Toast.LENGTH_SHORT).show();
            }
            else if ((success!=null) && success) {
                container.user = Username.getText().toString().trim().toLowerCase();
                if(container.client.isAdmin(Username.getText().toString())) {
                    startActivity(new Intent(this, Admin.class));
                }
                else {
                    startActivity(new Intent(this, User.class));
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Incorrect Username or Password! Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            startActivity(new Intent(this, Register.class));
        }
    }
}