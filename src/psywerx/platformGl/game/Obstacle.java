package psywerx.platformGl.game;

import javax.media.opengl.GL2ES2;

public class Obstacle implements Drawable {

    protected Vector velocity = new Vector(0f, 0.5f);
    protected Square obstacle;
    private Square shadow;
    public static enum Type {NORMAL, BONUS}; 
    protected Type type = Type.NORMAL;
    

    public Obstacle() {
        obstacle = new Square();
        obstacle.z = -0.002f;
        obstacle.c = 'D';
        shadow = new Square();
        shadow.color = new float[] { 0f, 0f, 0f };
        shadow.z = -0.001f;
        shadow.alpha = 0.6f;
        
        if(Math.random() > 0.9 ){
            type = Type.BONUS;
            obstacle.c = '+';
            shadow.c = '+';
            obstacle.z = -0.0015f;
            obstacle.isText = 0.0f;
            obstacle.color = new float[] { 0f, 1f, 0f };
            shadow.isText = 0.0f;
        }

    }

    public void update(double theta) {
        // position.x += theta * direction.x;
        obstacle.position.y += theta * velocity.y;
        
        if (obstacle.position.y > Main.game.player.main.position.y - 2 * Main.game.player.main.size &&
                obstacle.position.y < Main.game.player.main.position.y + 2 * Main.game.player.main.size){
            if(obstacle.position.x + obstacle.size > Main.game.player.main.position.x - Main.game.player.main.size && 
                    obstacle.position.x - obstacle.size < Main.game.player.main.position.x + Main.game.player.main.size){
                // Collision:
                
                switch(type){
                case NORMAL:
                    obstacle.color = new float[] {0.6f,0.0f,0.0f};
                    Main.game.die();
                    break;
                case BONUS:
                    obstacle.position.x = 100f;
                    Main.game.score += 3;
                    break;
                }
                
            }
        }
        
        if (obstacle.position.y > 1) {
            obstacle.position.x = (float) Math.random() * 2 - 1;
            velocity.y = (float) Math.random();
            obstacle.position.y = -1;
            Main.game.score += 1;
        }
        shadow.position.x = obstacle.position.x - 0.01f;
        shadow.position.y = obstacle.position.y - 0.01f;
    }

    public void draw(GL2ES2 gl) {
        obstacle.draw(gl);
        shadow.draw(gl);
    }
}
