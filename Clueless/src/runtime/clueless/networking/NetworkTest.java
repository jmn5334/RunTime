package runtime.clueless.networking;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jmnew
 */
public class NetworkTest {
    public static void main(String [ ] args){
        
        GameServer gs;
        GameClient gc;
        
        gs =  new GameServer();
        
        System.out.println("Hello world!!!");
        
        gs.acceptClients();
        
    }
}
