package com.gejiahui.wavecircleprogressbar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;
/**
 * Created by gejiahui on 2015/12/15.
 */
public class WaveCircleProgressBar extends ProgressBar {
    private Paint mPathPaint;
    private Paint mCirclePaint;
    private TextPaint mBeforeTextPaint;
    private TextPaint mAfterTextPaint;
    private Paint mBitmapPaint;
    private Paint mClearPaint;
    private Path mPath;
    private int mRadius = (200);
    private Bitmap mFrontBitmap;
    private Bitmap mBackBitmap;
    private Canvas mFrontCanvas;
    private Canvas mBackCanvas;
    int mWidth;
    int mHeight;
    int mControlX ,mControlY;
    String mText = "";
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
        mFrontCanvas.drawPaint(mClearPaint);
        mBackCanvas.drawPaint(mClearPaint);

        mText = getProgress()+"%";
        float textXPosition = mRadius - mBeforeTextPaint.measureText(mText)/2;
        float textYPosition = mRadius - (mBeforeTextPaint.descent()+mBeforeTextPaint.ascent())/2;

        mFrontCanvas.translate(getPaddingLeft(), getPaddingTop());
        mFrontCanvas.drawCircle(mRadius + mCirclePaint.getStrokeWidth(), mRadius + mCirclePaint.getStrokeWidth(), mRadius, mCirclePaint);
        mFrontCanvas.drawText(mText, textXPosition, textYPosition, mBeforeTextPaint);

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

        canvas.drawBitmap(mFrontBitmap,0,0,null);



        if(mControlX<mWidth){
            mControlX+=15;
        }else {
            mControlX = 0;
        }

    }




}





