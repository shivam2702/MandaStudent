package shivam.cvm.MyTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import android.os.Process;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import shivam.cvm.ui.activity.Login;


/**
 * Created by Shivam on 1/11/2016.
 */
public class DefaultExceptionHandler implements UncaughtExceptionHandler {

    private final Context myContext;
    private final Class<?> myActivityClass;

    public DefaultExceptionHandler(Context context, Class<?> c) {

        myContext = context;
        myActivityClass = c;
    }

    public void uncaughtException(Thread thread, Throwable exception) {

        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        System.err.println(stackTrace);// You can use LogCat too
        Intent intent = new Intent(myContext, myActivityClass);
        String s = stackTrace.toString();
        //you can use this String to know what caused the exception and in which Activity
        intent.putExtra("uncaughtException",
                "Exception is: " + stackTrace.toString());
        intent.putExtra("stacktrace", s);
        Process.killProcess(Process.myPid());
        myContext.startActivity(intent);
        Intent App = new Intent(myContext, Login.class);
        App.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        System.exit(0);
    }
}