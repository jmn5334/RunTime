/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime.clueless.networking;

import java.io.IOException;
import static java.lang.Thread.sleep;

/**
 *
 * @author jmnew
 */
public class NetworkTest2 {
    
    public static void main(String [ ] args) throws InterruptedException, IOException, ClassNotFoundException{
        
        GameClient gc;
        
        gc =  new GameClient();
        
        System.out.println("Hello world 2!!!");
        
        gc.connectToServer();
        
        //wait until we've recieved a message
        gc.waitForMsg();
/*
        gc.sendAccusation();
        gc.sendSuggestion();
*/

        gc.sendMove();

        
    }
}
