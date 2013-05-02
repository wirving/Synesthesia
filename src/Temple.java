import java.util.ArrayList;


public class Temple {
	
	private ArrayList<BasicObject> staticEntities;
	private ArrayList<BasicObject> dynamicEntities;
	private Boolean[][] collisionArray; 
	private ArrayList<Light> lights;
	
	public Temple(){
		
		setStaticLights();
		staticEntities = new ArrayList<BasicObject>();
		collisionArray = new Boolean[25][25];
		
		float[] column_color = new float[]{.95f, .9f, .9f, 1f};
		float[] wall_color = new float[]{.65f, .6f, .6f, 1f}; //.6 .4 .3
		float[] zero_array = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
		
		//Floor & ceiling
		BasicObject floor = new BasicObject(40, -1f, 5, 100, 2, 10);
		BasicObject ceiling = new BasicObject(-40, -8f, 5, 100, 2, 10, 0f, 0f, 180f);
		
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
		
		staticEntities.add(floor);
		staticEntities.add(ceiling);

		//Columns & capitols
		for (int x = 0; x < 9; x++){
		BasicObject column_L = new BasicObject(10 * x, 3f, .5f, 1, 10, 1);
		BasicObject column_R = new BasicObject(10 * x, 3f, 9.5f, 1, 10, 1);
		
		BasicObject capitol_L_bot = new BasicObject(10 * x, -1f, 0f, 1.2f, .8f, 2f);
		BasicObject capitol_R_bot = new BasicObject(10 * x, -1f, 10f, 1.2f, .8f, 2f);
		BasicObject capitol_R_top = new BasicObject(10 * x, 8f, 10f, 1.2f, .8f, 2f);
		BasicObject capitol_L_top = new BasicObject(10 * x, 8f, 0f, 1.2f, .8f, 2f);

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
		BasicObject wall_L = new BasicObject(40, 0f, -3, 100, 2.5f, 10, 90f, 0, 0);
		BasicObject wall_R = new BasicObject(40, -10f, 3, 100, 2.5f, 10, -90f, 0, 0);
		
		wall_L.setDiffuse(wall_color);
		wall_R.setDiffuse(wall_color);
		wall_L.setSpecular(zero_array);
		wall_R.setSpecular(zero_array);
		
		wall_L.setObject("plane");
		wall_R.setObject("plane");
		
		staticEntities.add(wall_L);
		staticEntities.add(wall_R);
		
		BasicObject wall_back = new BasicObject(0f, -83f, 5f, 30f, 2.5f, 30f, 0f, 0f, 90f);
		BasicObject wall_behind = new BasicObject(-5f, -3f, 5f, 25f, 2.5f, 25f, 0f, 0f, -90f);

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
		staticEntities.add(pedestal);
		
		//Statue
		BasicObject statue = new BasicObject(80, 1f, 5f, 4f, 4f, 4f, 0f, 0f, 0f);
		statue.setDiffuse(column_color);
		statue.setSpecular(zero_array);
		statue.setObject("statue");
		//statue.setTexParams(columns);
		staticEntities.add(statue);
		
		//Pond and plants
		BasicObject pond = new BasicObject(80, -.9f, 5f, 6f, 1f, 6f);
		pond.setDiffuse(new float[]{.2f, .5f, .9f, 0f});
		pond.setObject("plane");
		staticEntities.add(pond);
		
		BasicObject plant_L = new BasicObject(80, -.9f, 3f);
		BasicObject plant_R = new BasicObject(80, -.9f, 7f);
		
		plant_L.setDiffuse(new float[]{.5f, .9f, .2f, 0f});
		plant_R.setDiffuse(new float[]{.5f, .9f, .2f, 0f});
		
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

}
