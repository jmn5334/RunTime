/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime.clueless.networking;

import static java.lang.Thread.sleep;

/**
 *
 * @author jmnew
 */
public class NetworkTest2 {
    
    public static void main(String [ ] args) throws InterruptedException{
        
        GameClient gc;
        
        gc =  new GameClient();
        
        System.out.println("Hello world 2!!!");
        
        gc.connectToServer();

        gc.sendAccusation();
        gc.sendMove();
        gc.sendSuggestion();
        
        while(true){
            sleep(10);
        }
        
    }
}
