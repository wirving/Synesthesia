
public class RotatingObject extends BasicObject {
	
	public RotatingObject(float xPos, float yPos, float zPos) {
		super(xPos, yPos, zPos);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Move(float player_x, float player_y, float player_z){
	
		
		this.xRot += .5f;
		this.zRot += .5f;
		
	}


}
