

package com.ljh.mytest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SlideTab extends View {
    private String TAG = "SlideTab";
    private int mTextSize;          //文本的字体大小
    private int mColorTextDef;      // 默认文本的颜色
    private int mColorDef;          // 线段和圆圈颜色
    private int mColorSelected;     //选中的字体和圆圈颜色
    private int mLineHight;         //基准线高度
    private int mCircleHight;       //圆圈的高度（直径）
    private int mCircleSelStroke;   //被选中圆圈（空心）的粗细
    private int mMarginTop;         //圆圈和文字之间的距离
    private List<String> tabNames = new ArrayList<>();      //需要绘制的文字

    /**
     * 下面需要计算
     */
    private float splitLength;         //每一段横线长度
    private int textStartY;            //文本绘制的Y轴坐标
    private List<Rect> mBounds;     //保存文本的量的结果

    private int selectedIndex = 0;      //当前选中序号

    private Paint mTextPaint;      //绘制文字的画笔
    private Paint mLinePaint;      //绘制基准线的画笔
    private Paint mCirclePaint;    //绘制基准线上灰色圆圈的画笔
    private Paint mCircleSelPaint; //绘制被选中位置的蓝色圆圈的画笔

    public SlideTab(Context context) {
        this(context, null);
    }

    public SlideTab(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mColorTextDef = Color.GRAY;
        mColorSelected = Color.GREEN;
        mColorDef = Color.argb(255, 234, 234, 234);   //#EAEAEA
        mTextSize = 20;

        mLineHight = 5;
        mCircleHight = 20;
        mCircleSelStroke = 10;
        mMarginTop = 50;

        mLinePaint = new Paint();
        mCirclePaint = new Paint();
        mTextPaint = new Paint();
        mCircleSelPaint = new Paint();

        mLinePaint.setColor(mColorDef);
        mLinePaint.setStyle(Paint.Style.FILL);//设置填充
        mLinePaint.setStrokeWidth(mLineHight);//笔宽像素
        mLinePaint.setAntiAlias(true);//锯齿不显示

        mCirclePaint.setColor(mColorDef);
        mCirclePaint.setStyle(Paint.Style.FILL);//设置填充
        mCirclePaint.setStrokeWidth(1);//笔宽像素
        mCirclePaint.setAntiAlias(true);//锯齿不显示
        mCircleSelPaint.setColor(mColorSelected);
        mCircleSelPaint.setStyle(Paint.Style.STROKE);    //空心圆圈
        mCircleSelPaint.setStrokeWidth(mCircleSelStroke);
        mCircleSelPaint.setAntiAlias(true);

        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mColorTextDef);
        mTextPaint.setAntiAlias(true);
    }

    /**
     * measure the text bounds by paint
     */
    private void measureText() {
        mBounds = new ArrayList<>();
        for (String name : tabNames) {
            Rect mBound = new Rect();
            mTextPaint.getTextBounds(name, 0, name.length(), mBound);
            mBounds.add(mBound);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);  //获取宽的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);  //获取高的模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);  //获取宽的尺寸
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);  //获取高的尺寸

        int height;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            float textHeight = mBounds.size() > 0 ? mBounds.get(0).height() : 20;
            height = (int) (textHeight + mCircleHight + mMarginTop);
        }
        //保存测量宽度和测量高度
        setMeasuredDimension(widthSize, height);
        initConstant();
    }

    private void initConstant() {
        int lineLength = getWidth() - getPaddingLeft() - getPaddingRight() - mCircleHight;
        splitLength = lineLength / (tabNames.size() - 1);
//        textStartY = mCircleHight + mMarginTop + getPaddingTop();
        // FontMetrics对象
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        textStartY = getHeight() - (int) fontMetrics.bottom;    //baseLine的位置
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(mCircleHight / 2, mCircleHight / 2, getWidth() - mCircleHight / 2, mCircleHight / 2, mLinePaint);
        float centerY = mCircleHight / 2;
        for (int i = 0; i < tabNames.size(); i++) {
            float centerX = mCircleHight / 2 + (i * splitLength);
            canvas.drawCircle(centerX, mCircleHight / 2, mCircleHight / 2, mCirclePaint);

            mTextPaint.setColor(mColorTextDef);
            if (i == selectedIndex) {
                if (!isSliding) {
                    mCircleSelPaint.setStrokeWidth(mCircleSelStroke);
                    mCircleSelPaint.setStyle(Paint.Style.STROKE);
                    canvas.drawCircle(centerX, mCircleHight / 2, (mCircleHight - mCircleSelStroke) / 2, mCircleSelPaint);
                }
                mTextPaint.setColor(mColorSelected);
            }

            float startX = 0;
            if (i == 0) {
                startX = 0;
            } else if (i == tabNames.size() - 1) {
                startX = getWidth() - mBounds.get(i).width();
            } else {
                startX = centerX - (mBounds.get(i).width() / 2);
            }
            canvas.drawText(tabNames.get(i), startX, textStartY, mTextPaint);
        }

        //画手指拖动位置圆圈,最后画，避免被其他圆圈覆盖
        if (isSliding) {
//            Log.v(TAG, "手指拖动画圆：X:"+slidX+"  Y:"+centerY+"  半径："+mCircleHight/2);
            mCircleSelPaint.setStrokeWidth(1);
            mCircleSelPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(slidX, centerY, mCircleHight / 2, mCircleSelPaint);
        }
    }

    private boolean isSliding = false;  //手指是否在拖动
    private float slidX, slidY;         //手指当前位置（相对于本控件左上角的坐标）

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        slidX = event.getX();
        slidY = event.getY();
        //左右越界
        if (slidX < mCircleHight / 2) {
            slidX = mCircleHight / 2;
        }
        if (slidX > (getWidth() - mCircleHight / 2)) {
            slidX = getWidth() - mCircleHight / 2;
        }
        float select = slidX / splitLength;
        int xs = (int) ((select * 10) - (((int)select) * 10));
        selectedIndex = (int) (select + (xs > 5 ? 1 : 0));

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isSliding = true;
//                Log.e(TAG, "手指按下:  getX:"+slidX+"  getY:"+slidY);
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.i(TAG, "手指滑动:  getX:"+slidX+"  getY:"+slidY);
                break;
            case MotionEvent.ACTION_UP:
//                Log.e(TAG, "手指抬起:  getX:"+slidX+"  getY:"+slidY);
                isSliding = false;
                break;
        }
        invalidate();
        return true;
    }

    public List<String> getTabNames() {
        return tabNames;
    }

    public void setTabNames(List<String> tabNames) {
        this.tabNames = tabNames;
        measureText();
        invalidate();
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        invalidate();
    }
}
