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

            // TODO Auto-generated catch block
            malformedURLException.printStackTrace();
            TextHolder = malformedURLException.toString();

        } catch (IOException iOException) {

            // TODO Auto-generated catch block
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

//public class TextFileReader extends AsyncTask<String, Void, String> {
//    public AsyncResponse delegate = null;
//    private String TextHolder = "", TextHolder2 = "";
//    private URL url;
//    private BufferedReader bufferReader;
//
//    @Override
//    protected String doInBackground(String... params) {
//        try {
////            url = new URL(params[0]);
////            bufferReader = new BufferedReader(new InputStreamReader(url.openStream()));
////
////            if (params[1].equals("0")) {
////                while ((TextHolder2 = bufferReader.readLine()) != null) {
////                    TextHolder += TextHolder2 + "\n";
////                }
////            } else {
////                int count = Integer.parseInt(params[1]);
////                if (0 > count) {  //negative number
////                    //discard the first count number of lines, and then read the rest
////                    for (int i = 0; i < count; i++) {
////                        TextHolder2 = bufferReader.readLine();
////                    }
////                }
////                for (int i = 0; i < Math.abs(count); i++) {
////                    TextHolder2 = bufferReader.readLine();
////                    TextHolder += TextHolder2;
////                }
////            }
//
//            try {
//                url = new URL(params[0]);
//                bufferReader = new BufferedReader(new InputStreamReader(url.openStream()));
//                while ((TextHolder2 = bufferReader.readLine()) != null) {
//                    TextHolder += TextHolder2 + "\n";
//                }
//                bufferReader.close();
//
//            } catch (MalformedURLException malformedURLException) {
//
//                // TODO Auto-generated catch block
//                malformedURLException.printStackTrace();
//                TextHolder = malformedURLException.toString();
//
//            } catch (IOException iOException) {
//
//                // TODO Auto-generated catch block
//                iOException.printStackTrace();
//
//                TextHolder = iOException.toString();
//            }
//
//            bufferReader.close();
//
//        } catch (MalformedURLException malformedURLException) {
//            malformedURLException.printStackTrace();
//            TextHolder = malformedURLException.toString();
//
//        } catch (IOException iOException) {
//            iOException.printStackTrace();
//            TextHolder = iOException.toString();
//        }
//
//        return TextHolder;
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//        delegate.processFinish(result);
//    }
//
//}
