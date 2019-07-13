package com.example.dynamic_btns;

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
import java.util.Scanner;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    private ArrayList<String> story_names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set linearlayout for dynamically created buttons
        linearLayout = findViewById(R.id.rootContainer);

        //testing
        TextView t = (TextView) findViewById(R.id.text1);
        t.setText(readFile("jon.txt"));

    }

    // Given @filename, this returns an arraylist with the contents of @filename
    // Each element in returned list is one line in file
    private ArrayList<String> readFileIntoList(String filename) {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            InputStream inputreader = getAssets().open(filename);
            BufferedReader buffreader = new BufferedReader(new InputStreamReader(inputreader));

            boolean hasNextLine = true;
            String line = buffreader.readLine();
            while (line != null) {
                lines.add(line);
                line = buffreader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    // Return string contains contents of @filename
    private String readFile(String filename) {
        String lines = "";
        try {
            InputStream inputreader = getAssets().open(filename);
            BufferedReader buffreader = new BufferedReader(new InputStreamReader(inputreader));

            boolean hasNextLine = true;
            String line = buffreader.readLine();
            while (line != null) {
                lines += line + "\n";
                line = buffreader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    // Automatically adds a button the current view. It's dimensions match the layout params in the
    // xml file
    private void addBtn(String s) {
        // Create Button Dynamically
        Button btnShow = new Button(this);
        btnShow.setText(s);
        btnShow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, R.string.welcome_message, Toast.LENGTH_LONG).show();
            }
        });

        // Add Button to LinearLayout
        if (linearLayout != null) {
            linearLayout.addView(btnShow);
        }
    }
}
