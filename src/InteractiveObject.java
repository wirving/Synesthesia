
public class InteractiveObject extends BasicObject{

	public boolean interactive = false;
	
	public InteractiveObject(float xPos, float yPos, float zPos) {
		super(xPos, yPos, zPos);
	}
	
	public InteractiveObject(float xPos, float yPos, float zPos, float xScale,
			float yScale, float zScale) {
		super(xPos, yPos, zPos, xScale, yScale, zScale);
	}
	
	public InteractiveObject(float xPos, float yPos, float zPos, float xScale,
			float yScale, float zScale, float xRot, float yRot, float zRot) {
		super(xPos, yPos, zPos, xScale, yScale, zScale, xRot, yRot, zRot);
	}
	
	@Override
    public void Move(float playerX, float playerY, float playerZ){

    }

	@Override
    public float[] getDiffuseColor(){
    	float[] tempfloat = { (float) Math.random(), (float)Math.random(), (float)Math.random(), 1 };
    	return tempfloat;
    }
	
	public void setInteractive(boolean interactive){
		this.interactive = interactive;
	}
	
	public boolean isInteractive(){
		return interactive;
	}
}
