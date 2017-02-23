package com.jinjin.viewstudy.viewstudy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.jinjin.viewstudy.viewstudy.R;

/**
 * @since 1.0
 * <p/>带水印的图片
 * SnowJun  2017/2/9
 */
public class DemoImageView01 extends View {

    /**
     *要绘制的右下角的水印内容
     */
    private String mWatermark;
    /**
     * 水印的规格
     */
    private int mWatermarkSize;
    /**
     * 水印的颜色
     */
    private int mWatermarkColor;
    /**
     * 图片路径
     */
    private Bitmap mImageSrc;

    private Paint mPaint;
    private Rect mBound;
    private Rect mWaterMarkBound;

    /**
     * 宽度
     */
    private int mWidth;
    /**
     * 高度
     */
    private int mHeight;

    public DemoImageView01(Context context) {
        this(context,null);
    }

    public DemoImageView01(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DemoImageView01(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DemoImageView01,defStyleAttr,0);
        for (int i = 0; i < array.getIndexCount(); i++) {
            int attr = array.getIndex(i);
            switch (attr){
                case R.styleable.DemoImageView01_image:
                    Log.d("test","image-->" + array.getResourceId(attr,0));
                    mImageSrc = BitmapFactory.decodeResource(getResources(),array.getResourceId(attr,0));
                    break;
                case R.styleable.DemoImageView01_watermark:
                    mWatermark = array.getString(attr);
                    break;
                case R.styleable.DemoImageView01_watermarkcolor:
                    mWatermarkColor = array.getColor(attr, Color.BLUE);
                    break;
                case R.styleable.DemoImageView01_watermarksize:
                    int style = array.getInt(attr,0);
                    switch (style){
                        case 0:
                            mWatermarkSize = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());
                            break;
                        case 1:
                            mWatermarkSize = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());
                            break;
                        case 2:
                            mWatermarkSize = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics());
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }

        mPaint = new Paint();
        mBound = new Rect();
        mWaterMarkBound = new Rect();
        mPaint.setTextSize(mWatermarkSize);
        mPaint.getTextBounds(mWatermark, 0, mWatermark.length(), mWaterMarkBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY){
            mWidth = widthSize;
        }else {
            //图片的宽度  （注：水印覆盖在图片上，不占宽度）
            int desire = getPaddingLeft() + getPaddingRight() + mImageSrc.getWidth();
            mWidth = Math.min(desire,widthSize);
        }
        if (heightMode == MeasureSpec.EXACTLY){
            mHeight  = heightSize;
        }else {
            ///图片的宽度  （注：水印覆盖在图片上，不占高度）
            int desire = getPaddingTop() + getPaddingBottom() + mImageSrc.getHeight();
            mHeight = Math.min(desire,heightSize);
        }
        Log.d("test","mWidth" + mWidth);
        Log.d("test","mHeight" + mHeight);
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        /*
        整体绘制区域的规定
         */
        mBound.left = getPaddingLeft();
        mBound.right = mWidth - getPaddingRight();
        mBound.top = getPaddingTop();
        mBound.bottom = mHeight - getPaddingBottom();

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.YELLOW);
        //绘制图片
        canvas.drawBitmap(mImageSrc,null,mBound,mPaint);

        mPaint.setTextSize(mWatermarkSize);
        mPaint.getTextBounds(mWatermark,0,mWatermark.length(),mWaterMarkBound);
        mPaint.setColor(mWatermarkColor);
        /*
            如果水印的宽度 大于整体区域的宽度  则将水印多出的部分处理成为省略号
         */
        if (mWaterMarkBound.width() > mWidth){
            TextPaint paint = new TextPaint(mPaint);
            String mWaterMarkNow = TextUtils.ellipsize(mWatermark,paint,(float) (mWidth - getPaddingLeft() - getPaddingRight()), TextUtils.TruncateAt.END).toString();
            canvas.drawText(mWaterMarkNow,getPaddingLeft(), mHeight - getPaddingBottom() - mWaterMarkBound.height(), mPaint);
        }else {
            canvas.drawText(mWatermark, mWidth - mWaterMarkBound.width() - getPaddingRight() , mHeight - getPaddingBottom() - mWaterMarkBound.height(), mPaint);
        }

    }

}
