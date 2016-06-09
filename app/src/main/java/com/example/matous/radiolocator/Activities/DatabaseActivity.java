package com.example.matous.radiolocator.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.matous.radiolocator.Adapters.MeasurementsAdapter;
import com.example.matous.radiolocator.Classes.DBHelper;
import com.example.matous.radiolocator.Classes.DataParser;
import com.example.matous.radiolocator.Models.Measurement;
import com.example.matous.radiolocator.Classes.XmlExporter;
import com.example.matous.radiolocator.R;


import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;



public class DatabaseActivity extends AppCompatActivity {

    public  ArrayList<Measurement> measurements = new ArrayList<>();
    private RecyclerView DBRecycler;
    private DBHelper db;
    private Context c;
    private MeasurementsAdapter adapter;

    static final int FILE_SELECT_CODE = 1;
    static final int MEASUREMENT_DELETED = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        db = new DBHelper(this);
        measurements = db.getMeasurements();
        c = this;

        DBRecycler = (RecyclerView) findViewById(R.id.DB_list);
        DBRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        DBRecycler.setLayoutManager(llm);
        adapter = new MeasurementsAdapter(this,measurements);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.databaseactivity_toolbar);
        myToolbar.setTitle(getString(R.string.databaseactivity_title));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_export:
                        XmlExporter xmlex = new XmlExporter(measurements,c);
                        File f = xmlex.export();
                        if (f != null){
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
                            shareIntent.setType("text/xml");
                            startActivity(Intent.createChooser(shareIntent,"Odeslat měření"));
                            return true;
                        }
                        return false;
                    case R.id.action_import:
                        showFileChooser();
                        return true;
                    case R.id.action_delete:
                        for (Measurement m : measurements) {
                            if (m.isSelected()) {
                                db.deleteMeasurement(m.getNumber());
                            }
                        }
                        measurements = db.getMeasurements();
                        adapter = new MeasurementsAdapter(c,measurements);
                        DBRecycler.setAdapter(adapter);
                        return true;
                    default:
                        return false;
                }
             }
        });

        if(!db.isEmpty()){
            DBRecycler.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.databaseactivity_menu, menu);
        return true;
    }


    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/xml");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Vyberte xml soubor měření"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Omlouváme se, ale k importování xml souboru měření je zapotřebí správce souborů.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    try {
                        InputStream is = getContentResolver().openInputStream(uri);
                        DataParser.parse(is,this);
                        measurements = db.getMeasurements();
                        adapter = new MeasurementsAdapter(this,measurements);
                        DBRecycler.setAdapter(adapter);
                    }catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Omlouváme se, ale soubor se nepodařilo naimportovat. Zkuste to prosím znovu.",Toast.LENGTH_SHORT).show();
                }
            case MEASUREMENT_DELETED:
                if(resultCode == RESULT_OK){
                    measurements = db.getMeasurements();
                    adapter = new MeasurementsAdapter(this,measurements);
                    DBRecycler.setAdapter(adapter);
                }
        }
    }

}
