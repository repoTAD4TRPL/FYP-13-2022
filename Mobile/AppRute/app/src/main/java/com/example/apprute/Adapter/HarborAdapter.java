package com.example.apprute.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprute.R;
import com.example.apprute.model.Harbor;

import java.util.List;

public class HarborAdapter extends RecyclerView.Adapter<HarborAdapter.HarborHolder>{
    Context context;
    List<Harbor> harborList;

    public HarborAdapter(Context context, List<Harbor> harborList) {
        this.context = context;
        this.harborList = harborList;
    }

    @NonNull
    @Override
    public HarborHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.harbor_item , parent , false);
        return new HarborHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HarborHolder holder, int position) {
        Harbor harbor = harborList.get(position);
        holder.harborName.setText(harbor.getHarborName());
        holder.location.setText(harbor.getLocation());
        holder.latitude.setText(Double.toString(harbor.getLatitude()));
        holder.longitude.setText(Double.toString(harbor.getLongitude()));
    }

    @Override
    public int getItemCount() {
        return harborList.size();
    }

    public class HarborHolder extends RecyclerView.ViewHolder {
        TextView harborName, location, latitude, longitude;
        public HarborHolder(@NonNull View itemView) {
            super(itemView);
            harborName =itemView.findViewById(R.id.harborName);
            location = itemView.findViewById(R.id.location);
            latitude = itemView.findViewById(R.id.latitude);
            longitude = itemView.findViewById(R.id.longitude);
        }
    }
}
