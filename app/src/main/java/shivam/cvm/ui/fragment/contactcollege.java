package shivam.cvm.ui.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import shivam.cvm.AppStatus;
import shivam.cvm.ui.ProgressMessage;
import shivam.cvm.ui.activity.Login;
import shivam.cvm.MyTest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class contactcollege extends Fragment {
    BufferedReader reader = null;
    StringBuilder sb = new StringBuilder();
    TextView tv;
    String line = null;
    String line1 = null;
    String result = "";
    String result1 = "";
    public static ProgressDialog progress;
    public static ProgressDialog progress1;
    //int length;
    View rootView;
    public static String teachername[] = new String[5000];
    public static String teacheremail[] = new String[5000];
    public static ArrayList<String> teachernameget = new ArrayList<String>();
    public static ArrayList<String> teacheremailget = new ArrayList<String>();
    AutoCompleteTextView actv;
    String tpocontact = "";
    String examcontact = "";
    String discontact = "";
    String eventcontact = "";
    String csecontact = "";
    String mecontact = "";
    String cecontact = "";
    String eecontact = "";
    String eccontact = "";
    String fdcontact = "";
    String firstcontact = "";

    public contactcollege() {
        // Required empty public constructorpublic static ArrayList<String>teacherlist;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentV
        home.key = 1;
        teachernameget.clear();
        teacheremailget.clear();
        rootView = inflater.inflate(R.layout.fragment_contactcollege, container, false);
        AutoCompleteTextView actv;
        if (AppStatus.getInstance(getActivity()).isOnline()) {
            progress1 = new ProgressDialog(getActivity());
            MyAsyncTaskContact task = new MyAsyncTaskContact();
            task.execute(links.contactcollegeNumbers);
        } else {
            ProgressMessage.NotConnected(getActivity());
        }


        Button emailfact = (Button) rootView.findViewById(R.id.sendmail);
        emailfact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) rootView.findViewById(R.id.temail);
                EditText message = (EditText) rootView.findViewById(R.id.tmsg);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                String to[] = {textView.getText().toString()};
                String msg = message.getText().toString();
                intent.putExtra(Intent.EXTRA_EMAIL, to);
                intent.putExtra(Intent.EXTRA_SUBJECT, "This Email is Send By: " + Login.name);
                intent.putExtra(Intent.EXTRA_TEXT, msg);
                intent.setType("message/rfc822");
                Intent chooser = Intent.createChooser(intent, "Send Email");
                startActivity(chooser);
            }
        });


        return rootView;
    }


    public class MyAsyncTask extends AsyncTask<String, Void, String> {
        String response = "";

        @Override
        protected String doInBackground(String... params) {

            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(params[0]);
                String text = "";
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

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray jArray1 = new JSONArray(s);
                for (int i = 0; i < jArray1.length(); i++) {
                    JSONObject json_data = jArray1.getJSONObject(i);
                    teachernameget.add(json_data.getString("Teacher_Name"));
                    teacheremailget.add(json_data.getString("Teacher_Email"));
                }
            } catch (Exception e) {
                Log.e("Error",e.getMessage());
                e.printStackTrace();
            }
            actv = (AutoCompleteTextView) rootView.findViewById(R.id.tname);
            ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.select_dialog_item, teachernameget);
            actv.setThreshold(1);
            actv.setAdapter(adapter);
            actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = (TextView) rootView.findViewById(R.id.temail);
                    textView.setText(teacheremailget.get(position));

                }
            });
            super.onPostExecute(s);
            progress.dismiss();
        }
    }


    public class MyAsyncTaskContact extends AsyncTask<String, Void, String> {

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
            ProgressMessage.Progress_pre(progress1);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    tpocontact = json_data.getString("tpo");
                    examcontact = json_data.getString("exam");
                    discontact = json_data.getString("discipline");
                    eventcontact = json_data.getString("event");
                    csecontact = json_data.getString("csehod");
                    mecontact = json_data.getString("mehod");
                    cecontact = json_data.getString("cehod");
                    eecontact = json_data.getString("eehod");
                    eccontact = json_data.getString("echod");
                    fdcontact = json_data.getString("fdhod");
                    firstcontact = json_data.getString("firstcontact");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            progress1.dismiss();
            super.onPostExecute(s);
            if (AppStatus.getInstance(getActivity()).isOnline()) {
                progress = new ProgressDialog(getActivity());
                MyAsyncTask task1 = new MyAsyncTask();
                task1.execute(links.contact);
            } else {
                ProgressMessage.NotConnected(getActivity());
            }

            Button tpo = (Button) rootView.findViewById(R.id.tpo);
            tpo.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + tpocontact));
                            getContext().startActivity(intent);
                        }
                    });

            Button examc = (Button) rootView.findViewById(R.id.examc);
            examc.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + examcontact));
                            getContext().startActivity(intent);
                        }
                    });

            Button disc = (Button) rootView.findViewById(R.id.dc);
            disc.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + discontact));
                            getContext().startActivity(intent);
                        }
                    });


            Button event = (Button) rootView.findViewById(R.id.ev);
            event.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + eventcontact));
                            getContext().startActivity(intent);
                        }
                    });

            Button cse = (Button) rootView.findViewById(R.id.cse);
            cse.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + csecontact));
                            getContext().startActivity(intent);
                        }
                    });

            Button me = (Button) rootView.findViewById(R.id.me);
            me.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + mecontact));
                            getContext().startActivity(intent);
                        }
                    });

            Button ce = (Button) rootView.findViewById(R.id.ce);
            ce.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + cecontact));
                            getContext().startActivity(intent);
                        }
                    });

            Button ee = (Button) rootView.findViewById(R.id.ee);
            ee.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + eecontact));
                            getContext().startActivity(intent);
                        }
                    });

            Button ec = (Button) rootView.findViewById(R.id.ec);
            ec.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + eccontact));
                            getContext().startActivity(intent);
                        }
                    });

            Button fd = (Button) rootView.findViewById(R.id.fd);
            fd.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + fdcontact));
                            getContext().startActivity(intent);
                        }
                    });

            Button fy = (Button) rootView.findViewById(R.id.fy);
            fy.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + firstcontact));
                            getContext().startActivity(intent);
                        }
                    });
        }
    }

}
