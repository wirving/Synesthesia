
public class DropObject extends BasicObject{

	public DropObject(float xPos, float yPos, float zPos) {
		super(xPos, yPos, zPos);
	}
    public DropObject(float xPos, float yPos, float zPos, float xScale, float yScale, float zScale){
    	super(xPos, yPos,zPos,xScale,yScale,zScale);
    }
    public DropObject(float xPos, float yPos, float zPos, float xScale, float yScale, float zScale, float xRot, float yRot, float zRot){
    	super(xPos, yPos,zPos,xScale,yScale,zScale, xRot, yRot, zRot);
    }
    
    
}
