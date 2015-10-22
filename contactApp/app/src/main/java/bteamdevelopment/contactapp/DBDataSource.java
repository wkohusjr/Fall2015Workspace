package bteamdevelopment.contactapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLException;
import android.content.ContentValues;
import android.database.Cursor;
import static bteamdevelopment.contactapp.DatabaseHelper.SQLITE_TABLE;

/**
 * Created by wkohusjr on 9/24/2015.
 */
public class DBDataSource {

    public DatabaseHelper dbHelper;

    private SQLiteDatabase db;

    // Array of Columns
    private String[] allColumns = {DatabaseHelper.KEY_ROWID, DatabaseHelper.KEY_FNAME, DatabaseHelper.KEY_LNAME, DatabaseHelper.KEY_MOBILE, DatabaseHelper.KEY_HOME, DatabaseHelper.KEY_EMAIL, DatabaseHelper.KEY_ADDRESS, DatabaseHelper.KEY_CITY, DatabaseHelper.KEY_STATE, DatabaseHelper.KEY_ZIP, DatabaseHelper.KEY_IMAGE};

    // Constructor
    public DBDataSource(Context context)
    {
        dbHelper = new DatabaseHelper(context);
    }

    // Function to Open Database
    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close()
    {
        dbHelper.close();
    }

    /* createContact Inserts a Contact into the Database from the String and Image Variables Passed in
      The Passed in Data will be Added Using ContentValues
      Using Cursor Run a SQL Query to Add Data to Database
      Returns the Created Contact
  */
    public Contact createContact(String fName, String lName, String mobile, String home, String email, String address, String city, String state, String zip, byte[] image)
    {
        // Create New ContactValues and Put Data
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_FNAME, fName);
        values.put(DatabaseHelper.KEY_LNAME, lName);
        values.put(DatabaseHelper.KEY_MOBILE, mobile);
        values.put(DatabaseHelper.KEY_HOME, home);
        values.put(DatabaseHelper.KEY_EMAIL, email);
        values.put(DatabaseHelper.KEY_ADDRESS, address);
        values.put(DatabaseHelper.KEY_CITY, city);
        values.put(DatabaseHelper.KEY_STATE, state);
        values.put(DatabaseHelper.KEY_ZIP, zip);
        values.put(DatabaseHelper.KEY_IMAGE, image);

        long insertId = db.insert(SQLITE_TABLE, null, values);

        // Insert Data using Query
        Cursor cursor = db.query(SQLITE_TABLE,
                allColumns, DatabaseHelper.KEY_ROWID + " = " + insertId, null, null, null, null);

        // Move Cursor to Contact
        Contact newContact = cursorToContact(cursor);

        cursor.close();

        return newContact;
    }

    // Move Cursor to Passed in Cursor
    // Used to get Contact Data from Cursor
    private Contact cursorToContact(Cursor cursor)
    {
        // Instantiate Contact
        Contact contact = new Contact();

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                contact.setId(cursor.getInt(0));
                contact.setfName(cursor.getString(1));
                contact.setlName(cursor.getString(2));
                contact.setMobile(cursor.getString(3));
                contact.setHome(cursor.getString(4));
                contact.setEmail(cursor.getString(5));
                contact.setAddress(cursor.getString(6));
                contact.setCity(cursor.getString(7));
                contact.setState(cursor.getString(8));
                contact.setZip(cursor.getString(9));
                contact.setImage(cursor.getBlob(10));
                cursor.moveToNext();
            }
        }

        return contact;
    }

    // Get Contact with Passed in ID.  Select All Columns with Matching ID
    public Contact getContact(long id)
    {
        Contact contact;

        Cursor cursor = db.query(SQLITE_TABLE, allColumns, "_id =" + id, null, null, null, null);

        // Place Cursor at First
        cursor.moveToFirst();

        // Set Contact to Cursor Value
        contact = cursorToContact(cursor);
        cursor.close();

        return contact;
    }

    // Pass in Values from EditText Fields
    // Update Contact Row Based on ID using ContentValues and Cursor
    public Contact updatedContact(String fName, String lName, String mobile, String home, String email, String address, String city, String state, String zip, byte[] image, long id)
    {
        // Use Passed in ID to Filter Row
        String idFilter = "_id=" + id;

        // Put Values into ContentValues
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_FNAME, fName);
        values.put(DatabaseHelper.KEY_LNAME, lName);
        values.put(DatabaseHelper.KEY_MOBILE, mobile);
        values.put(DatabaseHelper.KEY_HOME, home);
        values.put(DatabaseHelper.KEY_EMAIL, email);
        values.put(DatabaseHelper.KEY_ADDRESS, address);
        values.put(DatabaseHelper.KEY_CITY, city);
        values.put(DatabaseHelper.KEY_STATE, state);
        values.put(DatabaseHelper.KEY_ZIP, zip);
        values.put(DatabaseHelper.KEY_IMAGE, image);

        db.update(SQLITE_TABLE, values, idFilter, null);

        Cursor cursor = db.query(SQLITE_TABLE,
                allColumns, DatabaseHelper.KEY_ROWID + " = " + id, null, null, null, null);

        Contact newContact = cursorToContact(cursor);

        cursor.close();

        return newContact;
    }

    public void deleteContact(long id)
    {
        String strFilter = "_id=" + id;
        db.delete(SQLITE_TABLE, strFilter, null);
    }

}