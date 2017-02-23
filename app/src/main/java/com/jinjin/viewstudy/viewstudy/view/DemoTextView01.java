package com.jinjin.viewstudy.viewstudy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.jinjin.viewstudy.viewstudy.R;

/**
 * @since 1.0
 * <p/>
 * SnowJun  2017/2/9
 */
public class DemoTextView01 extends View {


    private String mText;
    private int mTextColor;
    private int mTextSize;

    private Paint mPaint;
    private Rect mBound;

    public DemoTextView01(Context context) {
        this(context,null);
    }

    public DemoTextView01(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DemoTextView01(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取到我们自定义的属性值
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DemoTextView01,defStyleAttr,0);
        // 遍历取到相关的值
        for (int i = 0; i < array.getIndexCount(); i++) {
            //取到相关属性的ID值，根据Id匹配取到相关的值
            int attr = array.getIndex(i);
            switch (attr){
                case R.styleable.DemoTextView01_text://指定的属性对应的ID值写法：R.styleable.类名_属性名
                    mText=array.getString(attr);
                    break;
                case R.styleable.DemoTextView01_textColor:
                    //取到设置的字体颜色的值，默认的字体颜色为黑色
                    mTextColor=array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.DemoTextView01_textSize:
                    //设置默认值为16sp   TypeValue是对sp和dp进行相互的转换（各种尺寸的转换）
                    mTextSize = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                default:
                    break;

            }
        }
        array.recycle();

        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);

        mBound = new Rect();
        mPaint.getTextBounds(mText,0,mText.length(),mBound);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else {
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mText,0,mText.length(),mBound);
            float textWidth = mBound.width();
            int desire = (int)(getPaddingLeft()+textWidth+ getPaddingRight());
            width = Math.min(desire,widthSize);
        }
        if (heightMode == MeasureSpec.EXACTLY){
            height  = heightSize;
        }else {
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mText,0,mText.length(),mBound);
            float textHeight = mBound.height();
            int desire = (int)(getPaddingTop()+textHeight+ getPaddingBottom());
            height = Math.min(desire,heightSize);
        }
        setMeasuredDimension(width,height);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        //设置底色为灰色 并进行绘制
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);

        //设置字体颜色为设置的字体颜色，并进行内容的绘制
        mPaint.setColor(mTextColor);
        canvas.drawText(mText,getWidth()/2-mBound.width()/2,getHeight()/2+mBound.height()/2, mPaint);
    }


}
