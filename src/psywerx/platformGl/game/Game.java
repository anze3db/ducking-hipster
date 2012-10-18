package psywerx.platformGl.game;

import java.nio.FloatBuffer;

import javax.media.opengl.GL2ES2;

import com.jogamp.common.nio.Buffers;

public class Game {
    protected final InputHandler input = new InputHandler();
    private double s;

    protected void tick(GL2ES2 gl, double theta) {
        s = Math.sin(theta);
    }

    protected void draw(GL2ES2 gl) {
        // Clear screen
        gl.glClearColor(0, 0, 0, 0.5f);
        gl.glClear(GL2ES2.GL_STENCIL_BUFFER_BIT | GL2ES2.GL_COLOR_BUFFER_BIT
                | GL2ES2.GL_DEPTH_BUFFER_BIT);

        // Use the shaderProgram that got linked during the init part.
        gl.glUseProgram(Main.shaderProgram);

        float[] model_view_projection; // Gets sent to the vertex shader

        model_view_projection = ProjectionHelper.translate(ProjectionHelper.identity_matrix, 0.0f,
                0.0f, -0.1f);
        model_view_projection = ProjectionHelper.rotate(model_view_projection, (float) 30f
                * (float) s, 1.0f, 0.0f, 1.0f);
        // Send the final projection matrix to the vertex shader by
        // using the uniform location id obtained during the init part.
        gl.glUniformMatrix4fv(Main.ModelViewProjectionMatrix_location, 1, false,
                model_view_projection, 0);

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
}
