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

public class VolumeActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_volume);

        // User Input Value to be Calculated and Converted
        user_input=(EditText) findViewById(R.id.editText);
        // Set background color of EditText
        // Use white so it shows up against the background
        user_input.setBackgroundColor(getResources().getColor(R.color.white));

        // Declare Input and Output Spinners
        final Spinner input_unit = (Spinner) findViewById(R.id.convertFromVolume);
        final Spinner output_unit = (Spinner) findViewById(R.id.convertToVolume);

        // Connect the Adapter to the Spinners
        input_unit.setAdapter(loadSpinnerData());
        output_unit.setAdapter(loadSpinnerData());

        // Declare Fun Fact
        factText = (TextView) findViewById(R.id.fact);
        // Declare Output TextView
        result = (TextView) findViewById(R.id.volumeResult);

        // Declare Convert Button
        Button convertButton = (Button) findViewById(R.id.volumeButton);


        // Create onClickListener for the Convert Button
        convertButton.setOnClickListener(new View.OnClickListener() {
            /**
             * OnClickListener for the button triggers onClick() and checks for:
             *      1) A value in $editText TextView
             *      2) Units have been selected in the spinners
             *  If data is valid, it takes the value from editText and calculates and converts the volume
             *  The result is displayed to $output_value
             */
            @Override
            public void onClick(View v) {
                // Declare Variables
                double initialValue;
                double finalValue = 0;
                double mid = 0;

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
                 * Get value of spin1 and and spin2 then use $mid, $initialValue, $finalValue to calculate volume
                 * If the same options are chosen in each spinner the calculations will nullify themselves and output the user's input
                 * Unit Conversions from https://github.com/a--hoang/EC327-Unit-Converter
                 * */
                if (spin1==1) {
                    mid = 29.57*initialValue;
                } else if (spin1==2) {
                    mid = 16.0*initialValue;
                } else if (spin1==3) {
                    mid = 250.0*initialValue;
                } else if (spin1==4) {
                    mid = 500.0*initialValue;
                } else if (spin1==5) {
                    mid = 1000.0 * initialValue;
                } else if (spin1==6) {
                    mid = 3785.0 * initialValue;
                }else if (spin1==7){
                    mid =  initialValue;
                } else if (spin1==8){
                    mid = 1000.0 * initialValue;
                }

                //convert ml to other unit
                if (spin2==1) {
                    finalValue = mid/29.57;
                } else if (spin2==2) {
                    finalValue  = mid/16.0;
                } else if (spin2==3) {
                    finalValue = mid/250.0;
                } else if (spin2==4) {
                    finalValue  = mid/500.0;
                } else if (spin2== 5){
                    finalValue  = mid/1000.0;
                } else if (spin2==6) {
                    finalValue  = mid/3785.0;
                } else if (spin2==7 ) {
                    finalValue  = mid;
                }else if (spin2==8 ) {
                    finalValue  = mid/1000.0;
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
        String unit = "VOLUME";
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
                fact = "Symbol: fll oz, is a unit of volume (also called capacity) typically used for measuring liquids. It is equivalent to approximately 30 millilitres .";
                break;
            case 2:
                measure = "tbsp";
                fact = "Symbol: tbsp or T,  The unit of measurement varies by region: a United States tablespoon is approximately 14.7 mL, a United Kingdom tablespoon is exactly 15 mL, and an Australian tablespoon is 20 mL.";
                break;
            case 3:
                measure = "cup";
                fact = "The cup is a unit of measurement for volume, used in cooking to measure liquids (fluid measurement) and bulk foods such as granulated sugar (dry measurement). ";
                break;
            case 4:
                measure = "pt";
                fact= "Symbol: pt or p, The pint is a unit of volume or capacity in both the United States customary and British imperial measurement systems. ";
                break;
            case 5:
                measure = "qt";
                fact = "Symbol: qt, is a unit of volume (for either the imperial or United States customary units) equal to a quarter of a gallon (hence the name quart), two pints, or four cups.";
                break;
            case 6:
                measure = "g";
                fact = "The gallon is a measure of liquid capacity in both the US customary units and the British imperial systems of measurement. Three significantly different sizes are in current use: the imperial gallon defined as 4.54609 litres, which is used in the United Kingdom, Canada, and some Caribbean nations; the US gallon defined as 231 cubic inches.";
                break;
            case 7:
                measure = "ml";
                fact = "A milliliter (ml) is a derived metric measurement unit of volume with sides equal to one centimeter (1cm). The milliliter is used to measure volume of liquid.";
                break;
            case 8:
                measure = "l";
                fact = "A liter (L) or (l) is a metric unit of volume with sides equal to one decimeter (1dm) or ten centimeters (10cm). The liter is used to measure volume of liquid.";
                break;
        }
        return measure + fact;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_volume, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_length:
                Intent lengthIntent = new Intent(VolumeActivity.this, LengthActivity.class);
                startActivity(lengthIntent);
                return true;
            case R.id.action_temp:
                Intent tempIntent = new Intent(VolumeActivity.this, TempActivity.class);
                startActivity(tempIntent);
                return true;
            case R.id.action_time:
                Intent timeIntent = new Intent(VolumeActivity.this, TimeActivity.class);
                startActivity(timeIntent);
                return true;
            case R.id.action_volume:
                Intent volumeIntent = new Intent(VolumeActivity.this, VolumeActivity.class);
                startActivity(volumeIntent);
                return true;
            case R.id.action_weight:
                Intent weightIntent = new Intent(VolumeActivity.this, WeightActivity.class);
                startActivity(weightIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}