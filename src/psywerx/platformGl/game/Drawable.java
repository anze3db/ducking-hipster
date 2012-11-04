package psywerx.platformGl.game;

import javax.media.opengl.GL2ES2;

public interface Drawable {
    abstract void update(double theta);
    abstract void draw(GL2ES2 gl);
}
