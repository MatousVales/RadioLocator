package com.example.matous.radiolocator.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.matous.radiolocator.Activities.DatabaseActivity;
import com.example.matous.radiolocator.Models.Datapoint;
import com.example.matous.radiolocator.R;

import java.util.ArrayList;

/**
 * Created by Matous on 26.02.2016.
 */
public class MeasurementAdapter extends RecyclerView.Adapter<MeasurementAdapter.MeasurementViewHolder> {
    private int measurementID;
    private ArrayList<Datapoint> datapoints;

    public MeasurementAdapter(int m, ArrayList<Datapoint> d){
        this.measurementID = m;
        this.datapoints = d;
    }
    public static class MeasurementViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView text1;
        TextView text2;
        TextView text3;
        TextView text4;

        MeasurementViewHolder(View itemView){
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.Datapoint_card);
            text1 = (TextView) itemView.findViewById(R.id.latitude);
            text2 = (TextView) itemView.findViewById(R.id.longitude);
            text3 = (TextView) itemView.findViewById(R.id.angle);
            text4 = (TextView) itemView.findViewById(R.id.time);

        }
    }

        @Override
        public int getItemCount(){
            return datapoints.size(); //predelat na .getDatapointsArrayList().size()
        }

        @Override
        public MeasurementViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.datapointcard, viewGroup, false);
            MeasurementViewHolder mvh = new MeasurementViewHolder(v);
            return mvh;
        }

        @Override
        public void onBindViewHolder(MeasurementViewHolder measurementViewHolder, int i) {
            measurementViewHolder.text1.setText("X: " + Double.toString(datapoints.get(i).getLatitude()));
            measurementViewHolder.text2.setText("Y: " + Double.toString(datapoints.get(i).getLongitude()));
            measurementViewHolder.text3.setText("Úhel: " + Double.toString(datapoints.get(i).getAngle()));
            measurementViewHolder.text4.setText("Čas: " + datapoints.get(i).getTime());
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
}

