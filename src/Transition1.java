import java.util.ArrayList;


public class Transition1 extends BasicLevel{

	public Transition1(){
		staticEntities = new ArrayList<BasicObject>();
		interactiveEntities = new ArrayList<InteractiveObject>();
		BasicObject floor = new BasicObject(50,-1,1.5f,100,1,2);
		floor.setObject("cube");
		float[] tempDiffuse = {.5f,.5f,.5f,1};
		floor.setDiffuse(tempDiffuse);
		staticEntities.add(floor);
		
		collisionArray = new Boolean[160][5];
		for(int i = 0; i<4; i++){
			if(i != 1 && i != 2){
				for(int j = 0; j<160; j++){
					collisionArray[j][i] = true;
				}
			}
			else{
				for(int j = 0; j<160; j++){
					collisionArray[j][i] = false;
				}
			}
		}
		
		InteractiveObject newEndObj = new InteractiveObject(150,0,1.5f,1,1,1);
		newEndObj.setObject("tree_aspen");
		newEndObj.radius = 2;
		newEndObj.setFunction(InteractiveObject.Function.NEXT_LEVEL);
		interactiveEntities.add(newEndObj);
		
	}
	@Override
	public int getStartX(){
		return 1;
	}
	@Override
	public int getStartZ(){
		return 1;
	}
	
	@Override
	public String getLevelMusic(){
		return "transition1.wav";
	}
	
}
