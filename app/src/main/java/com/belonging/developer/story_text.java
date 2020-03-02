package com.belonging.developer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.lang.Math.*;


public class story_text extends AppCompatActivity implements AsyncResponse {
    // default text size for textview used to show story
    private static final int TEXT_SIZE = 20;
    private TextView story_box;

    private static final int SIZE_CHANGE = 2;
    private static final int MAX_SIZE = 30;
    private static final int MIN_SIZE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_text);

        Intent i = getIntent();
        String curUrl = i.getStringExtra("curUrl");
        startNewAsyncTask(curUrl, -2);

        Button button = (Button) findViewById(R.id.return_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(story_text.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        Button increase = (Button) findViewById(R.id.increaseSizeButton);
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                story_box.setTextSize(Math.min(story_box.getTextSize()+SIZE_CHANGE,MAX_SIZE));
            }
        });

        Button decrease = (Button) findViewById(R.id.decreaseSizeButton);
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                story_box.setTextSize(Math.max(story_box.getTextSize()-SIZE_CHANGE,MIN_SIZE));
            }
        });
    }

    public void startNewAsyncTask(String url, int lines) {
        TextFileReader asyncTask = new TextFileReader();
        //use this to set delegate/listener back to this class
        asyncTask.delegate = story_text.this;
        asyncTask.execute(url, lines + "");
    }

    public void processFinish(String output) {
        story_box = new TextView(this);
        story_box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        story_box.setText(output);

        story_box.setTextColor(Color.BLACK);
        story_box.setTextSize(TEXT_SIZE);
        story_box.setHighlightColor(Color.BLUE);
        int padding = 10;
        story_box.setPadding(padding, padding, padding, padding);
        story_box.setMovementMethod(new ScrollingMovementMethod());

        // Add textview to LinearLayout
        LinearLayout linearLayout = findViewById(R.id.rootContainer);
        if (linearLayout != null) {
            linearLayout.addView(story_box);
        }
    }
}
