package bteamdevelopment.contactappparse;

/**
 * Created by wkohusjr on 10/17/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/*
    UpdateActivity Allows the User to Update or Delete the Contact.
    On Update and Delete the User will be sent back to MainActivity.

    This Class uses ParseQuery to Retrieve Data from Parse.com
 */
public class UpdateActivity extends Activity implements View.OnClickListener {
    // Declare Variables
    String fName, lName, mobile, email, home, address, city, state, zip, id;
    private int PICK_IMAGE_REQUEST = 1;
    Button btn_Delete, btn_Upload, btn_Edit;
    EditText editLName, editFName, editMobile, editHome, editEmail, editAddress, editCity, editState, editZip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_update.xml
        setContentView(R.layout.activity_update);

        btn_Delete = (Button) findViewById(R.id.btn_Delete);
        btn_Edit = (Button) findViewById(R.id.btn_Edit);
        btn_Upload = (Button) findViewById(R.id.btn_Upload);
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

        // Get data from selected Contact from the MainActivity
        // This data is loaded in the ListViewAdapter Class
        Intent i = getIntent();
        id = i.getStringExtra("objectId");
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
        editFName = (EditText) findViewById(R.id.editFName);
        editLName = (EditText) findViewById(R.id.editLName);
        editMobile = (EditText) findViewById(R.id.editMobile);
        editHome = (EditText) findViewById(R.id.editHome);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editCity = (EditText) findViewById(R.id.editCity);
        editState = (EditText) findViewById(R.id.editState);
        editZip = (EditText) findViewById(R.id.editZip);
        editMobile.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        editHome.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        // Set results to the TextViews
        editFName.setText(fName);
        editLName.setText(lName);
        editMobile.setText(mobile);
        editHome.setText(home);
        editEmail.setText(email);
        editAddress.setText(address);
        editCity.setText(city);
        editState.setText(state);
        editZip.setText(zip);

        /*
            Locate the Image from Parse.com and Put it into the ImageView
         */
        ParseQuery<ParseObject> query = new ParseQuery<>("contactData");

        // Locate the objectId from the class
        query.getInBackground(id, new GetCallback<ParseObject>() {

                    public void done(ParseObject object, ParseException e) {
                        // TODO Auto-generated method stub

                        // Locate the column named "Image" and set to ImageView
                        ParseFile fileObject = (ParseFile) object.get("image");
                        fileObject.getDataInBackground(new GetDataCallback() {
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                     Log.d("Image Retrieve", "An Image Exists!");
                                            // Decode the Byte[] into bitmap
                                            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                            ImageView image = (ImageView) findViewById(R.id.contactPhoto);

                                            // Set the Bitmap into the ImageView
                                            image.setImageBitmap(bmp);
                                } else {
                                    Log.d("Image Retrieve", "There was a problem downloading the Image.");

                                }
                            }
                        });
                    }
                });

    }

    /*
        OnClick Method from OnClickListener() listens for update/delete button clicks.  When clicked call corresponding methods.
     */
    @Override
    public void onClick(View v) {

        // if Button Click updateContact() and Return User back to Main
        if (v.getId() == R.id.btn_Edit) {
            updateContact();
        }

        // On Button Click deleteContact() and Return User back to Main
        if (v.getId() == R.id.btn_Delete) {
            deleteContact();
        }

    }

    /*
        Query Parse.com Database Table and Delete Object based on ID
        On Success: @Return to MainActivity
     */
    public void deleteContact() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("contactData");
        query.whereEqualTo("objectId", id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() != 0) {
                        final ParseObject contactData = objects.get(0);
                        final String objID = contactData.getObjectId();
                        ParseQuery<ParseObject> newquery = ParseQuery.getQuery("contactData");
                        newquery.getInBackground(objID, new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e == null) {
                                    for (ParseObject delete : objects) {
                                        delete.deleteInBackground();
                                    }

                                }
                            }
                        });
                    }
                }
            }
        });

        // Send Success Message and Send User to MainActivity
        sendSuccessMessage(editFName.getText().toString(), editLName.getText().toString());
    }

    /*
       Using the ID from the Bundle Update the Contact Row in the Database with Data from the EditText Fields and Image
       On Success: @Return to MainActivity
    */
    public void updateContact() {
        // Only Continue if Name and Mobile Number are Inserted Making those Fields Required...
        if (editFName.getText().toString().equals("") || editLName.getText().toString().equals("") || editMobile.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Name and Mobile are Required Fields!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // Query contactData Table on Parse.com where ID = ID to be Updated
            ParseQuery<ParseObject> query = ParseQuery.getQuery("contactData");
            query.whereEqualTo("objectId", id);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        // If there is an object to be updated...
                        if (objects.size() != 0) {
                            // Get Object
                            final ParseObject contactData = objects.get(0);
                            // Convert ID to String
                            final String objID = contactData.getObjectId();
                            // Query ID and put Data on Parse.com
                            ParseQuery<ParseObject> newquery = ParseQuery.getQuery("contactData");
                            newquery.getInBackground(objID, new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {
                                    if (e == null) {
                                        contactData.put("fName", editFName.getText().toString());
                                        contactData.put("lName", editLName.getText().toString());
                                        contactData.put("mobile", editMobile.getText().toString());
                                        contactData.put("email", editEmail.getText().toString());
                                        contactData.put("home", editHome.getText().toString());
                                        contactData.put("address", editAddress.getText().toString());
                                        contactData.put("city", editCity.getText().toString());
                                        contactData.put("state", editState.getText().toString());
                                        contactData.put("zip", editZip.getText().toString());

                                        // Set ImageView
                                        ImageView view = (ImageView) findViewById(R.id.contactPhoto);

                                        // Enable and Build Drawing Cache
                                        view.setDrawingCacheEnabled(true);
                                        view.buildDrawingCache();

                                        // Convert Image to byte[]
                                        Bitmap bitmap = view.getDrawingCache();
                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                        byte[] data = stream.toByteArray();

                                        // Set Converted Image as ParseFile and put on Parse.com
                                        ParseFile file = new ParseFile("contact.img", data);
                                        contactData.put("image", file);

                                        // Save object and file to db
                                        file.saveInBackground();
                                        object.saveInBackground();
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }

        // Send success message and return user to MainActivity
        sendSuccessMessage(editFName.getText().toString(), editLName.getText().toString());

    }

    /*
    Success Message to User to Let them Know a Contact has been Updated
    Return them to Main
 */
    public void sendSuccessMessage(String first, String last) {
        // Send Updated Success Message and Send User Back to Main
        Toast.makeText(this, "Contact Updated!\n" +
                "First Name: " + first + "\n" +
                "Last Name: " + last + "\n"
                , Toast.LENGTH_LONG).show();

        // Return to Main
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