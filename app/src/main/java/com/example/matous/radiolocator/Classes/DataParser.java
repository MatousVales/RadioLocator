package com.example.matous.radiolocator.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.util.Xml;

import com.example.matous.radiolocator.Models.Datapoint;
import com.example.matous.radiolocator.Models.Measurement;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by Matous on 18.02.2016.
 */
public class DataParser {
    private static final String ns = null; // xml namespacy nepoužíváme
    private static DBHelper db;

     public static void parse(InputStream in, Context context) throws XmlPullParserException, IOException {
        db = new DBHelper(context);
         try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false); // xml namespacy nepoužíváme
            parser.setInput(in, null);
            parser.nextTag();
            readFeed(parser);
        } finally {
            in.close();
        }
    }

    private static void readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        Datapoint currentDatapoint = null;
        int id = 0;

        parser.require(XmlPullParser.START_TAG, ns, "Datapoints");

        String tag = null;

        while (parser.next() != XmlPullParser.END_DOCUMENT){
            switch (parser.getEventType()){
                case XmlPullParser.START_TAG:
                    tag = parser.getName();
                    if (tag.equals("Datapoint")){
                        currentDatapoint = new Datapoint();
                    } else if (currentDatapoint != null){
                        if (tag.equals("latitude")){
                            currentDatapoint.setLatitude(Double.parseDouble(parser.nextText()));
                        } else if (tag.equals("id")) {
                            id = Integer.parseInt(parser.nextText());
                        } else if (tag.equals("time")) {
                            currentDatapoint.setTime(parser.nextText());
                        } else if (tag.equals("longitude")){
                            currentDatapoint.setLongitude(Double.parseDouble(parser.nextText()));
                        } else if (tag.equals("angle")){
                            currentDatapoint.setAngle(Double.parseDouble(parser.nextText()));
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tag = parser.getName();
                    if(tag.equalsIgnoreCase("Datapoint") && currentDatapoint != null){
                        ContentValues cv = new ContentValues(5);
                        cv.put("mID",id);
                        cv.put("datetime",currentDatapoint.getTime());
                        cv.put("latitude",currentDatapoint.getLatitude());
                        cv.put("longitude",currentDatapoint.getLongitude());
                        cv.put("angle",currentDatapoint.getAngle());
                        db.insertMeasurement(cv);
                    }
            }
        }
    }
}
