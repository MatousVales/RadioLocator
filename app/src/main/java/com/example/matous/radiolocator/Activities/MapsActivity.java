package com.example.matous.radiolocator.Activities;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.matous.radiolocator.Adapters.MapsMeasurementsAdapter;

import com.example.matous.radiolocator.Adapters.MeasurementsAdapter;
import com.example.matous.radiolocator.Classes.DBHelper;
import com.example.matous.radiolocator.Classes.TCPClient;

import com.example.matous.radiolocator.Models.Datapoint;
import com.example.matous.radiolocator.Models.Measurement;
import com.example.matous.radiolocator.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;


import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String ip;
    private int port;
    private int distance;
    private GoogleMap mMap;
    private DBHelper db;
    private TCPClient TcpClient;
    private int position;
    private ArrayList<Measurement> measurements = null;
    private ArrayList<Datapoint> datapoints = null;
    private Context c;
    private MapsMeasurementsAdapter adapter;
    private RecyclerView DBRecycler;
    private SupportMapFragment mapFragment;
    static final int MEASUREMENT_DELETED = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.mapsactivity_toolbar);
        myToolbar.setTitle(getString(R.string.mapsactivity_title));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_start:
                        new connectTask().execute("");
                        Toast.makeText(c, "Spojení navázáno", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_stop:
                        if (TcpClient != null) {
                            TcpClient.stopClient();
                            Toast.makeText(c, "Ukončuji spojení", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(c, "Kliknutím na tlačítko start zahájíte spojení", Toast.LENGTH_SHORT).show();
                        }
                    case R.id.settings:
                        Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(i);
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });

        c = this;
        db = new DBHelper(this);
        measurements = db.getMeasurements();
        DBRecycler = (RecyclerView) findViewById(R.id.Maps_list);
        DBRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        DBRecycler.setLayoutManager(llm);
        adapter = new MapsMeasurementsAdapter(this,measurements);
        DBRecycler.setAdapter(adapter);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        ip = SP.getString("address", "teeworlds.cz");
        port = Integer.parseInt(SP.getString("port", "1989"));
        distance = Integer.parseInt(SP.getString("distance", "10000"));

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(TcpClient != null){
            TcpClient.stopClient();
            Toast.makeText(c, "Ukončuji spojení", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng pos = null;
        LatLng opos = null;

        for (Datapoint d : datapoints){
            pos = new LatLng(d.getLatitude(),d.getLongitude());
            mMap.addMarker(new MarkerOptions().position(pos)
                            .title("Marker")
                             );
            opos = SphericalUtil.computeOffset(pos, distance, d.getAngle());
            mMap.addMarker(new MarkerOptions().position(opos).title("opposite Marker"));
            PolylineOptions rectOptions = new PolylineOptions()
                    .add(pos)
                    .add(opos)
                    .color(Color.parseColor("#ff5722"))
                    .width(3);
            mMap.addPolyline(rectOptions);
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15.5f));
    }

    public void setPosition(int pos){
        this.position = pos;
        if(mMap != null){
            mMap.clear();
        }
        datapoints = db.getDatapoints(position);
        mapFragment.getMapAsync(this);
    }

    public class connectTask extends AsyncTask<String,String,TCPClient> {
        @Override
        protected TCPClient doInBackground(String... message) {
            TcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                public void messageReceived(String message) {
                    publishProgress(message);
                }
            },getApplicationContext(),ip,port);
            TcpClient.run();
            return null;
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            measurements = db.getMeasurements();
            adapter = new MapsMeasurementsAdapter(c,measurements);
            DBRecycler.setAdapter(adapter);
            Toast.makeText(c, "Datapoint načten, aktualizuji mapu", Toast.LENGTH_SHORT).show();
            setPosition(Integer.parseInt(values[0]));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mapsactivity_menu, menu);
        return true;
    }

     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MEASUREMENT_DELETED:
                if(resultCode == RESULT_OK){
                    measurements = db.getMeasurements();
                    adapter = new MapsMeasurementsAdapter(this, measurements);
                    DBRecycler.setAdapter(adapter);
                    if(mMap != null){
                        mMap.clear();
                    }
                }
        }
    }
}
