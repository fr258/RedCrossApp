package com.example.bloodygoodgpa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Admin extends AppCompatActivity{

    private CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    private Container container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        container = ((BaseApp)getApplication()).container;

        ListView list = findViewById(R.id.event_list);

        compactCalendar = findViewById(R.id.compactcalendar_view);
        compactCalendar.showCalendarWithAnimation();
        compactCalendar.setUseThreeLetterAbbreviation(true);
        ArrayList<String> eventsString = container.client.getEventArray();
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

    private Event convert(String event) {
        String[] arr = event.split("\\|");
        long timeInMillis = Long.parseLong(arr[0]);
        String description = arr[2];
        return new Event(Color.WHITE, timeInMillis, description);
    }

    private String formattedTime(long timeInMillis) {
        Date temp = new Date(timeInMillis);
        SimpleDateFormat dateFormatTime = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return dateFormatTime.format(temp);
    }

    public void goBack(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void manageEvents(View view) {
        startActivity(new Intent(this, ManageEvents.class));
    }
}