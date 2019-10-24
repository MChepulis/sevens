package com.example.sevens;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

public class ViewMusicButton extends AppCompatImageButton {

    public static final int RETURN_TOUCH_OFFSET = 5;

    private ActivityMain m_app;

    private boolean inside;
    private boolean isOn;

    public ViewMusicButton(Context context) {
        super(context);
        m_app = (ActivityMain) context;
        init(null);
    }

    public ViewMusicButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_app = (ActivityMain) context;
        init(attrs);
    }

    public ViewMusicButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        m_app = (ActivityMain) context;
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        resetImage();
        setBackgroundResource(R.drawable.sound_on);
        setScaleType(ScaleType.CENTER_INSIDE);
        setBackgroundColor(0);
    }

    private void resetImage() {
        if (SettingsHandler.getMusicVolume() == 0) {
            setImageResource(R.drawable.sound_off);
            isOn = false;
        } else {
            setImageResource(R.drawable.sound_on);
            isOn = true;
        }

    }

    public void onClick(View v) {
        SettingsHandler.setIsMusicOn(!SettingsHandler.getIsMusicOn());
        resetImage();
        System.out.println("sound =\t" + SettingsHandler.getMusicVolume());
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        resetImage();
        super.onDraw(canvas);
    }

}
