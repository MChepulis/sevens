package com.example.sevens;

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;

//import android.os.Handler;
import android.view.View;



//****************************************************************
//class RefreshHandler
//****************************************************************
class RedrawHandler extends Handler 
{
	ViewIntro m_viewIntro;
	
	public RedrawHandler(ViewIntro v)
	{
		System.out.println("RedrawHandler C-TOR");
		m_viewIntro = v;
	}

	public void handleMessage(Message msg)
	{
		m_viewIntro.update();
		m_viewIntro.invalidate();
	}
	
	public void sleep(long delayMillis) 
	{
		this.removeMessages(0);
	    sendMessageDelayed(obtainMessage(0), delayMillis);
	}
	public void stop() {
		this.removeMessages(0);
	}
	public void start() {
		this.sendEmptyMessage(0);
	}

}

public class ViewIntro extends View 
{
	// CONST
	private static final int UPDATE_TIME_MS = 30; 
	

	// DATA
	ActivityMain	m_app;
	RedrawHandler   m_handler;
	long			m_startTime;
	int				m_lineLen;
	boolean			m_active;
	AppIntro		appIntro;
	
	// METHODS
	public ViewIntro(ActivityMain app)
	{
		super(app);
		System.out.println("ViewIntro C-TOR");
		m_app = app;
		
		m_handler 	= new RedrawHandler(this);
		m_startTime = 0;
		m_lineLen 	= 0;
		m_active 	= false;
		appIntro =  new AppIntro(m_app, 0);  //// тут задаётся язык для интро, но в интро он не используется, потому просто заглушка
		setOnTouchListener(app);
	}
	public boolean performClick()
	{
		System.out.println("ViewIntro performClick");
		return super.performClick();
	}
	
	public void start()
	{
		System.out.println("ViewIntro start");
		m_active 	= true;
		m_handler.start();
	}
	public void stop()
	{
		System.out.println("ViewIntro stop");
		m_active 	= false;
		m_handler.stop();
	}

	public void pause()
	{
		m_handler.stop();
	}

	public void resume()
	{
		m_handler.start();
	}
	
	public void update()
	{
		if (!m_active)
			  return;
		// send next update to game
		if (m_active)
			m_handler.sleep(UPDATE_TIME_MS);
	}

	public void onBackPressed() {
		m_app.returnToPrevView();
		return;
	}
	/*
	public boolean onTouch(int x, int y, int evtType)
	{
		System.out.println("ViewIntro OnTouch");
		AppIntro app = m_app.getApp();
		return app.onTouch(x,  y, evtType);
	}
	public void onConfigurationChanged(Configuration confNew)
	{
		System.out.println("ViewIntro onConfigurationChanged");
		AppIntro app = m_app.getApp();
		if (confNew.orientation == Configuration.ORIENTATION_LANDSCAPE)
			app.onOrientation(AppIntro.APP_ORI_LANDSCAPE);
		if (confNew.orientation == Configuration.ORIENTATION_PORTRAIT)
			app.onOrientation(AppIntro.APP_ORI_PORTRAIT);
	}
	public void onDraw(Canvas canvas)
	{
		AppIntro app = m_app.getApp();
		app.drawCanvas(canvas);
		//appIntro.drawCanvas(canvas);
	}
	*/



	public boolean onTouch(int x, int y, int evtType)
	{
		System.out.println("ViewIntro OnTouch");
		return appIntro.onTouch(x,  y, evtType);
	}
	public void onConfigurationChanged(Configuration confNew)
	{
		System.out.println("ViewIntro onConfigurationChanged");
		if (confNew.orientation == Configuration.ORIENTATION_LANDSCAPE)
			appIntro.onOrientation(AppIntro.APP_ORI_LANDSCAPE);
		if (confNew.orientation == Configuration.ORIENTATION_PORTRAIT)
			appIntro.onOrientation(AppIntro.APP_ORI_PORTRAIT);
	}
	public void onDraw(Canvas canvas)
	{
		appIntro.drawCanvas(canvas);
	}



}
