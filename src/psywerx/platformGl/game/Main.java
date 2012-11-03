package psywerx.platformGl.game;

import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;

public class Main implements GLEventListener {

    protected static int WIDTH = 250;
    protected static int HEIGHT = 800;

    protected static final Game game = new Game();

    private double t0 = System.currentTimeMillis();
    private double theta;

    protected static int shaderProgram;
    protected static int ModelViewProjectionMatrix_location;
    protected static int ModelProjectionMatrix_location;

    private int vertShader;
    private int fragShader;

    public static void main(String[] args) {

        GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2ES2));

        caps.setBackgroundOpaque(false);
        GLWindow glWindow = GLWindow.create(caps);

        glWindow.setTitle("Ducking hipster");
        glWindow.setSize(WIDTH, HEIGHT);
        glWindow.setUndecorated(false);
        glWindow.setPointerVisible(true);
        glWindow.setVisible(true);

        glWindow.addGLEventListener(new Main());
        glWindow.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent key) {
                switch (key.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    game.direction -= 25f;
                    game.player.direction.x -= 1f;
                    break;
                case KeyEvent.VK_RIGHT:
                    game.direction += 25f;
                    game.player.direction.x += 1f;
                    break;
                }

            }

            @Override
            public void keyPressed(KeyEvent key) {
                switch (key.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    game.direction += 25f;
                    game.player.direction.x += 1f;
                    break;
                case KeyEvent.VK_RIGHT:
                    game.direction -= 25f;
                    game.player.direction.x -= 1f;
                    break;
                }
            }
        });
        Animator animator = new Animator(glWindow);
        animator.add(glWindow);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2ES2 gl = drawable.getGL().getGL2ES2();

        // Create shaders
        // OpenGL ES retuns a index id to be stored for future reference.
        vertShader = gl.glCreateShader(GL2ES2.GL_VERTEX_SHADER);
        fragShader = gl.glCreateShader(GL2ES2.GL_FRAGMENT_SHADER);

        // Compile the vertexShader String into a program.
        String[] vlines = { Shaders.vertexShader };
        int[] vlengths = { vlines[0].length() };
        gl.glShaderSource(vertShader, vlines.length, vlines, vlengths, 0);
        gl.glCompileShader(vertShader);

        // Check compile status.
        int[] compiled = new int[1];
        gl.glGetShaderiv(vertShader, GL2ES2.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            int[] logLength = new int[1];
            gl.glGetShaderiv(vertShader, GL2ES2.GL_INFO_LOG_LENGTH, logLength, 0);

            byte[] log = new byte[logLength[0]];
            gl.glGetShaderInfoLog(vertShader, logLength[0], (int[]) null, 0, log, 0);

            System.err.println("Error compiling the vertex shader: " + new String(log));
            System.exit(1);
        }

        // Compile the fragmentShader String into a program.
        String[] flines = new String[] { Shaders.fragmentShader };
        int[] flengths = new int[] { flines[0].length() };
        gl.glShaderSource(fragShader, flines.length, flines, flengths, 0);
        gl.glCompileShader(fragShader);

        // Check compile status.
        gl.glGetShaderiv(fragShader, GL2ES2.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            int[] logLength = new int[1];
            gl.glGetShaderiv(fragShader, GL2ES2.GL_INFO_LOG_LENGTH, logLength, 0);

            byte[] log = new byte[logLength[0]];
            gl.glGetShaderInfoLog(fragShader, logLength[0], (int[]) null, 0, log, 0);

            System.err.println("Error compiling the fragment shader: " + new String(log));
            System.exit(1);
        }

        // Each shaderProgram must have
        // one vertex shader and one fragment shader.
        shaderProgram = gl.glCreateProgram();
        gl.glAttachShader(shaderProgram, vertShader);
        gl.glAttachShader(shaderProgram, fragShader);

        // Associate attribute ids with the attribute names inside
        // the vertex shader.
        gl.glBindAttribLocation(shaderProgram, 0, "attribute_Position");
        gl.glBindAttribLocation(shaderProgram, 1, "attribute_Color");

        gl.glLinkProgram(shaderProgram);

        // Get a id number to the uniform_Projection matrix
        // so that we can update it.
        ModelViewProjectionMatrix_location = gl.glGetUniformLocation(shaderProgram, "uniform_Projection");
        ModelProjectionMatrix_location = gl.glGetUniformLocation(shaderProgram, "uniform_Model");

    }

    @Override
    public void display(GLAutoDrawable drawable) {

        // Update variables used in animation
        double t1 = System.currentTimeMillis();
        theta = (t1 - t0) * 0.001;
        t0 = t1;

        GL2ES2 gl = drawable.getGL().getGL2ES2();

        game.tick(theta);

        game.draw(gl);

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        WIDTH = w;
        HEIGHT = h;

        // Get gl
        GL2ES2 gl = drawable.getGL().getGL2ES2();

        // Optional: Set viewport
        // Render to a square at the center of the window.
        gl.glViewport(0, 0, WIDTH, HEIGHT);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        GL2ES2 gl = drawable.getGL().getGL2ES2();
        gl.glUseProgram(0);
        gl.glDetachShader(shaderProgram, vertShader);
        gl.glDeleteShader(vertShader);
        gl.glDetachShader(shaderProgram, fragShader);
        gl.glDeleteShader(fragShader);
        gl.glDeleteProgram(shaderProgram);
        System.exit(0);
    }
}
