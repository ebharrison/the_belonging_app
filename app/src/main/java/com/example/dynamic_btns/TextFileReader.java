package com.example.dynamic_btns;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class TextFileReader extends AsyncTask<String, Void, String> {
    private String TextHolder = "", TextHolder2 = "";
    private URL url;
    private BufferedReader bufferReader;

    public AsyncResponse delegate = null;

    @Override
    protected String doInBackground(String... params) {
        try {
            url = new URL(params[0]);
            bufferReader = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((TextHolder2 = bufferReader.readLine()) != null) {
                TextHolder += TextHolder2 + "\n";
            }
            bufferReader.close();

        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
            TextHolder = malformedURLException.toString();

        } catch (IOException iOException) {
            iOException.printStackTrace();

            TextHolder = iOException.toString();
        }

        return TextHolder;

    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

}
