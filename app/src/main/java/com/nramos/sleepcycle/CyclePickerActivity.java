package com.nramos.sleepcycle;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.AlarmClock;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CyclePickerActivity extends AppCompatActivity
{
    private static final String TAG = "CyclePickerActivity";
    ArrayList <Cycle> listOfSleepCycles;
    ArrayList <String> listOfCycleTimes;
    Calendar c;
    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm aa");
    String typePicked;
    int hPicked, mPicked, hourReminder, minuteReminder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cyclepicker);

        ListView list = findViewById(R.id.cycleList);
        TextView sleepAtTextView = findViewById(R.id.sleepAtTextView);
        TextView wakeUpAtTextView = findViewById(R.id.wakeUpAtTextView);
        listOfSleepCycles = new ArrayList<>();
        listOfCycleTimes = new ArrayList<>();
        c = Calendar.getInstance();

        int hourOfDayPicked, minutePicked;

        if (savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();
            if(extras == null)
            {
                String currentTime = timeFormat.format(c.getTime());
                sleepAtTextView.setText("Sleep At: " + currentTime);
                typePicked = "Sleep Now";
            }
            else
            {
                typePicked = extras.getString("typePicked");
                hourOfDayPicked= extras.getInt("hourOfDayPicked");
                minutePicked= extras.getInt("minutePicked");
                c.set(Calendar.HOUR_OF_DAY, hourOfDayPicked);
                c.set(Calendar.MINUTE, minutePicked);
                hPicked = c.get(Calendar.HOUR_OF_DAY);//store current time
                mPicked = c.get(Calendar.MINUTE);//store current time

                String timePicked = timeFormat.format(c.getTime());
                if(typePicked.equals("Awake By"))
                {
                    sleepAtTextView.setText("Sleep At: ");
                    wakeUpAtTextView.setText("Wake Up At: " + timePicked);
                }
                else if(typePicked.equals("Sleep By"))
                {
                    sleepAtTextView.setText("Sleep At: " + timePicked);
                    wakeUpAtTextView.setText("Wake Up At: ");
                }

                Log.d(TAG, "time passed? getString " + hourOfDayPicked + ":" + minutePicked);
            }
        }

        createSleepCycles();

        Log.d(TAG, "List of Sleep Cycles \n");

        CycleListAdapter adapter = new CycleListAdapter(this, R.layout.adapter_view_layout, listOfSleepCycles);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
            {
                LinearLayout adapterViewLayout = view.findViewById(R.id.adapterLinearLayout);
                TextView cycleTimeTextView = adapterViewLayout.findViewById(R.id.cycleTimeTextView);
                String cycleTime = cycleTimeTextView.getText().toString();
                Log.d(TAG, "item selected: " + cycleTime);
                //extract hour and minute from cycleTime String using SDF to pass to createAlarm():
                try
                {
                    Date date = timeFormat.parse(cycleTime);
                    Log.d(TAG, "item selected date " + date);
                    c.setTime(date);
                    Log.d(TAG, "item selected date set " + c.getTime());
                    String alarmMessage;
                    if(typePicked.equals("Awake By"))
                    {
                        alarmMessage = "(SC) Time for bed";
                        Log.d(TAG, "Wake Up " + hPicked + " " + mPicked);
                       // createAlarm("Wake Up! (SC)", hPicked, mPicked);
                    }
                    else if(typePicked.equals("Sleep By"))
                    {
                        alarmMessage = "(SC) Wake Up!";
                    }
                    else
                    {
                        alarmMessage = "(SC) Wake Up!";
                    }
                    hourReminder = c.get(Calendar.HOUR_OF_DAY);
                    minuteReminder = c.get(Calendar.MINUTE);
                    //we use HOUR_OF_DAY to specify AM/PM for Alarm Clock
                    createAlarm(alarmMessage, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));

                } catch (ParseException e)
                {
                    Toast.makeText(getApplicationContext(), "Unable to Create Alarm. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createSleepCycles()
    {
        int i, cycleHour = 1, cycleMinute = 30;

        SharedPreferences sp = getSharedPreferences("sc_prefs", Activity.MODE_PRIVATE);
        int minToSleep = sp.getInt("minToFallAsleep", 14);
        if(typePicked.equals("Awake By"))
        {
            for(i = 1; i < 10; i++)
            {
                if (i == 1)
                {
                    c.add(Calendar.HOUR_OF_DAY, -1);
                    c.add(Calendar.MINUTE, ((-minToSleep + -30)));
                    c.getTime();
                }
                else
                {
                    //keep adding 1h 30m (14mins is already taken into account above with start off)
                    c.add(Calendar.HOUR_OF_DAY, -1);
                    c.add(Calendar.MINUTE,  -30);
                    c.getTime();
                }
                Cycle cycle = new Cycle(c.getTime(), i+"", cycleHour, cycleMinute);
                listOfSleepCycles.add(cycle);
            }
        }
        else
        {
            //Sleep By/SleepNow
            for(i = 1; i < 10; i++)
            {
                if (i == 1)
                {
                    //start off
                    c.add(Calendar.HOUR_OF_DAY, cycleHour);
                    c.add(Calendar.MINUTE, minToSleep + 30);
                    c.getTime();
                }
                else
                {   //keep adding 1h 30mins (14mins is already taken into account above with start off)
                    c.add(Calendar.HOUR_OF_DAY, 1);
                    c.add(Calendar.MINUTE,  30);
                    c.getTime();
                }
                //get the hour and minute separately from the time
                Cycle cycle = new Cycle(c.getTime(), i + "", cycleHour, cycleMinute);
                listOfSleepCycles.add(cycle);
            }
        }
    }

    //create reminder alarm
    public void createNotification()
    {
        new AlertDialog.Builder(this)
                .setTitle("Create Reminder Alarm")
                .setMessage("Would you like to create a reminder before alarm?")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent reminderActivityIntent = new Intent(getApplicationContext(), ReminderActivity.class);
                        reminderActivityIntent.putExtra("hourReminder", hourReminder);
                        reminderActivityIntent.putExtra("minuteReminder", minuteReminder);
                        startActivity(reminderActivityIntent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AlarmClock.ACTION_SHOW_ALARMS)
                                .putExtra(AlarmClock.EXTRA_SKIP_UI, false);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void createAlarm(String message, int hour, int minutes)
    {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);

        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
            if(!typePicked.equals("Sleep Now"))
            {
                c.set(Calendar.HOUR_OF_DAY, hPicked);
                c.set(Calendar.MINUTE, mPicked);
                String timeToDisplay = timeFormat.format(c.getTime());
                new AlertDialog.Builder(this)
                        .setTitle("Create Additional Alarm")
                        .setMessage("Would you like to set an additional alarm for " + timeToDisplay + "?")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(typePicked.equals("Awake By"))
                                {
                                    Intent secondIntent = new Intent(AlarmClock.ACTION_SET_ALARM)
                                            .putExtra(AlarmClock.EXTRA_MESSAGE, "(SC) Wake Up!")
                                            .putExtra(AlarmClock.EXTRA_HOUR, hPicked)
                                            .putExtra(AlarmClock.EXTRA_MINUTES, mPicked)
                                            .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                                    startActivity(secondIntent);
                                    createNotification();
                                }
                                else if(typePicked.equals("Sleep By"))
                                {
                                    hourReminder = hPicked;
                                    minuteReminder = mPicked;
                                    Intent secondIntent = new Intent(AlarmClock.ACTION_SET_ALARM)
                                            .putExtra(AlarmClock.EXTRA_MESSAGE, "(SC) Time for Bed")
                                            .putExtra(AlarmClock.EXTRA_HOUR, hPicked)
                                            .putExtra(AlarmClock.EXTRA_MINUTES, mPicked)
                                            .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                                    startActivity(secondIntent);
                                    createNotification();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(typePicked.equals("Awake By"))
                                {
                                    createNotification();
                                }
                                else
                                {
                                    finish();
                                }
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }  else
            {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        this.finish();
    }
}
