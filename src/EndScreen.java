import java.util.ArrayList;
import java.util.Arrays;


public class EndScreen extends BasicLevel{

	public EndScreen(){
		staticEntities = new ArrayList<BasicObject>();
		interactiveEntities = new ArrayList<InteractiveObject>();
		textObjects = new ArrayList<TextObject>();
		collisionArray = new Boolean[6][31];
		
		InteractiveObject restart = new InteractiveObject(3f, -.25f, 28, .5f, .5f, .5f);
		restart.setObject("cube");
		restart.setFunction(InteractiveObject.Function.RESTART);
		restart.radius = 2;
		interactiveEntities.add(restart);
		
		for (Boolean[] row : collisionArray){
			Arrays.fill(row, false);
			}
		
		for (int x = 0; x < 6; x++){
			for (int z = 0; z < 30; z++){
				
				collisionArray[1][z] = true;
				collisionArray[x][0] = true;
				collisionArray[5][z] = true;
				collisionArray[x][30] = true;
				
			}
		}
		
		collisionArray[3][28] = true;
		
		BasicObject floor = new BasicObject(3, -0.5f, 15, 3f, 1f, 30f);
		floor.setDiffuse(new float[]{1f, 1f, .5f});
		staticEntities.add(floor);
		floor.setObject("plane");
		
		TextObject text1 = new TextObject(-30,100f,-200,.3f,.3f,.3f,0,2f,0);
		text1.setRotDegree(45);
		text1.setDiffuse(new float[] {1f,1f,1f,1f});
		text1.setSpecular(new float[]{1f,1f,1f,1f});
		text1.setText("THAT'S IT");
		textObjects.add(text1);
		
		TextObject text2 = new TextObject(-30,80f,-200,.2f,.2f,.2f,0,2f,0);
		text2.setRotDegree(45);
		text2.setDiffuse(new float[] {1f,.5f,.5f,1f});
		text2.setSpecular(new float[]{1f,1f,1f,1f});
		text2.setText("YOU CAN GO HOME");
		textObjects.add(text2);
		
		TextObject text3 = new TextObject(-30,40f,-200,.1f,.1f,.1f,0,2f,0);
		text3.setRotDegree(45);
		text3.setDiffuse(new float[] {.3f,.8f,.5f});
		text3.setSpecular(new float[]{1f,1f,1f,1f});
		text3.setText("THERE'S NOTHING ELSE HERE...");
		textObjects.add(text3);
		
		TextObject text4 = new TextObject(-30,30f,-200,.1f,.1f,.1f,0,2f,0);
		text4.setRotDegree(45);
		text4.setDiffuse(new float[] {.3f,.5f,.8f});
		text4.setSpecular(new float[]{1f,1f,1f,1f});
		text4.setText("...EXCEPT FOR THIS TEXT");
		textObjects.add(text4);
		
		TextObject text5 = new TextObject(-30,10f,-200,.1f,.1f,.1f,0,2f,0);
		text5.setRotDegree(45);
		text5.setDiffuse(new float[] {1f,1f,.5f});
		text5.setSpecular(new float[]{1f,1f,1f,1f});
		text5.setText("...OR IS THERE?");
		textObjects.add(text5);
		
		
		
		
	}
	
	@Override
	public String getLevelMusic(){
		return "endscreen.wav";
	}
	
	public int getStartX(){
		return 3;
	}
	
	public int getStartZ(){
		return 24;
	}
}
