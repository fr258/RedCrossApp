package com.example.bloodygoodgpa;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Container {
    public ExecutorService executorService;
    public HttpClient client;
    public String user;
    public Container() {
       executorService = Executors.newFixedThreadPool(4);
       client = new HttpClient(executorService);
    }


}
