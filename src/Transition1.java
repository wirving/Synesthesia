import java.util.ArrayList;

import javax.media.opengl.GL2;


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
		
		textObjects = new ArrayList<TextObject>();
		
		TextObject text1 = new TextObject(15,2,-2,.01f,.005f,.005f,0,2f,0);
		text1.setRotDegree(-90);
		text1.setDiffuse(new float[] {1f,1f,0f,0f});
		text1.setText("A WALK IN THE DARK");
		textObjects.add(text1);
		
		TextObject text2 = new TextObject(30,-.25f,-3,.005f,.005f,.005f,0,1f,0);
		text2.setRotDegree(255);
		text2.setDiffuse(new float[] { 0f, 1f, 1f, 0f});
		text2.setText("TIME TO REFLECT ON YOUR CURRENT SITUATION");
		textObjects.add(text2);
		
		TextObject text3 = new TextObject(35,1,-4,.005f,.005f,.005f,0,1,0);
		text3.setRotDegree(-45);
		text3.setDiffuse(new float[] { 1f, 1f, 1f, 0f});
		text3.setText("SURE IS EMPTY HERE, ISN'T IT?");
		textObjects.add(text3);
		
		TextObject text4 = new TextObject(55,1.5f,4,.005f,.005f,.005f,0,1f,0);
		text4.setRotDegree(255);
		text4.setDiffuse(new float[] { 1f, 0f, 0f, 0f});
		text4.setText("THERE'S SOMETHING BEYOND THE ROAD");
		textObjects.add(text4);
		
		TextObject text5 = new TextObject(58,0,-12,.005f,.005f,.005f,0,1f,0);
		text5.setRotDegree(-45);
		text5.setDiffuse(new float[] { 0f, 0f, 1f, 0f});
		text5.setText("DEEP IN THE DARK, CAN YOU SEE IT?");
		textObjects.add(text5);
		
		TextObject text6 = new TextObject(80,-.05f,4,.005f,.005f,.005f,0,1f,0);
		text6.setRotDegree(255);
		text6.setDiffuse(new float[]{ 0f, 1f, 0f, 0f});
		text6.setText("MIGHT AS WELL GO, THERE'S NOTHING ELSE");
		textObjects.add(text6);
		
		TextObject text7 = new TextObject(85,0,-8,.005f,.005f,.005f,0,1f,0);
		text7.setRotDegree(-45);
		text7.setDiffuse(new float[]{ 1f, 1f, 1f, 0f});
		text7.setText("UNLESS... YOU'D LIKE TO STAY...");
		textObjects.add(text7);
		
		TextObject text8 = new TextObject(100,1.5f,-3f,.005f,.005f,.005f,0,-1,0);
		text8.setRotDegree(90);
		text8.setDiffuse(new float[]{ 1f, 1f, 1f, 0f});
		text8.setText("AND WRITE WORDS IN THE SKY");
		textObjects.add(text8);
		
		TextObject text9 = new TextObject(100,-1,2f,.01f,.01f,.01f,1,0,0);
		text9.setRotDegree(-90);
		text9.setDiffuse(new float[]{ .8f, 0f, 1f, 0f});
		text9.setText("LET MY WORDS GUIDE YOU TO WHERE YOU THINK YOU NEED TO GO");
		textObjects.add(text9);
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
