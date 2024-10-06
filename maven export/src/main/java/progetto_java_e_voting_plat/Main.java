package progetto_java_e_voting_plat;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primapryStage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
		primapryStage.setTitle("E-voting plat Login");
		primapryStage.setResizable(false);
		primapryStage.setScene(new Scene(root, 428, 349));
		primapryStage.show();
		
	}

	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		launch(args);
	}


}