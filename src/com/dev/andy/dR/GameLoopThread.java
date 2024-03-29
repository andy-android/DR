package com.dev.andy.dR;

import android.graphics.Canvas;

public class GameLoopThread extends Thread{
	private GameView view;

	private final static int 	MAX_FPS = 60;

	private final static int	MAX_FRAME_SKIPS = 5;

	private final static int	FRAME_PERIOD = 1000 / MAX_FPS;
	
	boolean running = false;

	public GameLoopThread(GameView view){
		this.view = view;
	}
	
	public void setRunning(){
		running = true;
	}
	@Override
	public void run() {
		Canvas canvas;
		long beginTime;		
		long timeDiff;		
		int sleepTime;		
		int framesSkipped;

		sleepTime = 0;

		while (running) {
			canvas = null;
	
			try {
				canvas = view.holder.lockCanvas();
				synchronized (view.holder) {
					beginTime = System.currentTimeMillis();
					framesSkipped = 0;
					this.view.onDraw(canvas);

					timeDiff = System.currentTimeMillis() - beginTime;

					sleepTime = (int)(FRAME_PERIOD - timeDiff);

					if (sleepTime > 0) {

						try {

							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {}
					}

					while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {


						this.view.update();

						sleepTime += FRAME_PERIOD;
						framesSkipped++;
					}
				}
			} finally {

				if (canvas != null) {
					view.holder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
}