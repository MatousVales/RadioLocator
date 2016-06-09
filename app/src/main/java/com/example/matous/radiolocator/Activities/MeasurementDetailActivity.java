package com.example.matous.radiolocator.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.matous.radiolocator.Adapters.MeasurementAdapter;
import com.example.matous.radiolocator.Classes.DBHelper;
import com.example.matous.radiolocator.Classes.XmlExporter;
import com.example.matous.radiolocator.Models.Datapoint;
import com.example.matous.radiolocator.Models.Measurement;
import com.example.matous.radiolocator.R;

import java.io.File;
import java.util.ArrayList;

public class MeasurementDetailActivity extends AppCompatActivity {

    private RecyclerView MeasurementsRecycler;
    private TextView date;
    private int measurementID = 0;
    private ArrayList<Datapoint> datapoints;
    private ArrayList<Measurement> measurementToExport;
    private DBHelper db;
    private Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_detail);
        date = (TextView) findViewById(R.id.date);
        db = new DBHelper(this);
        c = this;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.measurement_detail_activity_toolbar);
        myToolbar.setTitle(getString(R.string.MeasurementDetail_activity_title));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DatabaseActivity.class);
                startActivity(intent);
                finish();
            }
        });
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_export:
                        measurementToExport = db.getMeasurement(measurementID);
                        measurementToExport.get(0).setSelected(true);
                        XmlExporter xmlex = new XmlExporter(measurementToExport,c);
                        File f = xmlex.export();
                        if (f != null){
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
                            shareIntent.setType("text/xml");
                            startActivity(Intent.createChooser(shareIntent, "Odeslat měření"));
                            setResult(RESULT_OK);
                            finish();
                            return true;
                        }
                        return false;
                    case R.id.action_delete:
                        db.deleteMeasurement(measurementID);
                        setResult(RESULT_OK);
                        finish();
                        return true;
                    default:
                        return false;
                }

            }
        });

        Intent i = getIntent();
        Bundle b = i.getExtras();

        if(b != null){
            measurementID = (int) b.get("measurementID");
            datapoints = db.getDatapoints(measurementID);
            date.setText(datapoints.get(0).getDate());
        }

        MeasurementsRecycler = (RecyclerView) findViewById(R.id.measurementDetail_list);
        MeasurementsRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        MeasurementsRecycler.setLayoutManager(llm);
        MeasurementAdapter adapter = new MeasurementAdapter(measurementID,datapoints);
        MeasurementsRecycler.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.measurement_detail_activity_menu, menu);
        return true;
    }

}
