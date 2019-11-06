package com.nramos.sleepcycle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class Settings extends AppCompatActivity
{
    TextView settingsInfo;
    EditText minToFallAsleepET;
    Button saveSettingsBT;
    int minToFallAsleep;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settingsInfo = findViewById(R.id.settingsInfo);
        minToFallAsleepET = findViewById(R.id.minToSleepET);
        saveSettingsBT = findViewById(R.id.saveSettingsButton);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SharedPreferences sp = getSharedPreferences("sc_prefs", Activity.MODE_PRIVATE);
        minToFallAsleep = sp.getInt("minToFallAsleep", 14);

        minToFallAsleepET.setText(minToFallAsleep+"");

        saveSettingsBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String value = minToFallAsleepET.getText().toString();
                int minToFallAsleep;
                SharedPreferences sp = getSharedPreferences("sc_prefs", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if(value.equals(""))
                {
                    minToFallAsleep = 0;
                }
                else
                {
                    minToFallAsleep = Integer.parseInt(value);

                }
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

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
