package com.example.bloodygoodgpa;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BaseApp extends Application {
    public Container container;

    public BaseApp() {
        container = new Container();
    }


}
