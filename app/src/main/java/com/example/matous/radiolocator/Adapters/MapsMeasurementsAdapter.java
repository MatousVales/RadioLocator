package com.example.matous.radiolocator.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.matous.radiolocator.Activities.DatabaseActivity;
import com.example.matous.radiolocator.Activities.MapsActivity;
import com.example.matous.radiolocator.Activities.MeasurementDetailActivity;
import com.example.matous.radiolocator.Models.Measurement;
import com.example.matous.radiolocator.R;

import java.util.ArrayList;

/**
 * Created by Matous on 26.02.2016.
 */
public class MapsMeasurementsAdapter  extends RecyclerView.Adapter<MapsMeasurementsAdapter.MapsMeasurementsViewHolder> {
    private Context c = null;
    static final int MEASUREMENT_DELETED = 6;
    private ArrayList<Measurement> measurements = null;

    public MapsMeasurementsAdapter(Context co,ArrayList<Measurement> m){
        this.c = co;
        this.measurements = m;
    }
    public static class MapsMeasurementsViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView text1;
        TextView text2;
        Button btn1;
        Button btn2;


        MapsMeasurementsViewHolder(View itemView){
            super(itemView);
            itemView.setClickable(true);
            cv = (CardView) itemView.findViewById(R.id.mapsmeasurement_card);
            text1 = (TextView) itemView.findViewById(R.id.maps_number);
            text2 = (TextView) itemView.findViewById(R.id.maps_amountOfDatapoints);
            btn1 = (Button) itemView.findViewById(R.id.maps_btn1);
            btn2 = (Button) itemView.findViewById(R.id.maps_btn2);

        }
    }


    @Override
    public int getItemCount(){
        return measurements.size();
    }

    @Override
    public MapsMeasurementsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mapsmeasurementscard, viewGroup, false);
        MapsMeasurementsViewHolder mvh = new MapsMeasurementsViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MapsMeasurementsViewHolder measurementsViewHolder, int i) {
        final int pos = i;
        measurementsViewHolder.text1.setText("Měření " + Integer.toString(measurements.get(i).getNumber()));
        measurementsViewHolder.text2.setText("Počet bodů: " + Integer.toString(measurements.get(i).getCount()));
        measurementsViewHolder.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int measurementID = measurements.get(pos).getNumber();
                ((MapsActivity)c).setPosition(measurementID);
            }
        });
        measurementsViewHolder.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int measurementID = measurements.get(pos).getNumber();
                measurements.get(pos).setSelected(true);

                Intent intent = new Intent(v.getContext(),MeasurementDetailActivity.class);
                intent.putExtra("measurementID",measurementID);
                ((Activity)c).startActivityForResult(intent,MEASUREMENT_DELETED);
            }
        });


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}