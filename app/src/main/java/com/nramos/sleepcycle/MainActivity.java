package com.nramos.sleepcycle;

import android.animation.Animator;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tomer.fadingtextview.FadingTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.util.concurrent.TimeUnit.MINUTES;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener
{
    private static final String TAG = "MainActivity";
    TextView viewAlarms;
    FloatingActionButton fabMain, fabSleepBy, fabWakeBy, fabSleepNow;
    LinearLayout fabLayout1, fabLayout2, fabLayout3;
    View fabBGLayout;
    boolean isFABOpen=false;
    Boolean floatTV;
    FadingTextView infoTextview;
    ArrayList <Date> listOfSleepCycles;
    String typePicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewAlarms = findViewById(R.id.viewAlarmsTextview);
        viewAlarms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmClock.ACTION_SHOW_ALARMS)
                        .putExtra(AlarmClock.EXTRA_SKIP_UI, false);
                startActivity(intent);
            }
        });

        listOfSleepCycles = new ArrayList<>();
        infoTextview = findViewById(R.id.fadingTextView);

        //region Floating Action Button
        fabMain = findViewById(R.id.fabMain);
        fabSleepBy = findViewById(R.id.fabSleepBy);
        fabWakeBy = findViewById(R.id.fabAwakeBy);
        fabSleepNow = findViewById(R.id.fabSleepNow);
        fabLayout1= findViewById(R.id.fabLayout1);
        fabLayout2= findViewById(R.id.fabLayout2);
        fabLayout3= findViewById(R.id.fabLayout3);
        fabBGLayout=findViewById(R.id.fabBGLayout);

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else
                {
                    closeFABMenu();
                }
            }
        });

        fabBGLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });
        //endregion

    }//end of oncreate


    private void showFABMenu()
    {
        infoTextview.pause();
        infoTextview.setVisibility(View.INVISIBLE);

        isFABOpen=true;
        fabLayout1.setVisibility(View.VISIBLE);
        fabLayout2.setVisibility(View.VISIBLE);
        fabLayout3.setVisibility(View.VISIBLE);
        fabBGLayout.setVisibility(View.VISIBLE);

        fabMain.animate().rotationBy(225).setListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                if (fabMain.getRotation() == 180) {
                    fabMain.setRotation(225);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        fabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        fabLayout3.animate().translationY(-getResources().getDimension(R.dimen.standard_145));

        //region FAB Actions

        fabSleepNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                goToActivity(CyclePickerActivity.class);
            }
        });

        fabWakeBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                typePicked = "Awake By";
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        fabSleepBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                typePicked = "Sleep By";
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });


        //endregion

    }

    public void goToActivity(Class activity)
    {
        startActivity(new Intent(MainActivity.this, activity));
    }

    private void closeFABMenu()
    {
        infoTextview.resume();
        infoTextview.setVisibility(View.VISIBLE);

        isFABOpen=false;
        fabBGLayout.setVisibility(View.GONE);
        fabMain.animate().rotationBy(225);
        fabLayout1.animate().translationY(0);
        fabLayout2.animate().translationY(0);
        fabLayout3.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!isFABOpen){
                    fabLayout1.setVisibility(View.GONE);
                    fabLayout2.setVisibility(View.GONE);
                    fabLayout3.setVisibility(View.GONE);
                }
                if (fabMain.getRotation() == 225) {
                    fabMain.setRotation(180);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(!isFABOpen){
            super.onBackPressed();
        }else{
            closeFABMenu();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        Log.d(TAG, "Hour Picked " + hourOfDay + " Minute Picked " + minute);
        Intent pickerToCycleActivityIntent = new Intent(view.getContext(), CyclePickerActivity.class);
        pickerToCycleActivityIntent.putExtra("typePicked", typePicked);
        pickerToCycleActivityIntent.putExtra("hourOfDayPicked", hourOfDay);
        pickerToCycleActivityIntent.putExtra("minutePicked", minute);
        startActivity(pickerToCycleActivityIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info)
        {
            Intent goToInfo = new Intent(this, Info.class);
            startActivity(goToInfo);
            return true;
        }
        if (id == R.id.action_help)
        {
            Intent goToHelp = new Intent(this, Help.class);
            startActivity(goToHelp);
            return true;
        }
        if (id == R.id.action_settings)
        {
            Intent goToSettings = new Intent(this, Settings.class);
            startActivity(goToSettings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
