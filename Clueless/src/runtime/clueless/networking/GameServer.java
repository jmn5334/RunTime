/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime.clueless.networking;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author jmnew
 */
public class GameServer {
    
    private ServerSocket server;
    private String textArea;
    private final int MAX_THREADS = 6;
    private int numClients;
    private int numPlayers;

    public GameServer(int numPlayers){
        System.out.println("GameServer is being created.");
        server = null;
        textArea = "";
        numClients = 0;
        this.numPlayers = numPlayers;
    }
    
    public void acceptClients(){
        try{
            server = new ServerSocket(5000);
        }catch (IOException e){
            System.out.println("Could not listen on port 5000");
            System.exit(-1);
        }
        while(true){
            ClientThread td;
            try{
                //make sure we are under the max and the number desired by the user
                if(numClients < 6 && numClients < numPlayers){
                    System.out.println("Waiting for client connections...");
                    td = new ClientThread(server.accept());
                    System.out.println("Recieved a client connect request!!!");
                    System.out.println("Creating thread "+Integer.toString(numClients+1)+"...");
                    Thread t = new Thread(td);
                    t.start();
                    numClients++;
                }
            } catch (IOException e){
                System.out.println("Accept failed: 5000");
                System.exit(-1);
            }
        }
    }
    
    /**
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable{
        try{
            server.close();
        } catch (IOException e) {
            System.out.println("Could not close socket");
            System.exit(-1);
        } finally {         //called whether an exception is thrown or not
            super.finalize();
        }
    }
    
}
