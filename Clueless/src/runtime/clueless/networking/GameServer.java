/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime.clueless.networking;

import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    //shared object
    public static GameMsg msg;
    public static volatile int turn;

    public GameServer(int numPlayers){
        System.out.println("GameServer is being created.");
        server = null;
        numClients = 0;
        this.numPlayers = numPlayers;
        msg = new GameMsg();
        turn = -99;
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
                    cthreads[numClients] = new ClientThread(server.accept(),numClients);
                    
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
    
    //starts the game once everyone has joined
    public void startGame() throws InterruptedException{
        
        //send state to threads
        System.out.println("Sending board state to all clients.");
        sendBoardState();
        System.out.println("Done sending board state to all clients.");
        
        //Need turn logic here
        dealCards();
    
        
    }
    
    public void dealCards(){
        
    }
    
    public void sendBoardState() {

        for (int i = 0; i < numClients; i++) {

            msg.name = "Server";
            msg.command = GameMsg.cmd.board_state;
            msg.id = i;
            //TODO: set boardstate

            turn = i;
            serverWait();
            
            //verify that msg worked, otherwise return failure
            switch (msg.command) {
                case ack:
                    System.out.println("Successfully sent board state to client "+Integer.toString(i));
                    break;
                case invalid:
                    System.out.println("Failed to send board state to client "+Integer.toString(i));
                    break;
                default:
                    System.out.println("Recieved invalid response from client "+Integer.toString(i));
                    break;
            }
        }

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
    
    //wait for a thread to do its business
    private void serverWait(){
        while(turn != -99){
            try {
                sleep(25);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
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
