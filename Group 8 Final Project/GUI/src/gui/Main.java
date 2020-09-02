package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * UW-Madison, CS 564, Summer 2020, Team 8 Final Project: Game Ranking Database
 * GUI/Front End by @author Nate Sackett Requires JavaFX and mysql-connector;
 * FXML GUI design implemented using SceneBuilder
 * 
 * Main really only used to call FXMLLoader for SceneBuilder implementation
 */
public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			stage.setTitle("GUI");
			stage.setScene(new Scene(root, 958, 660));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
