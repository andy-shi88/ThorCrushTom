package characters;

import com.ND.thorcrushtom.Constant;

import android.graphics.Bitmap;
import android.media.SoundPool;

public class ThorHammer {
	private Bitmap mBitmapHammer;
	private Bitmap mShowImage;
	private int xPosition;
	private int yPosition;
	private int mSpriteWidth = 200;
	private int mSpriteHeight = 200;
	//animation members
	private int frameNumber = 4;
	private int activeFrame;
	private String mStatus;
	private int hitSoundId;
	private SoundPool soundPool;
	
	public ThorHammer(Bitmap hammer, SoundPool soundPool, int hitSoundId, int xStart, int yStart){
		this.mBitmapHammer= Bitmap.createScaledBitmap(hammer, 
				800, this.mSpriteHeight, false);;
		this.mShowImage = this.mBitmapHammer;
		this.xPosition = xStart;
		this.yPosition = yStart;
		this.activeFrame = 0;
		this.hitSoundId = hitSoundId;
		this.soundPool = soundPool;
	}
	//function: getter
	public Bitmap getShowImage(){
		return this.mShowImage;
	}
	public Bitmap getHammer(){
		return this.mBitmapHammer;
	}
	public int getxPosition() {
		return xPosition;
	}
	public int getyPosition() {
		return yPosition;
	}
	public int getmSpriteWidth() {
		return mSpriteWidth;
	}
	public int getmSpriteHeight() {
		return mSpriteHeight;
	}
	

	public int getFrameNumber() {
		return frameNumber;
	}
	public int getActiveFrame() {
		return activeFrame;
	}
	public void Hit(int xPosition, int yPosition){
		this.mStatus = Constant.STATUS_HIT;		
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		soundPool.play(hitSoundId, 1, 1, 1, 0, 1f);
	}
	public void StandBy(int xPosition, int yPosition){
		this.mStatus = Constant.STATUS_STANDBY;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.activeFrame = 0;
	}
	
	public void animate(){
		if(this.mStatus == Constant.STATUS_HIT){
			//return to standby status
			if(this.activeFrame == frameNumber - 1){
				this.activeFrame = 0;
				this.mStatus = Constant.STATUS_STANDBY;
			}else{
				this.activeFrame++;
			}
		}else{
			this.StandBy(this.xPosition, this.yPosition);
		}
	}
	
}
