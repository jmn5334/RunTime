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
import javafx.application.Platform;
import runtime.clueless.Controller;
import runtime.clueless.networking.GameClient;


public class MainGUIFXML {
    
    //Jake's code STARTS HERE
    
    //biz logic variables
    private JPlayer player;
    private GameClient gc;
    
    //GUI elements
    //rooms
    @FXML private ListView studyList;
    @FXML private ListView hallList;
    @FXML private ListView loungeList;
    @FXML private ListView libraryList;
    @FXML private ListView billiardRoomList;
    @FXML private ListView diningRoomList;
    @FXML private ListView conservatoryList;
    @FXML private ListView ballroomList;
    @FXML private ListView kitchenList;
    
    //hallways
    @FXML private TextField h0Field;
    @FXML private TextField h1Field;
    @FXML private TextField h2Field;
    @FXML private TextField h3Field;
    @FXML private TextField h4Field;
    @FXML private TextField h5Field;
    @FXML private TextField h6Field;
    @FXML private TextField h7Field;
    @FXML private TextField h8Field;
    @FXML private TextField h9Field;
    @FXML private TextField h10Field;
    @FXML private TextField h11Field;
    
    //combo boxes
    @FXML private ComboBox movePlaceCombo;
    @FXML private ComboBox accusePlaceCombo;
    @FXML private ComboBox cardCombo;
    @FXML private ComboBox suspectSuggestCombo;
    @FXML private ComboBox weaponSuggestCombo;
    @FXML private ComboBox suspectAccuseCombo;
    @FXML private ComboBox weaponAccuseCombo;
    
    //buttons
    @FXML private Button moveButton;
    @FXML private Button suggestButton;
    @FXML private Button accuseButton;
    @FXML private Button endTurnButton;
    @FXML private Button revealButton;
    @FXML private Button hostButton;
    @FXML private Button joinButton;
    @FXML private Button passButton;
    
    
    //txt field
    @FXML private TextField msgField;
    @FXML private TextField nameField;
    @FXML private TextField ipField;
    @FXML private TextField hostPortField;
    @FXML private TextField clientPortField;
    @FXML private TextField charField;
    @FXML private TextField playersField;
    
    @FXML
    public void initialize(){
        
        player = new JPlayer("Test",false);
        gc = new GameClient(player, this);
        
        initComboBoxes();
        refreshBoardLists();
        
        //disable buttons except for host and join
        disableTurnButtons(true);
        disableCardButtons(true);
    }
    
    public void disableAllButtons(boolean areDisabled){
        disableGameButtons(areDisabled);
        disableCardButtons(areDisabled);
        disableTurnButtons(areDisabled);
    }
    
    public void disableGameButtons(boolean areDisabled){
        joinButton.setDisable(areDisabled);
        hostButton.setDisable(areDisabled);
    }
    
    public void disableCardButtons(boolean isDisabled){
        revealButton.setDisable(isDisabled);
        passButton.setDisable(isDisabled);
    }
    
    //true disables buttons, false enables them
    public void disableTurnButtons(boolean areDisabled){
        moveButton.setDisable(areDisabled);
        suggestButton.setDisable(areDisabled);
        accuseButton.setDisable(areDisabled);
        endTurnButton.setDisable(areDisabled);
    }
    
    @FXML
    public void revealCard(){
        gc.revealCard(cardCombo.getSelectionModel().getSelectedItem().toString());
    }
    
    @FXML
    public void pass(){
        gc.pass();
    }
    
    @FXML
    public void accuse(){
        gc.sendAccusation(suspectAccuseCombo.getSelectionModel().getSelectedItem().toString(), weaponAccuseCombo.getSelectionModel().getSelectedItem().toString(), accusePlaceCombo.getSelectionModel().getSelectedItem().toString());
    }
    
    @FXML
    public void joinGame(){
        
        String host = ipField.getText();
        String sport = clientPortField.getText();
        
        if(host.equals("")||sport.equals(""))
            return;
        
        int port = Integer.parseInt(sport);
        
        gc.connectToServer(host,port);
        disableGameButtons(true);
    }
    
