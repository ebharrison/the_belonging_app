package com.belonging.developer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class resources_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        TextView textView = findViewById(R.id.textView);

        String text = " UC DAVIS POLICE (Non-Emergency) &\n" +
                " CAMPUS ESCORT SERVICE \n 530-752-COPS(2677) \n \n CAMPUS EMERGENCY STATUS LINE\n" +
                " 530-752-4000 \n \n REPORT AND EMERGENCY \n DIAL 911 \n (From a cell, 530-752-1230) \n \n COUNSELING SERVICES \n 530-752-2349\n" +
                "\n Visit the SITE for more information";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(resources_activity.this, "success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ebeler.faculty.ucdavis.edu/resources/faq-student-resources/"));
                startActivity(intent);
            }
        };

        ss.setSpan(clickableSpan, 243, 247, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
