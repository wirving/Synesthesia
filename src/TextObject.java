
public class TextObject {

	float xPos = 0;
	float yPos = 0;
	float zPos = 0;
	
	float xScale = 0;
	float yScale = 0;
	float zScale = 0;
	
	float xRot = 0;
	float yRot = 0;
	float zRot = 0;
	float rotDegree = 0;
	
	String text = "";
	
	float[] diffuse;
	float[] specular = new float[]{.5f,.5f,.5f,0};
	public TextObject(float xPos, float yPos, float zPos){
		this.xPos = xPos;
		this.yPos = yPos;
		this.zPos = zPos;
	}
	
	public TextObject(float xPos, float yPos, float zPos, float xScale, float yScale, float zScale){
		this.xPos = xPos;
		this.yPos = yPos;
		this.zPos = zPos;
		
		this.xScale = xScale;
		this.yScale = yScale;
		this.zScale = zScale;
	}
	
	public TextObject(float xPos, float yPos, float zPos, float xScale, float yScale, float zScale, float xRot, float yRot, float zRot){
		this.xPos = xPos;
		this.yPos = yPos;
		this.zPos = zPos;
		
		this.xScale = xScale;
		this.yScale = yScale;
		this.zScale = zScale;
		
		this.xRot = xRot;
		this.yRot = yRot;
		this.zRot = zRot;
	}
	
	public void setText(String newText){
		text = newText;
	}
	
	public String getText(){
		return text;
	}
	
	public float getXPos(){
		return xPos;
	}
	
	public float getYPos(){
		return yPos;
	}
	
	public float getZPos(){
		return zPos;
	}
	
	public float getXScale(){
		return xScale;
	}
	
	public float getYScale(){
		return yScale;
	}
	
	public float getZScale(){
		return zScale;
	}
	
	public float getXRot(){
		return xRot;
	}
	
	public float getYRot(){
		return yRot;
	}
	
	public float getZRot(){
		return zRot;
	}
	
	public void setDiffuse(float[] newDiffuse){
		this.diffuse = newDiffuse;
	}
	
	public void setSpecular(float[] newSpecular){
		this.specular = newSpecular;
	}
	
	public float[] getDiffuse(){
		return this.diffuse;
	}
	
	public float[] getSpecular(){
		return this.specular;
	}
	
	public void setRotDegree(float newRot){
		this.rotDegree = newRot;
	}
	
	public float getRotDegree(){
		return this.rotDegree;
	}
}
