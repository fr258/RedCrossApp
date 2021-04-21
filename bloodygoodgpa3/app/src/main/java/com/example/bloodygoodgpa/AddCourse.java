package com.example.bloodygoodgpa;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddCourse extends AppCompatActivity implements View.OnClickListener{

    Button add;
    EditText code, prof;
    Container container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        code = (EditText) findViewById(R.id.code);
        prof = (EditText) findViewById(R.id.prof);

        FloatingActionButton fab = findViewById(R.id.back);
        fab.setOnClickListener(this);

        add = (Button) findViewById(R.id.add_course);
        add.setOnClickListener(this);

        container =  ((BaseApp)getApplication()).container;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_course){
            String Scode = code.getText().toString().toUpperCase().trim();
            String Sprof = prof.getText().toString().trim();
            if(!Scode.isEmpty() && !Sprof.isEmpty()) {
                //add into the db
                Boolean retVal = container.client.addCourse(container.user, Scode, Sprof);
                //toast formatting
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast, findViewById(R.id.toast_layout_root));
                TextView text = layout.findViewById(R.id.toastText);
                if (retVal == null) {
                    text.setText("Error connecting to database. Please check your connection and try again.");
                }
                else if (retVal) {
                    code.getText().clear();
                    prof.getText().clear();
                    text.setText("Successfully added new course!");
                }
                else {
                    text.setText("Course already exists in collection.");
                }
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            }

        }
        else {
            startActivity(new Intent(this, EditDelete.class));
        }
    }


}