package com.example.bloodygoodgpa;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEvent extends AppCompatActivity {
    private Container container;
    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    private SimpleDateFormat dateFormatDay = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
    private TextView date;
    private EditText description;
    private EditText time;
    private Spinner spinner;
    private String zone;
    private long dateInMillis;
    private Calendar cal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        container = ((BaseApp)getApplication()).container;
        date = findViewById(R.id.date_display);
        description = findViewById(R.id.description);

        spinner = findViewById(R.id.spinner);
        String[] times = {"am", "pm"};
        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.event, times));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zone = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                zone = (String) parent.getItemAtPosition(0);
            }
        });

        time = findViewById(R.id.edit_text_time);

        compactCalendar = findViewById(R.id.compactcalendar_view);
        compactCalendar.showCalendarWithAnimation();
        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                date.setText(dateFormatDay.format(dateClicked));
                dateInMillis = dateClicked.getTime();
            }
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });


    }

    private void selectZone(int pos) {
        TextView temp = (TextView) spinner.getChildAt(pos);
        zone = temp.getText().toString();
    }

    public void addEvent(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, findViewById(R.id.toast_layout_root));
        TextView text = layout.findViewById(R.id.toastText);
        if((date.getText() != null) && (description.getText() != null)) {
            String timeStr = time.getText().toString();
            if(timeStr.contains(":")) {
                String[] timeArr = timeStr.split(":");
                if(timeArr.length == 2) {
                    try {
                        if((Integer.parseInt(timeArr[0]) <= 12) && ((Integer.parseInt(timeArr[01]) <= 59))) {
                            long timeInMillis = dateInMillis + Long.parseLong(timeArr[0])*60*60*1000 + Long.parseLong(timeArr[01])*60*1000;
                            if(zone.equals("pm")) timeInMillis += 12*60*60*1000;
                            container.client.addEvent(date.getText().toString(), description.getText().toString(), timeStr, zone, timeInMillis);
                            text.setText("Successfully added event.");
                        }
                    }
                    catch(NumberFormatException e) {
                        text.setText("invalid number.");
                    }
                }
                else {
                    text.setText("invalid number of arrays.");
                }
            }
            else {
                text.setText("no colon");
            }
        }
        else {
            text.setText("outer loop");
        }
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void goBack(View v) {
        startActivity(new Intent(this, ManageEvents.class));
    }
}
