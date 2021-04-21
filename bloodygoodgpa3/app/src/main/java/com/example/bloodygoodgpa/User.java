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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class User extends AppCompatActivity implements View.OnClickListener{
    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    Button Redeem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Redeem = (Button) findViewById(R.id. Redeem);
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
        actionBar.setTitle("Event Calender");

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.showCalendarWithAnimation();

        Event ev1 = new Event(Color.WHITE,1617076800000L, "Test");
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
        long timeInDayInMinutes = timeInMillis%(24*60*60*1000)/(1000*60);
        String minutes = String.format("%02d",(int)(timeInDayInMinutes%60));
        int hoursMilitary = (int)(timeInDayInMinutes/24);
        String hours = String.format("%02d",(int)((hoursMilitary+11)%12+1));
        String period = (hoursMilitary < 11) ? "AM" : "PM";
        return hours+":"+minutes+" "+period;
    }


}
