package psywerx.platformGl.game;

import java.nio.FloatBuffer;

import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;

import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;

public class Main implements GLEventListener {

    private double t0 = System.currentTimeMillis();
    private double theta;
    private double s;

    private static int width = 250;
    private static int height = 800;

    private int shaderProgram;
    private int vertShader;
    private int fragShader;
    private int ModelViewProjectionMatrix_location;

    protected static final Game game = new Game();

    public static void main(String[] args) {

        GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2ES2));

        caps.setBackgroundOpaque(false);
        GLWindow glWindow = GLWindow.create(caps);

        glWindow.setTitle("Ducking hipster");
        glWindow.setSize(width, height);
        glWindow.setUndecorated(false);
        glWindow.setPointerVisible(true);
        glWindow.setVisible(true);

        glWindow.addGLEventListener(new Main() /* GLEventListener */);
        Animator animator = new Animator(glWindow);
        animator.add(glWindow);
        animator.start();
    }

    @Override
    public void display(GLAutoDrawable drawable) {

        // Update variables used in animation
        double t1 = System.currentTimeMillis();
        theta += (t1 - t0) * 0.005f;
        t0 = t1;
        s = Math.sin(theta);

        // Get gl
        GL2ES2 gl = drawable.getGL().getGL2ES2();

        // Clear screen
        gl.glClearColor(0, 0, 0, 0.5f);
        gl.glClear(GL2ES2.GL_STENCIL_BUFFER_BIT | GL2ES2.GL_COLOR_BUFFER_BIT
                | GL2ES2.GL_DEPTH_BUFFER_BIT);

        // Use the shaderProgram that got linked during the init part.
        gl.glUseProgram(shaderProgram);

        float[] model_view_projection; // Gets sent to the vertex shader

        model_view_projection = ProjectionHelper.translate(ProjectionHelper.identity_matrix, 0.0f,
                0.0f, -0.1f);
        model_view_projection = ProjectionHelper.rotate(model_view_projection, (float) 30f
                * (float) s, 1.0f, 0.0f, 1.0f);

        // Send the final projection matrix to the vertex shader by
        // using the uniform location id obtained during the init part.
        gl.glUniformMatrix4fv(ModelViewProjectionMatrix_location, 1, false, model_view_projection,
                0);

        float[] vertices = { 0.0f, 1.0f, 0.0f, // Top
                -1.0f, -1.0f, 0.0f, // Bottom Left
                1.0f, -1.0f, 0.0f // Bottom Right
        };

        // This is done so that the data doesn't get garbage collected
        FloatBuffer fbVertices = Buffers.newDirectFloatBuffer(vertices);

        gl.glVertexAttribPointer(0, 3, GL2ES2.GL_FLOAT, false, 0, fbVertices);
        gl.glEnableVertexAttribArray(0);

        float[] colors = { 1.0f, 0.0f, 0.0f, 1.0f, // Top color (red)
                0.0f, 0.0f, 0.0f, 1.0f, // Bottom Left color (black)
                1.0f, 1.0f, 0.0f, 0.9f // Bottom Right color (yellow) with 10%
                                       // transparence
        };

        FloatBuffer fbColors = Buffers.newDirectFloatBuffer(colors);

        gl.glVertexAttribPointer(1, 4, GL2ES2.GL_FLOAT, false, 0, fbColors);
        gl.glEnableVertexAttribArray(1);

        gl.glDrawArrays(GL2ES2.GL_TRIANGLES, 0, 3); // Draw the vertices as
                                                    // triangle

        gl.glDisableVertexAttribArray(0); // Allow release of vertex position
                                          // memory
        gl.glDisableVertexAttribArray(1); // Allow release of vertex color
                                          // memory
        // It is only safe to let the garbage collector collect the vertices and
        // colors
        // NIO buffers data after first calling glDisableVertexAttribArray.
        fbVertices = null;
        fbColors = null;
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        System.out.println("cleanup, remember to release shaders");
        GL2ES2 gl = drawable.getGL().getGL2ES2();
        gl.glUseProgram(0);
        gl.glDetachShader(shaderProgram, vertShader);
        gl.glDeleteShader(vertShader);
        gl.glDetachShader(shaderProgram, fragShader);
        gl.glDeleteShader(fragShader);
        gl.glDeleteProgram(shaderProgram);
        System.exit(0);
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
        ModelViewProjectionMatrix_location = gl.glGetUniformLocation(shaderProgram,
                "uniform_Projection");
        
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        System.out.println("Window resized to width=" + w + " height=" + h);
        width = w;
        height = h;

        // Get gl
        GL2ES2 gl = drawable.getGL().getGL2ES2();

        // Optional: Set viewport
        // Render to a square at the center of the window.
        gl.glViewport((width - height) / 2, 0, height, height);

    }
}
