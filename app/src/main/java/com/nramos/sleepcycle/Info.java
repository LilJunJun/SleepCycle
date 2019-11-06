package com.nramos.sleepcycle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
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

        body3.setMovementMethod(LinkMovementMethod.getInstance());
        body4.setMovementMethod(LinkMovementMethod.getInstance());
        body5.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
