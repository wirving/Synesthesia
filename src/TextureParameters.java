
import java.nio.ByteBuffer;
import java.util.Random;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;


public class TextureParameters {
	
	public enum texCoordGenMode{
		PLANE, SPHERE, SPHERE_MAP
	}
	
	//Texture parameter info, all in one place!
	
	ByteBuffer randomTexture;
	
	public boolean tile_x_plane = true;
	public boolean tile_y_plane = false;
	public boolean tile_z_plane = true;
	
    //Texture to use
    private String tex_name;
    
    //Which mode to use?
    private texCoordGenMode texCoords;
    
    //Is there a texture?
    //By default turn off
    private boolean textured = false;
    
    private boolean useRandomTexture = false;
    
    private float[] tiling_coefficients = {1f, 1f, 1f};
   
    public TextureParameters(){
    	//Default values as above
  
    }
    
    public void newRandomTexture(){
    	useRandomTexture = true;
    	byte[] bytearray = new byte[32 * 3 * 32];
		new Random().nextBytes(bytearray);
	    randomTexture = ByteBuffer.allocate(256 * 256* 3);
	    randomTexture.put(bytearray);
	    randomTexture.flip();
    	
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
    
    public boolean useRandomTexture(){
    	return useRandomTexture;
    }
    
    public ByteBuffer getRandomBuffer(){
    	return randomTexture;
    }
}
