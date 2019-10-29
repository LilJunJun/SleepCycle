package com.nramos.sleepcycle;

import android.content.Intent;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity
{
    int hourReminder, minuteReminder;
    TextView reminderTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        String[] options = {"5 minutes before","10 minutes before","15 minutes before","20 minutes before",
                "30 minutes before","45 minutes before","60 minutes before"};

        final ListView listView = findViewById(R.id.reminderListView);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                hourReminder = 0;
                minuteReminder = 0;
            } else {
                hourReminder= extras.getInt("hourReminder");
                minuteReminder= extras.getInt("minuteReminder");
            }
        } else
            {
                hourReminder = (int) savedInstanceState.getSerializable("hourReminder");
                minuteReminder = (int) savedInstanceState.getSerializable("minuteReminder");
        }


        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourReminder);
        c.set(Calendar.MINUTE, minuteReminder);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm aa");

        reminderTextview = findViewById(R.id.reminderTextView);
        reminderTextview.setText("Reminder for " + sdf.format(c.getTime()));

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //minutes
                int selectedFromList = Integer.parseInt(listView.getItemAtPosition(position).toString().replaceAll("[^0-9]", ""));
                int setReminderMinutes = minuteReminder - selectedFromList;
                int minutes;
                Log.d("ReminderActivity", "mPicked " + minuteReminder + " - " + selectedFromList + " selected " + " = " + setReminderMinutes);
                if(setReminderMinutes < 0)
                {
                    //negative number
                    hourReminder = hourReminder - 1;
                    minutes = 60 - Math.abs(setReminderMinutes);
                }
                else
                {
                    minutes = Math.abs(setReminderMinutes);
                }

                Intent secondIntent = new Intent(AlarmClock.ACTION_SET_ALARM)
                        .putExtra(AlarmClock.EXTRA_MESSAGE, "Reminder for Bedtime")
                        .putExtra(AlarmClock.EXTRA_HOUR, hourReminder)
                        .putExtra(AlarmClock.EXTRA_MINUTES, minutes)
                        .putExtra(AlarmClock.EXTRA_SKIP_UI, false);
                startActivity(secondIntent);
                finish();
            }
        });
    }
}
