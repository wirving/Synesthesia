import javax.media.opengl.GL;


public class Rock implements Entity {

	private float xPos;
	private float yPos;
	private float zPos; 
	
	private float scaleX;
	private float scaleY;
	private float scaleZ;
	
    float mat_ambient[] = { 0, 1f, 0, 1 };
    float mat_specular[] = { .8f, .8f, .8f, 1 };
    float mat_diffuse[] = { 0f, 1f, 0f, 1 };
    float mat_shininess[] = { 128 };
	
	private String name = "Rock";
	private objModel object; 
	
	public Rock(float x, float y, float z, objModel object){
		this.xPos = x;
		this.yPos = y;
		this.zPos = z;
		this.object = object; 
	
		
	}
	
	public Rock(float x, float y, float z, float scaleX, float scaleY, float scaleZ){
		this.xPos = x;
		this.yPos = y;
		this.zPos = z;
		
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
		
	}
	@Override
	public void move() {
		// TODO Auto-generated method stub
		/*
		gl.glPushMatrix();
		gl.glTranslatef(this.x, this.y, this.z);
	
	    float mat_ambient[] = new float[]{ 0, 0, 0, 1 };
	    float mat_specular[] = new float[]{ .3f, .3f, .3f, 1 };
	    float mat_diffuse[] = new float[]{ .4f, .3f, .4f, 1 };
	    float mat_shininess[] = new float[]{ 128 };
	    gl.glMaterialfv( GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient, 0);
	    gl.glMaterialfv( GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
	    gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
	    gl.glMaterialfv( GL.GL_FRONT, GL.GL_SHININESS, mat_shininess, 0);
	    
		rock.Draw();
		
		gl.glPopMatrix();
		*/
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
		return scaleX;
	}
	
	public float getYScale(){
		return scaleY;
	}
	
	public float getZScale(){
		return scaleZ;
	}

	public String getObject(){
		return name;
	}
}
