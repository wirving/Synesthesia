
public class Level2EndObject extends InteractiveObject{

	public Level2EndObject(float xPos, float yPos, float zPos) {
		super(xPos, yPos, zPos);
	}
	
	public Level2EndObject(float xPos, float yPos, float zPos, float xScale,
			float yScale, float zScale) {
		super(xPos, yPos, zPos, xScale, yScale, zScale);
	}
	
	public Level2EndObject(float xPos, float yPos, float zPos, float xScale,
			float yScale, float zScale, float xRot, float yRot, float zRot) {
		super(xPos, yPos, zPos, xScale, yScale, zScale, xRot, yRot, zRot);
	}
	
	public Level2EndObject(int i, float f, float g, float h, float j,
			float k, float l, float m, float n, int o) {
		super(i, f, g, h, j, k, l, m, n);
		this.radius = o;
		
	}
	
	@Override
    public void Move(float playerX, float playerY, float playerZ){
		if(isDynamic){
			if(this.yPos < 0){
				yPos += .005f;
				this.function = InteractiveObject.Function.NEXT_LEVEL;
			}
		}
    }
}
