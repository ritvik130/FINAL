package com.example.servicenovigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.servicenovigrad.data.Class.NovService;
import com.example.servicenovigrad.data.Class.NovServiceCheckList;
import com.example.servicenovigrad.data.Class.Request;
import com.example.servicenovigrad.data.Class.RequestsRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestsActivity extends AppCompatActivity implements RequestsRecyclerViewAdapter.onclickListener {

    RecyclerView requestRecyclerView;
    Request request;
    RequestsRecyclerViewAdapter requestsRecyclerViewAdapter;
    DatabaseReference databaseRequests;
    List<Request> requestsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        requestRecyclerView = findViewById(R.id.recyclerView);
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseRequests = FirebaseDatabase.getInstance().getReference("customer_requests");
        requestsList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseRequests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (requestsList.size() > 0)
                    requestsList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot1 : postSnapshot.getChildren()) {
                        request = dataSnapshot1.getValue(Request.class);
                        requestsList.add(request);

                    }

                }
                requestsRecyclerViewAdapter = new RequestsRecyclerViewAdapter(RequestsActivity.this, requestsList, RequestsActivity.this);
                requestRecyclerView.setAdapter(requestsRecyclerViewAdapter);
                requestsRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onclick(int position, int id) {
        if (id == R.id.approveBtn) {
            databaseRequests.child(requestsList.get(position).getCustomer().getUsername()).child(requestsList.get(position).getId()).child("status").setValue("Approved");
            sendMessage(requestsList.get(position).getToken(),"Approved");
        } else if (id == R.id.rejectBtn) {
            databaseRequests.child(requestsList.get(position).getCustomer().getUsername()).child(requestsList.get(position).getId()).child("status").setValue("Rejected");
            sendMessage(requestsList.get(position).getToken(),"Rejected");
        }
    }
    private void sendMessage(String token,String status) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject root = new JSONObject();
        JSONObject notification = new JSONObject();
        try {
            notification.put("title", "Notification");

            notification.put("content", "Your application has been "+status);
            JSONObject data = new JSONObject();
            data.put("message", "Your application has been "+status);
            root.put("data", notification);
            root.put("to", token);


            String URL = "https://fcm.googleapis.com/fcm/send";

            Log.i("VOLLEY0", root.toString());
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(com.android.volley.Request.Method.POST, URL, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    System.out.println(response);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("authorization", "key=AAAAjLdgwtY:APA91bHh0EFLabyaSnT83EhR9ZaIcAO9fYAwNdorpt85CJpUdydwFz5NXaZRiIqVraO7anMlXxX6pY_hasrObKHjt0LAPO2k8mEesRam3v14uMjTJ-Eye5WQ6bv1x9GZjxJeXy-as6yj");

                    return params;
                }

                @Override
                public byte[] getBody() {
                    try {
                        return root.toString() == null ? null : root.toString().getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", root.toString(), "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(jsonArrayRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}