package com.nramos.sleepcycle;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;

import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.thekhaeng.pushdownanim.PushDownAnim;
import com.tomer.fadingtextview.FadingTextView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener
{
    private static final String TAG = "MainActivity";
    TextView viewAlarms;
    Button sleepByBtn, sleepNowBtn, awakeByBtn;
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

        //region initialize
        sleepByBtn = findViewById(R.id.sleepByBtn);
        sleepNowBtn = findViewById(R.id.sleepNowBtn);
        awakeByBtn = findViewById(R.id.awakeByBtn);
       //endregion

        PushDownAnim.setPushDownAnimTo(sleepNowBtn)
        .setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick( View view ){
                goToActivity(CyclePickerActivity.class);
            }

        } );

        PushDownAnim.setPushDownAnimTo(awakeByBtn)
                .setOnClickListener( new View.OnClickListener(){
                    @Override
                    public void onClick( View view ){
                        typePicked = "Awake By";
                        DialogFragment timePicker = new TimePickerFragment();
                        timePicker.show(getSupportFragmentManager(), "time picker");
                    }

                } );

        PushDownAnim.setPushDownAnimTo(sleepByBtn)
                .setOnClickListener( new View.OnClickListener(){
                    @Override
                    public void onClick( View view ){
                        typePicked = "Sleep By";
                        DialogFragment timePicker = new TimePickerFragment();
                        timePicker.show(getSupportFragmentManager(), "time picker");
                    }

                } );

        //region old button no animation
        /*
        sleepNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                goToActivity(CyclePickerActivity.class);
            }
        });



        awakeByBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                typePicked = "Awake By";
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        sleepByBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                typePicked = "Sleep By";
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
         */
        //endregion



    }//end of oncreate



    public void goToActivity(Class activity)
    {
        startActivity(new Intent(MainActivity.this, activity));
    }

    @Override
    public void onBackPressed() {

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
