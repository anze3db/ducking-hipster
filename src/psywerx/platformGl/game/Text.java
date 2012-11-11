package psywerx.platformGl.game;

import java.util.LinkedList;

import javax.media.opengl.GL2ES2;

public class Text implements Drawable {
    
    private LinkedList<Square> chars = new LinkedList<Square>();
    
    public Text(String s, Vector position){
        char[] sc = s.toCharArray();
        for(int i = 0; i < sc.length; i++){
            Square sq = new Square();
            sq.position.x = sq.size*i*2*-1;
            sq.position.x += position.x;
            sq.position.y = position.y;
            sq.c = sc[i];
            sq.isText = 0.0f;
            chars.add(sq);
        }
    }

    public void updateText(String s){
        if(s.length() > chars.size()){
            return;
        }
        char[] sc = s.toCharArray();
        for(int i = 0; i < sc.length; i++){
            chars.get(i).c = sc[i];
        }
    }

    @Override
    public void update(double theta) {
        
    }


    @Override
    public void draw(GL2ES2 gl) {
        for (Square s : chars) {
            s.draw(gl);
        }
    }

    
    
}
