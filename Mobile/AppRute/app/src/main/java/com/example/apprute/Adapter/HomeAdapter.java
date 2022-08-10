package com.example.apprute.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprute.Maps.MapActivity;
import com.example.apprute.R;
import com.example.apprute.ScheduleActivity;
import com.example.apprute.model.PortRoute;
import com.example.apprute.model.Route;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class HomeAdapter  extends RecyclerView.Adapter<HomeAdapter.HomeHolder>{
    Context context;
    List<PortRoute> routeList;

    public HomeAdapter(Context context, List<PortRoute> routeList) {
        this.context = context;
        this.routeList = routeList;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_port_route , parent , false);
        return new HomeAdapter.HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        PortRoute route  = routeList.get(position);
        holder.route_name.setText(route.getRouteName());
        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "" + position   , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ScheduleActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (routeList.size()<0){
            return 0;
        }else {
        return routeList.size();
        }
    }

    public class HomeHolder extends RecyclerView.ViewHolder {
        TextView route_name,location;
        MaterialCardView materialCardView;
        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            route_name = itemView.findViewById(R.id.route_name_home);
            location  = itemView.findViewById(R.id.location_home);
            materialCardView =itemView.findViewById(R.id.homeCard);
        }
    }
}
