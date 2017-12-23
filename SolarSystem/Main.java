package SolarSystem;

import PentagonalPrism.PentaPrism;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import graphicslib3D.*;

import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.*;
import javax.swing.*;
import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.*;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GLContext;
import sphere.codedversion.Sphere;

public class Main extends JFrame implements GLEventListener, java.awt.event.KeyListener
{	private GLCanvas myCanvas;
    private int rendering_program;
    private int vao[] = new int[1];
    private int vbo[] = new int[8];
    private float cameraX, cameraY, cameraZ;
    private float pentaLocX, pentaLocY, pentaLocZ;
    private float sphLocX, sphLocY, sphLocZ;
    private float sunLocX, sunLocY, sunLocZ;
    private float earthLocX, earthLocY, earthLocZ;
    private GLSLUtils util = new GLSLUtils();

    private	MatrixStack mvStack = new MatrixStack(20);

    private Sphere mySphere = new Sphere(24);
    private PentaPrism myPentaPrism = new PentaPrism(1.0f);

    private int sunTexture;
    private Texture joglSunTexture;
    private int venusTexture;
    private Texture joglVenusTexture;
    private int marsTexture;
    private Texture joglMarsTexture;
    private int earthTexture;
    private Texture joglEarthTexture;
    private int moonTexture;
    private Texture joglMoonTexture;
    private int myFace;
    private Texture myFaceTexture;
    private Texture joglRedTexture;
    private int redTexture;
    private Texture joglGreenTexture;
    private int greenTexture;
    private Texture joglBlueTexture;
    private int blueTexture;

    private boolean worldAxis; //space bar to toggle world axis on and off
    private boolean zoomIn; //w - zoom in with camera
    private boolean zoomOut; //s - zoom out with camera
    private boolean strafeLeft; //a – move the camera a small amount in the negative-U direction (also called “strafe left”).
    private boolean strafeRight; //d – move the camera a small amount in the positive-U direction (also called “strafe right”).
    private boolean moveDown;//e – move the camera a small amount in the negative-V direction (“move down”).
    private boolean moveUp;//q – move the camera a small amount in the positive-V direction (“move up”).
    private boolean panLeft;//(left and right arrow) – rotate the camera by a small amount left/right around its V axis (“pan”).
    private boolean panRight;
    private boolean pitchUp;//(up and down arrow) – rotate the camera by a small amount up/down around its U axis (“pitch).
    private boolean pitchDown;

    Vector3D cameraUp;
    Vector3D cameraRight;
    Vector3D cameraFwd;

    Vector3D eye;
    Vector3D target;
    Vector3D up;

    float yaw;
    float pitch;

    public Main()
    {	setTitle("Solar System");
        setSize(2000, 1500);
        myCanvas = new GLCanvas();
        myCanvas.addGLEventListener(this);
        getContentPane().add(myCanvas);
        this.setVisible(true);
        FPSAnimator animator = new FPSAnimator(myCanvas, 50);
        animator.start();

        setFocusable(true);
        requestFocus();
        addKeyListener(this);

        worldAxis = true;
    }

    public void handleInput() {
        double cameraSpeed = 0.4f;
        if (zoomIn) {
            eye = eye.add(cameraFwd.mult(cameraSpeed));
        }
        if(zoomOut) {
            eye = eye.add(cameraFwd.mult(-cameraSpeed));
        }
        if (strafeLeft) {
            eye = eye.add(cameraRight.mult(-cameraSpeed));
        }
        if (strafeRight) {
            eye = eye.add(cameraRight.mult(cameraSpeed));
        }
        if (moveUp) {
            eye = eye.add(cameraUp.mult(cameraSpeed));
        }
        if (moveDown) {
            eye = eye.add(cameraUp.mult(-cameraSpeed));
        }
        if (panLeft) {
            yaw -= 1.0f;
        }
        if(panRight) {
            yaw += 1.0f;
        }
        if(pitchUp) {
            pitch += 1.0f;
            if (pitch > 90.0f)
            {
                pitch = 90.0f;
            }
        }
        if(pitchDown) {
            pitch -= 1.0f;
            if (pitch < -90.0f)
            {
                pitch = -90.0f;
            }
        }
    }

