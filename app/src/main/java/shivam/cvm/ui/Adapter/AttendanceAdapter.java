package shivam.cvm.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import shivam.cvm.MyTest.R;
import shivam.cvm.ui.fragment.home;
import shivam.cvm.ui.fragment.totallecture;

/**
 * Created by Shivam on 12/17/2015.
 */
public class AttendanceAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;

    public AttendanceAdapter(Context context, ArrayList<String> values) {
        super(context, R.layout.attendanceperforma, values);
        this.context = context;
        this.values = values;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.attendanceperforma, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.subjectnameatt);
        TextView attview = (TextView) rowView.findViewById(R.id.att);
        textView.setText(values.get(position));
        attview.setText(home.att.get(position));
        return rowView;
    }

}
