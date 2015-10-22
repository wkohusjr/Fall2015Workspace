package bteamdevelopment.contactapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by wkohusjr on 9/14/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name - Lives in the Assets Folder
    public static String DB_NAME = "contactInfo";
    public static final String SQLITE_TABLE = "user_data";
    private static final String TAG = "DatabaseHelper";

    public static final String KEY_ROWID = "_id";
    public static final String KEY_FNAME = "f_name";
    public static final String KEY_LNAME = "l_name";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_HOME = "home";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CITY = "city";
    public static final String KEY_STATE = "state";
    public static final String KEY_ZIP = "zip";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_IMAGE = "image";

    // Path on Device
    private static String DB_PATH = "/data/data/bteamdevelopment.contactapp/databases/";

    private final Context myContext;

    private SQLiteDatabase db;

    // Constructor
    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /*
        Attempt to create database
            if - exists, do nothing
            else - call copyDatabase()
            catch errors if something goes wrong
     */
    public void createDatabase() throws IOException {
        boolean dbExist = checkDatabase();

        if (dbExist) {

        }
        else {
            this.getReadableDatabase();

            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error Copying Database");
            }
        }
    }

    // checkDatabase() takes the path and name and opens the path.
    // If database is created and not null - close it.
    private boolean checkDatabase ()
    {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath,null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {

        }
        if (checkDB != null)
        {
            checkDB.close();
        }
        return checkDB != null;
    }

    // copyDatabase() gets unitDB file from Assets folder and uses Input and Output Stream to write Database
    private void copyDatabase() throws IOException{

        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        // Open the db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // Transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    // Open SQLite database with Path.  This is not used at this current time.
    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(db != null)
            db.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }

    // Method to Get All Contacts from the Database using Cursor and Return them in Alphabetical Order by Last Name
    public Cursor getAllContacts()
    {
        Cursor mCursor = db.query(SQLITE_TABLE, new String[] {
                KEY_ROWID, KEY_FNAME, KEY_LNAME, KEY_MOBILE, KEY_HOME, KEY_ADDRESS, KEY_CITY, KEY_STATE,
                KEY_ZIP, KEY_EMAIL, KEY_IMAGE}, null, null, null, null,  KEY_LNAME + " ASC");
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}