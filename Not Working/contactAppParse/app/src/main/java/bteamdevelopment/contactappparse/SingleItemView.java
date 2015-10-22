package bteamdevelopment.contactappparse;

/**
 * Created by wkohusjr on 10/16/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleItemView extends Activity {
    // Declare Variables
    String fName, lName, mobile, email, home, address, city, state, zip;
    String position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.activity_update);

        Intent i = getIntent();
        // Get the result of first name
        fName = i.getStringExtra("fName");
        // Get the result of last name
        lName = i.getStringExtra("lName");
        // Get the result of home
        home = i.getStringExtra("home");
        // Get the result of mobile
        mobile = i.getStringExtra("mobile");
        // Get the result of email
        email = i.getStringExtra("email");
        // Get the result of address
        address = i.getStringExtra("address");
        // Get the result of city
        city = i.getStringExtra("city");
        // Get the result of state
        state = i.getStringExtra("state");
        // Get the result of zip
        zip = i.getStringExtra("zip");


        // Locate the TextViews
        TextView txtfName = (TextView) findViewById(R.id.editFName);
        TextView txtlName = (TextView) findViewById(R.id.editLName);
        TextView txtmobile = (TextView) findViewById(R.id.editMobile);
        TextView txthome = (TextView) findViewById(R.id.editHome);
        TextView txtemail = (TextView) findViewById(R.id.editEmail);
        TextView txtaddress = (TextView) findViewById(R.id.editAddress);
        TextView txtcity = (TextView) findViewById(R.id.editCity);
        TextView txtstate = (TextView) findViewById(R.id.editState);
        TextView txtzip = (TextView) findViewById(R.id.editZip);

        // Set results to the TextViews
        txtfName.setText(fName);
        txtlName.setText(lName);
        txtmobile.setText(mobile);
        txthome.setText(home);
        txtemail.setText(email);
        txtaddress.setText(address);
        txtcity.setText(city);
        txtstate.setText(state);
        txtzip.setText(zip);

    }
}