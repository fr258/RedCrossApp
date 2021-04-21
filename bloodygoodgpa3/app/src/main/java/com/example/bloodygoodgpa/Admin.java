package com.example.bloodygoodgpa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Admin extends AppCompatActivity implements View.OnClickListener{

    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.back);
        fab.setOnClickListener(this);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        ListView list = findViewById(R.id.event_list);

        compactCalendar = findViewById(R.id.compactcalendar_view);
        compactCalendar.showCalendarWithAnimation();
        compactCalendar.setUseThreeLetterAbbreviation(true);

        Event ev1 = new Event(Color.BLUE,1617076800000L, "Test");
        compactCalendar.addEvent(ev1);
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

    private String formattedTime(long timeInMillis) {
        long timeInDayInMinutes = timeInMillis%(24*60*60*1000)/(1000*60);
        String minutes = String.format("%02d",(int)(timeInDayInMinutes%60));
        int hoursMilitary = (int)(timeInDayInMinutes/24);
        String hours = String.format("%02d",(int)((hoursMilitary+11)%12+1));
        String period = (hoursMilitary < 11) ? "AM" : "PM";
        return hours+":"+minutes+" "+period;
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }
}