package com.nramos.sleepcycle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class Settings extends AppCompatActivity
{
    SwitchCompat floatingTVSwitch;
    Boolean floatTV;
    EditText minToFallAsleepET;
    Button saveSettingsBT;
    int minToFallAsleep;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        floatingTVSwitch = findViewById(R.id.disableTextviewSwitch);
        minToFallAsleepET = findViewById(R.id.minToSleepET);
        saveSettingsBT = findViewById(R.id.saveSettingsButton);

        SharedPreferences sp = getSharedPreferences("sc_prefs", Activity.MODE_PRIVATE);
        floatTV = sp.getBoolean("switch", true);
        minToFallAsleep = sp.getInt("minToFallAsleep", 14);

        minToFallAsleepET.setText(minToFallAsleep+"");

        if(floatTV)
        {
            floatingTVSwitch.setChecked(true);
        }
        else
        {
            floatingTVSwitch.setChecked(false);
        }

        floatingTVSwitch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               floatTV = floatingTVSwitch.isChecked();
               SharedPreferences sp = getSharedPreferences("sc_prefs", Activity.MODE_PRIVATE);
               SharedPreferences.Editor editor = sp.edit();
               editor.putBoolean("switch", floatTV);
               editor.apply();
           }
       });

        saveSettingsBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String value = minToFallAsleepET.getText().toString();
                int minToFallAsleep = Integer.parseInt(value);

                SharedPreferences sp = getSharedPreferences("sc_prefs", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("minToFallAsleep", minToFallAsleep);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        String value = minToFallAsleepET.getText().toString();
        int minToFallAsleep = Integer.parseInt(value);

        SharedPreferences sp = getSharedPreferences("sc_prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("minToFallAsleep", minToFallAsleep);
        editor.apply();

        floatTV = floatingTVSwitch.isChecked();
        editor.putBoolean("switch", floatTV);
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
