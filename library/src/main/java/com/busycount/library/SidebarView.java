package com.busycount.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * 项目名称：IndexSidebar
 * 类描述：侧边栏View
 * 创建人：Chen.h
 * 创建时间：2017/4/17 15:38
 * 修改人：Chen.h
 * 修改时间：2017/4/17 15:38
 * 修改备注：
 */
public class SidebarView extends View {

    private String[] letters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
                                            "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    /**
     * 默认view长度
     */
    private int def_length = 1080;
    /**
     * 默认view宽度
     */
    private int def_width = 40;
    /**
     * 字符颜色
     */
    private int letterColor;
    /**
     * 字符大小
     */
    private int letterSize;
    /**
     * 全角
     */
    private boolean isFullWidth;
    /**
     * 是否水平，默认垂直
     */
    private boolean isHorizontal;
    /**
     * 总字符长度
     */
    private int totalLength;
    /**
     * 单元长度
     */
    private float cellWidth;
    /**
     * 单元宽度
     */
    private float cellHeight;
    /**
     * Paint
     */
    private Paint mPaint;
    /**
     * 单元列表
     */
    private ArrayList<Cell> cellList = new ArrayList<>();
    /**
     * 索引监听
     */
    private OnLetterChangedListener onLetterChangedListener;

    public SidebarView(Context context) {
        this(context, null);
    }

