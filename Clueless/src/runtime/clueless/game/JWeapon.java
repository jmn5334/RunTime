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
    private JRoom rLocation;
    
    //constructor
    public JWeapon(String name){
        this.name = name;
        rLocation = null;
    }

    /**
     * @param hLocation the hLocation to set
     */
    public void setHallwayLocation(JHallway hLocation) {
        this.rLocation = null;
    }

    /**
     * @return the rLocation
     */
    public JRoom getRoomLocation() {
        return rLocation;
    }

    /**
     * @param rLocation the rLocation to set
     */
    public void setRoomLocation(JRoom rLocation) {
        this.rLocation = rLocation;
    }
    
    public void printWeapon(){
        if(rLocation != null)
            System.out.println(name+" located in "+rLocation.getName());    
        else
            System.out.println(name+" is LOST!");
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
}
