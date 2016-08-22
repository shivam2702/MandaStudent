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

public class assignment extends Fragment {
    public static ProgressDialog progress;
    public static ArrayList<String> subject1 = new ArrayList<String>();
    public static ArrayList<String> subject2 = new ArrayList<String>();
    public static ArrayList<String> subject3 = new ArrayList<String>();
    public static ArrayList<String> subject4 = new ArrayList<String>();
    public static ArrayList<String> subject5 = new ArrayList<String>();
    public static ArrayList<String> subject6 = new ArrayList<String>();
    //public static ArrayList<String> subjectlist = new ArrayList<String>();

    public assignment() {
        // Required empty public constructor
    }

    public static View rootView;
    public static ExpandableListView expandableListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        home.key = 1;
        home.use = "Assignment ";
        rootView = inflater.inflate(R.layout.fragment_assignment, container, false);

        expandableListView = (ExpandableListView) rootView.findViewById(R.id.exp_listview);

        if (AppStatus.getInstance(this.getActivity()).isOnline()) {
            progress = new ProgressDialog(rootView.getContext());
            MyAsyncTask task = new MyAsyncTask();
            task.execute(links.assignment);
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

                    subject1.add(json_data.getString("subject1a1"));
                    subject1.add(json_data.getString("subject1a2"));
                    subject1.add(json_data.getString("subject1a3"));
                    subject1.add(json_data.getString("subject1a4"));
                    subject1.add(json_data.getString("subject1a5"));

                    subject2.add(json_data.getString("subject2a1"));
                    subject2.add(json_data.getString("subject2a2"));
                    subject2.add(json_data.getString("subject2a3"));
                    subject2.add(json_data.getString("subject2a4"));
                    subject2.add(json_data.getString("subject2a5"));

                    subject3.add(json_data.getString("subject3a1"));
                    subject3.add(json_data.getString("subject3a2"));
                    subject3.add(json_data.getString("subject3a3"));
                    subject3.add(json_data.getString("subject3a4"));
                    subject3.add(json_data.getString("subject3a5"));

                    subject4.add(json_data.getString("subject4a1"));
                    subject4.add(json_data.getString("subject4a2"));
                    subject4.add(json_data.getString("subject4a3"));
                    subject4.add(json_data.getString("subject4a4"));
                    subject4.add(json_data.getString("subject4a5"));

                    subject5.add(json_data.getString("subject5a1"));
                    subject5.add(json_data.getString("subject5a2"));
                    subject5.add(json_data.getString("subject5a3"));
                    subject5.add(json_data.getString("subject5a4"));
                    subject5.add(json_data.getString("subject5a5"));

                    subject6.add(json_data.getString("subject6a1"));
                    subject6.add(json_data.getString("subject6a2"));
                    subject6.add(json_data.getString("subject6a3"));
                    subject6.add(json_data.getString("subject6a4"));
                    subject6.add(json_data.getString("subject6a5"));
                    HashMap<String, List<String>> ChildList = new HashMap<String, List<String>>();
//                    subjectlist.clear();
//                    for (int k = 0; k < home.subjectlist.size(); k++) {
//                        subjectlist.add(home.subjectlist.get(k));
//                    }
                    try{
                        ChildList.put(home.subjectlist.get(0), subject1);
                        ChildList.put(home.subjectlist.get(1), subject2);
                        ChildList.put(home.subjectlist.get(2), subject3);
                        ChildList.put(home.subjectlist.get(3), subject4);
                        ChildList.put(home.subjectlist.get(4), subject5);
                        ChildList.put(home.subjectlist.get(5), subject6);
                    }
                    catch (Exception e)
                    {
                        Log.e("EXCEPTION:",e.getMessage());
                    }

                    MyAdapter myAdapter = new MyAdapter(rootView.getContext(), home.subjectlist, ChildList);
                    expandableListView.setAdapter(myAdapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            progress.dismiss();


            super.onPostExecute(s);
        }
    }


}


