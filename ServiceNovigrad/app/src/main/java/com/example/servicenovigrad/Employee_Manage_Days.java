package com.example.servicenovigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.servicenovigrad.data.Class.NovService;
import com.example.servicenovigrad.data.Class.NovServiceCheckList;
import com.example.servicenovigrad.data.Class.WorkingDaysList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Employee_Manage_Days extends AppCompatActivity {
    DatabaseReference databaseEmployeeDays;
    List<String> daysList;
    ListView manageDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee__manage__days);
        databaseEmployeeDays = FirebaseDatabase.getInstance().getReference("users").child(Common.userName).child("workingdays");
        daysList = new ArrayList<>();
        manageDays = (ListView) findViewById(R.id.daysList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseEmployeeDays.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    daysList.add(postSnapshot.getValue(String.class));
                    WorkingDaysList usersAdaptor = new WorkingDaysList(Employee_Manage_Days.this, daysList);
                    manageDays.setAdapter(usersAdaptor);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}