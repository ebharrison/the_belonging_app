package com.belonging.developer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * IMPORTANT! STORY TITLE PRECEEDS TAGS IN EACH STORY FILE
 */

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    private static final String TAG_DELIMITER = ",";

    // constant set to name of file that contains the list of all stories to be read
    private static final String STORY_LIST = "index";

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
    private String[] allStoryUrl = null;
    private int indexUrl = 0;

    private boolean hasStoryListBeenRead = false;
    private int asyncTaskRunning = 0;

    private Button searchBtn;
    private ProgressBar loadingSpinner;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> storyTitles = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBtn = findViewById(R.id.search_button);

        // only show search button when done loading
        searchBtn.setVisibility(View.INVISIBLE);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // change to search activity
                Intent i = new Intent(MainActivity.this, search_bar_activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // pass data to search activity class
                // here we pass the hashtable of each story with a specific tag, and each story and
                // it's url. We used the first one to search for stories via their tags, and the
                // second when a user clicks on the story to view (the search activity passes it to
                // the story class)
                Bundle extras = new Bundle();
                extras.putSerializable("STORIES_AND_TAGS", storiesAndTags);
                extras.putSerializable("STORIES_AND_URLS", storyToUrl);
                i.putExtras(extras);

                startActivity(i);
            }
        });

        listView = findViewById(R.id.storyList);  // main list to display all the stories
        // set listview so when a story is clicked, it takes you to the story page
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String user_choice = adapter.getItem(position);

                Intent i = new Intent(MainActivity.this, story_text.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("curUrl", storyToUrl.get(user_choice));
                startActivity(i);
            }
        });

        loadingSpinner = findViewById(R.id.progressBar);

        // connect to internet and load stories
        // we begin by reading the index file, the file with all the story titles
        // STORY_LIST is a static url for the the github file with the file names. This has to be
        // hard coded.
        // we pass 0 to read the whole file
        startNewAsyncTask(STORY_LIST, 0);
    }

    public void startNewAsyncTask(String url, int num_lines) {
        asyncTaskRunning++;  // for every running asyncTask, increase the counter to keep track of
        // the total running

        // instatiate a new async task
        TextFileReader asyncTask = new TextFileReader();

        // use this to set delegate/listener back to this class, main activity, so when task finished,
        // it uses the processFinish for this class
        asyncTask.delegate = MainActivity.this;

        // need to cast num_lines to string since async task takes multiple string arg
        asyncTask.execute(url, num_lines + "");
    }

    public void processFinish(String output) {
        asyncTaskRunning--;

        fileContents = output.split("\n");
        if (!hasStoryListBeenRead) {  // special boolean flag to if block only runs once, when this
            // function is first called

            // every call to this function from here on is only reading actual stories

            processStories();
            hasStoryListBeenRead = true;
        } else {
            readStoryData();
        }

        checkProgressAsyncTasks();
    }

    // Returns hashmap with tags as keys, and list of stories with the tag as the value
    // the filename must be a list of urls to the corresponding stories
    private void processStories() {
        // fileContents now contains the url of all current stories

        //copy the urls into a new array since we will use the async task again and it will save
        //the new data to fileContents
        allStoryUrl = fileContents;

        // for every file name, read the file on github
        for (String storyUrl : allStoryUrl) {
            //read first two lines of every file to extract the tags and story title
            startNewAsyncTask(storyUrl, 2);
        }
    }

    private void readStoryData() {
        //fileContents now contain first two lines of present story to be handled
        //first line is the title, second line is tags
        //add story and tags to hashmap

        // need static counter for index of which url is currently being read because yeah coding
        // this was the best working solution i could come up with
        String cur_url = allStoryUrl[indexUrl++];

        try {
            // first analyze the tags for the story
            for (String tag : fileContents[1].split(TAG_DELIMITER)) {
                tag = tag.trim();  // clean each tag

                // if hashmap doesn't contain tag, add tag and initalize array
                if (!storiesAndTags.containsKey(tag))
                    storiesAndTags.put(tag, new ArrayList<String>());

                // for every tag in the hashmap, it corresponds to an arraylist of stories with said
                // tag
                storiesAndTags.get(tag).add(fileContents[0]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Check file of list of stories");
            e.printStackTrace();
        }

        // build hash map of story title -> story url
        storyToUrl.put(fileContents[0], cur_url);
    }

    public void checkProgressAsyncTasks() {
        // check for all async tasks finished
        if (asyncTaskRunning == 0) {  // true if no tasks running

            searchBtn.setVisibility(View.VISIBLE);
            loadingSpinner.setVisibility(View.INVISIBLE);

            storyTitles.addAll(storyToUrl.keySet());
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, storyTitles);
            listView.setAdapter(adapter);
        }
    }
}
