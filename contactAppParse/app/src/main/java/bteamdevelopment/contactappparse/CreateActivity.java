package bteamdevelopment.contactappparse;

        import android.content.Intent;
        import android.graphics.Bitmap;
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

        import com.parse.ParseACL;
        import com.parse.ParseFile;
        import com.parse.ParseObject;
        import com.parse.ParseUser;

        import java.io.ByteArrayOutputStream;
        import java.io.IOException;

/*
    CreateActivity class Provides Activity for Creating New Contacts
    Takes Data from EditTexts and ImageView and Inserts Values into Database by Calling createContact()
    On Success: It will Display Message to User and Return User to MainActivity
 */
public class CreateActivity extends AppCompatActivity {

    // Declare Variables
    private EditText new_fName, new_lName, new_mobile, new_home, new_email, new_address, new_city, new_state, new_zip;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        // Declare Buttons
        Button btnCreate = (Button) findViewById(R.id.button_submit);
        Button btnUpload = (Button) findViewById(R.id.button_addImage);

        // Declare ImageView and Set Default Image to blank.jpg from Drawable Folder
        ImageView contactPhoto = (ImageView) findViewById(R.id.contactPhoto);
        contactPhoto.setImageResource(R.drawable.blank);

        // Set EditText Fields
        new_fName = (EditText)findViewById(R.id.new_fname);
        new_lName = (EditText)findViewById(R.id.new_lname);
        new_mobile = (EditText)findViewById(R.id.new_mobile);
        new_home = (EditText)findViewById(R.id.new_home);
        new_email = (EditText)findViewById(R.id.new_email);
        new_address = (EditText)findViewById(R.id.new_address);
        new_city = (EditText)findViewById(R.id.new_city);
        new_state = (EditText)findViewById(R.id.new_state);
        new_zip = (EditText)findViewById(R.id.new_zip);
        new_mobile.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        new_home.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        // On Upload Button Click - Create Intent for Choosing Image from External Source
        // Call ActivityResult to Set Image to ImageView
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_IMAGE_REQUEST);

            }

        });

        // On Button Create Click Get Values from Each EditText and ImageView and Send to Database by Calling createContact()
        // ImageView Image will have to be Converted to byte[] to be Inserted
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Only Continue if Name and Mobile Number are Inserted
                // Making those Fields Required...
                if (new_fName.getText().toString().equals("") || new_lName.getText().toString().equals("") || new_mobile.getText().toString().equals("")) {
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

                    // Set read/write Access
                    ParseACL defaultACL = new ParseACL();
                    defaultACL.setPublicReadAccess(true);
                    defaultACL.setPublicWriteAccess(true);
                    ParseACL.setDefaultACL(defaultACL, true);

                    ParseFile file = new ParseFile("resume.img", data);

                    ParseObject contactData = new ParseObject("contactData");
                    ParseACL postACL = new ParseACL(ParseUser.getCurrentUser());
                    ParseACL.setDefaultACL(defaultACL, true);
                    contactData.setACL(postACL);
                    contactData.put("fName", new_fName.getText().toString());
                    contactData.put("lName", new_lName.getText().toString());
                    contactData.put("mobile", new_mobile.getText().toString());
                    contactData.put("email", new_email.getText().toString());
                    contactData.put("home", new_home.getText().toString());
                    contactData.put("address", new_address.getText().toString());
                    contactData.put("city", new_city.getText().toString());
                    contactData.put("state", new_state.getText().toString());
                    contactData.put("zip", new_zip.getText().toString());
                    contactData.put("image", file);

                    contactData.saveInBackground();
                    file.saveInBackground();

                    sendSuccessMessage(new_fName.getText().toString(), new_lName.getText().toString());

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);
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
        Success Message to User to Let them Know a New Contact has been Created
        Return them to Main
     */
    public void sendSuccessMessage(String first, String last)
    {
        // Send Updated Success Message and Send User Back to Main
        Toast.makeText(this, "Contact Updated!\n" +
                "First Name: " + first + "\n" +
                "Last Name: " + last + "\n"
                , Toast.LENGTH_LONG).show();

        Intent mainIntent = new Intent(CreateActivity.this, MainActivity.class);
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