package com.gejiahui.wavecircleprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;

/**
 * Created by gejiahui on 2015/12/15.
 */
public class WaveCircleProgressBar extends ProgressBar {
    private final int default_reached_text_color = Color.rgb(0, 128, 255);
    private final int default_unreached_text_color = Color.rgb(204, 204, 204);
    private final int default_wave_color = Color.rgb(126, 205, 241);
    private final int default_circle_color = Color.rgb(238,238,238);
    private final int default_outline_circle_unreached_color = Color.rgb(204, 204, 204);
    private final int default_outline_circle_reached_color = Color.rgb(0, 128, 255);
    private final float default_circle_radius ;
    private final float default_text_size ;
    private final int default_wave_height = 10;
    private final int default_wave_speed = 15;
    /**
     * used to draw the unreached part of the outline circle
     */
    private Paint mOutlineCircleUnreachedPaint;
    /**
     * used to draw the reached part of the outline circle
     */
    private Paint mOutlineCircleReachedPaint;
    /**
     * used to draw the wave
     */
    private Paint mWavePaint;
    /**
     * used to draw the circle
     */
    private Paint mCirclePaint;
    /**
     * used to draw the text on the mBackBitmap
     */
    private TextPaint mUnreachedTextPaint;
    /**
     * used to draw the text on the mFrontBitmap
     */
    private TextPaint mReachedTextPaint;
    /**
     * used to draw the mFrontBitmap on the mBackBitmap
     */
    private Paint mBitmapPaint;
    /**
     * used to clear the bitmap
     */
    private Paint mClearPaint;
    /**
     * draw wave line
     */
    private Path mPath;

    /**
     * bitmap on the front
     */
    private Bitmap mFrontBitmap;
    /**
     * bitmap on the background
     */
    private Bitmap mBackBitmap;
    /**
     * used to draw on the mFrontBitmap
     */
    private Canvas mFrontCanvas;
    /**
     * used to draw on the mBackBitmap
     */
    private Canvas mBackCanvas;
    /**
     * view's width
     */
    private int mWidth;
    /**
     * view's height
     */
    private int mHeight;
    /**
     * the point to control the wave
     */
    private  int mControlX ,mControlY ;
    /**
     * text on the center of the circle
     */
    private String mText = "";
    /**
     * wave's color
     */
    private int mWaveColor ;
    /**
     * circle's color
     */
    private int mCircleColor;
    /**
     * the color of the text under the wave
     */
    private int mReachedTextColor;
    /**
     * the color of the text on the wave
     */
    private int mUnreachedTextColor;
    /**
     * the color of the unreached part of the outline circle
     */
    private int mOutlineCircleUnreachedColor;
    /**
     * the color of the reached part of the outline circle
     */
    private int mOutlineCircleReachedColor;

    /**
     * circle's radius
     */
    private float mCircleRadius;
    /**
     *  text's size
     */
    private float mTextSize;
    /**
     * the height of the wave
     */
    private int mWaveHeight ;
    /**
     * the speed of the wave
     */
    private int mWaveSpeed ;

    /**
     * used to update the data to make wave more vivid
     */
    boolean ifXAdd = true,ifYAdd = false,ifStop = false;

    Handler updateHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            if(mControlX<mWidth){
                mControlX+=mWaveSpeed;
            }else {
                mControlX = mControlX + mWaveSpeed -mWidth;
            }

            if(mControlY >mWaveHeight ){
                ifYAdd =false ;
            }
            if(mControlY <mWaveHeight/2 ){
                ifYAdd =true;
            }

            mControlY = ifYAdd ? mControlY + 1 : mControlY -1;

            invalidate();


