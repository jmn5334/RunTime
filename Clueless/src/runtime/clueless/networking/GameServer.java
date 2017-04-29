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
    private final int MAX_THREADS = 6;
    private int numClients;
    private final int numPlayers;
    private final Thread[] threads = new Thread[MAX_THREADS];
    private final ClientThread[] cthreads = new ClientThread[MAX_THREADS];
    public static GameMsg msg;
    

    public GameServer(int numPlayers){
        System.out.println("GameServer is being created.");
        server = null;
        numClients = 0;
        this.numPlayers = numPlayers;
        msg = new GameMsg();
    }
    
    public void acceptClients(){
        try{
            server = new ServerSocket(5000);
        }catch (IOException e){
            System.out.println("Could not listen on port 5000");
            System.exit(-1);
        }
        while(true){
            try{
                //make sure we are under the max and the number desired by the user
                if(numClients < MAX_THREADS && numClients < numPlayers){
                    
                    
                    System.out.println("Waiting for client connections...");
                    cthreads[numClients] = new ClientThread(server.accept());
                    
                    System.out.println("Recieved a client connect request!!!");
                    System.out.println("Creating thread "+Integer.toString(numClients)+"...");
                    threads[numClients] = new Thread(cthreads[numClients]);
                    threads[numClients].start();

                    numClients++;
                }
                else{
                    System.out.println("Done accepting clients.");
                    return;
                }
            } catch (IOException e){
                System.out.println("Accept failed: 5000");
                System.exit(-1);
            }
        }
    }
    
    public void startGame(){
        
        dealCards();
        
        sendBoardState();
        
        //Need turn logic here
        
    }
    
    public void dealCards(){
        
    }
    
    public void sendBoardState(){
        
    }
    
    public void startTurn(){
        
    }
    
    public void endGame(){
        
    }
    
    public void killPlayer(){
        
    }
    
    public void askNextPlayer(){
        
    }
    
    public void nooneHadIt(){
        
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
