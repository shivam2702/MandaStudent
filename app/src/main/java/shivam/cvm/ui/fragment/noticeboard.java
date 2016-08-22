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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import shivam.cvm.AppStatus;
import shivam.cvm.ui.Adapter.AttendanceAdapter;
import shivam.cvm.ui.ProgressMessage;
import shivam.cvm.MyTest.R;
/**
 * A simple {@link Fragment} subclass.
 */
public class noticeboard extends Fragment {

    public static ArrayList<String> notice = new ArrayList<String>();
    public static ArrayList<String> tname = new ArrayList<String>();
    public static View rootView;
    public noticeboard() {
        // Required empty public constructor
    }

    BufferedReader reader = null;
    StringBuilder sb = new StringBuilder();
    public static ProgressDialog progress;
    String line = null;
    String result = "";
    public static ListView list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        home.key=1;

        rootView = inflater.inflate(R.layout.fragment_noticeboard, container, false);
        list = (ListView)rootView.findViewById(R.id.listnoticeBoard);
        progress = new ProgressDialog(getActivity());
        if (AppStatus.getInstance(getActivity()).isOnline()) {
            progress = new ProgressDialog(getActivity());
            MyAsyncTask task = new MyAsyncTask();
            task.execute(links.noticeboard);
        } else {
            ProgressMessage.NotConnected(getActivity());
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SnackbarManager.show(
                        Snackbar.with(getContext()) // context
                                .text("By: " + tname.get(position)) // text to display
                                .actionLabel("Close") // action button label
                                .actionListener(new ActionClickListener() {
                                    @Override
                                    public void onActionClicked(Snackbar snackbar) {
                                        snackbar.dismiss();
                                    }
                                }) // action button's ActionClickListener
                        , getActivity()); // activity where it is displayed
            }
        });
        // Inflate the layout for this fragment
        return rootView;
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
            notice.clear();
            tname.clear();
            home.att.clear();
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    notice.add(json_data.getString("NewNotice"));
                    tname.add(json_data.getString("teachername"));
                    home.att.add("");
                }
            } catch (Exception e) {
                Toast.makeText(rootView.getContext(), "No New Notice Found.....", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            super.onPostExecute(s);
            list.setAdapter(new AttendanceAdapter(rootView.getContext(),notice));
            progress.dismiss();
        }
    }
}
