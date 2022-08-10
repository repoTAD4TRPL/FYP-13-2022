package com.example.apprute.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprute.DetailTouristAttraction;
import com.example.apprute.Fragment.OptionFragment;
import com.example.apprute.Fragment.TouristFragment;
import com.example.apprute.Maps.MapActivity;
import com.example.apprute.OptionRouteActivity;
import com.example.apprute.R;
import com.example.apprute.TouristAttractionChoice;
import com.example.apprute.model.TouristAttraction;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class TouristAttractionAdapterFragment extends RecyclerView.Adapter<TouristAttractionAdapterFragment.TouristFragmentHolder>{
    Context context;
    String time,location;
    private List<TouristAttraction> touristAttractionList;


    public TouristAttractionAdapterFragment(Context context, String time, String location, List<TouristAttraction> touristAttractionList) {
        this.context = context;
        this.time = time;
        this.location = location;
        this.touristAttractionList = touristAttractionList;
    }
    @NonNull
    @Override
    public TouristFragmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tourist_attraction_item2, parent, false);
        return new TouristAttractionAdapterFragment.TouristFragmentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TouristFragmentHolder holder, int position) {
        TouristAttraction touristAttraction = touristAttractionList.get(position);
        holder.name.setText(touristAttraction.getName());
        holder.location.setText(touristAttraction.getLocation());
        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OptionRouteActivity.class);
                intent.putExtra("id_tourist_attraction",touristAttraction.getId());
                intent.putExtra("time", time);
                intent.putExtra("location", location);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        if(touristAttractionList.size()>0){
            return touristAttractionList.size();
        }else{
        return 0;
        }

    }

    public class TouristFragmentHolder extends RecyclerView.ViewHolder {
        TextView name,location;
        MaterialCardView materialCardView;
        public TouristFragmentHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tourist_attraction_name2);
            location = itemView.findViewById(R.id.location_tourist_attraction2);
            materialCardView = itemView.findViewById(R.id.parent_layout2);
        }
    }
}
