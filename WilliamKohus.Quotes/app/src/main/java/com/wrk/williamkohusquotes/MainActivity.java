package com.wrk.williamkohusquotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //onCreate request random quote from getQuote()
    //use findViewById to set quote to TextView
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        TextView tq = (TextView) findViewById(R.id.quote);
        //set value of quote to return value of getQuote()
        tq.setText(getQuote());

    }

    /**
     * @des open buffered data from quotes.txt and loop through @lines placing data in an ArrayList.  Using Random get an @index of random quote and return it.
     * @param //BufferedReader br - Wrap input from quotes.txt with a Buffer
     * @param //ArrayList quoteList - An array list of quotes from quotes.txt
     * @param //String line - line of data from @br
     * @param //Random random - pseudo-random value used to pick random line from ArrayList
     * @param //String quote - quote to be returned to calling function
     * @return quote to calling function
     */
    private String getQuote() {
        /* Declare variables */
        BufferedReader br;
        ArrayList<String> quoteList = new ArrayList<>();
        String line;
        Random random = new Random();
        String quote = null;

        try {
            //use BufferedReader to access quotes.txt from assets folder
            br = new BufferedReader(new InputStreamReader(getAssets().open("quotes.txt")));
            //while line is not null - add line to quoteList Array
            while((line = br.readLine()) != null)
                //add line to quoteList
                quoteList.add(line);
            //assign random integer to index to get random line from ArrayList
            int index = random.nextInt(quoteList.size());
            //assign quote to random line
            quote = quoteList.get(index);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //return quote
        return quote;

    }

    /**
     * function used when button in activity_main is clicked.  Call getQuote() method and set new random quote on button click.
     * @return void
     */
    public void buttonClick (View v)
    {
        //instantiate TextView and find the quote TextView
        TextView tq = (TextView)findViewById(R.id.quote);
        //set text to return value of function getQuote()
        tq.setText(getQuote());
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
