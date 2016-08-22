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
import java.util.Scanner;

import shivam.cvm.AppStatus;
import shivam.cvm.ui.ProgressMessage;
import shivam.cvm.ui.activity.Login;
import shivam.cvm.MyTest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class libaccount extends Fragment {
    public static String title,duedate;
    public static TextView bb1,bb2;
    public static ProgressDialog progress;


    public libaccount() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        home.key=1;

        View rootView = inflater.inflate(R.layout.fragment_libaccount, container, false);
        bb1 = (TextView)rootView.findViewById(R.id.BookBank1);
        bb2 = (TextView)rootView.findViewById(R.id.BookBank2);
        if (AppStatus.getInstance(getActivity()).isOnline()) {
            progress = new ProgressDialog(getActivity());
            MyAsyncTask task = new MyAsyncTask();
            task.execute(links.library);
        } else {
            ProgressMessage.NotConnected(getActivity());
        }


        // Inflate the layout for this fragment
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
                String text = "param1=" + URLEncoder.encode(Login.rollno, "UTF-8");
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
                int countbb=0;
                for(int i=0;i<jArray.length();i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    title= json_data.getString("title");
                    duedate= json_data.getString("duedate");
                    if(duedate!="null"){
                        countbb++;
                        if(countbb==1)
                        {
                            bb1.setText("1. " + title + "\nDue Date: "+duedate+"\n");
                            bb2.setText("");
                        }
                        else
                        {
                            if(countbb==2)
                            {
                                bb2.setText("2. " + title + "\nDue Date: "+duedate);
                            }
                        }
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
