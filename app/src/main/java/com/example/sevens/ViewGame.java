package com.example.sevens;

import java.util.Random;

import android.content.ClipData;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.graphics.Paint.Align;

import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;


//****************************************************************
//class RefreshHandler
//****************************************************************
class GameRefreshHandler extends Handler
{
	ViewGame	m_viewGame;
	
	public GameRefreshHandler(ViewGame v) {
		System.out.println("GameRefreshHandler C-TOR");
		m_viewGame = v;
	}

	public void handleMessage(Message msg) {
		System.out.println("handleMessage ");
		m_viewGame.update();
		m_viewGame.invalidate();
	}
	
	public void sleep(long delayMillis) {
		System.out.println("sleep");
		this.removeMessages(0);
	    sendMessageDelayed(obtainMessage(0), delayMillis);
	}

	public void stop() {
		this.removeMessages(0);
	}
};

public class ViewGame extends View {
	private static final int UPDATE_TIME_MS = 30;
	ActivityMain m_app;
	GameRefreshHandler m_handler;
	boolean m_isActive;


	ConstraintLayout main_layout;
	LinearLayout top_layout;
	ConstraintLayout mid_layout;
	LinearLayout bot1_layout;
	LinearLayout bot2_layout;
	LinearLayout bot1_right_layout;
	LinearLayout bot1_middle_layout;
	LinearLayout bot1_left_layout;

	Button back;

	ImageView trash_bin;
	ImageView background;

	HexagonGrid hexGrid;
	HexagonGrid newHexGrid;


	boolean flag = true;

	private long backPressedTime;
	private long backDoublePressedInterval = 2000;
	private Toast backToast;

	public ViewGame(ActivityMain app) {
		super(app);
		m_app = app;
		m_handler 	= new GameRefreshHandler(this);
		m_isActive = false;
		setOnTouchListener(app);


		back = (Button) m_app.findViewById(R.id.game_btn_back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("Game backPressed");

				close();
			}
		});

		trash_bin = (ImageView) m_app.findViewById(R.id.game_trash_bin);
		trash_bin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag) {
					m_handler.sendEmptyMessage(1);
					flag = false;
				}
				else {
					m_handler.stop();
					flag = true;
				}
			}
		});
		top_layout = m_app.findViewById(R.id.game_top_layout);


		mid_layout = m_app.findViewById(R.id.game_middle_layout);
		hexGrid = new HexagonGrid(m_app);
		hexGrid.setGridParams(5, 5);
		hexGrid.deleteHex(0, 0);
		hexGrid.deleteHex(0, 4);
		hexGrid.deleteHex(4, 0);
		hexGrid.deleteHex(4, 4);
		mid_layout.addView(hexGrid);
		hexGrid.setOnDragListener(new HexGridDragDropDragListener(m_app.getApplicationContext()));


		bot1_layout = m_app.findViewById(R.id.game_bot1_layout);
		bot1_middle_layout = m_app.findViewById(R.id.game_bot1_middle_layout);

		newHexGrid = new HexagonGrid(m_app);
		newHexGrid.setGridParams(2, 2);
		newHexGrid.setTag("hexGrid2");
		newHexGrid.deleteHex(0, 0);
		bot1_middle_layout.addView(newHexGrid);

		hexGrid.synchronizeRadius(newHexGrid);
		newHexGrid.synchronizeRadius(hexGrid);
		newHexGrid.setOnTouchListener(new HexGridDragDropOnTouchListener());



	}

	void OnTouchListener() {

	}



	private void init() {
	}

	private void gameRestart() {
	}

	public boolean performClick() {
		boolean b = super.performClick();
		return b;
	}

	public void onPause() {
	}

	public void onDestroy() {
	}

	public void onDraw(Canvas canvas)  /// нужно ли оно сейчас?
	{
		System.out.println("Game onDraw");
		//main_layout.draw(canvas);
	}

	private void soundOnBuild(int numIcons) {
	}

	private boolean isPossibleThree() {
		return true;
	}


	public boolean onTouch(int x, int y, int evtType) {
		System.out.println("Game OnTouch");
		return true;
	}


	private void drawButton(Canvas canvas, RectF rectIn, String str, int color1, int color2, int alpha) {
	}

	private void prepareScreenValues(Canvas canvas) {
	}

	private void close() {
		if (backPressedTime + backDoublePressedInterval > System.currentTimeMillis()) {
			m_app.setView(ActivityMain.VIEW_MAIN_MENU);
			backToast.cancel();
			return;
		} else {
			backToast = Toast.makeText(m_app.getBaseContext(), R.string.game_back_toast_text, Toast.LENGTH_SHORT);
			backToast.show();
		}
		backPressedTime = System.currentTimeMillis();
	}

	// обработка системной кнопки "Назад" - начало
	public void onBackPressed() {
		close();
	}


	public void start() {
		System.out.println("Game start");
		m_isActive = true;
		init();
		//m_handler.sleep(UPDATE_TIME_MS);
	}

	public void stop() {
		System.out.println("Game stop");
		m_isActive = false;
		//m_handler.sleep(UPDATE_TIME_MS);
	}

	public void update() {
		System.out.println("Game update");
		if (!m_isActive)
			return;
		// send next update to game
		if (m_isActive)
			m_handler.sleep(UPDATE_TIME_MS);
	}

	public void onConfigurationChanged(Configuration confNew) {
		System.out.println("ViewIntro onConfigurationChanged");
	}

}
