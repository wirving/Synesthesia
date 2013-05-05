import java.util.ArrayList;


<<<<<<< HEAD
public class Statue extends InteractiveObject {
=======
public class Statue extends CollisionObject {
>>>>>>> origin/Working

	
	public Statue(float xPos, float yPos, float zPos) {
		super(xPos, yPos, zPos);
		// TODO Auto-generated constructor stub
	}

	
<<<<<<< HEAD
	public Statue(int i, float f, float g, float h, float i2, float i3,
=======
	public Statue(float i, float f, float g, float h, float i2, float i3,
>>>>>>> origin/Working
			float i4, float i5, float i6) {
		super(i, f, g, h, i2, i3, i4, i5, i6);
		// TODO Auto-generated constructor stub
	}

<<<<<<< HEAD
	public void Move(float player_x, float player_y, float player_z){
		
		//If player is close, change to NEXT_LEVEL
		if(Math.sqrt((Math.pow(Math.abs(Math.max(this.getXPos(),player_x) - Math.min(this.getXPos(),player_x)),2)+Math.pow(Math.abs(Math.max(this.getZPos(), player_z) - Math.min(this.getZPos(),  player_z)),2))) < 20 ){
		this.function = InteractiveObject.Function.NEXT_LEVEL;	
=======

	public void Move(float player_x, float player_y, float player_z){
		
		//If player is close, change to NEXT_LEVEL
		//if(Math.sqrt((Math.pow(Math.abs(Math.max(this.getXPos(),player_x) - Math.min(this.getXPos(),player_x)),2)+Math.pow(Math.abs(Math.max(this.getZPos(), player_z) - Math.min(this.getZPos(),  player_z)),2))) < 20 ){
		
>>>>>>> origin/Working
		
		//Increase y
		if (yPos < 1.5){
		this.yPos += .02f; 
		}
		
		//Interpolate between player's x and z and current x and z
<<<<<<< HEAD
		this.xPos = (float)(.99 * (this.xPos) + .01 *(player_x));
		this.zPos = (float)(.99 * (this.zPos) + .01 * (player_z));
		
		animateObject();
=======
		this.xPos = (float)(.995 * (this.xPos) + .005 *(player_x));
		this.zPos = (float)(.995 * (this.zPos) + .005 * (player_z));
		
		animateObject(2);
		
		
		//Collided?
	//	if(Math.sqrt((Math.pow(Math.abs(Math.max(this.getXPos(),player_x) - Math.min(this.getXPos(),player_x)),2)+Math.pow(Math.abs(Math.max(this.getZPos(), player_z) - Math.min(this.getZPos(),  player_z)),2))) < 2 ){
			
		//}
		
>>>>>>> origin/Working
		
		}
	}

<<<<<<< HEAD
}
=======
//}
>>>>>>> origin/Working
