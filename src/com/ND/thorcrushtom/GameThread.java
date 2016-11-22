package com.ND.thorcrushtom;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread{
	private boolean running;
	private SurfaceHolder surfaceHolder;
	private GamePanel gamePanel;
	public int fps= Constant.FPS_VALUE; 
	private int loopPeriod=1000/fps; 
	   
	public GameThread(SurfaceHolder surfaceHolder, GamePanel gamePanel){
		super();
		this.surfaceHolder=surfaceHolder;
		this.gamePanel=gamePanel;
	}
	
	public void setRunning(boolean running){
		this.running=running;
	}

	@Override
	public void run() {
		Canvas canvas;
		long beginTime; 
		long endTime; 
		long deltaTime; 
		long sleepTime; 
		
		while(running){
			canvas=null;
			try{
			   canvas=this.surfaceHolder.lockCanvas();
			   synchronized(this.surfaceHolder){
				   beginTime=System.currentTimeMillis(); 
				   this.gamePanel.updateStatus();
				   this.gamePanel.draw(canvas);
				   endTime=System.currentTimeMillis(); 
				   deltaTime=endTime-beginTime; 
				   sleepTime=loopPeriod-deltaTime; 
				   try{
					if(sleepTime>0) 
					   Thread.sleep(sleepTime); 
				   } catch(InterruptedException ex){
					ex.printStackTrace();
				   }
			   }
			} finally {
			   if(canvas!=null)
				this.surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
}
