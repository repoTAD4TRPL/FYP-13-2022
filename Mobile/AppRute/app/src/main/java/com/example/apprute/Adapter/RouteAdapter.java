package com.example.apprute.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprute.Maps.MapActivity;
import com.example.apprute.R;
import com.example.apprute.model.Harbor;
import com.example.apprute.model.Route;
import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteHolder> {
    Context context;
    List<Route> routeList;

    public RouteAdapter(Context context, List<Route> harborList) {
        this.context = context;
        this.routeList = harborList;
    }

    @NonNull
    @Override
    public RouteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.option_item , parent , false);
        return new RouteAdapter.RouteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteHolder holder, int position) {
        Route route  = routeList.get(position);
        holder.route_name.setText(route.getRouteName());
        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "" + position   , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MapActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public class RouteHolder extends RecyclerView.ViewHolder {
        TextView route_name;
        MaterialCardView materialCardView;
        public RouteHolder(@NonNull View itemView) {
            super(itemView);
            route_name = itemView.findViewById(R.id.route_name);
            materialCardView = itemView.findViewById(R.id.item_option);


        }
    }
}
