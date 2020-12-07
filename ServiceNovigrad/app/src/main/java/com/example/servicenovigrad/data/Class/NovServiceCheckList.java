package com.example.servicenovigrad.data.Class;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.ServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class NovServiceCheckList extends ArrayAdapter<NovService>{
    private Activity context;
    List<NovService> NovServices;
    List<String> servicesList;
    ServiceProvider serviceProvider;

    public NovServiceCheckList(Activity context, List<NovService> NovServices,ServiceProvider serviceProvider) {
        super(context, R.layout.activity_layout_service_check_list, NovServices);
        this.context = context;
        this.NovServices = NovServices;
        this.serviceProvider = serviceProvider;
        this.servicesList = new ArrayList<>();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_layout_service_check_list, null, true);

        CheckBox checkBoxServiceName = (CheckBox) listViewItem.findViewById(R.id.checkBoxServiceName);
        NovService currentService = NovServices.get(position);
        servicesList.add(NovServices.get(position).getId());
        serviceProvider.servicesProvided(servicesList);
        checkBoxServiceName.setText(String.valueOf(currentService.getServiceName()));
        checkBoxServiceName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked && !servicesList.contains(NovServices.get(position).getId())) {
                    servicesList.add(NovServices.get(position).getId());
                }else{
                    servicesList.remove(NovServices.get(position).getId());
                }
                serviceProvider.servicesProvided(servicesList);
            }
        });
        return listViewItem;
    }
}
