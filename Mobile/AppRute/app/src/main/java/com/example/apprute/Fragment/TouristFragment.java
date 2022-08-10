package com.example.apprute.Fragment;

import static android.content.ContentValues.TAG;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apprute.Adapter.TouristAttractionAdapterFragment;
import com.example.apprute.Adapter.TouristAttractionsAdapter;
import com.example.apprute.R;
import com.example.apprute.model.TouristAttraction;
import com.example.apprute.service.ApiInterface;
import com.example.apprute.service.BaseUrl;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TouristFragment extends Fragment implements TouristAttractionsAdapter.OnNoteListener,View.OnClickListener{
    private RecyclerView recyclerView;
    ApiInterface apiInterface;
    Button btn_getLocation;
    TextView tv_latlng;
    EditText et_location;
    ArrayList<Double> latlng;
    String location ="",time="",getlocation="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_touris, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_tourist_attraction_choice);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        et_location = view.findViewById(R.id.edittext_time);
        btn_getLocation = view.findViewById(R.id.btn_inputlocation);
        latlng = new ArrayList<>();
        Bundle bundle = this.getArguments();
        if (bundle !=null){
            time = bundle.getString("time");
            getlocation = bundle.getString("location");
        }
        System.out.println("Location kali"+ getlocation);
        btn_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationFromAddress();
                Bundle bundle = new Bundle();
                OptionFragment optionFragment = new OptionFragment();
                bundle.putDouble("lat_input",latlng.get(0));
                bundle.putDouble("lng_input",latlng.get(1));
                bundle.putString("time",time);
                bundle.putString("location",getlocation);
                optionFragment.setArguments(bundle);
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, optionFragment).addToBackStack(null).commit();
            }
        });
        getTouristAttraction();


        return view;
    }
    private void getTouristAttraction(){
        Call<List<TouristAttraction>> listCall = apiInterface.getTouristAttraction();
        listCall.enqueue(new Callback<List<TouristAttraction>>() {
            @Override
            public void onResponse(Call<List<TouristAttraction>> call, Response<List<TouristAttraction>> response) {
                List<TouristAttraction> touristAttractions = response.body();
                TouristAttractionAdapterFragment touristAttractionAdapterFragment = new TouristAttractionAdapterFragment(getView().getContext(),time,getlocation,touristAttractions);
                recyclerView.setAdapter(touristAttractionAdapterFragment);
            }
            @Override
            public void onFailure(Call<List<TouristAttraction>> call, Throwable t) {
            }
        });
    }
    public GeoPoint getLocationFromAddress() {
        location =et_location.getText().toString();
        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(location, 20);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            latlng.add(location.getLatitude());
            latlng.add(location.getLongitude());
            return p1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onNoteClick(int position) {
        Toast toast = Toast.makeText(getActivity(),"CLICKED",Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onClick(View view) {

    }
}