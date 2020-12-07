package com.example.servicenovigrad.data.Class;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.admin_manage_services;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RequestsRecyclerViewAdapter extends RecyclerView.Adapter<RequestsRecyclerViewAdapter.ViewHolder> {
    Context context;
    List<Request> requestList;
    onclickListener onclickListener;
    DatabaseReference databaseService;

    public RequestsRecyclerViewAdapter(Context context, List<Request> requestList, onclickListener onclickListener) {
        this.context = context;
        this.requestList = requestList;
        this.onclickListener = onclickListener;
        databaseService = FirebaseDatabase.getInstance().getReference("services");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.activity_request_item, null);
        return new ViewHolder(view, onclickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.datetimeView.setText(requestList.get(position).getDate() + " " + requestList.get(position).getTime());
        holder.customerNameView.setText(requestList.get(position).getCustomer().getFirstName());
        if (!requestList.get(position).getStatus().equals("Pending")) {
            holder.approveBtn.setVisibility(View.GONE);
            holder.rejectBtn.setVisibility(View.GONE);
        }

        holder.statusView.setText("Status: " + requestList.get(position).getStatus());
        databaseService.child(requestList.get(position).getServiceId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.serviceNameView.setText("Request for " + dataSnapshot.child("serviceName").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView serviceNameView, statusView, datetimeView, customerNameView;
        LinearLayout branchContainer;
        Button approveBtn, rejectBtn;

        public ViewHolder(@NonNull View itemView, onclickListener onclickListener) {
            super(itemView);
            serviceNameView = itemView.findViewById(R.id.serviceNameView);
            datetimeView = itemView.findViewById(R.id.datetimeView);
            customerNameView = itemView.findViewById(R.id.customerNameView);
            branchContainer = itemView.findViewById(R.id.branchContainer);
            approveBtn = itemView.findViewById(R.id.approveBtn);
            rejectBtn = itemView.findViewById(R.id.rejectBtn);
            statusView = itemView.findViewById(R.id.statusView);
            approveBtn.setOnClickListener(this);
            rejectBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onclickListener.onclick(getAdapterPosition(), view.getId());
        }
    }

    public interface onclickListener {
        void onclick(int position, int id);
    }
}
