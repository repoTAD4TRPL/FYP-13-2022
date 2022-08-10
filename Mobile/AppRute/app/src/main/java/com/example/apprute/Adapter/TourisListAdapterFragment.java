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
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprute.DetailTouristAttraction;
import com.example.apprute.Fragment.OptionFragment;
import com.example.apprute.R;
import com.example.apprute.model.TouristAttraction;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class TourisListAdapterFragment extends RecyclerView.Adapter<TourisListAdapterFragment.TouristListFragmentHolder> {
    Context context;
    private List<TouristAttraction> touristAttractionList;

    public TourisListAdapterFragment(Context context, List<TouristAttraction> touristAttractionList) {
        this.context = context;
        this.touristAttractionList = touristAttractionList;
    }

    @NonNull
    @Override
    public TouristListFragmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tourist_attraction_item, parent, false);
        return new TourisListAdapterFragment.TouristListFragmentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TouristListFragmentHolder holder, int position) {
        TouristAttraction touristAttraction = touristAttractionList.get(position);
        holder.name.setText(touristAttraction.getName());
        holder.location.setText(touristAttraction.getLocation());
        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailTouristAttraction.class);
                intent.putExtra("id",touristAttraction.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return touristAttractionList.size();
    }

    public class TouristListFragmentHolder extends RecyclerView.ViewHolder {
        TextView name,location;
        MaterialCardView materialCardView;
        public TouristListFragmentHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tourist_attraction_name);
            location = itemView.findViewById(R.id.location_tourist_attraction);
            materialCardView = itemView.findViewById(R.id.parent_layout);
        }
    }
}
