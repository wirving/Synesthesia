import java.util.ArrayList;


public class LevelOne {

	private ArrayList<BasicObject> staticEntities;
	private ArrayList<BasicObject> dynamicEntities;
	private Boolean[][] collisionArray; 
	
	public LevelOne(){
		staticEntities = new ArrayList<BasicObject>();
		collisionArray = new Boolean[25][25];
		for(int i = -1; i < 26; i++){
			for(int j = -1; j < 26; j++){
				float y = -1;
				if(i == -1 || i == 25 || j == -1 || j == 25){
					y = 0;
				}
				BasicObject newObj = new BasicObject(i,y,j);
				BasicObject ceiling = new BasicObject(i,1,j);
				
				if(i != -1 && i != 25 && j != -1 && j != 25){
					newObj.setDynamic(true);
				}
				
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
		}

	}
	
	public ArrayList<BasicObject> getStaticEntities(){
		return staticEntities;
	}
}
