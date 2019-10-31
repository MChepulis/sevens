package com.example.sevens;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;


public class ViewHammerButton extends AppCompatImageButton {

    private ActivityMain m_app;
    private boolean inside;
    private boolean isOn;
    private HexagonGrid hexGrid;


    public ViewHammerButton(Context context) {
        super(context);
        m_app = (ActivityMain) context;
        init(null);
    }

    public ViewHammerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_app = (ActivityMain) context;
        init(attrs);
    }

    public ViewHammerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        m_app = (ActivityMain) context;
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        isOn = false;
        resetImage();
        setImageResource(R.drawable.hammer);
        //setBackgroundResource(R.drawable.btn_red_orange_yellow);
        //setBackgroundColor(0);
        setScaleType(ScaleType.CENTER_INSIDE);

    }


    private void resetImage() {
        if (isOn) {
            setBackgroundResource(R.drawable.btn_blue_darkblue_violet);
        } else {
            setBackgroundResource(R.drawable.btn_red_orange_yellow);
        }
    }

    public void setHexGrid(HexagonGrid new_grid){
        hexGrid = new_grid;
        hexGrid.setHammer(this);
        resetHexGridHammerState();
    }

    private void resetHexGridHammerState(){
        hexGrid.resetHammerState();
        hexGrid.invalidate();
    }
    public void off(){
        isOn = false;
        resetHexGridHammerState();
        this.invalidate();
    }

    public void done(){
        isOn = false;
        resetHexGridHammerState();
        this.invalidate();
    }

    public boolean getState(){return isOn;}


    public void onClick(View v) {
        isOn = !isOn;
        this.invalidate();
        resetHexGridHammerState();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        resetImage();
        super.onDraw(canvas);
    }

}

