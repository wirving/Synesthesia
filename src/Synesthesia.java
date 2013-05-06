import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

//import javax.media.opengl.GL;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.Window;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

class Synesthesia extends JFrame implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, ActionListener {

	private static final int LEVEL_LEVEL_2 = 4;
	private static final int LEVEL_OUTSIDE = 7;
	private static final int LEVEL_END_SCREEN = 0;
	private static final int LEVEL_TITLE_SCREEN = 1;
	private static final int LEVEL_TRANS_2 = 6;

	/* This defines the objModel class, which takes care
	 * of loading a triangular mesh from an obj file,
	 * estimating per vertex average normal,
	 * and displaying the mesh.
	 */
	public class objModel {
		public FloatBuffer vertexBuffer;
		public IntBuffer faceBuffer;
		public FloatBuffer normalBuffer;
		public FloatBuffer texCoordBuffer;
		public Point3f center;
		public int num_verts;		// number of vertices
		public int num_faces;		// number of triangle faces

		public void Draw(Texture tex, TextureParameters params) {
			vertexBuffer.rewind();
			normalBuffer.rewind();
			faceBuffer.rewind();
			texCoordBuffer.rewind();
			
			float[] tiling = params.getTilingCoefficients();
			
			float[] zPlane = {0.0f, 0.0f, 1.0f *tiling[2], 0.0f};
			float[] xPlane = {1.0f * tiling[0], 0.0f, 0.0f, 0.0f};
			float[] yPlane = {0.0f, 1.0f * tiling[LEVEL_TITLE_SCREEN], 0.0f, 0.0f};
			
			Texture random = null;
			if (params != null && params.useRandomTexture() == true){ //Random texture
				//gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGB, 256, 256, 0, GL2.GL_RGB, GL2.GL_BYTE, params.getRandomBuffer());
				
				TextureData data = new TextureData(gl.getGLProfile(), GL2.GL_RGB, 32, 32, 0, GL2.GL_RGB, GL2.GL_BYTE, false, false, false, params.getRandomBuffer(), null );
				random = new Texture(gl, data);
				random.enable(gl);
				random.bind(gl);
				gl.glEnable(GL2.GL_TEXTURE_GEN_S);
				gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_SPHERE_MAP);
				
			}
			if (tex != null){
				tex.enable(gl);
				tex.bind(gl);
				
					if (params.getTexGenMode() == TextureParameters.texCoordGenMode.PLANE)
					{
						gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
						gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
				
						//Wrapping
						gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
						gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
				
						gl.glEnable(GL2.GL_TEXTURE_GEN_S);
						gl.glEnable(GL2.GL_TEXTURE_GEN_T);
						gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_OBJECT_LINEAR);
						gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_OBJECT_LINEAR);
						
						if (params.tile_x_plane){
						gl.glTexGenfv(GL2.GL_S, GL2.GL_OBJECT_PLANE, xPlane, 0);
						}
						
						if (params.tile_z_plane){
						gl.glTexGenfv(GL2.GL_T, GL2.GL_OBJECT_PLANE, zPlane, 0);
						}
						
						if (params.tile_y_plane){
						gl.glTexGenfv(GL2.GL_T, GL2.GL_OBJECT_PLANE, yPlane, 0);
						}
					}
					
					else if (params.getTexGenMode() == TextureParameters.texCoordGenMode.SPHERE) //Sphere
					{
						gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
						gl.glTexCoordPointer(2, GL2.GL_FLOAT, 0, texCoordBuffer);
					}
					
