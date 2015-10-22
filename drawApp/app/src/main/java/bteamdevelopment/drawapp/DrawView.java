package bteamdevelopment.drawapp;

/**
 * Created by wkohusjr on 9/16/2015.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {

    // Declare Variables
    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private int paintColor = 0xff000030;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private float brushSize;
    boolean erase = false;

    public DrawView(Context context, AttributeSet attrs){
        super(context, attrs);
        createDrawing();
    }

    // Setup Drawing
    private void createDrawing(){

        // Prepare for Drawing and Setup Paint Properties
        brushSize = 20;
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    // Handles Size Change to the Screen
    // Android Layout Method Called when the Size of this View has Changed.
    // http://developer.android.com/intl/zh-tw/reference/android/view/View.html
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    // Draw the View
    // Android Drawing Method Called when the View Should Render its Content.
    // http://developer.android.com/intl/zh-tw/reference/android/view/View.html
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    // Android Event Processing Method Called when a Touch Screen Motion Event Occurs.
    // @return True if the Event was Handled, False Otherwise.
    // https://developer.android.com/intl/zh-tw/training/gestures/detector.html
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        //respond to down, move and up events
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        // Re-Draw
        invalidate();
        return true;

    }

    // http://developer.android.com/intl/zh-tw/reference/android/util/TypedValue.html#TypedValue()
    // Set Paint Brush Size based on Passed in Float Value from Spinner
    public void setBrushSize(float newSize){
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize = pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    // Clear Canvas
    // http://developer.android.com/intl/zh-tw/reference/android/graphics/PorterDuff.Mode.html
    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    // Update Color
    // @newColor is the String Value from the Color Buttons
    public void setColor(String newColor){
        invalidate();
        // Receive paint color from MainActivity and Set Color
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
        drawPaint.setShader(null);
    }

    // Set Erase True or False
    // @isErase is Called when the btn_erase is Clicked
    public void setErase(boolean isErase){
        erase=isErase;
        if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);
    }
}