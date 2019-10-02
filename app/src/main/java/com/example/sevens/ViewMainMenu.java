package com.example.sevens;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class MenuRefreshHandler extends Handler
{
    ViewMainMenu	m_viewMainMenu;


    public MenuRefreshHandler(ViewMainMenu v)
    {
        m_viewMainMenu = v;
    }

    public void handleMessage(Message msg)
    {
        m_viewMainMenu.update();
        m_viewMainMenu.invalidate();
    }

    public void sleep(long delayMillis)
    {
        this.removeMessages(0);
        sendMessageDelayed(obtainMessage(0), delayMillis);
    }
};

public class ViewMainMenu extends View {

    // CONST
    private static final int UPDATE_TIME_MS = 30;

    ActivityMain	m_app;
    MenuRefreshHandler   m_handler;
    boolean			m_isActive;

    private int m_scrW, m_scrH;
    private int m_scrCenterX, m_scrCenterY;
    private ImageView background;


    public ViewMainMenu(ActivityMain app)
    {
        super(app);
        m_app = app;
        m_handler 	= new MenuRefreshHandler(this);
        m_isActive 	= false;
        setOnTouchListener(app);
        /*
        System.out.println(R.id.main_btn_start);
        Button start = (Button)findViewById(R.id.main_btn_start);
        System.out.println(start);  /// он не смог найти!!!!
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("StartPressed");
                m_app.setView(ActivityMain.VIEW_GAME);
            }
        });

        Button goto_Logo = (Button)findViewById(R.id.main_btn_toLogo);
        goto_Logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("goto_Logo Pressed");
                m_app.setView(ActivityMain.VIEW_INTRO);
            }
        });*/



    }

    public void start()
    {
        m_isActive 	= true;
        m_handler.sleep(UPDATE_TIME_MS);
    }
    public void stop()
    {
        m_isActive 	= false;
        //m_handler.sleep(UPDATE_TIME_MS);
    }

    public boolean onTouch(int x, int y, int evtType)
    {
        System.out.println("MainMenu OnTouch");
        return true;
    }

    public void update()
    {
        if (!m_isActive)
            return;
        // send next update to game
        if (m_isActive)
            m_handler.sleep(UPDATE_TIME_MS);
    }

    public boolean performClick()
    {
        boolean b = super.performClick();
        return b;
    }

    public Boolean flag = true;
    public void onDraw(Canvas canvas)  /// нужно ли оно сейчас?
    {
        System.out.println("MainMenu onDraw");

    }
}

