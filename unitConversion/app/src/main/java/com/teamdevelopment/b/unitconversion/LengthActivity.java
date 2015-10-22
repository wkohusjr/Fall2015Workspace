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


public class LengthActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_length);

        // User Input Value to be Calculated and Converted
        user_input=(EditText) findViewById(R.id.editText);

        // Set background color of EditText
        // Use white so it shows up against the background
        user_input.setBackgroundColor(getResources().getColor(R.color.white));

        // Declare Input and Output Spinners
        final Spinner input_unit = (Spinner) findViewById(R.id.convertFromLength);
        final Spinner output_unit = (Spinner) findViewById(R.id.convertToLength);

        // Connect the Adapter to the Spinners
        input_unit.setAdapter(loadSpinnerData());
        output_unit.setAdapter(loadSpinnerData());

        // Declare Fun Fact
        factText = (TextView) findViewById(R.id.fact);
        // Declare Output TextView
        result = (TextView) findViewById(R.id.lengthResult);

        // Declare Convert Button
        Button convertButton = (Button) findViewById(R.id.lengthButton);

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

                // Unit Initial Input to be Converted
                spin1 = input_unit.getSelectedItemPosition();

                // Unit Final Output Result
                spin2 = output_unit.getSelectedItemPosition();

                // if input_value Text Box is Empty, send message to user
                if  ((user_input.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(), "Please Enter a Value to Convert", Toast.LENGTH_SHORT).show();
                    return;

                    // else parse value as double
                } else {
                    initialValue = Double.parseDouble(user_input.getText().toString());
                }

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
                 * Get value of spin1 and and spin2 then use $mid, $initialValue, $finalValue to calculate length
                 * If the same options are chosen in each spinner the calculations will nullify themselves and output the user's input
                 * Unit conversions from https://github.com/a--hoang/EC327-Unit-Converter
                 * */
                if (spin1 == 1) {
                    inter = initialValue*1000;
                } else if (spin1 == 2) {
                    inter = initialValue;
                } else if (spin1 == 3) {
                    inter = initialValue/100;
                } else if (spin1 == 4) {
                    inter = initialValue/1000;
                } else if (spin1 == 5) {
                    inter = initialValue/39.3701;
                } else if (spin1 == 6) {
                    inter = initialValue/3.2808;
                } else if (spin1 == 7) {
                    inter = initialValue/1.0936;
                } else if (spin1 == 8) {
                    inter = initialValue*1609;
                }

                if (spin2 == 1) {
                    finalValue = inter/1000.0;
                } else if (spin2 == 2) {
                    finalValue = inter;
                } else if (spin2 == 3) {
                    finalValue = inter*100;
                } else if (spin2 == 4) {
                    finalValue = inter*1000;
                } else if (spin2 == 5) {
                    finalValue = inter*39.3701;
                } else if (spin2 == 6) {
                    finalValue = inter*3.2808;
                } else if (spin2 == 7) {
                    finalValue = inter*1.0936;
                } else if (spin2 == 8) {
                    finalValue = inter/1609.0;
                }
                // Set Decimal Places
                DecimalFormat df = new DecimalFormat("0.000");

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
        Create fun fact to display for each result.
        Also display measure which will correspond to the fun fact based on the second spinner option.
     */
    public String setValue(int spin2) {
        switch (spin2) {
            case 1:
                measure = "km";
                fact = "Symbol: km or kilometer, is a unit of length in the metric system, equal to one thousand metres (kilo- being the SI prefix for 1000)";
                break;
            case 2:
                measure = "m";
                fact = "Symbol: m or meter. The meter is defined as the distance travelled by light in a specific fraction – about one three-hundred millionths – of a second.";
                break;
            case 3:
                measure = "cm";
                fact = "Symbol: cm or centimeter, is a unit of length in the metric system, equal to one hundredth of a metre, centi being the SI prefix for a factor of 1/100. The centimetre was the base unit of length in the now deprecated centimetre–gram–second (CGS) system of units.";
                break;
            case 4:
                measure = "mm";
                fact= "Symbol: mm or millimeter, is a unit of length in the metric system, equal to one thousandth of a metre, which is the SI base unit of length. It is equal to 1,000 micrometres and 1,000,000 nanometres. There are 25.4 mm in one inch by definition, so a millimetre is exactly equal to 5/127 inch.";
                break;
            case 5:
                measure = "in";
                fact = "Symbol: in or inch, is a unit of length in the imperial and United States customary systems of measurement. Historically an inch was also used in a number of other systems of units. Traditional standards for the exact length of an inch have varied in the past, but since July 1959 when the international yard was defined as 0.9144 metres, the international inch has been exactly 25.4 mm. There are 12 inches in a foot and 36 inches in a yard.";
                break;
            case 6:
                measure = "ft";
                fact = "Symbol: ft or foot, is a unit of length in the imperial and US customary systems of measurement. Since 1959, both units have been defined by international agreement as equivalent to 0.3048 meters exactly. In both systems, the foot comprises 12 inches and three feet compose a yard.";
                break;
            case 7:
                measure = "yd";
                fact = "Symbol: yd or yard, is an English unit of length, in both the British imperial and US customary systems of measurement, that comprises 3 feet or 36 inches. It is by international agreement in 1959 standardized as exactly 0.9144 meters. A metal yardstick originally formed the physical standard from which all other units of length were officially derived in both English systems.";
                break;
            case 8:
                measure = "mi";
                fact = "Symbol: mi or mile, the mile is an English unit of length equal to 1,760 yards (1,610 m) and standardised as exactly 1.609344 kilometres by international agreement in 1959.";
                break;
        }
        return measure + fact;
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
        String unit = "LENGTH";
        List<String> units = db.getAllData(unit);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return dataAdapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_length, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_length:
                Intent lengthIntent = new Intent(LengthActivity.this, LengthActivity.class);
                startActivity(lengthIntent);
                return true;
            case R.id.action_temp:
                Intent tempIntent = new Intent(LengthActivity.this, TempActivity.class);
                startActivity(tempIntent);
                return true;
            case R.id.action_time:
                Intent timeIntent = new Intent(LengthActivity.this, TimeActivity.class);
                startActivity(timeIntent);
                return true;
            case R.id.action_volume:
                Intent volumeIntent = new Intent(LengthActivity.this, VolumeActivity.class);
                startActivity(volumeIntent);
                return true;
            case R.id.action_weight:
                Intent weightIntent = new Intent(LengthActivity.this, WeightActivity.class);
                startActivity(weightIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
