package runtime.clueless.game;

/**
 * Created by tesfaz1 on 4/20/17.
 */
public class Hallway  implements Place{
    Room above=null;
    Room below=null;
    Room left=null;
    Room right=null;
    Room diagonal=null;

    int pixelX=0 , pixelY = 0;

    public Hallway(){

    }

    String label = "---";
    public void setLabel(String l){ label = l;}
    public String getLabel(){ return label;}

    public boolean canMoveAbove(){ return above!=null; }
    public boolean canMoveBelow(){ return below!=null; }
    public boolean canMoveLeft(){ return left!=null; }
    public boolean canMoveRight(){ return right!=null; }
    public boolean canMoveDiagonal() {return diagonal!=null;}

    public void setAbove(Place h){ above=(Room)h; }
    public void setBelow(Place h){ below=(Room)h; }
    public void setLeft(Place h){  left=(Room)h; }
    public void setRight(Place h){ right=(Room)h; }
    public void setDiagonal(Place h){ diagonal=(Room)h;}

    public Place getAbove(){ return above; }
    public Place getBelow(){ return below; }
    public Place getLeft(){ return left; }
    public Place getRight(){ return right; }
    public Room getDiagonal(){ return diagonal;}


    public void setPixel(int x, int y){ pixelX = x; pixelY=y;}
    public int getX(){ return pixelX;}
    public int getY(){ return pixelY;}
}
