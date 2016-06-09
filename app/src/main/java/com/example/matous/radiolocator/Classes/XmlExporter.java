package com.example.matous.radiolocator.Classes;

import android.content.Context;
import android.os.Environment;
import android.util.Xml;
import android.widget.Toast;

import com.example.matous.radiolocator.Activities.DatabaseActivity;
import com.example.matous.radiolocator.Models.Datapoint;
import com.example.matous.radiolocator.Models.Measurement;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Matous on 09.03.2016.
 */
public class XmlExporter {
    private ArrayList<Measurement> measurements = null;
    private ArrayList<Datapoint> datapoints = null;
    private DBHelper db;
    private Context context;

    public  XmlExporter(ArrayList<Measurement> m, Context c){
        this.measurements = m;
        this.context = c;
        db = new DBHelper(context);
    }

    public File export(){
        try {
            File f = new File(context.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath(),"exportedMeasurement.xml");
            if(!f.exists()) {
                f.createNewFile();
            }
            boolean empty = true;

            FileWriter writer = new FileWriter(f);
            XmlSerializer xml = Xml.newSerializer();
            xml.setOutput(writer);
            xml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            xml.startDocument("UTF-8", true);
            xml.startTag("", "Datapoints");

            for (Measurement m : measurements) {
                if (m.isSelected()) {
                    empty = false;
                    datapoints = db.getDatapoints(m.getNumber());
                    for (Datapoint d : datapoints){
                        xml.startTag("", "Datapoint");
                        xml.startTag("", "id");
                        xml.text(Integer.toString(m.getNumber()));
                        xml.endTag("","id");
                        xml.startTag("","latitude");
                        xml.text(Double.toString(d.getLatitude()));
                        xml.endTag("","latitude");
                        xml.startTag("","longitude");
                        xml.text(Double.toString(d.getLongitude()));
                        xml.endTag("","longitude");
                        xml.startTag("","angle");
                        xml.text(Double.toString(d.getAngle()));
                        xml.endTag("","angle");
                        xml.startTag("","time");
                        xml.text(d.getTime());
                        xml.endTag("","time");
                        xml.endTag("","Datapoint");

                    }
                }
            }
            xml.endTag("","Datapoints");
            xml.endDocument();
            writer.flush();
            writer.close();

            if (empty){
                Toast.makeText(context, "Nejprve zaškrtněte měření, která chcete exportovat.",Toast.LENGTH_SHORT).show();

            } else {
                return f;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
