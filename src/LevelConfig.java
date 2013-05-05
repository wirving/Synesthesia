import java.util.ArrayList;


public class LevelConfig {

	private ArrayList<BasicObject> staticEntities;
	private ArrayList<BasicObject> dynamicEntities;
	private ArrayList<InteractiveObject> interactiveEntities;
	private ArrayList<CollisionObject> collisionObjects;
	private Boolean[][] collisionArray; 
	
	public LevelConfig(ArrayList<BasicObject> staticEnt, ArrayList<InteractiveObject> interactiveEnt, ArrayList<CollisionObject> colls, Boolean[][] collision){
		this.staticEntities = staticEnt;
		//this.dynamicEntities = level.getDynamicEntities();
		this.interactiveEntities = interactiveEnt;
		this.collisionObjects = colls;
		this.collisionArray = collision;
	}
	
	public LevelConfig(ArrayList<BasicObject> staticEnt){
		this.staticEntities = staticEnt;
		//this.dynamicEntities = level.getDynamicEntities();
		//this.interactiveEntities = interactiveEnt;
		//this.collisionArray = level.getCollisionArray();
	}
	
	public ArrayList<BasicObject> getStaticEntities(){
		return staticEntities;

	}
	
	public ArrayList<BasicObject> getdynamicEntities(){
		return dynamicEntities;
	}
	
	public ArrayList<InteractiveObject> getInteractiveEntities(){
		return interactiveEntities;
	}
	
	public ArrayList<CollisionObject> getCollisionEntities(){
		return collisionObjects;
	}
	
	public Boolean[][] getCollisionArray(){
		return collisionArray;
	}
	
	@Override
	public boolean equals(Object o){
		
		LevelConfig other = (LevelConfig)o;
		if ((other.getCollisionArray() == this.collisionArray) && (other.getStaticEntities() == this.staticEntities) && (other.getInteractiveEntities() == this.interactiveEntities))
			return true;
		
		else return false;
	}
}
