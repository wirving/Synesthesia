import java.util.ArrayList;


public class BasicLevel {


<<<<<<< HEAD
		//Turn off by default
		boolean hasTrigger = false;
		boolean eventTriggered = false;
		boolean levelUpdated = false;

		protected ArrayList<BasicObject> staticEntities;
		private ArrayList<BasicObject> dynamicEntities;
		protected ArrayList<InteractiveObject> interactiveEntities;
		protected ArrayList<GrabableObject> grabableEntities;
		protected ArrayList<ConditionalTimedObject> conditionalTimedEntities;
		protected ArrayList<TextObject> textObjects;
=======
		protected ArrayList<BasicObject> staticEntities;
		private ArrayList<BasicObject> dynamicEntities;
		protected ArrayList<InteractiveObject> interactiveEntities;
		protected ArrayList<CollisionObject> collisionObjects;
>>>>>>> origin/Working
		protected Boolean[][] collisionArray; 
		
		public BasicLevel(){
			staticEntities = new ArrayList<BasicObject>();
			//collisionArray = new Boolean[25][25];
			/*Boolean[][] tempArray = { 
					{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
					{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
					{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
					{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
					{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
					{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
					{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
					{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
					{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
					{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
					{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
					{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
					{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
					{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
					{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
					{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
					{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
					{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
					{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
					{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
					{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
					{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
					
					};
					*/
			//collisionArray = tempArray;
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
						newObj.setDynamic(false);
					}
					//else if(collisionArray[i][j] == true){
					//	newObj.setDynamic(true);
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
			}

		}
		
		public ArrayList<BasicObject> getStaticEntities(){
			return staticEntities;
		}
		
		public int getStartX(){
			return 0;
		}
		
		public int getStartY(){
			return 0;
		}
		public int getStartZ(){
			return 0;
		}
		
		public float getStartRotv(){
			return 0;
		}
		
		public float getStartRoth(){
			return 0;
		}
		
		public String getLevelMusic(){
			return "maze.wav";
		}
		
		public Boolean[][] getCollisionArray(){
			return collisionArray;
		}
		
		public ArrayList<InteractiveObject> getInteractiveEntities(){
			return interactiveEntities;
		}
		
<<<<<<< HEAD
		public ArrayList<GrabableObject> getGrabableEntities(){
			return grabableEntities;
		}
		
		public ArrayList<ConditionalTimedObject> getConditionalTimedObjects(){
			return conditionalTimedEntities;
		}
		
		public ArrayList<TextObject> getTextObjects(){
			return textObjects;
=======
		public ArrayList<CollisionObject> getCollisionEntities(){
			return collisionObjects;
>>>>>>> origin/Working
		}
		
		public void eventHappened(){
			
			//Change something
		}
		
		//Check a condition to start a change in the level
		public boolean checkTrigger(float xpos, float ypos, float zpos){
			return false;
		}
		
<<<<<<< HEAD
=======
		public ArrayList<BasicObject> getDynamicEntities(){
			return dynamicEntities;
		}
>>>>>>> origin/Working
		
		
	}

