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
public class JSuspect {

    
    private final String name;
    private JHallway hLocation;
    private JRoom rLocation;
    private boolean wasMovedOnSuggest; //set if this player was moved by a suggestion
    
    //constructor
    public JSuspect(String name){
        this.name = name;
        hLocation = null;
        rLocation = null;
        wasMovedOnSuggest = false;
    }
    
    //returns 1 for room, 0 for hallway, -1 for error
    public int inRoom(){
        if(hLocation == null){
            if(rLocation == null){
                System.out.println("inRoom error: Can't find suspect "+name);
                return -1;
            } 
            return 1;
        }
        return 0;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return the hLocation
     */
    public JHallway getHallwayLocation() {
        return hLocation;
    }

    /**
     * @param hLocation the hLocation to set
     */
    public void setHallwayLocation(JHallway hLocation) {
        this.hLocation = hLocation;
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
        this.hLocation = null;
    }
    
    /**
     * @return the wasMovedOnSuggest
     */
    public boolean isWasMovedOnSuggest() {
        return wasMovedOnSuggest;
    }

    /**
     * @param wasMovedOnSuggest the wasMovedOnSuggest to set
     */
    public void setWasMovedOnSuggest(boolean wasMovedOnSuggest) {
        this.wasMovedOnSuggest = wasMovedOnSuggest;
    }
    
    public void printSuspect(){
        if(rLocation == null && hLocation == null)
            System.out.println(name+" is LOST!");
        else if(rLocation == null)
            System.out.println(name+" located in "+Integer.toString(hLocation.getId()));
        else if(hLocation == null)
            System.out.println(name+" located in "+rLocation.getName());    
        else
            System.out.println(name+" is located in both"+rLocation.getName()+" and "+Integer.toString(hLocation.getId()));
    }
}
