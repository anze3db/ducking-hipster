package psywerx.platformGl.game;

import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;

import com.jogamp.common.nio.Buffers;

public class Square implements Drawable {

    protected float size = 0.05f;
    protected Vector position = new Vector(0.0f, 0.0f);
    protected Vector velocity = new Vector(0f, 0.5f);
    protected float[] color = { 1f, 1f, 0f };
    protected float z = 0.0f;
    protected char c = ' ';
    protected int sp = Main.shaderProgram;
    protected float isText = 1.0f;
    protected float alpha = 1.0f;
    private FloatBuffer fb;

    public void update(double theta) {

    }

    public void draw(GL2ES2 gl) {
        float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, position.x, position.y, z);
        Matrix.rotateM(modelMatrix, 0, -180, 0f, 0f, 1f);
        gl.glUniformMatrix4fv(Main.modelMatrix_location, 1, false, modelMatrix, 0);
        gl.glUniform1f(Main.isText_location, isText);
        float[] vertices = { 1.0f, -1.0f, 0.0f, // Bottom Right
                -1.0f, -1.0f, 0.0f, // Bottom Left
                1.0f, 1.0f, 0.0f, // Top Right
                -1.0f, 1.0f, 0.0f, // Top Left
        };
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] *= size;
        }

        // This is done so that the data doesn't get garbage collected
        fb = Buffers.newDirectFloatBuffer(vertices);

        gl.glVertexAttribPointer(0, 3, GL2ES2.GL_FLOAT, false, 0, fb);
        gl.glEnableVertexAttribArray(0);

        float[] colors = { color[0], color[1], color[2], alpha, // Top color
                color[0], color[1], color[2], alpha, // Bottom Left color
                color[0], color[1], color[2], alpha, // Bottom Right
                color[0], color[1], color[2], alpha, // Transparency
        };

        fb = Buffers.newDirectFloatBuffer(colors);

        gl.glVertexAttribPointer(1, 4, GL2ES2.GL_FLOAT, false, 0, fb);
        gl.glEnableVertexAttribArray(1);

        int charIndex = (int) c - 32;
        float charWidth = 0.03125f * 2;
        int NUM_SPRITES = 16;
        float uVal = (charIndex % NUM_SPRITES);
        float vVal = (charIndex / NUM_SPRITES);

        float[] tex = {
                // Mapping coordinates for the vertices
                (uVal + 1) * charWidth, vVal * charWidth, // top left (V2)
                (uVal) * charWidth, vVal * charWidth, // bottom left (V1)
                (uVal + 1) * charWidth, (vVal + 1) * charWidth, // top right
                uVal * charWidth, (vVal + 1) * charWidth, // bottom right (V3)
                                                                // (V4)
        };

        fb = Buffers.newDirectFloatBuffer(tex);

        gl.glVertexAttribPointer(2, 2, GL2ES2.GL_FLOAT, false, 0, fb);
        gl.glEnableVertexAttribArray(2);

        gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, Main.texture1);

        // Set the sampler texture unit to 0
        gl.glUniform1i(Main.sampler_location, 0);

        gl.glDrawArrays(GL2ES2.GL_TRIANGLE_STRIP, 0, 4); // Draw the vertices as

        gl.glDisableVertexAttribArray(0); // Allow release of vertex position
                                          // memory
        gl.glDisableVertexAttribArray(1); // Allow release of vertex color
                                          // memory
        gl.glDisableVertexAttribArray(2); 
        // It is only safe to let the garbage collector collect the vertices and
        // colors
        // NIO buffers data after first calling glDisableVertexAttribArray.
        fb = null;
    }

}
