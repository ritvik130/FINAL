package com.example.servicenovigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;;import com.example.servicenovigrad.data.Class.NovService;
import com.example.servicenovigrad.data.Class.NovServiceCheckList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Employee_Home_Page extends AppCompatActivity {
    Button manageService, manageDays;
    DatabaseReference databaseEmployeeServices, databaseServices;
    List<NovService> servicesList;
    RecyclerView requestRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home_page);
        databaseEmployeeServices = FirebaseDatabase.getInstance().getReference("users").child(Common.userName).child("services");
        databaseServices = FirebaseDatabase.getInstance().getReference("services");
        manageService = (Button) findViewById(R.id.manageServicesBtn);
        manageDays = (Button) findViewById(R.id.manageDaysBtn);
        requestRecyclerView =  findViewById(R.id.recyclerView);
        servicesList = new ArrayList<>();
        manageService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Employee_Manage_Services.class);
                startActivityForResult(intent, 0);
            }
        });
        manageDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Employee_Manage_Days.class);
                startActivityForResult(intent, 0);
            }
        });

    }
}