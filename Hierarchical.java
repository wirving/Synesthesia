import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

//import com.sun.opengl.util.BufferUtil;

//import com.jogamp.opengl.swt.*;
//import com.jogamp.opengl.swt.GLCanvas;
import javax.media.opengl.awt.GLCanvas;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.PMVMatrix;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.*;

import java.util.ArrayList;

class Hierarchical extends JFrame implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, ActionListener {

	/* This defines the objModel class, which takes care
	 * of loading a triangular mesh from an obj file,
	 * estimating per vertex average normal,
	 * and displaying the mesh.
	 */
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
			
			float[] zPlane = {0.0f, 0.0f, 1.0f, 0.0f};
			float[] xPlane = {1.0f, 0.0f, 0.0f, 0.0f};
			
			cat_tex.enable(gl);
			cat_tex.bind(gl);
			
			gl.glEnable(GL2.GL_TEXTURE_GEN_S);
			gl.glEnable(GL2.GL_TEXTURE_GEN_T);
			gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_OBJECT_LINEAR);
			gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_OBJECT_LINEAR);
			gl.glTexGenfv(GL2.GL_S, GL2.GL_OBJECT_PLANE, xPlane, 0);
			gl.glTexGenfv(GL2.GL_T, GL2.GL_OBJECT_PLANE, zPlane, 0);
			
			
			gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertexBuffer);
			gl.glNormalPointer(GL.GL_FLOAT, 0, normalBuffer);
			
			gl.glDrawElements(GL.GL_TRIANGLES, num_faces*3, GL.GL_UNSIGNED_INT, faceBuffer);
			
			gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
			gl.glDisable(GL2.GL_TEXTURE_GEN_S);
			gl.glDisable(GL2.GL_TEXTURE_GEN_T);
			cat_tex.disable(gl);
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
		case 'w':
		case 'W':
			wireframe = ! wireframe;
			break;
		case 'b':
		case 'B':
			cullface = !cullface;
			break;
		case 'f':
		case 'F':
			flatshade = !flatshade;
			break;
		case 'a':
		case 'A':
			if (animator.isAnimating())
				animator.stop();
			else 
				animator.start();
			break;
		case '+':
		case '=':
			animation_speed *= 1.2f;
			break;
		case '-':
		case '_':
			animation_speed /= 1.2;
			break;
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
	private boolean cullface = true;
	private boolean flatshade = false;
	
	private float xpos = 0, ypos = 0, zpos = 0;
	private float centerx, centery, centerz;
	private float roth = 0, rotv = 0;
	private float znear, zfar;
	private int mouseX, mouseY, mouseButton;
	private float motionSpeed, rotateSpeed;
	private float animation_speed = 0.5f;
	
	// ----------------------
	Texture cat_tex;
	
	
	/* === YOUR WORK HERE === */
	/* Define more models you need for constructing your scene */
	//private objModel head = new objModel("head.obj");
	private objModel box1 = new objModel("box.obj");
	private objModel bunny = new objModel("./a3_objmodels/bunny2.obj");
	
	double counter = 0.0; //For the head
	
	private objModel tail1 = new objModel("small_sphere.obj");
	
	private objModel wing1 = new objModel("paddle.obj");
	private objModel decoration = new objModel("./a3_objmodels/wind_chimes.obj");
	private objModel plane = new objModel("plane.obj");
	
	//For shadow mapping, not working yet
	//private objModel floor = new objModel("./a3_objmodels/plane.obj");
	
	
	float wings_color[] = { 0.8f, 1.0f, 1.0f, 1 };
	float wing_shine[] = {20}; 
	float wing_specular[] = { .2f, 0.2f, .2f, 1 };
	
	float decoration_color[] = {1.0f, 0.5f, 0.5f};
	
	// ): 
	private objModel wings[] = new objModel[]{wing1, wing1, wing1, wing1, wing1, wing1, wing1, wing1, wing1, wing1, wing1, wing1, wing1, wing1, wing1, wing1};
	private float wingRotations[] = new float[]{-90.0f, -80.0f, -70.0f, -60.0f, -50.0f, -40.0f, -30.0f, -20.0f, -90.0f, -80.0f, -70.0f, -60.0f, -50.0f, -40.0f, -30.0f, -20.0f};
	private boolean rotateWingCW[] = new boolean[]{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};
	
	//private objModel[] body = new objModel[]{box1, box1, box1, box1, box1, box1, tail1, tail1, tail1, tail1};
	
	//float[] transforms = new float[body.length/2 + 1];
	
	public int increment = 0;
	private float head_translate = 0.0f;
	private float box_translate = 0.0f;
	/* Here you should give a conservative estimate of the scene's bounding box
	 * so that the initViewParameters function can calculate proper
	 * transformation parameters to display the initial scene.
	 * If these are not set correctly, the objects may disappear on start.
	 */
	private float xmin = -5f, ymin = -5f, zmin = -5f;
	private float xmax = 5f, ymax = 5f, zmax = 5f;	
	
	
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, wireframe ? GL2.GL_LINE : GL2.GL_FILL);	
		gl.glShadeModel(flatshade ? GL2.GL_FLAT : GL2.GL_SMOOTH);		
		if (cullface)
			gl.glEnable(GL.GL_CULL_FACE);
		else
			gl.glDisable(GL.GL_CULL_FACE);		
		
		gl.glLoadIdentity();
		
		/* this is the transformation of the entire scene */
		gl.glTranslatef(-xpos, -ypos, -zpos);
		gl.glTranslatef(centerx, centery, centerz);
		gl.glRotatef(360.f - roth, 0, 1.0f, 0);
		gl.glRotatef(rotv, 1.0f, 0, 0);
		gl.glTranslatef(-centerx, -centery, -centerz);	

		
		/* === YOUR WORK HERE === */
		
		float body_color[] = { .4f, 0.6f, .8f, 1 }; gl.glMaterialfv( GL.GL_FRONT, GL2.GL_DIFFUSE, body_color, 0);
		float body_shine[] = {50};   gl.glMaterialfv( GL.GL_FRONT, GL2.GL_SHININESS, body_shine, 0);
		float body_specular[] = { .4f, 0.6f, .6f, 1 }; gl.glMaterialfv( GL.GL_FRONT, GL2.GL_SPECULAR, body_specular, 0);
		
		
		gl.glPushMatrix();	//Default
		//Rotate head
		
		/*
		gl.glRotatef(-90, 0, 1, 0);
		//make it larger
		gl.glScalef(1.25f, 1.25f, 1.25f);
		
		gl.glTranslatef(0.0f, (float)head_translate, 0.0f); //Move up and down
		head.Draw(); 
		
		//Feathers on head
		gl.glPushMatrix(); //Head
		//Same color as wings
		gl.glMaterialfv( GL.GL_FRONT, GL2.GL_DIFFUSE, wings_color, 0);
	    gl.glMaterialfv( GL.GL_FRONT, GL2.GL_SHININESS, wing_shine, 0);
	    gl.glMaterialfv( GL.GL_FRONT, GL2.GL_SPECULAR, wing_specular, 0);
	    
	    //Each feather's position is based on the previous one
		gl.glRotatef(90, 0, 1, 0);
		
		gl.glTranslatef((float).75, (float).1, (float).4);
		gl.glRotatef(90, 1, 0, 0);
		gl.glRotatef(30, 0, 0, 1);
		gl.glRotatef(head_translate * 5, 0, 1, 0);
		wing1.Draw();
		
		gl.glTranslatef(0, 0, (float).1);
		gl.glRotatef(head_translate * 5, 0, 1, 0);
		
		wing1.Draw();
		gl.glTranslatef(0, 0, (float).1);
		gl.glRotatef(head_translate, 0, 1, 0);
		
		wing1.Draw();
		
		//Decoration
		gl.glPopMatrix(); //Get Head
		gl.glPushMatrix(); //Push Head
		
		gl.glTranslatef(.3f, -.5f, -.5f);
		gl.glRotatef(head_translate * 10, 1, 0, 0);
		gl.glMaterialfv( GL.GL_FRONT, GL2.GL_DIFFUSE, decoration_color, 0);
		decoration.Draw();
	
		gl.glPopMatrix(); //get head
		gl.glRotatef(180, 0, 1, 0);
		
		//
		//
		//Other side, symmetrical (hopefully)
		gl.glTranslatef(.4f, .1f, .75f);
		gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(60, 0, 0, 1);
		gl.glRotatef(head_translate * 5, 0, 1, 0);
		gl.glMaterialfv( GL.GL_FRONT, GL2.GL_DIFFUSE, wings_color, 0);
		wing1.Draw();
		
		gl.glTranslatef(0.0f, 0.0f, .1f);
		gl.glRotatef(head_translate * 5, 0, 1, 0);
		
		wing1.Draw();
		gl.glTranslatef(0, 0, .1f);
		gl.glRotatef(head_translate, 0, 1, 0);
		
		wing1.Draw();
		
		//Decoration other side
		gl.glRotatef(-90, 1, 0, 0);
		gl.glTranslatef(-.2f, -.4f, 0);
		gl.glRotatef(-head_translate, 1, 0, 0);
		gl.glMaterialfv( GL.GL_FRONT, GL2.GL_DIFFUSE, decoration_color, 0);
		decoration.Draw();
		
		
		gl.glPopMatrix(); //Default
		
		 gl.glMaterialfv( GL.GL_FRONT, GL2.GL_DIFFUSE, body_color, 0);
		 gl.glMaterialfv( GL.GL_FRONT, GL2.GL_SHININESS, body_shine, 0);
		 gl.glMaterialfv( GL.GL_FRONT, GL2.GL_SPECULAR, body_specular, 0);
		 
		//There was probably a better way to do this
		//first half of body
		for (int i = 0; i <= body.length/2; i++ ){
			transforms[i] = (float)(1/(Math.pow(2,i)));
			gl.glTranslatef(1, (float)transforms[i] *(box_translate), 0);
			objModel whichBox = body[i];
			whichBox.Draw();
		}
		
		//Wings
		gl.glPushMatrix(); //Box 3

		
		gl.glRotatef(-270, 0, 1, 0);
		gl.glTranslatef(0, 0, -1);
		gl.glScalef(4, 4, 4);
		gl.glMaterialfv( GL.GL_FRONT, GL2.GL_DIFFUSE, wings_color, 0);
	    gl.glMaterialfv( GL.GL_FRONT, GL2.GL_SHININESS, wing_shine, 0);
	    gl.glMaterialfv( GL.GL_FRONT, GL2.GL_SPECULAR, wing_specular, 0);
	    
		for (int i = 0; i < wings.length/2; i++){
		gl.glPushMatrix();
		gl.glRotatef(wingRotations[i], 0, 0, 1);
		gl.glTranslatef(.5f, 0, -(float).1 * i);
		wings[i].Draw();
		gl.glPopMatrix();
			
		}
		
		//Other side wings
		gl.glRotatef(-180, 0, 1, 0);
		gl.glTranslatef(0, 0, -.8f); //I don't know why but when using -1 (symmetrical to above) the wings are off. 
		
		for (int i = wings.length/2; i < wings.length; i++){
		gl.glPushMatrix();
		gl.glRotatef(wingRotations[i], 0, 0, 1);
		gl.glTranslatef(.5f, 0, (float).1f * i);
		wings[i].Draw();
		gl.glPopMatrix();
			
		}
		
		
		gl.glPopMatrix(); //Box 3
		
		//second half
		for (int i = (body.length/2)+1; i < body.length; i++){
			
			gl.glTranslatef(1.0f,  - transforms[body.length-i] *(box_translate), 0.0f);
			objModel whichBox = body[i];
			
			//It's a sphere! Tail
			if (whichBox.num_verts == 482){
			
				float tail_color[] = { 0.8f, 0.8f, 0.2f, 1 };
			    gl.glMaterialfv( GL.GL_FRONT, GL2.GL_DIFFUSE, tail_color, 0);
			    //Shiny!
			    float tail_shine[] = {128};   gl.glMaterialfv( GL.GL_FRONT, GL2.GL_SHININESS, tail_shine, 0);
			    float tail_specular[] = { 1.0f, 1.0f, 1.0f, 1 }; gl.glMaterialfv( GL.GL_FRONT, GL2.GL_SPECULAR, tail_specular, 0);
			gl.glScalef(.75f, .75f, .75f);
			whichBox.Draw();
			
			}
			else{
				whichBox.Draw();
			}
			
		}
		
		
		*/
		
		box1.Draw();
		
		gl.glTranslatef(1f, 0f, 1f);
		bunny.Draw();
		
		gl.glTranslatef(0.0f, -.5f, 0f);
		gl.glScalef(10f, 0f, 10f);
		plane.Draw();

		//floor.Draw();
		/* increment example_rotateT */
		if (animator.isAnimating()){
		/*	box_translate = (float)1.5 * head_translate;
			head_translate = (float)Math.sin(counter);

			//Rotate from -90 to 90, then change direction
			for(int i = 0; i < wingRotations.length; i++){
				
				if (wingRotations[i] >= 80.0)
					rotateWingCW[i] = false;
				if (wingRotations[i] <= -80.0)
					rotateWingCW[i] = true;
				
				if(rotateWingCW[i]){
				wingRotations[i] = wingRotations[i] + 10*animation_speed; 
				}
				else{
				wingRotations[i] = wingRotations[i] - 10*animation_speed;
				}
				
			}
			
			//It was too fast so I had to slow it down
			counter+= animation_speed/6;
		*/	
		}	
	}	
	
	public Hierarchical() {
		super("Assignment 3 -- Hierarchical Modeling"); 
		canvas = new GLCanvas();
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		animator = new FPSAnimator(canvas, 30);	// create a 30 fps animator
		getContentPane().add(canvas);
		setSize(winW, winH);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		animator.start();
		canvas.requestFocus();
		
		
	}
	
	public static void main(String[] args) {

		new Hierarchical();
	}
	
	public void init(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2();

		System.out.println(gl.isExtensionAvailable("GL_ARB_depth_texture"));
		System.out.println(gl.isExtensionAvailable("GL_ARB_shadow"));
		
		initViewParameters();
		gl.glClearColor(0f, 0f, 0f, 0f);
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

	    //material
	    float mat_ambient[] = { 0, 0, 0, 1 };
	   // float mat_specular[] = { .8f, .8f, .8f, 1 };
	   // float mat_diffuse[] = { .4f, .8f, .6f, 1 };
	   // float mat_shininess[] = { 128 };
	    
	    gl.glMaterialfv( GL.GL_FRONT, GL2.GL_AMBIENT, mat_ambient, 0);
	    //gl.glMaterialfv( GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
	  //  gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
	    //gl.glMaterialfv( GL.GL_FRONT, GL.GL_SHININESS, mat_shininess, 0);

	    float bmat_ambient[] = { 1, 1, 1, 1 };
	    float bmat_specular[] = { 0, .8f, .8f, 1 };
	    float bmat_diffuse[] = { 0, .4f, .4f, 1 };
	    float bmat_shininess[] = { 128 };
	    gl.glMaterialfv( GL.GL_BACK, GL2.GL_AMBIENT, bmat_ambient, 0);
	    gl.glMaterialfv( GL.GL_BACK, GL2.GL_SPECULAR, bmat_specular, 0);
	    gl.glMaterialfv( GL.GL_BACK, GL2.GL_DIFFUSE, bmat_diffuse, 0);
	    gl.glMaterialfv( GL.GL_BACK, GL2.GL_SHININESS, bmat_shininess, 0);

	    float lmodel_ambient[] = { 0, 0, 0, 1 };
	    gl.glLightModelfv( GL2.GL_LIGHT_MODEL_AMBIENT, lmodel_ambient, 0);
	    gl.glLightModeli( GL2.GL_LIGHT_MODEL_TWO_SIDE, 1 );

	    gl.glEnable( GL2.GL_NORMALIZE );
	    gl.glEnable( GL2.GL_LIGHTING );
	    gl.glEnable( GL2.GL_LIGHT0 );
	    gl.glEnable( GL2.GL_LIGHT1 );
	    gl.glEnable( GL2.GL_LIGHT2 );

	    gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		gl.glCullFace(GL.GL_BACK);
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glShadeModel(GL2.GL_SMOOTH);		
		
		cat_tex = null;
		try{
			cat_tex = TextureIO.newTexture(new File("bakeneko.png"), false);
			
		} catch (IOException e){
			System.out.println("Could not open the file!");
		}
		
		if (cat_tex != null){
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
			
			//Wrapping
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		}
		
		/*
		Texture shadow_map = TextureIO.newTexture(0);
		shadow_map.bind(gl);
		//Create the shadow map texture
		gl.glTexImage2D( GL2.GL_TEXTURE_2D, 0, GL2.GL_DEPTH_COMPONENT, winW, winH, 0, GL2.GL_DEPTH_COMPONENT, GL2.GL_UNSIGNED_BYTE, null);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
	    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
	    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
	    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
		
		
	    */
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
	public void actionPerformed(ActionEvent e) { }
	public void mouseClicked(MouseEvent e) { }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) {	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}	
}
