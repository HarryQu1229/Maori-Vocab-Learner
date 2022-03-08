package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class TopicsController implements Initializable {
	public static Boolean isPractice;
	public static String topic;

	@FXML
	private BorderPane HelpBorderPane;

	@FXML
	private Button helpButton;

	@FXML
	private Label chooseLabel, helpLabel;

	@FXML
	private ComboBox<String> topicBox = new ComboBox<>();

	@FXML
	private AnchorPane rootPane;

	@FXML
	private HBox practiceWordNumberHBox;

	@FXML
	private VBox practiceModeMessageVBox;

	@FXML
	private CheckBox quickPracticeButton, normalPracticeButton, practiceAllButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) { // Initialize
		helpLabel.getStyleClass().add("helplabel");
		chooseLabel.getStyleClass().add("largerlabel");
		rootPane.setOpacity(0);

		ArrayList<String> topicsOutput;

		makeFadeTransition(0, 1);
		try {
			// Reset variables for new game
			WordQuizController.list.clear();
			WordQuizController.words.clear();
			WordQuizController.wordCount = 0;
			WordQuizController.score = 0;
			PracticeQuizController.numberOfWords = 0;
			PracticeQuizController.list.clear();
			PracticeQuizController.words.clear();
			PracticeQuizController.wordCount = 0;
			topic = null;
			WordQuizController.isStart = false;
			PracticeQuizController.isStart = false;

			// Read the files from system and delete . and .. files from output
			topicsOutput = BashProcesses.returnArray("ls -a ./words");
			topicsOutput.remove(0);
			topicsOutput.remove(0);

			// Remove the leading dot and _ from the file names
			ArrayList<String> allTopics = new ArrayList<String>();
			for (String eachTopic : topicsOutput) {
				if (eachTopic.contains("_")) {
					eachTopic = eachTopic.replace("_", " ");
				}
				allTopics.add(eachTopic.substring(1));
			}
			topicBox.getItems().addAll(allTopics);

			// Update the chooseLabel
			topicBox.setOnAction(arg01 -> {
				topic = topicBox.getValue();
				chooseLabel.setText("Topic: " + topic);
				// Adjust to correct filename
				if (topic.contains(" ")) {
					topic = topic.replace(" ", "_");
				}
			});

			// Show the word amount option for practice module
			if (isPractice) {
				practiceWordNumberHBox.setVisible(true);
				practiceModeMessageVBox.setVisible(true);
				
			} else {
				practiceWordNumberHBox.setVisible(false);
				practiceModeMessageVBox.setVisible(false);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to make fade out/in animation of the scene
	 */
	public void makeFadeTransition(int startValue, int endValue) {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(500));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(startValue);
		fadeTransition.setToValue(endValue);
		fadeTransition.play();
	}

	/**
	 * Enter WordQuiz scene by pressing the "Continue" button
	 */
	public void NewGameTopic(ActionEvent event) throws Exception {
		if (topic == null) {
			chooseLabel.setText("You must choose a topic to continue!");

		} else {
			// pass the words to be tested to the WordQuiz/PracticeQuiz Controller
			if (isPractice) {
				// Update the number of words to the right topic when continue is pressed
				if(practiceAllButton.isSelected()) {
					PracticeQuizController.numberOfWords = BashProcesses.returnArray("cat ./words/." + topic).size();
				}
				
				if (PracticeQuizController.numberOfWords == 0) {
					chooseLabel.setText("Please select a practice mode!");

				} else {
					PracticeQuizController.words = BashProcesses.returnArray("cat ./words/." + topic);
					Collections.shuffle(PracticeQuizController.words);

					GeneralMethods gm = new GeneralMethods();
					gm.switchScene(event, "PracticeQuiz.fxml");

					PracticeQuizController.isStart = true;
					// speak first word
					Thread.sleep(500);
					PracticeQuizController.speakWord("1.0", PracticeQuizController.words.get(0));
				}

			} else {
				WordQuizController.words = BashProcesses.returnArray("cat ./words/." + topic);
				Collections.shuffle(WordQuizController.words);

				GeneralMethods gm = new GeneralMethods();
				gm.switchScene(event, "WordQuiz.fxml");

				WordQuizController.isStart = true;
				// speak first word
				Thread.sleep(500);
				WordQuizController.speakWord("1.0", WordQuizController.words.get(0));
			}
		}
	}

	/**
	 * Methods to choose the number of words for practice
	 */
	public void quickPractice(ActionEvent event) throws IOException {
		Boolean isButtonChecked = quickPracticeButton.isSelected();
		if (isButtonChecked) {
			// Quick practice only test on 3 words
			normalPracticeButton.setDisable(true);
			practiceAllButton.setDisable(true);
			PracticeQuizController.numberOfWords = 3;
		} else {
			normalPracticeButton.setDisable(false);
			practiceAllButton.setDisable(false);
			PracticeQuizController.numberOfWords = 0;
		}
	}

	public void normalPractice(ActionEvent event) throws IOException {
		Boolean isButtonChecked = normalPracticeButton.isSelected();
		if (isButtonChecked) {
			// Normal practice only test on 5 words
			quickPracticeButton.setDisable(true);
			practiceAllButton.setDisable(true);
			PracticeQuizController.numberOfWords = 5;
		} else {
			quickPracticeButton.setDisable(false);
			practiceAllButton.setDisable(false);
			PracticeQuizController.numberOfWords = 0;
		}
	}

	public void practiceAll(ActionEvent event) throws IOException {
		Boolean isButtonChecked = practiceAllButton.isSelected();
		if (isButtonChecked) {
			// Practice all test on all words of the word list
			quickPracticeButton.setDisable(true);
			normalPracticeButton.setDisable(true);
		} else {
			quickPracticeButton.setDisable(false);
			normalPracticeButton.setDisable(false);
			PracticeQuizController.numberOfWords = 0;
		}
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
	 * Return to the main menu when the return icon is pressed
	 */
	public void returnMenu(ActionEvent event) throws IOException {
		GeneralMethods gm = new GeneralMethods();
		gm.switchScene(event, "MainMenu.fxml");
	}
}
