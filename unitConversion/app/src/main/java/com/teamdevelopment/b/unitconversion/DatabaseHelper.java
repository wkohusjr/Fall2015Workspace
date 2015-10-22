package com.teamdevelopment.b.unitconversion;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wkohusjr on 9/14/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name - Lives in the Assets Folder
    private static String DB_NAME = "unitDB";

    // Path on Device
    private static String DB_PATH = "/data/data/com.teamdevelopment.b.unitconversion/databases/";

    private final Context myContext;

    private SQLiteDatabase myDataBase;

    public DatabaseHelper(Context context) {

        // Use Constructor to Call Database Once
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

        // Check Database for Existence
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

    // copyDatabase() gets unitDB file from Assets folder and uses Input and Output Stream
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
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }

    /*
    * getAllData() gives Read/Write Access
    * @tbName = Table Name needing Units
    * @return List<> of Items from Database
    * getAllData() is used to load the spinners and called by each Activity
     */
    public List<String> getAllData(String tbName) {
        List<String> units = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + tbName;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                units.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return units;
    }
}

    /*
        I added this function to count each button click.  It works and stores the value in the database.
        I had hoped to arrange buttons by highest count, but was unable to do so.  Maybe in future development...
//         */
//    public void increaseCount(String tbName) {
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor;
//        String selectQuery = "UPDATE " + tbName + " SET COUNT = COUNT + 1" ;
//        cursor = db.rawQuery(selectQuery, null);
//
//        cursor.close();
//        db.close();
//
//    }
//}
