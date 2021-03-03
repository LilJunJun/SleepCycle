package com.nramos.sleepcycle;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class Info extends AppCompatActivity
{
    TextView body3, body4, body5;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        body3 = findViewById(R.id.body3);
        body4 = findViewById(R.id.body4);
        body5 = findViewById(R.id.body5);
        body3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent browser= new Intent(Intent.ACTION_VIEW, Uri.parse("https://my.clevelandclinic.org/health/articles/12148-sleep-basics"));
                startActivity(browser);
            }
        });
        body4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent browser= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sleep.org/articles/sleepwake-cycle/"));
                startActivity(browser);
            }
        });
        body5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent browser= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sleepfoundation.org/articles/what-happens-when-you-sleep"));
                startActivity(browser);
            }
        });

    }

}
