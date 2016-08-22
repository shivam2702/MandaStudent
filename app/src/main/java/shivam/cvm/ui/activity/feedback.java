package shivam.cvm.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import shivam.cvm.MyTest.R;
import shivam.cvm.ui.fragment.links;

public class feedback extends AppCompatActivity {
    String review = "";
    public static ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        progress = new ProgressDialog(this);
        ImageButton happy =(ImageButton)findViewById(R.id.happy);
        ImageButton neutral =(ImageButton)findViewById(R.id.neutral);
        ImageButton sad =(ImageButton)findViewById(R.id.sad);
        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                review ="Happy";
                MyAsyncTask task = new MyAsyncTask();
                task.execute(links.feedback);
            }
        });
        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                review="Neutral";
                MyAsyncTask task = new MyAsyncTask();
                task.execute(links.feedback);
            }
        });
        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                review="Sad";
                MyAsyncTask task = new MyAsyncTask();
                task.execute(links.feedback);
            }
        });
    }
    public class MyAsyncTask extends AsyncTask<String, Void, String> {
        String response = "";

        @Override
        protected String doInBackground(String... params) {

            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(params[0]);
                String text = "param1=" + URLEncoder.encode(Login.rollno, "UTF-8") + "&param2=" + URLEncoder.encode(Login.name, "UTF-8")
                        + "&param3=" + URLEncoder.encode(review, "UTF-8")+"&param4=" + URLEncoder.encode(Login.appversion, "UTF-8");
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
            progress.setTitle("Posting Review");
            progress.setMessage("Thank you.");
            progress.show();
            progress.setCancelable(false);
        }

        protected void onPostExecute(String s) {
            String changeStatus=null;
            try {
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    changeStatus = json_data.getString("status");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(changeStatus.equals("pass"))
            {
                Toast.makeText(feedback.this, "Thank You for your Feedback.", Toast.LENGTH_LONG).show();
                Intent j = new Intent(feedback.this, MainActivity.class);
                finish();
                startActivity(j);
            }
            else
            {
                progress.dismiss();
                Toast.makeText(feedback.this,"Sorry our server is down. Please review us later.",Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(s);
        }
    }

}
