package gov.epa.equitableresiliencebuilder.noteboard;

import java.net.URL;
import java.util.ResourceBundle;

import gov.epa.equitableresiliencebuilder.utility.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class NoteBoardItemController implements Initializable{
	
	private Stage editsStage = null;
	private Constants constants = new Constants();
	
	protected NoteBoardContentController noteBoardContentController;
	public NoteBoardItemController(NoteBoardContentController noteBoardContentController ) {
		this.noteBoardContentController = noteBoardContentController;
	}
	
	@FXML
	VBox mainVBox;
	@FXML
	HBox flipHBox;
	@FXML
	Label numberLabel;
	@FXML
	TextFlow textFlow;
	@FXML
	ImageView imageView;
	@FXML
	ScrollPane scrollPane;
	@FXML
	VBox noteBoardItemVBox;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initPostItNote();	
		handleControls();
	}
	
	private void initPostItNote() {
		noteBoardItemVBox.setPrefWidth(0);
		noteBoardItemVBox.setId("noteBoardItem");
	}
	
	private void handleControls() {
		textFlow.setStyle("-fx-background-color: " + constants.getPostItNoteColor() + ";");
		noteBoardItemVBox.setStyle("-fx-background-color: " + constants.getPostItNoteColor() + ";");
	}
	

	void setNumberLabelText(String text) {
		numberLabel.setText(text);
	}

	public Stage getEditsStage() {
		return editsStage;
	}

	public void setEditsStage(Stage editsStage) {
		this.editsStage = editsStage;
	}

	public Label getNumberLabel() {
		return numberLabel;
	}

	public TextFlow getTextFlow() {
		return textFlow;
	}

}
