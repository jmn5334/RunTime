/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime.clueless.game;

/**
 *
 * @author jmnew
 */
public class JCard {
    
    public enum card_type{
        suspect,
        weapon,
        room
    }
    
    private final String name;
    private final card_type type;
    
    public JCard(String name, card_type type){
        this.name = name;
        this.type = type;
    }
    
}
