package shivam.cvm.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import shivam.cvm.AppStatus;
import shivam.cvm.ui.ProgressMessage;
import shivam.cvm.ui.activity.Login;
import shivam.cvm.MyTest.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class stuoneview extends Fragment {


    public stuoneview() {
        // Required empty public constructor
    }


    public static ProgressDialog progress;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        home.key=1;

        View rootView = inflater.inflate(R.layout.fragment_stuoneview, container, false);
        WebView browser = (WebView)rootView.findViewById(R.id.webview);

        if (AppStatus.getInstance(this.getActivity()).isOnline())
        {
            progress = new ProgressDialog(getActivity());
            progress.setTitle("Please Wait");
            progress.setMessage("Loading.....");
            progress.show();
            browser.loadUrl("http://14.139.244.54/oneview.php?eno=" + Login.rollno);
            WebSettings webSettings = browser.getSettings();
            webSettings.setJavaScriptEnabled(true);
            browser.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    progress.dismiss();
                }
            });
        }
        else
        {
            ProgressMessage.NotConnected(getActivity());
        }

        return rootView;
        }
    }