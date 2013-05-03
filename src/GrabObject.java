
public class GrabObject extends BasicObject{

	boolean isGrabbed = false;
	public GrabObject(float xPos, float yPos, float zPos) {
		super(xPos, yPos, zPos);
	}
	
	public GrabObject(float xPos, float yPos, float zPos, float xScale,
			float yScale, float zScale) {
		super(xPos, yPos, zPos, xScale, yScale, zScale);
	}
	
	public GrabObject(float xPos, float yPos, float zPos, float xScale,
			float yScale, float zScale, float xRot, float yRot, float zRot) {
		super(xPos, yPos, zPos, xScale, yScale, zScale, xRot, yRot, zRot);
	}
	
	@Override
    public void Move(float playerX, float playerY, float playerZ){

    }

	public void setGrabbed(boolean grabbed){
		isGrabbed = grabbed;
	}
	
	public boolean isGrabbed(){
		return isGrabbed;
	}
}
