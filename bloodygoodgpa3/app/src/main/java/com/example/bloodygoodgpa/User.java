package com.example.bloodygoodgpa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class User extends AppCompatActivity implements View.OnClickListener{
    private CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    private Button Redeem;
    private ArrayList<String> eventsString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Redeem = findViewById(R.id. Redeem);
        Redeem.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.back);
        fab.setOnClickListener(this);
        FloatingActionButton add_course = findViewById(R.id.Edit_Course);
        add_course.setOnClickListener(this);
        ListView list = findViewById(R.id.event_list);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Event Calendar");

        compactCalendar = findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.showCalendarWithAnimation();

        Container container = ((BaseApp) getApplication()).container;

        eventsString = container.client.getEventArray();
        ArrayList<Event> eventsConverted = new ArrayList<>();
        for(String a: eventsString) {
            eventsConverted.add(convert(a));
        }
        compactCalendar.addEvents(eventsConverted);
        Context currContext = getApplicationContext();
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendar.getEvents(dateClicked);
                ArrayList<String> eventStrings = new ArrayList<>();
                if(!events.isEmpty()) {
                    for(Event event: events)
                        eventStrings.add(event.getData()+" at "+formattedTime(event.getTimeInMillis()));
                }
                else {
                    eventStrings.add("No Events Planned for that day");
                }
                list.setAdapter(new ArrayAdapter<>(currContext, R.layout.event, eventStrings));
            }
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Edit_Course){
            startActivity(new Intent(this, EditDelete.class));
        }
        else if (v.getId() == R.id.Redeem) {
            startActivity(new Intent(this, Redeem.class));
        }
        else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private String formattedTime(long timeInMillis) {
        Date temp = new Date(timeInMillis);
        SimpleDateFormat dateFormatTime = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return dateFormatTime.format(temp);

    }

    private Event convert(String event) {
        String[] arr = event.split("\\|");
        long timeInMillis = Long.parseLong(arr[0]);
        String description = arr[2];
        return new Event(Color.WHITE, timeInMillis, description);
    }


}
