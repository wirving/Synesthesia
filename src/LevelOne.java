import java.util.ArrayList;


public class LevelOne extends BasicLevel {

	private static final boolean F = false;
	private static final boolean T = true;
	private ArrayList<BasicObject> dynamicEntities;
	private Boolean[][] collisionArray; 
	
	public LevelOne(){
		staticEntities = new ArrayList<BasicObject>();
		interactiveEntities = new ArrayList<InteractiveObject>();
		
		//collisionArray = new Boolean[25][25];
		Boolean[][] tempArray = { 
				{T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T},
				{T, F, F, F, F, T, F, F, F, T, F, T, F, F, F, F, F, F, F, F, F, T}, //Start Row
				{T, T, F, T, F, T, F, T, F, F, F, F, F, T, F, T, T, T, T, T, F, T},
				{T, T, F, T, F, F, F, T, T, F, F, F, T, F, F, T, F, F, F, F, F, T},
				{T, F, F, F, T, T, T, F, F, T, F, T, F, F, T, T, F, T, T, T, T, T},
				{T, F, T, F, T, F, F, F, F, F, T, F, F, T, F, F, F, F, F, F, F, T},
				{T, F, T, F, F, T, T, T, T, F, T, T, F, F, F, T, F, T, T, T, F, T},
				{T, F, T, T, T, T, F, F, F, F, T, T, F, T, T, F, F, T, F, T, F, T},
				{T, F, F, F, F, F, F, T, T, T, F, F, F, F, F, F, T, T, F, F, F, T},
				{T, T, T, T, F, F, T, F, F, F, T, F, T, F, F, T, F, F, T, T, T, T},
				{T, F, F, F, F, T, T, F, T, F, F, F, T, F, F, F, F, F, F, F, F, T},
				{T, F, T, F, T, F, T, F, F, T, T, T, F, F, F, T, T, T, F, F, F, T},
				{T, F, F, F, F, F, F, T, F, T, F, F, F, T, F, F, F, F, T, F, F, T},
				{T, F, F, T, F, T, F, T, F, T, F, F, T, F, T, F, F, F, F, T, F, T},
				{T, T, T, F, F, T, F, T, F, F, F, T, F, F, F, T, F, T, F, F, F, T},
				{T, F, F, F, T, T, F, T, F, F, T, F, F, T, F, F, F, T, F, T, T, T},
				{T, T, F, T, F, T, F, F, T, T, T, F, T, F, T, T, F, F, F, F, F, T},
				{T, F, F, F, F, T, F, F, F, F, F, F, T, F, F, F, T, T, F, F, F, T},
				{T, F, T, F, F, F, T, F, T, T, T, T, F, F, F, F, F, F, T, T, F, T},
				{T, F, T, T, T, T, T, F, T, F, F, F, F, F, F, F, F, F, F, F, T, T},
				{T, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, T},
				{T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T, T},
				
				};
		collisionArray = tempArray;
		for(int i = 0; i < 22; i++){
			for(int j = 0; j < 22; j++){
				float y = -1;
				if(i == 0 || i == 21 || j == 0 || j == 21){
					y = 0;
				}
				Cube newObj = new Cube(i,y,j,1,1,1);
				Cube ceiling = new Cube(i,1,j,1,1,1);
				newObj.setObject("cube");
				ceiling.setObject("cube");
				
				if(i == 0 || i == 21 || j == 0 || j == 21){
					newObj.setDynamic(F);
				}
				else if(collisionArray[i][j] == T){
					newObj.setDynamic(T);
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
		InteractiveObject newEndObj = new InteractiveObject(20,0,20,1,1,1);
		newEndObj.setObject("bottle");
		newEndObj.setFunction(InteractiveObject.Function.NEXT_LEVEL);
		interactiveEntities.add(newEndObj);
	}
	
	public ArrayList<BasicObject> getStaticEntities(){
		return staticEntities;
	}
	
	public int getStartX(){
		return 1;
	}
	public int getStartZ(){
		return 10;
	}
	@Override
	public Boolean[][] getCollisionArray(){
		return collisionArray;
	}
}
