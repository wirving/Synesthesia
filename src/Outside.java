import java.util.ArrayList;


public class Outside extends BasicLevel {
	
	private ArrayList<BasicObject> staticEntities;
	private ArrayList<BasicObject> dynamicEntities;
	private ArrayList<InteractiveObject> interactiveEntities;
	private Boolean[][] collisionArray; 
	
	LevelConfig frozen;
	LevelConfig moving;
	
	LevelConfig currentConfig;
	
	public Outside(){
		
		
		buildFrozen();
		clear();
		buildBroken();
		clear();
		setLevelConfig(frozen);
		currentConfig = frozen;
		
		
	}
	
	public void clear(){
		staticEntities.clear();
		//dynamicEntities.clear();
		interactiveEntities.clear();
		//Collision array?
		//Shit, fix this later
		collisionArray = new Boolean[25][25];
	}
	
	public void buildFrozen(){
		
		float[] zero_array = new float[] {0.0f, 0.0f, 0.0f};
		staticEntities = new ArrayList<BasicObject>();
		interactiveEntities = new ArrayList<InteractiveObject>();
		
		BasicObject sky = new BasicObject(0,40,0, 100f, 100f, 100f);
		sky.setObject("sphere");
		sky.setDiffuse(new float[]{1.0f, 1.0f, 1.0f, 0.0f});
		TextureParameters sky_tex = new TextureParameters();
		sky_tex.setTexGenMode(TextureParameters.texCoordGenMode.SPHERE);
		sky_tex.setTextured(true);
		sky_tex.setTextureName("sky");
		sky.setTexParams(sky_tex);
		staticEntities.add(sky);
		
		BasicObject ground = new BasicObject(0,-1,0, 60f, 1f, 60f);
		ground.setDiffuse(new float[]{1f, 1f, 1f});
		ground.setSpecular(zero_array);
		TextureParameters grass_tex = new TextureParameters();
		grass_tex.setTextured(true);
		grass_tex.setTexGenMode(TextureParameters.texCoordGenMode.PLANE);
		grass_tex.setTilingCoefficients(new float[]{25f, 1f, 25f});
		grass_tex.setTextureName("grass");
		ground.setTexParams(grass_tex);
		staticEntities.add(ground);
		
		//Some trees!
		BasicObject tree = new BasicObject(5, 1, 5, 2f, 2f, 2f);
		BasicObject tree2 = new BasicObject(8, 0.5f, 4, 1.5f, 1.5f, 1.5f);
		BasicObject tree3 = new BasicObject(4, 1, 2, 2f, 2f, 2f);
		
		BasicObject pond = new BasicObject(2, -0.5f, 2, 5f, 1f, 5f);
		
		BasicObject rock = new BasicObject(10, -0.75f, 10, 1f, 1f, 1f);
		BasicObject rock2 = new BasicObject(0, -0.75f, 0, 1f, 1f, 1f);
		
		BasicObject plant = new BasicObject(3, -0.5f, 4);
		
		BasicObject deer = new BasicObject(5, 0.5f, -1);
		
		tree.setDiffuse(new float[]{.4f, .5f, .3f});
		tree2.setDiffuse(new float[]{.7f, .7f, .5f});
		tree3.setDiffuse(new float[]{.4f, .4f, .3f});
		rock.setDiffuse(new float[]{.9f, .9f, .9f});
		plant.setDiffuse(new float[]{.5f, .9f, .2f, 0f});
		rock2.setDiffuse(new float[]{.9f, .9f, .9f});
		deer.setDiffuse(new float[]{.5f, .4f, .2f});
		//pond.setDiffuse(new float[]{.2f, .5f, .9f, 0f});
		pond.setDiffuse(new float[]{1f, 1f, 1f});
		
		//Pond reflects sky
		pond.setTexParams(sky_tex);
		
		
		tree.setSpecular(zero_array);
		tree2.setSpecular(zero_array);
		tree3.setSpecular(zero_array);
		rock.setSpecular(zero_array);
		plant.setSpecular(zero_array);
		rock2.setSpecular(zero_array);
		pond.setSpecular(zero_array);
		deer.setSpecular(zero_array);
		
		tree.setObject("tree_aspen");
		tree2.setObject("tree_aspen");
		tree3.setObject("tree_aspen");
		plant.setObject("plant");
		rock.setObject("rock");
		rock2.setObject("rock");
		pond.setObject("pond");
		deer.setObject("deer");
		
		staticEntities.add(tree);
		staticEntities.add(tree2);
		staticEntities.add(tree3);
		staticEntities.add(rock);
		staticEntities.add(plant);
		staticEntities.add(rock2);
		staticEntities.add(pond);
		staticEntities.add(deer);
		
		InteractiveObject magicSphere = new InteractiveObject(-3,-.25f,10,.5f,.5f,.5f);
		magicSphere.setDiffuse(new float[]{1f, 1f, 1f});
		magicSphere.setSpecular(new float[]{1f, 1f, 1f});
		magicSphere.setFunction(InteractiveObject.Function.LEVEL_EVENT);
		magicSphere.setObject("sphere");
		//sky_tex.setTexGenMode(TextureParameters.texCoordGenMode.SPHERE_MAP);
		//magicSphere.setTexParams(sky_tex);
		
		TextureParameters random_tex = new TextureParameters();
		random_tex.setTextured(true);
		random_tex.newRandomTexture();
		magicSphere.setTexParams(random_tex);
		interactiveEntities.add(magicSphere);
		
		frozen = new LevelConfig((ArrayList<BasicObject>) this.getStaticEntities().clone(), (ArrayList<InteractiveObject>)this.getInteractiveEntities().clone());
		//currentConfig = frozen;
		
	}
	
