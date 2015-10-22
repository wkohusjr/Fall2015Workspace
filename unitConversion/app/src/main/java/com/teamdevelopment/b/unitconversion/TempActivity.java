package com.teamdevelopment.b.unitconversion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.List;

public class TempActivity extends AppCompatActivity {
    private EditText user_input;
    private TextView result;
    private TextView factText;
    private String fact;
    private int spin1;
    private int spin2;
    private String measure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        // User Input Value to be Calculated and Converted
        user_input=(EditText) findViewById(R.id.editText);
        // Set background color of EditText
        // Use white so it shows up against the background
        user_input.setBackgroundColor(getResources().getColor(R.color.white));

        // Declare Input and Output Spinners
        final Spinner input_unit = (Spinner) findViewById(R.id.convertFromTemp);
        final Spinner output_unit = (Spinner) findViewById(R.id.convertToTemp);

        // Connect the Adapter to the Spinners
        input_unit.setAdapter(loadSpinnerData());
        output_unit.setAdapter(loadSpinnerData());

        // Declare Fun Fact
        factText = (TextView) findViewById(R.id.fact);
        // Declare Output TextView
        result = (TextView) findViewById(R.id.tempResult);

        // Declare Convert Button
        Button convertButton = (Button) findViewById(R.id.tempButton);

        // Create onClickListener for the Convert Button
        convertButton.setOnClickListener(new View.OnClickListener() {
            /**
             * OnClickListener for the button triggers onClick() and checks for:
             *      1) A value in $editText TextView
             *      2) Units have been selected in the spinners
             *  If data is valid, it takes the value from editText and calculates and converts the weight
             *  The result is displayed to $output_value
             */
            @Override
            public void onClick(View v) {
                // Declare Variables
                double initialValue = 0;
                double finalValue = 0;
                double mid = 0;

                // if Text Box is Empty, do Nothing
                if  ((user_input.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(), "Please Enter a Value to Convert", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Unit Initial Input to be Converted
                spin1 = input_unit.getSelectedItemPosition();

                // Unit Final Output Result
                spin2 = output_unit.getSelectedItemPosition();

                // Check to see if both Units have been Selected
                // Print error if not
                if (spin1 == 0 || spin2 == 0) {
                    Toast.makeText(getApplicationContext(), "Please Choose Units", Toast.LENGTH_SHORT).show();
                    return;
                }
                /**
                 * $spin1 = spinner option #1
                 * $mid = value to be calculated by spinner option #2
                 * $spin2 = spinner option #2
                 * Get value of spin1 and and $spin2 then use $mid, $initialValue, $finalValue to calculate temperature
                 * Unit conversions from https://github.com/a--hoang/EC327-Unit-Converter
                 * */
                // Convert Initial Value
                // If Celsius
                if (spin1 == 1) {
                    mid = initialValue;

                 // else if Fahrenheit
                } else if (spin1 == 2) {
                    mid = (initialValue - 32) * (5.0/9.0);

                 // else if Kelvin
                }else if (spin1 == 3) {
                    mid = initialValue - 273.15;
                }

                // Convert to Final Value
                // If Celsius
                if (spin2 == 1) {
                    finalValue = mid;

                // else if Fahrenheit
                } else  if (spin2 == 2) {
                    finalValue = mid * (9.0/5.0) + 32;

                // else if Kelvin
                }else if (spin2 == 3) {
                    finalValue = Math.round((mid + 273.150) * 100.0) / 100.0;
                }
                    // Set Decimal Places
                    DecimalFormat df = new DecimalFormat("0.0");

                // Check $spin2 value and set String label for end result
                setValue(spin2);

                // Set fact value based on $spin2
                factText.setText(fact);

                // Print Final Calculation
                result.setText(df.format(finalValue) + " " + measure);

            }

        });

    }

    /*
    * loadSpinnerData() calls getAllData() in the DatabaseHelper class to get data from passed in Table name.
    * Creates a List<> Array of units and uses a ArrayAdapter to connect it to the spinner.
    * Returns the ArrayAdapter<> to the spinner that uses setAdapter.
    * @unit = Name of Table
    */
    private ArrayAdapter loadSpinnerData()
    {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        String unit = "TEMP";
        List<String> units = db.getAllData(unit);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return dataAdapter;
    }

    /*
        Create fun fact to display for each result.
        Also display measure which will correspond to the fun fact based on the second spinner option.
     */
    public String setValue(int spin2) {
        switch (spin2) {
            case 1:
                measure = "Celsius";
                fact = "The Celsius temperature scale is also referred to as the centigrade scale. Centigrade means consisting of or divided into 100 degrees.";
                break;
            case 2:
                measure = "Fahrenheit";
                fact = "Fahrenheit was proposed in 1724 by the German physicist Daniel Gabriel Fahrenheit, after whom the scale is named.";
                break;
            case 3:
                measure = "Kelvin";
                fact = "The Kelvin temperature scale was the brainchild of Belfast-born British inventor and scientist William Thomson — also known as Lord Kelvin. It is one of the three best-known scales used to measure temperature, along with Fahrenheit and Celsius.";
                break;
        }
        return measure + fact;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_temp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_length:
                Intent lengthIntent = new Intent(TempActivity.this, LengthActivity.class);
                startActivity(lengthIntent);
                return true;
            case R.id.action_temp:
                Intent tempIntent = new Intent(TempActivity.this, TempActivity.class);
                startActivity(tempIntent);
                return true;
            case R.id.action_time:
                Intent timeIntent = new Intent(TempActivity.this, TimeActivity.class);
                startActivity(timeIntent);
                return true;
            case R.id.action_volume:
                Intent volumeIntent = new Intent(TempActivity.this, VolumeActivity.class);
                startActivity(volumeIntent);
                return true;
            case R.id.action_weight:
                Intent weightIntent = new Intent(TempActivity.this, WeightActivity.class);
                startActivity(weightIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}