package shivam.cvm.ui.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import shivam.cvm.AppStatus;
import shivam.cvm.MyTest.R;
import shivam.cvm.ui.Adapter.MyAdapter;
import shivam.cvm.ui.ProgressMessage;
import shivam.cvm.ui.activity.Login;

public class midterm extends Fragment {
    public static ProgressDialog progress;
    public static ArrayList<String> subject1 = new ArrayList<String>();
    public static ArrayList<String> subject2 = new ArrayList<String>();
    public static ArrayList<String> subject3 = new ArrayList<String>();
    public static ArrayList<String> subject4 = new ArrayList<String>();
    public static ArrayList<String> subject5 = new ArrayList<String>();
    public static ArrayList<String> subject6 = new ArrayList<String>();
    //public static ArrayList<String> subjectlist = new ArrayList<String>();


    public midterm() {
        // Required empty public constructor
    }

    public static View rootView;
    public static ExpandableListView expandableListViewMid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        home.key = 1;
        home.use = "MidTerm ";
        rootView = inflater.inflate(R.layout.fragment_midterm, container, false);
        expandableListViewMid = (ExpandableListView) rootView.findViewById(R.id.exp_listviewmid);
        if (AppStatus.getInstance(getActivity()).isOnline()) {
            progress = new ProgressDialog(getActivity());
            MyAsyncTask task = new MyAsyncTask();
            task.execute(links.midterm);
        } else {
            ProgressMessage.NotConnected(getActivity());
        }
        return rootView;

    }

    public static class MyAsyncTask extends AsyncTask<String, Void, String> {
        String response = "";
        String classs = Login.stuclass;
        String rollno = Login.rollno;

        @Override
        protected String doInBackground(String... params) {

            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(params[0]);
                String text = "param1=" + URLEncoder.encode(classs, "UTF-8") + "&param2=" + URLEncoder.encode(rollno, "UTF-8");
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
            subject1.clear();
            subject2.clear();
            subject3.clear();
            subject4.clear();
            subject5.clear();
            subject6.clear();

            try {
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    subject1.add(json_data.getString("subject1mt1"));
                    subject1.add(json_data.getString("subject1mt2"));
                    subject1.add(json_data.getString("subject1mt3"));
                    subject1.add(json_data.getString("subject1mt4"));
                    subject1.add(json_data.getString("subject1mt5"));

                    subject2.add(json_data.getString("subject2mt1"));
                    subject2.add(json_data.getString("subject2mt2"));
                    subject2.add(json_data.getString("subject2mt3"));
                    subject2.add(json_data.getString("subject2mt4"));
                    subject2.add(json_data.getString("subject2mt5"));

                    subject3.add(json_data.getString("subject3mt1"));
                    subject3.add(json_data.getString("subject3mt2"));
                    subject3.add(json_data.getString("subject3mt3"));
                    subject3.add(json_data.getString("subject3mt4"));
                    subject3.add(json_data.getString("subject3mt5"));

                    subject4.add(json_data.getString("subject4mt1"));
                    subject4.add(json_data.getString("subject4mt2"));
                    subject4.add(json_data.getString("subject4mt3"));
                    subject4.add(json_data.getString("subject4mt4"));
                    subject4.add(json_data.getString("subject4mt5"));

                    subject5.add(json_data.getString("subject5mt1"));
                    subject5.add(json_data.getString("subject5mt2"));
                    subject5.add(json_data.getString("subject5mt3"));
                    subject5.add(json_data.getString("subject5mt4"));
                    subject5.add(json_data.getString("subject5mt5"));

                    subject6.add(json_data.getString("subject6mt1"));
                    subject6.add(json_data.getString("subject6mt2"));
                    subject6.add(json_data.getString("subject6mt3"));
                    subject6.add(json_data.getString("subject6mt4"));
                    subject6.add(json_data.getString("subject6mt5"));
                    HashMap<String, List<String>> ChildList = new HashMap<String, List<String>>();
//                    subjectlist.clear();
//                    for (int k = 0; k < home.subjectlist.size(); k++) {
//                        subjectlist.add(home.subjectlist.get(k));
//                    }
                    try {
                        ChildList.put(home.subjectlist.get(0), subject1);
                        ChildList.put(home.subjectlist.get(1), subject2);
                        ChildList.put(home.subjectlist.get(2), subject3);
                        ChildList.put(home.subjectlist.get(3), subject4);
                        ChildList.put(home.subjectlist.get(4), subject5);
                        ChildList.put(home.subjectlist.get(5), subject6);
                    }
                    catch (Exception e)
                    {
                        Log.e("EXCEPTION:", e.getMessage());
                    }

                    MyAdapter myAdapter = new MyAdapter(rootView.getContext(), home.subjectlist, ChildList);
                    expandableListViewMid.setAdapter(myAdapter);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
            progress.dismiss();
        }
    }
}


