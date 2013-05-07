import java.util.ArrayList;


public class Temple extends BasicLevel{
	
	private ArrayList<BasicObject> staticEntities;
	private ArrayList<BasicObject> dynamicEntities;
	private ArrayList<InteractiveObject> interactiveEntities;
	private ArrayList<CollisionObject> collisionObjects;
	private Boolean[][] collisionArray; 
	private ArrayList<Light> lights;
	InteractiveObject statue;
	float[] column_color = new float[]{.95f, .9f, .9f, 1f};
	float[] wall_color = new float[]{.65f, .6f, .6f, 1f}; //.6 .4 .3
	float[] zero_array = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
	String music;
		
	public boolean levelUpdated = false;
	
	public Temple(){
		
		music = "temptemple.wav";
		setStaticLights();
		hasTrigger = true;
		eventTriggered = false;
		staticEntities = new ArrayList<BasicObject>();
		collisionArray = new Boolean[84][11];
		interactiveEntities = new ArrayList<InteractiveObject>();
		collisionObjects = new ArrayList<CollisionObject>();
		
		for(int x = 0; x < 84; x++){
			for (int z = 0; z < 11; z++){
					
					if (z == 0 || z == 10){ //L, R walls
					collisionArray[x][z] = true;
					}
					else if (x == 0 || x == 83){ //Front, back walls
						collisionArray[x][z] = true;
					}
					
					else if (x % 9 == 0 && (z == 0 || z == 10) ) //Columns
						collisionArray[x][z] = true;
					
					else if((x == 79 || x == 80) && (z == 4 || z == 5 || z == 6)){ //Statue
						collisionArray[x][z] = true;
					}
					
					
					else{
						collisionArray[x][z] = false;
					}
				
			}
		}
		
	
		
		float[] column_color = new float[]{.95f, .9f, .9f, 1f};
		float[] wall_color = new float[]{.65f, .6f, .6f, 1f}; //.6 .4 .3
		float[] zero_array = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
		
		//Floor & ceiling
		BasicObject floor = new BasicObject(40, -1f, 5, 100, 2, 10);
		BasicObject ceiling = new BasicObject(-40, -19f, 5, 100, 2, 10, 0f, 0f, 180f);
		
		floor.setDiffuse(wall_color);
		ceiling.setDiffuse(wall_color);
		floor.setSpecular(zero_array);
		ceiling.setSpecular(zero_array);
		
		floor.setObject("plane");
		ceiling.setObject("plane");
		
		TextureParameters floor_tex = new TextureParameters();
		floor_tex.setTextured(true);
		floor_tex.setTextureName("floor");
		floor_tex.setTilingCoefficients(new float[]{8f, 1f, 1f});
		floor.setTexParams(floor_tex);
		floor_tex.setTexGenMode(TextureParameters.texCoordGenMode.PLANE);
		ceiling.setTexParams(floor_tex);
		
		staticEntities.add(floor);
		staticEntities.add(ceiling);

		//Columns & capitols
		for (int x = 0; x < 9; x++){
		BasicObject column_L = new BasicObject(10 * x, 10f, .5f, 1, 24, 1);
		BasicObject column_R = new BasicObject(10 * x, 10f, 9.5f, 1, 24, 1);
		
		BasicObject capitol_L_bot = new BasicObject(10 * x, -1f, 0f, 1.2f, .8f, 2f);
		BasicObject capitol_R_bot = new BasicObject(10 * x, -1f, 10f, 1.2f, .8f, 2f);
		BasicObject capitol_R_top = new BasicObject(10 * x, 19f, 10f, 1.2f, .8f, 2f);
		BasicObject capitol_L_top = new BasicObject(10 * x, 19f, 0f, 1.2f, .8f, 2f);

		TextureParameters columns = new TextureParameters();
		columns.setTexGenMode(TextureParameters.texCoordGenMode.SPHERE);
		columns.setTextured(true);
		columns.setTextureName("marble");
		
		
		column_L.setDiffuse(column_color);
		column_R.setDiffuse(column_color);
		capitol_L_bot.setDiffuse(column_color);
		capitol_R_bot.setDiffuse(column_color);
		capitol_L_top.setDiffuse(column_color);
		capitol_R_top.setDiffuse(column_color);


		column_L.setSpecular(zero_array);
		column_R.setSpecular(zero_array);
		capitol_L_bot.setSpecular(zero_array);
		capitol_R_bot.setSpecular(zero_array);
		capitol_L_top.setSpecular(zero_array);
		capitol_R_top.setSpecular(zero_array);


		column_L.setObject("cylinder");
		column_R.setObject("cylinder");
		capitol_L_bot.setObject("cube");
		capitol_R_bot.setObject("cube");
		capitol_R_top.setObject("cube");
		capitol_L_top.setObject("cube");

		column_L.setTexParams(columns);
		column_R.setTexParams(columns);
		capitol_L_bot.setTexParams(columns);
		capitol_L_top.setTexParams(columns);
		capitol_R_bot.setTexParams(columns);
		capitol_R_top.setTexParams(columns);
		
		staticEntities.add(column_L);
		staticEntities.add(column_R);
		staticEntities.add(capitol_L_bot);
		staticEntities.add(capitol_R_bot);
		staticEntities.add(capitol_L_top);
		staticEntities.add(capitol_R_top);
		
		
		//Walls
		BasicObject wall_L = new BasicObject(40, 0f, -3, 100, 2.5f, 40, 90f, 0, 0);
		BasicObject wall_R = new BasicObject(40, -10f, 3, 100, 2.5f, 40, -90f, 0, 0);
		
		wall_L.setDiffuse(wall_color);
		wall_R.setDiffuse(wall_color);
		wall_L.setSpecular(zero_array);
		wall_R.setSpecular(zero_array);
		
		TextureParameters wall_tex = new TextureParameters();
		wall_tex.setTextured(true);
		wall_tex.setTextureName("floor");
		wall_tex.setTexGenMode(TextureParameters.texCoordGenMode.PLANE);
		wall_tex.setTilingCoefficients(new float[]{1f, 1f, 4f});
		wall_L.setTexParams(floor_tex);
		wall_R.setTexParams(floor_tex);
		
		wall_L.setObject("plane");
		wall_R.setObject("plane");
		
		
		staticEntities.add(wall_L);
		staticEntities.add(wall_R);
		
		BasicObject wall_back = new BasicObject(0f, -83f, 5f, 50f, 2.5f, 30f, 0f, 0f, 90f);
		BasicObject wall_behind = new BasicObject(-5f, -3f, 5f, 50f, 2.5f, 25f, 0f, 0f, -90f);

		TextureParameters behind_statue = new TextureParameters();
		behind_statue.setTextured(true);
		behind_statue.setTextureName("mosaic");
		//behind_statue.tile_x_plane = false;
		//behind_statue.tile_y_plane = true;
		behind_statue.setTexGenMode(TextureParameters.texCoordGenMode.PLANE);
		behind_statue.setTilingCoefficients(new float[]{8f,1f, 6f});
		
		wall_back.setTexParams(behind_statue);
		
		TextureParameters back_wall = new TextureParameters();
		back_wall.setTextured(true);
		back_wall.setTexGenMode(TextureParameters.texCoordGenMode.PLANE);
		back_wall.setTextureName("floor");
		back_wall.setTilingCoefficients(new float[]{4f, 1f, 1f});
		
		wall_behind.setTexParams(back_wall);
		wall_back.setDiffuse(wall_color);
		wall_behind.setDiffuse(wall_color);
		wall_behind.setSpecular(zero_array);
		wall_back.setSpecular(zero_array);
		
		wall_back.setObject("plane");
		wall_behind.setObject("plane");
		
		staticEntities.add(wall_back);
		staticEntities.add(wall_behind);
		
		
		//Pedestal
		BasicObject pedestal = new BasicObject(80, -1f, 5f, 2, .8f, 2);
		pedestal.setDiffuse(column_color);
		pedestal.setSpecular(zero_array);
		pedestal.setObject("cube");
		pedestal.setTexParams(columns);
		staticEntities.add(pedestal);
		
		//Statue
		statue = new InteractiveObject(80, 1f, 5f, 4f, 4f, 4f, 0f, -90f, 0f, 10);
		statue.setDiffuse(column_color);
		statue.setSpecular(zero_array);
		statue.setObject("statue");
		statue.setFunction(InteractiveObject.Function.LEVEL_EVENT);
		statue.setTexParams(columns);
		statue.radius = 10;
		
		interactiveEntities.add(statue);
		
		//Pond and plants
		BasicObject pond = new BasicObject(80, -.9f, 5f, 6f, 1f, 6f);
		pond.setDiffuse(new float[]{.2f, .5f, .9f, 0f});
		pond.setSpecular(zero_array);
		pond.setObject("plane");
		staticEntities.add(pond);
		
		BasicObject plant_L = new BasicObject(80, -.9f, 3f);
		BasicObject plant_R = new BasicObject(80, -.9f, 7f);
		
		plant_L.setDiffuse(new float[]{.5f, .9f, .2f, 0f});
		plant_R.setDiffuse(new float[]{.5f, .9f, .2f, 0f});
		plant_L.setSpecular(zero_array);
		plant_R.setSpecular(zero_array);
		
		plant_L.setObject("plant");
		plant_R.setObject("plant");
		
		staticEntities.add(plant_L);
		staticEntities.add(plant_R);
		}
	}
	
