import java.util.ArrayList;
<<<<<<< HEAD
=======
import java.util.Arrays;
>>>>>>> origin/Working


public class Outside extends BasicLevel {
	
	private ArrayList<BasicObject> staticEntities;
	private ArrayList<BasicObject> dynamicEntities;
	private ArrayList<InteractiveObject> interactiveEntities;
<<<<<<< HEAD
	private Boolean[][] collisionArray; 
	
	public Outside(){
		
		staticEntities = new ArrayList<BasicObject>();
		
		BasicObject sky = new BasicObject(0,10,0, 50f, 50f, 50f);
		sky.setObject("sphere");
		sky.setDiffuse(new float[]{1.0f, 1.0f, 1.0f, 0.0f});
		TextureParameters texParams = new TextureParameters();
		texParams.setTexGenMode(TextureParameters.texCoordGenMode.SPHERE_MAP);
		texParams.setTextured(true);
		texParams.setTextureName("sky");
		sky.setTexParams(texParams);
		staticEntities.add(sky);
		
		
	}
	
=======
	private ArrayList<CollisionObject> collisionObjects;
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
		collisionObjects.clear();
		collisionArray = new Boolean[31][31];
		for (Boolean[] row : collisionArray){
		Arrays.fill(row, false);
		}
	}
	
	public void buildFrozen(){
		
		float[] zero_array = new float[] {0.0f, 0.0f, 0.0f};
		staticEntities = new ArrayList<BasicObject>();
		interactiveEntities = new ArrayList<InteractiveObject>();
		collisionObjects = new ArrayList<CollisionObject>();
		
		collisionArray = new Boolean[31][31];
		for (Boolean[] row : collisionArray){
		Arrays.fill(row, false);
		}
		
		for (int z = 0; z < 30; z++){
			collisionArray[0][z] = true;
			collisionArray[z][0] = true;
			collisionArray[30][z] = true;
			collisionArray[z][30] = true;
		}
		
		
		
		BasicObject sky = new BasicObject(10,40,10, 100f, 100f, 100f);
		sky.setObject("sphere");
		sky.setDiffuse(new float[]{1.0f, 1.0f, 1.0f, 0.0f});
		TextureParameters sky_tex = new TextureParameters();
		sky_tex.setTexGenMode(TextureParameters.texCoordGenMode.SPHERE);
		sky_tex.setTextured(true);
		sky_tex.setTextureName("sky");
		sky.setTexParams(sky_tex);
		staticEntities.add(sky);
		
		BasicObject ground = new BasicObject(10,-1,10, 60f, 1f, 60f);
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
		BasicObject tree = new BasicObject(15, 1, 18, 3f, 3f, 3f);
		collisionArray[(int)tree.getXPos()][(int)tree.getZPos()] = true;
		
		BasicObject tree2 = new BasicObject(18, 0.5f, 17, 1.5f, 1.5f, 1.5f);
		collisionArray[(int)tree2.getXPos()][(int)tree2.getZPos()] = true;
		
		BasicObject tree3 = new BasicObject(6, 2, 13, 4f, 4f, 4f);
		collisionArray[(int)tree3.getXPos()][(int)tree3.getZPos()] = true;
		
		BasicObject tree4 = new BasicObject(2, 1, 20, 3f, 3f, 3f, 0f, 45f, 0f);
		collisionArray[(int)tree4.getXPos()][(int)tree4.getZPos()] = true;
		
		BasicObject pond = new BasicObject(12, -0.5f, 15, 8f, 1f, 8f);
		
		BasicObject rock = new BasicObject(15, -0.75f, 15, 1f, 1f, 1f);
		collisionArray[(int)rock.getXPos()][(int)rock.getZPos()] = true;
		
		BasicObject rock2 = new BasicObject(10, -0.75f, 13, 1f, 1f, 1f);
		collisionArray[(int)rock.getXPos()][(int)rock.getZPos()] = true;
		
		BasicObject plant = new BasicObject(13, -0.5f, 18);
		collisionArray[(int)plant.getXPos()][(int)plant.getZPos()] = true;
		
		BasicObject plant2 = new BasicObject(14f, -0.5f, 18, .85f, .85f, .85f, 0f, 20f, 0f);
		collisionArray[(int)plant2.getXPos()][(int)plant2.getZPos()] = true;
		
		BasicObject plant3 = new BasicObject(10f, -0.5f, 11f, .5f, .5f, .5f, 0f, 0f, 0f);
		collisionArray[(int)plant3.getXPos()][(int)plant3.getZPos()] = true;
	
		
		BasicObject deer = new BasicObject(17, 0.5f, 13);
		collisionArray[(int)deer.getXPos()][(int)deer.getZPos()] = true;
		
		BasicObject deer_fem = new BasicObject(18, -3.75f, 11f, .85f, .85f, .85f, -20f, 0f, 0f);
		collisionArray[(int)deer_fem.getXPos()][(int)deer_fem.getZPos()] = true;
		
		BasicObject deer_small = new BasicObject(17.5f, 1f, 14f, .5f, .5f, .5f, 5f, 0f, 0f);
		collisionArray[(int)deer_small.getXPos()][(int)deer_small.getZPos()] = true;
		
		BasicObject bird = new BasicObject(10, 5f, 13);
		collisionArray[(int)bird.getXPos()][(int)bird.getZPos()] = true;
		
		BasicObject bird2 = new BasicObject(13, 8f, 11f, 1f, 1f, 1f, 0f, 0f, 20f);
		collisionArray[(int)bird2.getXPos()][(int)bird2.getZPos()] = true;
		
		BasicObject bird3 = new BasicObject(0, 6f, 16f, 1f, 1f, 1f, 0f, 0f, -20f);
		collisionArray[(int)bird3.getXPos()][(int)bird3.getZPos()] = true;
		
		BasicObject bird4 = new BasicObject(8, 3f, 15);
		collisionArray[(int)bird4.getXPos()][(int)bird4.getZPos()] = true;
		
		BasicObject bunny = new BasicObject(10f, -.35f, 10f, .15f, .15f, .15f);
		collisionArray[(int)bunny.getXPos()][(int)bunny.getZPos()] = true;
		
		tree.setDiffuse(new float[]{.4f, .5f, .3f});
		bird.setDiffuse(new float[]{.3f, .3f, .3f});
		bird2.setDiffuse(new float[]{.3f, .3f, .3f});
		bird3.setDiffuse(new float[]{.3f, .3f, .3f});
		bird4.setDiffuse(new float[]{.3f, .3f, .3f});
		tree2.setDiffuse(new float[]{.7f, .7f, .5f});
		tree3.setDiffuse(new float[]{.4f, .4f, .3f});
		tree4.setDiffuse(new float[]{.3f, .4f, .2f});
		rock.setDiffuse(new float[]{.9f, .9f, .9f});
		plant.setDiffuse(new float[]{.5f, .9f, .2f, 0f});
		plant2.setDiffuse(new float[]{.7f, .9f, .2f, 0f});
		plant3.setDiffuse(new float[]{.7f, .9f, .2f, 0f});
		rock2.setDiffuse(new float[]{.9f, .9f, .9f});
		deer.setDiffuse(new float[]{.5f, .4f, .2f});
		deer_fem.setDiffuse(new float[]{.6f, .4f, .2f});
		deer_small.setDiffuse(new float[]{.7f, .5f, .3f});
		bunny.setDiffuse(new float[]{.8f, .8f, .7f});

		//pond.setDiffuse(new float[]{.2f, .5f, .9f, 0f});
		pond.setDiffuse(new float[]{1f, 1f, 1f});
		
		//Pond reflects sky
		TextureParameters pond_tex = new TextureParameters();
		pond_tex.setTexGenMode(TextureParameters.texCoordGenMode.SPHERE_MAP);
		pond_tex.setTextured(true);
		pond_tex.setTextureName("sky");
		pond.setTexParams(pond_tex);
		//pond.setTexParams(sky_tex);
		
		
		tree.setSpecular(zero_array);
		tree2.setSpecular(zero_array);
		tree3.setSpecular(zero_array);
		tree4.setSpecular(zero_array);
		rock.setSpecular(zero_array);
		plant.setSpecular(zero_array);
		plant2.setSpecular(zero_array);
		plant3.setSpecular(zero_array);
		rock2.setSpecular(zero_array);
		pond.setSpecular(zero_array);
		deer.setSpecular(zero_array);
		deer_fem.setSpecular(zero_array);
		deer_small.setSpecular(zero_array);
		bird.setSpecular(zero_array);
		bird2.setSpecular(zero_array);
		bird3.setSpecular(zero_array);
		bird4.setSpecular(zero_array);
		bunny.setSpecular(zero_array);

		
		tree.setObject("tree_aspen");
		tree2.setObject("tree_aspen");
		tree3.setObject("tree_aspen");
		tree4.setObject("tree_aspen");
		plant.setObject("plant");
		plant2.setObject("plant");
		plant3.setObject("plant");
		rock.setObject("rock");
		rock2.setObject("rock");
		pond.setObject("pond");
		deer.setObject("deer");
		deer_fem.setObject("doe");
		deer_small.setObject("doe");
		bird.setObject("bird");
		bird2.setObject("bird");
		bird3.setObject("bird");
		bird4.setObject("bird");
		bunny.setObject("bunny");
		
		staticEntities.add(tree);
		staticEntities.add(tree2);
		staticEntities.add(tree3);	
		staticEntities.add(tree4);
		staticEntities.add(rock);
		staticEntities.add(plant);
		staticEntities.add(plant2);
		staticEntities.add(plant3);
		staticEntities.add(rock2);
		staticEntities.add(pond);
		staticEntities.add(deer);
		staticEntities.add(bird);
		staticEntities.add(bird2);
		staticEntities.add(bird3);
		staticEntities.add(bird4);
		staticEntities.add(bunny);
		staticEntities.add(deer_fem);
		staticEntities.add(deer_small);
		
		
		InteractiveObject magicSphere = new InteractiveObject(7,-.25f,20,.5f,.5f,.5f);
		magicSphere.setDiffuse(new float[]{1f, 1f, 1f});
		magicSphere.setSpecular(new float[]{1f, 1f, 1f});
		magicSphere.setFunction(InteractiveObject.Function.LEVEL_EVENT);
		magicSphere.setObject("sphere");
		magicSphere.radius = 2;
		
		collisionArray[7][20] = true;
		//sky_tex.setTexGenMode(TextureParameters.texCoordGenMode.SPHERE_MAP);
		//magicSphere.setTexParams(sky_tex);
		
		TextureParameters random_tex = new TextureParameters();
		random_tex.setTextured(true);
		random_tex.newRandomTexture();
		magicSphere.setTexParams(random_tex);
		interactiveEntities.add(magicSphere);
		
		frozen = new LevelConfig((ArrayList<BasicObject>) this.getStaticEntities().clone(), (ArrayList<InteractiveObject>)this.getInteractiveEntities().clone(), (ArrayList<CollisionObject>)this.getCollisionEntities().clone(), collisionArray.clone());
		//currentConfig = frozen;
		
	}
	
	public void buildBroken(){
		
		float[] zero_array = new float[] {0.0f, 0.0f, 0.0f};
		staticEntities = new ArrayList<BasicObject>();
		interactiveEntities = new ArrayList<InteractiveObject>();
		collisionObjects = new ArrayList<CollisionObject>();
		
		collisionArray = new Boolean[31][31];
		for (Boolean[] row : collisionArray){
		Arrays.fill(row, false);
		}
		
		for (int z = 0; z < 30; z++){
			collisionArray[0][z] = true;
			collisionArray[z][0] = true;
			collisionArray[30][z] = true;
			collisionArray[z][30] = true;
		}
		
		
		BasicObject sky = new BasicObject(10,40,10, 100f, 100f, 100f);
		sky.setObject("sphere");
		sky.setDiffuse(new float[]{1.0f, 1.0f, 1.0f, 0.0f});
		TextureParameters sky_tex = new TextureParameters();
		sky_tex.setTexGenMode(TextureParameters.texCoordGenMode.SPHERE_MAP);
		sky_tex.setTextured(true);
		sky_tex.setTextureName("sky");
		//sky.setTexParams(sky_tex);
		staticEntities.add(sky);
		
		BasicObject ground = new BasicObject(10,-1,10, 60f, 1f, 60f);
		ground.setDiffuse(new float[]{1f, 1f, 1f});
		ground.setSpecular(zero_array);
		TextureParameters grass_tex = new TextureParameters();
		grass_tex.setTextured(true);
		grass_tex.setTexGenMode(TextureParameters.texCoordGenMode.PLANE);
		grass_tex.setTilingCoefficients(new float[]{25f, 1f, 25f});
		grass_tex.setTextureName("static");
		ground.setTexParams(grass_tex);
		staticEntities.add(ground);
		
		JitteringObject tree = new JitteringObject(15f, 1f, 18f, 2f, 2f, 2f, 1f, 50f);
		JitteringObject tree2 = new JitteringObject(18, 0.5f, 17, 1.5f, 1.5f, 1.5f, 0.5f, 50f);
		JitteringObject tree3 = new JitteringObject(6, 2, 13, 2f, 2f, 2f, 1f, 50f);
		JitteringObject tree4 = new JitteringObject(2f, 1f, 20f, 3f, 3f, 3f, 0f, 45f, 0f, 1f, 50f);
		
		CollisionObject pond = new CollisionObject(12f, -0.5f, 15f, 5f, 1f, 5f);
		
		JitteringObject rock = new JitteringObject(15, -0.75f, 15, 1f, 1f, 1f, -0.75f, -.5f);
		JitteringObject rock2 = new JitteringObject(10, -0.75f, 13, 1f, 1f, 1f, -0.75f, -.5f);
		
		JitteringObject plant = new JitteringObject(13, -0.5f, 18, -0.5f, -0.5f);
		JitteringObject plant2 = new JitteringObject(14f, -0.5f, 18, .85f, .85f, .85f, 0f, 20f, 0f, -0.5f, -0.5f);
		JitteringObject plant3 = new JitteringObject(10f, -0.5f, 11f, .5f, .5f, .5f, 0f, 45f, 0f, -0.5f, -0.5f);
		
		RotatingObject deer = new RotatingObject(17, 0.5f, 13);
		RotatingObject deer_fem = new RotatingObject(18, -3.75f, 11f, .85f, .85f, .85f, -20f, 0f, 0f);
		RotatingObject deer_small = new RotatingObject(17.5f, 1f, 14f, .5f, .5f, .5f, 5f, 0f, 0f);
		
		
		RotatingObject bird = new RotatingObject(10, 5f, 13);
		RotatingObject bird2 = new RotatingObject(13f, 8f, 11f, 1f, 1f, 1f, 0f, 0f, 20f);
		RotatingObject bird3 = new RotatingObject(0f, 6f, 16f, 1f, 1f, 1f, 0f, 0f, -20f);
		RotatingObject bird4 = new RotatingObject(8, 3f, 15);
		
		RotatingObject bunny = new RotatingObject(10f, -.35f, 10f, .15f, .15f, .15f, 0f, 0f, 0f);

		
		TextureParameters random = new TextureParameters();
		random.setTextured(true);
		random.newRandomTexture();
		deer.setTexParams(random);
		
		TextureParameters random2 = new TextureParameters();
		random2.setTextured(true);
		random2.newRandomTexture();
		tree.setTexParams(random2);
		
		TextureParameters random3 = new TextureParameters();
		random3.setTextured(true);
		random3.newRandomTexture();
		tree2.setTexParams(random3);
		
		TextureParameters random4 = new TextureParameters();
		random4.setTextured(true);
		random4.newRandomTexture();
		tree3.setTexParams(random4);
		
		bird.setTexParams(random2);
		bird2.setTexParams(random3);
		bird3.setTexParams(random4);
		bird4.setTexParams(random);
		rock.setTexParams(random);
		rock2.setTexParams(random4);
		tree4.setTexParams(random);
		bunny.setTexParams(random);
		deer_fem.setTexParams(random3);
		deer_small.setTexParams(random2);
		plant.setTexParams(random);
		plant2.setTexParams(random3);
		plant3.setTexParams(random2);
		
		
		tree.setDiffuse(tree.getDiffuseFlicker());
		tree2.setDiffuse(new float[]{1f, 1f, 1f});
		tree3.setDiffuse(new float[]{1f, 1f, 1f});
		bird2.setDiffuse(new float[]{1f, 1f, 1f});
		bird3.setDiffuse(new float[]{1f, 1f, 1f});
		bird4.setDiffuse(new float[]{1f, 1f, 1f});
		rock.setDiffuse(new float[]{1f, 1f, 1f});
		plant.setDiffuse(new float[]{1f, 1f, 1f, 1f});
		rock2.setDiffuse(new float[]{1f, 1f, 1f});
		deer.setDiffuse(new float[]{1f, 1f, 1f});
		deer_fem.setDiffuse(new float[]{1f, 1f, 1f});
		deer_small.setDiffuse(new float[]{1f, 1f, 1f});
		bunny.setDiffuse(new float[]{1f, 1f, 1f});
		//pond.setDiffuse(new float[]{.2f, .5f, .9f, 0f});
		pond.setDiffuse(new float[]{0f, 0f, 0f});
		
		//Pond reflects sky
		//pond.setTexParams(sky_tex);
		
		tree.setSpecular(zero_array);
		tree2.setSpecular(zero_array);
		tree3.setSpecular(zero_array);
		tree4.setSpecular(zero_array);
		rock.setSpecular(zero_array);
		plant.setSpecular(zero_array);
		plant2.setSpecular(zero_array);
		plant3.setSpecular(zero_array);
		rock2.setSpecular(zero_array);
		pond.setSpecular(zero_array);
		deer.setSpecular(zero_array);
		deer_fem.setSpecular(zero_array);
		deer_small.setSpecular(zero_array);
		bird.setSpecular(zero_array);
		bird2.setSpecular(zero_array);
		bird3.setSpecular(zero_array);
		bird4.setSpecular(zero_array);
		bunny.setSpecular(zero_array);

		
		tree.setObject("tree_aspen");
		tree2.setObject("tree_aspen");
		tree3.setObject("tree_aspen");
		tree4.setObject("tree_aspen");
		plant.setObject("plant");
		plant2.setObject("plant");
		plant3.setObject("plant");
		rock.setObject("rock");
		rock2.setObject("rock");
		pond.setObject("pond");
		deer.setObject("deer");
		deer_fem.setObject("doe");
		deer_small.setObject("doe");
		bird.setObject("bird");
		bird2.setObject("bird");
		bird3.setObject("bird");
		bird4.setObject("bird");
		bunny.setObject("bunny");
		
		staticEntities.add(tree);
		staticEntities.add(tree2);
		staticEntities.add(tree3);	
		staticEntities.add(tree4);
		staticEntities.add(rock);
		staticEntities.add(plant);
		staticEntities.add(plant2);
		staticEntities.add(plant3);
		staticEntities.add(rock2);
		staticEntities.add(pond);
		staticEntities.add(deer);
		staticEntities.add(bird);
		staticEntities.add(bird2);
		staticEntities.add(bird3);
		staticEntities.add(bird4);
		staticEntities.add(bunny);
		staticEntities.add(deer_fem);
		staticEntities.add(deer_small);
		
		collisionObjects.add(pond);
		
		InteractiveObject magicSphere = new InteractiveObject(7,-.25f,20,.5f,.5f,.5f);
		collisionArray[7][20] = true;
		
		magicSphere.setDiffuse(new float[]{1f, 1f, 1f});
		magicSphere.setSpecular(new float[]{1f, 1f, 1f});
		magicSphere.setFunction(InteractiveObject.Function.LEVEL_EVENT);
		magicSphere.setObject("sphere");
		magicSphere.radius = 2;
		//sky_tex.setTexGenMode(TextureParameters.texCoordGenMode.SPHERE_MAP);
		magicSphere.setTexParams(sky_tex);
		interactiveEntities.add(magicSphere);
		
		
		//Fuck off, Java
		ArrayList<BasicObject> static_copy = (ArrayList<BasicObject>) this.getStaticEntities().clone();
		ArrayList<InteractiveObject> inter_copy = (ArrayList<InteractiveObject>) this.getInteractiveEntities().clone();
		ArrayList<CollisionObject> coll_copy = (ArrayList<CollisionObject>)this.getCollisionEntities().clone();
		
		moving = new LevelConfig(static_copy, inter_copy, coll_copy, collisionArray.clone());
	}
	
	public Boolean[][] getCollisionArray(){
		return collisionArray;
	}
	
>>>>>>> origin/Working
	public ArrayList<BasicObject> getStaticEntities(){
		return staticEntities;
	}
	
<<<<<<< HEAD

=======
	public ArrayList<InteractiveObject> getInteractiveEntities(){
		return interactiveEntities;
	}
	

	public ArrayList<CollisionObject> getCollisionEntities(){
		return this.collisionObjects;
	}
	
	public int getStartX(){
		return 3;
	}

	public int getStartZ(){
		return 3;
		
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
		this.collisionObjects = l.getCollisionEntities();
		this.collisionArray = l.getCollisionArray();
		currentConfig = l;
	}
>>>>>>> origin/Working
}