    public void display(GLAutoDrawable drawable)
    {	GL4 gl = (GL4) GLContext.getCurrentGL();

        handleInput();

        gl.glClear(GL_DEPTH_BUFFER_BIT);
        float bkg[] = { 0.0f, 0.0f, 0.0f, 1.0f };
        FloatBuffer bkgBuffer = Buffers.newDirectFloatBuffer(bkg);
        gl.glClearBufferfv(GL_COLOR, 0, bkgBuffer);

        gl.glClear(GL_DEPTH_BUFFER_BIT);

        gl.glUseProgram(rendering_program);

        int mv_loc = gl.glGetUniformLocation(rendering_program, "mv_matrix");
        int proj_loc = gl.glGetUniformLocation(rendering_program, "proj_matrix");

        float aspect = (float) myCanvas.getWidth() / (float) myCanvas.getHeight();
        Matrix3D pMat = perspective(60.0f, aspect, 0.1f, 1000.0f);

        cameraFwd = new Vector3D(
                (Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw))),
                (Math.sin(Math.toRadians(pitch))),
                (Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw)))
                );

        Matrix3D lookAt = lookAt(eye, target, up);

        // push view matrix onto the stack
        mvStack.pushMatrix();
        mvStack.multMatrix(lookAt);
        mvStack.translate(-cameraX * 1.5, -cameraY * 1.5, -cameraZ * 1.5);
        double amt = (double)(System.currentTimeMillis())/1000.0;

        gl.glUniformMatrix4fv(proj_loc, 1, false, pMat.getFloatValues(), 0);

        int numVerts = mySphere.getIndices().length;

        // ----------------------  sphere == sun
        mvStack.pushMatrix();
        mvStack.translate(pentaLocX, pentaLocY, pentaLocZ);
        mvStack.pushMatrix();
        mvStack.rotate((System.currentTimeMillis())/20.0,0.5,1.0,0.0);
