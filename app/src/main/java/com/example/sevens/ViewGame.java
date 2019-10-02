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



//****************************************************************
//class RefreshHandler
//****************************************************************
class RefreshHandler extends Handler 
{
	ViewGame	m_viewGame;
	
	public RefreshHandler(ViewGame v)
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

	public ViewGame(ActivityMain app)
	{
		super(app);


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

	
}
