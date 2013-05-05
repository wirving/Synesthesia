import java.util.ArrayList;


public class EndScreen extends BasicLevel{

	public EndScreen(){
		staticEntities = new ArrayList<BasicObject>();
	}
	
	@Override
	public String getLevelMusic(){
		return "endscreen.wav";
	}
}
