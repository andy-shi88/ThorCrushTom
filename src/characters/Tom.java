package characters;

import com.ND.thorcrushtom.Constant;

import android.graphics.Bitmap;

public class Tom {
	private Bitmap tom_normal;
	private Bitmap tom_hit;
	private Bitmap tom_invicible;
	private Bitmap mShowImage;
	private int xCoord;
	private int yCoord;
	private int spriteWidth = 200;
	private int spriteHeight = 200;
	private String status = Constant.STATUS_APPEAR;
	private int temp_count = 0;
	//tom time value [in frame]
	private int appearance_duration;
	private static int hit_duration = 4;
	private boolean missed = false;
	/*
	 * @param tom_normal set tom normal bitmap
	 * @param tom_hit set tom hit bitmap
	 * @param tom_invicible set tom disappear bitmap
	 * @param default_appearanceDuration set the default of tom appearance duration each time in second
	 */
	public Tom(Bitmap tom_normal, Bitmap tom_hit, Bitmap tom_invicible, int default_appearanceDuration){
		this.tom_normal = tom_normal;
		this.tom_hit = tom_hit;
		this.tom_invicible = tom_invicible;
		this.appearance_duration = default_appearanceDuration;
		this.mShowImage = this.tom_invicible;
		this.status = Constant.STATUS_HIDE;
	}
	
	public int getxCoord() {
		return xCoord;
	}
	/*
	 * @xCoord: the x coordinate we are going to assign to the object
	 */
	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	public int getyCoord() {
		return yCoord;
	}
	/*
	 * @yCoord: the y coordinate we are going to assign to the object
	 */
	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}
	

	public int getSpriteWidth() {
		return spriteWidth;
	}

	public void setSpriteWidth(int spriteWidth) {
		this.spriteWidth = spriteWidth;
	}
	/*
	 * @param: newDuration will be the new durration of the object appearance. measured in milliseconds
	 */
	public void set_appearance_duration(int newDuration){
		this.appearance_duration = (newDuration/1000) * Constant.FPS_VALUE;
	}
	public int get_appearance_duration(){
		return this.appearance_duration;
	}
	public boolean isMissed(){
		return this.missed;
	}
	public void set_miss(boolean miss){
		this.missed = miss;
	}

	public int getSpriteHeight() {
		return spriteHeight;
	}

	public void setSpriteHeight(int spriteHeight) {
		this.spriteHeight = spriteHeight;
	}

	public Bitmap getmShowImage() {
		return mShowImage;
	}
	public void hit(){
		this.setmShowImage(this.tom_hit);
		this.temp_count = 0;
		this.status = Constant.STATUS_HIT;
	}
	private void setmShowImage(Bitmap mShowImage) {
		this.mShowImage = mShowImage;
	}

	public void setCoordinate(int x, int y){
		this.xCoord = x;
		this.yCoord = y;
	}
	public void setStatus(String status){
		this.status = status;
	}
	public String getStatus(){
		return this.status;
	}
	public void animate(){
		//hit delay
		if(this.temp_count> Tom.hit_duration && this.status == Constant.STATUS_HIT){
			this.status = Constant.STATUS_HIDE;
			this.temp_count = 0;
		}
		//appearance time
		if(this.temp_count > this.appearance_duration && this.status == Constant.STATUS_APPEAR){
			this.status = Constant.STATUS_HIDE;
			this.temp_count = 0;
			this.missed = true;
		}
		//status check
		if (this.status == Constant.STATUS_APPEAR) {
			this.mShowImage = tom_normal;
			this.temp_count++;
		}else if(this.status == Constant.STATUS_HIT){
			this.mShowImage = tom_hit;
			this.temp_count++;
		}else if(this.status == Constant.STATUS_HIDE){
			this.mShowImage = tom_invicible;
		}
		//end of status check
	}
}
