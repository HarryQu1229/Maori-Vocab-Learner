package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class RewardSceneController implements Initializable {
	
	@FXML
	private Label RewardLabel;

	@FXML
	private Label scoreLabel;

	@FXML
	private TableView<TableValues> resultTable;

	@FXML
	private TableColumn<TableValues, String> wordColumn;

	@FXML
	private TableColumn<TableValues, String> userAnswerColumn;

	@FXML
	private TableColumn<TableValues, String> resultColumn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		RewardLabel.getStyleClass().add("largerlabel");
		wordColumn.setCellValueFactory(new PropertyValueFactory<TableValues, String>("word"));
		userAnswerColumn.setCellValueFactory(new PropertyValueFactory<TableValues, String>("userAnswer"));
		resultColumn.setCellValueFactory(new PropertyValueFactory<TableValues, String>("Result"));

		if (TopicsController.isPractice) {
			scoreLabel.setText("");
			resultTable.setItems(PracticeQuizController.list);
		} else {
			scoreLabel.setText("Your score is "+WordQuizController.score+" out of 500");
			resultTable.setItems(WordQuizController.list);
		}
	}

	// enter Topics scene by pressing the "Play Again" button in rewardScne
	public void NewGame(ActionEvent event) throws IOException { // Goes to choose topic screen
		GeneralMethods gm = new GeneralMethods();
		gm.switchScene(event, "Topics.fxml");
	}

	// return to main menu from reward scene
	public void switchToMain(ActionEvent event) throws IOException { // Returns back to main page.
		GeneralMethods gm = new GeneralMethods();
		gm.switchScene(event, "MainMenu.fxml");
	}
	
}
