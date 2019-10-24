package com.example.sevens;

import java.util.ArrayList;
import java.util.Random;

import android.content.ClipData;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.arch.core.util.Function;
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
		//System.out.println("handleMessage ");
		m_viewGame.update();
		m_viewGame.invalidate();
	}
	
	public void sleep(long delayMillis) {
		//System.out.println("sleep");
		this.removeMessages(0);
	    sendMessageDelayed(obtainMessage(0), delayMillis);
	}

	public void stop() {
		this.removeMessages(0);
	}
	public void start() {
		this.sendEmptyMessage(0);
	}


};

public class ViewGame extends View {
	private static final int UPDATE_TIME_MS = 30;
	ActivityMain m_app;
	GameRefreshHandler m_handler;
	boolean m_isActive;


	ConstraintLayout main_layout;
	LinearLayout L_main_layout;
	LinearLayout top_layout;
	LinearLayout game_over_layout;
	ConstraintLayout mid_layout;
	LinearLayout bot1_layout;
	LinearLayout bot2_layout;
	LinearLayout bot1_right_layout;
	LinearLayout bot1_middle_layout;
	LinearLayout bot1_left_layout;
	HexGridDragDropDragListener dragListener;
	TextView game_over_Text;
	Button game_over_btn_try_again;
	Button game_over_btn_main_menu;
	Button staff_only_btn;
	LinearLayout game_over_text_layout;
	LinearLayout game_over_button_layout;


	Button back;

	ImageView trash_bin;
	ImageView background;

	HexagonGrid hexGrid;
	HexagonGrid puzzle;
	boolean isNeedGenerateNewPuzzle;


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
		isNeedGenerateNewPuzzle = true;


