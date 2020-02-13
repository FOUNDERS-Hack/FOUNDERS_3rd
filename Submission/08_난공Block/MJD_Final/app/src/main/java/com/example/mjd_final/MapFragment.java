package com.example.mjd_final;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;


public class MapFragment extends Fragment implements OnMapReadyCallback {
//    MapView mapView;
    private Marker currentMarker=null;
    private GoogleMap mMap;
    private Place currentplace;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.activity_map_fragment,container,false);
        Places.initialize(getActivity().getApplicationContext(),"AIzaSyCRuMEjdNWzmNU_Zu6RJcdjr-dnJ5OdqIU");
        PlacesClient placesClient = Places.createClient(getActivity());
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setCountry("KR").build();

        AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment)getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS_COMPONENTS));
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Location location = new Location("");
                location.setLatitude(place.getLatLng().latitude);
                location.setLongitude(place.getLatLng().longitude);
                currentplace= place;
                LatLng currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
                setCurrentLocation(location,place.getName(),place.getAddress());
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i("Error","An error occurred" + status);
            }
        });

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng gangnam = new LatLng(37.497996,127.027733);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gangnam,15));
    }



public void setCurrentLocation(Location location, String markerTitle, String markerSnippet){
        if(currentMarker != null)currentMarker.remove();

        if(location!=null){
            LatLng currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
            currentMarker= mMap.addMarker(new MarkerOptions()
            .position(currentLocation).title(currentplace.getName()).snippet(currentplace.getAddress()));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentLocation);
            markerOptions.title(markerTitle);
            markerOptions.snippet(markerSnippet);
            markerOptions.draggable(true);

            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }
    }
}
