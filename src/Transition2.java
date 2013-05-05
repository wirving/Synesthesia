import java.util.ArrayList;


public class Transition2 extends BasicLevel{

	public Transition2(){
		staticEntities = new ArrayList<BasicObject>();
		interactiveEntities = new ArrayList<InteractiveObject>();
		BasicObject floor = new BasicObject(4.5f,-1,4.5f,5,1,5);
		floor.setObject("sphere");
		TextureParameters sphere_tex = new TextureParameters();
		sphere_tex.setTextured(true);
		sphere_tex.setTextureName("sky");
		sphere_tex.setTexGenMode(TextureParameters.texCoordGenMode.SPHERE_MAP);
		floor.setTexParams(sphere_tex);
		float[] tempDiffuse = {1f,1f,1f,1};
		floor.setDiffuse(tempDiffuse);
		staticEntities.add(floor);
		
		collisionArray = new Boolean[10][10];
		for(int i = 0; i<10; i++){
			for(int j = 0; j<10; j++){
				collisionArray[i][j] = false;
			}
		}
		
		DropObject drop1 = new DropObject(4,450,4);
		drop1.setObject("sphere");
		staticEntities.add(drop1);
		
		DropObject drop2 = new DropObject(5,450,5);
		drop2.setObject("sphere");
		staticEntities.add(drop2);
		
		DropObject drop3 = new DropObject(5,450,4);
		drop3.setObject("sphere");
		staticEntities.add(drop3);
		
		DropObject drop4 = new DropObject(4,450,5);
		drop4.setObject("sphere");
		staticEntities.add(drop4);
		
		textObjects = new ArrayList<TextObject>();
		
		TextObject text1 = new TextObject(5,475,0,.01f,.005f,.005f,0,2f,0);
		text1.setRotDegree(-90);
		text1.setDiffuse(new float[] {1f,1f,0f,0f});
		text1.setText("A WALK IN THE DARK");
		textObjects.add(text1);
	}
	
	@Override
	public int getStartY(){
		return 500;
	}
	
	@Override
	public int getStartX(){
		return 4;
	}
	@Override
	public int getStartZ(){
		return 4;
	}
	
	@Override
	public String getLevelMusic(){
		return "Trans2.wav";
	}
}
