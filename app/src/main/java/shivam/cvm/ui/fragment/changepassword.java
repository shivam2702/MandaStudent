package shivam.cvm.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Scanner;

import shivam.cvm.MyTest.R;
import shivam.cvm.ui.ProgressMessage;
import shivam.cvm.ui.activity.Login;

public class changepassword extends Fragment {


    public changepassword() {
        // Required empty public constructor
    }

    public static String finalnewpassword;
    public static ProgressDialog progress;
    public static View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_changepassword, container, false);
        final EditText newpass1  = (EditText)rootView.findViewById(R.id.newpassword);
        final EditText newpass2  = (EditText)rootView.findViewById(R.id.confirmpassword);
        Button change = (Button)rootView.findViewById(R.id.changeit);
        progress = new ProgressDialog(rootView.getContext());
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                String password1 = newpass1.getText().toString();
                String password2 = newpass2.getText().toString();
                if(password1.equals("")||password1.equals(null))
                {
                    SnackbarManager.show(
                            Snackbar.with(getContext()) // context
                                    .text("Blank password? Are you kidding me??") // text to display
                                    .actionLabel("Close") // action button label
                                    .actionListener(new ActionClickListener() {
                                        @Override
                                        public void onActionClicked(Snackbar snackbar) {
                                            snackbar.dismiss();
                                        }
                                    }) // action button's ActionClickListener
                            , getActivity()); // activity where it is displayed
                }
                else
                {
                    if(password1.equals(password2))
                    {
                        finalnewpassword=password1;
                        MyAsyncTask task = new MyAsyncTask();
                        task.execute(links.changepassword);
                    }
                    else
                    {
                        SnackbarManager.show(
                                Snackbar.with(getContext()) // context
                                        .text("Both Passwords are different") // text to display
                                        .actionLabel("Close") // action button label
                                        .actionListener(new ActionClickListener() {
                                            @Override
                                            public void onActionClicked(Snackbar snackbar) {
                                                snackbar.dismiss();
                                            }
                                        }) // action button's ActionClickListener
                                , getActivity()); // activity where it is displayed
                    }
                }

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
                String text = "param1=" + URLEncoder.encode(Login.rollno, "UTF-8") + "&param2=" + URLEncoder.encode(finalnewpassword, "UTF-8");
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
            String changeStatus=null;
            try {
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    changeStatus = json_data.getString("Status");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(changeStatus.equals("Sucess"))
            {
                Toast.makeText(getContext(),"Password Successfully Changed,\nPlease Re-login with new Password",Toast.LENGTH_LONG).show();
                SharedPreferences pref;
                SharedPreferences.Editor editor;
                pref = getContext().getSharedPreferences("MyPref", 0);
                editor = pref.edit();
                editor.putBoolean("keepmelogin", false);
                editor.commit();
                progress.dismiss();
                Intent j = new Intent(getActivity(), Login.class);
                getActivity().finish();
                startActivity(j);
            }
            else
            {
                progress.dismiss();
                Toast.makeText(getContext(),"Update failed.",Toast.LENGTH_LONG).show();
            }
             super.onPostExecute(s);
        }
    }

}

