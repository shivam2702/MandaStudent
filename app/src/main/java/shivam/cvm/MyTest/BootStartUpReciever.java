package shivam.cvm.MyTest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import shivam.cvm.ui.activity.Login;

/**
 * Created by Shivam on 1/11/2016.
 */
public class BootStartUpReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving

        // Start Service On Boot Start Up
        Intent service = new Intent(context, TestService.class);
        context.startService(service);

        //Start App On Boot Start Up
        Intent App = new Intent(context, Login.class);
        App.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.startActivity(App);
    }
}
