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

    private HashMap<String, ArrayList<String>> stories;
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
        stories = (HashMap<String, ArrayList<String>>) i.getSerializableExtra("story_and_tags");

        storiesAndTags = new ArrayList<>(stories.keySet());
        storiesAndTags = sort(storiesAndTags);

        // add tags, without duplicates, to list of story titles
        for (ArrayList<String> tags : stories.values()) {
            for (String tag : tags) {
                if (!storiesAndTags.contains(tag)) storiesAndTags.add(tag);
            }
        }

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
                            new_list.retainAll(stories.get(word));
                    new_list.addAll(stories.keySet());
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
                    i.putExtra("cur_story", user_choice);
                    startActivity(i);
                }
            }
        });
    }

    // return true if item is a tag for stories
    private boolean isTag(String item) {
        return stories.keySet().contains(item);
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