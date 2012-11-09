package psywerx.platformGl.game;

import javax.media.opengl.GL2ES2;

public class Obstacle implements Drawable {

    protected Vector velocity = new Vector(0f, 0.5f);
    protected Square obstacle;
    private Square shadow;

    public Obstacle() {
        obstacle = new Square();
        obstacle.z = -0.002f;
        shadow = new Square();
        shadow.color = new float[] { 0f, 0f, 0f };
        shadow.z = -0.001f;
    }

    public void update(double theta) {
        // position.x += theta * direction.x;
        obstacle.position.y += theta * velocity.y;
        
        if (obstacle.position.y > Main.game.player.main.position.y - 2 * Main.game.player.main.size &&
                obstacle.position.y < Main.game.player.main.position.y + 2 * Main.game.player.main.size){
            if(obstacle.position.x + obstacle.size > Main.game.player.main.position.x - Main.game.player.main.size && 
                    obstacle.position.x - obstacle.size < Main.game.player.main.position.x + Main.game.player.main.size){
                // Collision:
                obstacle.color = new float[] {0.6f,0.0f,0.0f};
                Main.game.die();
            }
        }
        
        if (obstacle.position.y > 1) {
            obstacle.position.x = (float) Math.random() * 2 - 1;
            velocity.y = (float) Math.random();
            obstacle.position.y = -1;
        }
        shadow.position.x = obstacle.position.x - 0.01f;
        shadow.position.y = obstacle.position.y - 0.01f;
    }

    public void draw(GL2ES2 gl) {
        obstacle.draw(gl);
        shadow.draw(gl);
    }
}
