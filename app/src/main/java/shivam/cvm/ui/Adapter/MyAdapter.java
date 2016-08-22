package shivam.cvm.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import shivam.cvm.MyTest.R;
import shivam.cvm.ui.fragment.home;

/**
 * Created by Shivam on 12/16/2015.
 */
public class MyAdapter extends BaseExpandableListAdapter {
    private List<String> header_titles;
    private HashMap<String,List<String>> child_titles;
    private Context ctx;
    public MyAdapter(Context ctx, List<String> header_titles, HashMap<String, List<String>> child_titles)
    {
        this.ctx = ctx;
        this.header_titles = header_titles;
        this.child_titles = child_titles;
    }

    @Override
    public int getGroupCount() {
        return header_titles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child_titles.get(header_titles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return header_titles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child_titles.get(header_titles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = (String)this.getGroup(groupPosition);
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.submitname,null);
        }
        TextView textView = (TextView)convertView.findViewById(R.id.subjectname);
        textView.setText(title);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String title = (String)this.getChild(groupPosition, childPosition);
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.numberslayout,null);
        }
        String output="";
        if(home.use.equals("Assignment "))
        {
            if(title.equals("1"))
            {
                output="Received";
            }
            else
            {
                output="Pending";
            }
        }
        int num=0;
        if(home.use.equals("MidTerm "))
        {
            try
            {
                num = Integer.parseInt(title);
                if(num>=0 && num<=4)
                {
                    output = "C Grade";
                }
                if(num>4 && num<=7)
                {
                    output = "B Grade";
                }
                if(num>7)
                {
                    output = "A Grade";
                }
            }
            catch (Exception e)
            {
                if (title.equals("-"))
                {
                    output = "Pending";
                }
                else
                {
                    output = "Absent";
                }
            }
            //output=title+"/10";
        }

        TextView textView = (TextView)convertView.findViewById(R.id.Numbers);
        textView.setText(output);

        TextView text = (TextView)convertView.findViewById(R.id.numberof);
        int pos = childPosition+1;
        String utilise = home.use;
        text.setText(utilise+pos);
        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
