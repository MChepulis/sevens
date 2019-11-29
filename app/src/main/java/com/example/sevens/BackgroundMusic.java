package com.example.sevens;

import android.media.*;
import android.os.*;
import android.util.*;

public class BackgroundMusic extends AsyncTask<Void, Void, Void> 
{
	ActivityMain		m_activity;
	MediaPlayer 		m_player;
	boolean				m_started = false;
	
	public void create(ActivityMain act)
	{
		m_activity 	= act;
		m_player 	= null;
		m_started 	= false;
	}
	public boolean isStarted()
	{
		return m_started;
	}
	public void start()
	{
		if (m_player == null)
			return;
		
		if (!m_started)
		{
			m_player.start();
			Log.d("THREE", "m_player.start()");
		}
		m_started = true;
	}
	public void resume()
	{
		if (!m_started)
		{
			m_player.start();
			Log.d("THREE", "m_player.start()");
		}
		m_started = true;
	}
	
	public void pause()
	{
		m_player.pause();
		m_started = false;
		Log.d("THREE", "m_player.pause()");
	}
	public void stop()
	{
		m_player.stop();
		m_started = false;
		Log.d("THREE", "m_player.stop()");
	}
	MediaPlayer getMediaPlayer()
	{
		return m_player;
	}
	

    protected Void doInBackground(Void... params) 
    {
    	if (m_player == null)
    	{
    		m_player = MediaPlayer.create(m_activity, R.raw.bamboo);
    		m_player.setLooping(true); // Set looping 
    		m_player.setVolume(100,100);
    		start();
    	}

        return null;
    }

}
