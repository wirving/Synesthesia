
public class TextureParameters {
	
	public enum texCoordGenMode{
		PLANE, SPHERE, SPHERE_MAP
	}
	
	//Texture parameter info, all in one place!

    //Texture to use
    private String tex_name;
    
    //Which mode to use?
    private texCoordGenMode texCoords;
    
    //Is there a texture?
    //By default turn off
    private boolean textured = false;
    
    private float[] tiling_coefficients = {1f, 1f, 1f};
   
    public TextureParameters(){
    	//Default values as above
  
    }
    
    public void setTextured(boolean tex){
    	this.textured = tex;
    }
    
    public boolean isTextured(){
    	return textured;
    }
    
    public void setTextureName(String name){
    	this.tex_name = name;
    }
    
    public String getTextureName(){
    	
    	if (textured)
    		return this.tex_name;
    	
    	else
    		return null;
    }
    	
    public void setTilingCoefficients(float[] coeffs){
    	this.tiling_coefficients = coeffs;
    }
    
    public float[] getTilingCoefficients(){
    	return this.tiling_coefficients;
    }

    public void setTexGenMode(texCoordGenMode tex){
    	this.texCoords = tex;
    }
    
    public texCoordGenMode getTexGenMode(){
    	
    	return this.texCoords;
    }
}
