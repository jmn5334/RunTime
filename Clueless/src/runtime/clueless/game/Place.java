package runtime.clueless.game;

/**
 * Created by tesfaz1 on 4/20/17.
 */
public interface Place {

    public boolean canMoveAbove();
    public boolean canMoveBelow();
    public boolean canMoveLeft();
    public boolean canMoveRight();

    public void setAbove(Place h);
    public void setBelow(Place h);
    public void setLeft(Place h);
    public void setRight(Place h);

    public void setPixel(int x, int y);
    public int getX();
    public int getY();
    public void setLabel(String l);
    public String getLabel();
    public Place getAbove();
    public Place getBelow();
    public Place getLeft();
    public Place getRight();
}
