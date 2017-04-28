package runtime.clueless.game;

/**
 * Created by tesfaz1 on 4/27/17.
 */
public class SuspectCard {

    public String name = "";
    public SuspectCard(String n){
        name = n;
    }

    Player player;
    public void setPlayer(Player p ){ player = p;}
    public Player getPlayer(){ return player;}
}
