package runtime.clueless.gui.fxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import runtime.clueless.config.Config;


public class MainGUIFXML {


    // used by the logger to output status to the gui
    @FXML private WebView status_webview;

    @FXML Button moveleft_button;
    @FXML Button moveright_button;
    @FXML Button moveup_button;
    @FXML Button movedown_button;

    @FXML Button p1_chip;
    @FXML Button p2_chip;
    @FXML Button p3_chip;
    @FXML Button p4_chip;
    @FXML Button p5_chip;
    @FXML Button p6_chip;

    String playerid;
    Button playerChip;

    @FXML
    public void initialize(){
        playerid = Config.ptr().str("playernumber");

        if(playerid.contains("p1")){
            playerChip = p1_chip;
        }
    }

    @FXML void moveleftCallback(){

       playerChip.setLayoutX( playerChip.getLayoutX()+50);
    }
    
    @FXML void moverightCallback(){

       playerChip.setLayoutX( playerChip.getLayoutX()-50);
    }
    @FXML void moveupCallback(){

       playerChip.setLayoutY( playerChip.getLayoutY()-50);
    }
  @FXML void movedownCallback(){

       playerChip.setLayoutY( playerChip.getLayoutY()+50);
    }

}