	public void buildBroken(){
		
		float[] zero_array = new float[] {0.0f, 0.0f, 0.0f};
		staticEntities = new ArrayList<BasicObject>();
		interactiveEntities = new ArrayList<InteractiveObject>();
		
		BasicObject sky = new BasicObject(0,40,0, 100f, 100f, 100f);
		sky.setObject("sphere");
		sky.setDiffuse(new float[]{1.0f, 1.0f, 1.0f, 0.0f});
		TextureParameters sky_tex = new TextureParameters();
		sky_tex.setTexGenMode(TextureParameters.texCoordGenMode.SPHERE_MAP);
		sky_tex.setTextured(true);
		sky_tex.setTextureName("sky");
		sky.setTexParams(sky_tex);
		staticEntities.add(sky);
		
		BasicObject ground = new BasicObject(0,-1,0, 60f, 1f, 60f);
		ground.setDiffuse(new float[]{1f, 1f, 1f});
		ground.setSpecular(zero_array);
		TextureParameters grass_tex = new TextureParameters();
		grass_tex.setTextured(true);
		grass_tex.setTexGenMode(TextureParameters.texCoordGenMode.PLANE);
		grass_tex.setTilingCoefficients(new float[]{25f, 1f, 25f});
		grass_tex.setTextureName("grass");
		ground.setTexParams(grass_tex);
		staticEntities.add(ground);
		
		JitteringObject tree = new JitteringObject(5f, 1f, 5f, 2f, 2f, 2f, 1f, 50f);
		JitteringObject tree2 = new JitteringObject(8, 0.5f, 4, 1.5f, 1.5f, 1.5f, 0.5f, 50f);
		JitteringObject tree3 = new JitteringObject(4, 1, 2, 2f, 2f, 2f, 1f, 50f);
		
		BasicObject pond = new BasicObject(2, -0.5f, 2, 5f, 1f, 5f);
		
		JitteringObject rock = new JitteringObject(10, -0.75f, 10, 1f, 1f, 1f, -0.75f, -0.75f);
		JitteringObject rock2 = new JitteringObject(0, -0.75f, 0, 1f, 1f, 1f, -0.75f, -0.75f);
		
		JitteringObject plant = new JitteringObject(3, -0.5f, 4, -0.5f, -0.5f);
		
		RotatingObject deer = new RotatingObject(5, 0.5f, -1);
		TextureParameters random = new TextureParameters();
		random.setTextured(true);
		random.newRandomTexture();
		deer.setTexParams(random);
		
		tree.setDiffuse(tree.getDiffuseFlicker());
		tree2.setDiffuse(new float[]{.7f, .7f, .5f});
		tree3.setDiffuse(new float[]{.4f, .4f, .3f});
		rock.setDiffuse(new float[]{.9f, .9f, .9f});
		plant.setDiffuse(new float[]{.5f, .9f, .2f, 0f});
		rock2.setDiffuse(new float[]{.9f, .9f, .9f});
		deer.setDiffuse(new float[]{.5f, .4f, .2f});
		//pond.setDiffuse(new float[]{.2f, .5f, .9f, 0f});
		pond.setDiffuse(new float[]{1f, 1f, 1f});
		
		//Pond reflects sky
		pond.setTexParams(sky_tex);
		
		tree.setSpecular(zero_array);
		tree2.setSpecular(zero_array);
		tree3.setSpecular(zero_array);
		rock.setSpecular(zero_array);
		plant.setSpecular(zero_array);
		rock2.setSpecular(zero_array);
		pond.setSpecular(zero_array);
		deer.setSpecular(zero_array);
		
		tree.setObject("tree_aspen");
		tree2.setObject("tree_aspen");
		tree3.setObject("tree_aspen");
		plant.setObject("plant");
		rock.setObject("rock");
		rock2.setObject("rock");
		pond.setObject("pond");
		deer.setObject("deer");
		
		staticEntities.add(tree);
		staticEntities.add(tree2);
		staticEntities.add(tree3);
		staticEntities.add(rock);
		staticEntities.add(plant);
		staticEntities.add(rock2);
		staticEntities.add(pond);
		staticEntities.add(deer);
		
		InteractiveObject magicSphere = new InteractiveObject(-3,-.25f,10,.5f,.5f,.5f);
		magicSphere.setDiffuse(new float[]{1f, 1f, 1f});
		magicSphere.setSpecular(new float[]{1f, 1f, 1f});
		magicSphere.setFunction(InteractiveObject.Function.LEVEL_EVENT);
		magicSphere.setObject("sphere");
		//sky_tex.setTexGenMode(TextureParameters.texCoordGenMode.SPHERE_MAP);
		magicSphere.setTexParams(sky_tex);
		interactiveEntities.add(magicSphere);
		
		
		//Fuck off, Java
		ArrayList<BasicObject> static_copy = (ArrayList<BasicObject>) this.getStaticEntities().clone();
		ArrayList<InteractiveObject> inter_copy = (ArrayList<InteractiveObject>) this.getInteractiveEntities().clone();

		moving = new LevelConfig(static_copy, inter_copy);
	}
	
	public ArrayList<BasicObject> getStaticEntities(){
		return staticEntities;
	}
	
	public ArrayList<InteractiveObject> getInteractiveEntities(){
		return interactiveEntities;
	}
	
	
	public int getStartX(){
		return 0;
	}

	public int getStartZ(){
		return -5;
		
	}
	
	public void eventHappened(){
		
		invertLevelConfig();
	}
	
	public void invertLevelConfig(){
		
		if (currentConfig.equals(frozen)){
			setLevelConfig(moving);
		}
		
		else{
			setLevelConfig(frozen);
		}
	}
	
	public void setLevelConfig(LevelConfig l){
		
		this.staticEntities = 	l.getStaticEntities();
		this.dynamicEntities = l.getdynamicEntities();
		this.interactiveEntities = l.getInteractiveEntities();
		this.collisionArray = l.getCollisionArray();
		currentConfig = l;
	}
}
