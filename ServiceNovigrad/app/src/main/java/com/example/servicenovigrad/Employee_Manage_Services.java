package com.example.servicenovigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.servicenovigrad.data.Class.NovService;
import com.example.servicenovigrad.data.Class.NovServiceCheckList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Employee_Manage_Services extends AppCompatActivity implements ServiceProvider{
    DatabaseReference databaseEmployeeServices,databaseServices;
    List<NovService> servicesList;
    ListView manageService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee__manage__services);
        databaseEmployeeServices = FirebaseDatabase.getInstance().getReference("users").child(Common.userName).child("services");
        databaseServices = FirebaseDatabase.getInstance().getReference("services");
        servicesList = new ArrayList<>();
        manageService = (ListView) findViewById(R.id.servicesList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseEmployeeServices.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                System.out.println(dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
//                    servicesList.clear();
                    databaseServices.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.child(postSnapshot.getValue(String.class)).child("serviceName").getValue(String.class);
                            NovService novServices = snapshot.child(postSnapshot.getValue(String.class)).getValue(NovService.class);
                            System.out.println(novServices.getServiceName());
                            servicesList.add(novServices);
                            NovServiceCheckList usersAdaptor = new NovServiceCheckList(Employee_Manage_Services.this, servicesList,Employee_Manage_Services.this);
                            manageService.setAdapter(usersAdaptor);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });
    }

    @Override
    public void servicesProvided(List<String> servicesId) {

    }
}