package com.zhoumushui.zygotepalette;

import android.app.Application;
import android.content.Context;

import com.zhoumushui.zygotepalette.util.MyUncaughtExceptionHandler;


public class MyApp extends Application {

    private Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        MyUncaughtExceptionHandler myUncaughtExceptionHandler = MyUncaughtExceptionHandler
                .getInstance();
        myUncaughtExceptionHandler.init(context);

        super.onCreate();
    }

}
