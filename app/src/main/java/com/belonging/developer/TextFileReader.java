package com.belonging.developer;

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

            /**
             * If no second parameter is given to the reader, it will, by default, read the whole
             * file contents
             */
            if (params.length == 1) {  //only url given. No specified number of lines to read
                readFile();
                return TextHolder;
            }
            // now we now an argument must exist for params[1]

            int readLines;
            try {
                readLines = Integer.parseInt(params[1]);

                if (readLines > 0) {
                    // num of lines to read is positive, so read readLines amount of lines and return
                    for (int i = 0; i < readLines; i++) {
                        TextHolder2 = bufferReader.readLine();
                        TextHolder += TextHolder2 + "\n";
                    }
                } else {
                    //if negative number, or 0, then we can  discard the first readLines amount of
                    //lines
                    for (int i = 0; i < Math.abs(readLines); i++) {
                        TextHolder2 = bufferReader.readLine();
                    }

                    // read what's left of the file (if it hasn't all been discarded. If nothing is left,
                    // then it will return null)
                    readFile();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid string: " + params[1] + " given to TextFileReader");
                System.exit(1);
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

    private void readFile() {
        try {
            TextHolder2 = bufferReader.readLine();
            while (TextHolder2 != null) {
                TextHolder += TextHolder2 + "\n";
                TextHolder2 = bufferReader.readLine();
            }
        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
            TextHolder = malformedURLException.toString();
        } catch (IOException iOException) {
            iOException.printStackTrace();
            TextHolder = iOException.toString();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

}
