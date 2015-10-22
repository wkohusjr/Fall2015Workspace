package bteamdevelopment.contactapp;

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
    private DBDataSource dbSource;
    private ImageView contactPhoto;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        dbSource = new DBDataSource(this);
        try {
            dbSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

        // Get Chosen ID from Bundle
        Bundle bundle = this.getIntent().getExtras();
        Long userID = bundle.getLong("User ID");

        // Get Contact by Passing Chosen ID
        Contact c = dbSource.getContact(userID);

        // Sets EditText Fields
        editFName.setText(c.getfName());
        editLName.setText(c.getlName());
        editMobile.setText(c.getMobile());
        editHome.setText(c.getHome());
        editEmail.setText(c.getEmail());
        editAddress.setText(c.getAddress());
        editCity.setText(c.getCity());
        editState.setText(c.getState());
        editZip.setText(c.getZip());

        // Set Contact's Image to ImageView
        byte[] outImage= c.getImage();
        Bitmap bmp = BitmapFactory.decodeByteArray(outImage, 0, outImage.length);
        contactPhoto.setImageBitmap(bmp);

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
    public void sendSuccessMessage(Contact contact)
    {
        // Send Updated Success Message and Send User Back to Main
        Toast.makeText(this, "Contact Updated!\n" +
                "First Name: " + contact.getfName() + "\n" +
                "Last Name: " + contact.getlName() + "\n" +
                "Mobile: " + contact.getMobile()
                , Toast.LENGTH_LONG).show();

        Intent mainIntent = new Intent(UpdateActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

    /*
       Using the ID from the Bundle Update the Contact Row in the Database with Data from the EditText Fields and Image
    */
    public void updateContact()
    {
        // Get Bundled ID
        Bundle bundle = this.getIntent().getExtras();
        Long userID = bundle.getLong("User ID");

        // Set Image View and Enable Drawing Cache
        ImageView view = (ImageView)findViewById(R.id.contactPhoto);
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bm = view.getDrawingCache();

        // Convert Bitmap to Byte for Database Entry
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte imageInByte[] = stream.toByteArray();

        // Declare Strings to be Passed into the Database
        String fName, lName, mobile, home, email, address, city, state, zip;

        // Declare Byte Array to be Passed into Database
        byte[] photo;

        Contact contact;

        // Populate String Variables with EditText Values
        // These Values will be Passed into the Database
        fName = editFName.getText().toString();
        lName = editLName.getText().toString();
        mobile = editMobile.getText().toString();
        home = editHome.getText().toString();
        email = editEmail.getText().toString();
        address = editAddress.getText().toString();
        city = editCity.getText().toString();
        state = editState.getText().toString();
        zip = editZip.getText().toString();
        photo = imageInByte;

        // Pass Values into Database updateContact()
        contact = dbSource.updatedContact(fName, lName, mobile, home, email, address, city, state, zip, photo, userID);

        // Send User Success Message
        sendSuccessMessage(contact);
    }

    // Get Bundled ID and Delete Corresponding Contact
    public void deleteContact() {
        Bundle bundle = this.getIntent().getExtras();
        Long userID = bundle.getLong("User ID");
        dbSource.deleteContact(userID);

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
