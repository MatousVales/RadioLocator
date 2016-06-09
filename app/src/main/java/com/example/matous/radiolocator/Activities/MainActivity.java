package com.example.matous.radiolocator.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.matous.radiolocator.Classes.DBHelper;
import com.example.matous.radiolocator.R;

public class MainActivity extends AppCompatActivity {

    Button Card1Button;
    Button Card2Button;
    Button Card3Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Card1Button = (Button)findViewById(R.id.card1_button1);
        Card1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }
        });

        Card2Button = (Button)findViewById(R.id.card2_button);
        Card2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DatabaseActivity.class);
                startActivity(intent);
            }
        });
    }
}
