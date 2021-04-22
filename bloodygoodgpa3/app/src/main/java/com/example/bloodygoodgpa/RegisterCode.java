package com.example.bloodygoodgpa;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RegisterCode extends AppCompatActivity {
    private EditText code;
    private EditText pass;
    private Container container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_code);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        code = findViewById(R.id.code);
        pass = findViewById(R.id.password);

        container = ((BaseApp)getApplication()).container;

    }

    public void goBack(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void submit(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, findViewById(R.id.toast_layout_root));
        TextView text = layout.findViewById(R.id.toastText);
        if(container.client.isCorrectCode(code.getText().toString())) {
            container.client.newUser(container.user, pass.getText().toString());
            text.setText("Successfully created new user!");
        }
        else {
            text.setText("Incorrect code. Please try again.");
        }
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
