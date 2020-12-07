package com.example.servicenovigrad.data.Class;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.servicenovigrad.R;

import java.util.List;

public class WorkingDaysList extends ArrayAdapter<String> {
private Activity context;
    List<String> workingDaysList;

    public WorkingDaysList(Activity context, List<String> workingDaysList) {
        super(context,R.layout.activity_layout_service_list, workingDaysList);
        this.context = context;
        this.workingDaysList = workingDaysList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_layout_service_list, null, true);

        TextView textViewServiceName = (TextView) listViewItem.findViewById(R.id.textViewServiceName);
        textViewServiceName.setText(workingDaysList.get(position));

        return listViewItem;
    }
}
