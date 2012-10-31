package psywerx.platformGl.game;

import java.lang.reflect.Array;
import java.nio.FloatBuffer;
import java.util.Arrays;

import javax.media.opengl.GL2ES2;

import com.jogamp.common.nio.Buffers;

public class GameObject {

    protected float size = 0.1f;
    protected Vector position = new Vector(0.0f, 0.0f);
    protected Vector direction = new Vector(1f,1f);
    protected void update(double theta) {
        position.x += theta *  direction.x;
        position.y += theta *  direction.y;
        if (position.x > 1 || position.x < -1) {
            direction.x *= -1;
        }
        if (position.y > 1 || position.y < -1) {
            direction.y *= -1;
        }
        size = 0.1f;
    }

    protected void draw(GL2ES2 gl) {
        
        float[] model_projection = new float[16];
        Matrix.setIdentityM(model_projection, 0);
        Matrix.translateM(model_projection, 0, position.x, position.y, 0f);
        
        gl.glUniformMatrix4fv(Main.ModelViewProjectionMatrix_location, 1, false, model_projection, 0);
        
        float[] vertices = { 1.0f, -1.0f, 0.0f, // Bottom Right
                -1.0f, -1.0f, 0.0f, // Bottom Left
                1.0f, 1.0f, 0.0f, // Top Right
                -1.0f, 1.0f, 0.0f, // Top Left
        };
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] *= size;
        }
        

        // This is done so that the data doesn't get garbage collected
        FloatBuffer fbVertices = Buffers.newDirectFloatBuffer(vertices);

        gl.glVertexAttribPointer(0, 3, GL2ES2.GL_FLOAT, false, 0, fbVertices);
        gl.glEnableVertexAttribArray(0);

        float[] colors = { 1.0f, 1.0f, 0.0f, 1.0f, // Top color (red)
                1.0f, 1.0f, 0.0f, 1.0f, // Bottom Left color (black)
                1.0f, 1.0f, 0.0f, 0.9f, // Bottom Right color (yellow) with 10%
                1.0f, 1.0f, 0.0f, 1.0f, // transparence
        };

        FloatBuffer fbColors = Buffers.newDirectFloatBuffer(colors);

        gl.glVertexAttribPointer(1, 4, GL2ES2.GL_FLOAT, false, 0, fbColors);
        gl.glEnableVertexAttribArray(1);

        gl.glDrawArrays(GL2ES2.GL_TRIANGLE_STRIP, 0, 4); // Draw the vertices as

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
}
