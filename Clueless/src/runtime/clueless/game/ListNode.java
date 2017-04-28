package runtime.clueless.game;

/**
 * Created by tesfaz1 on 4/28/17.
 */
public class ListNode {

    public String label = "test";
    public int id = 0;
    public Object data = null;
    public ListNode(String l, int d){ label = l; id=d;}
    public void setData(Object o){ data = o;}
    public Object getData(){ return data;}

    public String toString(){ return label;}
    public String getValue(){ return String.valueOf(id);}
    public int getID(){ return id;}
}
