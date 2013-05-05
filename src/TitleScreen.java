import java.util.ArrayList;


public class TitleScreen extends BasicLevel{

	
	public TitleScreen(){
		staticEntities = new ArrayList<BasicObject>();
		interactiveEntities = new ArrayList<InteractiveObject>();
		textObjects = new ArrayList<TextObject>();
		
		TextObject text1 = new TextObject(-30,100f,-200,.3f,.3f,.3f,0,2f,0);
		text1.setRotDegree(-45);
		text1.setDiffuse(new float[] {1f,1f,1f,1f});
		text1.setSpecular(new float[]{1f,1f,1f,1f});
		text1.setText("SYNESTHESIA");
		textObjects.add(text1);
		
		TextObject text2 = new TextObject(-30,80f,-200,.2f,.2f,.2f,0,2f,0);
		text2.setRotDegree(-45);
		text2.setDiffuse(new float[] {1f,.5f,.5f,1f});
		text2.setSpecular(new float[]{1f,1f,1f,1f});
		text2.setText("AN ABSTRACT EXPLORATION GAME");
		textObjects.add(text2);
		
		TextObject text3 = new TextObject(-30,40f,-200,.1f,.1f,.1f,0,2f,0);
		text3.setRotDegree(-45);
		text3.setDiffuse(new float[] {.3f,.8f,.5f});
		text3.setSpecular(new float[]{1f,1f,1f,1f});
		text3.setText("WADS TO MOVE");
		textObjects.add(text3);
		
		TextObject text4 = new TextObject(-30,30f,-200,.1f,.1f,.1f,0,2f,0);
		text4.setRotDegree(-45);
		text4.setDiffuse(new float[] {.3f,.5f,.8f});
		text4.setSpecular(new float[]{1f,1f,1f,1f});
		text4.setText("SPACE TO INTERACT");
		textObjects.add(text4);
		
		TextObject text5 = new TextObject(-30,10f,-200,.1f,.1f,.1f,0,2f,0);
		text5.setRotDegree(-45);
		text5.setDiffuse(new float[] {1f,1f,.5f});
		text5.setSpecular(new float[]{1f,1f,1f,1f});
		text5.setText("PRESS SPACE TO BEGIN...");
		textObjects.add(text5);
		
	
	}
	
	public int getStartX(){
		return 0;
	}
	
	public int getStartZ(){
		return 26;
	}
}
