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
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.matous.radiolocator.Activities.DatabaseActivity;
import com.example.matous.radiolocator.Activities.MeasurementDetailActivity;
import com.example.matous.radiolocator.Classes.DBHelper;
import com.example.matous.radiolocator.Models.Measurement;
import com.example.matous.radiolocator.R;

import java.util.ArrayList;

/**
 * Created by Matous on 26.02.2016.
 */
public class MeasurementsAdapter  extends RecyclerView.Adapter<MeasurementsAdapter.MeasurementsViewHolder> {
    private Context context = null;
    static final int MEASUREMENT_DELETED = 6;
    ArrayList<Measurement> measurements = null;

    public MeasurementsAdapter(Context c,ArrayList<Measurement> m){
        context = c;
        measurements = m;
    }
    public static class MeasurementsViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView text1;
        TextView text2;
        CheckBox cbox;
        Button btn;


        MeasurementsViewHolder(View itemView){
            super(itemView);
            itemView.setClickable(true);
            cv = (CardView) itemView.findViewById(R.id.measurement_card);
            text1 = (TextView) itemView.findViewById(R.id.measurement_number);
            text2 = (TextView) itemView.findViewById(R.id.measurement_amountOfDatapoints);
            cbox = (CheckBox) itemView.findViewById(R.id.measurement_cbox);
            btn = (Button) itemView.findViewById(R.id.measurement_btn);
        }
    }


    @Override
    public int getItemCount(){
        return measurements.size();
    }

    @Override
    public MeasurementsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.measurementscard, viewGroup, false);
        MeasurementsViewHolder mvh = new MeasurementsViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MeasurementsViewHolder measurementsViewHolder, int i) {
        final int pos = i;
        measurementsViewHolder.text1.setText("Měření " + Integer.toString(measurements.get(i).getNumber()));
        measurementsViewHolder.text2.setText("Počet bodů: " + Integer.toString(measurements.get(i).getCount()));
        measurementsViewHolder.cbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                measurements.get(pos).setSelected(cb.isChecked());
            }
        });

        measurementsViewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int measurementID = pos;
                Intent intent = new Intent(v.getContext(),MeasurementDetailActivity.class);
                intent.putExtra("measurementID",measurements.get(pos).getNumber());
                ((Activity)context).startActivityForResult(intent, MEASUREMENT_DELETED);
            }
        });
   }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}