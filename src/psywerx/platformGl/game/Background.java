package psywerx.platformGl.game;

import javax.media.opengl.GL2ES2;

public class Background implements Drawable {

    protected Square bgSquare;

    public Background() {

        bgSquare = new Square();
        bgSquare.size = 1;
        bgSquare.color = new float[] { 0.2f, 0.2f, 0.2f };
        bgSquare.z = 0.01f;
    }

    public void update(double theta) {
    }

    public void draw(GL2ES2 gl) {
        bgSquare.draw(gl);
    }
}
