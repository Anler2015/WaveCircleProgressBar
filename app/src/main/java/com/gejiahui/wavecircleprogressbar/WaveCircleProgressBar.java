package com.gejiahui.wavecircleprogressbar;
import android.content.Context;
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
    /**
     * used to draw the wave
     */
    private Paint mPathPaint;
    /**
     * used to draw the circle
     */
    private Paint mCirclePaint;
    /**
     * used to draw the text on the mBackBitmap
     */
    private TextPaint mBeforeTextPaint;
    /**
     * used to draw the text on the mFrontBitmap
     */
    private TextPaint mAfterTextPaint;
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
     * circle's radius
     */
    private int mRadius = (200);
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
    int mWidth;
    /**
     * view's height
     */
    int mHeight;
    /**
     * the point to control the wave
     */
    int mControlX ,mControlY;
    /**
     * text on the center of the circle
     */
    String mText = "";
    /**
     * used to update the data to make wave more vivid
     */
    Handler updateHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if(mControlX<mWidth){
                mControlX+=15;
            }else {
                mControlX = 0;
            }

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

        mBeforeTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mBeforeTextPaint.setTextSize(50);

        mAfterTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mAfterTextPaint.setTextSize(50);
        mAfterTextPaint.setColor(Color.WHITE);
        mAfterTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.RED);

        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setColor(Color.BLUE);

        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        mPath = new Path();
        updateHandler.sendEmptyMessageDelayed(0,100);
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(measure(widthMeasureSpec,true),measure(heightMeasureSpec,false));
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
            result = padding + 2 * mRadius +2*(int)mCirclePaint.getStrokeWidth();
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
        mControlX = 0;
        mControlY = 100;

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
        float textXPosition = mRadius - mBeforeTextPaint.measureText(mText)/2;
        float textYPosition = mRadius - (mBeforeTextPaint.descent()+mBeforeTextPaint.ascent())/2;

        //draw a circle and progress on the  mFrontCanvas
        mFrontCanvas.translate(getPaddingLeft(), getPaddingTop());
        mFrontCanvas.drawCircle(mRadius + mCirclePaint.getStrokeWidth(), mRadius + mCirclePaint.getStrokeWidth(), mRadius, mCirclePaint);
        mFrontCanvas.drawText(mText, textXPosition, textYPosition, mBeforeTextPaint);

        // use path to draw wave and  text in different color  on mBackCanvas
        mPath.reset();
        mPath.moveTo(mWidth + mWidth / 4, mHeight);
        mPath.lineTo(-mWidth , mHeight);
        mPath.lineTo( mControlX-mWidth , mHeight+10 - (mHeight+20) * getProgress() / getMax());
        int x = mWidth/10;
        for(int i=0;i<10;i++){
            mPath.rQuadTo(x/2, 10,x, 0);
            mPath.rQuadTo(x/2, -10,x, 0);
        }
        mPath.close();
        mBackCanvas.drawPath(mPath, mPathPaint);
        mBackCanvas.drawText(mText, textXPosition, textYPosition, mAfterTextPaint);


        mFrontCanvas.drawBitmap(mBackBitmap,0,0,mBitmapPaint);
        canvas.drawBitmap(mFrontBitmap, 0, 0, null);

    }


    @Override
    public synchronized void setProgress(int progress) {
        invalidate();
        super.setProgress(progress);
    }
}





