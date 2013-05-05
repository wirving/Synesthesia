
public class RotatingObject extends BasicObject {
	
	public RotatingObject(float xPos, float yPos, float zPos) {
		super(xPos, yPos, zPos);
		// TODO Auto-generated constructor stub
	}

	public RotatingObject(float f, float g, float h, float i, float j, float k,
			float l, float m, float n) {
		// TODO Auto-generated constructor stub
		super(f, g, h, i, j, k, l, m, n);
	}

	@Override
	public void Move(float player_x, float player_y, float player_z){
	
		
		this.xRot += .5f;
		this.zRot += .5f;
		
	}


}
