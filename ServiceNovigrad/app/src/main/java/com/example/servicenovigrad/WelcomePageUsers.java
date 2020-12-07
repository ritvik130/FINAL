package com.example.servicenovigrad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.servicenovigrad.data.Class.Address;
import com.example.servicenovigrad.data.Class.Branch;
import com.example.servicenovigrad.data.Class.NovLocalTime;
import com.example.servicenovigrad.data.Class.NovService;
import com.example.servicenovigrad.data.Class.NovServiceCheckList;
import com.example.servicenovigrad.data.Class.Rating;
import com.example.servicenovigrad.data.Class.ServicesRecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class WelcomePageUsers extends AppCompatActivity implements ServicesRecyclerViewAdapter.onclickListener, ServicesRecyclerViewAdapter.onLongClickListener {
    RecyclerView recyclerView;
    List<NovService> serviceList, suggestionList;
    NovService novService;
    ServicesRecyclerViewAdapter servicesRecyclerViewAdapter;
    TextView welcomeTextView;
    DatabaseReference databaseService;
    SearchView searchView;
    Button logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page_users);

        recyclerView = findViewById(R.id.servicesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceList = new ArrayList<>();
        suggestionList = new ArrayList<>();
        welcomeTextView = findViewById(R.id.welcomeText3);
        welcomeTextView.setText(Common.userName);
        databaseService = FirebaseDatabase.getInstance().getReference("services");
        searchView = findViewById(R.id.searchView);
        logOutButton = findViewById(R.id.logOutButton);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("Searchh", "Searching");
                if (suggestionList.size() > 0) {
                    suggestionList.clear();
                }
                for (NovService suggestionModel : serviceList) {
                    if (suggestionModel.getServiceName() != null && suggestionModel.getServiceName()
                            .trim()
                            .replace(" ", "")
                            .toLowerCase()
                            .contains(s.trim().replace(" ", "").toLowerCase())) {
                        suggestionList.add(suggestionModel);
                    } else if (suggestionModel.getBranchList().size() > 0) {
                        for (Branch branch : suggestionModel.getBranchList()) {
                            if (branch.getAddress().getStreet() != null &&
                                    branch.getAddress().getStreet().trim().replace(" ", "").toLowerCase()
                                            .contains(s.trim().replace(" ", "").toLowerCase())) {
                                suggestionList.add(suggestionModel);
                            }
                        }
                    }
                    setAdapter(suggestionList);
                }
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("Searchh", "Searching...");
                if (suggestionList.size() > 0) {
                    suggestionList.clear();
                }
                for (NovService suggestionModel : serviceList) {
                    if (suggestionModel.getServiceName() != null && suggestionModel.getServiceName()
                            .trim()
                            .replace(" ", "")
                            .toLowerCase()
                            .contains(s.trim().replace(" ", "").toLowerCase())) {
                        suggestionList.add(suggestionModel);
                    } else if (suggestionModel.getBranchList().size() > 0) {
                        for (Branch branch : suggestionModel.getBranchList()) {
                            if (branch.getAddress().getStreet() != null &&
                                    branch.getAddress().getStreet().trim().replace(" ", "").toLowerCase()
                                            .contains(s.trim().replace(" ", "").toLowerCase())) {
                                suggestionList.add(suggestionModel);
                            }
                        }

                    }

                    if (s == null || TextUtils.isEmpty(s)) {
                        if (suggestionList.size() > 0) {
                            suggestionList.clear();
                        }
                    }
                    setAdapter(suggestionList);

                }
                if (s == null || TextUtils.isEmpty(s)) {
                    if (suggestionList.size() > 0) {
                        suggestionList.clear();
                    }
                    setAdapter(serviceList);
                }
//                searchView.clearFocus();
                return false;
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CustomerRequest.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    private void setAdapter(List<NovService> list) {
        servicesRecyclerViewAdapter = new ServicesRecyclerViewAdapter(WelcomePageUsers.this, list, WelcomePageUsers.this, WelcomePageUsers.this);
        recyclerView.setAdapter(servicesRecyclerViewAdapter);
        servicesRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseService.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                serviceList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    NovService novServices = postSnapshot.getValue(NovService.class);
                    serviceList.add(novServices);
                }
                setAdapter(serviceList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onclick(int position) {
        Intent intent = new Intent(getApplicationContext(), CustomerRequest.class);
        intent.putExtra("serviceName", serviceList.get(position).getServiceName());
        intent.putExtra("serviceId", serviceList.get(position).getId());
        startActivityForResult(intent, 0);
    }

    @Override
    public void onLongclick(int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_rating_dialog, null);
        dialogBuilder.setView(dialogView);

        final Button ratingBtn = (Button) dialogView.findViewById(R.id.ratingBtn);
        final EditText ratingField = (EditText) dialogView.findViewById(R.id.ratingField);

        dialogBuilder.setTitle(serviceList.get(position).serviceName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        ratingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rating = ratingField.getText().toString().trim();
                if (!TextUtils.isEmpty(rating)) {
                    if(Double.parseDouble(rating) > 5 || Double.parseDouble(rating)<0){
                        ratingField.setError("Input rating 1 to 5");
                    }else{
                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("services").child(serviceList.get(position).getId());
                        NovService novService = serviceList.get(position);
                        Rating rating1 = new Rating(Common.userName,rating);
                        novService.getRating().add(rating1);
                        dR.setValue(novService);
                        b.dismiss();
                    }

                } else {
                    ratingField.setError("Input rating 1 to 5");
                }
            }
        });
    }
}