package runtime.clueless;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import javafx.concurrent.Service;
import javafx.concurrent.Task;


public class Main extends Application {

    public static Stage mainstage = null;
    public static String[] program_args = null;

    /**
     *  Entry point for the program
     *
     *  The program requires the config filepath as an argument
     * @param args
     */
    public static void main(String[] args) {

        if(args.length<1 || args.length>1){
            System.out.println(" ---- HELP MENU -------");
            System.out.println(" workstation#> clueless [config file path] ");
            System.out.println("         [config file path] = absolute path to where the clueless.properties file is located");
            System.out.println(" ----------------------");
        }

        program_args = args;

        launch(args);


        System.out.println(" Clueless : Exiting program gracefully");
    }

    /**
     * This function starts the main gui and starts the controller
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        initProgram();

        // set the main stage
        mainstage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        mainstage.setTitle("Clueless");
        mainstage.setScene(new Scene(root, 768, 658));
        //mainstage.setMaxHeight(2500);
        mainstage.setMinHeight(400);
        //mainstage.setMaxWidth(640);
        //mainstage.setMinWidth(500);
        mainstage.setMinWidth(950);
        mainstage.show();

        // load the main icon for the application
        mainstage.getIcons().add(new Image("/icons/time.png"));
        //setUserAgentStylesheet(STYLESHEET_CASPIAN);
        //initProgram();
    }

    public void initProgram(){
        /**
         *  If the user doesn't specify a property file, prompt for one
         */
        if(program_args.length!=1) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Clueless properties file");
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
            );
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Files", "*.*"),
                    new FileChooser.ExtensionFilter("Properties", "*.properties"),
                    new FileChooser.ExtensionFilter("TXT", "*.txt")
            );
            File fid = fileChooser.showOpenDialog(mainstage);
            if (fid != null) {
                program_args = new String[1];
                program_args[0] = fid.getAbsolutePath();
            }

        }
        // start the controller
        Controller.ptr().init(program_args);

        Controller.ptr().run();

        // start the logger
        System.out.println(" Starting GUI ");

    }
/*
    public static class NetworkDaemon extends Service<Void> {

        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {

                private int clientCount = 0;

                @Override
                protected Void call() throws Exception {


                        final String clientName = "Client " + (++clientCount);
                        final int connectTime = rng.nextInt(3000) + 4000;
                        Thread clientThread = new Thread(() -> {
                            try {
                                Thread.sleep(connectTime);
                            } catch (InterruptedException exc) {
                                exc.printStackTrace();
                            }
                            Platform.runLater(() -> {
                                disconnectedNotification.accept(clientName);
                            });
                        });
                        clientThread.setDaemon(true);
                        Platform.runLater(() -> {
                            connectedNotification.accept(clientName);
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setContentText(clientName + " connected");
                            alert.show();
                        });
                        clientThread.start();
                    

                    return null;
                }

            };
        }

    }
*/

}
