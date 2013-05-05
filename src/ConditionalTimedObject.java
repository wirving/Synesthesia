
public class ConditionalTimedObject extends Cube{

	boolean steppedOn = false; 
	int timer;
	int timerValue = 1800;
	boolean isCollideable = false; 
	boolean firstTime = true;
	
	public ConditionalTimedObject(float xPos, float yPos, float zPos) {
		super(xPos, yPos, zPos);
	}
    public ConditionalTimedObject(float xPos, float yPos, float zPos, float xScale, float yScale, float zScale){
    	super(xPos, yPos,zPos,xScale,yScale,zScale);
    }
    public ConditionalTimedObject(float xPos, float yPos, float zPos, float xScale, float yScale, float zScale, float xRot, float yRot, float zRot){
    	super(xPos, yPos,zPos,xScale,yScale,zScale, xRot, yRot, zRot);
    }
    
    @Override
	 public void Move(float playerX, float playerY, float playerZ){
	    	
	    	// Checks to see if object can move
	    	if(isDynamic){
		    	if(Math.sqrt((Math.pow(Math.abs(Math.max(playerX,this.xPos) - Math.min(playerX,this.xPos)),2)+Math.pow(Math.abs(Math.max(playerZ, this.zPos) - Math.min(playerZ,  this.zPos)),2))) <= .75 ){
	
		    		steppedOn = true;
		    		timer = timerValue;
		    	}
		    	else if(Math.sqrt((Math.pow(Math.abs(Math.max(playerX,this.xPos) - Math.min(playerX,this.xPos)),2)+Math.pow(Math.abs(Math.max(playerZ, this.zPos) - Math.min(playerZ,  this.zPos)),2))) >= 2){
		    		timer = timer-1;
		    		if(timer <= 0){
		    			steppedOn = false;
		    			isCollideable = false; 
		    			if(this.yPos > -1){
			    			this.yPos -= .05f;
			    			
			    			if(fadeToBlack){
				    			for(int i = 0; i < this.mat_diffuse.length; i++){
				    				if(this.mat_diffuse[i] > .05){
				    					this.mat_diffuse[i] += .05f;
				    				}
				    			}
			    			}
			    		}
			    		else{
			    			this.yPos = -1;
			    		}
		    			
		    		}
		    		if(steppedOn){
		    			isCollideable = true;
		    			if(this.yPos < 0){
			    			this.yPos += .05;
			    			
			    			if(fadeToBlack){
				    			for(int i = 0; i < this.mat_diffuse.length; i++){
				    				if(this.mat_diffuse[i] > .05){
				    					this.mat_diffuse[i] -=.05f;
				    				}
			    			}
			    			}
			    		}
			    		else{
			    			this.yPos = 0;
			    		}
			    	}
		    	}
	    	}
	    }
 
    public boolean isCollideable(){
    	return isCollideable;
    }
}
