package com.example.matous.radiolocator.Classes;

import android.content.Context;
import android.util.Log;


import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Created by Matous on 02.04.2016.
 */
public class TCPClient {


    private String serverMessage;
    private String currentID;
    public static String SERVERIP;
    public static int SERVERPORT;
    private boolean mRun = false;
    private Context c;
    private OnMessageReceived mMessageListener = null;

    BufferedReader in;
    DBHelper db;
    StringBuilder b = new StringBuilder();

    public TCPClient(OnMessageReceived listener,Context context, String ip, int port) {
        mMessageListener = listener;
        c = context;
        db = new DBHelper(c);
        this.SERVERIP = ip;
        this.SERVERPORT = port;
    }

    public void stopClient(){
        mRun = false;
    }

    public void run() {

        mRun = true;

        try {
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
            Socket socket = new Socket(serverAddr, SERVERPORT);

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (mRun) {
                    serverMessage = in.readLine();
                    b.append(serverMessage);
                    if(serverMessage.substring(0,8).equals("    <id>")){
                        currentID = serverMessage.substring(8,serverMessage.length() - 5);
                    }
                    if (serverMessage.equals("  </Datapoint></Datapoints>")) {
                        InputStream is = new ByteArrayInputStream(b.toString().getBytes(StandardCharsets.UTF_16));
                        DataParser.parse(is, c);
                        b.setLength(0);
                        mMessageListener.messageReceived(currentID);
                    }
                }
            }catch (Exception e) {
                Log.e("TCP", "S: Error", e);
            }finally {
                socket.close();
            }

        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }

    }

    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

}
