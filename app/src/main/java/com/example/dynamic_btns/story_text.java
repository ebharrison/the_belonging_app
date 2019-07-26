package com.example.dynamic_btns;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class story_text extends AppCompatActivity implements AsyncResponse {
    // default text size for textview used to show story
    private static final int TEXT_SIZE = 20;
    private TextView story_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_text);

        Intent i = getIntent();
        String curUrl = i.getStringExtra("curUrl");
        startNewAsyncTask(curUrl);

        Button button = (Button) findViewById(R.id.return_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(story_text.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    public void startNewAsyncTask(String url) {
        TextFileReader asyncTask = new TextFileReader();
        //use this to set delegate/listener back to this class
        asyncTask.delegate = story_text.this;
        asyncTask.execute(url);
    }

    public void processFinish(String output) {
        story_box = new TextView(this);
        story_box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        story_box.setText(output);

        // Add textview to LinearLayout
        LinearLayout linearLayout = findViewById(R.id.rootContainer);
        if (linearLayout != null) {
            linearLayout.addView(story_box);
        }
    }
}
