package shivam.cvm.ui.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import shivam.cvm.MyTest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class developercontact extends Fragment {


    public developercontact() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_developercontact, container, false);
        home.key = 1;

        Button btn = (Button) rootView.findViewById(R.id.callmebtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callme();
            }
        });

        Button email = (Button) rootView.findViewById(R.id.emailmebtn);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailme();
            }
        });


        return rootView;
    }

    public void callme() {
        String shivamcontact = "+917023271505";
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + shivamcontact));
        getContext().startActivity(intent);
    }

    public void emailme() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        String to[] = {"shivam.mathur10@gmail.com"};
        String msg = "";
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hey Shivam. I want to Contact You.");
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        intent.setType("message/rfc822");
        Intent chooser = Intent.createChooser(intent, "Send Email");
        startActivity(chooser);
    }

}
