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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class SettingsRefreshHandler extends Handler
{
    ViewSettings m_viewSettings;


    public SettingsRefreshHandler(ViewSettings v)
    {
        m_viewSettings = v;
    }

    public void handleMessage(Message msg)
    {
        m_viewSettings.update();
        m_viewSettings.invalidate();
    }

    public void sleep(long delayMillis)
    {
        this.removeMessages(0);
        sendMessageDelayed(obtainMessage(0), delayMillis);
    }

    public void stop() {
        this.removeMessages(0);
    }
};

public class ViewSettings extends View {

    // CONST
    private static final int UPDATE_TIME_MS = 30;

    ActivityMain	m_app;
    //MenuRefreshHandler   m_handler;
    boolean			m_isActive;

    private int m_scrW, m_scrH;
    private int m_scrCenterX, m_scrCenterY;
    private ImageView background;
    private ViewSoundButton sound;
    private ViewMusicButton music;
    private Button backButton;
    private TextView title;

    private Button for_stuff;
    boolean flag = true;


    public ViewSettings(ActivityMain app)
    {
        super(app);
        m_app = app;
        //m_handler 	= new MenuRefreshHandler(this);
        m_isActive 	= false;
        setOnTouchListener(app);

        backButton = m_app.findViewById(R.id.settings_back_button);
        background = m_app.findViewById(R.id.settings_background);
        sound = m_app.findViewById(R.id.settings_sound);
        music = m_app.findViewById(R.id.settings_music);
        title = m_app.findViewById(R.id.settings_title);
        for_stuff = m_app.findViewById(R.id.settings_for_Stuff);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("backButton Pressed");
                close();
            }
        });

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.onClick(v);
            }
        });


        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                music.onClick(v);
            }
        });


        for_stuff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("flag = " + flag);

                if(flag) {
                    //m_app.soundBox.resumeBackSound();
                    m_app.soundBox.playJumpSound();
                   // m_app.soundBox.resumeAllSound();
                }

                else {
                    m_app.soundBox.pauseAllSound();
                    //m_app.soundBox.pauseBackSound();
                }

                flag = !flag;
            }
        });
    }

    public void start()
    {
        m_app.soundBox.playBackSound();
        m_isActive 	= true;
        System.out.println("Settings start()");
        //m_handler.sleep(UPDATE_TIME_MS);
    }
    public void stop()
    {
        System.out.println("Settings stop");
        m_app.soundBox.pauseBackSound();
        m_isActive 	= false;
        //m_handler.sleep(UPDATE_TIME_MS);
    }

    public void pause()
    {
        System.out.println("Settings pause");
        m_isActive 	= false;
        m_app.soundBox.pauseBackSound();
        //m_handler.sleep(UPDATE_TIME_MS);
    }

    public void resume()
    {
        System.out.println("Settings resume");
        m_isActive 	= true;
        m_app.soundBox.resumeBackSound();
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
        if (m_isActive) {
            //m_handler.sleep(UPDATE_TIME_MS);
        }
    }

    public boolean performClick()
    {
        boolean b = super.performClick();
        return b;
    }

    public void onDraw(Canvas canvas)  /// нужно ли оно сейчас?
    {
        System.out.println("MainMenu onDraw");

    }

    public void close()
    {
        stop();
        m_app.returnToPrevView();
        //m_app.setView(ActivityMain.VIEW_MAIN_MENU);
    }

    // обработка системной кнопки "Назад" - начало
    public void onBackPressed() {
        close();
        return;
    }

    // обработка системной кнопки "Назад" - конец
}

