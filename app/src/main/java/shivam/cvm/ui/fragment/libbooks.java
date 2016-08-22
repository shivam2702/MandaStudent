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
public class libbooks extends Fragment {


    public static String title,duedate;
    public static ProgressDialog progress;


    public libbooks() {
        // Required empty public constructor
    }

    public static TextView lib1, lib2, lib3, lib4, lib5, lib6;
    public static View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        home.key = 1;
        rootView = inflater.inflate(R.layout.fragment_libbooks, container, false);
        lib1 = (TextView) rootView.findViewById(R.id.LibraryA1);
        lib2 = (TextView) rootView.findViewById(R.id.LibraryA2);
        lib3 = (TextView) rootView.findViewById(R.id.LibraryA3);
        lib4 = (TextView) rootView.findViewById(R.id.LibraryA4);
        lib5 = (TextView) rootView.findViewById(R.id.LibraryA5);
        lib6 = (TextView) rootView.findViewById(R.id.LibraryA6);

        if (AppStatus.getInstance(getActivity()).isOnline()) {
            progress = new ProgressDialog(getActivity());
            MyAsyncTask task = new MyAsyncTask();
            task.execute(links.library);
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
            ProgressMessage.Progress_pre(progress);
        }

        protected void onPostExecute(String s) {
            try {
                JSONArray jArray = new JSONArray(response);
                int countlib = 0;
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    title = json_data.getString("title");
                    duedate = json_data.getString("duedate");
                    if (duedate.equals("null")) {
                        countlib++;
                        if (countlib == 1) {
                            lib1.setText("1. " + title+"\n");
                            lib2.setText("");
                            lib3.setText("");
                            lib4.setText("");
                            lib5.setText("");
                            lib6.setText("");
                        } else {
                            if (countlib == 2) {
                                lib2.setText("2. " + title+"\n");
                            } else {
                                if (countlib == 3) {
                                    lib3.setText("3. " + title+"\n");
                                } else {
                                    if (countlib == 4) {
                                        lib4.setText("4. " + title+"\n");
                                    } else {
                                        if (countlib == 5) {
                                            lib5.setText("5. " + title+"\n");
                                        } else {
                                            if (countlib == 6) {
                                                lib6.setText("6. " + title+"\n");
                                            }
                                        }
                                    }
                                }
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
