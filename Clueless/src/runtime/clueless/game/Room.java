package runtime.clueless.game;

/**
 * Created by tesfaz1 on 4/20/17.
 */
public class Room implements Place {

    Hallway above=null;
    Hallway below=null;
    Hallway left=null;
    Hallway right=null;

    RoomCard card = null;

    private Room diagonal=null;

    int pixelX=0 , pixelY = 0;

    public Room(){
    }


    public void setCard(RoomCard c){ card = c; label = card.name;}
    public RoomCard getCard(){ return card;}

    String label = "---";
    public void setLabel(String l){ label = l;}
    public String getLabel(){ return label;}

    public boolean canMoveAbove(){ return above!=null; }
    public boolean canMoveBelow(){ return below!=null; }
    public boolean canMoveLeft(){ return left!=null; }
    public boolean canMoveRight(){ return right!=null; }
    public boolean canMoveDiagonal() {return  diagonal!=null;}


    public void setAbove(Place h){ above=(Hallway)h; }
    public void setBelow(Place h){ below=(Hallway)h; }
    public void setLeft(Place h){ left=(Hallway)h; }
    public void setRight(Place h){ right=(Hallway)h; }
    public void setDiagonal(Place h){diagonal=(Room) h;}

    public Place getAbove(){ return above; }
    public Place getBelow(){ return below; }
    public Place getLeft(){ return left; }
    public Place getRight(){ return right; }
    public Room getDiagonal(){return diagonal;}

    public void setPixel(int x, int y){ pixelX = x; pixelY=y;}
    public int getX(){ return pixelX;}
    public int getY(){ return pixelY;}

}
