package bteamdevelopment.contactappparse;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

/*
    UpdateActivity Updates the Current Contact Record in the Database
    Ability: to Change Image, Edit Contact, Save and Delete
    Takes Passed in ID, which is Bundled, and Pulls Corresponding Record
 */
public class UpdateActivity extends AppCompatActivity implements View.OnClickListener{

    // Declare Variables
    EditText editFName, editLName, editMobile, editHome, editEmail, editAddress, editCity, editState, editZip;
    String fName, lName, mobile, email, home, address, city, state, zip, id;

    private ImageView contactPhoto;
    private int PICK_IMAGE_REQUEST = 1;
    ParseObject contactData = new ParseObject("contactData");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Intent i = getIntent();
        id = i.getStringExtra("objectId");
        // Get the result of first name
        fName = i.getStringExtra("fName");
        // Get the result of last name
        lName = i.getStringExtra("lName");
        // Get the result of mobile
        mobile = i.getStringExtra("mobile");
        // Get the result of home
        home = i.getStringExtra("home");
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


        // Set EditTexts
        editFName = (EditText) findViewById(R.id.editFName);
        editLName = (EditText) findViewById(R.id.editLName);
        editMobile = (EditText)findViewById(R.id.editMobile);
        editHome = (EditText)findViewById(R.id.editHome);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editAddress = (EditText)findViewById(R.id.editAddress);
        editCity = (EditText)findViewById(R.id.editCity);
        editState = (EditText)findViewById(R.id.editState);
        editZip = (EditText)findViewById(R.id.editZip);


        ParseFile contactFile = (ParseFile)contactData.get("image");
        contactFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, com.parse.ParseException e) {
                if (e == null) {
                    // data has the bytes for the resume
                    ImageView image = (ImageView) findViewById(R.id.contactPhoto);
                    Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    image.setImageBitmap(bMap);
                }
            }
        });


        // Add PhoneNumber Formatting Watcher
        editMobile.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        editHome.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        contactPhoto = (ImageView)findViewById(R.id.contactPhoto);

        // Declare Buttons and Set Listeners
        Button btn_Delete = (Button) findViewById(R.id.btn_Delete);
        Button btn_Edit = (Button) findViewById(R.id.btn_Edit);
        Button btn_Upload = (Button) findViewById(R.id.btn_Upload);
        btn_Delete.setOnClickListener(this);
        btn_Edit.setOnClickListener(this);

        // Set OnClickListener for Upload Photo Button
        btn_Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_IMAGE_REQUEST);

            }
        });

        try {
            // Display Contact Data Takes Bundled ID and Pulls Corresponding Record
            displayContactData();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
        displayContactData() Takes Bundled ID and calls getContact() to Retrieve Data
     */
    public void displayContactData() throws SQLException, IOException {

        // Sets EditText Fields
        editFName.setText(fName);
        editLName.setText(lName);
        editMobile.setText(mobile);
        editHome.setText(home);
        editEmail.setText(email);
        editAddress.setText(address);
        editCity.setText(city);
        editState.setText(state);
        editZip.setText(zip);

    }

    @Override
    public void onClick(View v) {

        // if Button Click updateContact() and Return User back to Main
        if(v.getId()==R.id.btn_Edit) {
            updateContact();
        }

        // On Button Click deleteContact() and Return User back to Main
        if(v.getId() == R.id.btn_Delete)
        {
            deleteContact();
        }
    }

    /*
    Success Message to User to Let them Know a Contact has been Updated
    Return them to Main
 */
    public void sendSuccessMessage(String s, String t)
    {
        // Send Updated Success Message and Send User Back to Main
        Toast.makeText(this, "Contact Updated!\n" +
                "First Name: " + s + "\n" +
                "Last Name: " + t + "\n"
                , Toast.LENGTH_LONG).show();

        Intent mainIntent = new Intent(UpdateActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

    /*
       Using the ID from the Bundle Update the Contact Row in the Database with Data from the EditText Fields and Image
    */
    public void updateContact()
    {
        // Only Continue if Name and Mobile Number are Inserted
        // Making those Fields Required...
        if (editFName.getText().toString().equals("") || editLName.getText().toString().equals("") || editMobile.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Name and Mobile are Required Fields!", Toast.LENGTH_SHORT).show();
            return;
        } else {

            // Set ImageView
            ImageView view = (ImageView) findViewById(R.id.contactPhoto);

            // Enable and Build Drawing Cache
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();

            Bitmap bitmap = view.getDrawingCache();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] data = stream.toByteArray();

            ParseFile file = new ParseFile("resume.txt", data);
            file.saveInBackground();


            contactData.put("fName", editFName.getText().toString());
            contactData.put("lName", editLName.getText().toString());
            contactData.put("mobile", editMobile.getText().toString());
            contactData.put("email", editEmail.getText().toString());
            contactData.put("home", editHome.getText().toString());
            contactData.put("address", editAddress.getText().toString());
            contactData.put("city", editCity.getText().toString());
            contactData.put("state", editState.getText().toString());
            contactData.put("zip", editZip.getText().toString());
            contactData.put("image", file);
            contactData.saveInBackground();

            sendSuccessMessage(editFName.getText().toString(), editLName.getText().toString());

        }

    }

    // Get Bundled ID and Delete Corresponding Contact
    public void deleteContact() {

        // Delete Contact

        // Return User to Main
        Intent mainIntent = new Intent(UpdateActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

    // Display Results Obtained from the Application Behavior
    // When the Upload Button is Clicked Open MediaStore Images and Place Selected Image into ImageView
    // http://codetheory.in/android-pick-select-image-from-gallery-with-intents/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get Data
            Uri selectedImage = data.getData();

            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                ImageView imageView = (ImageView) findViewById(R.id.contactPhoto);
                imageView.setImageBitmap(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
