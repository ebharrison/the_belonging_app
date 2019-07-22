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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class search_bar_activity extends AppCompatActivity {
    private HashMap<String, String[]> stories;
    private ArrayList<String> storiesAndTags;
    private ArrayAdapter<String> adapter;

    private SearchView searchView;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);

        searchView = (SearchView) findViewById(R.id.searchView);
        listView = (ListView) findViewById(R.id.myList);

        Intent i = getIntent();
        stories = (HashMap<String, String[]>) i.getSerializableExtra("story_and_tags");

        //Getting Set of keys from HashMap
        Set<String> keySet = stories.keySet();
        storiesAndTags = new ArrayList<>(keySet);

        // add tags, without duplicates, to list of story titles
        for (String[] tags : stories.values()) {
            for (String tag : tags) {
                if (!storiesAndTags.contains(tag)) storiesAndTags.add(tag);
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, storiesAndTags);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        // Handle and decide if a tag or a story was selected
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Click List Item " + adapter.getItem(position), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
