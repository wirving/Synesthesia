import java.util.ArrayList;


public class BasicObject {
	
	ArrayList<String> animationCycle = new ArrayList<String>();
	boolean goUp = true;
	int whichFrame = 0;
	
<<<<<<< HEAD
	public void animateObject(){
=======
	public boolean equals(Object o){
		BasicObject other = (BasicObject)o;
		
		if (other.getXPos() == this.getXPos() && other.getYPos() == this.getYPos() && other.getZPos() == this.getZPos() && other.getObject().equals(this.getObject()))
			return true;
		
		return false;
	}
	
	public void animateObject(int interval){
		
	//if (System.currentTimeMillis()%interval == 0){	
>>>>>>> origin/Working
	if (whichFrame == animationCycle.size()-1){
		goUp = false;
	}
	else if (whichFrame == 0){
		goUp = true;
	}
	
	if (goUp == true){
		whichFrame ++;
	}
	else {
		whichFrame --;
	}
	
	this.setObject(animationCycle.get(whichFrame));
<<<<<<< HEAD
	}
	
	public void setAnimation(String nextObjName){
		animationCycle.add(nextObjName);
=======
	//}
	
	//else{
	
	//	this.setObject(this.getObject());
	//}
}
	
	public float[] getRandomDiffuseColor(){
    	float[] tempfloat = { (float) Math.random(), (float)Math.random(), (float)Math.random(), 1 };
    	return tempfloat;
    }
	
	public void setAnimation(String nextObjName){
		animationCycle.add(nextObjName);	
	
>>>>>>> origin/Working
	}

	// X,Y,Z Position of Object
	protected float xPos = 0, yPos = 0, zPos = 0;
	
	// X,Y,Z Scale of Object
	protected float xScale = 1, yScale = 1, zScale = 1;
	
	// X,Y,Z Rotation of Object
	protected float xRot = 0, yRot = 0, zRot = 0;
	
	// Default Ambiant Color
	private float mat_ambient[] = { 0, 1f, 0, 1 };
	
	// Default Specular Color
    private float mat_specular[] = { .8f, .8f, .8f, 1 };
    
    // Default Diffuse Color
    protected float mat_diffuse[] = { 0f, 1f, 0f, 1 };
    
    // Default Shininess Factor
    private float mat_shininess[] = { 128 };
    
    // Determines whether the object can move
    // Defaults to No Movement
    protected boolean isDynamic = false;
    protected boolean fadeToBlack = true;
    
    // Name of Object used to Draw
    private String object = "cube";
    
    //All of the texture things
    private TextureParameters texParams;
    
    // Constructor for position only
    public BasicObject(float xPos,float yPos,float zPos){
    	this.xPos = xPos;
    	this.yPos = yPos;
    	this.zPos = zPos;
    	
    	texParams = new TextureParameters();
    }

    // Constructor for position and scale
    public BasicObject(float xPos, float yPos, float zPos, float xScale, float yScale, float zScale){
    	this.xPos = xPos;
    	this.yPos = yPos;
    	this.zPos = zPos;
    	
    	this.xScale = xScale;
    	this.yScale = yScale;
    	this.zScale = zScale;
    	
    	texParams = new TextureParameters();
    }
    
    // Constructor for position, scale and rotation
    public BasicObject(float xPos, float yPos, float zPos, float xScale, float yScale, float zScale, float xRot, float yRot, float zRot){
    	this.xPos = xPos;
    	this.yPos = yPos;
    	this.zPos = zPos;
    	
    	this.xScale = xScale;
    	this.yScale = yScale;
    	this.zScale = zScale;
    	
    	this.xRot = xRot;
    	this.yRot = yRot;
    	this.zRot = zRot;
    	
    	texParams = new TextureParameters();
    }
    
    public void setAmbient(float[] ambient){
    	this.mat_ambient = ambient;
    }
    
    public float[] getAmbientColor(){
    	return this.mat_ambient;
    }
    
    public void setSpecular(float[] specular){
    	this.mat_specular = specular;
    }
    
    public float[] getSpecularColor(){
    	return this.mat_specular;
    }
    
    public void setShininess(float[] shininess){
    	this.mat_shininess = shininess;
    }
    
    public float[] getShininess(){
    	return this.mat_shininess;
    }
    
    // Sets the Diffuse Color
    public void setDiffuse(float[] diffuse){
    	this.mat_diffuse = diffuse;
    }
    
    // Returns the Diffuse Color
    public float[] getDiffuseColor(){
    	return this.mat_diffuse;	
    }
    
    // Returns Color with a variation to give flickering effect
    public float[] getDiffuseFlicker(){
    	float[] tempDiffuse = this.mat_diffuse;
    	for(int i = 0; i < tempDiffuse.length; i++){
    		if(tempDiffuse[i] != 0){
    			tempDiffuse[i] += (Math.random()-.5)/15;
    		}
    	}
    	return tempDiffuse;
    }
    
    // Determines whether the move() function call does any calculations
    // True - Yes, Object Moves
    // False - No, Object Stays Still
    public void setDynamic(boolean dynamic){
    	this.isDynamic = dynamic;
    }
    
    // Calculates how to update the position of the object relative to the player
    // Currently just moves the box up or down depending on distance to player
    public void Move(float playerX, float playerY, float playerZ){
    	
    	// Checks to see if object can move
    	if(isDynamic){
	    	//if(Math.abs(playerX - this.xPos) < 2 && Math.abs(playerZ - this.zPos) < 2 ){
	    	if(Math.sqrt((Math.pow(Math.abs(Math.max(playerX,this.xPos) - Math.min(playerX,this.xPos)),2)+Math.pow(Math.abs(Math.max(playerZ, this.zPos) - Math.min(playerZ,  this.zPos)),2))) < 2 ){

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
	    	else{
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
    	}
    }
    
    // Set name of object model
    public void setObject(String objName){
    	this.object = objName;
    }
    
    // Returns name of object model
    public String getObject(){
    	return this.object;
    }

    // Returns X position of object
    public float getXPos(){
    	return this.xPos;
    }
    
    // Returns Y position of object
    public float getYPos(){
    	return this.yPos;
    }
    
    // Returns Z position of object
    public float getZPos(){
    	return this.zPos;
    }
    
    // Returns X scale factor
    public float getXScale(){
    	return this.xScale;
    }
    
    // Returns Y scale factor
    public float getYScale(){
    	return this.yScale;
    }
    
    // Returns Z scale factor
    public float getZScale(){
    	return this.zScale;
    }
    
    // Returns X rotation factor
    public float getXRot(){
    	return this.xRot;
    }
    
    // Returns Y rotation factor
    public float getYRot(){
    	return this.yRot;
    }
    
    // Returns Z rotation factor
    public float getZRot(){
    	return this.zRot;
    }
    
    public TextureParameters getTexParams(){
    	return this.texParams;
    }
    
    public void setTexParams(TextureParameters tex){
    	this.texParams = tex;
    }
}
