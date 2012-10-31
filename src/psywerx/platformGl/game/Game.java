package psywerx.platformGl.game;

import java.util.LinkedList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;

public class Game {
    protected final InputHandler input = new InputHandler();
    LinkedList<GameObject> objects = new LinkedList<GameObject>();

    protected Game(){
        for(int i=0; i<10; i++){
            objects.add(new GameObject());
        }
    }
    
    protected void tick(double theta) {
        
        for (GameObject o : objects) {
            o.update(theta);
        }
    }

    protected void draw(GL2ES2 gl) {
        // Clear screen
        gl.glEnable(GL.GL_DEPTH_TEST);
        // gl.glViewport(0, 0, Main.WIDTH, Main.HEIGHT);
        gl.glClearColor(0, 0f, 0, 1f);
        gl.glClear(GL2ES2.GL_STENCIL_BUFFER_BIT | GL2ES2.GL_COLOR_BUFFER_BIT | GL2ES2.GL_DEPTH_BUFFER_BIT);
        
        // Use the shaderProgram that got linked during the init part.
        gl.glUseProgram(Main.shaderProgram);
        
        float[] model_view_projection = new float[16]; // Gets sent to the
        float ratio = Main.WIDTH / (float)Main.HEIGHT;
        float[] model_projection = new float[16];
        Matrix.setLookAtM(model_projection , 0, 1f, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.frustumM(model_view_projection, 0, -ratio, ratio, -1, 1, 3, 7);
        
        float[] projection = new float[16];
        Matrix.multiplyMM(projection, 0, model_view_projection, 0, model_projection, 0);
        
        
        
        gl.glUniformMatrix4fv(Main.ModelProjectionMatrix_location, 1, false, projection, 0);
        
        
        for (GameObject g : objects) {
            g.draw(gl);
        }
    }
}
