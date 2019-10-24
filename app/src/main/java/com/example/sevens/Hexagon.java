package com.example.sevens;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.Comparator;

public class Hexagon extends View{

    public static final int STATE_EMPTY = 0;
    public static final int STATE_MAX = 7;

    private static final int tmp_alpha = 0xCF;

    /*Integer[] colorForState = {R.color.state_0, R.color.state_1, R.color.state_2, R.color.state_3,
            R.color.state_4, R.color.state_5, R.color.state_6,  R.color.state_7};
*/
    int state_0 = Color.argb(255, 0, 0, 0);
    int state_1 = Color.argb(255, 176, 0, 0);
    int state_2 = Color.argb(255, 255, 117, 20);
    int state_3 = Color.argb(255, 255, 220, 51);
    int state_4 = Color.argb(255, 0, 128, 0);
    int state_5 = Color.argb(255, 66, 170, 255);
    int state_6 = Color.argb(255, 0, 71, 171);
    int state_7 = Color.argb(255, 83, 55, 122);
    int state_8 = Color.argb(255, 255, 255, 255);
    /*Integer[] colorForState = {Color.BLACK, Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN,
            Color.BLUE, Color.MAGENTA, Color.WHITE}; */
    Integer[] colorForState = {state_0, state_1, state_2, state_3, state_4, state_5,
            state_6, state_7, state_8};

    private float radius;
    private Integer color;
    private ActivityMain m_app;
    private Boolean m_isExist;
    private int state;
    private boolean isDoneFlag;
    private int tmp_state;

    public void setIsDoneFlag( boolean new_value) { isDoneFlag = new_value;    }
    public void setColor(Integer color) {
        this.color = color;
    }
    public void setExist(Boolean isExist_) {
        this.m_isExist= isExist_;
    }
    public void setState(int newState) {state = newState; }
    public void setTmpState(int  newState) {tmp_state = newState; }


    public float getRadius(){return radius;}
    public int getColor(){return  color;}
    public Boolean isExist(){ return  m_isExist; }
    public int getState() { return  state; }
    public void ApdateState() { state = (state +  1) % (STATE_MAX + 1); }
    public static int getMaxState() { return STATE_MAX; }
    public boolean getIsDoneFlag(){ return isDoneFlag; }
    public boolean IsEmpty() { return state == STATE_EMPTY; }
    public int getTmpState() { return tmp_state; }

    public void ResetState() {state = STATE_EMPTY; }
    public void ResetTmpState() {tmp_state = STATE_EMPTY; }


    private void init(Context context){
        m_app = (ActivityMain) context;
        m_isExist = true;
        state = STATE_EMPTY;
        color = Color.RED;
        isDoneFlag = false;
        tmp_state = STATE_EMPTY ;

    }
    public Hexagon(Context context) {
        super(context);
        init(context);
    }

    public Hexagon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Hexagon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setParams (float x, float y, float radius)
    {
        this.setX(x);
        this.setY(y);
        this.radius = radius;
        color = Color.RED;
    }

    //сейчас апроксимируется вписанной окружностью
    public boolean isContain(float x, float y)
    {

        float tmp_x = x-getX();
        float tmp_y = y-getY();
        float rad_intersect = (float)(radius * Math.sqrt(3.0) / 2.0);
        if(tmp_x * tmp_x  + tmp_y * tmp_y <  rad_intersect *  rad_intersect)
        {
            return true;
        }
        else return false;
    }

    private void drawLikePath(Canvas canvas)
    {
        if(!isExist())
        {
            return;
        }
        int alpha;
        if(tmp_state == STATE_EMPTY)
        {
            color = colorForState[state];
            alpha = 0xFF;
        }
        else
        {
            color = colorForState[tmp_state];
            alpha = tmp_alpha;
        }

        Paint paintGraftFill = new Paint();
        paintGraftFill.setStyle(Paint.Style.FILL);
        paintGraftFill.setColor(color);
        paintGraftFill.setAlpha(alpha);
        paintGraftFill.setAntiAlias(true);

        int vertNum = 6;
        float x_tmp, y_tmp;
        double angleStep = 2 * Math.PI / vertNum;

        Path path = new Path();
        path.reset();

        x_tmp = this.getX() + radius * (float) Math.sin(0);
        y_tmp = this.getY() + radius * (float) Math.cos(0);
        path.moveTo(x_tmp, y_tmp);
        for (int i = 1; i < vertNum; i++)  ////тут можно скруглить углы  (например кривой Безье)
        {

            x_tmp = this.getX() + radius * (float) Math.sin(i * angleStep);
            y_tmp = this.getY() + radius * (float) Math.cos(i * angleStep);
            path.lineTo(x_tmp, y_tmp);
        }
        path.close();

        canvas.drawPath(path, paintGraftFill);
    }
    public void onDraw(Canvas canvas)
    {
        drawLikePath(canvas);

    }

}
