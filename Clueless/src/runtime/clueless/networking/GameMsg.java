/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime.clueless.networking;

import java.io.Serializable;

/**
 *
 * @author jmnew
 */
public class GameMsg implements Serializable{
    
    public String text;
    
    public GameMsg(){
        text = "Hello there!";
    }
    
}
