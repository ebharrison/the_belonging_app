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
    // constant set to name of file that contains the list of all stories to be read
    private static final String STORY_LIST_FILE = "story_titles.txt";

    private LinearLayout linearLayout;
    // used for searching both tags and stories
    // hashmap connects the tags to the stories
    // key: tag
    // value: stories with @key tag
    private HashMap<String, ArrayList<String>> storiesAndTags;

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

        //todo:rewrite for async task
        //read in all story titles
        storiesAndTags = processStoryTags(STORY_LIST_FILE);
    }

    // Given @filename, this returns an arraylist with the contents of @filename
    // Each element in returned list is one line in file
    private HashMap<String, ArrayList<String>> processStoryTags(String filename) {
        checkFileExistsInAssets(filename);

        HashMap<String, ArrayList<String>> storiesAndTags = new HashMap<String, ArrayList<String>>();
        try {
            /**todo
             * alter this method to read from url
             * for async task pass 2 param
             * first is url
             * second is num lines to read
             * second param is good so i can read 2 lines for tags and title, and stop thread
             * ie if 0 read whole file, if 2, read 2 lines
             * also have to do error check for param in async class
             *
             * i think i should also make a second hashmap with storytitle -> url for story_text.java
             */

            InputStream inputreader = getAssets().open(filename);
            BufferedReader buffreader = new BufferedReader(new InputStreamReader(inputreader));

            String story_title = buffreader.readLine();
            while (story_title != null) {
                //add story and tags to hashmap
                for (String tag : getTagsFor(story_title)) {
                    // if hashmap doesn't contain tag, add tag and initalize array
                    if (!storiesAndTags.containsKey(tag))
                        storiesAndTags.put(tag, new ArrayList<String>());

                    storiesAndTags.get(tag).add(story_title);
                }
                // while looping over all story titles, make button for each story
                addBtn(story_title);

                story_title = buffreader.readLine();
            }

            inputreader.close();
            buffreader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return storiesAndTags;
    }

    // @return an array with each element corresponding to a tag for @param
    private String[] getTagsFor(String story_title) {
        checkFileExistsInAssets(story_title);

        String[] tags = null;
        try {
            //todo rewrite since this also reads files
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

    //todo comment out checkFileExistsInAssets

    // Determine if @param filename exists in the assets folder. If it does, the program will
    // proceed. If it does not exist, an exception is thrown
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

    public boolean isConnected() {
        try {
            final String command = "ping -c 1 google.com";
            return Runtime.getRuntime().exec(command).waitFor() == 0;
        } catch (Exception e) {

        }
        return false;
    }
}