					else{ //Sphere map
						gl.glEnable(GL2.GL_TEXTURE_GEN_S);
						gl.glEnable(GL2.GL_TEXTURE_GEN_T);
						gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_SPHERE_MAP);
						gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_SPHERE_MAP);
					}
			}
			
			gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
			
			gl.glVertexPointer(3, GL2.GL_FLOAT, 0, vertexBuffer);
			gl.glNormalPointer(GL2.GL_FLOAT, 0, normalBuffer);
			
			gl.glDrawElements(GL2.GL_TRIANGLES, num_faces*3, GL2.GL_UNSIGNED_INT, faceBuffer);
			
			gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
			
			if (tex != null){
				if (params.getTexGenMode() == TextureParameters.texCoordGenMode.PLANE || params.getTexGenMode() == TextureParameters.texCoordGenMode.SPHERE_MAP){
					gl.glDisable(GL2.GL_TEXTURE_GEN_S);
					gl.glDisable(GL2.GL_TEXTURE_GEN_T);
				}
				else if (params.getTexGenMode() == TextureParameters.texCoordGenMode.SPHERE) { //Sphere
					gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
				}
				
				
			tex.disable(gl);
			}
			
			if (params != null && params.useRandomTexture() == true){
			
			gl.glDisable(GL2.GL_TEXTURE_GEN_S);
			//gl.glDisable(GL2.GL_TEXTURE_GEN_T);
			random.disable(gl);
			}
		}
		
		public objModel(String filename) {
			/* load a triangular mesh model from a .obj file */
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(filename));
			} catch (IOException e) {
				System.out.println("Error reading from file " + filename);
				System.exit(0);
			}

			center = new Point3f();			
			float x, y, z;
			int v1, v2, v3;
			float minx, miny, minz;
			float maxx, maxy, maxz;
			float bbx, bby, bbz;
			minx = miny = minz = 10000.f;
			maxx = maxy = maxz = -10000.f;
			
			String line;
			String[] tokens;
			ArrayList<Point3f> input_verts = new ArrayList<Point3f> ();
			ArrayList<Integer> input_faces = new ArrayList<Integer> ();
			ArrayList<Vector3f> input_norms = new ArrayList<Vector3f> ();
			try {
			while ((line = in.readLine()) != null) {
				if (line.length() == 0)
					continue;
				switch(line.charAt(0)) {
				case 'v':
					tokens = line.split("[ ]+");
					x = Float.valueOf(tokens[LEVEL_TITLE_SCREEN]);
					y = Float.valueOf(tokens[2]);
					z = Float.valueOf(tokens[3]);
					minx = Math.min(minx, x);
					miny = Math.min(miny, y);
					minz = Math.min(minz, z);
					maxx = Math.max(maxx, x);
					maxy = Math.max(maxy, y);
					maxz = Math.max(maxz, z);
					input_verts.add(new Point3f(x, y, z));
					center.add(new Point3f(x, y, z));
					break;
				case 'f':
					tokens = line.split("[ ]+");
					v1 = Integer.valueOf(tokens[LEVEL_TITLE_SCREEN])-LEVEL_TITLE_SCREEN;
					v2 = Integer.valueOf(tokens[2])-LEVEL_TITLE_SCREEN;
					v3 = Integer.valueOf(tokens[3])-LEVEL_TITLE_SCREEN;
					input_faces.add(v1);
					input_faces.add(v2);
					input_faces.add(v3);				
					break;
				default:
					continue;
				}
			}
			in.close();	
			} catch(IOException e) {
				System.out.println("Unhandled error while reading input file.");
			}

			System.out.println("Read " + input_verts.size() +
						   	" vertices and " + input_faces.size() + " faces.");
			
			center.scale(1.f / (float) input_verts.size());
			
			bbx = maxx - minx;
			bby = maxy - miny;
			bbz = maxz - minz;
			float bbmax = Math.max(bbx, Math.max(bby, bbz));
			
			for (Point3f p : input_verts) {
				
				p.x = (p.x - center.x) / bbmax;
				p.y = (p.y - center.y) / bbmax;
				p.z = (p.z - center.z) / bbmax;
			}
			center.x = center.y = center.z = 0.f;
			
			/* estimate per vertex average normal */
			int i;
			for (i = 0; i < input_verts.size(); i ++) {
				input_norms.add(new Vector3f());
			}
			
			Vector3f e1 = new Vector3f();
			Vector3f e2 = new Vector3f();
			Vector3f tn = new Vector3f();
			for (i = 0; i < input_faces.size(); i += 3) {
				v1 = input_faces.get(i+0);
				v2 = input_faces.get(i+LEVEL_TITLE_SCREEN);
				v3 = input_faces.get(i+2);
				
				e1.sub(input_verts.get(v2), input_verts.get(v1));
				e2.sub(input_verts.get(v3), input_verts.get(v1));
				tn.cross(e1, e2);
				input_norms.get(v1).add(tn);
				
				e1.sub(input_verts.get(v3), input_verts.get(v2));
				e2.sub(input_verts.get(v1), input_verts.get(v2));
				tn.cross(e1, e2);
				input_norms.get(v2).add(tn);
				
				e1.sub(input_verts.get(v1), input_verts.get(v3));
				e2.sub(input_verts.get(v2), input_verts.get(v3));
				tn.cross(e1, e2);
				input_norms.get(v3).add(tn);			
			}

			/* convert to buffers to improve display speed */
			for (i = 0; i < input_verts.size(); i ++) {
				input_norms.get(i).normalize();
			}
			
			//Calculate sphere texture coordinates
			ArrayList<Point2f> tex_coords = new ArrayList<Point2f>();
			for (int j = 0; j < input_norms.size(); j++){
				Vector3f vertex = input_norms.get(j);
				Point2f uv = new Point2f((float)((Math.asin(vertex.x)/Math.PI) + .5), (float)((Math.asin(vertex.y)/Math.PI) + .5));
				tex_coords.add(uv);
				
			}
			
			vertexBuffer = Buffers.newDirectFloatBuffer(input_verts.size()*3);
			normalBuffer = Buffers.newDirectFloatBuffer(input_verts.size()*3);
			faceBuffer = Buffers.newDirectIntBuffer(input_faces.size());
			texCoordBuffer = Buffers.newDirectFloatBuffer(input_norms.size()*2);
			
			for (i = 0; i < input_verts.size(); i ++) {
				vertexBuffer.put(input_verts.get(i).x);
				vertexBuffer.put(input_verts.get(i).y);
				vertexBuffer.put(input_verts.get(i).z);
				normalBuffer.put(input_norms.get(i).x);
				normalBuffer.put(input_norms.get(i).y);
				normalBuffer.put(input_norms.get(i).z);	
				texCoordBuffer.put(tex_coords.get(i).x);
				texCoordBuffer.put(tex_coords.get(i).y);
			}
			
			for (i = 0; i < input_faces.size(); i ++) {
				faceBuffer.put(input_faces.get(i));	
			}			
			num_verts = input_verts.size();
			num_faces = input_faces.size()/3;
		}		
	}


	public void keyPressed(KeyEvent e) {
		pressed.add(e.getKeyCode());
		if(pressed.size() >= LEVEL_TITLE_SCREEN){
			for(Integer key : pressed){
		switch(key) {
		case KeyEvent.VK_ESCAPE:
		case (int)'Q':
			System.exit(0);
			break;		
			/*
		case (int) 'r':
		case (int) 'R':
			initViewParameters();
			break;
			*/
		/*case 'w':
		case 'W':
			wireframe = ! wireframe;
			break;
		case (int) 'b':
		case (int) 'B':
			cullface = !cullface;
			break;
		case (int) 'f':
		case (int) 'F':
			flatshade = !flatshade;
			break;
		/*case 'a':
		case 'A':
			if (animator.isAnimating())
				animator.stop();
			else 
				animator.start();
			break;
		case (int) '+':
		case (int) '=':
			animation_speed *= 1.2f;
			break;
		case (int) '-':
		case (int) '_':
			animation_speed /= 1.2;
			break;
			*/
		//case KeyEvent.VK_UP:
		//case (int) 'I':
		case (int) 'W':
		   float xrotrad, yrotrad;
		if(canMove){
			xrotrad = (rotv / 180 * 3.141592654f); 
			yrotrad = (roth / 180 * 3.141592654f);

			   if(collisionDetect){
				    //need to conditionally floor/ceiling i think
				    double deltaX = (Math.sin(yrotrad))/10;
				    double deltaZ = (Math.cos(yrotrad))/10;
				    double xOffset = 0;
				    double zOffset = 0;
				    int yOffset = 0;
				    int newX;
				    int newZ;
				    if(deltaX >= 0){
				    	xOffset = -0.25;
				    	newX = (int) (Math.ceil(xpos+deltaX+xOffset));
				    }
				    else{
				    	xOffset = 0.25;
				    	newX = (int) (Math.floor(xpos+deltaX+xOffset));
				    }
				    if(deltaZ >= 0){
				    	zOffset = .25;
				    	newZ = (int) (Math.floor(zpos-deltaZ+zOffset));
				    }
				    else{
				    	zOffset = -.25;
				    	newZ = (int) (Math.ceil(zpos-deltaZ+zOffset));
				    }
				    if(collisionArray != null){
						if(collisionArray[newX][newZ] == false){
						    xpos += (float)(Math.sin(yrotrad))/10 ;
						    zpos -= (float)(Math.cos(yrotrad))/10 ;
						 }
				    }
				    else{
					    xpos += (float)(Math.sin(yrotrad))/10 ;
					    zpos -= (float)(Math.cos(yrotrad))/10 ;
				    }
			   }
			   else{
				   xpos += (float)(Math.sin(yrotrad))/10 ;
				   zpos -= (float)(Math.cos(yrotrad))/10 ;
			   }
		}
			break;
		//case KeyEvent.VK_DOWN:
		//case (int) 'K':
		case (int) 'S':
			
			if(canMove){
				xrotrad = (rotv / 180 * 3.141592654f); 
				yrotrad = (roth / 180 * 3.141592654f);

				if(collisionDetect){
					//need to conditionally floor/ceiling i think
					double deltaX = (Math.sin(yrotrad))/10;
					double deltaZ = (Math.cos(yrotrad))/10;
					double xOffset = 0;
					double zOffset = 0;
					int newX;
					int newZ;
					if(deltaX >= 0){
						xOffset = 0.25;
						newX = (int) (Math.floor(xpos+deltaX+xOffset));
					}
					else{
						xOffset = -0.25;
						newX = (int) (Math.ceil(xpos+deltaX+xOffset));
					}
					if(deltaZ >= 0){
						zOffset = -.25;
						newZ = (int) (Math.ceil(zpos-deltaZ+zOffset));
					}
					else{
						zOffset = .25;
						newZ = (int) (Math.floor(zpos-deltaZ+zOffset));
					}
					if(collisionArray != null){
						if(collisionArray[newX][newZ] == false){
							xpos -= (float)(Math.sin(yrotrad))/10 ;
							zpos += (float)(Math.cos(yrotrad))/10 ;
						}
					}
					else{
						xpos -= (float)(Math.sin(yrotrad))/10 ;
						zpos += (float)(Math.cos(yrotrad))/10 ;
					}
				}
				else{
					xpos -= (float)(Math.sin(yrotrad))/10 ;
					zpos += (float)(Math.cos(yrotrad))/10 ;
				}
			}
			break;
			
		//case KeyEvent.VK_LEFT:
		//case (int) 'J':
		case (int) 'A':
			if(canMove){
				xrotrad = (rotv / 180 * 3.141592654f); 
				yrotrad = (roth / 180 * 3.141592654f);
	
			   if(collisionDetect){
				    //need to conditionally floor/ceiling i think
				    double deltaX = (Math.cos(yrotrad))/10;
				    double deltaZ = (Math.sin(yrotrad))/10;
				    double xOffset = 0;
				    double zOffset = 0;
				    int yOffset = 0;
				    int newX, newZ;
				    
				    if(deltaX >= 0){
				    	xOffset = 0.25;
				    	newX = (int) (Math.floor(xpos+deltaX+xOffset));
				    }
				    else{
				    	xOffset = -0.25;
				    	newX = (int) (Math.ceil(xpos+deltaX+xOffset));
				    }
				    if(deltaZ >= 0){
				    	zOffset = .25;
				    	newZ = (int) (Math.floor(zpos-deltaZ+zOffset));
				    }
				    else{
				    	zOffset = -.25;
				    	newZ = (int) (Math.ceil(zpos-deltaZ+zOffset));
				    }
				    if(collisionArray != null){
						if(collisionArray[newX][newZ] == false){
						    xpos -= (float)(Math.cos(yrotrad))/10 ;
						    zpos -= (float)(Math.sin(yrotrad))/10 ;
						 }
				    }
				    else{
					    xpos -= (float)(Math.cos(yrotrad))/10 ;
					    zpos -= (float)(Math.sin(yrotrad))/10 ;
				    }
			   }
			   else{
				   xpos -= (float)(Math.cos(yrotrad))/10 ;
				   zpos -= (float)(Math.sin(yrotrad))/10 ;
			   }
			}
			break;
		
		//Strafe Right
		case (int) 'D':
			if(canMove){
				xrotrad = (rotv / 180 * 3.141592654f); 
				yrotrad = (roth / 180 * 3.141592654f);
	
			   if(collisionDetect){
				    //need to conditionally floor/ceiling i think
				    double deltaX = (Math.cos(yrotrad))/10;
				    double deltaZ = (Math.sin(yrotrad))/10;
				    double xOffset = 0;
				    double zOffset = 0;
				    int newX;
				    int newZ;
				    if(deltaX >= 0){
				    	xOffset = -0.25;
				    	newX = (int) (Math.ceil(xpos+deltaX+xOffset));
				    }
				    else{
				    	xOffset = 0.25;
				    	newX = (int) (Math.floor(xpos+deltaX+xOffset));
				    }
				    if(deltaZ >= 0){
				    	zOffset = -.25;
				    	newZ = (int) (Math.ceil(zpos-deltaZ+zOffset));
				    }
				    else{
				    	zOffset = .25;
				    	newZ = (int) (Math.floor(zpos-deltaZ+zOffset));
				    }
				    if(collisionArray != null){
						if(collisionArray[newX][newZ] == false){
						    xpos += (float)(Math.cos(yrotrad))/10 ;
						    zpos += (float)(Math.sin(yrotrad))/10 ;
						 }
				    }
				    else{
					    xpos += (float)(Math.cos(yrotrad))/10 ;
					    zpos += (float)(Math.sin(yrotrad))/10 ;
				    }
			   }
			   else{
				   xpos += (float)(Math.cos(yrotrad))/10 ;
				   zpos += (float)(Math.sin(yrotrad))/10 ;
			   }
			}
			break;
			
		case KeyEvent.VK_LEFT:
			roth -= 2f; //Smaller value will be smoother but slower
			if(roth < 360)
				roth += 360;
			break;
			
		case KeyEvent.VK_RIGHT:
			roth += 2f;
			if(roth > -360)
				roth -= 360;
			break;
		case KeyEvent.VK_UP:
			rotv -= 2f;
			if(rotv < -360)
				rotv += 360;
			break;
		case KeyEvent.VK_DOWN:
			rotv += 2f;
			if(rotv > 360)
				rotv -= 360;
			break;
			
			//Turn Collision Detection On and Off
		case (int)'O':
			collisionDetect = !collisionDetect;
			break;
		//Action Button
		case KeyEvent.VK_SPACE:
			if(nextLevel == LEVEL_TITLE_SCREEN){
				nextLevel();
			}
			// Check for objects that the player can pick up
			if(grabableObjects != null){
				for(GrabableObject currentObject: grabableObjects){
					if(holdingObject){
						if(currentObject.isGrabbed()){
							if(Math.sqrt((Math.pow(Math.abs(Math.max(xpos,currentObject.getDestX()) - Math.min(xpos,currentObject.getDestX())),2)+Math.pow(Math.abs(Math.max(zpos, currentObject.getDestZ()) - Math.min(zpos,  currentObject.getDestZ())),2))) < 2 ){
								currentObject.place();
								holdingObject = false;
								objectCount = objectCount +1;
							}
						}
					}
					else if(Math.sqrt((Math.pow(Math.abs(Math.max(xpos,currentObject.getXPos()) - Math.min(xpos,currentObject.getXPos())),2)+Math.pow(Math.abs(Math.max(zpos, currentObject.getZPos()) - Math.min(zpos,  currentObject.getZPos())),2))) < 2 ){
						if(!holdingObject && currentObject.getDestX() != currentObject.getXPos() && currentObject.getDestZ() != currentObject.getZPos()){
							currentObject.setIsGrabbed(true);
							holdingObject = true;
							break;
						}
						
					}
				}
			}
			// Objects that transition to the next level or perform some action within the level
			if(interactiveObjects != null){
				for(InteractiveObject currentObject: interactiveObjects){
					if(nextLevel == LEVEL_LEVEL_2){
						if(objectCount == 4){
							currentObject.setDynamic(true);
						}
					}
					InteractiveObject.Function function = currentObject.getFunction();
					
					switch (function){
					case NEXT_LEVEL:
						if(Math.sqrt((Math.pow(Math.abs(Math.max(xpos,currentObject.getXPos()) - Math.min(xpos,currentObject.getXPos())),2)+Math.pow(Math.abs(Math.max(zpos, currentObject.getZPos()) - Math.min(zpos,  currentObject.getZPos())),2))) < currentObject.radius ){
				    		nextLevel();
				    	}
						break;
					case LEVEL_EVENT:
						if(Math.sqrt((Math.pow(Math.abs(Math.max(xpos,currentObject.getXPos()) - Math.min(xpos,currentObject.getXPos())),2)+Math.pow(Math.abs(Math.max(zpos, currentObject.getZPos()) - Math.min(zpos,  currentObject.getZPos())),2))) < currentObject.radius ){
							//Do something in level
							currentLevel.eventHappened();
							updateLevel();
						}
						break;
					case RESTART:
						if(Math.sqrt((Math.pow(Math.abs(Math.max(xpos,currentObject.getXPos()) - Math.min(xpos,currentObject.getXPos())),2)+Math.pow(Math.abs(Math.max(zpos, currentObject.getZPos()) - Math.min(zpos,  currentObject.getZPos())),2))) < currentObject.radius ){
							restart();
						}
						break;
					}		
		
				}
			}
			break;
		case (int) 'P':
			nextLevel();
		break;
		default:
			break;
		}
			}
		}
		canvas.display();
		
	}
	
	/* GL, display, model transformation, and mouse control variables */
	private final GLCanvas canvas;
	private GL2 gl;
	private GLUT glut = new GLUT();
	private final GLU glu = new GLU();
	private Robot robot;
	private boolean mouseLock=false;
	private FPSAnimator animator;
	//private int winW = 800, winH = 800;
	private boolean wireframe = false;
	private boolean cullface = false;
	private boolean flatshade = false;
	
	private float xpos = 0, ypos = 0, zpos = 0;
	private float centerx, centery, centerz;
	private float roth = 0, rotv = 0;
	private float znear, zfar;
	private int mouseX, mouseY, mouseButton;
	private float motionSpeed, rotateSpeed;
	private float animation_speed = 1.0f;
	private boolean holdingObject = false;
	private int counter = 0; 
	
	private Set<Integer> pressed = new ConcurrentSkipListSet<Integer>();
	
	//For mouse movement
	 int dmx = 0; int dmy = 0;
	 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
	
			int screenXDiv2=width>>LEVEL_TITLE_SCREEN;
	          int screenYDiv2=height>>LEVEL_TITLE_SCREEN;

	private int nextLevel =0;
	private boolean canMove = true;
	private int objectCount = 0;
	private TitleScreen titleScreen = new TitleScreen();
	private LevelOne levelOne = new LevelOne();
	private Transition1 transition1 = new Transition1();
	private LevelTwo levelTwo = new LevelTwo();
	private Temple temple = new Temple();
	private Transition2 transition2 = new Transition2();
	private Outside outside = new Outside();
	private EndScreen endScreen = new EndScreen();
	
	private BasicLevel currentLevel;
	
	Clip clip;
	File soundFile;
	AudioInputStream ais;
	
	//For temple set light0 position to {0, 5, 1, 0}
	private ArrayList<BasicObject> objects;
	private ArrayList<InteractiveObject> interactiveObjects;
	private ArrayList<GrabableObject> grabableObjects;
	private ArrayList<ConditionalTimedObject> conditionalTimedObjects;
	private ArrayList<TextObject> textObjects;
	private ArrayList<CollisionObject> collisionObjects;
	private Boolean[][] collisionArray; 
	private TextRenderer renderer;
	
	private boolean collisionDetect = true;
	HashMap<String, objModel> objectMap = new HashMap<String, objModel>();
	private ArrayList<BasicLevel> levelList = new ArrayList<BasicLevel>();
	HashMap<String, Texture> textureMap = new HashMap<String, Texture>();
	
	private float example_rotateT = 0.f;
	/* Here you should give a conservative estimate of the scene's bounding box
	 * so that the initViewParameters function can calculate proper
	 * transformation parameters to display the initial scene.
	 * If these are not set correctly, the objects may disappear on start.
	 */
	private float xmin = -1f, ymin = -1f, zmin = -1f;
	private float xmax = 1f, ymax = 1f, zmax = 1f;	
	
	private float float_translate = 0.f;
	private int float_counter = 0;
	private boolean up = true;
	
	//Do this every time we load a new level?
	public void makeObjectMap(){
		
		objectMap.put("cube", new objModel("floorobj.obj"));
		objectMap.put("plane", new objModel("plane.obj"));
		objectMap.put("cylinder", new objModel("trunk.obj"));
		objectMap.put("statue", new objModel("statue_sword_static.obj"));
		objectMap.put("plant", new objModel("plant.obj"));
		objectMap.put("bottle", new objModel("bottle.obj"));
		objectMap.put("tree_aspen",  new objModel("tree_aspen.obj"));
		objectMap.put("sphere", new objModel("small_sphere.obj"));
		objectMap.put("statue2", new objModel("statue_step_15.obj"));
		objectMap.put("statue3", new objModel("statue_step_2_new.obj"));
		objectMap.put("statue4", new objModel("statue_step_25.obj"));
		objectMap.put("statue5", new objModel("statue_step_3_new.obj"));
		objectMap.put("statue6", new objModel("statue_step_35.obj"));
		objectMap.put("statue7", new objModel("statue_step_4_new.obj"));
		objectMap.put("rock", new objModel("rock.obj"));
		objectMap.put("pond", new objModel("pond2.obj"));
		objectMap.put("deer", new objModel("deer_jump.obj"));
		objectMap.put("doe", new objModel("deer_female.obj"));
		objectMap.put("bird", new objModel("bird2.obj"));
		objectMap.put("bunny", new objModel("bunny.obj"));
		objectMap.put("vase", new objModel("vase.obj"));
	}
	
	public void restart(){
		levelList.clear();
		interactiveObjects.clear();
		objects.clear();
		grabableObjects.clear();
		conditionalTimedObjects.clear();
		textObjects.clear();
		conditionalTimedObjects.clear();
		collisionObjects.clear();
		//collisionArray.clear();
		
		this.titleScreen = new TitleScreen();
		this.endScreen = new EndScreen();
		this.levelOne = new LevelOne();
		this.levelTwo = new LevelTwo();
		this.temple = new Temple();
		this.outside = new Outside();
		this.transition1 = new Transition1();
		this.transition2 = new Transition2();
		nextLevel = 0;
		this.objectCount = 0;
		makeLevelList();
	}
	
