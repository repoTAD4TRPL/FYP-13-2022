package com.example.apprute.Fragment;

import android.os.Bundle;

                                            import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
                                            import android.view.View;
                                            import android.view.ViewGroup;

import com.example.apprute.Adapter.TourisListAdapterFragment;
import com.example.apprute.R;
import com.example.apprute.model.TouristAttraction;
import com.example.apprute.service.ApiInterface;
import com.example.apprute.service.BaseUrl;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NaturalTourismFragment extends Fragment {
    private RecyclerView recyclerView;
    ApiInterface apiInterface;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_natural_tourism, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_tourist_attraction_natural_tourism);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        getTouristAttraction();
        return view;
    }
    private void getTouristAttraction(){
        Call<List<TouristAttraction>> listCall = apiInterface.getTouristAttractionCategory(1);
        listCall.enqueue(new Callback<List<TouristAttraction>>() {
            @Override
            public void onResponse(Call<List<TouristAttraction>> call, Response<List<TouristAttraction>> response) {
                List<TouristAttraction> touristAttractions = response.body();
                TourisListAdapterFragment touristAttractionAdapterFragment = new TourisListAdapterFragment(getView().getContext(),touristAttractions);
                final GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(touristAttractionAdapterFragment);
            }

            @Override
            public void onFailure(Call<List<TouristAttraction>> call, Throwable t) {

            }
        });
    }

}