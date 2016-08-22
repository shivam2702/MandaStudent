package shivam.cvm.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import br.liveo.Model.HelpLiveo;
import br.liveo.interfaces.OnItemClickListener;
import br.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import br.liveo.navigationliveo.NavigationLiveo;
import shivam.cvm.MyTest.DefaultExceptionHandler;
import shivam.cvm.MyTest.R;
import shivam.cvm.ui.fragment.MainFragment;
import shivam.cvm.ui.fragment.academiccalendar;
import shivam.cvm.ui.fragment.assignment;
import shivam.cvm.ui.fragment.changepassword;
import shivam.cvm.ui.fragment.contactcollege;
import shivam.cvm.ui.fragment.developercontact;
import shivam.cvm.ui.fragment.eventlist;
import shivam.cvm.ui.fragment.home;
import shivam.cvm.ui.fragment.lab;
import shivam.cvm.ui.fragment.leavestatus;
import shivam.cvm.ui.fragment.leavesubmit;
import shivam.cvm.ui.fragment.lecture;
import shivam.cvm.ui.fragment.links;
import shivam.cvm.ui.fragment.midterm;
import shivam.cvm.ui.fragment.noticeboard;
import shivam.cvm.ui.fragment.stuoneview;
import shivam.cvm.ui.fragment.totallab;
import shivam.cvm.ui.fragment.totallecture;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends NavigationLiveo implements OnItemClickListener {

    private HelpLiveo mHelpLiveo;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    public void onInt(Bundle bundle) {
        if(Login.name.equals(null)||Login.name.equals(""))
        {
            progress1 = new ProgressDialog(MainActivity.this);
            DeRegister deRegister = new DeRegister();
            deRegister.execute(links.deregister);
        }
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this,
                MainActivity.class));
        // User Information
        this.userName.setText(Login.name);
        this.userEmail.setText(Login.rollno);
        this.userPhoto.setImageResource(R.drawable.mitlogofinal);
