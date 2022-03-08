package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class MainMenuController implements Initializable {

	@FXML
	private AnchorPane rootPane;
	
	@FXML
	private BorderPane HelpBorderPane;
	
	@FXML
	private Button helpButton;
	
	@FXML
	private Label helpLabel;

	
	
	/**
	 * Quit program when press the quit button
	 */
	public void handleCloseButtonAction(ActionEvent event) {
		Platform.exit();
	}

	/**
	 * Enter Topics scene by pressing the "Games module" button in MainMenu
	 */
	public void NewGame(ActionEvent event) throws IOException {
		TopicsController.isPractice = false;
		makeFadeTransition(1, 0, event);
	}

	/**
	 * Enter Topics scene by pressing the "Practice module" button in MainMenu
	 */
	public void PracticeGame(ActionEvent event) throws IOException {
		TopicsController.isPractice = true;
		makeFadeTransition(1, 0, event);
	}

	/**
	 * Method to load the Topics Scene
	 */
	public void loadTopicsScene(ActionEvent event) throws IOException {
		GeneralMethods gm = new GeneralMethods();
		gm.switchScene(event, "Topics.fxml");
	}
	
	/**
	 * Show help when help button is pressed
	 */
	public void help(ActionEvent event) throws IOException {
		HelpBorderPane.setVisible(true);
		helpButton.setVisible(false);
	}
	
	/**
	 * Close help page when close button is pressed
	 */
	public void closeHelp(ActionEvent event) throws IOException {
		HelpBorderPane.setVisible(false);
		helpButton.setVisible(true);
	}

	/**
	 * Method to make fade out/in animation of the scene
	 */
	public void makeFadeTransition(int startValue, int endValue, ActionEvent event) {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(500));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(startValue);
		fadeTransition.setToValue(endValue);
		fadeTransition.play();

		fadeTransition.setOnFinished(arg01 -> {
			try {
				loadTopicsScene(event);
			} catch (IOException e) {
				
			}
		});
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		helpLabel.getStyleClass().add("helplabel");
		
	}
}
