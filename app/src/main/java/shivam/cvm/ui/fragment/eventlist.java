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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import shivam.cvm.AppStatus;
import shivam.cvm.ui.ProgressMessage;
import shivam.cvm.MyTest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class eventlist extends Fragment {
    public static ProgressDialog progress;
    BufferedReader reader = null;
    StringBuilder sb = new StringBuilder();
    TextView tv;
    String line = null;
    String result = "";

    public eventlist() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        home.key=1;

        if (AppStatus.getInstance(getActivity()).isOnline()) {
            progress = new ProgressDialog(getActivity());
            MyAsyncTask task = new MyAsyncTask();
            task.execute(links.event);
        } else {
            ProgressMessage.NotConnected(getActivity());
        }

        return inflater.inflate(R.layout.fragment_eventlist, container, false);
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }

        protected void onPreExecute() {
            ProgressMessage.Progress_pre(progress);
        }

        @Override
        protected void onPostExecute(String s) {
            String date, event,days;
            tv = (TextView) getActivity().findViewById(R.id.eventlist);
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    date = json_data.getString("Date");
                    event = json_data.getString("Reason");
                    days = json_data.getString("NumberOfDays");
                    tv.append("Reason: " + event +  "\nNumber of Days: " + days + "\n\n\n");
                }
            } catch (Exception e) {
                tv.append("No new Events.....\nStay tuned for updates...");
                e.printStackTrace();
            }
            super.onPostExecute(s);
            progress.dismiss();
        }
    }

}
