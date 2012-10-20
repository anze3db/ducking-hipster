package psywerx.platformGl.game;

import java.nio.FloatBuffer;

import javax.media.opengl.GL2ES2;

import com.jogamp.common.nio.Buffers;

public class Game {
    protected final InputHandler input = new InputHandler();
    private double s;
    private float x;

    protected void tick(GL2ES2 gl, double theta) {
        s = theta;// Math.sin(theta);
        x += theta * 0.0000001;
        if (x > 1) {
            x = 0;
        }
    }

    protected void draw(GL2ES2 gl) {
        // Clear screen
        gl.glViewport((Main.WIDTH - Main.HEIGHT) / 2, 0, Main.HEIGHT, Main.HEIGHT);
        gl.glClearColor(0, 0, 0, 0.5f);
        gl.glClear(GL2ES2.GL_STENCIL_BUFFER_BIT | GL2ES2.GL_COLOR_BUFFER_BIT | GL2ES2.GL_DEPTH_BUFFER_BIT);

        // Use the shaderProgram that got linked during the init part.
        gl.glUseProgram(Main.shaderProgram);

        float[] model_view_projection; // Gets sent to the vertex shader
        model_view_projection = ProjectionHelper.translate(ProjectionHelper.identity_matrix, 0f, -x, 0);
        model_view_projection = ProjectionHelper.rotate(model_view_projection, (float) 30f * (float) s, 1f, 0f, 0.0f);
        // Send the final projection matrix to the vertex shader by
        // using the uniform location id obtained during the init part.
        gl.glUniformMatrix4fv(Main.ModelViewProjectionMatrix_location, 1, false, model_view_projection, 0);

        float[] vertices = { 1.0f, -1.0f, 0.0f, // Bottom Right
                -1.0f, -1.0f, 0.0f, // Bottom Left
                1.0f, 1.0f, 0.0f, // Top Right
                -1.0f, 1.0f, 0.0f, // Top Left
        };
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] *= 0.1;
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
        // triangle

        gl.glUniformMatrix4fv(Main.ModelViewProjectionMatrix_location, 1, false, ProjectionHelper.identity_matrix, 0);

        colors = new float[] { 1.0f, 1.0f, 0.0f, 1.0f, // Top color (red)
                1.0f, 1.0f, 0.0f, 1.0f, // Bottom Left color (black)
                0.0f, 0.0f, 0.0f, 1.0f, // Bottom Right color (yellow) with 10%
                0.0f, 0.0f, 0.0f, 1.0f, // transparence
        };
        fbColors = Buffers.newDirectFloatBuffer(colors);
        gl.glVertexAttribPointer(1, 4, GL2ES2.GL_FLOAT, false, 0, fbColors);

        gl.glDrawArrays(GL2ES2.GL_TRIANGLE_STRIP, 0, 4);

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
