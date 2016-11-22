package com.ND.thorcrushtom;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import characters.ThorHammer;
import characters.Tom;
import characters.TomManage;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
	GameThread gameThread;
	//Coordinate Values:
	int scoreXCoord;
	int scoreYCoord;
	int try_again_button_x;
	int try_again_button_y;
	int play_button_x;
	int play_button_y;
	//others value
	int tom_index = 0;
	int score = 0;
	String _status_game = Constant.STATUS_TITLE;
	//level related variables
	int score_increment = Constant.GAME_SCORE_INCREMENT;
	int random_gap = Constant.DEFAULT_RANDOM_GAP;
	int appearance_limit = 2;
	int default_appearanceDuration = Constant.DEFAULT_APPEARANCE_DURATION; //in millisecond
	int level = 0;
	//temporary used variables
	int temp_level =0;
	int curr_random_count = 0;
	//Bitmaps
	Bitmap mBackground;
	Bitmap mTitle;
	Bitmap mGameOver;
	Bitmap mScaledBackground;
	Bitmap mScaledTitle;
	Bitmap mScaledGameOver;
	Bitmap mButton_tryAgain;
	Bitmap mButton_play;
	//background scaled and non-scaled width and height [in pixel]
	int mBackWidth = 1080;
	int mBackHeight = 1920;
	//characters Bitmaps
	ThorHammer mThor;
	TomManage mTomManager;
	Bitmap mThorHammer;
	Bitmap mTomNormal;
	Bitmap mTomHit;
	Bitmap mTomInvicible;
	//phone screen width and height
	int pWidth;
	int pHeight;
	//value of starter coordinate
	int xStart = 100;
	int yStart = 100;
	//rectangle panel for animating sprite
	Rect sourceRect;
	Rect tomSourceRect;
	//application activity
	Context con;
	//music
	private SoundPool soundpool;
	private int hitSoundId;
	private MediaPlayer mp;
	/*
	 * @param width contains the width of phone screen value
	 * @param height contains the height of phone screen value
	 */
	@SuppressWarnings("deprecation")
	public GamePanel(Context context, int width, int height) {
		super(context);
		getHolder().addCallback(this);
		soundpool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);		
		hitSoundId = soundpool.load(getContext(), R.raw.bam, 1);
		gameThread= new GameThread(getHolder(), this);
		this.con = context;
		this.pHeight = height;
		this.pWidth = width;
		this.scoreXCoord =(int)(this.pWidth/2 - (this.pWidth/4));
		this.scoreYCoord = (int)(this.pHeight/2 - (0.25*this.pHeight));
		this.mBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background_portrait);
		this.mTitle = BitmapFactory.decodeResource(getResources(), R.drawable.title);
		this.mGameOver = BitmapFactory.decodeResource(getResources(), R.drawable.gameover);
		this.mScaledBackground= Bitmap.createScaledBitmap(mBackground, width, height, false);
		this.mScaledTitle= Bitmap.createScaledBitmap(mTitle, width, height, false);
		this.mScaledGameOver= Bitmap.createScaledBitmap(mGameOver, width, height, false);
		this.mButton_tryAgain = BitmapFactory.decodeResource(getResources(), R.drawable.try_again_button);
		this.mButton_play = BitmapFactory.decodeResource(getResources(), R.drawable.play_button);		
		this.mThorHammer = BitmapFactory.decodeResource(getResources(), R.drawable.hammer);
		this.mTomNormal = BitmapFactory.decodeResource(getResources(), R.drawable.tom_normal);
		this.mTomHit = BitmapFactory.decodeResource(getResources(), R.drawable.tom_hit);
		this.mTomInvicible = BitmapFactory.decodeResource(getResources(), R.drawable.invicible_tom);
		
		this.mThor = new ThorHammer(this.mThorHammer, soundpool, hitSoundId, xStart, yStart);
		this.mTomManager = new TomManage(mTomNormal, mTomHit, mTomInvicible, default_appearanceDuration, width, height);
		//button coordinate
		this.try_again_button_x = ((this.pWidth/2) - (mButton_tryAgain.getWidth()/2)); 
		this.try_again_button_y = ((this.pHeight/2) - (mButton_tryAgain.getHeight()/2));
		this.play_button_x = ((this.pWidth) - (mButton_play.getWidth())); 
		this.play_button_y = (this.pHeight/2);
		
		mp = MediaPlayer.create(getContext(), R.raw.main);
		mp.setLooping(true);
		mp.start();
		this.sourceRect=new Rect(0, 0, this.mThor.getmSpriteWidth(), 
									this.mThor.getmSpriteHeight());
		this.tomSourceRect=new Rect(0, 0, this.mTomManager.toms[0].getSpriteWidth(), 
									this.mTomManager.toms[0].getSpriteHeight());
		setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		gameThread.setRunning(true);
		gameThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		boolean ulang=true; 
		while(ulang){	
			try{
				gameThread.join(); 
				ulang=false; 
			} catch(InterruptedException ex) { }
		}
	}
	/*function: random number generator:int
	 *
	 * @param limit: set the random range 
	 */
	private int generateRandomNumber(int limit){
		int result = 0;
		float temp_res = (float)(Math.random() * limit);
		result = Math.round(temp_res);
		return result;
	}
	public void updateStatus(){
		//check if it's time to generate random number
		if (this.curr_random_count >= this.random_gap && this._status_game == Constant.STATUS_LIVE){
			//appear_index used to keep track of random generated number
			int appear_index;
			//generate random number to get tomManager index
			do{	
				//assign random number for index
				appear_index = this.generateRandomNumber(8);
			}while(this.mTomManager.toms[appear_index].getStatus() == Constant.STATUS_APPEAR
					&& this.mTomManager.get_appearance_count() < this.appearance_limit);
			
			if(this.mTomManager.get_appearance_count() < this.appearance_limit){
				//set tom status at [appear_index] to appear
				this.mTomManager.toms[appear_index].setStatus(Constant.STATUS_APPEAR);
			}
			this.curr_random_count = 0; //reset random number delay timer to zero
		}
		//check hit: {condition: index is not 0 [missed] and still living
		if(this.tom_index != 0 && this._status_game == Constant.STATUS_LIVE){
			//recheck tom condition
			Tom curr_tom = this.mTomManager.toms[this.tom_index-1];
			//second check of collision: checking if tom is appeared to be hit :lol
			if (curr_tom.getStatus() == Constant.STATUS_APPEAR) {
				this.mTomManager.toms[this.tom_index-1].hit();
				this.score+= this.score_increment;
				//check score for level upgrade
				this.temp_level = this.getLevel(this.score);
				//check level change
				if(temp_level != this.level){
					//level up some value in game [level; n-tom; tom-appearance speed; tom-
					//appearance duration]
					this.setLevelAttribute(temp_level);
				}
			}
			this.tom_index = 0;
		}
		//end of check hit
		this.curr_random_count++;
		
		//check score to level up the game
		this.mThor.animate();
		this.mTomManager.animate();
		this.sourceRect.left= this.mThor.getActiveFrame()*this.mThor.getmSpriteWidth();
		this.sourceRect.right= sourceRect.left+this.mThor.getmSpriteWidth();
		//check game over
		if (this.mTomManager.isMissed()) {
			this._status_game = Constant.STATUS_GAMEOVER;
		}
	}
	private void setLevelAttribute(int temp_level){
		this.level = temp_level;
		this.appearance_limit = Constant.APPEARANCE_LEVEL[this.level];
		this.random_gap = Constant.RANDOM_LEVEL[this.level];
		this.mTomManager.reset_appearanceDuration(Constant.TIME_LEVEL[this.level]);
	}
	private int getLevel(int score){
		int result = 0;
		if (score >=Constant.SCORE_LEVEL[1] && score < Constant.SCORE_LEVEL[2]) {
			result = 1;
		}else if(score >= Constant.SCORE_LEVEL[2] && score < Constant.SCORE_LEVEL[3] ){
			result = 2;
		}else if(score >= Constant.SCORE_LEVEL[3] && score < Constant.SCORE_LEVEL[4]){
			result = 3;
		}else if(score >= Constant.SCORE_LEVEL[4] && score < Constant.SCORE_LEVEL[5]){
			result = 4;
		}else if(score >= Constant.SCORE_LEVEL[5] && score < Constant.SCORE_LEVEL[6]){
			result = 5;
		}else if(score >= Constant.SCORE_LEVEL[6]){
			result = 6;
		}
		return result;
	}
	@Override
	public void draw(Canvas canvas) {
		if (this._status_game.equals(Constant.STATUS_TITLE)) {
			canvas.drawBitmap(this.mScaledTitle, 0, 0, null);
			canvas.drawBitmap(mButton_play,this.play_button_x, this.play_button_y, null);
		}else{
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			paint.setStyle(Style.FILL);
			paint.setTextSize(70);
			Rect destRect= new Rect(this.mThor.getxPosition(), this.mThor.getyPosition(),
					this.mThor.getxPosition()+(this.mThor.getmSpriteWidth()*2), 
					this.mThor.getyPosition()+(this.mThor.getmSpriteHeight()*2));
			
			if(this._status_game.equals(Constant.STATUS_LIVE)){//else
				canvas.drawBitmap(this.mScaledBackground, 0, 0, null);
				for(byte i=0; i<9; i++){
					Tom curr_tom = this.mTomManager.toms[i];
					Rect tomRect= new Rect(curr_tom.getxCoord(), curr_tom.getyCoord(),
							curr_tom.getxCoord()+(curr_tom.getSpriteWidth()*1), 
							curr_tom.getyCoord()+(curr_tom.getSpriteHeight()*1));
					canvas.drawBitmap(this.mTomManager.toms[i].getmShowImage(), this.tomSourceRect, 
										tomRect, null);
				}
				canvas.drawBitmap(this.mThor.getShowImage(), this.sourceRect, destRect, null);
				canvas.drawText("Score: " + this.score, this.scoreXCoord, this.scoreYCoord, paint);
			}else if(this._status_game.equals(Constant.STATUS_GAMEOVER)){
				//if Game is already over
				canvas.drawBitmap(this.mScaledGameOver, 0, 0, null);
				canvas.drawBitmap(mButton_tryAgain,this.try_again_button_x, this.try_again_button_y, null);
				canvas.drawText("Your Score: \n" + this.score, this.try_again_button_x, 
						(this.try_again_button_y + 400), paint);
			}
		}
		
	}
	/*
	 * @param player: player object
	 * @param tom: target object
	 */
	private int CheckCollision(ThorHammer player, TomManage tom){
		int result = 0;
		int playerXCoord = player.getxPosition();
		int playerYCoord = player.getyPosition() + player.getmSpriteHeight();
		if(playerXCoord < tom.column_seps[1]){
			if(playerYCoord > tom.row_seps[0] && playerYCoord < tom.row_seps[1]){
				result = 1;
			}else if(playerYCoord > tom.row_seps[1] && playerYCoord < tom.row_seps[2]){
				result = 2;
			}else if(playerYCoord > tom.row_seps[2]){
				result = 3;
			}
		}else if(playerXCoord > tom.column_seps[1] && playerXCoord < tom.column_seps[2]){
			if(playerYCoord > tom.row_seps[0] && playerYCoord < tom.row_seps[1]){
				result = 4;
			}else if(playerYCoord > tom.row_seps[1] && playerYCoord < tom.row_seps[2]){
				result = 5;
			}else if(playerYCoord > tom.row_seps[2]){
				result = 6;
			}
		}else if(playerXCoord > tom.column_seps[1]){
			if(playerYCoord > tom.row_seps[0] && playerYCoord < tom.row_seps[1]){
				result = 7;
			}else if(playerYCoord > tom.row_seps[1] && playerYCoord < tom.row_seps[2]){
				result = 8;
			}else if(playerYCoord > tom.row_seps[2]){
				result = 9;
			}
		}
		return result;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			if(this._status_game == Constant.STATUS_LIVE){
				int x = (int)event.getX();
				int y = (int)event.getY() - this.mThor.getmSpriteHeight();
				this.mThor.Hit(x, y);
				tom_index = CheckCollision(this.mThor, this.mTomManager);
			}else if(this._status_game == Constant.STATUS_GAMEOVER){
				int x = (int)event.getX();
				int y = (int)event.getY();
				if(x >= this.try_again_button_x && x <= (this.try_again_button_x + this.mButton_tryAgain.getWidth())){
					if (y >= this.try_again_button_y && y <= (this.try_again_button_y+this.mButton_tryAgain.getHeight())){
						//if try again is clicked
						this.score = 0;
						this.mTomManager.resetMissed();
						this.setLevelAttribute(0);
						this._status_game = Constant.STATUS_LIVE;
					}
				}
			}else if(this._status_game == Constant.STATUS_TITLE){
				int x = (int)event.getX();
				int y = (int)event.getY();
				if(x >= this.play_button_x && x <= (this.play_button_x + this.mButton_play.getWidth())){
					if (y >= this.play_button_y && y <= (this.play_button_y +this.mButton_play.getHeight())){
						this._status_game = Constant.STATUS_LIVE;
					}
				}
			}
		} else if(event.getAction()==MotionEvent.ACTION_UP){
			
		}

		return true;
	}
}
