package bteamdevelopment.drawapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by wkohusjr on 9/18/2015.
 */

// Creating an Adapter Class
public class SpinnerAdapter extends ArrayAdapter<Object> {

    private Context context;

    // String Array for Spinner.
    // Could Add Brush Size Descriptions if Wanted
    String[] brushText = { "Change Brush Size...", "", "", "", "", ""};

    // Create an Array of Brush Images
    Integer brushes [] = { 0, R.drawable.xsmall_circle, R.drawable.small_circle, R.drawable.medium_circle, R.drawable.large_circle, R.drawable.xlarge_circle };

    public SpinnerAdapter(Context context, int textViewResourceId,
                     String[] objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {

        // Inflate the View Using layout_inflater_service
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        // Inflating the Layout for the Custom Spinner
        View brushLayout = inflater.inflate(R.layout.brush_spinner, parent, false);

        // Declaring and Typecasting the Textview in the Inflated Layout
        // The TextView is Located in brush_spinner.xml
        TextView setBrush = (TextView) brushLayout.findViewById(R.id.setBrush);

        // Setting the Text using the Array
        // Load Spinner with brushText Array
        setBrush.setText(brushText[position]);

        // Declaring and Typecasting the ImageView in the Inflated Layout
        ImageView xsmallbrush = (ImageView) brushLayout.findViewById(R.id.xSmallBrush);
        ImageView small = (ImageView) brushLayout.findViewById(R.id.smallBrush);
        ImageView medium = (ImageView) brushLayout.findViewById(R.id.mediumBrush);
        ImageView large = (ImageView) brushLayout.findViewById(R.id.largeBrush);
        ImageView xlarge = (ImageView) brushLayout.findViewById(R.id.xLargeBrush);


        // Setting an Image using the ID's in the Array
        xsmallbrush.setImageResource(brushes[position]);
        small.setImageResource(brushes[position]);
        medium.setImageResource(brushes[position]);
        large.setImageResource(brushes[position]);
        xlarge.setImageResource(brushes[position]);

        // If Position is Set to 0 ("Change Brush Size...")
        if (position == 0) {

            // Remove the Brushes if they aren't Selected
            xsmallbrush.setVisibility(View.GONE);
            small.setVisibility(View.GONE);
            medium.setVisibility(View.GONE);
            large.setVisibility(View.GONE);
            xlarge.setVisibility(View.GONE);

        }

        // Set Color of Spinner Text to Lime Green
        setBrush.setTextColor(Color.rgb(37, 255, 0));

        return brushLayout;
    }


    // It gets a View that Displays in the Drop Down Pop-up
    // The Data at the Specified Position
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // It gets a View that Displays the Data at the Specified Position
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


}