//        Picasso.with(this).invalidate("mit.png");
//        Picasso.with(this).load(links.logo).into(this.userPhoto);
//        this.userPhoto.setImageResource(R.drawable.ic_rudsonlive);
        this.userBackground.setImageResource(R.drawable.ic_user_background_first);
        this.userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // Creating items navigation
        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.add("Home");
        mHelpLiveo.addSubHeader("Academics"); //0 Item subHeader
        mHelpLiveo.add("Mid Term Records"); //1
        mHelpLiveo.add("Assignment Records");//2
        mHelpLiveo.add("Student One View");//3

        mHelpLiveo.addSubHeader("Attendance"); //4  //Item subHeader
        mHelpLiveo.add("Total Lecture Attendance"); //5
        mHelpLiveo.add("Lecture Attendance"); //6
        mHelpLiveo.add("Total Lab Attendance"); //7
        mHelpLiveo.add("Lab Attendance");//8

        mHelpLiveo.addSubHeader("College"); //9 //Item subHeader
        mHelpLiveo.add("Notice Board");//10
        mHelpLiveo.add("Academic Calendar");//11
        mHelpLiveo.add("Events List");//12

        //mHelpLiveo.addSubHeader("Library");//13 //Item subHeader
        //mHelpLiveo.add("Book Bank Account");//14
        //mHelpLiveo.add("Library Account");//15

        mHelpLiveo.addSubHeader("Leave");//19 //Item subHeader
        mHelpLiveo.add("Leave Submit");//20
        mHelpLiveo.add("Leave Status");//21

        mHelpLiveo.addSubHeader("Contact");//16 //Item subHeader
        mHelpLiveo.add("College");//17
        mHelpLiveo.add("Developer");//18

        mHelpLiveo.addSeparator(); // Item separator

        mHelpLiveo.add("Change Password");


        with(this).startingPosition(0) //Starting position in the list
                .addAllHelpItem(mHelpLiveo.getHelp())
                .colorNameSubHeader(R.color.nliveo_blue_colorPrimary)
                .colorItemSelected(R.color.nliveo_blue_colorPrimary)
                .footerItem("Logout", R.mipmap.ic_settings_black_24dp)
                .setOnClickUser(onClickPhoto)
                .setOnPrepareOptionsMenu(onPrepare)
                .setOnClickFooter(onClickFooter)
                .build();

        int position = this.getCurrentPosition();
        this.setElevationToolBar(position != 2 ? 15 : 0);
    }

    @Override
    public void onItemClick(int position) {
        Fragment mFragment;
        FragmentManager mFragmentManager = getSupportFragmentManager();

        switch (position) {
            case 0:
                mFragment = new home();
                break;
            case 2:
                mFragment = new midterm();
                break;
            case 3:
                mFragment = new assignment();
                break;
            case 4:
                mFragment = new stuoneview();
                break;
            case 6:
                mFragment = new totallecture();
                break;
            case 7:
                mFragment = new lecture();
                break;
            case 8:
                mFragment = new totallab();
                break;
            case 9:
                mFragment = new lab();
                break;
            case 11:
                mFragment = new noticeboard();
                break;
            case 12:
                mFragment = new academiccalendar();
                break;
            case 13:
                mFragment = new eventlist();
                break;
//            case 15:
//                Toast.makeText(MainActivity.this, "We are working on it", Toast.LENGTH_SHORT).show();
//                //mFragment = new libaccount();
//                mFragment = null;
//                break;
//            case 16:
//                Toast.makeText(MainActivity.this, "We are working on it", Toast.LENGTH_SHORT).show();
//                //mFragment = new libbooks();
//                mFragment = null;
//                break;
            case 15:
                mFragment = new leavesubmit();
                break;
            case 16:
                mFragment = new leavestatus();
                break;
            case 18:
                mFragment = new contactcollege();
                break;
            case 19:
                mFragment = new developercontact();
                break;
            case 21:
                mFragment = new changepassword();
                break;
            default:
                mFragment = MainFragment.newInstance(mHelpLiveo.get(position).getName());
                break;
        }

        if (mFragment != null) {
            mFragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
        }

        setElevationToolBar(position != 2 ? 15 : 0);
    }

    private OnPrepareOptionsMenuLiveo onPrepare = new OnPrepareOptionsMenuLiveo() {
        @Override
        public void onPrepareOptionsMenu(Menu menu, int position, boolean visible) {
        }
    };

    private View.OnClickListener onClickPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


        }
    };

    public class DeRegister extends AsyncTask<String, Void, String> {
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
            progress1.setTitle("Please Wait");
            progress1.setMessage("We are Logging you out..");
            progress1.setCancelable(false);
            progress1.show();
        }

        protected void onPostExecute(String s) {
            pref = getApplicationContext().getSharedPreferences("MyPref", 0);
            editor = pref.edit();
            editor.putBoolean("keepmelogin", false);
            editor.commit();
            progress1.dismiss();
            finishAffinity();
            Intent j = new Intent(getApplicationContext(), Login.class);
            startActivity(j);
            Toast.makeText(MainActivity.this, "Hope to see you soon.....", LENGTH_SHORT).show();
            super.onPostExecute(s);
        }
    }

    public static ProgressDialog progress1;


    private View.OnClickListener onClickFooter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progress1 = new ProgressDialog(MainActivity.this);
            DeRegister deRegister = new DeRegister();
            deRegister.execute(links.deregister);
        }
    };

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (home.key == 1) {
            Fragment mFragment;
            FragmentManager mFragmentManager = getSupportFragmentManager();
            mFragment = new home();
            mFragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
        } else {
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finishAffinity();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
            if (id==R.id.feedback)
            {
                Intent j = new Intent(this, feedback.class);
                startActivity(j);
                return true;
            }
        return true;
    }
}