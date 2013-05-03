import java.util.ArrayList;


public class LevelTwo extends BasicLevel{

	private ArrayList<BasicObject> staticEntities;
	private ArrayList<BasicObject> dynamicEntities;
	private Boolean[][] collisionArray; 
	
	public LevelTwo(){
		staticEntities = new ArrayList<BasicObject>();
		collisionArray = new Boolean[25][25];
		int lowX = 0;
		int highX = 23;
		int lowZ = 0;
		int highZ = 23; 
		while(lowX <= highX && lowZ <= highZ){
			for(int i = lowX; i <= highX; i++){
				BasicObject newObj = new BasicObject(i, -1,lowZ);
				BasicObject newObj2 = new BasicObject(i, -1,highZ);
				switch(lowX%6){
				case 0:
					newObj.setDiffuse(new float[]{1,0,0,1});
					newObj2.setDiffuse(new float[]{1,0,0,1});
					break;
				case 1:
					newObj.setDiffuse(new float[]{0,1,0,1});
					newObj2.setDiffuse(new float[]{0,1,0,1});
					break;
				case 2:
					newObj.setDiffuse(new float[]{0,0,1,1});
					newObj2.setDiffuse(new float[]{0,0,1,1});
					break;
				case 3:
					newObj.setDiffuse(new float[]{1,1,0,1});
					newObj2.setDiffuse(new float[]{1,1,0,1});
					break;
				case 4:
					newObj.setDiffuse(new float[]{1,0,1,1});
					newObj2.setDiffuse(new float[]{1,0,1,1});
					break;
				case 5:
					newObj.setDiffuse(new float[]{0,1,1,1});
					newObj2.setDiffuse(new float[]{0,1,1,1});
					break;
					
				default:
					newObj.setDiffuse(new float[]{1,1,1,1});
					newObj2.setDiffuse(new float[]{1,1,1,1});
					break;
				}
				staticEntities.add(newObj);
				staticEntities.add(newObj2);
			}
			for(int j = lowZ; j <= highZ; j++){
				BasicObject newObj = new BasicObject(lowX, -1,j);
				BasicObject newObj2 = new BasicObject(highX, -1,j);
				switch(lowZ%6){
				case 0:
					newObj.setDiffuse(new float[]{1,0,0,1});
					newObj2.setDiffuse(new float[]{1,0,0,1});
					break;
				case 1:
					newObj.setDiffuse(new float[]{0,1,0,1});
					newObj2.setDiffuse(new float[]{0,1,0,1});
					break;
				case 2:
					newObj.setDiffuse(new float[]{0,0,1,1});
					newObj2.setDiffuse(new float[]{0,0,1,1});
					break;
				case 3:
					newObj.setDiffuse(new float[]{1,1,0,1});
					newObj2.setDiffuse(new float[]{1,1,0,1});
					break;
				case 4:
					newObj.setDiffuse(new float[]{1,0,1,1});
					newObj2.setDiffuse(new float[]{1,0,1,1});
					break;
				case 5:
					newObj.setDiffuse(new float[]{0,1,1,1});
					newObj2.setDiffuse(new float[]{0,1,1,1});
					break;
					
				default:
					newObj.setDiffuse(new float[]{1,1,1,1});
					newObj2.setDiffuse(new float[]{1,1,1,1});
					break;
				}
				
				staticEntities.add(newObj);
				staticEntities.add(newObj2);
			}
			lowX = lowX +1;
			lowZ = lowZ +1;
			highX = highX-1;
			highZ = highZ-1;
		}
		/*for(int i = 0; i < 23; i++){
			for(int j = 0; j < 23; j++){
				float y = -1;
				if(i == 0 || i == 22 || j == 0 || j == 22){
					y = 0;
				}
				BasicObject newObj = new BasicObject(i,y,j);
				BasicObject ceiling = new BasicObject(i,1,j);
				
				//if(i != -1 && i != 25 && j != -1 && j != 25){
					//newObj.setDynamic(true);
				//}
				
				switch(i%6){
				case 0:
					newObj.setDiffuse(new float[]{1,0,0,1});
					ceiling.setDiffuse(new float[]{1,0,0,1});
					break;
				case 1:
					newObj.setDiffuse(new float[]{0,1,0,1});
					ceiling.setDiffuse(new float[]{0,1,0,1});
					break;
				case 2:
					newObj.setDiffuse(new float[]{0,0,1,1});
					ceiling.setDiffuse(new float[]{0,0,1,1});
					break;
				case 3:
					newObj.setDiffuse(new float[]{1,1,0,1});
					ceiling.setDiffuse(new float[]{1,1,0,1});
					break;
				case 4:
					newObj.setDiffuse(new float[]{1,0,1,1});
					ceiling.setDiffuse(new float[]{1,0,1,1});
					break;
				case 5:
					newObj.setDiffuse(new float[]{0,1,1,1});
					ceiling.setDiffuse(new float[]{0,1,1,1});
					break;
					
				default:
					newObj.setDiffuse(new float[]{1,1,1,1});
					ceiling.setDiffuse(new float[]{1,1,1,1});
					break;
				}
				staticEntities.add(newObj);
				staticEntities.add(ceiling);
				
			}
		}*/

	}
	
	public ArrayList<BasicObject> getStaticEntities(){
		return staticEntities;
	}
	
	public int getStartX(){
		return 10;
	}
	public int getStartZ(){
		return 10;
	}
}