	public ArrayList<BasicObject> getStaticEntities(){
		return staticEntities;
	}
	
	public void setStaticLights(){
		
	}
	@Override
	public int getStartX(){
		return 1;
	}
	@Override
	public int getStartZ(){
		return 5;
	}
	
	@Override
	public String getLevelMusic(){
		return music;
	}
	
	public void setLevelMusic(String filename){
		music = filename;
	}
	
	public Boolean[][] getCollisionArray(){
		return collisionArray;
	}
	
	public ArrayList<InteractiveObject> getInteractiveEntities(){
		return interactiveEntities;
	}
	
	public ArrayList<CollisionObject> getCollisionEntities(){
		return collisionObjects;
	}
	
	public void eventHappened(){
		
		interactiveEntities.clear();
		Statue statue_coll = new Statue(80f, 1f, 5f, 4f, 4f, 4f, 0f, -90f, 0f);
		statue_coll.setDiffuse(column_color);
		statue_coll.setSpecular(zero_array);
		
		//statue_coll.setAnimation("statue1");
		//statue_coll.setAnimation("statue1");
		statue_coll.setAnimation("statue2");
		statue_coll.setAnimation("statue2");
		statue_coll.setAnimation("statue3");
		statue_coll.setAnimation("statue3");
		statue_coll.setAnimation("statue4");
		statue_coll.setAnimation("statue4");
		statue_coll.setAnimation("statue5");
		statue_coll.setAnimation("statue5");
		statue_coll.setAnimation("statue6");
		statue_coll.setAnimation("statue6");
		statue_coll.setAnimation("statue7");
		statue_coll.setAnimation("statue7");
		collisionObjects.add(statue_coll);
		music = "StatueAttack.wav";
		
	}
}
