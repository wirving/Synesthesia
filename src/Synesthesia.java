import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

//import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.FPSAnimator;

class Synesthesia extends JFrame implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, ActionListener {

	class objModel {
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
		pressed.add(e.getKeyCode());
		if(pressed.size() >= 1){
			for(Integer key : pressed){
		switch(key) {
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
			   if(canMove){
			    yrotrad = (roth / 180 * 3.141592654f);
			    xrotrad = (rotv / 180 * 3.141592654f); 
			    xpos += (float)(Math.sin(yrotrad))/10 ;
			    zpos -= (float)(Math.cos(yrotrad))/10 ;
			   }
			break;
		case KeyEvent.VK_DOWN:
		case 'K':
			if(canMove){
			 yrotrad = (roth / 180 * 3.141592654f);
			 xrotrad = (rotv / 180 * 3.141592654f); 
			 xpos -= (float)(Math.sin(yrotrad))/10 ;
			 zpos += (float)(Math.cos(yrotrad))/10 ;
			}
			break;
			
		case KeyEvent.VK_LEFT:
		case 'J':
			if(canMove){
		    yrotrad = (roth / 180 * 3.141592654f);
		    xpos -= (float)(Math.cos(yrotrad)) * 0.1;
		    zpos -= (float)(Math.sin(yrotrad)) * 0.1;
			}

			break;
		
		case KeyEvent.VK_RIGHT:
		case 'L':
			if(canMove){
		    yrotrad = (roth / 180 * 3.141592654f);
		    xpos += (float)(Math.cos(yrotrad)) * 0.1;
		    zpos += (float)(Math.sin(yrotrad)) * 0.1;
			}
			break;
			
		case 'a':
		case 'A':
			roth -= 2f; //Smaller value will be smoother but slower
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
			}
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
	
	private Set<Integer> pressed = new ConcurrentSkipListSet<Integer>();
	
	
	
	
	/* === YOUR WORK HERE === */
	/* Define more models you need for constructing your scene */

	private objModel cube = new objModel("floorobj.obj");

	
	private LevelOne levelOne = new LevelOne();
	//private LevelTwo levelTwo = new LevelTwo();
	private Temple temple = new Temple();
	
	//For temple set light0 position to {0, 5, 1, 0}
	private ArrayList<BasicObject> objects = temple.getStaticEntities();
	//private ArrayList<BasicObject> objects = levelTwo.getStaticEntities();
	
	private boolean canMove = true;
	HashMap<String, objModel> objectMap = new HashMap<String, objModel>();
	
	
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
		objectMap.put("statue", new objModel("female.obj"));
		objectMap.put("plant", new objModel("plant.obj"));
		
	}
	
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
		
		//Background Cube
		/*
		gl.glPushMatrix();
		gl.glScalef(40,40,40);
	    gl.glMaterialfv( GL2.GL_BACK, GL2.GL_DIFFUSE, new float[]{.5f,.5f,.5f,1f}, 0);
		cube.Draw();
		gl.glPopMatrix();
		*/
		
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
		    
		    objectMap.get(currentObject.getObject()).Draw();
			//cube.Draw();
			gl.glPopMatrix();
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
		setSize(winW, winH);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		animator.start();
		canvas.requestFocus();
	}
	
	public static void main(String[] args) {
		
		new Synesthesia();
	}
	
	public void init(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2();

		initViewParameters();
		
		makeObjectMap();
		
		gl.glClearColor(.1f, .1f, .1f, 1f);
		gl.glClearDepth(1.0f);

		
		//Eventually set lights based on scene description
	    float light0_position[] = { 0, 5, 1, 0 };
	    float light0_diffuse[] = { 1, 1, 1, 1 };
	    float light0_specular[] = { 1, 1, 1, 1 };
	    gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_POSITION, light0_position, 0);
	    gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light0_diffuse, 0);
	    gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_SPECULAR, light0_specular, 0);

	    float light1_position[] = { -1, 0, 0, 0 };
	    float light1_diffuse[] = { 1,1,1, 1 };
	    float light1_specular[] = { 1,1,1, 1 };
	    gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_POSITION, light1_position, 0);
	    gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_DIFFUSE, light1_diffuse, 0);
	    gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_SPECULAR, light1_specular, 0);

	    float light2_position[] = { 1, 0, 0, 0 };
	    float light2_diffuse[] ={ 1,1,1, 1 };
	    float light2_specular[] = { 1,1,1, 1 };
	    gl.glLightfv( GL2.GL_LIGHT2, GL2.GL_POSITION, light2_position, 0);
	    gl.glLightfv( GL2.GL_LIGHT2, GL2.GL_DIFFUSE, light2_diffuse, 0);
	    gl.glLightfv( GL2.GL_LIGHT2, GL2.GL_SPECULAR, light2_specular, 0);

	    
	    float light3_position[] = { 0, 0, -1f, 0 };
	    float light3_diffuse[] = { 1,1,1, 1 };
	    float light3_specular[] = { 1,1,1,1 };
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
		if(pressed.size() >= 1){
			e.setKeyCode(pressed.iterator().next());
			keyPressed(e);
		}
	}
	//public void mouseMoved(MouseEvent e) { }
	public void mouseMoved(MouseEvent e) { 
		int x = e.getX();
		int y = e.getY();
		//if(mouseLock == false){
		//if(mouseButton == MouseEvent.NOBUTTON){
		roth +=  (x- mouseX) * rotateSpeed;
		rotv +=  (y-mouseY) * rotateSpeed;
		mouseX = x;
		mouseY = y;

		
		canvas.display();
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


