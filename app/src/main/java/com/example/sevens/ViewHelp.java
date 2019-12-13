package com.example.sevens;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

class HelpRefreshHandler extends Handler
{
    ViewHelp m_viewHelp;


    public HelpRefreshHandler(ViewHelp v)
    {
        m_viewHelp = v;
    }

    public void handleMessage(Message msg)
    {
        m_viewHelp.update();
        m_viewHelp.invalidate();
    }

    public void sleep(long delayMillis)
    {
        this.removeMessages(0);
        sendMessageDelayed(obtainMessage(0), delayMillis);
    }

    public void stop() {
        this.removeMessages(0);
    }
}

public class ViewHelp extends View {

    // CONST
    private static final int UPDATE_TIME_MS = 30;

    ActivityMain	m_app;
    //MenuRefreshHandler   m_handler;
    boolean			m_isActive;

    private int m_scrW, m_scrH;
    private int m_scrCenterX, m_scrCenterY;

    private ImageView backButton;
    private TextView title;
    //private TextView help;
    private WebView help;

    boolean flag = true;


    public ViewHelp(ActivityMain app)
    {
        super(app);
        m_app = app;
        //m_handler 	= new MenuRefreshHandler(this);
        m_isActive 	= false;
        setOnTouchListener(app);

        backButton = m_app.findViewById(R.id.help_back_button);
        title = m_app.findViewById(R.id.help_title);
        help = m_app.findViewById(R.id.help_view);
        final WebSettings webSettings = help.getSettings();
        Resources res = getResources();
        float fontSize = res.getDimension(R.dimen.txt_help_size);
        webSettings.setDefaultFontSize((int)fontSize);
        help.setBackgroundColor(Color.TRANSPARENT);
        help.loadUrl(m_app.getString(R.string.help_info));
        //help = m_app.findViewById(R.id.help_text);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("backButton Pressed");
                close();
            }
        });
    }

    public void start()
    {
        m_app.soundBox.playBackSound();
        m_isActive 	= true;
        System.out.println("Help start()");
        //m_handler.sleep(UPDATE_TIME_MS);
    }
    public void stop()
    {
        System.out.println("Help stop");
        m_app.soundBox.pauseBackSound();
        m_isActive 	= false;
        //m_handler.sleep(UPDATE_TIME_MS);
    }

    public void pause()
    {
        System.out.println("Help pause");
        m_isActive 	= false;
        m_app.soundBox.pauseBackSound();
        //m_handler.sleep(UPDATE_TIME_MS);
    }

    public void resume()
    {
        System.out.println("Help resume");
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
        return super.performClick();
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
