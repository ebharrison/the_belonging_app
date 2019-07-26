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

//TODO STORY TITLE PRECEEDS TAGS

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    private static final String TAG_DELIMITER = " ";

    // constant set to name of file that contains the list of all stories to be read
    private static final String STORY_LIST_URL = "https://raw.githubusercontent.com/sensishadow818/belonging_app_story_files/master/stories/story_titles.txt?token=AKOWS3SAUZUFMBA7KUFZZ5K5IOAHK";

    private LinearLayout linearLayout;
    // used for searching both tags and stories
    // hashmap connects the tags to the stories
    // key: tag
    // value: stories with @key tag
    private HashMap<String, ArrayList<String>> storiesAndTags = new HashMap<String, ArrayList<String>>();

    //key: story title
    //value: url of story
    private HashMap<String, String> storyToUrl = new HashMap<String, String>();

    private String[] fileContents = null;
    TextFileReader asyncTask = null;
    private String cur_url = null;
    private boolean hasStoryListBeenRead = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button search_btn = (Button) findViewById(R.id.search_button);
//        search_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, search_bar_activity.class);
//                i.putExtra("story_and_tags", storiesAndTags);
//                startActivity(i);
//            }
//        });

        // set linearlayout for dynamically created buttons
//        linearLayout = findViewById(R.id.rootContainer);


        //use this to set delegate/listener back to this class


        Button button = findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (String s : fileContents) {
                    System.out.println(s + " yay");
                }
            }
        });

        startNewAsyncTask(STORY_LIST_URL);
    }

    public void startNewAsyncTask(String url) {
        asyncTask = new TextFileReader();
        asyncTask.delegate = MainActivity.this;
        asyncTask.execute(url);
    }

    public void processFinish(String output) {
        fileContents = output.split("\n");
        if (!hasStoryListBeenRead) {
            processStories();
            hasStoryListBeenRead = true;
        } else {
            readStoryData();
        }
    }

    // Returns hashmap with tags as keys, and list of stories with the tag as the value
    // the filename must be a list of urls to the corresponding stories
    private void processStories() {
        // fileContents now contains the url of all current stories

        //copy the urls into a new array since we will use the async task again and it will save
        //the new data to fileContents
        String[] allStoryUrl = fileContents;
        for (String storyUrl : allStoryUrl) {
//            System.out.println(storyUrl+" whoo hoo");
////            //read first two lines of every file to extract the tags and story title
            cur_url = storyUrl;
            startNewAsyncTask(cur_url);
        }
    }

    private void readStoryData() {
        //fileContents now contain first two lines of present story to be handled
        //first line is the title, second line is tags
        //add story and tags to hashmap

//        System.out.println(fileContents[0] + " howdy");
//        System.out.println(fileContents[1] + " howdy");

        for (String tag : fileContents[1].split(TAG_DELIMITER)) {
//                // if hashmap doesn't contain tag, add tag and initalize array
//                if (!storiesAndTags.containsKey(tag))
//                    storiesAndTags.put(tag, new ArrayList<String>());
//                storiesAndTags.get(tag).add(fileContents[0]);
        }
//
//            storyToUrl.put(fileContents[0], storyUrl);
//
//            addBtn(fileContents[0]);
    }

    // Automatically adds a button the current view. It's dimensions match the layout params in the
    // xml file
    //todo rewrite to handle story title and url
    private void addBtn(final String story_name) {

        // Create Button Dynamically
        Button btnShow = new Button(this);
        btnShow.setText(story_name);
        btnShow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        //todo for button putextra, it will use hashtable to give url of story
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, story_text.class);
                i.putExtra("curUrl", storyToUrl.get(story_name));
                startActivity(i);
            }
        });

        // Add Button to LinearLayout
        if (linearLayout != null) {
            linearLayout.addView(btnShow);
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
