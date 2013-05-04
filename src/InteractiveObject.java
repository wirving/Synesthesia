
public class InteractiveObject extends BasicObject{

	public enum Function{
		NEXT_LEVEL, LEVEL_EVENT
	}
	
	Function function;
	
	public boolean interactive = false;
	public int radius;
	
	public InteractiveObject(float xPos, float yPos, float zPos) {
		super(xPos, yPos, zPos);
	}
	
	public InteractiveObject(float xPos, float yPos, float zPos, float xScale,
			float yScale, float zScale) {
		super(xPos, yPos, zPos, xScale, yScale, zScale);
	}
	
	public InteractiveObject(float xPos, float yPos, float zPos, float xScale,
			float yScale, float zScale, float xRot, float yRot, float zRot) {
		super(xPos, yPos, zPos, xScale, yScale, zScale, xRot, yRot, zRot);
	}
	
	public InteractiveObject(int i, float f, float g, float h, float j,
			float k, float l, float m, float n, int o) {
		super(i, f, g, h, j, k, l, m, n);
		// TODO Auto-generated constructor stub
		this.radius = o;
		
	}

	@Override
    public void Move(float playerX, float playerY, float playerZ){

    }

	
	
	
	public void setInteractive(boolean interactive){
		this.interactive = interactive;
	}
	
	public boolean isInteractive(){
		return interactive;
	}
	
	public void setFunction(InteractiveObject.Function func){
		this.function = func;
	}
	
	public Function getFunction(){
		return this.function;
	}
}


