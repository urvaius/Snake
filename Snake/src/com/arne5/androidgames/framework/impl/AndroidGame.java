package com.arne5.androidgames.framework.impl;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

import com.arne5.androidgames.framework.Audio;
import com.arne5.androidgames.framework.FileIO;
import com.arne5.androidgames.framework.Game;
import com.arne5.androidgames.framework.Graphics;
import com.arne5.androidgames.framework.Input;
import com.arne5.androidgames.framework.Screen;



public abstract class AndroidGame extends Activity implements Game {
	AndroidFastRenderView renderView;
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	WakeLock wakeLock;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		int frameBufferWidth = isLandscape ? 480 :320;
		int frameBufferHeight= isLandscape ? 320 :480;
		Bitmap frameBuffer =Bitmap.createBitmap(frameBufferWidth,frameBufferHeight, Config.RGB_565);
		
		float scaleX=(float) frameBufferWidth / getWindowManager().getDefaultDisplay().getWidth();
		float scaleY=(float) frameBufferHeight / getWindowManager().getDefaultDisplay().getHeight();
		
		renderView = new AndroidFastRenderView(this, frameBuffer);
		graphics = new AndroidGraphics(getAssets(), frameBuffer);
		fileIO = new AndroidFileIO(this);
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView, scaleX, scaleY);
		screen = getStartScreen();
		setContentView(renderView);
		
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");
		
		@Override
		public void onResume(){
			super.onResume();
			wakeLock.acquire();
			screen.resume();
			renderView.resume();
		}
		@Override
		public void onPause(){
			super.onPause();
			wakeLock.release();
			renerView.pause();
			screen.pause();
			if(isFinishing())
				screen.dispose();
		}
		
		
	}
}