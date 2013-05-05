
public class GrabableObject extends BasicObject{

	boolean isGrabbed = false;
	boolean goingUp = true;
	
	private float destX, destY,destZ; 
	
	public GrabableObject(float xPos, float yPos, float zPos) {
		super(xPos, yPos, zPos);
	}
    public GrabableObject(float xPos, float yPos, float zPos, float xScale, float yScale, float zScale){
    	super(xPos, yPos,zPos,xScale,yScale,zScale);
    }
    public GrabableObject(float xPos, float yPos, float zPos, float xScale, float yScale, float zScale, float xRot, float yRot, float zRot){
    	super(xPos, yPos,zPos,xScale,yScale,zScale, xRot, yRot, zRot);
    }
    
    public boolean isGrabbed(){
    	return isGrabbed;
    }
    
    public void setIsGrabbed(boolean grabbed){
    	isGrabbed = grabbed;
    }
    
    public void setDestX(float Xdest){
    	destX = Xdest;
    }
    
    public void setDestZ(float Zdest){
    	destZ = Zdest;
    }
    
    public float getDestX(){
    	return destX;
    }
    
    public float getDestZ(){
    	return destZ;
    }
    
    public void place(){
    	isGrabbed = false;
    	xPos = destX;
    	zPos = destZ;
    }
    
    @Override
    public void Move(float playerX, float playerY, float playerZ){

    	if(goingUp){
    		this.yPos = this.yPos + .01f;
    		if(this.yPos >= .25){
    			goingUp = false;
    		}
    	}
    	if(!goingUp){
    		this.yPos = this.yPos - .01f;
    		if(this.yPos <= -.25){
    			goingUp = true;
    		}
    	}
    }
}
