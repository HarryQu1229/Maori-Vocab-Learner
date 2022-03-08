package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PracticeQuizController implements Initializable {
	public static List<String> words = new ArrayList<>();
	public static int wordCount = 0;
	public static int numberOfWords;
	private static double sliderValue;
	public static Boolean isStart;
	public static ObservableList<TableValues> list = FXCollections.observableArrayList();
	private Boolean isFirstAttempt = true;
	private Timeline timeline;
	private double timerProgress = 1.0;
	private int timerLabelValue = 25;

	@FXML
	private BorderPane HelpBorderPane, speedBorderPane;

	@FXML
	private Button checkButton, speakButton, dontKnowButton, helpButton;

	@FXML
	private Label progressLabel, speedLabel, hintLabel, encourageLabel, spellSpokenLabel, lengthLabel, timerLabel,
			resultLabel, helpLabel;

	@FXML
	private TextField answerText;

	@FXML
	private Slider speedSlider;

	@FXML
	private ProgressBar timerProgressBar;

	@FXML
	private ButtonBar macronButtonBar;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		spellSpokenLabel.getStyleClass().add("largerlabel");
		hintLabel.getStyleClass().add("greenlabel");
		encourageLabel.getStyleClass().add("greenlabel");
		helpLabel.getStyleClass().add("helplabel");
		timerLabel.getStyleClass().add("timerlabel");
		

		showWord();
		progressLabel.setText("Word 1 of " + numberOfWords);
		try {
			// set up the slider and display the value
			speedSlider.valueProperty().addListener(new ChangeListener<Number>() {

				public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
					sliderValue = speedSlider.getValue();
					speedLabel.setText("Speed: " + String.format("%.2f", sliderValue) + "x");

				}
			});
			// set up the timer
			setTimer();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to set up the timer for each word
	 */
	private void setTimer() throws InterruptedException {
		// Assign the initial values before the timer countdown
		timerProgress = 1.0;
		timerLabelValue = 25;

		this.timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
			timerProgress -= 0.04;
			timerLabelValue--;
			timerProgressBar.setProgress(timerProgress);
			timerLabel.setText(String.valueOf(timerLabelValue) + "s");

			// If the time is up and the user still haven't pressed checked or don't know
			// button
			if ((timerLabelValue == 0) && (isStart)) {
				try {
					timeline.stop();
					answerText.setText("");
					if (wordCount == numberOfWords) {
						switchToRewardScene();
					} else {
						list.add(new TableValues(words.get(wordCount), answerText.getText(), "Time Ran Out"));
						setLabelTexts("You can be faster!", "Time Ran Out");
						updateToNextWord();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}));
		// The timer counts down for 25 times (seconds)
		timeline.setCycleCount(25);
		timeline.play();
	}

	/**
	 * Jump to next word or finishes when press the I don't know button
	 */
	public void dontKnow(ActionEvent event) throws IOException, InterruptedException {
		// stop the timer for previous word
		timeline.stop();
		answerText.setText("");
		list.add(new TableValues(words.get(wordCount), "", "You skipped this word"));
		setLabelTexts("Keep it up! You can do this!", "");
		updateToNextWord();
	}

	/**
	 * Check whether entered word is correct when check is pressed
	 */
	public void check(ActionEvent event) throws Exception {
		// stop the timer for previous word
		timeline.stop();

		// get word from text field
		String currentWord = answerText.getText();
		answerText.setText("");

		// word is right on first attempt
		if (currentWord.toLowerCase().equals(words.get(wordCount).toLowerCase()) && isFirstAttempt) {
			list.add(new TableValues(words.get(wordCount), currentWord, "Correct!"));
			setLabelTexts("", "Correct!");
			updateToNextWord();

			// word is right on second attempt
		} else if (currentWord.toLowerCase().equals(words.get(wordCount).toLowerCase()) && !isFirstAttempt) {
			list.add(new TableValues(words.get(wordCount), currentWord, "Correct on the 2nd try!"));
			setLabelTexts("", "Correct!");
			updateToNextWord();

			// word wrong on first attempt
		} else if (!currentWord.toLowerCase().equals(words.get(wordCount).toLowerCase()) && isFirstAttempt) {
			isFirstAttempt = false;
			setLabelTexts("Keep up! You can do this!", "Incorrect, try again");
			// Show the word with added letters
			showWordWithAddedLetters();

			setTimer();

			// wrong on second attempt
		} else if (!currentWord.toLowerCase().equals(words.get(wordCount).toLowerCase()) && !isFirstAttempt) {
			list.add(new TableValues(words.get(wordCount), currentWord, "Incorrect"));
			setLabelTexts("", "Incorrect, the word is " + words.get(wordCount));
			updateToNextWord();
		}

	}

	/**
	 * Set text for corresponding labels
	 */
	public void setLabelTexts(String encourageLabelText, String resultLabelText) {
		encourageLabel.setText(encourageLabelText);
		resultLabel.setText(resultLabelText);
	}

	/**
	 * Check whether there are more words to go through, speak the word is there is,
	 * or switch to reward scene if not.
	 */
	public void updateToNextWord() throws IOException, InterruptedException {
		wordCount++;
		isFirstAttempt = true;

		Thread.sleep(1000);

		if (wordCount < numberOfWords) {
			progressLabel.setText("Word " + (wordCount + 1) + " of " + numberOfWords);
			showWord();
			speakWord(String.format("%.2f", 1.0 / (speedSlider.getValue())), words.get(wordCount));
			setTimer();

		} else {
			switchToRewardScene();
		}
	}

	/**
	 * Speaks the word once the speak button is pressed
	 */
	public void speakWordButton(ActionEvent event) throws IOException {
		speakWord(String.format("%.2f", 1.0 / (speedSlider.getValue())), words.get(wordCount));
	}

	/**
	 * Create scm file to call festival to speak the word at required speed
	 */
	public static void speakWord(String currentSpeed, String currentWord) {
		BashProcesses.sendProcesses(">.speakWord.scm");
		BashProcesses.sendProcesses("echo \"(voice_akl_mi_pk06_cg)\">>.speakWord.scm");
		BashProcesses.sendProcesses("echo \"(Parameter.set 'Duration_Stretch " + currentSpeed + ")\">>.speakWord.scm");
		BashProcesses.sendProcesses("echo \'(SayText \"" + currentWord + "\")\'>>.speakWord.scm");
		BashProcesses.sendProcesses("festival -b .speakWord.scm");
	}

	/**
	 * Indicate the user how many characters to type
	 */
	public void showWord() {
		String output = "";
		for (char c : words.get(wordCount).toCharArray()) {
			if (c == ' ') {
				output += "   ";
			} else if (c == '-') {
				output += "-";
			} else {
				output += "_ ";
			}
		}
		lengthLabel.setText(output);
	}

	/**
	 * Show the user how many characters to type with added letters
	 */
	public void showWordWithAddedLetters() {
		String output = "";
		int temp = 0;
		for (char c : words.get(wordCount).toCharArray()) {
			if (c == ' ') {
				output += "   ";
			} else if (c == '-') {
				output += "-";
			} else if (temp % 2 == 0) {
				output += "_ ";
			} else {
				output += Character.toLowerCase(words.get(wordCount).charAt(temp)) + " ";
			}
			temp++;
		}
		lengthLabel.setText(output);
	}

	/**
	 * return to main menu from reward scene
	 */
	public void switchToRewardScene() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("RewardScene.fxml"));
		Scene scene = new Scene(root);
		Stage stage = (Stage) timerLabel.getScene().getWindow();
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
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
		timeline.stop();
		GeneralMethods gm = new GeneralMethods();
		gm.switchScene(event, "MainMenu.fxml");
	}

	/**
	 * This method opens/closes the borderpane for changing speed when the speed
	 * toggle button is pressed
	 */
	public void toggleSpeedBorderPane(ActionEvent event) throws IOException {
		if (speedBorderPane.isVisible()) {
			speedBorderPane.setVisible(false);
		} else {
			speedBorderPane.setVisible(true);
		}
	}

	/**
	 * This method opens/closes the ButtonBar for macron buttons
	 */
	public void toggleMacronButtonBar(ActionEvent event) throws IOException {
		if (macronButtonBar.isVisible()) {
			macronButtonBar.setVisible(false);
		} else {
			macronButtonBar.setVisible(true);
		}
	}

	/**
	 * Enter the macrons into the answer Text
	 */
	public void ōEnter(ActionEvent event) {
		answerText.appendText("ō");
	}

	public void āEnter(ActionEvent event) {
		answerText.appendText("ā");
	}

	public void ēEnter(ActionEvent event) {
		answerText.appendText("ē");
	}

	public void ūEnter(ActionEvent event) {
		answerText.appendText("ū");
	}

	public void īEnter(ActionEvent event) {
		answerText.appendText("ī");
	}
}