    @FXML
    public void hostGame(){
        
    }
    
    @FXML
    public void endTurn(){
        gc.endTurn();
    }
    
    public String getNameFromGUI(){
        nameField.setDisable(true);
        return nameField.getText();
    }
    
    public void setCharacterOnGUI(String c){
        charField.setDisable(false);
        charField.setText(c);
        charField.setDisable(true);
    }
    
    //returns integer >=0 on success, -1 on failure(probably a room)
    private int getHallwayId(String s){
        
        String numString = "";
        
        for(char c : s.toCharArray()){
            if(Character.isDigit(c))
                numString += c;
        }
        
        //if no numbers added return -1
        if(numString.equals("")){
            return -1;
        }
        else{
            return Integer.parseInt(numString);
        }
    }
    
    @FXML
    public void move(){
        
        //get place from combo box
        String destName = movePlaceCombo.getSelectionModel().getSelectedItem().toString();
        
        //determine if room or hallway
        int id = getHallwayId(destName);
        
        if(id == -1){
            JRoom dest = player.getBoard().findRoom(destName);
            
            String msgStr = "player "+player.getName()+"("+player.getSuspect().getName()+") to "+dest.getName()+".";
            
            //send to server first
            gc.sendMove(dest.getName(), -1, true);
        }
        else{
            JHallway dest = player.getBoard().findHallway(id);
            
            String msgStr = "player "+player.getName()+"("+player.getSuspect().getName()+") to hallway "+Integer.toString(dest.getId())+".";
            
            //send to server first
            gc.sendMove("", id, false);
        } 
    }
    
    @FXML
    public void makeSuggestion(){
        
        //get values of the suggestion combo boxes
        String s = suspectSuggestCombo.getSelectionModel().getSelectedItem().toString();
        String w = weaponSuggestCombo.getSelectionModel().getSelectedItem().toString();
        
        JRoom r = player.getSuspect().getRoomLocation();
        
        if(r == null){
            message("Suggestion failed. You must be in a room");
            return;
        }
        
        JSuspect suspect = player.getBoard().findSuspect(s);
        JWeapon weapon = player.getBoard().findWeapon(w);
        
        gc.sendSuggestion(suspect.getName(), weapon.getName(), r.getName()); 
    }
    
    //populates move combo box TODO: remove tag
    public void initComboBoxes(){
        
        ObservableList<String> items1 = FXCollections.observableArrayList();
        ObservableList<String> items2 = FXCollections.observableArrayList();
        ObservableList<String> items3 = FXCollections.observableArrayList();
        ObservableList<String> items4 = FXCollections.observableArrayList();
        ObservableList<String> items5 = FXCollections.observableArrayList();
        
        //populate combos that contain places
        ArrayList<JRoom> rooms;
        ArrayList<JHallway> halls;
        
        movePlaceCombo.getItems().clear();
        accusePlaceCombo.getItems().clear();
        
        rooms = player.getRooms();
        halls = player.getHallways();
        
        //add rooms to items
        for(JRoom r:rooms){
            items1.add(r.getName());
            items2.add(r.getName());
        }
        
        accusePlaceCombo.setItems(items1);
        
        //add hallways to items
        for(JHallway h:halls)
            items2.add("Hallway "+Integer.toString(h.getId()));
        
        movePlaceCombo.setItems(items2);
        
        //populate combos that contain things
        ArrayList<JSuspect> suspects;
        ArrayList<JWeapon> weapons;
        
        suspectAccuseCombo.getItems().clear();
        suspectSuggestCombo.getItems().clear();
        weaponAccuseCombo.getItems().clear();
        weaponSuggestCombo.getItems().clear();
        
        suspects = player.getSuspects();
        weapons = player.getWeapons();
        
        for(JSuspect s: suspects){
            items3.add(s.getName());
        }
        
        for(JWeapon w: weapons){
            items4.add(w.getName());
        }
        
        suspectAccuseCombo.setItems(items3);
        suspectSuggestCombo.setItems(items3);
        weaponAccuseCombo.setItems(items4);
        weaponSuggestCombo.setItems(items4);
        
        //finally we can add cards
        ArrayList<JCard> cards = player.getCards();
        
        //check if cards are null
        if(cards == null)
            return;
        
        cardCombo.getItems().clear();
        
        for(JCard c: cards){
            items5.add(c.getName());
        }
        
        cardCombo.setItems(items5);
       
    }
    
