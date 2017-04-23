package runtime.clueless.gui.fxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import runtime.clueless.config.Config;
import runtime.clueless.game.GameManager;
import runtime.clueless.game.Player;


public class MainGUIFXML {


    // used by the logger to output status to the gui
    @FXML private WebView status_webview;

    @FXML Button moveleft_button;
    @FXML Button moveright_button;
    @FXML Button moveup_button;
    @FXML Button movedown_button;
    @FXML Button secretpass_button;

    @FXML ImageView gameboard_imageview;

    @FXML Button p1_chip;
    @FXML Button p2_chip;
    @FXML Button p3_chip;
    @FXML Button p4_chip;
    @FXML Button p5_chip;
    @FXML Button p6_chip;

    String playerid;
    Button playerChip = p1_chip;

    @FXML
    public void initialize(){
        playerid = Config.ptr().str("playernumber");

        if(playerid.contains("p1")){
            playerChip = p1_chip;
        }

        gameboard_imageview.setImage(new Image("/icons/gameboard.png"));

        GameManager gm = GameManager.getInstance();
        gm.setPlayerChip(playerChip);

        refreshMovementButtons();

    }

    void refreshMovementButtons(){
        GameManager gm = GameManager.getInstance();
        if(gm.canMoveAbove())
            moveup_button.setDisable(false);
        else
            moveup_button.setDisable(true);

        if(gm.canMoveBelow())
            movedown_button.setDisable(false);
        else
            movedown_button.setDisable(true);
        if(gm.canMoveLeft())
            moveleft_button.setDisable(false);
        else
            moveleft_button.setDisable(true);
        if(gm.canMoveRight())
            moveright_button.setDisable(false);
        else
            moveright_button.setDisable(true);
    }

    @FXML void moveleftCallback(){

        GameManager.getInstance().moveLeft();refreshMovementButtons();
    }
    
    @FXML void moverightCallback(){

        GameManager.getInstance().moveRight();refreshMovementButtons();
    }
    @FXML void moveupCallback(){

        GameManager.getInstance().moveUp();refreshMovementButtons();
    }
    @FXML void movedownCallback(){

      GameManager.getInstance().moveDown();refreshMovementButtons();
    }
    @FXML void secretpassCallback(){
        GameManager.getInstance().moveDown();refreshMovementButtons();

    }

}
