package shivam.cvm.ui.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Scanner;

import shivam.cvm.AppStatus;
import shivam.cvm.MyTest.R;
import shivam.cvm.ui.ProgressMessage;
import shivam.cvm.ui.activity.Login;

/**
 * A simple {@link Fragment} subclass.
 */
public class leavesubmit extends Fragment {

    public static ProgressDialog progress;
    Button dateselected;
    View rootView;

    public leavesubmit() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        home.key = 1;

        rootView = inflater.inflate(R.layout.fragment_leavesubmit, container, false);
        dateselected = (Button) rootView.findViewById(R.id.dateselect);
        dateselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                System.out.println("the selected " + mDay);
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new mDateSetListener(), mYear, mMonth, mDay);
                dialog.show();

            }
        });

        final Button submit = (Button) rootView.findViewById(R.id.submitleave);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText reason = (EditText) rootView.findViewById(R.id.reason);
                EditText noofdays = (EditText) rootView.findViewById(R.id.noofdays);
                if (submit.getText().toString().equals("Select Date") || reason.getText().toString().equals("") || reason.getText().toString().equals(null) || noofdays.getText().toString().equals("") || noofdays.getText().toString().equals(null) || noofdays.getText().toString().equals("0")) {
                    if (reason.getText().toString().equals("") || reason.getText().toString().equals(null)) {
                        SnackbarManager.show(
                                Snackbar.with(getContext()) // context
                                        .text("Leave without a reason?") // text to display
                                        .duration(Snackbar.SnackbarDuration.LENGTH_LONG) // action button's ActionClickListener
                                , getActivity());
                    }
                    if (noofdays.getText().toString().equals("") || noofdays.getText().toString().equals(null) || noofdays.getText().toString().equals("0")) {
                        SnackbarManager.show(
                                Snackbar.with(getContext()) // context
                                        .text("Leave for 0 days? Kidding??") // text to display
                                        .duration(Snackbar.SnackbarDuration.LENGTH_LONG) // action button's ActionClickListener
                                , getActivity());
                    }

                } else {
                    if (AppStatus.getInstance(getActivity()).isOnline()) {
                        progress = new ProgressDialog(getActivity());
                        MyAsyncTask task = new MyAsyncTask();
                        task.execute(links.leavesend);
                    } else {
                        ProgressMessage.NotConnected(getActivity());
                    }

                }

            }

        });
        // Inflate the layout for this fragment
        return rootView;
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {
        String response = "";
        EditText reason = (EditText) rootView.findViewById(R.id.reason);
        EditText noofdays = (EditText) rootView.findViewById(R.id.noofdays);
        String rsn = reason.getText().toString();
        String date = dateselected.getText().toString();
        String nod = noofdays.getText().toString();

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(params[0]);
                String text = "param1=" + URLEncoder.encode(Login.rollno, "UTF-8") + "&param2=" + URLEncoder.encode(rsn, "UTF-8") + "&param3=" + URLEncoder.encode(date, "UTF-8") + "&param4=" + URLEncoder.encode(nod, "UTF-8") + "&param5=" + URLEncoder.encode(Login.name, "UTF-8");
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
            progress.dismiss();
            String status = "";
            try {
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    status = json_data.getString("Status");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (status.equals("pass")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Done");
                builder.setMessage("Leave Submitted Successfully.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reason.setText("");
                        noofdays.setText("");
                        dateselected.setText("Select Date");
                    }
                });
                AlertDialog dialog = builder.show();
                TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                messageText.setGravity(Gravity.CENTER);
                dialog.show();
            } else {
                Toast.makeText(getActivity(), "Leave Submitted Failed. Try Again Later.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }
    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            Time today = new Time(Time.getCurrentTimezone());
            today.setToNow();
            int date = today.monthDay;
            int month = today.month;
            int yearabhika = today.year;
            String dateaaj = yearabhika+""+month+""+date;
            String datechoosed = year+""+monthOfYear+""+dayOfMonth;
            if ((Integer.parseInt(dateaaj)) > Integer.parseInt(datechoosed))
            {
                SnackbarManager.show(
                        Snackbar.with(getContext())
                                .text("Back Date Leaves are not allowed")
                                .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                        , getActivity());
            }
            else
            {
                int mYear = year;
                int mMonth = monthOfYear;
                int mDay = dayOfMonth;
                dateselected.setText((mMonth + 1) + "/" + mDay + "/" + mYear);
            }
        }
    }
}