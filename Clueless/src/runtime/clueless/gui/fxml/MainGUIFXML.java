package runtime.clueless.gui.fxml;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import runtime.clueless.config.Config;
import runtime.clueless.game.*;

import java.util.ArrayList;


public class MainGUIFXML {


    
    // used by the logger to output status to the gui
    @FXML private WebView status_webview;

    @FXML ComboBox suggestion_person_combobox;
    @FXML ComboBox suggestion_weapon_combobox;
    
    //checkboxes
/*
    @FXML
    CheckBox scarlet_checkbox = new CheckBox();

    @FXML
    public void checkScarletBox() {
        if (scarlet_checkbox.isSelected()) {
            scarlet_checkbox.setSelected(false);
        } else {
            scarlet_checkbox.setSelected(true);
        }
    }
*/
    @FXML
    public void initialize(){

        GameManager gm = GameManager.getInstance();


        ArrayList<String> playerchoicelist = new ArrayList<>();
        for(int n=1; n<7; n++)
            playerchoicelist.add(String.valueOf(n));
        ObservableList<String> plist = FXCollections.observableArrayList(playerchoicelist);


        initSuggestOptions();
    }

    @FXML ComboBox accuse_room_combobox;
    @FXML ComboBox accuse_person_combobox;
    @FXML ComboBox accuse_weapon_combobox;

    private void initSuggestOptions(){

        GameManager gm = GameManager.getInstance();
        ArrayList<RoomCard> roomCards = gm.getRoomCards();
        ArrayList<SuspectCard> suspectCards = gm.getSuspectCards();
        ArrayList<WeaponCard> weaponCards = gm.getWeaponCards();

        ArrayList<ListNode> list = new ArrayList<>();
        for(int n=0;n<9; n++) {
            ListNode m = new ListNode(roomCards.get(n).name, n);
            m.setData(roomCards.get(n));
            list.add(m);
        }
        ObservableList<ListNode> plist = FXCollections.observableArrayList(list);
        accuse_room_combobox.setItems(plist);


        ArrayList<ListNode> list2 = new ArrayList<>();
        for(int n=0;n<6; n++) {
            ListNode m = new ListNode(suspectCards.get(n).name, n);
            m.setData(suspectCards.get(n));
            list2.add(m);
        }
        ObservableList<ListNode> plist2 = FXCollections.observableArrayList(list2);
        accuse_person_combobox.setItems(plist2);
        suggestion_person_combobox.setItems(plist2);

        ArrayList<ListNode> list3 = new ArrayList<>();
        for(int n=0;n<6; n++) {
            ListNode m = new ListNode(weaponCards.get(n).name, n);
            m.setData(weaponCards.get(n));
            list3.add(m);
        }
        ObservableList<ListNode> plist3 = FXCollections.observableArrayList(list3);
        accuse_weapon_combobox.setItems(plist3);
        suggestion_weapon_combobox.setItems(plist3);
    }

    @FXML public void playerSuggestMurderer(){

        GameManager gm = GameManager.getInstance();
        Player p = gm.getCurrentPlayer();
        if(p==null){
            message("please select a player");
            return;
        }

        String msg = " Player="+p.getLabel();

        if(p.getPlace() instanceof Room) {

            SuspectCard s = (SuspectCard) ((ListNode) suggestion_person_combobox.getValue()).getData();
            WeaponCard w = (WeaponCard) ((ListNode) suggestion_weapon_combobox.getValue()).getData();

            RoomCard r = ((Room)p.getPlace()).getCard();

            msg += " Suggestion( room:"+r.name+" weapon:"+w.name+" suspect:"+s.name+" )";

            setAlert(GameManager.getInstance().isGuessCorrect(r,s,w),msg);

        }

            message("please select a player and go to a room");

    }

    @FXML public void playerAccusesMurderer(){

        GameManager gm = GameManager.getInstance();
        Player p = gm.getCurrentPlayer();

        if(p==null){
            message("please select a player");
            return;
        }

        String msg = " Player="+p.getLabel();

        SuspectCard s = (SuspectCard) ((ListNode) accuse_person_combobox.getValue()).getData();
            WeaponCard w = (WeaponCard) ((ListNode) accuse_weapon_combobox.getValue()).getData();

            RoomCard r = (RoomCard) ((ListNode) accuse_room_combobox.getValue()).getData();

        msg += " Suggestion( room:"+r.name+" weapon:"+w.name+" suspect:"+s.name+" )";

        setAlert(GameManager.getInstance().isGuessCorrect(r,s,w),msg);


    }

    private void setAlert(boolean isWinner, String msg){

        try{
            if(isWinner){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Player has won!!!");
            alert.setHeaderText("Winner");
            alert.setContentText(msg);
            alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Player has made the wrong decision!!!");
                alert.setHeaderText("Loser");
                alert.setContentText(msg);
                alert.showAndWait();
            }
        }catch(IllegalStateException err){

        }
    }

    private void message(String msg){

        try{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Message");
                alert.setHeaderText("Alert!");
                alert.setContentText(msg);
                alert.showAndWait();

        }catch(IllegalStateException err){

        }
    }
}
