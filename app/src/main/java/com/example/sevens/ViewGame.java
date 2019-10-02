package com.example.sevens;

import java.util.Random;

import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.graphics.Paint.Align;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


//****************************************************************
//class RefreshHandler
//****************************************************************
class GameRefreshHandler extends Handler
{
	ViewGame	m_viewGame;
	
	public GameRefreshHandler(ViewGame v)
	{
		m_viewGame = v;
	}

	public void handleMessage(Message msg) 
	{
		m_viewGame.update();
		m_viewGame.invalidate();
	}
	
	public void sleep(long delayMillis) 
	{
		this.removeMessages(0);
	    sendMessageDelayed(obtainMessage(0), delayMillis);
	}
};

public class ViewGame extends View
{

	ActivityMain	m_app;
	//GameRefreshHandler   m_handler;
	boolean			m_isActive;

	public ViewGame(ActivityMain app)
	{
		super(app);
		m_app = app;
		//m_handler 	= new MenuRefreshHandler(this);
		m_isActive 	= false;
		setOnTouchListener(app);

		Button back = (Button) m_app.findViewById(R.id.game_btn_back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("Game backPressed");
				close();
				//m_app.setView(ActivityMain.VIEW_MAIN_MENU);
			}
		});




	}

	private void gameRestart()
	{
	}
	
	public boolean performClick()
	{
		boolean b = super.performClick();
		return b;
	}
	public void start()
	{	}
	public void onPause()
	{	}
	public void onDestroy()
	{	}
	public void update()
	{	}

	
	private void soundOnBuild(int numIcons)
	{	}


	private boolean isPossibleThree()
	{
		return true;
	}

	
	public boolean onTouch(int x, int y, int evtType)
	{
		System.out.println("Game OnTouch");
		return true;	}


	private void drawButton(Canvas canvas, RectF rectIn, String str, int color1, int color2, int alpha)
	{	}
	

	private void prepareScreenValues(Canvas canvas)
	{	}
	
	private void drawBackground(Canvas canvas, int opacityBackground)
	{}

	private void close()
	{
		if(backPressedTime + backDoublePressedInterval > System.currentTimeMillis())
		{
			m_app.setView(ActivityMain.VIEW_MAIN_MENU);
			backToast.cancel();
			return;
		}
		else
		{
			backToast = Toast.makeText(m_app.getBaseContext(), R.string.game_back_toast_text, Toast.LENGTH_SHORT);
			backToast.show();
		}
		backPressedTime = System.currentTimeMillis();
	}
	// обработка системной кнопки "Назад" - начало    // ну
	private long backPressedTime;
	private long backDoublePressedInterval = 2000;
	private Toast backToast;
	public void onBackPressed() {
		close();
	}

	// обработка системной кнопки "Назад" - конец

	
}
