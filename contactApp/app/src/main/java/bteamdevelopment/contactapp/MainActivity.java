package bteamdevelopment.contactapp;

        import android.content.Intent;
        import android.database.Cursor;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ImageButton;
        import android.widget.ListView;
        import android.widget.SimpleCursorAdapter;

        import java.io.IOException;
        import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    // Declare Variables
    DatabaseHelper contactInfo;
    SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Call constructor to Create Database
        contactInfo = new DatabaseHelper(this);

        // Call constructor to Create Database
        contactInfo = new DatabaseHelper(this);
        try {
            contactInfo.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            contactInfo.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }


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

        // Call to Method that will Populate ListView
        displayListView();
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

    // Display Items from Database in ListView
    // Data will be Alphabetical Order Due to SQL Statement ASC
    private void displayListView()
    {
        // Instantiation of cursor to All Contacts in Database
        Cursor cursor = contactInfo.getAllContacts();

        // Create String Array of Selected Data
        // Only want to display a few items - More Details will be Displayed in Detail View
        String [] columns = new String[] {DatabaseHelper.KEY_ROWID, DatabaseHelper.KEY_FNAME, DatabaseHelper.KEY_LNAME,
                                          DatabaseHelper.KEY_EMAIL, DatabaseHelper.KEY_MOBILE
        };

        // Create int Array of Corresponding IDs
        int[] to = new int [] {
                R.id.contactID, R.id.fName, R.id.lName, R.id.email, R.id.mobile
        };

        // Declare SimpleCursorAdapter and Add Items
        dataAdapter = new SimpleCursorAdapter(this, R.layout.contact_list, cursor, columns, to, 0);

        // ListView of All Contacts
        ListView myContacts = (ListView)findViewById(R.id.myContacts);

        // Connect Contacts to Adapter
        myContacts.setAdapter(dataAdapter);

        // Set OnClickListener for ListView
        myContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {

                // Get the Cursor Positioned to the Corresponding Row
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                Long contactID = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));

                // Bundle the ID and Send it to the Next Intent
                Bundle bundle = new Bundle();
                bundle.putLong("User ID", contactID);

                // Send to Intent with Bundled ID
                Intent contactIntent = new Intent(MainActivity.this, UpdateActivity.class);
                contactIntent.putExtras(bundle);
                startActivity(contactIntent);

            }
        });

    }

}