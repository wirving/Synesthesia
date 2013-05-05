
public class JitteringObject extends BasicObject{
	
	float maxYPos;
	float minYPos;

	public JitteringObject(float xPos, float yPos, float zPos) {
		super(xPos, yPos, zPos);
		// TODO Auto-generated constructor stub
	}
	
	public JitteringObject(float xPos, float yPos, float zPos, float minYPos, float maxYPos) {
		super(xPos, yPos, zPos);
		// TODO Auto-generated constructor stub
		this.minYPos = minYPos;
		this.maxYPos = maxYPos;
	}
	

	public JitteringObject(float i, float j, float k, float f, float g, float h) {
		// TODO Auto-generated constructor stub
		super(i, j, k, f, g, h);
	}


	public JitteringObject(float xPos, float yPos, float zPos, float xScale, float yScale,
			float zScale, float minYPos, float maxYPos) {
		// TODO Auto-generated constructor stub
		super(xPos, yPos, zPos, xScale, yScale, zScale);
		this.minYPos = minYPos;
		this.maxYPos = maxYPos;
	
	}

	public JitteringObject(float f, float g, float i, float h, float j, float k,
			float l, float m, float n, float minYPos, float maxYPos) {
		super(f, g, i, h, j, k, l, m, n );
		this.minYPos = minYPos;
		this.maxYPos = maxYPos;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Move(float player_x, float player_y, float player_z){
		
		this.xPos += (Math.random()-.5)/15;
		this.yPos += (Math.random()-.5)/15;
		
		if (this.yPos > this.maxYPos || this.yPos < this.minYPos){
			this.yPos = this.minYPos;
		}
		
		this.zPos += (Math.random()-.5)/15;
		
		this.xRot += (Math.random()-.5);
		this.yRot += (Math.random()-.5);
		this.zRot += (Math.random()-.5);
	
		this.xScale += (Math.random() -.5)/15;
		this.yScale += (Math.random() -.5)/15;
		this.zScale += (Math.random() -.5)/15;
		
	}

}
