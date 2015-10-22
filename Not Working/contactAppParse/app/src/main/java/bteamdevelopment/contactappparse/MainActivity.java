package bteamdevelopment.contactappparse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    ProgressDialog mProgressDialog;
    ListViewAdapter adapter;
    private List<Contact> contactList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();

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

    }

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(MainActivity.this);

            mProgressDialog.setTitle("Please Wait While Your Contacts are Loaded");

            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);

            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            contactList = new ArrayList<Contact>();
            try {

                ParseQuery<ParseObject> query = new ParseQuery<>(
                        "contactData");

                ob = query.find();
                for (ParseObject contactData : ob) {

                    Contact contact = new Contact();
                    contact.setfName((String) contactData.get("fName"));
                    contact.setlName((String) contactData.get("lName"));
                    contact.setMobile((String) contactData.get("mobile"));
                    contact.setEmail((String) contactData.get("email"));
                    contact.setHome((String) contactData.get("home"));
                    contact.setAddress((String) contactData.get("address"));
                    contact.setCity((String) contactData.get("city"));
                    contact.setState((String) contactData.get("state"));
                    contact.setZip((String) contactData.get("zip"));

                    contactList.add(contact);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            listview = (ListView) findViewById(R.id.myContacts);

            adapter = new ListViewAdapter(MainActivity.this,
                    contactList);

            listview.setAdapter(adapter);

            mProgressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

}