//        mvStack.scale(2.0, 2.0, 2.0);
        gl.glUniformMatrix4fv(mv_loc, 1, false, mvStack.peek().getFloatValues(), 0);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[2]);
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1);
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, myFace);
//        gl.glEnable(GL_CULL_FACE);
//        gl.glFrontFace(GL_CCW);
        gl.glDrawArrays(GL_TRIANGLES, 0, 48);
        mvStack.popMatrix();
        mvStack.popMatrix();

        // ----------------------  lines == world axis
        if (worldAxis) {
            //x-axis
            mvStack.pushMatrix();
            gl.glUniformMatrix4fv(mv_loc, 1, false, mvStack.peek().getFloatValues(), 0);
            gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
            gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            gl.glEnableVertexAttribArray(0);
            gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
            gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
            gl.glEnableVertexAttribArray(1);
            gl.glActiveTexture(GL_TEXTURE0);
            gl.glBindTexture(GL_TEXTURE_2D, redTexture);
            gl.glDrawArrays(GL_LINES, 0, 2);
            gl.glDrawArrays(GL_LINES, 0, 2);

            //y-axis
            gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[6]);
            gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            gl.glEnableVertexAttribArray(0);
            gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[6]);
            gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
            gl.glEnableVertexAttribArray(1);
            gl.glActiveTexture(GL_TEXTURE0);
            gl.glBindTexture(GL_TEXTURE_2D, greenTexture);
            gl.glDrawArrays(GL_LINES, 0, 2);
            gl.glDrawArrays(GL_LINES, 0, 2);

            //z-axis
            gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[7]);
            gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            gl.glEnableVertexAttribArray(0);
            gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[7]);
            gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
            gl.glEnableVertexAttribArray(1);
            gl.glActiveTexture(GL_TEXTURE0);
            gl.glBindTexture(GL_TEXTURE_2D, blueTexture);
            gl.glDrawArrays(GL_LINES, 0, 2);
            gl.glDrawArrays(GL_LINES, 0, 2);
            mvStack.popMatrix();
        }

        //-----------------------  sphere == planet Earth
        mvStack.pushMatrix();
        mvStack.translate(Math.sin(amt)*4.0f, 0.0f, Math.cos(amt)*4.0f);
        mvStack.pushMatrix();
        mvStack.rotate((System.currentTimeMillis())/10.0,0.0,1.0,0.0);
        gl.glUniformMatrix4fv(mv_loc, 1, false, mvStack.peek().getFloatValues(), 0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[3]);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[4]);
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1);
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, earthTexture);
//        gl.glEnable(GL_CULL_FACE);
        gl.glFrontFace(GL_CCW);
        gl.glDrawArrays(GL_TRIANGLES, 0, numVerts);
        mvStack.popMatrix();

        //-----------------------  smaller sphere == moon of Earth
        mvStack.pushMatrix();
        mvStack.translate(0.0f, Math.sin(amt)*1.4f, Math.cos(amt)*1.4f);
        mvStack.rotate((System.currentTimeMillis())/10.0,0.0,0.0,1.0);
        mvStack.scale(0.25, 0.25, 0.25);
        gl.glUniformMatrix4fv(mv_loc, 1, false, mvStack.peek().getFloatValues(), 0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[3]);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[4]);
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1);
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, moonTexture);
//        gl.glEnable(GL_CULL_FACE);
        gl.glFrontFace(GL_CCW);
        gl.glDrawArrays(GL_TRIANGLES, 0, numVerts);
        mvStack.popMatrix();
        mvStack.popMatrix();

        //-----------------------  sphere == planet Mars
        mvStack.pushMatrix();
        mvStack.translate(-Math.sin(amt/2.)*9.0f, 0.0f, Math.cos(amt/2.)*6.6f);
        mvStack.pushMatrix();
        mvStack.rotate((System.currentTimeMillis())/30.0,0.0,0.4,0.0);
        mvStack.scale(0.75, 0.75, 0.75);
        gl.glUniformMatrix4fv(mv_loc, 1, false, mvStack.peek().getFloatValues(), 0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[3]);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[4]);
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1);
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, marsTexture);
//        gl.glEnable(GL_CULL_FACE);
        gl.glFrontFace(GL_CCW);
        gl.glDrawArrays(GL_TRIANGLES, 0, numVerts);
        mvStack.popMatrix();

        //-----------------------  smaller sphere == moon of Mars
        mvStack.pushMatrix();
        mvStack.translate(-Math.sin(amt/2), 0.0f, Math.cos(amt/2));
        mvStack.rotate((System.currentTimeMillis())/10.0,1.0,0.0,1.0);
        mvStack.scale(0.15, 0.15, 0.15);
        gl.glUniformMatrix4fv(mv_loc, 1, false, mvStack.peek().getFloatValues(), 0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[3]);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[4]);
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1);
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, moonTexture);
//        gl.glEnable(GL_CULL_FACE);
        gl.glFrontFace(GL_CCW);
        gl.glDrawArrays(GL_TRIANGLES, 0, numVerts);
        mvStack.popMatrix();
        mvStack.popMatrix();

        //-----------------------  sphere == planet Venus
        mvStack.pushMatrix();
        mvStack.translate(Math.sin(amt/3.)*12.0f, 0.0f, Math.cos(amt/3.)*9.6f);
        mvStack.pushMatrix();
        mvStack.rotate((System.currentTimeMillis())/10.0,0.0,0.5,0.0);
        mvStack.scale(1.25, 1.25, 1.25);
        gl.glUniformMatrix4fv(mv_loc, 1, false, mvStack.peek().getFloatValues(), 0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[3]);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[4]);
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1);
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, venusTexture);
//        gl.glEnable(GL_CULL_FACE);
        gl.glFrontFace(GL_CCW);
        gl.glDrawArrays(GL_TRIANGLES, 0, numVerts);
        mvStack.popMatrix();
        mvStack.popMatrix();
        mvStack.popMatrix();
    }

    public void init(GLAutoDrawable drawable)
    {	GL4 gl = (GL4) GLContext.getCurrentGL();
        rendering_program = createShaderProgram();
        setupVertices();
        cameraX = 0.0f; cameraY = 0.0f; cameraZ = 9.0f;
        pentaLocX = 0.0f; pentaLocY = 0.0f; pentaLocZ = 0.0f;
        sunLocX = 0.0f; sunLocY = 0.0f; sunLocZ = 0.0f;

        eye = new Vector3D(cameraX, cameraY, cameraZ);
        target = new Vector3D(0.0f, 0.0f, 0.0f);
        up = new Vector3D(0.0f, 1.0f, 0.0f);

        pitch = 0.0f;
        yaw = -90.0f;

        joglEarthTexture = loadTexture("src/Resources/earth.jpg");
        earthTexture = joglEarthTexture.getTextureObject();
        joglSunTexture = loadTexture("src/Resources/sun.jpg");
        sunTexture = joglSunTexture.getTextureObject();
        joglMarsTexture = loadTexture("src/Resources/mars.jpg");
        marsTexture = joglMarsTexture.getTextureObject();
        joglMoonTexture = loadTexture("src/Resources/moon1.jpg");
        moonTexture = joglMoonTexture.getTextureObject();
        joglVenusTexture = loadTexture("src/Resources/venus.jpg");
        venusTexture = joglVenusTexture.getTextureObject();
        joglRedTexture = loadTexture("src/Resources/red.png");
        redTexture = joglRedTexture.getTextureObject();
        joglGreenTexture = loadTexture("src/Resources/green.png");
        greenTexture = joglGreenTexture.getTextureObject();
        joglBlueTexture = loadTexture("src/Resources/blue.png");
        blueTexture = joglBlueTexture.getTextureObject();
        myFaceTexture = loadTexture("src/Resources/myFace.jpg");
        myFace = myFaceTexture.getTextureObject();
    }

    private void setupVertices()
    {	GL4 gl = (GL4) GLContext.getCurrentGL();

        Vertex3D[] vertices = mySphere.getVertices();
        int[] indices = mySphere.getIndices();

        float[] pvalues = new float[indices.length*3];
        float[] tvalues = new float[indices.length*2];
        float[] nvalues = new float[indices.length*3];

        for (int i=0; i<indices.length; i++)
        {	pvalues[i*3] = (float) (vertices[indices[i]]).getX();
            pvalues[i*3+1] = (float) (vertices[indices[i]]).getY();
            pvalues[i*3+2] = (float) (vertices[indices[i]]).getZ();
            tvalues[i*2] = (float) (vertices[indices[i]]).getS();
            tvalues[i*2+1] = (float) (vertices[indices[i]]).getT();
            nvalues[i*3] = (float) (vertices[indices[i]]).getNormalX();
            nvalues[i*3+1]= (float)(vertices[indices[i]]).getNormalY();
            nvalues[i*3+2]=(float) (vertices[indices[i]]).getNormalZ();
        }

        float[] x_axis = {
                0.0f, 0.0f, 0.0f, 200.0f, 0.0f, 0.0f,
        };
        float[] y_axis = {
                0.0f, 0.0f, 0.0f, 0.0f, 200.0f, 0.0f,
        };
        float[] z_axis = {
                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -200.0f
        };

        float[] penta_prism = myPentaPrism.getPositions();
        float[] penta_tc = myPentaPrism.getTextureCoords();

        gl.glGenVertexArrays(vao.length, vao, 0);
        gl.glBindVertexArray(vao[0]);
        gl.glGenBuffers(vbo.length, vbo, 0);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
        FloatBuffer xAxisBuf = Buffers.newDirectFloatBuffer(x_axis);
        gl.glBufferData(GL_ARRAY_BUFFER, xAxisBuf.limit()*4, xAxisBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[6]);
        FloatBuffer yAxisBuf = Buffers.newDirectFloatBuffer(y_axis);
        gl.glBufferData(GL_ARRAY_BUFFER, yAxisBuf.limit()*4, yAxisBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[7]);
        FloatBuffer zAxisBuf = Buffers.newDirectFloatBuffer(z_axis);
        gl.glBufferData(GL_ARRAY_BUFFER, zAxisBuf.limit()*4, zAxisBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
        FloatBuffer pentaBuf = Buffers.newDirectFloatBuffer(penta_prism);
        gl.glBufferData(GL_ARRAY_BUFFER, pentaBuf.limit()*4, pentaBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[2]);
        FloatBuffer pentaTexBuf = Buffers.newDirectFloatBuffer(penta_tc);
        gl.glBufferData(GL_ARRAY_BUFFER, pentaTexBuf.limit()*4, pentaTexBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[3]);
        FloatBuffer vertBuf = Buffers.newDirectFloatBuffer(pvalues);
        gl.glBufferData(GL_ARRAY_BUFFER, vertBuf.limit()*4, vertBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[4]);
        FloatBuffer texBuf = Buffers.newDirectFloatBuffer(tvalues);
        gl.glBufferData(GL_ARRAY_BUFFER, texBuf.limit()*4, texBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[5]);
        FloatBuffer norBuf = Buffers.newDirectFloatBuffer(nvalues);
        gl.glBufferData(GL_ARRAY_BUFFER, norBuf.limit()*4, norBuf, GL_STATIC_DRAW);
    }

    private Matrix3D perspective(float fovy, float aspect, float n, float f)
    {	float q = 1.0f / ((float) Math.tan(Math.toRadians(0.5f * fovy)));
        float A = q / aspect;
        float B = (n + f) / (n - f);
        float C = (2.0f * n * f) / (n - f);
        Matrix3D r = new Matrix3D();
        r.setElementAt(0,0,A);
        r.setElementAt(1,1,q);
        r.setElementAt(2,2,B);
        r.setElementAt(3,2,-1.0f);
        r.setElementAt(2,3,C);
        return r;
    }

    public static void main(String[] args) { new Main(); }
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
    public void dispose(GLAutoDrawable drawable) {}

    private int createShaderProgram()
    {	GL4 gl = (GL4) GLContext.getCurrentGL();

        String vshaderSource[] = util.readShaderSource("src/SolarSystem/vert.shader");
        String fshaderSource[] = util.readShaderSource("src/SolarSystem/frag.shader");

        int vShader = gl.glCreateShader(GL_VERTEX_SHADER);
        int fShader = gl.glCreateShader(GL_FRAGMENT_SHADER);

        gl.glShaderSource(vShader, vshaderSource.length, vshaderSource, null, 0);
        gl.glShaderSource(fShader, fshaderSource.length, fshaderSource, null, 0);

        gl.glCompileShader(vShader);
        gl.glCompileShader(fShader);

        int vfprogram = gl.glCreateProgram();
        gl.glAttachShader(vfprogram, vShader);
        gl.glAttachShader(vfprogram, fShader);
        gl.glLinkProgram(vfprogram);
        return vfprogram;
    }

    private Matrix3D lookAt(Vector3D eyeV, Vector3D targetV, Vector3D y)
    {
        Matrix3D look = new Matrix3D();
        cameraRight = (cameraFwd.cross(y)).normalize();
        cameraUp = (cameraRight.cross(cameraFwd)).normalize();

        look.setElementAt(0, 0, cameraRight.getX());
        look.setElementAt(1, 0, cameraUp.getX());
        look.setElementAt(2, 0, -cameraFwd.getX());
        look.setElementAt(3, 0, 0);
        look.setElementAt(0, 1, cameraRight.getY());
        look.setElementAt(1, 1, cameraUp.getY());
        look.setElementAt(2, 1, -cameraFwd.getY());
        look.setElementAt(3, 1, 0);
        look.setElementAt(0, 2, cameraRight.getZ());
        look.setElementAt(1, 2, cameraUp.getZ());
        look.setElementAt(2, 2, -cameraFwd.getZ());
        look.setElementAt(3, 2, 0);
        look.setElementAt(0, 3, cameraRight.dot(eyeV.mult(-1)));
        look.setElementAt(1, 3, cameraUp.dot(eyeV.mult(-1)));
        look.setElementAt(2, 3, (cameraFwd.mult(-1)).dot(eyeV.mult(-1)));
        look.setElementAt(3, 3, 1);
        return look;
    }

    public Texture loadTexture(String textureFileName)
    {	Texture tex = null;
        try { tex = TextureIO.newTexture(new File(textureFileName), false); }
        catch (Exception e) { e.printStackTrace(); }
        return tex;
    }

    public void setWorldAxis(boolean b) {
        worldAxis = b;
    }

    public void keyPress(int keyCode) {
        if ((keyCode == KeyEvent.VK_W)) {
            zoomIn = true;
        }
        else if ((keyCode == KeyEvent.VK_S)) {
            zoomOut = true;

        }
        else if ((keyCode == KeyEvent.VK_A)) {
            strafeLeft = true;

        }
        else if ((keyCode == KeyEvent.VK_D)) {
            strafeRight = true;

        }
        else if ((keyCode == KeyEvent.VK_Q)) {
            moveUp = true;

        }
        else if ((keyCode == KeyEvent.VK_E)) {
            moveDown = true;
        }
        else if ((keyCode == KeyEvent.VK_LEFT)) {
            panLeft = true;
        }
        else if ((keyCode == KeyEvent.VK_RIGHT)) {
            panRight = true;
        }
        else if ((keyCode == KeyEvent.VK_UP)) {
            pitchUp = true;
        }
        else if ((keyCode == KeyEvent.VK_DOWN)) {
            pitchDown = true;
        }
        else if ((keyCode == KeyEvent.VK_SPACE)) {
            setWorldAxis(!worldAxis);
        }

    }

    public void keyRelease(int keyCode) {
        if ((keyCode == KeyEvent.VK_W)) {
            zoomIn = false;
        }
        else if ((keyCode == KeyEvent.VK_S)) {
            zoomOut = false;
        }
        else if ((keyCode == KeyEvent.VK_A)) {
            strafeLeft = false;

        }
        else if ((keyCode == KeyEvent.VK_D)) {
            strafeRight = false;

        }
        else if ((keyCode == KeyEvent.VK_Q)) {
            moveUp = false;
        }
        else if ((keyCode == KeyEvent.VK_E)) {
            moveDown = false;
        }
        else if ((keyCode == KeyEvent.VK_LEFT)) {
            panLeft = false;
        }
        else if ((keyCode == KeyEvent.VK_RIGHT)) {
            panRight = false;
        }
        else if ((keyCode == KeyEvent.VK_UP)) {
            pitchUp = false;
        }
        else if ((keyCode == KeyEvent.VK_DOWN)) {
            pitchDown = false;
        }
    }

    // key event
    public void keyTyped(KeyEvent key) {}
    public void keyPressed(KeyEvent key) {
        keyPress(key.getKeyCode());
    }
    public void keyReleased(KeyEvent key) {
        keyRelease(key.getKeyCode());
    }
}
