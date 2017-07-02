package com.example.dhirensc.finroute;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static GoogleMap st_mMap;
    private GPSTracker gps;
    double longitude;
    double latitude;
    LatLng latLng;
    MarkerOptions markerOptions;
    LatLng myLoc,markLoc;
    public static String myLat,myLng;

    static ArrayList<String> finlist = new ArrayList<String>();
    static ArrayList<Double> finlat = new ArrayList<Double>();
    static ArrayList<Double> finlon = new ArrayList<Double>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        try {
            gps = new GPSTracker(MapsActivity.this);


            if(gps.canGetLocation()){


                longitude = gps.getLongitude();
                latitude = gps .getLatitude();
                Log.e(latitude+":",longitude+"");
                }
            else
            {

                gps.showSettingsAlert();
            }

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }catch(Exception e){
            e.printStackTrace();
        }


//        Button btn_find = (Button) findViewById(R.id.BSearch);
//
//        View.OnClickListener findClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Getting reference to EditText to get the user input location
//                EditText etLocation = (EditText) findViewById(R.id.TFaddress);
//
//                // Getting user input location
//                String location = etLocation.getText().toString();
//
//                if(location!=null && !location.equals("")){
//                    new GeocoderTask().execute(location);
//                }
//            }
//        };
//        btn_find.setOnClickListener(findClickListener);


//        Button add_dest = findViewById(R.id.button3);
//
//        View.OnClickListener ClickListener = new View.OnClickListener() {
//            public void onClick(View v) {
//
//                Intent i = new Intent(MapsActivity.this, DestinationsActivity.class);
//                startActivity(i);
//            }
//
//        };
//        add_dest.setOnClickListener(ClickListener);



        }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        st_mMap = googleMap;

        // Add a marker in and move the camera
        mMap.clear();
        myLoc = new LatLng(latitude, longitude);
        myLat = myLoc.latitude+"";
        myLng = myLoc.longitude+"";
        Marker markerMe = mMap.addMarker(new MarkerOptions().position(myLoc).title("I'm here..."));
        markerMe.setVisible(true);
        markerMe.showInfoWindow();
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLoc,15);
        mMap.animateCamera(cameraUpdate);
    }


    public void findPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e("Tag", "Place: " + place.getName() + place.getPhoneNumber() + place.getLatLng().latitude);

                finlist.add(place.getName().toString());
                finlat.add(place.getLatLng().latitude);
                finlon.add(place.getLatLng().longitude);

                markLoc = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);

                Marker marker = mMap.addMarker(new MarkerOptions().position(markLoc).title(place.getName().toString()));
                marker.setVisible(true);
                marker.showInfoWindow();

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markLoc, 15);
                mMap.animateCamera(cameraUpdate);


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


        public void drawRoute(View v){
            String waypts="", origins=myLat + "," + myLng, destn="";

            for(int i=0; i<finlist.size();i++){
                if(i==(finlist.size()-1))
                    destn += finlat.get(i) + "," + finlon.get(i) ;
                else
                    waypts += finlat.get(i) + "," + finlon.get(i) + "|" ;
            }

            String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + origins + "&destination=" + destn + "&waypoints=optimize:true|" + waypts + "&key=AIzaSyADFECehSiN8VdnNEamDzzObDs6uH0gyPw";
            new Sample(this).execute(url);



    }




    public void onGo(View v) {

        Intent i = new Intent(MapsActivity.this, GoActivity.class);
        startActivity(i);
    }
}

