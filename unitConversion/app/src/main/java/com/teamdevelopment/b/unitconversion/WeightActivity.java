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

public class WeightActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_weight);

        // User Input Value to be Calculated and Converted
        user_input=(EditText) findViewById(R.id.editText);
        // Set background color of EditText
        // Use white so it shows up against the background
        user_input.setBackgroundColor(getResources().getColor(R.color.white));

        // Declare Input and Output Spinners
        final Spinner input_unit = (Spinner) findViewById(R.id.convertFromWeight);
        final Spinner output_unit = (Spinner) findViewById(R.id.convertToWeight);

        // Connect the Adapter to the Spinners
        input_unit.setAdapter(loadSpinnerData());
        output_unit.setAdapter(loadSpinnerData());

        // Declare Fun Fact
        factText = (TextView) findViewById(R.id.fact);
        // Declare Output TextView
        result = (TextView) findViewById(R.id.weightResult);

        // Declare Convert Button
        Button convertButton = (Button) findViewById(R.id.weightButton);

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
                double initialValue;
                double finalValue = 0;
                double mid = 0;

                // if Text Box is Empty, do Nothing
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
                 * Get value of spin1 and and spin2 then use $mid, $initialValue, $finalValue to calculate weight
                 * If the same options are chosen in each spinner the calculations will nullify themselves and output the user's input
                 * Unit conversions from https://github.com/a--hoang/EC327-Unit-Converter
                 * */
                if (spin1==1) {
                    mid= 28.6*initialValue;
                } else if (spin1==2) {
                    mid = 453.6 * initialValue;
                }else if (spin1==3) {
                    mid = initialValue;
                }else if (spin1==4) {
                    mid = 1000.0 * initialValue;
                }

                //convert gram to other unit
                if (spin2==1) {
                    finalValue = mid/28.6;
                } else if (spin2== 2){
                    finalValue = mid/453.6;
                } else if (spin2== 3) {
                    finalValue = mid;
                } else if (spin2== 4) {
                    finalValue = mid/1000.0;
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
    * loadSpinnerData() calls getAllData() in the DatabaseHelper class to get data from passed in Table name.
    * Creates a List<> Array of units and uses a ArrayAdapter to connect it to the spinner.
    * Returns the ArrayAdapter<> to the spinner that uses setAdapter.
     * @unit = Name of Table
     */
    private ArrayAdapter loadSpinnerData()
    {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        String unit = "WEIGHT";
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
                measure = "oz";
                fact = "Symbol: oz or ounce is usually the international avoirdupois ounce as used in the United States customary and British imperial systems, which is equal to one-sixteenth of a pound or approximately 28 grams";
                break;
            case 2:
                measure = "lb";
                fact = "Symbol: lb or lb, lbm, lbm, is a unit of mass used in the imperial, United States customary and other systems of measurement. A number of different definitions have been used, the most common today being the international avoirdupois pound which is legally defined as exactly 0.45359237 kilograms";
                break;
            case 3:
                measure = "g";
                fact = "The Symbol: is a metric system unit of mass. Gram can be abbreviated as gm or gr. The gram, is 1/1000th of a kilogram.";
                break;
            case 4:
                measure = "kg";
                fact = "The Symbol: kg, is the base unit of mass in the International System of Units (SI) (the Metric system) and is defined as being equal to the mass of the International Prototype of the Kilogram (IPK)";
                break;
        }
        return measure + fact;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weight, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_length:
                Intent lengthIntent = new Intent(WeightActivity.this, LengthActivity.class);
                startActivity(lengthIntent);
                return true;
            case R.id.action_temp:
                Intent tempIntent = new Intent(WeightActivity.this, TempActivity.class);
                startActivity(tempIntent);
                return true;
            case R.id.action_time:
                Intent timeIntent = new Intent(WeightActivity.this, TimeActivity.class);
                startActivity(timeIntent);
                return true;
            case R.id.action_volume:
                Intent volumeIntent = new Intent(WeightActivity.this, VolumeActivity.class);
                startActivity(volumeIntent);
                return true;
            case R.id.action_weight:
                Intent weightIntent = new Intent(WeightActivity.this, WeightActivity.class);
                startActivity(weightIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
