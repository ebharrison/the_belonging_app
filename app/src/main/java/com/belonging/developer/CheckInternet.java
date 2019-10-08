package com.belonging.developer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CheckInternet extends AppCompatActivity {
    private Toast toast;
    private String websiteToPing = "github.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeActivity();  // if there is no internet connection, this will do nothing. The rest of
        // the code block runs

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_internet);

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
            // if there is internet, proceed to app
            Intent i = new Intent(CheckInternet.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        } else {
            // no internet connection, show error message
            if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
                toast = Toast.makeText(CheckInternet.this, "Internet connection failed",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    // check for internet connection by pinging website
    public boolean isConnected() {
        try {
            // ping github because it holds all the stories. If we can't reach github, we have a
            // problem
            final String command = "ping -c 1 " + websiteToPing;
            return Runtime.getRuntime().exec(command).waitFor() == 0;
        } catch (Exception e) {
        }

        return false;
    }
}