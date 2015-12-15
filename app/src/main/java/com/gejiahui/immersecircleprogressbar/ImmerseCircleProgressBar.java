package com.gejiahui.immersecircleprogressbar;
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
public class ImmerseCircleProgressBar extends ProgressBar {
    private Paint mPathPaint;
    private Paint mCirclePaint;
    private TextPaint mTextPaint;
    private Path mPath;
    private int mRadius = (200);
    private Bitmap waveBitmap;
    int mWidth;
    int mHeight;
    int mControlX ,mControlY;
    String mText = "";
    public ImmerseCircleProgressBar(Context context) {
        this(context, null );
    }

    public ImmerseCircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImmerseCircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(50);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.RED);


        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setColor(Color.BLUE);
        mPathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
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
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {


        mText = getProgress()+"%";
        float textXPosition = mRadius - mTextPaint.measureText(mText)/2;
        float textYPosition = mRadius - (mTextPaint.descent()+mTextPaint.ascent())/2;
        if(waveBitmap == null){
            waveBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        }
        Canvas mBitmapCanvas = new Canvas(waveBitmap);

        mBitmapCanvas.translate(getPaddingLeft(), getPaddingTop());
        mBitmapCanvas.drawCircle(mRadius + mCirclePaint.getStrokeWidth(), mRadius + mCirclePaint.getStrokeWidth(), mRadius, mCirclePaint);


        mPath.reset();
        mPath.moveTo(mWidth + mWidth / 4, mHeight);
        mPath.lineTo(-mWidth , mHeight);
   //     mPath.lineTo(-mWidth / 4, mHeight - mHeight * getProgress() / getMax());
   //     mPath.cubicTo(mControlX,mControlY+mHeight - mHeight * getProgress() / getMax(),mControlX+mWidth/4,-mControlY+mHeight - mHeight * getProgress() / getMax(),mWidth + mWidth / 4,mHeight - mHeight * getProgress() / getMax());
     //   mPath.quadTo(mControlX,mControlY+mHeight - mHeight * getProgress() / getMax(),mWidth + mWidth / 4,mHeight - mHeight * getProgress() / getMax());
   //     mPath.lineTo( mControlX-400, mHeight);
        mPath.lineTo( mControlX-mWidth , mHeight+10 - (mHeight+20) * getProgress() / getMax());
        int x = mWidth/10;
        for(int i=0;i<10;i++){
            mPath.rQuadTo(x/2, 10,x, 0);
            mPath.rQuadTo(x/2, -10,x, 0);
        }


        mPath.close();
        mBitmapCanvas.drawPath(mPath, mPathPaint);
        mBitmapCanvas.drawText(mText, textXPosition, textYPosition, mTextPaint);
        canvas.drawBitmap(waveBitmap,0,0,null);

//        boolean b = false;
//        if(mControlX > mWidth + mWidth / 4){
//            b =true;
//        }
//
//        if(mControlX < -mWidth / 4)
//        {
//            b = false;
//        }
//        mControlX = b ? mControlX -20 :mControlX+20;
        if(mControlX<mWidth){
            mControlX+=20;
        }else {
            mControlX = 0;
        }

    }




}





