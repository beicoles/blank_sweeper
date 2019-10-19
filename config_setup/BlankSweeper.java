import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BlankSweeper extends Application {

    private final String layoutFile = "App.fxml";

    public void start(Stage primaryStage) throws IOException {

        Parent fxmlRoot = FXMLLoader.load(getClass().getResource(layoutFile));
        Scene scene = new Scene(fxmlRoot);
        primaryStage.setScene(scene);
    	primaryStage.setTitle("BlankSweeper");
        primaryStage.show();
    }

    public static void main(String[] args){
		launch(args);
	}
}