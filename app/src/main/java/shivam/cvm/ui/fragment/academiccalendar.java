package shivam.cvm.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.BufferedReader;

import shivam.cvm.AppStatus;
import shivam.cvm.MyTest.R;
import shivam.cvm.ui.ProgressMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class academiccalendar extends Fragment {
    public static ProgressDialog progressDialog;
    BufferedReader reader = null;
    StringBuilder sb = new StringBuilder();
    TextView tv;
    String line = null;
    String result = "";

    public academiccalendar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        home.key = 1;
        View rootView = inflater.inflate(R.layout.fragment_academiccalendar, container, false);
        if (AppStatus.getInstance(getActivity()).isOnline()) {
            SubsamplingScaleImageView img = (SubsamplingScaleImageView) rootView.findViewById(R.id.pic);
            img.setImage(ImageSource.resource(R.drawable.cal));

        } else {
            ProgressMessage.NotConnected(getActivity());
        }
        return rootView;
    }

}