		back = (Button) m_app.findViewById(R.id.game_btn_back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("Game backPressed");
				closeMessage();
			}
		});

		trash_bin = (ImageView) m_app.findViewById(R.id.game_trash_bin);
		trash_bin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setNeedGenerateNewPuzzle();
				//GameOver();
			}
		});
		L_main_layout = m_app.findViewById(R.id.game_main_layout);
		top_layout = m_app.findViewById(R.id.game_top_layout);
		mid_layout = m_app.findViewById(R.id.game_middle_layout);
		bot1_layout = m_app.findViewById(R.id.game_bot1_layout);
		bot2_layout = m_app.findViewById(R.id.game_bot2_layout);
		bot1_middle_layout = m_app.findViewById(R.id.game_bot1_middle_layout);
		game_over_layout = m_app.findViewById(R.id.game_over_layout);
		game_over_Text =  m_app.findViewById(R.id.game_over_text);
		game_over_btn_try_again =  m_app.findViewById(R.id.game_over_btn_try_again);
		game_over_btn_main_menu =  m_app.findViewById(R.id.game_over_btn_main_menu);
		game_over_text_layout =  m_app.findViewById(R.id.game_over_text_layout);
		game_over_button_layout =  m_app.findViewById(R.id.game_over_button_layout);
		staff_only_btn = m_app.findViewById(R.id.game_staff_only);

		game_over_btn_main_menu.setOnClickListener( new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				close();
			}
		});

		game_over_btn_try_again.setOnClickListener( new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				gameRestart();
				closeGameOverDialog();
			}
		});

		staff_only_btn.setOnClickListener( new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				GameOver();
			}
		});

		game_over_layout.setVisibility(INVISIBLE);

		hexGrid = new HexagonGrid(m_app);
		hexGrid.setGridParams(5, 5);
		hexGrid.deleteHex(0, 0);
		hexGrid.deleteHex(0, 4);
		hexGrid.deleteHex(4, 0);
		hexGrid.deleteHex(4, 4);
		mid_layout.addView(hexGrid);
		dragListener = new HexGridDragDropDragListener(m_app.getApplicationContext());
		dragListener.registeredCallBackFunk(new Function<Void, Void>() {
			@Override
			public Void apply(Void input) {
				setNeedGenerateNewPuzzle();
				return null;
			}
		});

		hexGrid.setOnDragListener(dragListener);

		puzzle = new HexagonGrid(m_app);
		puzzle.setGridParams(2, 2);
		puzzle.setTag("hexGrid2");
		puzzle.deleteHex(0, 0);
		bot1_middle_layout.addView(puzzle);

		hexGrid.synchronizeRadius(puzzle);
		puzzle.synchronizeRadius(hexGrid);
		puzzle.setOnTouchListener(new HexGridDragDropOnTouchListener());
	}


	int rotateCount;
	private void rotatePuzzle()
	{

		switch (rotateCount) {
			case 0:
				puzzle.swapHexagon(0, 1, 1, 0);
				break;
			case 1:
				puzzle.swapHexagon(0, 0, 0, 1);
				break;
			case 2:
				puzzle.swapHexagon(1, 0, 0, 0);
				break;
		}
		puzzle.OnCenter();
		rotateCount = (rotateCount + 1) % 3;
		System.out.println("rotateCount " + rotateCount);
	}


	public void setNeedGenerateNewPuzzle() {
		isNeedGenerateNewPuzzle = true;
	}

	// FIXME нужно сделать болле интересный генератор
	private int getNewState()
	{
		Random rand = new Random();
		int state = rand.nextInt(Hexagon.getMaxState());
		return state + 1;  //т.к. state==0б не имеет смысла и заодно включаем верхнюю границу
	}

	private int getNewAmount(int maxAmount)
	{
		Random rand = new Random();
		int result = rand.nextInt(maxAmount);
		return result;
	}


	private int getQuatityForPuzzle()
	{
		for( int i = 2; i > 0; i --)
		{
			if(hexGrid.IsCanPlace(i))
			{
				return i;
			}
		}
		return 0;

	}
	private void generatePuzzle() {

		isNeedGenerateNewPuzzle = false;
		puzzle.clearTrimedArr();

		Random rand = new Random();

		int quantity = getQuatityForPuzzle();
		if (quantity == 0) {
			GameOver();
			quantity = 1;
		}
		int amount = getNewAmount(quantity);
		int cur_order = rand.nextInt(3);
		switch (cur_order) {
			case 0:
				rotateCount = 0;
				break;
			case 1:
				rotateCount = 2;
				break;
			case 2:
				rotateCount = 1;
				break;
		}
		for (int i = 0; i < amount + 1; i++) {

			int newState = getNewState();
			//int newState = 6;
			switch (cur_order) {
				case 0:
					puzzle.setHexagonState(0, 0, newState);
					break;
				case 1:
					puzzle.setHexagonState(0, 1, newState);
					break;
				case 2:
					puzzle.setHexagonState(1, 0, newState);
					break;
			}
			cur_order = (cur_order + 1) % 3;
		}
		for (int i = amount + 1; i < 3; i++) {
			switch (cur_order) {
				case 0:
					puzzle.deleteHex(0, 0);
					break;
				case 1:
					puzzle.deleteHex(0, 1);
					break;
				case 2:
					puzzle.deleteHex(1, 0);
					break;
			}
			cur_order = (cur_order + 1) % 3;
		}
		puzzle.OnCenter();
		puzzle.invalidate();
	}


	boolean flag1 = true;

	private void openGameOverDialog()
	{
		game_over_layout.setVisibility(VISIBLE);
		pause();
		flag1 = false;

		//FIXME задать размер текста
		//FIXME кнопки
	}

	private void closeGameOverDialog()
	{
		game_over_layout.setVisibility(INVISIBLE);
		resume();
		flag1 = true;
	}
	public void GameOver() {
		System.out.println("Game Over");
		if(flag1) {
			openGameOverDialog();
		}
		else{
			closeGameOverDialog();
		}


	}
	private void init() {
	}

	private void gameRestart() {
		hexGrid.RefreshAllHexagonsState();

		isNeedGenerateNewPuzzle = true;
		generatePuzzle();
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


	public boolean onTouch(int x, int y, int evtType) {
		System.out.println("Game OnTouch");
		return true;
	}


	private void drawButton(Canvas canvas, RectF rectIn, String str, int color1, int color2, int alpha) {
	}

	private void prepareScreenValues(Canvas canvas) {
	}

	private void close()
	{
		m_isActive = false;
		stop();
		m_app.setView(ActivityMain.VIEW_MAIN_MENU);
	}
	private void closeMessage() {
		if (backPressedTime + backDoublePressedInterval > System.currentTimeMillis()) {
			close();
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
		closeMessage();
	}


	public void pause() {
		System.out.println("Game pause");
		puzzle.setEnabled(false);
		back.setEnabled(false);
		trash_bin.setEnabled(false);
		m_handler.stop();
	}

	public void resume() {
		System.out.println("Game  resume");
		puzzle.setEnabled(true);
		back.setEnabled(true);
		trash_bin.setEnabled(true);
		m_handler.start();
	}


	public void start() {
		System.out.println("Game start");
		m_isActive = true;
		m_handler.start();

		init();
		//m_handler.sleep(UPDATE_TIME_MS);
	}

	public void stop() {
		System.out.println("Game stop");
		m_handler.stop();
		m_isActive = false;  //?????????????????????????????????????????
		//m_handler.sleep(UPDATE_TIME_MS);
	}

	public void update() {
		//System.out.println("Game update");
		if (!m_isActive)
			return;
		// send next update to game
		if (m_isActive) {
			if(isNeedGenerateNewPuzzle) {
				generatePuzzle();
			}
			m_handler.sleep(UPDATE_TIME_MS);
		}
	}

	public void onConfigurationChanged(Configuration confNew) {
		System.out.println("ViewIntro onConfigurationChanged");
	}

	public class  HexGridDragDropOnTouchListener implements View.OnTouchListener {

		MyDragShadowBuilder dragShadowBuilder;
		HexGridDragDropOnTouchListener()
		{
			super();
		}
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {


			System.out.println("event " + motionEvent.getAction());
			if (motionEvent.getAction() == motionEvent.ACTION_DOWN) {

				// Create drag shadow builder object.
				float tmp_x = motionEvent.getX() > 0? motionEvent.getX():0;
				float tmp_y = motionEvent.getY() > 0? motionEvent.getY():0;

				dragShadowBuilder = new MyDragShadowBuilder(view);
				dragShadowBuilder.setOffsets(tmp_x, tmp_y );
				//dragShadowBuilder.setOffsets( view.getWidth() / 2,  view.getHeight() / 2);

				((HexagonGrid) view).dragTouchCoord_x = tmp_x;
				((HexagonGrid) view).dragTouchCoord_y = tmp_y ;
				System.out.println(motionEvent.getX() + " " + motionEvent.getY());
				((HexagonGrid) view).isDragTouchCoordSet = true;
			}
			if (motionEvent.getAction() == motionEvent.ACTION_UP) {
				rotatePuzzle();
				view.setVisibility(View.VISIBLE);
				return true;
			}
			if (motionEvent.getAction() == motionEvent.ACTION_MOVE) {
				// Get view object tag value.
				String tag = (String) view.getTag();

				// Create clip data.
				ClipData clipData = ClipData.newPlainText("", tag);


        	/* Invoke view object's startDrag method to start the drag action.
          	 	clipData : to be dragged data.
           		dragShadowBuilder : the shadow of the dragged view.
        	*/
				view.startDrag(clipData, dragShadowBuilder, view, 0);


				// Hide the view object because we are dragging it.
				view.setVisibility(View.INVISIBLE);

				return true;
			}
			return true;
		}
	}


}
