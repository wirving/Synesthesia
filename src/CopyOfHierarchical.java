import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

//import com.sun.opengl.util.BufferUtil;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.FPSAnimator;
//import com.sun.opengl.util.GLUT;

import java.awt.Robot;
import java.awt.AWTException; 
import java.awt.Toolkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class CopyOfHierarchical extends JFrame implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, ActionListener {

	/* This defines the objModel class, which takes care
	 * of loading a triangular mesh from an obj file,
	 * estimating per vertex average normal,
	 * and displaying the mesh.
	 */
	public class objModel {
		public FloatBuffer vertexBuffer;
		public IntBuffer faceBuffer;
		public FloatBuffer normalBuffer;
		public Point3f center;
		public int num_verts;		// number of vertices
		public int num_faces;		// number of triangle faces

		public void Draw() {
			vertexBuffer.rewind();
			normalBuffer.rewind();
			faceBuffer.rewind();
			gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
			
			gl.glVertexPointer(3, GL2.GL_FLOAT, 0, vertexBuffer);
			gl.glNormalPointer(GL2.GL_FLOAT, 0, normalBuffer);
			
			gl.glDrawElements(GL2.GL_TRIANGLES, num_faces*3, GL2.GL_UNSIGNED_INT, faceBuffer);
			
			gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
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
					x = Float.valueOf(tokens[1]);
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
					v1 = Integer.valueOf(tokens[1])-1;
					v2 = Integer.valueOf(tokens[2])-1;
					v3 = Integer.valueOf(tokens[3])-1;
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
				v2 = input_faces.get(i+1);
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
			
			vertexBuffer = Buffers.newDirectFloatBuffer(input_verts.size()*3);
			normalBuffer = Buffers.newDirectFloatBuffer(input_verts.size()*3);
			faceBuffer = Buffers.newDirectIntBuffer(input_faces.size());
			
			for (i = 0; i < input_verts.size(); i ++) {
				vertexBuffer.put(input_verts.get(i).x);
				vertexBuffer.put(input_verts.get(i).y);
				vertexBuffer.put(input_verts.get(i).z);
				normalBuffer.put(input_norms.get(i).x);
				normalBuffer.put(input_norms.get(i).y);
				normalBuffer.put(input_norms.get(i).z);			
			}
			
			for (i = 0; i < input_faces.size(); i ++) {
				faceBuffer.put(input_faces.get(i));	
			}			
			num_verts = input_verts.size();
			num_faces = input_faces.size()/3;
		}		
	}


	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
		case KeyEvent.VK_Q:
			System.exit(0);
			break;		
		case 'r':
		case 'R':
			initViewParameters();
			break;
		/*case 'w':
		case 'W':
			wireframe = ! wireframe;
			break;*/
		case 'b':
		case 'B':
			cullface = !cullface;
			break;
		case 'f':
		case 'F':
			flatshade = !flatshade;
			break;
		/*case 'a':
		case 'A':
			if (animator.isAnimating())
				animator.stop();
			else 
				animator.start();
			break;*/
		case '+':
		case '=':
			animation_speed *= 1.2f;
			break;
		case '-':
		case '_':
			animation_speed /= 1.2;
			break;
		case KeyEvent.VK_UP:
		case 'I':
			   float xrotrad, yrotrad;
			    yrotrad = (roth / 180 * 3.141592654f);
			    xrotrad = (rotv / 180 * 3.141592654f); 
			    xpos += (float)(Math.sin(yrotrad))/10 ;
			    zpos -= (float)(Math.cos(yrotrad))/10 ;
			   // ypos -= (float)(Math.sin(xrotrad))/2 ;
			//zpos -= .05;
			//centerx = xpos;
			//centery = ypos;
			//centerz = zpos;
			break;
		case KeyEvent.VK_DOWN:
		case 'K':
			// float xrotrad, yrotrad;
			 yrotrad = (roth / 180 * 3.141592654f);
			 xrotrad = (rotv / 180 * 3.141592654f); 
			 xpos -= (float)(Math.sin(yrotrad))/10 ;
			 zpos += (float)(Math.cos(yrotrad))/10 ;
			 //ypos += (float)(Math.sin(xrotrad))/2 ;
			//zpos -= -.05;
			//centerx = xpos;
			//centery = ypos;
			//centerz = zpos;
			break;
			
		case KeyEvent.VK_LEFT:
		case 'J':
		   // float yrotrad;
		    yrotrad = (roth / 180 * 3.141592654f);
		    xpos -= (float)(Math.cos(yrotrad)) * 0.1;
		    zpos -= (float)(Math.sin(yrotrad)) * 0.1;
			//centerx = xpos;
			//centery = ypos;
			//centerz = zpos;
			break;
		
		case KeyEvent.VK_RIGHT:
		case 'L':
		    yrotrad = (roth / 180 * 3.141592654f);
		    xpos += (float)(Math.cos(yrotrad)) * 0.1;
		    zpos += (float)(Math.sin(yrotrad)) * 0.1;
			//centerx = xpos;
			//centery = ypos;
			//centerz = zpos;
			break;
			
		case 'a':
		case 'A':
			roth -= 2f;
			if(roth < 360)
				roth += 360;
			break;
			
		case 'd':
		case 'D':
			roth += 2f;
			if(roth > -360)
				roth -= 360;
			break;
		case 'w':
		case 'W':
			rotv -= 2f;
			if(rotv < -360)
				rotv += 360;
			break;
		case 's':
		case 'S':
			rotv += 2f;
			if(rotv > 360)
				rotv -= 360;
		default:
			break;
		}
		canvas.display();
	}
	
	/* GL, display, model transformation, and mouse control variables */
	private final GLCanvas canvas;
	private GL2 gl;
	private final GLU glu = new GLU();
	private FPSAnimator animator;

	private int winW = 800, winH = 800;
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
	
	
	
	
	
	/* === YOUR WORK HERE === */
	/* Define more models you need for constructing your scene */
	//private objModel ceiling_fan = new objModel("ceiling_fan.obj");
	//private objModel ring = new objModel("ring.obj");
	//private objModel female = new objModel("female.obj");
	//private objModel male = new objModel("male.obj");
	//private objModel camera = new objModel("camera.obj");
	//private objModel bottle = new objModel("bottle.obj");
	//private objModel vase = new objModel("vase.obj");
	//private objModel tulip = new objModel("tulip.obj");
	//private objModel pitcher = new objModel("pitcher.obj");
	private objModel floor = new objModel("floorobj.obj");
	//private objModel cone = new objModel("cone.obj");
	//private objModel sky = new objModel("sky.obj");
	//private objModel rock = new objModel("rock.obj");
	//private objModel trunk = new objModel("trunk.obj");
	
	private BasicObject thing = new BasicObject(1f,1f,1f);
	private LevelOne levelOne = new LevelOne();
	private ArrayList<BasicObject> objects = levelOne.getStaticEntities();
	//HashMap<String, objModel> objectMap = new HashMap<String, objModel>();
	
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
	public void display(GLAutoDrawable drawable) {
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

		gl.glRotatef(roth, 0, 1.0f, 0);
		gl.glTranslatef(-centerx, -centery, -centerz);	

		gl.glTranslatef(-xpos, -ypos, -zpos);

		/* === YOUR WORK HERE === */
		
		/* Below is an example of a rotating bunny
		 * It rotates the bunny with example_rotateT degrees around the bunny's gravity center  
		 */
		
		
		gl.glPushMatrix();
		
		gl.glPushMatrix();
		gl.glScalef(20,20,20);
	    gl.glMaterialfv( GL2.GL_BACK, GL2.GL_DIFFUSE, new float[]{.5f,.5f,.5f,1f}, 0);
		floor.Draw();
		gl.glPopMatrix();
		
		/*
		for(int i = 0; i < objects.size(); i++){
			gl.glPushMatrix();
			BasicObject currentObject = objects.get(i);
			currentObject.Move(xpos,ypos,zpos);
			gl.glTranslatef(currentObject.getXPos(), currentObject.getYPos(), currentObject.getZPos());
		    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, currentObject.getDiffuse(), 0);

			floor.Draw();
			gl.glPopMatrix();
		}
		*/
		/*
		
		//Floor
		gl.glPushMatrix();
		gl.glTranslatef(0f, -1f, 0f);
		gl.glScalef(100f,.1f,100f);
		
	    float mat_ambient[] = { 0, 1f, 0, 1 };
	    float mat_specular[] = { .8f, .8f, .8f, 1 };
	    float mat_diffuse[] = { 0f, 1f, 0f, 1 };
	    float mat_shininess[] = { 128 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_AMBIENT, mat_ambient, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);
		
		floor.Draw();
		gl.glPopMatrix(); //Floor
		
		//Sky
		gl.glPushMatrix();
		gl.glScalef(50f,50f,50f);
		sky.Draw();
		gl.glPopMatrix(); //Sky
		
		//Rock
		
		gl.glPushMatrix();
		gl.glTranslatef(1f, -1f, 1f);
		
	    mat_ambient = new float[]{ 0, 0, 0, 1 };
	    mat_specular = new float[]{ .3f, .3f, .3f, 1 };
	    mat_diffuse = new float[]{ .4f, .3f, .4f, 1 };
	    mat_shininess = new float[]{ 128 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_AMBIENT, mat_ambient, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);
	    
		rock.Draw();
		gl.glPopMatrix(); //Rock
		
		//Rock
		
		gl.glPushMatrix();
		gl.glTranslatef(-3f, -1f, 4f);
		
	    mat_ambient = new float[]{ 0, 0, 0, 1 };
	    mat_specular = new float[]{ .3f, .3f, .3f, 1 };
	    mat_diffuse = new float[]{ .4f, .3f, .4f, 1 };
	    mat_shininess = new float[]{ 128 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_AMBIENT, mat_ambient, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);
	    
		rock.Draw();
		gl.glPopMatrix();  //Rock
		
		//Rock
		
		gl.glPushMatrix();
		gl.glTranslatef(6f, -1f, -8f);
		
	    mat_ambient = new float[]{ 0, 0, 0, 1 };
	    mat_specular = new float[]{ .3f, .3f, .3f, 1 };
	    mat_diffuse = new float[]{ .4f, .3f, .4f, 1 };
	    mat_shininess = new float[]{ 128 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_AMBIENT, mat_ambient, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);
	    
		rock.Draw();
		gl.glPopMatrix();  //Rock
		
		//Rock 
		
		gl.glPushMatrix();
		gl.glTranslatef(15f, -1f, 15f);
		
	    mat_ambient = new float[]{ 0, 0, 0, 1 };
	    mat_specular = new float[]{ .3f, .3f, .3f, 1 };
	    mat_diffuse = new float[]{ .4f, .3f, .4f, 1 };
	    mat_shininess = new float[]{ 128 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_AMBIENT, mat_ambient, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);
	    
		rock.Draw();
		gl.glPopMatrix();  //Rock
		
		
		//Rock
		
		gl.glPushMatrix();
		gl.glTranslatef(-10f, -1f, -5f);
		
	    mat_ambient = new float[]{ 0, 0, 0, 1 };
	    mat_specular = new float[]{ .3f, .3f, .3f, 1 };
	    mat_diffuse = new float[]{ .4f, .3f, .4f, 1 };
	    mat_shininess = new float[]{ 128 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_AMBIENT, mat_ambient, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);
	    
		rock.Draw();
		gl.glPopMatrix(); //Rock
		
		//Tree
		gl.glPushMatrix();
		gl.glScalef(.5f, 2f, .5f);
		gl.glTranslatef(5f, 0, 5f);
		
	    mat_specular = new float[]{ .8f, .8f, .8f, 1 };
	    mat_diffuse = new float[]{ 1f, 0f, 0f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_AMBIENT, mat_ambient, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);
	    
		trunk.Draw();
		
		gl.glScalef(2f, .5f, 2f);
		gl.glTranslatef(0f, 1f, 0f);
	    mat_diffuse = new float[]{ 0f, 1f, 0f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);

		rock.Draw();
		gl.glPopMatrix(); //Tree
		
		//Tree
		gl.glPushMatrix();
		gl.glScalef(.5f, 2f, .5f);
		gl.glTranslatef(-7f, 0, -12f);
		
	    mat_specular = new float[]{ .8f, .8f, .8f, 1 };
	    mat_diffuse = new float[]{ 1f, 0f, 0f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_AMBIENT, mat_ambient, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);
	    
		trunk.Draw();
		
		gl.glScalef(2f, .5f, 2f);
		gl.glTranslatef(0f, 1f, 0f);
	    mat_diffuse = new float[]{ 0f, 1f, 0f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);

		rock.Draw();
		gl.glPopMatrix(); //Tree
		
		//Tree
		gl.glPushMatrix();
		gl.glScalef(.25f, 2f, .25f);
		gl.glTranslatef(20f, 0, 20f);
		
	    mat_specular = new float[]{ .8f, .8f, .8f, 1 };
	    mat_diffuse = new float[]{ 1f, 0f, 0f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_AMBIENT, mat_ambient, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);
	    
		trunk.Draw();
		
		gl.glScalef(2.75f, .5f, 2.75f);
		gl.glTranslatef(0f, 1f, 0f);
	    mat_diffuse = new float[]{ 0f, 1f, 0f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);

		rock.Draw();
		gl.glPopMatrix(); //Tree
		
		//Tree
		gl.glPushMatrix();
		gl.glScalef(.5f, 2f, .5f);
		gl.glTranslatef(-18f, 0, 10f);
		
	    mat_specular = new float[]{ .8f, .8f, .8f, 1 };
	    mat_diffuse = new float[]{ 1f, 0f, 0f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_AMBIENT, mat_ambient, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);
	    
		trunk.Draw();
		
		gl.glScalef(2f, .5f, 2f);
		gl.glTranslatef(0f, 1f, 0f);
	    mat_diffuse = new float[]{ 0f, 1f, 0f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);

		rock.Draw();
		gl.glPopMatrix(); //Tree
		
		//Tree
		gl.glPushMatrix();
		gl.glScalef(.5f, 2f, .5f);
		gl.glTranslatef(8f, 0, -16f);
		
	    mat_diffuse = new float[]{ 1f, 0f, 0f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    
		trunk.Draw();
		
		gl.glScalef(2f, .5f, 2f);
		gl.glTranslatef(0f, 1f, 0f);
	    mat_diffuse = new float[]{ 0f, 1f, 0f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);

		rock.Draw();
		gl.glPopMatrix(); //Tree
		
		//Female
		gl.glPushMatrix();
		gl.glTranslatef(0, float_translate, 0);
		gl.glScalef(.5f,.5f,.5f);
		
	    mat_diffuse = new float[]{ (float)Math.random(), (float)Math.random(), (float)Math.random(), 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    
		female.Draw();
		
		
		gl.glPushMatrix();
		gl.glRotatef(example_rotateT, 0, 1, 0);
		gl.glTranslatef(0,-.75f,0);
		gl.glScalef(1,.10f,1);
		
	    mat_diffuse = new float[]{ 0f, .8f, .8f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    
		floor.Draw();
		
		//First floor
		gl.glPushMatrix();
		gl.glTranslatef(-.5f, 0, -.5f);
		gl.glRotatef(135, 0, 1f, 0);
		gl.glScalef(2f, 0, .25f);
		floor.Draw();
		gl.glPopMatrix(); //First Floor
		
		//First Cone
		gl.glPushMatrix();
		gl.glTranslatef(-1.25f,1f,-1.25f);
		gl.glScalef(.75f,2.5f,.75f);
		gl.glRotatef(180, 1, 0, 0);

	    mat_diffuse = new float[]{ .3f, 1f, .1f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
		
		cone.Draw();
		
		//First Cone Fan
		gl.glPushMatrix();
		gl.glRotatef(example_rotateT*15, 0, 1, 0);
		gl.glTranslatef(0,-.25f,0);
		ceiling_fan.Draw();
		gl.glPopMatrix(); //First Cone Fan
		
		gl.glPopMatrix(); //First Cone
		
		//Second Floor
		gl.glPushMatrix();
		gl.glTranslatef(.5f, 0, .5f);
		gl.glRotatef(135, 0, 1f, 0);
		gl.glScalef(2f, 0, .25f);

		
	    mat_ambient = new float[]{ 0, 0, 0, 1 };
	    mat_specular = new float[]{ .1f, .1f, 1f, 1 };
	    mat_diffuse = new float[]{ 0f, .8f, .8f, 1 };
	    mat_shininess = new float[]{ 128 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_AMBIENT, mat_ambient, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);
	    
		floor.Draw();
		gl.glPopMatrix(); //Second Floor
		
		//Second Cone
		gl.glPushMatrix();
		gl.glTranslatef(1.25f,1f,1.25f);
		gl.glScalef(1f,3f,1f);
		gl.glRotatef(180, 1, 0, 0);

	    mat_ambient = new float[]{ 0, 0, 0, 1 };
	    mat_specular = new float[]{ .8f, .8f, .8f, 1 };
	    mat_diffuse = new float[]{ .9f, 0f, .9f, 1 };
	    mat_shininess = new float[]{ 128 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_AMBIENT, mat_ambient, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);
		
		cone.Draw();
		
		//Second Cone Fan
		gl.glPushMatrix();
		gl.glRotatef(example_rotateT*15, 0, 1, 0);
		gl.glTranslatef(0,-.25f,0);
		ceiling_fan.Draw();
		gl.glPopMatrix(); //Second Cone Fan
		
		gl.glPopMatrix(); //Second Cone
		
		gl.glPopMatrix();
		
		gl.glTranslatef(0,-float_translate/2,0);

		

		
		//Camera
		gl.glPushMatrix();
		gl.glRotatef(example_rotateT, 0, 1, 0);
		gl.glTranslatef(1, 1f, 1);
		gl.glRotatef(135, 0, -1f, 0);
		gl.glRotatef(45, .75f, 0, 0);

		gl.glScalef(.25f,.25f,.25f);
		
	    mat_diffuse = new float[]{ 0f, 0f, 0f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    
		camera.Draw();
		
		
		//Male
		gl.glPushMatrix();
		gl.glTranslatef(0, -.75f, -1f);
		gl.glRotatef(45, -.25f, 0, 0);
		gl.glScalef(4f,4f,4f);
		
	    mat_diffuse = new float[]{ 0f, .2f, 1f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    
		male.Draw();
		

		
		//Ring
		gl.glPushMatrix();
		gl.glRotatef(example_rotateT*4, 0,1, 0);
		gl.glTranslatef(0,-.5f,0);
		
	    mat_diffuse = new float[]{ .2f, .8f, .1f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    
		ring.Draw();
		

		
		//Bottle
		gl.glPushMatrix();
		//gl.glRotatef(180, 1, 0, 0);
		gl.glScalef(.25f,.25f,.25f);
		gl.glTranslatef(1,1,0);
		
	    mat_diffuse = new float[]{ .8f, .1f, .8f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    
		bottle.Draw();
		gl.glPopMatrix(); //Bottle
		
		//Pitcher
		gl.glPushMatrix();
		gl.glTranslatef(.25f,.20f,.25f);
		gl.glScalef(.25f,.25f,.25f);
		
	    mat_diffuse = new float[]{ .8f, .4f, 0f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
		
		pitcher.Draw();
		gl.glPopMatrix(); //Pitcher
		
		gl.glPopMatrix(); // Ring
		
		gl.glPopMatrix(); //Male
		
		gl.glPopMatrix(); //Camera




		//Camera
		gl.glPushMatrix();

		gl.glRotatef(example_rotateT, 0, 1, 0);
		gl.glTranslatef(-1, .75f, -1);
		gl.glRotatef(45, 0, 1f, 0);
		gl.glRotatef(45, .75f, 0, 0);

		gl.glScalef(.25f,.25f,.25f);
		
	    mat_diffuse = new float[]{ 0f, 0f, 0f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    
		camera.Draw();
		
		//Male
		gl.glPushMatrix();
		gl.glTranslatef(0, -.75f, -1f);
		gl.glRotatef(45, -.25f, 0, 0);
		gl.glScalef(4f,4f,4f);

		
	    mat_diffuse = new float[]{ 0f, .2f, 1f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    
		male.Draw();
		
		//Ring
		gl.glPushMatrix();
		gl.glRotatef(example_rotateT*4, 0,-1, 0);
		gl.glTranslatef(0,-.5f,0);
		
	    mat_diffuse = new float[]{ .9f, 0, 0, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    
		ring.Draw();
		
		//Vase
		gl.glPushMatrix();
		//gl.glRotatef(180, 1, 0, 0);
		gl.glScalef(.25f,.20f,.25f);
		gl.glTranslatef(1,.75f,0);
		
	    mat_diffuse = new float[]{ .4f, .2f, .8f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    
		vase.Draw();
		
		//Tulip
		gl.glPushMatrix();
		gl.glTranslatef(0,1,0);
		
	    mat_diffuse = new float[]{ .2f, .8f, .4f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    
		tulip.Draw();
		gl.glPopMatrix();
		
		gl.glPopMatrix();
		
		gl.glPopMatrix(); // Ring
		
		gl.glPopMatrix(); //Male
		
		gl.glPopMatrix(); //Camera
		
		gl.glPopMatrix(); //female

		gl.glTranslatef(0,-.75f,0);
		gl.glRotatef(example_rotateT*15, 0,1,0);
		gl.glScalef(1.5f,.75f,1.5f);
		
	     mat_diffuse = new float[]{ .8f, .8f, 0f, 1 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    
		ceiling_fan.Draw();
		*/

		/*
		gl.glTranslatef(0, 2f, 0);
		gl.glRotatef(180, 1f, 0, 0);
		ceiling_fan.Draw();
		*/
		
		gl.glPopMatrix(); //ceiling fan
		
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
	
	public CopyOfHierarchical() {
		super("Synesthesia");
		canvas = new GLCanvas();
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		animator = new FPSAnimator(canvas, 60);	// create a 30 fps animator
		getContentPane().add(canvas);
		setSize(winW, winH);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		animator.start();
		canvas.requestFocus();
	}
	
	public static void main(String[] args) {
		
		new CopyOfHierarchical();
	}
	
	public void init(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2();

		initViewParameters();
		gl.glClearColor(.1f, .1f, .1f, 1f);
		gl.glClearDepth(1.0f);

	    // white light at the eye
	    float light0_position[] = { 0, 0, 1, 0 };
	    float light0_diffuse[] = { 1, 1, 1, 1 };
	    float light0_specular[] = { 1, 1, 1, 1 };
	    gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_POSITION, light0_position, 0);
	    gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light0_diffuse, 0);
	    gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_SPECULAR, light0_specular, 0);

	    //red light
	    float light1_position[] = { -.1f, .1f, 0, 0 };
	    float light1_diffuse[] = { .6f, .05f, .05f, 1 };
	    float light1_specular[] = { .6f, .05f, .05f, 1 };
	    gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_POSITION, light1_position, 0);
	    gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_DIFFUSE, light1_diffuse, 0);
	    gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_SPECULAR, light1_specular, 0);

	    //blue light
	    float light2_position[] = { .1f, .1f, 0, 0 };
	    float light2_diffuse[] = { .05f, .05f, .6f, 1 };
	    float light2_specular[] = { .05f, .05f, .6f, 1 };
	    gl.glLightfv( GL2.GL_LIGHT2, GL2.GL_POSITION, light2_position, 0);
	    gl.glLightfv( GL2.GL_LIGHT2, GL2.GL_DIFFUSE, light2_diffuse, 0);
	    gl.glLightfv( GL2.GL_LIGHT2, GL2.GL_SPECULAR, light2_specular, 0);

	    
	    float light3_position[] = { 1f, 3f, 1f, 0 };
	    float light3_diffuse[] = { 5f, 5f, 5f, 1 };
	    float light3_specular[] = { 5f, 5f, 5f, 1 };
	    gl.glLightfv( GL2.GL_LIGHT3, GL2.GL_POSITION, light3_position, 0);
	    gl.glLightfv( GL2.GL_LIGHT3, GL2.GL_DIFFUSE, light3_diffuse, 0);
	    gl.glLightfv( GL2.GL_LIGHT3, GL2.GL_SPECULAR, light3_specular, 0);
	    
	    //material
	    float mat_ambient[] = { 0, 0, 0, 1 };
	    float mat_specular[] = { .8f, .8f, .8f, 1 };
	    float mat_diffuse[] = { .4f, .4f, .4f, 1 };
	    float mat_shininess[] = { 128 };
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_AMBIENT, mat_ambient, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
	    gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);

	    float bmat_ambient[] = { 0, 0, 0, 1 };
	    float bmat_specular[] = { 0, .8f, .8f, 1 };
	    float bmat_diffuse[] = { 0, .4f, .4f, 1 };
	    float bmat_shininess[] = { 128 };
	    gl.glMaterialfv( GL2.GL_BACK, GL2.GL_AMBIENT, bmat_ambient, 0);
	    gl.glMaterialfv( GL2.GL_BACK, GL2.GL_SPECULAR, bmat_specular, 0);
	    gl.glMaterialfv( GL2.GL_BACK, GL2.GL_DIFFUSE, bmat_diffuse, 0);
	    gl.glMaterialfv( GL2.GL_BACK, GL2.GL_SHININESS, bmat_shininess, 0);

	    float lmodel_ambient[] = { 0, 0, 0, 1 };
	    gl.glLightModelfv( GL2.GL_LIGHT_MODEL_AMBIENT, lmodel_ambient, 0);
	    gl.glLightModeli( GL2.GL_LIGHT_MODEL_TWO_SIDE, 1 );

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
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		winW = width;
		winH = height;

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glLoadIdentity();
			glu.gluPerspective(45.f, (float)width/(float)height, znear, zfar);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}
	
	public void mousePressed(MouseEvent e) {	
		mouseX = e.getX();
		mouseY = e.getY();
		mouseButton = e.getButton();
		canvas.display();
	}
	
	public void mouseReleased(MouseEvent e) {
		mouseButton = MouseEvent.NOBUTTON;
		canvas.display();
	}	
	
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
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
	}
	
	
	/* computes optimal transformation parameters for OpenGL rendering.
	 * this is based on an estimate of the scene's bounding box
	 */	
	void initViewParameters()
	{
		roth = rotv = 0;

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
	public void keyReleased(KeyEvent e) { }
	public void mouseMoved(MouseEvent e) { }
	/*public void mouseMoved(MouseEvent e) { 
		int x = e.getX();
		int y = e.getY();
		if(mouseLock == false){
		//if(mouseButton == MouseEvent.NOBUTTON){
		roth -=  (x- mouseX) * rotateSpeed;
		rotv +=  (y-mouseY) * rotateSpeed;
		mouseX = x;
		mouseY = y;

		
		canvas.display();
		}
	}
	*/
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


