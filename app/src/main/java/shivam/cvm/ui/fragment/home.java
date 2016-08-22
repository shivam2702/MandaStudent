package shivam.cvm.ui.fragment;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

import shivam.cvm.AppStatus;
import shivam.cvm.ui.ProgressMessage;
import shivam.cvm.ui.activity.Login;
import shivam.cvm.MyTest.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class home extends Fragment {
    public static ProgressDialog progress;
    public static ArrayList<String> subjectlist = new ArrayList<String>();
    public static String subject1 = null, subject2 = null, subject3 = null, subject4 = null, subject5 = null, subject6 = null;
    public static int key = 0;
    public static String use = "abcd";
    public static ArrayList<String> att = new ArrayList<String>();
    public home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        home.key = 0;
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        TextView welcomeMessage = (TextView) rootView.findViewById(R.id.welcome);
        home.subjectlist.clear();
        welcomeMessage.setText("Welcome : " + Login.name.toUpperCase() + "\nRoll No : " + Login.rollno.toUpperCase());
        if (AppStatus.getInstance(this.getActivity()).isOnline()) {
            MyAsyncTask task = new MyAsyncTask();
            progress = new ProgressDialog(getActivity());
            task.execute(links.subjectlist);
        } else {
            ProgressMessage.NotConnected(getActivity());
        }


        return rootView;
    }

    public static class MyAsyncTask extends AsyncTask<String, Void, String> {
        String response = "";

        @Override
        protected String doInBackground(String... params) {

            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(params[0]);
                String text = "param1=" + URLEncoder.encode(Login.stuclass, "UTF-8");
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
            ProgressMessage.Progress_pre(progress);
        }

        protected void onPostExecute(String s) {
            try {
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    subjectlist.add(json_data.getString("Lectname"));
                    if (i == 0) {
                        subject1 = json_data.getString("Lectname");
                    }
                    if (i == 1) {
                        subject2 = json_data.getString("Lectname");
                    }
                    if (i == 2) {
                        subject3 = json_data.getString("Lectname");
                    }
                    if (i == 3) {
                        subject4 = json_data.getString("Lectname");
                    }
                    if (i == 4) {
                        subject5 = json_data.getString("Lectname");
                    }
                    if (i == 5) {
                        subject6 = json_data.getString("Lectname");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
            progress.dismiss();
        }
    }

}
