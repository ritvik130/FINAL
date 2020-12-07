package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servicenovigrad.data.Class.Employee;
import com.example.servicenovigrad.data.Class.NovService;
import com.example.servicenovigrad.data.Class.NovServiceCheckList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WelcomePageEmployee extends AppCompatActivity implements ServiceProvider {
    DatabaseReference databaseService,databaseUsers;
    List<NovService> servicesList;
    List<String> daysList;
    TextView welcomeTxt;
    ListView listViewService;
    Button saveBtn,requestBtn;
    LinearLayout checkboxes;
    Employee employee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page_employee);

        databaseService = FirebaseDatabase.getInstance().getReference("services");
        databaseUsers = FirebaseDatabase.getInstance().getReference("users").child(Common.userName);

        servicesList = new ArrayList<>();
        daysList = new ArrayList<>();

        welcomeTxt = findViewById(R.id.welcomeText2);
        saveBtn = findViewById(R.id.saveBtn);
        requestBtn = findViewById(R.id.requestBtn);
        checkboxes = findViewById(R.id.checkboxes);
        listViewService = (ListView) findViewById(R.id.listViewService);

        welcomeTxt.setText("Welcome "+Common.userName);

        employee = new Employee();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(daysList.size() > 0)
                    daysList.clear();

                for (int i = 0; i < checkboxes.getChildCount(); i++) {
                    CheckBox v = (CheckBox) checkboxes.getChildAt(i);
                    if(v.isChecked()){
                        daysList.add(v.getText().toString());
                    }
                }
                employee.setDaysList(daysList);
                if(employee.getServicesList() == null){
                    Toast.makeText(WelcomePageEmployee.this, "You must select a service", Toast.LENGTH_LONG).show();
                }else if(daysList.size() == 0){
                    Toast.makeText(WelcomePageEmployee.this, "You must select a day", Toast.LENGTH_LONG).show();
                }else{
                    databaseUsers.child("services").setValue(employee.getServicesList());
                    databaseUsers.child("workingdays").setValue(employee.getDaysList());
                    Intent intent = new Intent(getApplicationContext(), Employee_Home_Page.class);
                    startActivityForResult (intent,0);
                    finish();
                }
            }
        });
        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RequestsActivity.class);
                startActivityForResult (intent,0);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseService.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                servicesList.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    NovService novServices = postSnapshot.getValue(NovService.class);
                    servicesList.add(novServices);
                }
                NovServiceCheckList usersAdaptor = new NovServiceCheckList(WelcomePageEmployee.this, servicesList,WelcomePageEmployee.this);
                listViewService.setAdapter(usersAdaptor);
            }
            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });
    }


    @Override
    public void servicesProvided(List<String> servicesId) {
        employee.setServicesList(servicesId);
    }
}