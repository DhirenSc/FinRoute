package com.example.dhirensc.finroute;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class GoActivity extends AppCompatActivity {

    private ArrayList places;
    int o = 1, latest = 0;
    int r=0;
    DatabaseHandler DBH = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go);
        Intent i= getIntent();
        Bundle extras = i.getExtras();
        if(extras != null)
            r = extras.getInt("latest");
        initViews();
    }

    private void initViews(){
        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        places = new ArrayList<>();

        for(int i=0; i<DirectionsJSONParser.wayptList.size();i++){

            places.add(DirectionsJSONParser.wayptList.get(i));
        }
        places.add(MapsActivity.finlist.get(MapsActivity.finlist.size()-1));

        latest = DBH.getLatestTour();


        Log.d("Insert: ", "Inserting ..");
        for(int k=0; k<places.size(); k++)
            DBH.addContact(new TourData(places.get(k).toString(),"NV",(k+1),(latest+1)));


        RecyclerView.Adapter adapter = new DataAdapter(places);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {


            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
                    child.setBackgroundColor(Color.CYAN);

                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+(CharSequence)places.get(position));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (isGoogleMapsInstalled())
                        startActivity(mapIntent);
                    else
                        Toast.makeText(getApplicationContext(), "Please install Google Maps from Play Store",Toast.LENGTH_LONG).show();

                    Toast.makeText(getApplicationContext(), (CharSequence)places.get(position), Toast.LENGTH_SHORT).show();

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    public boolean isGoogleMapsInstalled()
    {
        try
        {
            ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

}