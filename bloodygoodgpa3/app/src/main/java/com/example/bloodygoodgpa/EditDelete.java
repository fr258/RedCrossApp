package com.example.bloodygoodgpa;

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

public class EditDelete extends AppCompatActivity {
    private ListView list;
    private List<String> courses;
    private Container container;
    private String courseCode;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editdelete);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        container = ((BaseApp)getApplication()).container;
        courseCode = null;

        list = findViewById(R.id.event_list);
        courses = container.client.getCourseArray(container.user);
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.event, courses);
        list.setAdapter(adapter);
        list.setOnItemClickListener((p, V, pos, id) -> selectCourse(pos));
    }

    public void deleteCourse(View v) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, findViewById(R.id.toast_layout_root));
        TextView text = layout.findViewById(R.id.toastText);
        if(courseCode != null) {
            if (container.client.removeCourse(container.user, courseCode)) {
                courses.remove(courseCode);
                adapter.notifyDataSetChanged();
                text.setText("Successfully deleted course.");
            }
        }
        else {
            text.setText("Must have course selected to redeem.");
        }
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void addCourse(View v) {
        startActivity(new Intent(this, AddCourse.class));
    }

    public void goBack(View v) {
        startActivity(new Intent(this, User.class));
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

}
