package bteamdevelopment.drawapp;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Declare drawView
    DrawView drawView;

    // Declare ImageButtons
    ImageButton saveImage, newImage, changeBackground, changeColor, eraseBtn, openBtn;

    // Set String Array of Spinner Descriptions
    // Want just the Images to Show up so Leave Blank
    String[] brushSizes = {"Change Brush Size...", "", "", "", "", ""};

    // For Opening Image from Gallery
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable OnSelectedListener for Selected Spinner option and set Brush Size Accordingly
        setBrushSizeListener();

        // Get Draw View
        drawView = (DrawView)findViewById(R.id.DrawViewCanvas);

        // Declare and Set Listener for changeColor ImageButton
        changeColor = (ImageButton)findViewById(R.id.change_color);
        changeColor.setOnClickListener(this);

        // Declare and Set Listener for changeBackground ImageButton
        changeBackground = (ImageButton)findViewById(R.id.change_background);
        changeBackground.setOnClickListener(this);

        // Declare and Set Listener for Save ImageButton
        saveImage = (ImageButton)findViewById(R.id.btn_save);
        saveImage.setOnClickListener(this);

        // Declare and Set Listener for New ImageButton
        newImage = (ImageButton)findViewById(R.id.btn_new);
        newImage.setOnClickListener(this);

        // Declare and Set Listener for Erase ImageButton
        eraseBtn = (ImageButton)findViewById(R.id.btn_erase);
        eraseBtn.setOnClickListener(this);

        // Create Spinner in onCreate and Load it with Values
        // Declaring and Typecasting a Spinner
        Spinner brushSpinner = (Spinner) findViewById(R.id.brushSizes);
        // Setting a Custom Adapter to the Spinner
        brushSpinner.setAdapter(new SpinnerAdapter(MainActivity.this, R.layout.brush_spinner,
                brushSizes));

        // Prevents the Double Click Issue with ImageButton
        eraseBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.performClick();
                }
            }
        });

        // Declare Open Button
        openBtn = (ImageButton)findViewById(R.id.btn_open);
        // Set OnClickListener for Upload Photo Button
        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_IMAGE_REQUEST);

            }
        });
    }

    @Override
    public void onClick(View v) {

        /*
            When change_color ImageButton is clicked, set color to chosen color.
            Create dialog with color images to choose from.
         */
        if(v.getId()==R.id.change_color){
            final Dialog colorDialog = new Dialog(this);
            colorDialog.setTitle("Change Color");

            // Set Layout as color_chooser
            colorDialog.setContentView(R.layout.color_chooser);

            final ImageButton blue = (ImageButton)colorDialog.findViewById(R.id.blue);
            setColorListeners(blue, colorDialog);

            final ImageButton red = (ImageButton)colorDialog.findViewById(R.id.red);
            setColorListeners(red, colorDialog);

            final ImageButton babyblue = (ImageButton)colorDialog.findViewById(R.id.babyblue);
            setColorListeners(babyblue, colorDialog);

            final ImageButton black = (ImageButton)colorDialog.findViewById(R.id.black);
            setColorListeners(black, colorDialog);

            final ImageButton green = (ImageButton)colorDialog.findViewById(R.id.green);
            setColorListeners(green, colorDialog);

            final ImageButton lime = (ImageButton)colorDialog.findViewById(R.id.lime);
            setColorListeners(lime, colorDialog);

            final ImageButton navy = (ImageButton)colorDialog.findViewById(R.id.navy);
            setColorListeners(navy, colorDialog);

            final ImageButton orange = (ImageButton)colorDialog.findViewById(R.id.orange);
            setColorListeners(orange, colorDialog);

            final ImageButton pink = (ImageButton)colorDialog.findViewById(R.id.pink);
            setColorListeners(pink, colorDialog);

            final ImageButton yellow = (ImageButton)colorDialog.findViewById(R.id.yellow);
            setColorListeners(yellow, colorDialog);

            final ImageButton teal = (ImageButton)colorDialog.findViewById(R.id.teal);
            setColorListeners(teal, colorDialog);

            final ImageButton purple = (ImageButton)colorDialog.findViewById(R.id.purple);
            setColorListeners(purple, colorDialog);

            colorDialog.show();

        }

        /*
            If erase button is clicked set erase to true.
            setErase() is called and sets drawPaint to PorterDuff.Mode.CLEAR
         */
        else if (v.getId() == R.id.btn_erase)
        {
            ImageButton ib = (ImageButton)findViewById(R.id.btn_erase);
            ib.setOnClickListener(new View.OnClickListener() {

                public void onClick(View button) {
                    //Set the button's appearance
                    button.setSelected(!button.isSelected());

                    if (button.isSelected()) {
                        drawView.setErase(true);
                        eraseBtn.setImageResource(R.drawable.brush);
                    } else {
                        drawView.setErase(false);
                        eraseBtn.setImageResource(R.drawable.eraser);
                    }

                }

            });
        }

        /*
            When change_background ImageButton is clicked, set background image to chosen background image.
            Create dialog with background images to choose from.
         */
        else if (v.getId()==R.id.change_background){

            final Dialog backgroundDialog = new Dialog(this);
            backgroundDialog.setTitle("Change Background");
            backgroundDialog.setContentView(R.layout.background_chooser);

            // Set Listeners for pictures
            final ImageButton dog = (ImageButton)backgroundDialog.findViewById(R.id.dog);
            Drawable dogDraw = getResources().getDrawable(R.drawable.dog);
            setBackgroundListeners(dog, dogDraw, backgroundDialog);

            final ImageButton bird = (ImageButton)backgroundDialog.findViewById(R.id.bird);
            Drawable birdDraw = getResources().getDrawable(R.drawable.bird);
            setBackgroundListeners(bird, birdDraw, backgroundDialog);

            final ImageButton cupcake = (ImageButton)backgroundDialog.findViewById(R.id.cupcake);
            Drawable cupcakeDraw = getResources().getDrawable(R.drawable.cupcake);
            setBackgroundListeners(cupcake, cupcakeDraw, backgroundDialog);

            final ImageButton dog2 = (ImageButton)backgroundDialog.findViewById(R.id.dog2);
            Drawable dog2Draw = getResources().getDrawable(R.drawable.dog2);
            setBackgroundListeners(dog2, dog2Draw, backgroundDialog);

            final ImageButton giraffe = (ImageButton)backgroundDialog.findViewById(R.id.giraffe);
            Drawable giraffeDraw = getResources().getDrawable(R.drawable.giraffe);
            setBackgroundListeners(giraffe, giraffeDraw, backgroundDialog);

            final ImageButton mush = (ImageButton)backgroundDialog.findViewById(R.id.mush);
            Drawable mushDraw = getResources().getDrawable(R.drawable.mush);
            setBackgroundListeners(mush, mushDraw, backgroundDialog);

            final ImageButton rabbit = (ImageButton)backgroundDialog.findViewById(R.id.rabbit);
            Drawable rabbitDraw = getResources().getDrawable(R.drawable.rabbit);
            setBackgroundListeners(rabbit, rabbitDraw, backgroundDialog);

            final ImageButton star = (ImageButton)backgroundDialog.findViewById(R.id.star);
            Drawable starDraw = getResources().getDrawable(R.drawable.star);
            setBackgroundListeners(star, starDraw, backgroundDialog);

            final ImageButton unicorn = (ImageButton)backgroundDialog.findViewById(R.id.unicorn);
            Drawable unicornDraw = getResources().getDrawable(R.drawable.unicorn);
            setBackgroundListeners(unicorn, unicornDraw, backgroundDialog);

            final ImageButton blank = (ImageButton)backgroundDialog.findViewById(R.id.blank);
            Drawable blankDraw = getResources().getDrawable(R.drawable.blank);
            setBackgroundListeners(blank, blankDraw, backgroundDialog);

            final ImageButton cow = (ImageButton)backgroundDialog.findViewById(R.id.cow);
            Drawable cowDraw = getResources().getDrawable(R.drawable.cow);
            setBackgroundListeners(cow, cowDraw, backgroundDialog);

            final ImageButton turkey = (ImageButton)backgroundDialog.findViewById(R.id.turkey);
            Drawable turkeyDraw = getResources().getDrawable(R.drawable.turkey);
            setBackgroundListeners(turkey, turkeyDraw, backgroundDialog);

            backgroundDialog.show();

        }

        /*
            Create Dialog Box with Warning.
            Yes - Clear Paint
            No - Cancel and Continue Drawing
         */
        else if(v.getId()==R.id.btn_new){
            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("Clear Canvas");
            newDialog.setMessage("Are you sure you want to clear the canvas?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    drawView.startNew();

                    // Set Background to White
                    drawView.setBackgroundColor(0xffffffff);

                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            newDialog.show();
        }

        else if(v.getId()== R.id.btn_open){

        }

        /*
            Create Dialog Box with Warning.
            Yes - Check External Memory Device for /DCIM/Camera Folder
                    - if Exist - Save Drawing to /DCIM/Camera Folder
                    - if not Create Folder and Save Image
            No - Cancel and Continue Drawing
         */
        else if(v.getId()== R.id.btn_save){
            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save Drawing to Device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //save drawing
                    drawView.setDrawingCacheEnabled(true);

                    final Bitmap screenshot = Bitmap.createBitmap(drawView.getDrawingCache());

                    // Find Folder
                    // If Doesn't Exist - Create Directory
                    File folder = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
                    if(!folder.exists()){
                        folder.mkdir();
                    }

                    // FileName and Get Existing Path
                    String existingFileName=Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";

                    // Create FileOutputStream
                    FileOutputStream fos;
                    try {

                        // Create Time Stamp to Add to Image to Prevent Overwriting File
                        String timeStamp = getCurrentDateAndTime();

                        // Add to FileOutputStream
                        fos = new FileOutputStream(existingFileName + "/" + timeStamp + ".jpg");
                        screenshot.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                        fos.flush();
                        fos.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Display Result to User on Success or Fail
                    if(screenshot!=null){
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Drawing has been Saved to Image Gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    }
                    else{
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Error: Image Could Not Be Saved!", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }

                    drawView.destroyDrawingCache();
                }
            });

            // Cancel Save
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }

    }

    /*
        setColorListeners sets onClick Listeners for Each Color Button.
        If Clicked, get the tag from the ImageButton and Change Paintbrush Color.
     */
    public void setColorListeners(ImageButton ib, final Dialog dia)
    {
        ib.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                // Set Color
                drawView.setColor(v.getTag().toString());
                dia.dismiss();
            }

        });
    }
    /*
         setBackgroundListeners sets onClick Listeners for Each Background Button.
           If Clicked Set Background Image to Image and Clear Canvas.
     */
    public void setBackgroundListeners(ImageButton ib, final Drawable d, final Dialog dia)
    {
        ib.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                // Set Background
                drawView.setBackground(d);

                // Clear Canvas
                drawView.startNew();
                dia.dismiss();
            }

        });
    }

    /*
        setBrushSizeListener Creates a New Adapter with the Spinner Values and Creates onClicklisteners for Each Option
         If Position 1 (xsmall brush) set to 8
         If Position 2 (small brush) set to 15
         If Position 3 (medium brush) set to 20
         If Position 4 (large brush) set to 25
         If Position 5 (xlarge brush) set to 35
     */
    public void setBrushSizeListener()
    {
        Spinner s1 = (Spinner) findViewById(R.id.brushSizes);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.brushSizes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);
        s1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {

                        switch (position) {
                            case 1:
                                drawView.setBrushSize(8);
                                break;
                            case 2:
                                drawView.setBrushSize(15);
                                break;
                            case 3:
                                drawView.setBrushSize(20);
                                break;
                            case 4:
                                drawView.setBrushSize(25);
                                break;
                            case 5:
                                drawView.setBrushSize(35);
                                break;
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
    }

    // Create Time and Date Stamp for Image Name
    // Prevents Overwriting Images
    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    // Display Results Obtained from the Application Behavior
    // When the Upload Button is Clicked Open MediaStore Images and Place Selected Image into ImageView
    // http://codetheory.in/android-pick-select-image-from-gallery-with-intents/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get Data
            Uri selectedImage = data.getData();

            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                // Set Background

                Drawable myPhoto = new BitmapDrawable(getResources(), photo);

                drawView.setBackground(myPhoto);

                // Clear Canvas
                drawView.startNew();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}