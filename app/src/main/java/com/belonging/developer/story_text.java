package com.belonging.developer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class story_text extends AppCompatActivity implements AsyncResponse {
    // default text size for textview used to show story
    private static final int TEXT_SIZE = 20;
    private static final int PADDING = 10;

    private TextView story_box;
    private TextView title_box;
    private TextView tags_box;

    private static final int SIZE_CHANGE = 2;
    private static final int MAX_SIZE = 30;
    private static final int MIN_SIZE = TEXT_SIZE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_text);

        Intent i = getIntent();
        String curUrl = i.getStringExtra("curUrl");
        startNewAsyncTask(curUrl, -2);

        Button button = findViewById(R.id.return_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(story_text.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        Button increase = findViewById(R.id.increaseSizeButton);
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                story_box.setTextSize(Math.min(story_box.getTextSize()+SIZE_CHANGE,MAX_SIZE));
            }
        });

        Button decrease = findViewById(R.id.decreaseSizeButton);
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
        String[] storyParts = output.split("\n");

        // Format title
        title_box = findViewById(R.id.title);
//        title_box.setText(storyParts[0]);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            title_box.setText(Html.fromHtml(storyParts[0], Html.FROM_HTML_MODE_LEGACY));
        } else {
            title_box.setText(Html.fromHtml(storyParts[0]));
        }
        title_box.setTextColor(Color.BLACK);
        title_box.setTextSize(TEXT_SIZE*3/2);
        title_box.setHighlightColor(Color.BLUE);
        title_box.setPadding(PADDING, PADDING, PADDING, PADDING);
        title_box.setGravity(Gravity.CENTER);


        // Format tags
        tags_box = findViewById(R.id.tags);
//        tags_box.setText(storyParts[1]);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tags_box.setText(Html.fromHtml(storyParts[1], Html.FROM_HTML_MODE_LEGACY));
        } else {
            tags_box.setText(Html.fromHtml(storyParts[1]));
        }
        tags_box.setTextColor(Color.BLACK);
        tags_box.setTextSize(TEXT_SIZE*5/4);
        tags_box.setHighlightColor(Color.BLUE);
        tags_box.setPadding(PADDING, PADDING, PADDING, PADDING);
        tags_box.setGravity(Gravity.CENTER);

        // Format story
        story_box = findViewById(R.id.STORY);
//        story_box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//        story_box.setText(storyParts[2]);
        story_box.setTextColor(Color.BLACK);
        story_box.setTextSize(TEXT_SIZE);
        story_box.setHighlightColor(Color.BLUE);
        story_box.setPadding(PADDING, PADDING, PADDING, PADDING);
        story_box.setMovementMethod(new ScrollingMovementMethod());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            story_box.setText(Html.fromHtml(storyParts[2], Html.FROM_HTML_MODE_LEGACY));
        } else {
            story_box.setText(Html.fromHtml(storyParts[2]));
        }

//        // Add textview to LinearLayout
//        LinearLayout linearLayout = findViewById(R.id.rootContainer);
//        if (linearLayout != null) {
//            linearLayout.addView(story_box);
//        }

    }
}
