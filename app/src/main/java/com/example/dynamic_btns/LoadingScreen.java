package com.example.dynamic_btns;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoadingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeActivity();  // if there is no internet connection, this will fail

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        Button btn = (Button) findViewById(R.id.retryBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity();
            }
        });
    }

    public void changeActivity() {
        if (isConnected()) {
            Intent i = new Intent(LoadingScreen.this, MainActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.FAIL_INTERNET_CONNECT), Toast.LENGTH_SHORT).show();
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
