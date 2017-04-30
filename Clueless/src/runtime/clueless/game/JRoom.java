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
    
    public void setAdjacentLocations(ArrayList<JHallway> adjacentHalls, 
            JRoom secretPassage){
        
        this.adjacentHalls = adjacentHalls;
        this.secretPassage = secretPassage;
    }
    
    //searchs room for a given suspect
    //returns JSuspect if we have one, otherwise return null
    public JSuspect hasSuspect(JSuspect s){
        
        int index = suspects.indexOf(s);
        
        if(index == -1)
            return null;
        else
            return suspects.get(index);
    }
    
    //searchs room for a given weapon
    //returns JWeapon if we have one, otherwise return null
    public JWeapon hasWeapon(JWeapon w){
        
        int index = suspects.indexOf(w);
        
        if(index == -1)
            return null;
        else
            return weapons.get(index);  
    }
    
    public void addSuspect(JSuspect s){
        suspects.add(s);
    }
    
    public boolean removeSuspect(JSuspect s){
        return suspects.remove(s);
    }
    
    public void addWeapon(JWeapon w){
        weapons.add(w);
    }
    
    public boolean removeWeapon(JWeapon w){
        return weapons.remove(w);
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
}
