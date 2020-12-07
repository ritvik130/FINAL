package com.example.servicenovigrad.data.Class;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicenovigrad.R;

import java.util.List;

public class ServicesRecyclerViewAdapter extends RecyclerView.Adapter<ServicesRecyclerViewAdapter.ViewHolder> {
    Context context;
    List<NovService> serviceList;
    onclickListener onclickListener;
    onLongClickListener onLongClickListener;

    public ServicesRecyclerViewAdapter(Context context, List<NovService> serviceList, onclickListener onclickListener, onLongClickListener onLongClickListener) {
        this.context = context;
        this.serviceList = serviceList;
        this.onclickListener = onclickListener;
        this.onLongClickListener = onLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.activity_service_item, null);
        return new ViewHolder(view, onclickListener, onLongClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.serviceNameView.setText(serviceList.get(position).getServiceName());
        if (serviceList.get(position).getBranchList().size() > 0) {
            for (Branch branch : serviceList.get(position).getBranchList()) {
                TextView textView = new TextView(context);
                textView.setPadding(3, 3, 3, 3);
                textView.setText(branch.getAddress().getStreet() + " " + branch.getAddress().getNumber() + " " + branch.getAddress().getTown() + " " + branch.getAddress().getZipCode());
                holder.branchContainer.addView(textView);
            }
        }
        if (serviceList.get(position).isExpiration()) {
            holder.expirationView.setText("Expired!");
            holder.expirationView.setTextColor(context.getResources().getColor(R.color.colorRed));
        }
        double rating = 0;
        if (serviceList.get(position).getRating().size() > 0) {
            for (int i = 0; i < serviceList.get(position).getRating().size(); i++) {
                rating = Double.parseDouble(serviceList.get(position).getRating().get(i).getRating()) + rating;
                holder.ratingView.setText("Rating: " + String.format("%.2f", (rating / serviceList.get(position).getRating().size())));
            }
        }

    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView serviceNameView, branchView, expirationView, ratingView;
        LinearLayout branchContainer;

        public ViewHolder(@NonNull View itemView, onclickListener onclickListener, onLongClickListener onLongClickListener) {
            super(itemView);
            serviceNameView = itemView.findViewById(R.id.serviceNameView);
            branchView = itemView.findViewById(R.id.branchNameView);
            expirationView = itemView.findViewById(R.id.expirationView);
            branchContainer = itemView.findViewById(R.id.branchContainer);
            ratingView = itemView.findViewById(R.id.ratingView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onclickListener.onclick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            onLongClickListener.onLongclick(getAdapterPosition());
            return false;
        }
    }

    public interface onclickListener {
        void onclick(int position);
    }

    public interface onLongClickListener {
        void onLongclick(int position);
    }
}
