/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime.clueless.networking;

import java.io.Serializable;
import runtime.clueless.game.JCard;
import runtime.clueless.game.RoomCard;
import runtime.clueless.game.SuspectCard;
import runtime.clueless.game.WeaponCard;

/**
 *
 * @author jmnew
 */
public class GameMsg implements Serializable{
    
    private static final long serialVersionUID = 6529685098267757690L;
    
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
        update,
        
        //client to server commands
        move,
        suggest,
        accuse,
        send_card_client,
        end_turn,
        join_game,
        pass,
        
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
    public sub_cmd subcommand;
    
    //DATA
    //for init msg
    public String suspect;
    
    //for card msg
    public String card;
    public JCard.card_type ctype;
    
    //for move message
    public String dest;
    public boolean isRoom;
    public int destId;
    
    //for suggest
    public String weapon;
    
    //for board update msgs
    //public Board board;

    public GameMsg(){
        text = "Hello there!";
    }
    
}
