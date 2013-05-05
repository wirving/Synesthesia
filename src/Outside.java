import java.util.ArrayList;


public class Outside extends BasicLevel {
	
	private ArrayList<BasicObject> staticEntities;
	private ArrayList<BasicObject> dynamicEntities;
	private ArrayList<InteractiveObject> interactiveEntities;
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
	
	public ArrayList<BasicObject> getStaticEntities(){
		return staticEntities;
	}
	

}
