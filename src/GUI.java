import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GUI extends Application {
    void instantiate(String[] args){
    launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent gui = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        Scene s = new Scene(gui);
        primaryStage.setTitle("Welcome Elevator Simulator");
        primaryStage.setScene(s);

        primaryStage.show();

      primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent t) {
          Platform.exit();
          System.exit(0);
        }
      });

    }
}
