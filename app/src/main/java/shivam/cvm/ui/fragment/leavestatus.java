package shivam.cvm.ui.fragment;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import com.nispok.snackbar.listeners.ActionClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

import shivam.cvm.AppStatus;
import shivam.cvm.ui.Adapter.AttendanceAdapter;
import shivam.cvm.ui.ProgressMessage;
import shivam.cvm.ui.activity.Login;
import shivam.cvm.MyTest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class leavestatus extends Fragment {

    public static ArrayList<String> Status = new ArrayList<String>();
    public static ArrayList<String> reason = new ArrayList<String>();
    public static ListView list;
    public static ProgressDialog progress;
    public static View rootView;

    public leavestatus() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        home.key = 1;

        rootView = inflater.inflate(R.layout.fragment_leavestatus, container, false);
        list = (ListView) rootView.findViewById(R.id.leavelist);
        if (AppStatus.getInstance(getActivity()).isOnline()) {
            progress = new ProgressDialog(getActivity());
            MyAsyncTask task = new MyAsyncTask();
            task.execute(links.leaveget);
        } else {
            ProgressMessage.NotConnected(getActivity());
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String message;
                if (Status.get(position).equals("Approve")) {
                    message="Congratulation's, Your leave is Approved.";
                } else if (Status.get(position).equals("Reject")) {
                    message="Sorry, Leave is Rejected.";
                } else {
                    message="Keep Calm, It's under process.";
                }
                SnackbarManager.show(
                        Snackbar.with(getContext()) // context
                                .text(message) // text to display
                                .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                          ,getActivity()); // activity where it is displayed
            }
        });

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

        protected void onPreExecute() {
            progress.setTitle("Please Wait");
            progress.setMessage("Getting Your Data Please Be Patient...");
            progress.show();
        }

        protected void onPostExecute(String s) {
            try {
                reason.clear();
                home.att.clear();
                Status.clear();
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    reason.add(json_data.getString("Reason"));
                    home.att.add(json_data.getString("Date"));
                    Status.add(json_data.getString("Status"));
                }
            } catch (Exception e) {
                Toast.makeText(rootView.getContext(),"There is no History Found.....",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            super.onPostExecute(s);
            list.setAdapter(new AttendanceAdapter(rootView.getContext(), reason));
            progress.dismiss();
        }
    }
}
