package runtime.clueless.game;

/**
 * Created by tesfaz1 on 4/27/17.
 */
public class WeaponCard {

    public String name = "";
    public WeaponCard(String n){
        name = n;
    }

    Player player;
    public void setPlayer(Player p ){ player = p;}
    public Player getPlayer(){ return player;}

}
