package com.belonging.developer;

public interface AsyncResponse {
    // implemented by any class that wants to use my TextFileReader and get a response back :)
    public void processFinish(String output);
}
