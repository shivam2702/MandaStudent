package shivam.cvm.ui.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Scanner;

import shivam.cvm.AppStatus;
import shivam.cvm.MyTest.R;
import shivam.cvm.ui.Adapter.AttendanceAdapter;
import shivam.cvm.ui.ProgressMessage;
import shivam.cvm.ui.activity.Login;

public class lecture extends Fragment {

    public lecture() {
        // Required empty public constructor
    }
    View rootView;
    String date="";
    public static ProgressDialog progress;
    Button dateselected;
    ListView list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        home.key=1;

        rootView = inflater.inflate(R.layout.fragment_lecture, container, false);
        list = (ListView) rootView.findViewById(R.id.lecture);
        dateselected = (Button)rootView.findViewById(R.id.submitdate);
        dateselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getActivity(),new mDateSetListener(), mYear, mMonth, mDay);
                dialog.show();
            }
        });
        progress = new ProgressDialog(rootView.getContext());
        return rootView;
    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            String monthdate="";
            switch (mDay)
            {
                case 1: monthdate = "01";
                    break;
                case 2: monthdate = "02";
                    break;
                case 3: monthdate = "03";
                    break;
                case 4: monthdate = "04";
                    break;
                case 5: monthdate = "05";
                    break;
                case 6: monthdate = "06";
                    break;
                case 7: monthdate = "07";
                    break;
                case 8: monthdate = "08";
                    break;
                case 9: monthdate = "09";
                    break;
                default: monthdate=""+mDay;
            }
            String month="";
            switch (mMonth)
            {
                case 0: month = "01";
                    break;
                case 1: month = "02";
                    break;
                case 2: month = "03";
                    break;
                case 3: month = "04";
                    break;
                case 4: month = "05";
                    break;
                case 5: month = "06";
                    break;
                case 6: month = "07";
                    break;
                case 7: month = "08";
                    break;
                case 8: month = "09";
                    break;
                case 9: month = "10";
                    break;
                case 10: month = "11";
                    break;
                case 11: month = "12";
                    break;

                default: month=""+mMonth;
            }

            dateselected.setText(monthdate+ "/" + (month) + "/"+ mYear);
            date=monthdate+ "/" + (month) + "/"+ mYear;
            if (AppStatus.getInstance(getActivity()).isOnline())
            {
                MyAsyncTask task = new MyAsyncTask();
                task.execute(links.lecturebydate);
            }
            else
            {
                ProgressMessage.NotConnected(getActivity());
            }
        }
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {
        String response = "";
        String classs= Login.stuclass;
        String rollno= Login.rollno;
        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(params[0]);
                String text = "param1=" + URLEncoder.encode(rollno, "UTF-8") + "&param2=" + URLEncoder.encode(classs, "UTF-8") + "&param3=" + URLEncoder.encode(date, "UTF-8") ;
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
            String subject1Total="",subject2Total="",subject3Total="",subject4Total="",subject5Total="",subject6Total="";
            String subject1Obtain="",subject2Obtain="",subject3Obtain="",subject4Obtain="",subject5Obtain="",subject6Obtain="";
            try {
                JSONArray jArray = new JSONArray(response);
                for(int i=0;i<jArray.length();i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    subject1Total= json_data.getString("maxatten1");
                    subject1Obtain= json_data.getString("atten1");
                    subject2Total= json_data.getString("maxatten2");
                    subject2Obtain= json_data.getString("atten2");
                    subject3Total= json_data.getString("maxatten3");
                    subject3Obtain= json_data.getString("atten3");
                    subject4Total= json_data.getString("maxatten4");
                    subject4Obtain= json_data.getString("atten4");
                    subject5Total= json_data.getString("maxatten5");
                    subject5Obtain= json_data.getString("atten5");
                    subject6Total= json_data.getString("maxatten6");
                    subject6Obtain= json_data.getString("atten6");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String sub1="Reload";
            String sub2="Reload";
            String sub3="Reload";
            String sub4="Reload";
            String sub5="Reload";
            String sub6="Reload";


            if(subject1Total.equals("0"))
            {
                sub1="No Lecture";
            }
            else
            {
                if (subject1Obtain.equals("0"))
                {
                    sub1="Absent";
                }
                else if (subject1Obtain.equals("1"))
                {
                    sub1="Present";
                }
            }


            if(subject2Total.equals("0"))
            {
                sub2="No Lecture";
            }
            else
            {
                if (subject2Obtain.equals("0"))
                {
                    sub2="Absent";
                }
                else if (subject2Obtain.equals("1"))
                {
                    sub2="Present";
                }
            }


            if(subject3Total.equals("0"))
            {
                sub3="No Lecture";
            }
            else
            {
                if (subject3Obtain.equals("0"))
                {
                    sub3="Absent";
                }
                else if (subject3Obtain.equals("1"))
                {
                    sub3="Present";
                }
            }


            if(subject4Total.equals("0"))
            {
                sub4="No Lecture";
            }
            else
            {
                if (subject4Obtain.equals("0"))
                {
                    sub4="Absent";
                }
                else  if (subject4Obtain.equals("1"))
                {
                    sub4="Present";
                }
            }


            if(subject5Total.equals("0"))
            {
                sub5="No Lecture";
            }
            else
            {
                if (subject5Obtain.equals("0"))
                {
                    sub5="Absent";
                }
                else  if (subject5Obtain.equals("1"))
                {
                    sub5="Present";
                }
            }


            if(subject6Total.equals("0"))
            {
                sub6="No Lecture";
            }
            else
            {
                if (subject6Obtain.equals("0"))
                {
                    sub6="Absent";
                }
                else  if (subject6Obtain.equals("1"))
                {
                    sub6="Present";
                }
            }
            home.att.clear();
            home.att.add(sub1);
            home.att.add(sub2);
            home.att.add(sub3);
            home.att.add(sub4);
            home.att.add(sub5);
            home.att.add(sub6);
            list.setAdapter(new AttendanceAdapter(rootView.getContext(), home.subjectlist));
            super.onPostExecute(s);
            progress.dismiss();
        }
    }
}
