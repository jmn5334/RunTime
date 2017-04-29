/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime.clueless.networking;

/**
 *
 * @author jmnew
 */
public class NetworkTest2 {
    
    public static void main(String [ ] args){
        
        GameClient gc;
        
        gc =  new GameClient();
        
        System.out.println("Hello world 2!!!");
        
        gc.connectToServer();
        while(true){
            gc.sayHi();
        }
        
    }
}
