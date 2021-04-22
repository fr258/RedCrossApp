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

import java.util.ArrayList;
import java.util.List;

public class ManageEvents extends AppCompatActivity {
    private ListView list;
    private Container container;
    private String courseCode;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> eventString;
    private ArrayList<String> eventStringF;
    private int position;
    private String currEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_events);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        container = ((BaseApp)getApplication()).container;
        courseCode = null;

        position = 0;

        list = findViewById(R.id.event_list);
        eventString = container.client.getEventArray();
        eventStringF = new ArrayList<>();
        for(String item: eventString) {
            eventStringF.add(item.substring(item.indexOf('|')+1));
        }
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.event, eventStringF);
        list.setAdapter(adapter);
        list.setOnItemClickListener((p, V, pos, id) -> selectEvent(pos));
    }

    public void deleteEvent(View v) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, findViewById(R.id.toast_layout_root));
        TextView text = layout.findViewById(R.id.toastText);
        if(currEvent != null) {
            container.client.removeEvent(currEvent);
            eventString.remove(currEvent);
            eventStringF.remove(currEvent.substring(currEvent.indexOf('|')+1));
            adapter.notifyDataSetChanged();
            text.setText("Successfully deleted event.");
        }
        else {
            text.setText("Must have event selected to redeem.");
        }
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void addEvent(View v) {
        startActivity(new Intent(this, AddEvent.class));
    }

    public void goBack(View v) {
        startActivity(new Intent(this, Admin.class));
    }

    void selectEvent(int pos) {
        TextView text = (TextView) list.getChildAt(position);
        position = pos;
        if(text != null) {
            currEvent = eventString.get(pos);
            text.setBackgroundColor(Color.WHITE);
        }
        list.setSelection(pos);

        text = (TextView) list.getChildAt(pos);
        if(text != null) {
            text.setBackgroundColor(Color.parseColor("#DFE6E9"));
        }

    }

}
