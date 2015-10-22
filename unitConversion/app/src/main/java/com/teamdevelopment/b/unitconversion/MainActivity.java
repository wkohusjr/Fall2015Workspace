package com.teamdevelopment.b.unitconversion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper unitDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Call constructor to Create Database
        unitDB = new DatabaseHelper(this);

        try {
            unitDB.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);

        // Declare Buttons
        Button temp = (Button)findViewById(R.id.temp);
        Button length = (Button)findViewById(R.id.length);
        Button weight = (Button)findViewById(R.id.weight);
        Button volume = (Button)findViewById(R.id.volume);
        Button time = (Button)findViewById(R.id.time);

        // Set listeners for buttons and pass @tableName for button click counter
        setButtonListeners(temp);
        setButtonListeners(length);
        setButtonListeners(volume);
        setButtonListeners(time);
        setButtonListeners(weight);
    }

    public void setButtonListeners(Button btn)
    {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(v);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_length:
                Intent lengthIntent = new Intent(MainActivity.this, LengthActivity.class);
                startActivity(lengthIntent);
                return true;
            case R.id.action_temp:
                Intent tempIntent = new Intent(MainActivity.this, TempActivity.class);
                startActivity(tempIntent);
                return true;
            case R.id.action_time:
                Intent timeIntent = new Intent(MainActivity.this, TimeActivity.class);
                startActivity(timeIntent);
                return true;
            case R.id.action_volume:
                Intent volumeIntent = new Intent(MainActivity.this, VolumeActivity.class);
                startActivity(volumeIntent);
                return true;
            case R.id.action_weight:
                Intent weightIntent = new Intent(MainActivity.this, WeightActivity.class);
                startActivity(weightIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openActivity(View v)
    {
        // Open Activity by ID
        switch (v.getId()){
            // If temp button is Clicked Open TempActivity
            case R.id.temp:
                Intent tempIntent = new Intent(MainActivity.this, TempActivity.class);
                startActivity(tempIntent);
                break;
            // If length button is Clicked Open LengthActivity
            case R.id.length:
                Intent lengthIntent = new Intent(MainActivity.this, LengthActivity.class);
                startActivity(lengthIntent);
                break;
            // If weight button is Clicked Open WeightActivity
            case R.id.weight:
                Intent weightIntent = new Intent(MainActivity.this, WeightActivity.class);
                startActivity(weightIntent);
                break;
            // If volume button is Clicked Open VolumeActivity
            case R.id.volume:
                Intent volumeIntent = new Intent(MainActivity.this, VolumeActivity.class);
                startActivity(volumeIntent);
                break;
            // If time button is Clicked Open TimeActivity
            case R.id.time:
                Intent timeIntent = new Intent(MainActivity.this, TimeActivity.class);
                startActivity(timeIntent);
                break;
        }
    }
    }
