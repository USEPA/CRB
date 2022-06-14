package com.epa.erb;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.project.ProjectSelectionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ERBLandingController implements Initializable {

	@FXML
	Circle circle1;
	@FXML
	Circle circle2;
	@FXML
	Circle circle3;
	@FXML
	VBox vBox1;
	@FXML
	VBox vBox2;
	@FXML
	VBox vBox3;
	@FXML
	VBox vBox1Content;
	@FXML
	VBox vBox2Content;
	@FXML
	VBox vBox3Content;


	private App app;
	public ERBLandingController(App app) {
		this.app = app;
	}

	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(ERBLandingController.class);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
	}

	private void handleControls() {
		circle1.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		circle2.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		circle3.setStyle("-fx-fill: " + constants.getAllChaptersColor() + ";");
		hideContent1();
		hideContent2();
		hideContent3();
	}

	@FXML
	public void beginERBProcessButtonClicked() {
		loadProjectSelectionToContainer();
	}
	
	private void loadProjectSelectionToContainer() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/ProjectSelection.fxml"));
			ProjectSelectionController projectSelectionController = new ProjectSelectionController(app);
			fxmlLoader.setController(projectSelectionController);
			app.loadContent(fxmlLoader.load());			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@FXML
	public void circle1Clicked() {
		if(isCircle1ContentShowing()) {
			hideContent1();
		} else {
			showContent1();
		}
	}
	
	@FXML
	public void circle2Clicked() {
		if(isCircle2ContentShowing()) {
			hideContent2();
		} else {
			showContent2();
		}
	}
	
	@FXML
	public void circle3Clicked() {
		if(isCircle3ContentShowing()) {
			hideContent3();
		} else {
			showContent3();
		}
	}
	
	private void hideContent1() {
		if(vBox1.getChildren().contains(vBox1Content)) {
			vBox1.getChildren().remove(vBox1Content);
		}
	}
	
	private void showContent1() {
		if(!vBox1.getChildren().contains(vBox1Content)) {
			vBox1.getChildren().add(1,vBox1Content);
		}
	}
	
	private boolean isCircle1ContentShowing() {
		if(vBox1.getChildren().contains(vBox1Content)) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isCircle2ContentShowing() {
		if(vBox2.getChildren().contains(vBox2Content)) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isCircle3ContentShowing() {
		if(vBox3.getChildren().contains(vBox3Content)) {
			return true;
		} else {
			return false;
		}
	}
	
	private void hideContent2() {
		if(vBox2.getChildren().contains(vBox2Content)) {
			vBox2.getChildren().remove(vBox2Content);
		}
	}
	
	private void showContent2() {
		if(!vBox2.getChildren().contains(vBox2Content)) {
			vBox2.getChildren().add(1,vBox2Content);
		}
	}
	
	private void hideContent3() {
		if(vBox3.getChildren().contains(vBox3Content)) {
			vBox3.getChildren().remove(vBox3Content);
		}
	}
	
	private void showContent3() {
		if(!vBox3.getChildren().contains(vBox3Content)) {
			vBox3.getChildren().add(1,vBox3Content);
		}
	}

}
