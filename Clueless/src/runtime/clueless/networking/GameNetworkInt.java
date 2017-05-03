/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

This class implements an interface that can easily called on by the GUI.
 */
package runtime.clueless.networking;

/**
 *
 * @author jmnew
 */
public class GameNetworkInt {
    
    private GameServer gs;
    private GameClient gc;
    boolean isServer;
    
    //contructor for GameNetworkInt class
    public GameNetworkInt(){
        gs = null;
        gc = null;
        isServer = false;
        System.out.println("Creating Game Network Interface object.");
    }
    
    //connects to an existing game server
    //return 0 for success 1 for failure
    public int joinGame(String name, String ip_addr, int port){
        
        //implement join game stuff here

        
        return 0;
    }
    
    //creates a game server for clients to connect to
    //this function doesn't return until the game ends
    public void hostGame(int port){
        //implement host game stuff here
        gs = new GameServer(3);
        gs.acceptClients();
    }
    
}
