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


public class story_text extends AppCompatActivity {
    // default text size for textview used to show story
    private static final int TEXT_SIZE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_text);
        LinearLayout linearLayout = findViewById(R.id.rootContainer);

        Intent i = getIntent();
        String cur_story = i.getStringExtra("cur_story");

        TextView story_box = new TextView(this);
        story_box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        story_box.setText(readStory(cur_story));
        story_box.setTextSize(TEXT_SIZE);

        // Add Button to LinearLayout
        if (linearLayout != null) {
            linearLayout.addView(story_box);
        }

        Button button = (Button) findViewById(R.id.return_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(story_text.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    // Return string contains contents of @param story_name. It skips the first line since the first
    // line should contain all the tags
    private String readStory(String story_name) {
        String story = "";
        try {
            InputStream inputreader = getAssets().open(story_name);
            BufferedReader buffreader = new BufferedReader(new InputStreamReader(inputreader));

            // read and discard first line of file. It only contains tags of story
            buffreader.readLine();

            String line = buffreader.readLine();
            while (line != null) {
                story += line + "\n";
                line = buffreader.readLine();
            }
            inputreader.close();
            buffreader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return story;
    }
}
