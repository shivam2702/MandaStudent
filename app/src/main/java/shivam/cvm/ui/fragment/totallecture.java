package shivam.cvm.ui.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

import shivam.cvm.AppStatus;
import shivam.cvm.MyTest.R;
import shivam.cvm.ui.Adapter.AttendanceAdapter;
import shivam.cvm.ui.ProgressMessage;
import shivam.cvm.ui.activity.Login;


public class totallecture extends Fragment {

    public static ProgressDialog progress;
    public static ArrayList<String> percent = new ArrayList<String>();

    public totallecture() {
        // Required empty public constructor

    }

    ListView list;
    ArrayList<String> subjectlist = new ArrayList<String>();

    public View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        home.key = 1;

        rootView = inflater.inflate(R.layout.fragment_totallecture, container, false);
        list = (ListView) rootView.findViewById(R.id.lecturetotal);
//        for (int i = 0; i < home.subjectlist.size(); i++)
//            subjectlist.add(home.subjectlist.get(i));

        if (AppStatus.getInstance(this.getActivity()).isOnline()) {
            progress = new ProgressDialog(rootView.getContext());
            MyAsyncTask task = new MyAsyncTask();
            task.execute(links.totalattendance);
        } else {
            ProgressMessage.NotConnected(getActivity());
        }
        return rootView;
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {
        String response = "";
        String classs = Login.stuclass;
        String rollno = Login.rollno;

        @Override
        protected String doInBackground(String... params) {

            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(params[0]);
                String text = "param1=" + URLEncoder.encode(rollno, "UTF-8") + "&param2=" + URLEncoder.encode(classs, "UTF-8");
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
            String subject1Total = "", subject2Total = "", subject3Total = "", subject4Total = "", subject5Total = "", subject6Total = "";
            String subject1Obtain = "", subject2Obtain = "", subject3Obtain = "", subject4Obtain = "", subject5Obtain = "", subject6Obtain = "";
            try {
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    subject1Total = json_data.getString("maxatten1");
                    subject1Obtain = json_data.getString("atten1");
                    subject2Total = json_data.getString("maxatten2");
                    subject2Obtain = json_data.getString("atten2");
                    subject3Total = json_data.getString("maxatten3");
                    subject3Obtain = json_data.getString("atten3");
                    subject4Total = json_data.getString("maxatten4");
                    subject4Obtain = json_data.getString("atten4");
                    subject5Total = json_data.getString("maxatten5");
                    subject5Obtain = json_data.getString("atten5");
                    subject6Total = json_data.getString("maxatten6");
                    subject6Obtain = json_data.getString("atten6");
                    if (subject1Total.equals("null")) {
                        subject1Total = "0";
                    }
                    if (subject2Total.equals("null")) {
                        subject2Total = "0";
                    }
                    if (subject3Total.equals("null")) {
                        subject3Total = "0";
                    }
                    if (subject4Total.equals("null")) {
                        subject4Total = "0";
                    }
                    if (subject5Total.equals("null")) {
                        subject5Total = "0";
                    }
                    if (subject6Total.equals("null")) {
                        subject6Total = "0";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            home.att.clear();
            double value=0;
            try {
                percent.clear();
                if (subject1Total.equals("0")) {
                    value = 100.00;
                    attgrade(value);
                //    percent.add("" + round(value, 2));
                } else {
                    value =(Double.parseDouble(subject1Obtain)) / (Double.parseDouble(subject1Total)) * 100;
                    attgrade(value);
                    //    percent.add("" + round(value, 2));
                }
                if (subject2Total.equals("0")) {
                    value = 100.00;
                    attgrade(value);
                    //    percent.add("100");
                } else {
                    value =(Double.parseDouble(subject2Obtain)) / (Double.parseDouble(subject2Total)) * 100;
                    attgrade(value);
                    //    percent.add(""+round(value, 2));
                }
                if (subject3Total.equals("0")) {
                    value = 100.00;
                    attgrade(value);
                    //    percent.add("100");
                } else {
                    value =(Double.parseDouble(subject3Obtain)) / (Double.parseDouble(subject3Total)) * 100;
                    attgrade(value);
                    //    percent.add(""+round(value, 2));
                }
                if (subject4Total.equals("0")) {
                    value = 100.00;
                    attgrade(value);
                    //    percent.add("100");
                } else {
                    value =(Double.parseDouble(subject4Obtain)) / (Double.parseDouble(subject4Total)) * 100;
                    attgrade(value);
                    //    percent.add(""+round(value, 2));
                }
                if (subject5Total.equals("0")) {
                    value = 100.00;
                    attgrade(value);
                    //    percent.add("100");
                } else {
                    value =(Double.parseDouble(subject5Obtain)) / (Double.parseDouble(subject5Total)) * 100;
                    attgrade(value);
                    //    percent.add(""+round(value, 2));
                }
                if (subject6Total.equals("0")) {
                    value = 100.00;
                    attgrade(value);
                    //    percent.add("100");
                } else {
                    value =round((Double.parseDouble(subject6Obtain)) / (Double.parseDouble(subject6Total)) * 100,2);
                    attgrade(value);
                    //    percent.add(""+round(value, 2));
                }
                list.setAdapter(new AttendanceAdapter(rootView.getContext(), home.subjectlist));

            } catch (Exception e) {
                Log.e("EXCEPTIONS", e.getMessage());
            }
            list.setAdapter(new AttendanceAdapter(rootView.getContext(), home.subjectlist));
            progress.dismiss();


            super.onPostExecute(s);
        }
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    void attgrade(double value)
    {
        String tag="";
        if (value<=33.33 && value>=0)
        {
            tag = "Poor";
        //    percent.add("Poor");
        }
        if (value<=50.00 && value>33.33)
        {
            tag = "Below Average";
        //    percent.add("Below Average");
        }
        if (value<=75.00 && value>50.00)
        {
            tag = "Average";
        //    percent.add("Average");
        }
        if (value<=90.00 && value>75.00)
        {
            tag = "Good";
        //    percent.add("Good");
        }
        if (value<=100.00 && value>90.00)
        {
            tag = "Excellent";
        //    percent.add("Excellent");
        }
        percent.add(tag);
        home.att.add(tag);
    }
}
