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

public class totallab extends Fragment {
    public totallab() {
        // Required empty public constructor
    }
    View rootView;
    public static ArrayList<String> Labname = new ArrayList<String>();
    public static ArrayList<String> markstotal = new ArrayList<String>();
    public static ArrayList<String> marksobtained = new ArrayList<String>();
    public static ProgressDialog progress;
    public static ListView list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        home.key=1;


        rootView = inflater.inflate(R.layout.fragment_totallab, container, false);
        list = (ListView)rootView.findViewById(R.id.lablist);
        if (AppStatus.getInstance(getActivity()).isOnline())
        {
            progress = new ProgressDialog(rootView.getContext());
            MyAsyncTaskLabName task = new MyAsyncTaskLabName();
            task.execute(links.lab2all);
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
                Labname.clear();
                markstotal.clear();
                marksobtained.clear();
                try {
                    JSONArray jArray = new JSONArray(response);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        Labname.add(i,json_data.getString("Labname"));
                        marksobtained.add(i,json_data.getString("marksobtained"));
                        markstotal.add(i,json_data.getString("markstotal"));
                    }
                } catch (Exception e) {
                    Log.e("Exception",e.getMessage());
                    e.printStackTrace();
                }

                progress.dismiss();
                home.att.clear();
                for(int i=0;i<Labname.size();i++)
                {
                    home.att.add(marksobtained.get(i)+"/"+markstotal.get(i));
                }
                Log.i("LabSize ", "" + Labname.size());
                list.setAdapter(new AttendanceAdapter(rootView.getContext(), Labname));
            }
            catch (Exception e)
            {
                Log.e("LogE",e.getMessage());
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }
}
