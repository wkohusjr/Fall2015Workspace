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

public class TimeActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_time);

        // User Input Value to be Calculated and Converted
        user_input=(EditText) findViewById(R.id.editText);
        // Set background color of EditText
        // Use white so it shows up against the background
        user_input.setBackgroundColor(getResources().getColor(R.color.white));

        // Declare Input and Output Spinners
        final Spinner input_unit = (Spinner) findViewById(R.id.convertFromTime);
        final Spinner output_unit = (Spinner) findViewById(R.id.convertToTime);

        // Connect the Adapter to the Spinners
        input_unit.setAdapter(loadSpinnerData());
        output_unit.setAdapter(loadSpinnerData());

        // Declare Fun Fact
        factText = (TextView) findViewById(R.id.fact);
        // Declare Output TextView
        result = (TextView) findViewById(R.id.timeResult);

        // Declare Convert Button
        Button convertButton = (Button) findViewById(R.id.timeButton);

        // Create onClickListener for the Convert Button
        convertButton.setOnClickListener(new View.OnClickListener() {
            /**
             * OnClickListener for the button triggers onClick() and checks for:
             *      1) A value in $editText TextView
             *      2) Units have been selected in the spinners
             *  If data is valid, it takes the value from editText and calculates and converts the length
             *  The result is displayed to $output_value
             */
            @Override
            public void onClick(View v) {
                // Declare Variables
                double initialValue;
                double finalValue = 0;
                double inter = 0;

                // if input_value Text Box is Empty, send message to user
                if  ((user_input.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(), "Please Enter a Value to Convert", Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    initialValue = Double.parseDouble(user_input.getText().toString());
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
                 * Get value of spin1 and and spin2 then use $mid, $initialValue, $finalValue to calculate time
                 * If the same options are chosen in each spinner the calculations will nullify themselves and output the user's input
                 * Unit conversions from https://github.com/a--hoang/EC327-Unit-Converter
                 * */
                //convert to intermediate
                if (spin1 == 1) {
                    inter = initialValue;
                } else if (spin1 == 2) {
                    inter = initialValue * 60;
                } else if (spin1 == 3) {
                    inter = initialValue * 60 * 60;
                } else if (spin1 == 4) {
                    inter = initialValue * 24 * 60 * 60;
                } else if (spin1 == 5) {
                    inter = initialValue * 7 * 24 * 60 * 60;
                }


                //convert to final value
                if (spin2 == 1) {
                    finalValue = inter;
                } else if (spin2 == 2) {
                    finalValue = inter / 60.0;
                } else if (spin2 == 3) {
                    finalValue = inter / 60.0 / 60.0;
                } else if (spin2 == 4) {
                    finalValue = inter / 24.0 / 60.0 / 60.0;
                } else if (spin2 == 5) {
                    finalValue = inter / 7.0 / 24.0 / 60.0 / 60.0;
                }
                // Set Decimal Places
                DecimalFormat df = new DecimalFormat("0");

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
        String unit = "TIME";
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
                measure = "Second(s)";
                fact = "The second (abbreviation, s or sec) is the Standard International unit of time. One second is the time that elapses during 9,192,631,770 (9.192631770 x 10 9 ) cycles of the radiation produced by the transition between two levels of the cesium 133 atom.";
                break;
            case 2:
                measure = "Minute(s)";
                fact = "The minute is a unit of time or of angle. As a unit of time, the minute is equal to 1/60 (the first sexagesimal fraction) of an hour or 60 seconds.";
                break;
            case 3:
                measure = "Hour(s)";
                fact = "The hour (common symbol: h or hr) is a unit of measurement of time. In modern usage, an hour comprises 60 minutes, or 3,600 seconds. It is approximately 1/24 of a mean solar day.";
                break;
            case 4:
                measure = "Day(s)";
                fact = "A day is a unit of time. In common usage, it is an interval equal to 24 hours.[1] It also can mean the consecutive period of time during which the Sun is above the horizon of a location, also known as daytime. The period of time during which the Earth completes one rotation with respect to the Sun is called a solar day.";
                break;
            case 5:
                measure = "Week(s)";
                fact = "A week is a time unit equal to seven days. It is the standard time period used for cycles of work days and rest days in most parts of the world, mostly alongside (but not strictly part of) the Gregorian calendar.";
                break;
        }
        return measure + fact;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_length:
                Intent lengthIntent = new Intent(TimeActivity.this, LengthActivity.class);
                startActivity(lengthIntent);
                return true;
            case R.id.action_temp:
                Intent tempIntent = new Intent(TimeActivity.this, TempActivity.class);
                startActivity(tempIntent);
                return true;
            case R.id.action_time:
                Intent timeIntent = new Intent(TimeActivity.this, TimeActivity.class);
                startActivity(timeIntent);
                return true;
            case R.id.action_volume:
                Intent volumeIntent = new Intent(TimeActivity.this, VolumeActivity.class);
                startActivity(volumeIntent);
                return true;
            case R.id.action_weight:
                Intent weightIntent = new Intent(TimeActivity.this, WeightActivity.class);
                startActivity(weightIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}