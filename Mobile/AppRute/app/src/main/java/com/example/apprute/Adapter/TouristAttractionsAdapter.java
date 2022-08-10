package com.example.apprute.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.window.SplashScreen;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprute.DetailTouristAttraction;
import com.example.apprute.Fragment.OptionFragment;
import com.example.apprute.MainActivity;
import com.example.apprute.R;
import com.example.apprute.model.TouristAttraction;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class TouristAttractionsAdapter extends RecyclerView.Adapter<TouristAttractionsAdapter.TouristHolder>{
    Context context;
    private List<TouristAttraction> touristAttractionList = new ArrayList<>();
    String time, location;


    public TouristAttractionsAdapter(Context context, List<TouristAttraction> touristAttractionList, String time, String location) {
        this.context = context;
        this.touristAttractionList = touristAttractionList;
        this.time = time;
        this.location  = location;
    }

    @NonNull
    @Override
    public TouristAttractionsAdapter.TouristHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tourist_attraction_item2, parent , false);
        return new TouristAttractionsAdapter.TouristHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TouristAttractionsAdapter.TouristHolder holder, final int position) {
        TouristAttraction touristAttraction = touristAttractionList.get(position);
        holder.name.setText(touristAttraction.getName());
        holder.location.setText(touristAttraction.getLocation());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailTouristAttraction.class);
                intent.putExtra("id",touristAttraction.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {return touristAttractionList.size();}

    public class TouristHolder extends RecyclerView.ViewHolder {
        TextView name, location;
        MaterialCardView parentLayout;
        public TouristHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tourist_attraction_name2);
            location = itemView.findViewById(R.id.location_tourist_attraction2);
            parentLayout = itemView.findViewById(R.id.parent_layout2);
        }
    }
    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
