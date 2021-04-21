package com.example.bloodygoodgpa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.List;

public class Redeem extends AppCompatActivity  {
    private Container container;
    private String courseCode;
    private ListView list;
    private List<String> courses;
    private TextView numTokens;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);
        container = ((BaseApp)getApplication()).container;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = findViewById(R.id.event_list);
        courses = container.client.getRedeemableCourses(container.user);
        list.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.event, courses));
        list.setOnItemClickListener((p, V, pos, id) -> selectCourse(pos));

        numTokens = findViewById(R.id.numCredits);
        numTokens.setText(String.format("%d",container.client.getNumTokens(container.user)));

    }

    void selectCourse(int pos) {
        int index = courses.indexOf(courseCode);
        TextView text = (TextView) list.getChildAt(index);
        if(text != null) {
            text.setBackgroundColor(Color.WHITE);
        }
        list.setSelection(pos);
        courseCode = courses.get(pos);
        text = (TextView) list.getChildAt(pos);
        if(text != null) {
            text.setBackgroundColor(Color.parseColor("#DFE6E9"));
        }
    }

    public void goBack(View v) {
        startActivity(new Intent(this, User.class));
    }

    public void redeemCredit(View v) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, findViewById(R.id.toast_layout_root));
        TextView text = layout.findViewById(R.id.toastText);
        if((courseCode == null)) {
            text.setText("Must have course selected to redeem.");
        }
        else if(Integer.parseInt(numTokens.getText().toString())<=0) {
            text.setText("Must have at least 1 token to redeem.");
        }
        else {
            if (container.client.redeem(container.user, courseCode)) {
                numTokens.setText(String.format("%d", container.client.getNumTokens(container.user)));
                text.setText("Successfully redeemed course!");
            } else {
                text.setText("Failed to redeem course.");
            }
        }
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}
