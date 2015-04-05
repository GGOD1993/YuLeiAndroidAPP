package com.example.pc.myapplication.ViewStyle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.pc.myapplication.AppConstant;

import java.util.Random;


public class ActiveView extends ImageView {

    //运动方向
    private int moveXDirection;
    private int moveYDirection;

    //运动速度
    private int moveXSpeed;
    private int moveYSpeed;

    //随机数发生器
    private Random random;

    private Paint mPaint = new Paint();

    public ActiveView(Context context) {
        super(context);
        initParams();
    }

    public ActiveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int getMoveXDirection() {
        return moveXDirection;
    }

    public void setMoveXDirection(int moveXDirection) {
        this.moveXDirection = moveXDirection;
    }

    public int getMoveYDirection() {
        return moveYDirection;
    }

    public void setMoveYDirection(int moveYDirection) {
        this.moveYDirection = moveYDirection;
    }

    public int getMoveXSpeed() {
        return moveXSpeed;
    }

    public void setMoveXSpeed(int moveXSpeed) {
        this.moveXSpeed = moveXSpeed;
    }

    public int getMoveYSpeed() {
        return moveYSpeed;
    }

    public void setMoveYSpeed(int moveYSpeed) {
        this.moveYSpeed = moveYSpeed;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
    }

    /**
     * 初始化view的移动参数
     */
    private void initParams() {
        random = new Random();
        moveXSpeed = (int) (random.nextInt(AppConstant.TOP_SPEED) + 1);
        moveYSpeed = (int) (random.nextInt(AppConstant.TOP_SPEED) + 1);
        if (random.nextInt(100)%2 == 0) {
            moveXDirection = AppConstant.RIGHT_DIRECTION;
        } else {
            moveXDirection = AppConstant.LEFT_DIRECTION;
        }

        if (random.nextInt(100)%2 == 0) {
            moveYDirection = AppConstant.UP_DIRECTION;
        } else {
            moveYDirection = AppConstant.DOWN_DIRECTION;
        }
    }
}
