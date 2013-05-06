import java.util.ArrayList;


public class LevelTwo extends BasicLevel{

	
	public LevelTwo(){
		staticEntities = new ArrayList<BasicObject>();
		collisionArray = new Boolean[25][25];
		interactiveEntities = new ArrayList<InteractiveObject>();

		for(int i = 0; i < 25; i++){
			for(int j = 0; j < 25; j++){
				if(i != 0 && j != 0 && i!=24 && j != 24){
					collisionArray[i][j] = false;
				}
				else{
					collisionArray[i][j] = true;
				}
			}
			
		}
		conditionalTimedEntities = new ArrayList<ConditionalTimedObject>();
		int lowX = 0;
		int highX = 23;
		int lowZ = 0;
		int highZ = 23; 
		while(lowX <= highX && lowZ <= highZ){
			for(int i = lowX; i <= highX; i++){
				ConditionalTimedObject newObj = new ConditionalTimedObject(i, -1,lowZ);
				ConditionalTimedObject newObj2 = new ConditionalTimedObject(i, -1,highZ);
				Cube ceil = new Cube(i, 1, lowZ);
				Cube ceil2 = new Cube(i, 1, highZ);
				
				if((i != 1 || lowZ != 1) && (i != 1 || lowZ != 22) &&  (i != 22 || lowZ != 1) && (i != 22 || lowZ != 22)){
					if(!(i < 14 && i > 9) || !(lowZ < 14 && lowZ > 9) ){
						//if(i != 1 || lowZ != 1){
						newObj.setDynamic(true);
					}
				}
				if((i != 1 || highZ != 1) && (i != 1 || highZ != 22) &&  (i != 22 || highZ != 1) && (i != 22 || highZ != 22)){
					if(!(i < 14 && i > 9) || !(highZ < 14 && highZ > 9) ){

				//if(i != 21 || highZ != 21){
					newObj2.setDynamic(true);
					}
				}
				switch(lowX%6){
				case 0:
					newObj.setDiffuse(new float[]{1,0,0,1});
					newObj2.setDiffuse(new float[]{1,0,0,1});
					ceil.setDiffuse(new float[]{1,0,0,1});
					ceil2.setDiffuse(new float[]{1,0,0,1});
					break;
				case 1:
					newObj.setDiffuse(new float[]{0,1,0,1});
					newObj2.setDiffuse(new float[]{0,1,0,1});
					ceil.setDiffuse(new float[]{0,1,0,1});
					ceil2.setDiffuse(new float[]{0,1,0,1});
					break;
				case 2:
					newObj.setDiffuse(new float[]{0,0,1,1});
					newObj2.setDiffuse(new float[]{0,0,1,1});
					ceil.setDiffuse(new float[]{0,0,1,1});
					ceil2.setDiffuse(new float[]{0,0,1,1});
					break;
				case 3:
					newObj.setDiffuse(new float[]{1,1,0,1});
					newObj2.setDiffuse(new float[]{1,1,0,1});
					ceil.setDiffuse(new float[]{1,1,0,1});
					ceil2.setDiffuse(new float[]{1,1,0,1});
					break;
				case 4:
					newObj.setDiffuse(new float[]{1,0,1,1});
					newObj2.setDiffuse(new float[]{1,0,1,1});
					ceil.setDiffuse(new float[]{1,0,1,1});
					ceil2.setDiffuse(new float[]{1,0,1,1});
					break;
				case 5:
					newObj.setDiffuse(new float[]{0,1,1,1});
					newObj2.setDiffuse(new float[]{0,1,1,1});
					ceil.setDiffuse(new float[]{0,1,1,1});
					ceil2.setDiffuse(new float[]{0,1,1,1});
					break;
					
				default:
					newObj.setDiffuse(new float[]{1,1,1,1});
					newObj2.setDiffuse(new float[]{1,1,1,1});
					ceil.setDiffuse(new float[]{1,1,1,1});
					ceil2.setDiffuse(new float[]{1,1,1,1});
					break;
				}
				conditionalTimedEntities.add(newObj);
				conditionalTimedEntities.add(newObj2);
				staticEntities.add(ceil);
				staticEntities.add(ceil2);
			}
			for(int j = lowZ; j <= highZ; j++){
				ConditionalTimedObject newObj = new ConditionalTimedObject(lowX, -1,j);
				ConditionalTimedObject newObj2 = new ConditionalTimedObject(highX, -1,j);
				Cube ceil = new Cube(lowX, 1, j);
				Cube ceil2 = new Cube(highX, 1, j);
				if((j != 1 || lowX != 1) && (j != 1 || lowX != 22) &&  (j != 22 || lowX != 1) && (j != 22 || lowX != 22)){
					if(!(j < 14 && j > 9) || !(lowX < 14 && lowX > 9) ){
						newObj.setDynamic(true);
					}
				}
				if((j != 1 || highX != 1) && (j != 1 || highX != 22) &&  (j != 22 || highX != 1) && (j != 22 || highX != 22)){
					if(!(j < 14 && j > 9) || !(highX < 14 && highX > 9) ){
						newObj2.setDynamic(true);
					}
				}
				switch(lowZ%6){
				case 0:
					newObj.setDiffuse(new float[]{1,0,0,1});
					newObj2.setDiffuse(new float[]{1,0,0,1});
					ceil.setDiffuse(new float[]{1,0,0,1});
					ceil2.setDiffuse(new float[]{1,0,0,1});
					break;
				case 1:
					newObj.setDiffuse(new float[]{0,1,0,1});
					newObj2.setDiffuse(new float[]{0,1,0,1});
					ceil.setDiffuse(new float[]{0,1,0,1});
					ceil2.setDiffuse(new float[]{0,1,0,1});
					break;
				case 2:
					newObj.setDiffuse(new float[]{0,0,1,1});
					newObj2.setDiffuse(new float[]{0,0,1,1});
					ceil.setDiffuse(new float[]{0,0,1,1});
					ceil2.setDiffuse(new float[]{0,0,1,1});
					break;
				case 3:
					newObj.setDiffuse(new float[]{1,1,0,1});
					newObj2.setDiffuse(new float[]{1,1,0,1});
					ceil.setDiffuse(new float[]{1,1,0,1});
					ceil2.setDiffuse(new float[]{1,1,0,1});
					break;
				case 4:
					newObj.setDiffuse(new float[]{1,0,1,1});
					newObj2.setDiffuse(new float[]{1,0,1,1});
					ceil.setDiffuse(new float[]{1,0,1,1});
					ceil2.setDiffuse(new float[]{1,0,1,1});
					break;
				case 5:
					newObj.setDiffuse(new float[]{0,1,1,1});
					newObj2.setDiffuse(new float[]{0,1,1,1});
					ceil.setDiffuse(new float[]{0,1,1,1});
					ceil2.setDiffuse(new float[]{0,1,1,1});
					break;
					
				default:
					newObj.setDiffuse(new float[]{1,1,1,1});
					newObj2.setDiffuse(new float[]{1,1,1,1});
					ceil.setDiffuse(new float[]{0,1,1,1});
					ceil2.setDiffuse(new float[]{0,1,1,1});
					break;
				}
				
				conditionalTimedEntities.add(newObj);
				conditionalTimedEntities.add(newObj2);
				staticEntities.add(ceil);
				staticEntities.add(ceil2);
			}
			
			for(int i = -1; i <24; i++){
				Cube newCube = new Cube(i,0,-1);
				newCube.setDiffuse(new float[]{0,1,1,1});
				staticEntities.add(newCube);
				Cube newCube2 = new Cube(i,0,24);
				newCube2.setDiffuse(new float[]{0,1,1,1});
				staticEntities.add(newCube2);
				Cube newCube3 = new Cube(-1,0,i);
				newCube3.setDiffuse(new float[]{0,1,1,1});
				staticEntities.add(newCube3);
				Cube newCube4 = new Cube(24,0,i);
				newCube4.setDiffuse(new float[]{0,1,1,1});
				staticEntities.add(newCube4);
			}
			lowX = lowX +1;
			lowZ = lowZ +1;
			highX = highX-1;
			highZ = highZ-1;
		}
		grabableEntities = new ArrayList<GrabableObject>();
		GrabableObject greenBox = new GrabableObject(10, 0, 10, .25f, .25f, .25f);
		greenBox.setObject("cube");
		greenBox.setDiffuse(new float[]{0f,1f,0f,0f});
		greenBox.setDestX(1);
		greenBox.setDestZ(1);
		grabableEntities.add(greenBox);
		
		BasicObject greenEnd = new BasicObject(1,-.5f,1,.5f,.25f,.5f);
		greenEnd.setDiffuse(new float[]{0f,1f,0f,0f});
		staticEntities.add(greenEnd);
		
		GrabableObject blueBox = new GrabableObject(13, 0, 13, .25f, .25f, .25f);
		blueBox.setObject("cube");
		blueBox.setDiffuse(new float[]{0f,0f,1f,0f});
		blueBox.setDestX(22);
		blueBox.setDestZ(22);
		grabableEntities.add(blueBox);
		
		BasicObject blueEnd = new BasicObject(22,-.5f,22,.5f,.25f,.5f);
		blueEnd.setDiffuse(new float[]{0f,0f,1f,0f});
		staticEntities.add(blueEnd);
		
		GrabableObject redBox = new GrabableObject(13, 0, 10, .25f, .25f, .25f);
		redBox.setObject("cube");
		redBox.setDiffuse(new float[]{1f,0f,0f,0f});
		redBox.setDestX(22);
		redBox.setDestZ(1);
		grabableEntities.add(redBox);
		
		BasicObject redEnd = new BasicObject(22,-.5f,1,.5f,.25f,.5f);
		redEnd.setDiffuse(new float[]{1f,0f,0f,0f});
		staticEntities.add(redEnd);
		
		GrabableObject purpleBox = new GrabableObject(10, 0, 13, .25f, .25f, .25f);
		purpleBox.setObject("cube");
		purpleBox.setDiffuse(new float[]{1f,0f,1f,0f});
		purpleBox.setDestX(1);
		purpleBox.setDestZ(22);
		grabableEntities.add(purpleBox);

		BasicObject purpleEnd = new BasicObject(1,-.5f,22,.5f,.25f,.5f);
		purpleEnd.setDiffuse(new float[]{1f,0f,1f,0f});
		staticEntities.add(purpleEnd);
		
		Level2EndObject endObject = new Level2EndObject(11.5f,-2f,11.5f);
		endObject.setFunction(InteractiveObject.Function.NOTHING);
		endObject.setObject("sphere");
		endObject.radius = 1;
		interactiveEntities.add(endObject);
		
	}
	
	public ArrayList<BasicObject> getStaticEntities(){
		return staticEntities;
	}
	
	public int getStartX(){
		return 12;
	}
	public int getStartZ(){
		return 1;
	}
	
	@Override
	public String getLevelMusic(){
		return "chipmaze.wav";
	}
}
