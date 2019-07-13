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

        linearLayout = findViewById(R.id.rootContainer);

        TextView t = (TextView) findViewById(R.id.text1);
        t.setText(readWholeFile("foo.txt"));

    }

    private ArrayList<String> readFileIntoList(String filename) {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            InputStream inputreader = getAssets().open("foo.txt");
            BufferedReader buffreader = new BufferedReader(new InputStreamReader(inputreader));

            boolean hasNextLine = true;
            String line = buffreader.readLine();
            while (line != null) {
                lines.add(line);
                line = buffreader.readLine();
            }

        } catch (IOException e) {

        }

        return lines;
    }

    private String readWholeFile(String filename) {
        AssetManager assetManager = getAssets();
        // To get names of all files inside the "Files" folder
        try {
            String[] files = assetManager.list("Files");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // To load text file
        InputStream input;
        String text = "Error: File not read";
        try {
            input = assetManager.open(filename);

            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            // byte buffer into a string
            text = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
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
