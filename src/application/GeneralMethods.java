package application;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * This class contains methods that can be used in multiple scenes
 */
public class GeneralMethods {

	/**
	 * This method will be called to change to a certain scene
	 */
	public void switchScene(ActionEvent event, String sceneName) { // Goes to choose topic screen
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource(sceneName));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
		}
		
	}
}
