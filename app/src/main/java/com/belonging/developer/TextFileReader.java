package com.belonging.developer;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class TextFileReader extends AsyncTask<String, Void, String> {
    private String TextHolder = "", TextHolder2 = "";
    private BufferedReader bufferReader;
    public AsyncResponse delegate = null;


    //todo: can we make this static?
    private final String GITHUB_DOMAIN = "https://ebharrison.github.io/the_belonging_app/";
    private final String STORY_FILE_TYPE = ".html";

    @Override
    protected String doInBackground(String... params) {
        try {
            //build url from title
            URL url = buildUrlFromStoryTitle(params[0]);
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
                    // num of lines to read is positive, so read num amount of lines and return what
                    // was read
                    for (int i = 0; i < readLines; i++) {
                        TextHolder2 = bufferReader.readLine();
                        TextHolder += TextHolder2 + "\n";
                    }
                } else {
                    // if negative number, or 0, then we can  discard the first num amount of
                    // lines
                    // if 0, then no lines discarded :)
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

        int start = TextHolder.indexOf("<body>")+("<body>".length());
        int end = TextHolder.indexOf("</body>");
        String clean = TextHolder.substring(start,end).trim();
        System.out.println("ASYNC:"+clean);
        if(isAlphaNumeric(clean)) {
            return clean;
        } else {
            return "";
        }
    }

    public static boolean isAlphaNumeric(String s) {
        return s != null && !s.matches("^[<>]*$");
    }

    // read entire contents of the file given in the above code block
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


    /**
     * @param storyTitle is name of story to be read. It matches the file name on github
     * @return URL object with url for story file
     */
    protected URL buildUrlFromStoryTitle(String storyTitle) {
        URL returnURL = null;
        String fullUrl = GITHUB_DOMAIN + storyTitle + STORY_FILE_TYPE;
        try {
            returnURL = new URL(fullUrl);
        } catch (MalformedURLException e) {
            System.out.println("Error: Could not build url for: " + storyTitle);
            e.printStackTrace();
        }
        return returnURL;
    }
}
