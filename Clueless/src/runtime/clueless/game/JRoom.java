/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime.clueless.game;

import java.util.ArrayList;

/**
 *
 * @author jmnew
 * 
 * This class represents a room on the clue game board. Note that all rooms start empty.
 */
public class JRoom {
    
    private final String name;
    private final ArrayList<JWeapon> weapons;
    private final ArrayList<JSuspect> suspects;
    private ArrayList<JHallway> adjacentHalls;
    private JRoom secretPassage;
            
    public JRoom(String name, ArrayList<JWeapon> weapons, 
            ArrayList<JSuspect> suspects){
        
        this.name = name;
        this.weapons = weapons;
        this.suspects = suspects;
        this.adjacentHalls = null;
        this.secretPassage = null;
    }
    
    //checks whether the hallway in question is adjacent to this room
    public boolean isHallwayAdjacent(JHallway h){
        
        boolean isAdjacent = false;
        
        for(JHallway hallway : adjacentHalls){
            if(h.equals(hallway))
                isAdjacent = true;
        }
        
        return isAdjacent;
    }
    
    public void setAdjacentLocations(ArrayList<JHallway> adjacentHalls, 
            JRoom secretPassage){
        
        this.adjacentHalls = adjacentHalls;
        this.secretPassage = secretPassage;
    }
    
    //searchs room for a given suspect
    //returns JSuspect if we have one, otherwise return null
    public JSuspect hasSuspect(JSuspect s){
        
        int index = getSuspects().indexOf(s);
        
        if(index == -1)
            return null;
        else
            return getSuspects().get(index);
    }
    
    //searchs room for a given weapon
    //returns JWeapon if we have one, otherwise return null
    public JWeapon hasWeapon(JWeapon w){
        
        int index = getSuspects().indexOf(w);
        
        if(index == -1)
            return null;
        else
            return getWeapons().get(index);  
    }
    
    public void addSuspect(JSuspect s){
        getSuspects().add(s);
    }
    
    public boolean removeSuspect(JSuspect s){
        return getSuspects().remove(s);
    }
    
    public void addWeapon(JWeapon w){
        getWeapons().add(w);
    }
    
    public boolean removeWeapon(JWeapon w){
        return getWeapons().remove(w);
    }
    
    public ArrayList<JHallway> getAdjacentHallways(){
        return adjacentHalls;
    }
    
    public String getName(){
        return name;
    }
    
    public JRoom getSecretPassage(){
        return secretPassage;
    }
    
    public void printRoom(){
        
        System.out.println(name+" contains:");
        
        System.out.println("WEAPONS");
        for (JWeapon weapon : getWeapons()) {
            weapon.printWeapon();
        }
        
        if(getWeapons().isEmpty())
            System.out.println("none");
        
        System.out.println("SUSPECTS");
        for (JSuspect suspect : getSuspects()) {
            suspect.printSuspect();
        }
        
        if(getSuspects().isEmpty())
            System.out.println("none");
        
        System.out.println("HALLWAYS");
        for (JHallway hallway : adjacentHalls) {
            System.out.println("Hallway "+Integer.toString(hallway.getId()));
        }
        
        System.out.println("SECRET PASSAGE");
        if(secretPassage != null)
            System.out.println(secretPassage.getName());
        else
            System.out.println("none");
        
        System.out.println();
    }
    
    /**
     * @return the weapons
     */
    public ArrayList<JWeapon> getWeapons() {
        return weapons;
    }

    /**
     * @return the suspects
     */
    public ArrayList<JSuspect> getSuspects() {
        return suspects;
    }
}
