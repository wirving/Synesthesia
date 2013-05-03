import java.util.ArrayList;


public class Temple extends BasicLevel{
	
	private ArrayList<BasicObject> staticEntities;
	private ArrayList<BasicObject> dynamicEntities;
	private Boolean[][] collisionArray; 
	private ArrayList<Light> lights;
	
	public Temple(){
		
		setStaticLights();
		staticEntities = new ArrayList<BasicObject>();
		//collisionArray = new Boolean[25][25];
		
		float[] column_color = new float[]{.8f, .6f, .5f, 1f};
		float[] wall_color = new float[]{.6f, .4f, .3f, 1f};
		
		//Floor & ceiling
		BasicObject floor = new BasicObject(40, -1f, 5, 100, 2, 10);
		BasicObject ceiling = new BasicObject(-40, -8f, 5, 100, 2, 10, 0f, 0f, 180f);
		
		floor.setDiffuse(wall_color);
		ceiling.setDiffuse(wall_color);
		
		floor.setObject("plane");
		ceiling.setObject("plane");
		
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

		
		column_L.setDiffuse(column_color);
		column_R.setDiffuse(column_color);
		capitol_L_bot.setDiffuse(column_color);
		capitol_R_bot.setDiffuse(column_color);
		capitol_L_top.setDiffuse(column_color);
		capitol_R_top.setDiffuse(column_color);


		column_L.setSpecular(new float[]{.2f, .2f, .2f, 1});
		column_R.setSpecular(new float[]{.2f, .2f, .2f, 1});
		capitol_L_bot.setSpecular(new float[]{.2f, .2f, .2f, 1});
		capitol_R_bot.setSpecular(new float[]{.2f, .2f, .2f, 1});
		capitol_L_top.setSpecular(new float[]{.2f, .2f, .2f, 1});
		capitol_R_top.setSpecular(new float[]{.2f, .2f, .2f, 1});


		column_L.setObject("cylinder");
		column_R.setObject("cylinder");
		capitol_L_bot.setObject("cube");
		capitol_R_bot.setObject("cube");
		capitol_R_top.setObject("cube");
		capitol_L_top.setObject("cube");

		
		staticEntities.add(column_L);
		staticEntities.add(column_R);
		staticEntities.add(capitol_L_bot);
		staticEntities.add(capitol_R_bot);
		staticEntities.add(capitol_L_top);
		staticEntities.add(capitol_R_top);
		}
		
		//Walls
		BasicObject wall_L = new BasicObject(40, 0f, -3, 100, 2.5f, 10, 90f, 0, 0);
		BasicObject wall_R = new BasicObject(40, -10f, 3, 100, 2.5f, 10, -90f, 0, 0);
		
		wall_L.setDiffuse(wall_color);
		wall_R.setDiffuse(wall_color);
		
		wall_L.setObject("plane");
		wall_R.setObject("plane");
		
		staticEntities.add(wall_L);
		staticEntities.add(wall_R);
		
		BasicObject wall_back = new BasicObject(5f, -90f, 5f, 25f, 2.5f, 25f, 0f, 0f, 90f);
		BasicObject wall_behind = new BasicObject(-5f, -10f, 5f, 25f, 2.5f, 25f, 0f, 0f, -90f);

		wall_back.setDiffuse(wall_color);
		wall_behind.setDiffuse(wall_color);
		
		wall_back.setObject("plane");
		wall_behind.setDiffuse(wall_color);
		
		staticEntities.add(wall_back);
		staticEntities.add(wall_behind);
		
		
		//Pedestal
		BasicObject pedestal = new BasicObject(80, -1f, 5f, 2, .8f, 2);
		pedestal.setDiffuse(column_color);
		pedestal.setObject("cube");
		staticEntities.add(pedestal);
		
		//Statue placeholder
		BasicObject statue = new BasicObject(80, 0f, 5f);
		statue.setDiffuse(column_color);
		statue.setObject("statue");
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
		return "temptemple.wav";
	}
}