            updateHandler.sendEmptyMessageDelayed(0, 100);


        }
    };
    public WaveCircleProgressBar(Context context) {
        this(context, null );
    }

    public WaveCircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveCircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_circle_radius = dp2px(40);
        default_text_size = sp2px(15);
        //load styled attributes.
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WaveCircleProgressBar,
                defStyleAttr, 0);
        mWaveColor = attributes.getColor(R.styleable.WaveCircleProgressBar_wave_color,default_wave_color);
        mCircleColor = attributes.getColor(R.styleable.WaveCircleProgressBar_circle_color,default_circle_color);
        mReachedTextColor = attributes.getColor(R.styleable.WaveCircleProgressBar_reached_text_color,default_reached_text_color);
        mUnreachedTextColor = attributes.getColor(R.styleable.WaveCircleProgressBar_unreached_text_color, default_unreached_text_color);
        mCircleRadius = attributes.getDimension(R.styleable.WaveCircleProgressBar_circle_radius, default_circle_radius);
        mTextSize = attributes.getDimension(R.styleable.WaveCircleProgressBar_text_size, default_text_size);
        mOutlineCircleReachedColor = attributes.getColor(R.styleable.WaveCircleProgressBar_outline_circle_reached_color, default_outline_circle_reached_color);
        mOutlineCircleUnreachedColor = attributes.getColor(R.styleable.WaveCircleProgressBar_outline_circle_unreached_color, default_outline_circle_unreached_color);
        mWaveHeight = attributes.getInt(R.styleable.WaveCircleProgressBar_wave_height, default_wave_height);
        mWaveSpeed = attributes.getInt(R.styleable.WaveCircleProgressBar_wave_speed,default_wave_speed);

        mControlX = 0;
        mControlY = 5;
        attributes.recycle();
        initializePaints();
        updateHandler.sendEmptyMessageDelayed(0,100);
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }

    private int measure(int measureSpec , boolean isWidth)
    {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingBottom() + getPaddingTop();
        if( mode == MeasureSpec.EXACTLY){
            result = size;
        }else {
            result = padding + 2 * (int)mCircleRadius +10;
            if(mode == MeasureSpec.AT_MOST){
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
        Log.v("gjh",mWidth+":"+mHeight);



        mFrontBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mBackBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mFrontCanvas = new Canvas(mFrontBitmap);
        mBackCanvas = new Canvas(mBackBitmap);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        //clear two canvas
        mFrontCanvas.drawPaint(mClearPaint);
        mBackCanvas.drawPaint(mClearPaint);

        mText = getProgress()+"%";
        float textXPosition = mCircleRadius - mUnreachedTextPaint.measureText(mText)/2+5;
        float textYPosition = mCircleRadius - (mUnreachedTextPaint.descent()+mUnreachedTextPaint.ascent())/2+5;

        //draw a circle and progress on the  mFrontCanvas
        mFrontCanvas.translate(getPaddingLeft(), getPaddingTop());
        mFrontCanvas.drawCircle(mCircleRadius + 5, mCircleRadius + 5, mCircleRadius, mCirclePaint);
        mFrontCanvas.drawText(mText, textXPosition, textYPosition, mUnreachedTextPaint);
        mFrontCanvas.drawCircle(mCircleRadius + 5, mCircleRadius + 5, mCircleRadius+1, mOutlineCircleUnreachedPaint);

        // use path to draw wave and  text in different color  on mBackCanvas
        // draw the wave line
        mPath.reset();
        mPath.moveTo(mWidth + mWidth / 4, mHeight);
        mPath.lineTo(-mWidth , mHeight);
        mPath.lineTo( mControlX-mWidth , mHeight+mWaveHeight/2 - (mHeight+mWaveHeight) * getProgress() / getMax());
        int x = mWidth/4;
        for(int i=0;i<10;i++){
            mPath.rQuadTo(x/2, mControlY,x, 0);
            mPath.rQuadTo(x / 2, -mControlY,x, 0);
        }
        mPath.close();
        mBackCanvas.drawPath(mPath, mWavePaint);
        mBackCanvas.drawText(mText, textXPosition, textYPosition, mReachedTextPaint);
        mBackCanvas.drawCircle(mCircleRadius + 5, mCircleRadius + 5, mCircleRadius + 1, mOutlineCircleReachedPaint);

        mFrontCanvas.drawBitmap(mBackBitmap, 0, 0, mBitmapPaint);
        canvas.drawBitmap(mFrontBitmap, 0, 0, null);


    }


    private void initializePaints(){
        mUnreachedTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mUnreachedTextPaint.setTextSize(mTextSize);
        mUnreachedTextPaint.setColor(mUnreachedTextColor);

        mReachedTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mReachedTextPaint.setTextSize(mTextSize);
        mReachedTextPaint.setColor(mReachedTextColor);
        mReachedTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(mCircleColor);

        mOutlineCircleUnreachedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOutlineCircleUnreachedPaint.setStyle(Paint.Style.STROKE);
        mOutlineCircleUnreachedPaint.setStrokeWidth(5);
        mOutlineCircleUnreachedPaint.setColor(mOutlineCircleUnreachedColor);

        mOutlineCircleReachedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOutlineCircleReachedPaint.setStyle(Paint.Style.STROKE);
        mOutlineCircleReachedPaint.setStrokeWidth(5);
        mOutlineCircleReachedPaint.setColor(mOutlineCircleReachedColor);
        mOutlineCircleReachedPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWavePaint.setColor(mWaveColor);

        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        mPath = new Path();
    }


    @Override
    public synchronized void setProgress(int progress) {

        invalidate();
        super.setProgress(progress);
    }



    public void setReachedTextColor(int reachedTextColor) {
        this.mReachedTextColor = reachedTextColor;
        mReachedTextPaint.setColor(mReachedTextColor);
        invalidate();
    }

    public void setUnreachedTextColor(int unreachedTextColor){
        this.mUnreachedTextColor = unreachedTextColor;
        mUnreachedTextPaint.setColor(mUnreachedTextColor);
        invalidate();
    }

    public void setWaveColor(int waveColor){
        this.mWaveColor = waveColor;
        mWavePaint.setColor(mWaveColor);
        invalidate();
    }

    public void setCircleColor(int circleColor){
        this.mCircleColor = circleColor;
        mCirclePaint.setColor(mCircleColor);
        invalidate();
    }

    public void setOutlineCircleUnreachedColor(int outlineCircleUnreachedColor){
        this.mOutlineCircleUnreachedColor = outlineCircleUnreachedColor;
        mOutlineCircleUnreachedPaint.setColor(mOutlineCircleUnreachedColor);
        invalidate();
    }

    public void setOutlineCircleReachedColor(int outlineCircleReachedColor){
        this.mOutlineCircleReachedColor = outlineCircleReachedColor;
        mOutlineCircleReachedPaint.setColor(mOutlineCircleReachedColor);
        invalidate();
    }

    public void setCircleRadius(int circleRadius){
        this.mCircleRadius = circleRadius;
        invalidate();
    }

    public void setTextSize(int textSize){
        this.mTextSize = textSize;
        invalidate();
    }

    public void setWaveHeight(int waveHeight){
        this.mWaveHeight = waveHeight;
        invalidate();
    }

    public void setmWaveSpeed(int waveSpeed){
        this.mWaveSpeed = waveSpeed;
        invalidate();
    }


    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public float sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}





