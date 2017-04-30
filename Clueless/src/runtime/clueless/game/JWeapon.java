/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime.clueless.game;

/**
 *
 * @author jmnew
 * 
 * This is a class that represents a weapon game piece.
 */
public class JWeapon {

    private final String name;
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    //constructor
    public JWeapon(String name){
        this.name = name;
    }
    
}
