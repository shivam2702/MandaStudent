package shivam.cvm.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import shivam.cvm.AppStatus;
import shivam.cvm.MyTest.BuildConfig;
import shivam.cvm.MyTest.R;
import shivam.cvm.ui.GCM.GCMClientManager;
import shivam.cvm.ui.Util;
import shivam.cvm.ui.fragment.links;

import static android.widget.Toast.LENGTH_SHORT;

public class Login extends Activity {
    public static String name = "";
    public static String rollno = "";
    public static String stuclass = null;
    public static ProgressDialog progress;
    public static ProgressDialog progress1;
    public static int count = 0;
    public static String appversion = BuildConfig.VERSION_NAME;;
    public static String regid = "";
    public EditText user;
    public EditText pass;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    CheckBox cb;
    CheckBox showpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getRegId();
        progress1 = new ProgressDialog(Login.this);
        MyAsyncTaskAppVersion versionchk = new MyAsyncTaskAppVersion();
        versionchk.execute(links.appversion);
        user = (EditText) findViewById(R.id.editText_username);
        pass = (EditText) findViewById(R.id.editText2_password);
        Button btn1 = (Button) findViewById(R.id.loginbtn);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();

        final String versionCode = BuildConfig.VERSION_NAME;
        String prefVersionCode = pref.getString("versionCode", "");
        if(prefVersionCode.equals(versionCode))
        {

        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
            builder.setTitle("Update Log");
            builder.setMessage("1. Stability Fix\n2. Congrats our app is now on Play Store");
            builder.setPositiveButton("Thank You", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editor.putString("versionCode", versionCode);
                }
            });
            AlertDialog dialog = builder.show();
            dialog.setCancelable(false);
            dialog.show();

        }


        String prefname = pref.getString("username", null);
        String prefpass = pref.getString("password", null);
        Boolean remember = pref.getBoolean("remember", true);
        Boolean keepmelogin = pref.getBoolean("keepmelogin", false);
        user.setText(prefname);
        pass.setText(prefpass);
        cb = (CheckBox) findViewById(R.id.checkBox);
        showpass = (CheckBox) findViewById(R.id.showpassword);
        showpass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    pass.setTransformationMethod(null);
                }
                else
                {
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        cb.setChecked(remember);
        editor.commit();
        if (keepmelogin == true) {
            stuclass = pref.getString("stuclass", stuclass);
            name = pref.getString("name", null);
            rollno = pref.getString("rollno", null);
            Intent j = new Intent(getApplicationContext(), MainActivity.class);
            finish();
            startActivity(j);

        }
        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                if (AppStatus.getInstance(Login.this).isOnline()) {
                    progress = new ProgressDialog(Login.this);
                    MyAsyncTask task = new MyAsyncTask();
                    task.execute(links.login);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setTitle("No Connection");
                    builder.setMessage("We are not Connected :( \n Please Check Your Connection and try again :) ");
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
        });
    }

    public void getRegId() {
        GCMClientManager pushClientManager = new GCMClientManager(this, Util.SENDER_ID);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {
                regid = registrationId;
                Log.e("Registration id", registrationId);
                //send this registrationId to your server
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
            }
        });
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {
        String response = "";
        String username = user.getText().toString().toLowerCase();
        String password = pass.getText().toString().toLowerCase();

        @Override
        protected String doInBackground(String... params) {

            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(params[0]);
                String text = "param1=" + URLEncoder.encode(username, "UTF-8") + "&param2=" + URLEncoder.encode(password, "UTF-8") + "&param3=" + URLEncoder.encode(regid, "UTF-8");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setFixedLengthStreamingMode(text.getBytes().length);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                PrintWriter out = new PrintWriter(conn.getOutputStream());
                out.print(text);
                out.close();
                Scanner inStream = new Scanner(conn.getInputStream());
                while (inStream.hasNextLine())
                    response += (inStream.nextLine());
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return response;
        }

        protected void onPreExecute() {
            progress.setTitle("Please Wait");
            progress.setMessage("We are checking for your Authentication");
            progress.show();
            progress.setCancelable(false);
        }

        protected void onPostExecute(String s) {
            try {
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    name = json_data.getString("name");
                    rollno = json_data.getString("rollno").toLowerCase();
                    String sem = json_data.getString("sem").toLowerCase();
                    String branch = json_data.getString("branch").toLowerCase();
                    String Status = json_data.getString("Status");
                    stuclass = sem + branch;
                    editor.putString("name", name);
                    editor.putString("rollno", rollno);
                    editor.putString("stuclass", stuclass);
                    editor.commit();
                    if (Status.equals("Sucess")) {
                        cb = (CheckBox) findViewById(R.id.checkBox);
                        if (cb.isChecked()) {
                            editor.putString("username", user.getText().toString().toLowerCase());
                            editor.putString("password", pass.getText().toString().toLowerCase());
                            editor.putBoolean("remember", true);
                            editor.putBoolean("keepmelogin", true);
                            editor.commit();
                        } else {
                            editor.clear();
                            editor.commit();
                        }
                        Intent j = new Intent(getApplicationContext(), MainActivity.class);
                        finish();
                        startActivity(j);
                    } else if (Status.equals("Fail")) {
                        Toast.makeText(Login.this, "Login Failed. Wrong Combination..", LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Login.this, "Some Other Error", LENGTH_SHORT).show();
                    }
                }
                progress.dismiss();
            } catch (Exception e) {
                Log.e("Errors:", e.getMessage());
                Toast.makeText(Login.this, "Sorry our Server is down.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                progress.dismiss();
            }

            super.onPostExecute(s);
        }
    }

    public class MyAsyncTaskAppVersion extends AsyncTask<String, Void, String> {
        String response = "";

        @Override
        protected String doInBackground(String... params) {

            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(params[0]);
                String text = "";
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setFixedLengthStreamingMode(text.getBytes().length);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                PrintWriter out = new PrintWriter(conn.getOutputStream());
                out.print(text);
                out.close();
                Scanner inStream = new Scanner(conn.getInputStream());
                while (inStream.hasNextLine())
                    response += (inStream.nextLine());
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return response;
        }

        protected void onPreExecute() {
            progress1.setTitle("Please Wait");
            progress1.setMessage("We are checking for Update");
            progress1.show();
            progress1.setCancelable(false);
        }

        protected void onPostExecute(String s) {
            String appversiondb;
            try {
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    appversiondb = json_data.getString("version");
                    if (appversiondb.equals(appversion)) {
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setTitle("Update Found");
                        builder.setMessage("There are some pending update of this app.\n Please Update for proper functioning");
                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mitbikaner.ac.in/mitstudent.apk"));
                                startActivity(i);
                            }
                        });
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                progress1.dismiss();
            } catch (Exception e) {

            }
            super.onPostExecute(s);
        }
    }

}