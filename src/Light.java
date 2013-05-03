
public class Light {
	
	private float[] position;
	private float[] diffuse;
	private float[] specular;
	private boolean dynamic;
	
	
	//Constructor with position
	public Light(float[] pos){
		this.position = pos;
	}
	
	public Light(float[] pos, float[] dif, float[] spec){
		this.position = pos;
		this.diffuse = dif;
		this.specular = spec;
	}
	
	//Moves?
	public boolean isDynamic(){
		return dynamic;
	}
	
	
	public void setDynamic(boolean dyn){
		this.dynamic = dyn;
		
	}
	
	public float[] getPosition(){
		return position;
	}
	
	public float[] getDiffuse(){
		return diffuse;
	}
	
	public float[] getSpecular(){
		return specular;
	}

}
