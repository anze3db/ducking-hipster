package psywerx.platformGl.game;

import javax.media.opengl.GL2ES2;

public class Player implements Drawable {

    protected Vector direction = new Vector(0.0f, 1.0f);
    protected Square[] pieces;
    private Square shadow;
    protected Square main;

    public Player() {
        // Set a different color:

        shadow = new Square();
        main = new Square();

        shadow.color = new float[] { 0.0f, 0.0f, 0.0f };
        main.color = new float[] { 0.0f, 0.0f, 1.0f };

        main.z = -0.0021f;
        shadow.z = -0.0011f;

        main.position = new Vector(0.0f, 0.9f);
        shadow.position = new Vector(-0.01f, 0.89f);

        main.size = 0.05f;
        shadow.size = 0.05f;

    }

    public void update(double theta) {

        main.position.x += direction.x * 0.03f;
        if (main.position.x < -0.95f) {
            main.position.x = -0.95f;
        }
        if (main.position.x > 0.95) {
            main.position.x = 0.95f;
        }
        shadow.position.x = main.position.x - 0.01f;
        shadow.position.y = main.position.y - 0.01f;
    }

    @Override
    public void draw(GL2ES2 gl) {
        main.draw(gl);
        shadow.draw(gl);
    }
}
