package com.example.dynamic_btns;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.DynamicLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    private LinearLayout linearLayout;

    // used for searching both tags and stories
    // hashmap connects the tags to the stories
    // key: story title
    // value: tags associated with story
    private HashMap<String, String[]> storiesAndTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button search_btn = (Button) findViewById(R.id.search_button);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, search_bar_activity.class);
                i.putExtra("story_and_tags", storiesAndTags);
                startActivity(i);
            }
        });

        // set linearlayout for dynamically created buttons
        linearLayout = findViewById(R.id.rootContainer);

        //read in all story titles
        storiesAndTags = processStoryTags("story_titles.txt");
        if (storiesAndTags.size() == 0) {
            System.out.println("Error: No stories named in list file.");
            System.exit(1);
        }

        // For each story, make a button, whose text is the title, and changes the text to be
        // the story
        for (String story : storiesAndTags.keySet()) {
            addBtn(story);
            System.out.print("Tags for " + story + " are:");
            for (String tag : storiesAndTags.get(story)) {
                System.out.print(tag + ", ");
            }
            System.out.println();
        }
    }

    // Given @filename, this returns an arraylist with the contents of @filename
    // Each element in returned list is one line in file
    private HashMap<String, String[]> processStoryTags(String filename) {
        checkFileExistsInAssets(filename);

        HashMap<String, String[]> storiesAndTags = new HashMap<String, String[]>();
        try {
            InputStream inputreader = getAssets().open(filename);
            BufferedReader buffreader = new BufferedReader(new InputStreamReader(inputreader));

            String story_title = buffreader.readLine();
            while (story_title != null) {
                //add story and tags to hashmap
                storiesAndTags.put(story_title, getTagsFor(story_title));

                story_title = buffreader.readLine();
            }

            inputreader.close();
            buffreader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return storiesAndTags;
    }

    private String[] getTagsFor(String story_title) {
        checkFileExistsInAssets(story_title);

        String[] tags = null;
        try {
            InputStream inputreader = getAssets().open(story_title);
            BufferedReader buffreader = new BufferedReader(new InputStreamReader(inputreader));

            String allTags = buffreader.readLine();
            tags = allTags.split(",");
            for (int i = 0; i < tags.length; i++) {
                tags[i] = tags[i].trim();
            }

            inputreader.close();
            buffreader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tags;
    }

    // Automatically adds a button the current view. It's dimensions match the layout params in the
    // xml file
    private void addBtn(final String filename) {
        checkFileExistsInAssets(filename);

        // Create Button Dynamically
        Button btnShow = new Button(this);
        btnShow.setText(filename);
        btnShow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_story_text);
                Intent i = new Intent(MainActivity.this, story_text.class);
                i.putExtra("cur_story", filename);
                startActivity(i);
            }
        });

        // Add Button to LinearLayout
        if (linearLayout != null) {
            linearLayout.addView(btnShow);
        }
    }

    private void checkFileExistsInAssets(String filename) {
        AssetManager mg = getResources().getAssets();
        InputStream is = null;
        try {
            // File exists since it was able to open an input stream
            is = mg.open(filename);
            is.close();
        } catch (IOException ex) {
            System.out.println("Error: " + filename + " was named in the list file, but was not found in" +
                    " the assets folder.");
            System.exit(1);
        }
    }
}
