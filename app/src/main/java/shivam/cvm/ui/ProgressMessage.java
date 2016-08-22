package shivam.cvm.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by Shivam on 9/28/2015.
 */
public class ProgressMessage {
    public static String Title1 = "Please Wait";
    public static String Message1 = "Getting Your Data please Be Patient...";

    public static void Progress_pre(ProgressDialog progress)
    {
        progress.setTitle(Title1);
        progress.setMessage(Message1);
        progress.show();
        progress.setCancelable(false);
    }

    public static void NotConnected(final Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("No Connection");
        builder.setMessage("We are not Connected :( \n Please Check Your Connection and retry :) ");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.show();
        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
        dialog.show();
    }
}