public void makeTextureMap(){
		
		ArrayList<String> filenames = new ArrayList<String>();
		filenames.add("floor.png");
		filenames.add("marble_tile2.jpg");
		filenames.add("mosaic.png");
		filenames.add("sky_map.png");
		filenames.add("grass.png");
		filenames.add("tvstatic.jpg");
				
		ArrayList<String> tex_names = new ArrayList<String>();
		tex_names.add("floor");
		tex_names.add("marble");
		tex_names.add("mosaic");
		tex_names.add("sky");
		tex_names.add("grass");
		tex_names.add("static");
				
		for (int i = 0; i < tex_names.size(); i++){
			Texture tex = null;
			try{
				tex = TextureIO.newTexture(new File(filenames.get(i)), false);	
			} catch (Exception e){
				System.out.println("Could not open the file!");
			}
			
			if (tex != null){
				
			}
			
			textureMap.put(tex_names.get(i), tex);
		}
		
	}
	
	public void makeLevelList(){
		levelList.add(titleScreen);
		levelList.add(levelOne);
		levelList.add(transition1);
		levelList.add(levelTwo);
		levelList.add(temple);
		levelList.add(transition2);
		levelList.add(outside);
		levelList.add(endScreen);
		currentLevel = levelList.get(0);
		nextLevel();
	}
	
	public void nextLevel(){
		BasicLevel level = levelList.get(nextLevel);
		/*if(nextLevel == 0){
			mouseLock = true;
			canMove = false;
		}
		else{
			canMove = true;
			mouseLock = false;
		}
		*/
		currentLevel = level;
		objects = level.getStaticEntities();
		interactiveObjects = level.getInteractiveEntities();
		grabableObjects = level.getGrabableEntities();
		conditionalTimedObjects = level.getConditionalTimedObjects();
		collisionArray = level.getCollisionArray();
		textObjects = level.getTextObjects();
		collisionObjects = level.getCollisionEntities();
		xpos = level.getStartX();
		ypos = level.getStartY();
		zpos = level.getStartZ();
		//rotv = level.getStartRotv();
		//roth = level.getStartRoth();
		changeMusic(level.getLevelMusic());
		nextLevel = (nextLevel+LEVEL_TITLE_SCREEN)%levelList.size();
	}
	
	public void updateLevel(){
		objects = currentLevel.getStaticEntities();
		interactiveObjects = currentLevel.getInteractiveEntities();
		collisionArray = currentLevel.getCollisionArray();
		grabableObjects = currentLevel.getGrabableEntities();
		conditionalTimedObjects = currentLevel.getConditionalTimedObjects();
		textObjects = currentLevel.getTextObjects();
		collisionObjects = currentLevel.getCollisionEntities();
		changeMusic(currentLevel.getLevelMusic());
	}
	
	public void changeMusic(String fileName){
		try{
			if(clip != null){
				clip.close();
			}
		clip = AudioSystem.getClip();
		soundFile = new File(fileName);
        ais = AudioSystem.
            getAudioInputStream( soundFile );
        clip.open(ais);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void display(GLAutoDrawable drawable) {
		
		System.out.println("Player X: " + xpos + " Player Z: "+ zpos);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, wireframe ? GL2.GL_LINE : GL2.GL_FILL);	
		gl.glShadeModel(flatshade ? GL2.GL_FLAT : GL2.GL_SMOOTH);	
		if (cullface)
			gl.glEnable(GL2.GL_CULL_FACE);
		else
			gl.glDisable(GL2.GL_CULL_FACE);		
		

		
		gl.glLoadIdentity();
		
		
		/* this is the transformation of the entire scene */
		gl.glTranslatef(centerx, centery, centerz);
		gl.glRotatef(rotv, 1.0f, 0, 0);
		if(grabableObjects != null){
			for(GrabableObject currentObject: grabableObjects){
				if(currentObject.isGrabbed()){
					gl.glPushMatrix();
					gl.glTranslatef(0, -.25f, -LEVEL_TITLE_SCREEN);
					gl.glScalef(.25f, .25f, .25f);
				    gl.glMaterialfv( GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, currentObject.getDiffuseColor(), 0);
				    gl.glMaterialfv( GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, currentObject.getSpecularColor(), 0);
				    Texture tex1 = textureMap.get(currentObject.getTexParams());
			    	objectMap.get(currentObject.getObject()).Draw(tex1, new TextureParameters());
					gl.glPopMatrix();
				}
			}
		}
		gl.glRotatef(roth, 0, 1.0f, 0);
		
		
		gl.glTranslatef(-centerx, -centery, -centerz);	

		gl.glTranslatef(-xpos, -ypos, -zpos);
		
		gl.glPushMatrix();
				
		//Check triggers
		
		boolean canDrop = true;

		if(objects != null){
			for(final BasicObject currentObject : objects){
				
				gl.glPushMatrix();
				currentObject.Move(xpos,ypos,zpos);
				
				gl.glRotatef(currentObject.getXRot(), 1f, 0f, 0f);
			    gl.glRotatef(currentObject.getYRot(), 0f, 1f, 0f);
			    gl.glRotatef(currentObject.getZRot(), 0f, 0f, 1f);
				
				gl.glTranslatef(currentObject.getXPos(), currentObject.getYPos(), currentObject.getZPos());
			    gl.glMaterialfv( GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, currentObject.getDiffuseColor(), 0);
			    gl.glMaterialfv( GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, currentObject.getSpecularColor(), 0);
			    gl.glScalef(currentObject.getXScale(), currentObject.getYScale(), currentObject.getZScale());
			    
			 
			    Texture tex = textureMap.get(currentObject.getTexParams().getTextureName());
			    objectMap.get(currentObject.getObject()).Draw(tex, currentObject.getTexParams());
		
				gl.glPopMatrix();
				
				if(nextLevel == LEVEL_TRANS_2){
					if(Math.sqrt((Math.pow(Math.abs(Math.max(xpos,currentObject.getXPos()) - Math.min(xpos,currentObject.getXPos())),2)+Math.pow(Math.abs(Math.max(zpos, currentObject.getZPos()) - Math.min(zpos,  currentObject.getZPos())),2))) < LEVEL_TITLE_SCREEN ){
						if(ypos - currentObject.getYPos() > 0 && ypos - currentObject.getYPos() < 2){
							canDrop = false; 
						}
					}
				}
			}
		}
		
		if((nextLevel == LEVEL_TRANS_2 || nextLevel == LEVEL_OUTSIDE || nextLevel == LEVEL_END_SCREEN) && canDrop){
			if(ypos > 0){
				if(counter == 0){
					counter = 5;
					ypos = ypos-1;
				}
				else if(counter > 0){
					counter--;
				}
			}
		}
		if(nextLevel == LEVEL_TRANS_2 && ypos == 0){
			nextLevel();
		}

		if(conditionalTimedObjects != null){
			for(final ConditionalTimedObject currentObject : conditionalTimedObjects){
				
				if(currentObject.isCollideable()){
					collisionArray[(int) currentObject.getXPos()][(int) currentObject.getZPos()] = true;
				}
				else{
					collisionArray[(int) currentObject.getXPos()][(int) currentObject.getZPos()] = false;
				}
				gl.glPushMatrix();
				currentObject.Move(xpos,ypos,zpos);
				
				gl.glRotatef(currentObject.getXRot(), 1f, 0f, 0f);
			    gl.glRotatef(currentObject.getYRot(), 0f, 1f, 0f);
			    gl.glRotatef(currentObject.getZRot(), 0f, 0f, 1f);
				
				gl.glTranslatef(currentObject.getXPos(), currentObject.getYPos(), currentObject.getZPos());
			    gl.glMaterialfv( GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, currentObject.getDiffuseColor(), 0);
			    gl.glMaterialfv( GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, currentObject.getSpecularColor(), 0);
			    gl.glScalef(currentObject.getXScale(), currentObject.getYScale(), currentObject.getZScale());
			    
			 
			    	Texture tex = textureMap.get(currentObject.getTexParams().getTextureName());
			    	objectMap.get(currentObject.getObject()).Draw(tex, currentObject.getTexParams());
		
			    
				//cube.Draw();
				gl.glPopMatrix();
			}
		}

		if(interactiveObjects != null){
		for(final InteractiveObject currentObject : interactiveObjects){
			
			gl.glPushMatrix();
			currentObject.Move(xpos,ypos,zpos);
			gl.glTranslatef(currentObject.getXPos(), currentObject.getYPos(), currentObject.getZPos());
			
		    gl.glRotatef(currentObject.getXRot(), 1f, 0f, 0f);
		    gl.glRotatef(currentObject.getYRot(), 0f, 1f, 0f);
		    gl.glRotatef(currentObject.getZRot(), 0f, 0f, 1f);
		    
			if (currentObject.getFunction() == InteractiveObject.Function.NEXT_LEVEL){
		    gl.glMaterialfv( GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, currentObject.getRandomDiffuseColor(), 0);
			}
		
			else{
			gl.glMaterialfv( GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, currentObject.getDiffuseColor(), 0);
			}
			
		    gl.glMaterialfv( GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, currentObject.getSpecularColor(), 0);
		    gl.glScalef(currentObject.getXScale(), currentObject.getYScale(), currentObject.getZScale());
		    
		    Texture tex = textureMap.get(currentObject.getTexParams().getTextureName());
	    	objectMap.get(currentObject.getObject()).Draw(tex, currentObject.getTexParams());
			//cube.Draw();
			gl.glPopMatrix();
			}
		}
		
		if(grabableObjects != null){
			for(final GrabableObject currentObject: grabableObjects){
				if(!currentObject.isGrabbed){
					gl.glPushMatrix();
					currentObject.Move(xpos,ypos,zpos);
					
					gl.glRotatef(currentObject.getXRot(), 1f, 0f, 0f);
				    gl.glRotatef(currentObject.getYRot(), 0f, 1f, 0f);
				    gl.glRotatef(currentObject.getZRot(), 0f, 0f, 1f);
					
					gl.glTranslatef(currentObject.getXPos(), currentObject.getYPos(), currentObject.getZPos());
				    gl.glMaterialfv( GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, currentObject.getDiffuseColor(), 0);
				    gl.glMaterialfv( GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, currentObject.getSpecularColor(), 0);
				    gl.glScalef(currentObject.getXScale(), currentObject.getYScale(), currentObject.getZScale());
				    
				 
				    	Texture tex = textureMap.get(currentObject.getTexParams().getTextureName());
				    	objectMap.get(currentObject.getObject()).Draw(tex, currentObject.getTexParams());
			
				    
					//cube.Draw();
					gl.glPopMatrix();
				}
			}
		}
		
		if(textObjects != null){
			for(TextObject currentObject: textObjects){
				gl.glPushMatrix();
					gl.glTranslatef(currentObject.getXPos(), currentObject.getYPos(), currentObject.getZPos());
					gl.glScalef(currentObject.getXScale(), currentObject.getYScale(), currentObject.getZScale());
					gl.glRotatef(currentObject.getRotDegree(), currentObject.getXRot(), currentObject.getYRot(), currentObject.getZRot());
				    gl.glMaterialfv( GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, currentObject.getDiffuse(), 0);
				    gl.glMaterialfv( GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, currentObject.getSpecular(), 0);
			    	glut.glutStrokeString(glut.STROKE_ROMAN, currentObject.getText());
		    	gl.glPopMatrix();
			}
		}
		
		if(collisionObjects != null){
			for(final CollisionObject currentObject : collisionObjects){
				
				gl.glPushMatrix();
				currentObject.Move(xpos,ypos,zpos);
				
				if (currentObject.playerCollision(xpos, ypos, zpos))
						nextLevel();
				
				gl.glTranslatef(currentObject.getXPos(), currentObject.getYPos(), currentObject.getZPos());
				
			    gl.glRotatef(currentObject.getXRot(), 1f, 0f, 0f);
			    gl.glRotatef(currentObject.getYRot(), 0f, 1f, 0f);
			    gl.glRotatef(currentObject.getZRot(), 0f, 0f, 1f);
			    
				gl.glMaterialfv( GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, currentObject.getRandomDiffuseColor(), 0);
				
			    gl.glMaterialfv( GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, currentObject.getSpecularColor(), 0);
			    gl.glScalef(currentObject.getXScale(), currentObject.getYScale(), currentObject.getZScale());
			    
			    Texture tex = textureMap.get(currentObject.getTexParams().getTextureName());
		    	objectMap.get(currentObject.getObject()).Draw(tex, currentObject.getTexParams());
				//cube.Draw();
				gl.glPopMatrix();
				}
			}
		
		
		gl.glPopMatrix(); //End Drawing
		
		/* increment example_rotateT */
		if (animator.isAnimating())
			example_rotateT += 1.0f * animation_speed;
		/*increment float_translate */
		if(up){
			float_counter++;
			float_translate += 0.020f;
		}
		else{
			float_counter--;
			float_translate += -0.020f;
		}
		if(float_counter == 30){
			up = false;
		}
		if(float_counter == 0){
			up = true;
		}
	}	
	
	public Synesthesia() {
		super("Synesthesia");
		canvas = new GLCanvas();
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		animator = new FPSAnimator(canvas, 60);	// create a 30 fps animator
		getContentPane().add(canvas);
		setSize(width, height);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		animator.start();
		canvas.requestFocus();
	}
	
	public static void main(String[] args) {
		//new().setVisible(true);
		new Synesthesia().setVisible(true);
	}
	
	public void init(GLAutoDrawable drawable) {
		
	    renderer = new TextRenderer(new Font("StrokeRoman", Font.BOLD, 36));

	    
		gl = drawable.getGL().getGL2();

		initViewParameters();
		
		makeObjectMap();
		makeTextureMap();
		makeLevelList();
		
		gl.glClearColor(.1f, .1f, .1f, 1f);
		gl.glClearDepth(1.0f);

		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Eventually set lights based on scene description
	    float light0_position[] = { 0, 5, LEVEL_TITLE_SCREEN, 0 };
	    float light0_diffuse[] = { LEVEL_TITLE_SCREEN, LEVEL_TITLE_SCREEN, LEVEL_TITLE_SCREEN, LEVEL_TITLE_SCREEN };
	    float light0_specular[] = { LEVEL_TITLE_SCREEN, LEVEL_TITLE_SCREEN, LEVEL_TITLE_SCREEN, LEVEL_TITLE_SCREEN };
	    gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_POSITION, light0_position, 0);
	    gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light0_diffuse, 0);
	    gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_SPECULAR, light0_specular, 0);

	    float light1_position[] = { -LEVEL_TITLE_SCREEN, 0, 0, 0 };
	    float light1_diffuse[] = { LEVEL_TITLE_SCREEN,LEVEL_TITLE_SCREEN,LEVEL_TITLE_SCREEN, LEVEL_TITLE_SCREEN };
	    float light1_specular[] = { LEVEL_TITLE_SCREEN,LEVEL_TITLE_SCREEN,LEVEL_TITLE_SCREEN, LEVEL_TITLE_SCREEN };
	    gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_POSITION, light1_position, 0);
	    gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_DIFFUSE, light1_diffuse, 0);
	    gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_SPECULAR, light1_specular, 0);

	    float light2_position[] = { LEVEL_TITLE_SCREEN, 0, 0, 0 };
	    float light2_diffuse[] ={ LEVEL_TITLE_SCREEN,LEVEL_TITLE_SCREEN,LEVEL_TITLE_SCREEN, LEVEL_TITLE_SCREEN };
	    float light2_specular[] = { LEVEL_TITLE_SCREEN,LEVEL_TITLE_SCREEN,LEVEL_TITLE_SCREEN, LEVEL_TITLE_SCREEN };
	    gl.glLightfv( GL2.GL_LIGHT2, GL2.GL_POSITION, light2_position, 0);
	    gl.glLightfv( GL2.GL_LIGHT2, GL2.GL_DIFFUSE, light2_diffuse, 0);
	    gl.glLightfv( GL2.GL_LIGHT2, GL2.GL_SPECULAR, light2_specular, 0);

	    
	    float light3_position[] = { 0, 0, -1f, 0 };
	    float light3_diffuse[] = { LEVEL_TITLE_SCREEN,LEVEL_TITLE_SCREEN,LEVEL_TITLE_SCREEN, LEVEL_TITLE_SCREEN };
	    float light3_specular[] = { LEVEL_TITLE_SCREEN,LEVEL_TITLE_SCREEN,LEVEL_TITLE_SCREEN,LEVEL_TITLE_SCREEN };
	    gl.glLightfv( GL2.GL_LIGHT3, GL2.GL_POSITION, light3_position, 0);
	    gl.glLightfv( GL2.GL_LIGHT3, GL2.GL_DIFFUSE, light3_diffuse, 0);
	    gl.glLightfv( GL2.GL_LIGHT3, GL2.GL_SPECULAR, light3_specular, 0);
	    
	    //material
	    float mat_ambient[] = { 0, 0, 0, LEVEL_TITLE_SCREEN };
	    float mat_specular[] = { .8f, .8f, .8f, LEVEL_TITLE_SCREEN };
	    float mat_diffuse[] = { .4f, .4f, .4f, LEVEL_TITLE_SCREEN };
	    float mat_shininess[] = { 128 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_AMBIENT, mat_ambient, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);

	    float bmat_ambient[] = { 0, 0, 0, LEVEL_TITLE_SCREEN };
	    float bmat_specular[] = { 0, .8f, .8f, LEVEL_TITLE_SCREEN };
	    float bmat_diffuse[] = { 0, .4f, .4f, LEVEL_TITLE_SCREEN };
	    float bmat_shininess[] = { 128 };
	    gl.glMaterialfv( GL2.GL_BACK, GL2.GL_AMBIENT, bmat_ambient, 0);
	    gl.glMaterialfv( GL2.GL_BACK, GL2.GL_SPECULAR, bmat_specular, 0);
	    gl.glMaterialfv( GL2.GL_BACK, GL2.GL_DIFFUSE, bmat_diffuse, 0);
	    gl.glMaterialfv( GL2.GL_BACK, GL2.GL_SHININESS, bmat_shininess, 0);

	    float lmodel_ambient[] = { 0, 0, 0, LEVEL_TITLE_SCREEN };
	    gl.glLightModelfv( GL2.GL_LIGHT_MODEL_AMBIENT, lmodel_ambient, 0);
	    gl.glLightModeli( GL2.GL_LIGHT_MODEL_TWO_SIDE, LEVEL_TITLE_SCREEN );

	    gl.glEnable( GL2.GL_NORMALIZE );
	    gl.glEnable( GL2.GL_LIGHTING );
	    gl.glEnable( GL2.GL_LIGHT0 );
	    gl.glEnable( GL2.GL_LIGHT1 );
	    gl.glEnable( GL2.GL_LIGHT2 );
	    gl.glEnable( GL2.GL_LIGHT3);

	    gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LESS);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		gl.glCullFace(GL2.GL_BACK);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glShadeModel(GL2.GL_SMOOTH);	
		
		canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		/*winW = width;
		winH = height;
*/
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glLoadIdentity();
			glu.gluPerspective(45.f, (float)width/(float)height, znear, zfar);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}
	
	public void mousePressed(MouseEvent e) {	
		/*
		mouseX = e.getX();
		mouseY = e.getY();
		mouseButton = e.getButton();
		canvas.display();
		*/
	}
	
	public void mouseReleased(MouseEvent e) {
		/*
		mouseButton = MouseEvent.NOBUTTON;
		canvas.display();
		*/
		
	}	
	
	public void mouseDragged(MouseEvent e) {
		/*int x = e.getX();
		int y = e.getY();
		if (mouseButton == MouseEvent.BUTTON3) {
			zpos -= (y - mouseY) * motionSpeed;
			mouseX = x;
			mouseY = y;
			canvas.display();
		} else if (mouseButton == MouseEvent.BUTTON2) {
			xpos -= (x - mouseX) * motionSpeed;
			ypos += (y - mouseY) * motionSpeed;
			mouseX = x;
			mouseY = y;
			canvas.display();
		} else if (mouseButton == MouseEvent.BUTTON1) {
			roth -= (x - mouseX) * rotateSpeed;
			rotv += (y - mouseY) * rotateSpeed;
			mouseX = x;
			mouseY = y;
			canvas.display();
		}
		*/
	}
	
	
	/* computes optimal transformation parameters for OpenGL rendering.
	 * this is based on an estimate of the scene's bounding box
	 */	
	void initViewParameters()
	{
		roth = 90; 
		rotv = 0;

		float ball_r = (float) Math.sqrt((xmax-xmin)*(xmax-xmin)
							+ (ymax-ymin)*(ymax-ymin)
							+ (zmax-zmin)*(zmax-zmin)) * 0.707f;

		centerx = (xmax+xmin)/2.f;
		centery = (ymax+ymin)/2.f;
		centerz = (zmax+zmin)/2.f;
		xpos = centerx;
		ypos = centery;
		zpos = ball_r/(float) Math.sin(45.f*Math.PI/180.f)+centerz;

		znear = 0.01f;
		zfar  = 1000.f;

		motionSpeed = 0.002f * ball_r;
		rotateSpeed = 0.1f;

	}	
	
	// these event functions are not used for this assignment
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) { }
	public void keyTyped(KeyEvent e) { }
	public void keyReleased(KeyEvent e) {
		pressed.remove(e.getKeyCode());
		if(pressed.size() >= LEVEL_TITLE_SCREEN){
			e.setKeyCode(pressed.iterator().next());
			keyPressed(e);
		}
	}
	//public void mouseMoved(MouseEvent e) { }
	public void mouseMoved(MouseEvent e) { 
		if(this.mouseLock == false){	          
	         int mx=e.getXOnScreen();
	          int my=e.getYOnScreen();
	          if(mx!=screenXDiv2 || my!=screenYDiv2)
	          {
	             if(mx!=screenXDiv2)
	             {
	                dmx+=mx-screenXDiv2;
	             }
	             if(my!=screenYDiv2)
	             {
	                dmy+=my-screenYDiv2;   
	             }
	             robot.mouseMove(screenXDiv2, screenYDiv2);
	             
	             roth = (float) (dmx *.1);
	             if(dmy*.1 > -100 && dmy*.1 < 100)
		         rotv = (float)(dmy *.1);
	          }
	          
	          
	          
		}
	}
	///
	
	public void actionPerformed(ActionEvent e) { }
	public void mouseClicked(MouseEvent e) { }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) {	}	
	public void mouseMove(MouseEvent e) { }

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}


