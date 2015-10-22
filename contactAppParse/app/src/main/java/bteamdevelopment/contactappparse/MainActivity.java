package bteamdevelopment.contactappparse;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Declare Variables
    ListView listview;
    List<ParseObject> ob;
    ListViewAdapter adapter;
    private List<Contact> contactList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Declare ImageButton to Create New Contact
        ImageButton addNewBtn = (ImageButton) findViewById(R.id.addNew);

        // Set OnClickListener to Go To New Intent (CreateActivity)
        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createIntent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(createIntent);
            }
        });

        new RemoteDataTask().execute();

    }

    // On Resume Reload ListView (NOT WORKING)
    @Override
    public void onResume(){
        super.onResume();
        new RemoteDataTask().execute();
    }

    // RemoteDataTask AsyncTask
    public class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            // Create New Array List of Objects from Parse.com
            contactList = new ArrayList<>();
            try {
                // Query contactData Table on Parse.com
                ParseQuery<ParseObject> query = new ParseQuery<>("contactData");
                ob = query.find();
                // For each object in contactData table
                for (ParseObject contactData : ob) {

                    // Create New contact for Each Contact in contactData
                    Contact contact = new Contact();
                    contact.setId(contactData.getObjectId());
                    contact.setfName((String) contactData.get("fName"));
                    contact.setlName((String) contactData.get("lName"));
                    contact.setMobile((String) contactData.get("mobile"));
                    contact.setEmail((String) contactData.get("email"));
                    contact.setHome((String) contactData.get("home"));
                    contact.setAddress((String) contactData.get("address"));
                    contact.setCity((String) contactData.get("city"));
                    contact.setState((String) contactData.get("state"));
                    contact.setZip((String) contactData.get("zip"));

                    // Add Objects to contactList ArrayList
                    contactList.add(contact);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Void result) {

            listview = (ListView) findViewById(R.id.myContacts);

            adapter = new ListViewAdapter(MainActivity.this,
                    contactList);

            listview.setAdapter(adapter);
            listview.deferNotifyDataSetChanged();
        }
    }
}
