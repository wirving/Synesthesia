import java.util.ArrayList;


public class Transition2 extends BasicLevel{

	public Transition2(){
		staticEntities = new ArrayList<BasicObject>();
		interactiveEntities = new ArrayList<InteractiveObject>();
		BasicObject floor = new BasicObject(4.5f,-1,4.5f,10,1,10);
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
				collisionArray[0][j] = true;
				collisionArray[i][0] = true;
				collisionArray[i][9] = true;
				collisionArray[9][j] = true;
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
		
		
		DropObject drop5 = new DropObject(8, 350, 7);
		drop5.setObject("sphere");
		staticEntities.add(drop5);
		DropObject drop6 = new DropObject(7, 350, 8);
		drop6.setObject("sphere");
		staticEntities.add(drop6);
		DropObject drop7 = new DropObject(7, 350, 7);
		drop7.setObject("sphere");
		staticEntities.add(drop7);
		DropObject drop8 = new DropObject(8, 350, 8);
		drop8.setObject("sphere");
		staticEntities.add(drop8);
		
		DropObject drop9 = new DropObject(2, 250, 3);
		drop9.setObject("sphere");
		staticEntities.add(drop9);
		DropObject drop10 = new DropObject(3, 250, 2);
		drop10.setObject("sphere");
		staticEntities.add(drop10);
		DropObject drop11 = new DropObject(2, 250, 2);
		drop11.setObject("sphere");
		staticEntities.add(drop11);
		DropObject drop12 = new DropObject(3, 250, 3);
		drop12.setObject("sphere");
		staticEntities.add(drop12);
		
		DropObject drop13 = new DropObject(4,150,4);
		drop13.setObject("sphere");
		staticEntities.add(drop13);
		DropObject drop14 = new DropObject(5,150,5);
		drop14.setObject("sphere");
		staticEntities.add(drop14);
		DropObject drop15 = new DropObject(5,150,4);
		drop15.setObject("sphere");
		staticEntities.add(drop15);
		DropObject drop16 = new DropObject(4,150,5);
		drop16.setObject("sphere");
		staticEntities.add(drop16);
		
		textObjects = new ArrayList<TextObject>();
		
		TextObject text1 = new TextObject(5,475,0,.01f,.005f,.005f,1,0f,0);
		text1.setRotDegree(-90);
		text1.setDiffuse(new float[] {1f,1f,0f,0f});
		text1.setText("OH NO!");
		textObjects.add(text1);
		
		TextObject text2 = new TextObject(5,400,10,.01f,.01f,.01f,1,0f,0);
		text2.setRotDegree(-90);
		text2.setDiffuse(new float[] {1f,.5f,1f,0f});
		text2.setText("LOOKS LIKE YOU'RE FALLING");
		textObjects.add(text2);
		
		TextObject text3 = new TextObject(12,425,12,.01f,.01f,.01f,0,2f,0);
		text3.setRotDegree(-90);
		text3.setDiffuse(new float[] {1f,1f,0f,0f});
		text3.setText("WILL YOU SURVIVE?");
		textObjects.add(text3);
		
		TextObject text4 = new TextObject(-50,450,10,.05f,.05f,.05f,0,2f,0);
		text4.setRotDegree(45);
		text4.setDiffuse(new float[] {1f,.5f,.5f,0f});
		text4.setText("CAREFUL... ");
		textObjects.add(text4);
		
		TextObject text5 = new TextObject(5,300,0,.01f,.01f,.01f,0,1f,1f);
		text5.setRotDegree(-90);
		text5.setDiffuse(new float[] {1f,.5f,.5f,0f});
		text5.setText("WHAT SORTS OF MONSTERS LURK IN THIS ABYSS? ... ");
		textObjects.add(text5);
		
		TextObject text6 = new TextObject(0,250,5,.01f,.01f,.01f,0,1f,1f);
		text6.setRotDegree(-90);
		text6.setDiffuse(new float[] {.5f,1f,1f,0f});
		text6.setText("... THIS ABYSS BETWEEN WORLDS ...");
		textObjects.add(text6);
		
		TextObject text7 = new TextObject(5,200,0,.01f,.01f,.01f,1,0f,0);
		text7.setRotDegree(90);
		text7.setDiffuse(new float[] {0f,1f,1f,0f});
		text7.setText("THERE'S NOTHING HERE... BUT MAYBE...?");
		textObjects.add(text7);
		
		TextObject text8 = new TextObject(0,125,5,.01f,.01f,.01f,1,0f,0);
		text8.setRotDegree(-90);
		text8.setDiffuse(new float[] {1f,1f,1f,0f});
		text8.setText("WHAT'S THAT BEHIND YOU?");
		textObjects.add(text8);
		
		//Random objects
		BasicObject cube = new BasicObject(40, 400, 40, 5f,5f, 5f, 20f, 0f, 0f);
		cube.setObject("cube");
		cube.setDiffuse(cube.getRandomDiffuseColor());
		staticEntities.add(cube);
		
		BasicObject cube2 = new BasicObject(-40, 350, 40, 5f,5f, 5f, 45f, 0f, 0f);
		cube2.setObject("cube");
		cube2.setDiffuse(cube.getRandomDiffuseColor());
		staticEntities.add(cube2);
		
		BasicObject plane = new BasicObject(80, 400, 80, 40f, 1f, 40f, 0f, 0f, 0f);
		plane.setObject("plane");
		plane.setDiffuse(plane.getRandomDiffuseColor());
		staticEntities.add(plane);
		
		BasicObject plane2 = new BasicObject(20, 200, 10, 20f, 1f, 20f, 0f, 20f, -90f);
		plane2.setObject("plane");
		plane2.setDiffuse(plane.getRandomDiffuseColor());
		staticEntities.add(plane2);
		
		BasicObject plane3 = new BasicObject(-10, 175, -20, 60f, 1f, 60f, 0f, -20f, -90f);
		plane3.setObject("plane");
		plane3.setDiffuse(plane.getRandomDiffuseColor());
		staticEntities.add(plane3);
		
		BasicObject cube3 = new BasicObject(-40, 125, -40, 5f,5f, 5f, 20f, 0f, 0f);
		cube3.setObject("cube");
		cube3.setDiffuse(cube.getRandomDiffuseColor());
		staticEntities.add(cube3);
		
		BasicObject cube4 = new BasicObject(0, 200, 10, 5f,5f, 5f, 20f, 0f, 0f);
		cube4.setObject("cube");
		cube4.setDiffuse(cube.getRandomDiffuseColor());
		staticEntities.add(cube4);
		
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
