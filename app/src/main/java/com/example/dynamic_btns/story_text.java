package com.example.dynamic_btns;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class story_text extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_text);

        Button button = (Button) findViewById(R.id.return_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
            }
        });
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
}
