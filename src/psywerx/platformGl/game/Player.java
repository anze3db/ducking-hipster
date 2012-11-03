package psywerx.platformGl.game;

public class Player extends GameObject {
    
    protected Vector direction = new Vector(0.0f, 1.0f);
    
    public Player(){
        color[0] = 0f;
        color[1] = 0f;
        color[2] = 1f;
    }
    
    protected void update(double theta){
        position.x += direction.x * 0.01f; 
        if(position.x < -1){
            position.x = -1f;
        }
        if(position.x > 1){
            position.x = 1f;
        }
        position.y = 1.0f;
    }
}
