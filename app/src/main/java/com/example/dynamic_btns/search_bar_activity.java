package com.example.dynamic_btns;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;


//todo: bug if query text contains partial tag and then user selects a tag
public class search_bar_activity extends AppCompatActivity {
    private static final String TAG_DELIMITER = " ";

    // key tag
    // value list of stories with this tag
    private HashMap<String, ArrayList<String>> storiesToTags;

    // key story
    // value url
    private HashMap<String, String> storyToUrl;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> storiesAndTags;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);

        final SearchView searchView = (SearchView) findViewById(R.id.searchView);
        listView = (ListView) findViewById(R.id.myList);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        storiesToTags = (HashMap<String, ArrayList<String>>) extras.getSerializable("STORIES_AND_TAGS");
        storyToUrl = (HashMap<String, String>) extras.getSerializable("STORIES_AND_URLS");

        //add tags, since no duplicates can exist
        storiesAndTags = new ArrayList<>(storiesToTags.keySet());
        // add stories, without duplicates, to list of tags
        for (ArrayList<String> stories : storiesToTags.values()) {
            for (String story : stories) {
                if (!storiesAndTags.contains(story)) storiesAndTags.add(story);
            }
        }
        storiesAndTags = sort(storiesAndTags);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                storiesAndTags);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // this function must be implemented, but since onQueryTextChange automatically updates
            // the list based text changes, this function doesn't need to do anything
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            //leave this alone as autosuggest, then when hit submit, check multi tags
            public boolean onQueryTextChange(String newText) {
                if (containsTag(newText)) {
                    //for each valid tag, filter list on that tag
                    ArrayList<String> new_list = new ArrayList<String>(storiesAndTags);
                    for (String word : newText.split(TAG_DELIMITER))
                        if (isTag(word))
                            new_list.retainAll(storiesToTags.get(word));
                    new_list.addAll(storiesToTags.keySet());
                    setAdapterTo(new_list);
                } else {
                    setDefaultAdapter();
                    adapter.getFilter().filter(newText);
                }

                return true;
            }
        });

        // Handle and decide if a tag or a story was selected
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String user_choice = adapter.getItem(position);

                if (isTag(user_choice.trim())) {
                    searchView.setQuery(searchView.getQuery() + TAG_DELIMITER + user_choice
                            + TAG_DELIMITER, true);
                } else {
                    //user_choice is an actual story
                    Intent i = new Intent(search_bar_activity.this, story_text.class);
                    i.putExtra("curUrl", storyToUrl.get(adapter.getItem(position)));
                    startActivity(i);
                }
            }
        });
    }

    // return true if item is a tag for stories
    private boolean isTag(String item) {
        return storiesToTags.keySet().contains(item);
    }

    private boolean containsTag(String text) {
        String[] words = text.split(TAG_DELIMITER);
        for (String word : words)
            if (isTag(word)) return true;
        return false;
    }

    private void setDefaultAdapter() {
        setAdapterTo(storiesAndTags);
    }

    private void setAdapterTo(ArrayList<String> newList) {
        newList = sort(newList);
        adapter = new ArrayAdapter<String>(search_bar_activity.this,
                android.R.layout.simple_list_item_1, newList);
        listView.setAdapter(adapter);
    }

    private boolean isEmpty(ArrayAdapter<String> adapter) {
        return adapter.getItem(0) != null;
    }

    private ArrayList<String> sort(ArrayList<String> list) {
        String[] old_list = list.toArray(new String[0]);
        Arrays.sort(old_list);
        return new ArrayList<String>(Arrays.asList(old_list));
    }
}