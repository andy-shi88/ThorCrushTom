package characters;

import com.ND.thorcrushtom.Constant;

import android.graphics.Bitmap;

public class TomManage {
	public Tom[] toms = new Tom[9];
	public int[] column_seps = new int[3];
	public int[] row_seps = new int[3];
	private int num_of_tom_appeared = 0;
	
	public TomManage(Bitmap tom_normal, Bitmap tom_hit, Bitmap tom_invicible, int default_appearanceDuration, int deviceWidth, int deviceHeight){
		this.initialize_tom(tom_normal, tom_hit, tom_invicible, default_appearanceDuration);
		this.initialize_status();
		this.initialize_coordinate(deviceWidth, deviceHeight);
	}
	/*
	 * @param newDuration set the new duration of tom appearance in millisecond
	 */
	public void reset_appearanceDuration(int newDuration){
		for (byte i = 0; i<9; i++){
			this.toms[i].set_appearance_duration(newDuration);
		}
	}
	
	public byte get_appearance_count(){
		byte temp_count = 0;
		for(byte i = 0; i<=8; i++){
			if(this.toms[i].getStatus() == Constant.STATUS_APPEAR){
				temp_count++;
			}
		}
		return temp_count;
	}
	public boolean isMissed(){
		boolean result =  false;
		for (byte i = 0; i< 9; i++){
			if (this.toms[i].isMissed()) {
				result = true;
				break;
			}
		}
		return result;
	}
	public void resetMissed(){
		for (byte i = 0; i< 9; i++){
			this.toms[i].set_miss(false);
		}
	}
	
	public int getNum_of_tom_appeared() {
		return num_of_tom_appeared;
	}
	public void setNum_of_tom_appeared(int num_of_tom_appeared) {
		this.num_of_tom_appeared = num_of_tom_appeared;
	}
	private void initialize_tom(Bitmap tom_normal, Bitmap tom_hit, Bitmap tom_invicible, int default_appearanceDuration){
		for(byte i=0; i<9; i++){
			toms[i] = new Tom(tom_normal, tom_hit, tom_invicible, default_appearanceDuration);
		}
	}
	private void initialize_status(){
		for (byte i=0; i<9; i++){
			toms[i].setStatus(Constant.STATUS_HIDE);
		}
	}
	private void initialize_coordinate(int deviceWidth, int deviceHeight){
		int bias = (deviceWidth/100)*3;
		int height_bias = deviceHeight - deviceWidth;
		int holes_area = deviceWidth;
		int holes_gap = (holes_area/3);
		byte counter = 0;
		for(byte i = 0; i<3; i++){
			int x = bias+((i)*holes_gap);
			this.column_seps[i] = x;
			for (byte j = 0; j<3; j++){
				int y = bias+(height_bias + ((j)*(holes_gap)));
				this.row_seps[j] = y;
				this.toms[counter].setCoordinate(x, y);
				counter++;
			}
		}
	}
	public void animate(){
		for(byte i=0; i<9; i++){
			this.toms[i].animate();
		}
	}
	
}