    public void refreshCards(){
        ObservableList<String> items = FXCollections.observableArrayList();
        ArrayList<JCard> cards = player.getCards();
        
        //check if cards are null
        if(cards == null)
            return;
        
        cardCombo.getItems().clear();
        
        for(JCard c: cards){
            items.add(c.getName());
        }
        
        cardCombo.setItems(items);
    }
    
    public void refresh(){
        refreshCards();
        refreshBoardLists();
    }

    //populates all of the board lists using player class TODO: remove tag
    public void refreshBoardLists() {
        
        //populate room lists
        populateRoomList("Study", studyList);
        populateRoomList("Hall", hallList);
        populateRoomList("Lounge", loungeList);
        populateRoomList("Library", libraryList);
        populateRoomList("Billiard Room", billiardRoomList);
        populateRoomList("Dining Room", diningRoomList);
        populateRoomList("Conservatory", conservatoryList);
        populateRoomList("Ballroom", ballroomList);
        populateRoomList("Kitchen", kitchenList);
        
        //set hallway fields
        populateHallwayField(0,h0Field);
        populateHallwayField(1,h1Field);
        populateHallwayField(2,h2Field);
        populateHallwayField(3,h3Field);
        populateHallwayField(4,h4Field);
        populateHallwayField(5,h5Field);
        populateHallwayField(6,h6Field);
        populateHallwayField(7,h7Field);
        populateHallwayField(8,h8Field);
        populateHallwayField(9,h9Field);
        populateHallwayField(10,h10Field);
        populateHallwayField(11,h11Field);
    }

    private void populateHallwayField(int id, TextField hall) {
        
        JHallway ourHall;
        
        //find hallway
        ourHall = player.getBoard().findHallway(id);
        
        if(ourHall == null)
            return;
        
        JSuspect s = ourHall.getSuspect();
        
        //set empty if we have no suspects
        hall.setEditable(true);
        if(s == null)
            hall.clear();
        else
            hall.setText(s.getName());
        hall.setEditable(false);

    }
    
    private void populateRoomList(String name, ListView list){
        
        JRoom ourRoom;
        ObservableList<String> items = FXCollections.observableArrayList();
        
        //make sure we don't see double
        list.getItems().clear();
        
        //find room
        ourRoom = player.getBoard().findRoom(name);
        
        if(ourRoom == null)
            return;
        
        ArrayList<JSuspect> suspects;
        ArrayList<JWeapon> weapons;
        
        suspects = ourRoom.getSuspects();
        weapons = ourRoom.getWeapons();
        
        //add suspects
        for(JSuspect s : suspects){
            items.add(s.getName());
        }
        
        //add weapons
        for(JWeapon w: weapons){
            items.add(w.getName());
        }
        
        //add items
        list.setEditable(true);
        list.getItems().clear();
        list.setItems(items);
        list.setEditable(false);
    }
    
    
    //END Jake's code
    
    


    
    // used by the logger to output status to the gui
    @FXML private WebView status_webview;

    @FXML ComboBox suggestion_person_combobox;
    @FXML ComboBox suggestion_weapon_combobox;
    


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

    public void message(String msg){

        try{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Message");
                alert.setHeaderText("Alert!");
                alert.setContentText(msg);
                alert.showAndWait();

        }catch(IllegalStateException err){
                System.out.println("message error");
        }
    }
    
    public void updateMsgField(String s){
        msgField.setEditable(true);
        msgField.setText(s);
        msgField.setEditable(false);
    }
}

