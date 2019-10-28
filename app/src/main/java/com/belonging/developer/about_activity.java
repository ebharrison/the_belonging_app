package com.belonging.developer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class about_activity extends AppCompatActivity {
    /* Display details about the version number, people that contributed to this project*/
    private TextView mDisplayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mDisplayText = (TextView) findViewById(R.id.tv_display);
    }
}
