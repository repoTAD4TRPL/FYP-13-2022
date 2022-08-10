package com.example.apprute.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprute.Maps.MapActivity;
import com.example.apprute.R;
import com.example.apprute.model.PortRoute;
import com.example.apprute.model.Route;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PortRouteAdapter extends RecyclerView.Adapter<PortRouteAdapter.PortRouteHolder>{
    Context context;
    List<PortRoute> portRoutes;
    List<String> listTime;
    List<Double> latlng;
    int id;
    double latitude;
    double longitude;
    String time, location;

    public PortRouteAdapter(Context context, List<PortRoute> portRoutes, double latitude, double longitude, String time, String location, int id, ArrayList<String> listTime) {
        this.context = context;
        this.portRoutes = portRoutes;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.location = location;
        this.id = id;
        this.listTime = listTime;
    }

    @NonNull
    @Override
    public PortRouteAdapter.PortRouteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.option_item , parent , false);
        return new PortRouteAdapter.PortRouteHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull PortRouteAdapter.PortRouteHolder holder, int position) {
        PortRoute portRoute  = portRoutes.get(position);
        listTime.get(position);
        List<String> sortedList = listTime.stream().sorted().collect(Collectors.toList());
        System.out.println("Sorted "+sortedList.get(position));
        String[] a = sortedList.get(0).split(" ");
        int j = 0;

            for (int i =0; i<listTime.size();i++){
                if(a.length>0) {
                    j++;
                    break;
                }

            }
        if (sortedList.get(j) == listTime.get(position)){
            holder.timeEstimation.setTextColor(Color.rgb(0,200,0));
            holder.route_name.setText(portRoute.getRouteName());
            holder.timeEstimation.setText("Time Estimation : "+listTime.get(position)+"(Rekomended)");
        }else{
            holder.route_name.setText(portRoute.getRouteName());
            holder.timeEstimation.setText("Time Estimation : "+listTime.get(position));
        }

        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapActivity.class);
                if (latitude !=0 && longitude!=0 ){
                    intent.putExtra("port_id",portRoute.getId());
                    intent.putExtra("id_tour",id);
                    intent.putExtra("latitude",latitude);
                    intent.putExtra("longitude",longitude);
                    intent.putExtra("time",time);
                    intent.putExtra("location",location);
                }

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return portRoutes.size();
    }

    public class PortRouteHolder extends RecyclerView.ViewHolder {
        TextView route_name,timeEstimation;
        MaterialCardView materialCardView;
        public PortRouteHolder(@NonNull View itemView) {
            super(itemView);
            route_name = itemView.findViewById(R.id.route_name);
            timeEstimation = itemView.findViewById(R.id.timeEstimation);
            materialCardView = itemView.findViewById(R.id.item_option);
        }
    }
}
