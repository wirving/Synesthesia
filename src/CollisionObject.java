
public class CollisionObject extends BasicObject{

	public CollisionObject(float xPos, float yPos, float zPos) {
		super(xPos, yPos, zPos);
		// TODO Auto-generated constructor stub
	}
	
	public CollisionObject(float i, float f, float g, float h, float i2,
			float i3, float i4, float i5, float i6) {
		// TODO Auto-generated constructor stub
		super(i, f, g, h, i2, i3, i4, i5, i6);
	}

	public CollisionObject(float f, float g, float h, float i, float j, float k) {
		// TODO Auto-generated constructor stub
		super(f, g, h, i, j, k);
	}


	public boolean playerCollision(float player_x, float player_y, float player_z){
		
		if(Math.sqrt((Math.pow(Math.abs(Math.max(this.getXPos(),player_x) - Math.min(this.getXPos(),player_x)),2)+Math.pow(Math.abs(Math.max(this.getZPos(), player_z) - Math.min(this.getZPos(),  player_z)),2))) < 2 ){
			return true;
		}
		else return false;
	}


}
