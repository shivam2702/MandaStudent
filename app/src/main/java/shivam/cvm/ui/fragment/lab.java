package shivam.cvm.ui.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import shivam.cvm.MyTest.R;
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

public class lab extends Fragment {
public int pos;
    public lab() {
        // Required empty public constructor
    }
    public static ArrayList<String> LabID = new ArrayList<String>();
    public static ArrayList<String> Labname = new ArrayList<String>();
    public static ArrayList<String> DBName = new ArrayList<String>();
    public static ArrayList<String> date = new ArrayList<String>();
    public static ArrayList<String> file = new ArrayList<String>();
    public static ArrayList<String> viva = new ArrayList<String>();
    public static ArrayList<String> attendance = new ArrayList<String>();
    public static ArrayList<String> performance = new ArrayList<String>();
    public static ArrayList<String> uniform = new ArrayList<String>();
    public static ArrayList<String> total = new ArrayList<String>();

    Spinner labname,spinnerdate;
    public static ProgressDialog progress;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        home.key=1;

        rootView = inflater.inflate(R.layout.fragment_lab, container, false);
        spinnerdate = (Spinner)rootView.findViewById(R.id.spinner2);
        if (AppStatus.getInstance(getActivity()).isOnline())
        {
            progress = new ProgressDialog(rootView.getContext());
            MyAsyncTaskLabName task = new MyAsyncTaskLabName();
            task.execute(links.labdate1);
        }
        else
        {
            ProgressMessage.NotConnected(getActivity());
        }
        return rootView;
    }

    public class MyAsyncTaskLabName extends AsyncTask<String, Void, String> {
        String response = "";
        String rollno= Login.rollno;
        @Override
        protected String doInBackground(String... params) {
            LabID.clear();
            Labname.clear();
            DBName.clear();
            date.clear();
            file.clear();
            viva.clear();
            attendance.clear();
            performance.clear();
            uniform.clear();
            total.clear();
            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(params[0]);
                String text = "param1=" + URLEncoder.encode(rollno, "UTF-8");
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
        protected void onPreExecute(){
            ProgressMessage.Progress_pre(progress);
        }
        protected void onPostExecute(String s) {
            try {
                JSONArray jArray = new JSONArray(response);
                for(int i=0;i<jArray.length();i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    LabID.add(i,json_data.getString("LabID"));
                    Labname.add(i,json_data.getString("Labname"));
                    DBName.add(i, json_data.getString("DBName"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            progress.dismiss();
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Labname);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            labname = (Spinner)rootView.findViewById(R.id.labsubjects);
            labname.setAdapter(spinnerArrayAdapter);
            labname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView labname = (TextView) rootView.findViewById(R.id.labname);
                    labname.setText(Labname.get(position));
                    if (AppStatus.getInstance(getActivity()).isOnline()) {
                        pos = position;
                        progress = new ProgressDialog(rootView.getContext());
                        MyAsyncTaskDate task = new MyAsyncTaskDate();
                        task.execute(links.labdate2);
                    } else {
                        ProgressMessage.NotConnected(getActivity());
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            super.onPostExecute(s);
        }
    }





    public class MyAsyncTaskDate extends AsyncTask<String, Void, String> {
        String response = "";
        String classs= Login.stuclass;
        String rollno= Login.rollno;
        @Override
        protected String doInBackground(String... params) {
            date.clear();
            file.clear();
            viva.clear();
            attendance.clear();
            performance.clear();
            uniform.clear();
            total.clear();
            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(params[0]);
                String text = "param1=" + URLEncoder.encode(rollno, "UTF-8") + "&param2=" + URLEncoder.encode(DBName.get(pos), "UTF-8");
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
        protected void onPreExecute(){
            ProgressMessage.Progress_pre(progress);
        }

        protected void onPostExecute(String s) {
            try {
                JSONArray jArray = new JSONArray(response);
                for(int i=0;i<jArray.length();i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    date.add(i,json_data.getString("date"));
                    file.add(i, json_data.getString("file"));
                    viva.add(i,json_data.getString("viva"));
                    attendance.add(i,json_data.getString("attendance"));
                    performance.add(i, json_data.getString("performance"));
                    uniform.add(i, json_data.getString("uniform"));
                    total.add(i, json_data.getString("total"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            progress.dismiss();
            ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, date);
            spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerdate.setAdapter(spinnerArrayAdapter1);
            spinnerdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView filetxt = (TextView) rootView.findViewById(R.id.filetxt);
                    TextView vivatxt = (TextView) rootView.findViewById(R.id.vivatxt);
                    TextView attendancetxt = (TextView) rootView.findViewById(R.id.attendancetxt);
                    TextView performacetxt = (TextView) rootView.findViewById(R.id.performacetxt);
                    TextView uniformtxt = (TextView) rootView.findViewById(R.id.uniformtxt);
                    TextView totaltxt = (TextView) rootView.findViewById(R.id.totaltxt);
                    filetxt.setText(file.get(position));
                    vivatxt.setText(viva.get(position));
                    attendancetxt.setText(attendance.get(position));
                    performacetxt.setText(performance.get(position));
                    uniformtxt.setText(uniform.get(position));
                    totaltxt.setText(total.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            super.onPostExecute(s);
        }
    }

}
