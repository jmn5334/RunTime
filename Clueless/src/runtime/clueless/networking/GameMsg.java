/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime.clueless.networking;

import java.io.Serializable;
import runtime.clueless.game.RoomCard;
import runtime.clueless.game.SuspectCard;
import runtime.clueless.game.WeaponCard;

/**
 *
 * @author jmnew
 */
public class GameMsg implements Serializable{
    
    public enum cmd {
        
        //server to client commands
        start_turn,
        reveal_card,
        send_card_server,
        kill_player,
        game_over,
        board_state,
        stuck,
        init,
        
        //client to server commands
        move,
        suggest,
        accuse,
        send_card_client,
        end_turn,
        join_game,
        
        //both
        ack,
        invalid
    }
    
    public enum sub_cmd {
        moveOnSuggest,
        move2hall,
        move2room
    }
    
    public String text; //for debugging/message content
    
    //HEADER
    
    public String name;
    public int id;
    public cmd command;
    
    //DATA
    //for init msg
    public String suspect;
    
    //for board update msgs
    //public Board board;

    public GameMsg(){
        text = "Hello there!";
    }
    
}
