package runtime.clueless;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeView;
import runtime.clueless.config.Config;

import javafx.stage.Stage;

public class Controller {

    private Stage mainStage = null;
    private static Controller instance = null;
    private boolean VERBOSE = false;

    // singleton
    private Controller() {
        /**
         * Exists only to defeat instantiation.*
         */
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public static Controller ptr() {
        return getInstance();
    }

    // path to property file
    private String main_config_filepath = "";

    public void init(String[] args) {
        // main input
        main_config_filepath = args[0];

        // start the config
        Config.ptr().init(main_config_filepath);

        System.out.println("started Config and Logger ");

        mainStage = Main.mainstage;
    }

    public Stage getStage() {
        return mainStage;
    }

    public void run() {

    }
}
