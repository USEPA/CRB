package com.epa.erb;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ERBContainerController implements Initializable{
	
	@FXML
	ScrollPane erbScrollPane;
	@FXML
	VBox welcomeVBox;
	@FXML
	VBox erbContainer;
	
	private App app;
	public ERBContainerController(App app) {
		this.app = app;
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(ERBContainerController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
	}
	
	private void handleControls() {
		welcomeVBox.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");		
		erbScrollPane.heightProperty().addListener(e-> heightChanged());
	}
	
	private void heightChanged() {
		if (erbContainer.getChildren().size() > 0) {
			VBox vBox = (VBox) erbContainer.getChildren().get(0);
			vBox.setMinHeight(erbScrollPane.getHeight() - 50);
		}
	}
	
	@FXML
	public void aboutMenuItemAction() {
		loadERBLandingToContainer();
		app.loadSavePopup(null, null, null, app.getSelectedProject(), null, "projectChange");
	}
	
	private void loadERBLandingToContainer() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/ERBLanding.fxml"));
			ERBLandingController erbLandingController = new ERBLandingController(app);
			fxmlLoader.setController(erbLandingController);
			app.loadContent(fxmlLoader.load());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@FXML
	public void glossaryMenuItemAction() {
		loadGlossary();
	}
	
	private void loadGlossary() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/Glossary.fxml"));
			GlossaryController glossaryController = new GlossaryController();
			fxmlLoader.setController(glossaryController);
			Parent root = fxmlLoader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("ERB: Glossary");
			stage.showAndWait();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public VBox getErbContainer() {
		return erbContainer;
	}
	
	public ScrollPane getERBScrollPane() {
		return erbScrollPane;
	}

}
