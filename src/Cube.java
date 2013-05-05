
public class Cube extends BasicObject {

	public Cube(float xPos, float yPos, float zPos) {
		super(xPos, yPos, zPos);
	}
    public Cube(float xPos, float yPos, float zPos, float xScale, float yScale, float zScale){
    	super(xPos, yPos,zPos,xScale,yScale,zScale);
    }
    public Cube(float xPos, float yPos, float zPos, float xScale, float yScale, float zScale, float xRot, float yRot, float zRot){
    	super(xPos, yPos,zPos,xScale,yScale,zScale, xRot, yRot, zRot);
    }
    
    @Override
    public float[] getDiffuseColor(){
    	float[] tempDiffuse = this.mat_diffuse;
    	for(int i = 0; i < tempDiffuse.length; i++){
    		if(tempDiffuse[i] != 0){
    			tempDiffuse[i] += (Math.random()-.5)/15;
    		}
    	}
    	return tempDiffuse;
    } 
}
