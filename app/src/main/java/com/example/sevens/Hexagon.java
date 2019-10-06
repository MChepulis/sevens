package com.example.sevens;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class Hexagon extends View{

    public static final int STATE_EMPTY = 0;
    public static final int STATE_MAX = 7;

    /*Integer[] colorForState = {R.color.state_0, R.color.state_1, R.color.state_2, R.color.state_3,
            R.color.state_4, R.color.state_5, R.color.state_6,  R.color.state_7};
*/
    Integer[] colorForState = {Color.BLACK, Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN,
            Color.BLUE, Color.MAGENTA, Color.WHITE};

    private float radius;
    private Integer color;
    private ActivityMain m_app;
    private Boolean m_isExist;
    private int state;


    public void setColor(Integer color) {
        this.color = color;
    }

    public void setExist(Boolean isExist_) {
        this.m_isExist= isExist_;
    }


    public int getColor(){return  color;}
    public Boolean isExist(){ return  m_isExist; }
    public int getState() { return  state; }
    public void ApdateState() { state = (state +  1) % STATE_MAX; }
    public void ResetState() {state = STATE_EMPTY; }

    public void setState(int newState) {state = newState; }




    public Hexagon(Context context) {
        super(context);
        m_app = (ActivityMain) context;
        m_isExist = true;
        state = STATE_EMPTY;
        color = Color.RED;
    }

    public Hexagon(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_app = (ActivityMain) context;
        m_isExist = true;
        state = STATE_EMPTY;
    }

    public Hexagon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        m_app = (ActivityMain) context;
        m_isExist = true;
        state = STATE_EMPTY;
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
        int tmp_color = color;
        if(color == Color.RED)
        {
            tmp_color = colorForState[state];
        }
        Paint paintGraftFill = new Paint();
        paintGraftFill.setStyle(Paint.Style.FILL);
        paintGraftFill.setColor(tmp_color);  ////////////////тут нужен цвет!!!
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