    public SidebarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SidebarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SidebarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SidebarView);
        letterColor = ta.getColor(R.styleable.SidebarView_letterColor, Color.GRAY);
        letterSize = ta.getDimensionPixelSize(R.styleable.SidebarView_letterSize, 24);
        isFullWidth = ta.getBoolean(R.styleable.SidebarView_isFullWidth, false);
        isHorizontal = ta.getBoolean(R.styleable.SidebarView_isHorizontal, false);
        ta.recycle();
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpec;
        int heightSpec;
        if (isHorizontal) {
            widthSpec = getMeasureSize(widthMeasureSpec, def_length);
            heightSpec = getMeasureSize(heightMeasureSpec, def_width);
        } else {
            widthSpec = getMeasureSize(widthMeasureSpec, def_width);
            heightSpec = getMeasureSize(heightMeasureSpec, def_length);
        }
        setMeasuredDimension(widthSpec, heightSpec);
    }


    /**
     * 测量后的尺寸
     *
     * @param measureSpec 待测measureSpec
     * @param defaultSize 默认尺寸
     * @return 测量后的尺寸
     */
    private int getMeasureSize(int measureSpec, int defaultSize) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            return size;
        } else if (mode == MeasureSpec.AT_MOST) {
            return Math.min(defaultSize, size);
        } else {
            return defaultSize;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initCell();
        drawSidebar(canvas);
    }


    /**
     * 初始化绘制区域参数
     */
    private void initCell() {
        totalLength = letters.length;
        int totalWidth = getMeasuredWidth();
        int totalHeight = getMeasuredHeight();
        cellWidth = totalWidth / totalLength;
        cellHeight = totalHeight / totalLength;
        float startX;
        float startY;
        if (isHorizontal) {
            getDrawLetterSize(cellWidth, totalHeight);
            startX = (cellWidth - getDrawLetterWidth()) / 2;
            startY = (totalHeight + letterSize) / 2;
            initCellHorizontal(totalHeight, startX, startY);
        } else {
            getDrawLetterSize(totalWidth, cellHeight);
            startX = (totalWidth - getDrawLetterWidth()) / 2;
            startY = (cellHeight + letterSize) / 2;
            initCellVertical(totalWidth, startX, startY);
        }
    }

    /**
     * 得到绘制文字尺寸
     *
     * @param x x
     * @param y y
     */
    private void getDrawLetterSize(float x, float y) {
        int minSize = (int) Math.min(x, y);
        if (letterSize > minSize) {
            letterSize = minSize;
        }
    }

    /**
     * 得到绘制文字宽度
     *
     * @return 绘制文字宽度
     */
    private int getDrawLetterWidth() {
        return isFullWidth ? letterSize * 2 : letterSize;
    }

    /**
     * 初始化绘制单元
     *
     * @param totalHeight 总高度
     * @param startX      文字开始x坐标
     * @param startY      文字开始y坐标
     */
    private void initCellHorizontal(int totalHeight, float startX, float startY) {
        cellList.clear();
        Cell cell;
        float tempWidth;
        for (int i = 0; i < totalLength; i++) {
            tempWidth = i * cellWidth;
            cell = new Cell();
            cell.setX1(tempWidth);
            cell.setX2((i + 1) * tempWidth);
            cell.setY1(0);
            cell.setY2(totalHeight);
            cell.setXt(startX + tempWidth);
            cell.setYt(startY);
            cellList.add(cell);
        }
    }

    /**
     * 初始化绘制单元
     *
     * @param totalWidth 总长度
     * @param startX     文字开始x坐标
     * @param startY     文字开始y坐标
     */
    private void initCellVertical(int totalWidth, float startX, float startY) {
        cellList.clear();
        Cell cell;
        float tempHeight;
        for (int i = 0; i < totalLength; i++) {
            tempHeight = i * cellHeight;
            cell = new Cell();
            cell.setX1(0);
            cell.setX2(totalWidth);
            cell.setY1(tempHeight);
            cell.setY2((i + 1) * cellHeight);
            cell.setXt(startX);
            cell.setYt(startY + tempHeight);
            cellList.add(cell);
        }
    }


    /**
     * 绘制Sidebar
     *
     * @param canvas Canvas
     */
    private void drawSidebar(Canvas canvas) {
        mPaint.setColor(letterColor);
        mPaint.setTextSize(letterSize);
        Cell c;
        for (int i = 0; i < totalLength; i++) {
            c = cellList.get(i);
            canvas.drawText(letters[i], c.getXt(), c.getYt(), mPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                onSidebarTouch(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                onSidebarTouch(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                onSidebarTouchIndex(-1);
                break;
        }
        return true;
    }

    /**
     * 触摸事件
     *
     * @param x x坐标
     * @param y y坐标
     */
    private void onSidebarTouch(float x, float y) {
        int index;
        if (isHorizontal) {
            index = getChooseIndexHorizontal(x);
        } else {
            index = getChooseIndexVertical(y);
        }
        onSidebarTouchIndex(index);
    }


    /**
     * 触摸事件
     *
     * @param index 索引下标
     */
    private void onSidebarTouchIndex(int index) {
        if (onLetterChangedListener == null) {
            return;
        }
        if (index < 0 || index >= totalLength) {
            onLetterChangedListener.onLetterChanged(-1, null);
        } else {
            onLetterChangedListener.onLetterChanged(index, letters[index]);
        }
    }

    /**
     * 获得touch的索引下标
     *
     * @param x x坐标
     * @return 索引下标
     */
    private int getChooseIndexHorizontal(float x) {
        return (int) (x / cellWidth);
    }

    /**
     * 获得touch的索引下标
     *
     * @param y y坐标
     * @return 索引下标
     */
    private int getChooseIndexVertical(float y) {
        return (int) (y / cellHeight);
    }

    public void setLetters(String[] letters) {
        this.letters = letters;
        invalidate();
    }

    /**
     * 设置字符大小
     *
     * @param size letter size
     */
    public void setLetterSize(int size) {
        this.letterSize = size;
        invalidate();
    }

    /**
     * 设置字符颜色
     *
     * @param color letter color
     */
    public void setLetterColor(int color) {
        this.letterColor = color;
        invalidate();
    }

    /**
     * 设置
     *
     * @param isHorizontal 是否水平绘制
     */
    public void setHorizontal(boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
        invalidate();
    }


    /**
     * @param isFullWidth
     */
    public void setFullWidth(boolean isFullWidth) {
        this.isFullWidth = isFullWidth;
        invalidate();
    }


    public OnLetterChangedListener getOnLetterChangedListener() {
        return onLetterChangedListener;
    }

    public void setOnLetterChangedListener(OnLetterChangedListener onLetterChangedListener) {
        this.onLetterChangedListener = onLetterChangedListener;
    }

    /**
     * 索引监听
     */
    public interface OnLetterChangedListener {

        void onLetterChanged(int position, String str);
    }

    /**
     * 绘制单元
     */
    static class Cell {
        /**
         * x start
         */
        private float x1;
        /**
         * x end
         */
        private float x2;
        /**
         * y start
         */
        private float y1;
        /**
         * y end
         */
        private float y2;
        /**
         * letter x start
         */
        private float xt;
        /**
         * letter y start
         */
        private float yt;

        public Cell() {
        }

        public Cell(float x1, float x2, float y1, float y2, float xt, float yt) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
            this.xt = xt;
            this.yt = yt;
        }

        public float getX1() {
            return x1;
        }

        public void setX1(float x1) {
            this.x1 = x1;
        }

        public float getX2() {
            return x2;
        }

        public void setX2(float x2) {
            this.x2 = x2;
        }

        public float getY1() {
            return y1;
        }

        public void setY1(float y1) {
            this.y1 = y1;
        }

        public float getY2() {
            return y2;
        }

        public void setY2(float y2) {
            this.y2 = y2;
        }

        public float getXt() {
            return xt;
        }

        public void setXt(float xt) {
            this.xt = xt;
        }

        public float getYt() {
            return yt;
        }

        public void setYt(float yt) {
            this.yt = yt;
        }
    }
}
