package com.epa.erb;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.epa.erb.forms.MainFormController;
import com.epa.erb.forms.AlternativeFormController;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.IdAssignments;
import com.epa.erb.utility.MainPanelHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ERBContainerController implements Initializable {

	private Logger logger;
	private FileHandler fileHandler;
	private MyBreadCrumbBar myBreadCrumbBar;
	private IdAssignments idAssignments = new IdAssignments();
	
	private App app;
	public ERBContainerController(App app) {
		this.app = app;
		
		logger = app.getLogger();
		fileHandler = new FileHandler(app);
	}

	@FXML
	MenuBar menuBar;
	@FXML
	Label titleLabel;
	@FXML
	Rectangle rectangle2;
	@FXML
	VBox erbVBox, erbContainer;
	@FXML
	ImageView erbMiniImageView;
	@FXML
	StackPane erbAboutStackPane;
	@FXML
	Menu faqMenu, resourcesMenu, aboutMenu;
	@FXML
	HBox breadCrumbHBox,headerHBox, erbAboutHBox;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		populateResourceMenu();
		populateFAQMenu();
		populateAboutMenu();
		populateBreadCrumbBar();

		rectangle2.widthProperty().bind(erbVBox.widthProperty().subtract(5.0));
	}

	private void populateResourceMenu() {
		for (String id : idAssignments.getResourceIdAssignments()) {
			for (ERBContentItem erbContentItem : app.getAvailableERBContentItems()) {
				if (id.equals(erbContentItem.getId())) {
//					if (id.equals("201")) {
//						MenuItem menuItem = new MenuItem("Funding and Finance Guide");
//						resourcesMenu.getItems().add(menuItem);
//						File fileToOpen = new File(fileHandler.getTempDirectory() + File.separator + "Funding_and_Financing_Guide.pdf");
//						menuItem.setOnAction(e -> fileHandler.openFileOnDesktop(fileToOpen));
//					} 
//					else {
						String name = erbContentItem.getLongName();
						MenuItem menuItem = createMenuItem(erbContentItem.getId(), name, true);
						resourcesMenu.getItems().add(menuItem);
//					}
				}
			}
		}
	}

	private void populateFAQMenu() {
	  for (String id : idAssignments.getFAQIdAssignments()) {
		  for (ERBContentItem erbContentItem : app.getAvailableERBContentItems()) {
			  if (id.equals(erbContentItem.getId())) {
				  String name = erbContentItem.getLongName();
				  MenuItem menuItem = createMenuItem(erbContentItem.getId(), name, true);
				  faqMenu.getItems().add(menuItem);
			  }
		  }
	  }
//	  MenuItem iconsMenuItem = new MenuItem("Centering Equity Icons");
//	  iconsMenuItem.setId("204");
//	  FormController fController = new FormController(app, app.getEngagementActionController());
//	  iconsMenuItem.setOnAction(e -> fController.loadImagePopUp("204")); 
	}
	
	private void populateAboutMenu() {
		for (ERBContentItem erbContentItem : app.getAvailableERBContentItems()) {
			if (idAssignments.getAboutIdAssignments().contains(erbContentItem.getId())) {
				String name = erbContentItem.getLongName();
				MenuItem menuItem = createMenuItem(erbContentItem.getId(), name, true);
				aboutMenu.getItems().add(menuItem);
			}
		}
	}


	private void populateBreadCrumbBar() {
		MainPanelHandler mainPanelHandler = new MainPanelHandler(app);
		myBreadCrumbBar = new MyBreadCrumbBar(app);
		myBreadCrumbBar.getStylesheets().add(getClass().getResource("/breadCrumb.css").toString());
		myBreadCrumbBar.setStyle("-fx-padding: 3.5 0 0 0");
		String erbLandingString = "ERB Home";
		myBreadCrumbBar.initMyBreadCrumbBar(erbLandingString,
				mainPanelHandler.getMainPanelIdHashMap().get(erbLandingString));
		breadCrumbHBox.getChildren().add(myBreadCrumbBar);
	}

	private MenuItem createMenuItem(String id, String name, boolean isResource) {
		if (id != null && name != null) {
			MenuItem menuItem = new MenuItem(name);
			menuItem.setId(id);
			menuItem.setOnAction(e -> menuItemSelected(menuItem));
			return menuItem;
		} else {
			return null;
		}
	}

	private void menuItemSelected(MenuItem menuItem) {
		if (menuItem != null) {
			File formContentXMLFile = getFormContentXML(menuItem.getId());
			VBox root = loadMainFormContentController(formContentXMLFile);
			Stage stage = new Stage();
			stage.getIcons().add(new Image("/bridge_tool_logo.png"));
			Scene scene = new Scene(root);
			stage.setWidth(app.getPrefWidth());
			stage.setHeight(app.getPrefHeight());
			stage.setScene(scene);
			stage.setTitle("ERBPedia: " + menuItem.getText());
			stage.showAndWait();
		}
	}

	public VBox loadMainFormContentController(File xmlContentFileToParse) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/forms/MainForm.fxml"));
			MainFormController formContentController = new MainFormController(app, xmlContentFileToParse, app.getEngagementActionController());
			fxmlLoader.setController(formContentController);
			VBox root = fxmlLoader.load();
			return root;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to load MainForm.fxml: " + e.getMessage());
			return null;
		}
	}

	public VBox loadAlternativeFormContentController(File xmlContentFileToParse) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/forms/AlternativeForm.fxml"));
			AlternativeFormController formContentController = new AlternativeFormController(app, xmlContentFileToParse, app.getEngagementActionController());
			fxmlLoader.setController(formContentController);
			VBox root = fxmlLoader.load();
			return root;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to load AlternativeForm.fxml: " + e.getMessage());
			return null;
		}
	}

	public void removeHeaderHBox() {
		if (erbVBox.getChildren().contains(headerHBox)) {
			erbVBox.getChildren().remove(headerHBox);
		}
	}

	public void removeERBAboutHBox() {
		if (erbVBox.getChildren().contains(erbAboutStackPane)) {
			erbVBox.getChildren().remove(erbAboutStackPane);
			erbVBox.getChildren().remove(rectangle2);
		}
	}

	public void addHeaderHBox() {
		if (!erbVBox.getChildren().contains(headerHBox)) {
			erbVBox.getChildren().add(0, headerHBox);
		}
	}

	public void addERBAboutHBox() {
		if (!erbVBox.getChildren().contains(erbAboutStackPane)) {
			erbVBox.getChildren().add(1, erbAboutStackPane);
			erbVBox.getChildren().add(2, rectangle2);
		}
	}

	public File getFormContentXML(String id) {
		File xmlFile = fileHandler.getFormContentFileFromResources(id);
		return xmlFile;
	}

	public VBox getErbVBox() {
		return erbVBox;
	}

	public VBox getErbContainer() {
		return erbContainer;
	}

	public MyBreadCrumbBar getMyBreadCrumbBar() {
		return myBreadCrumbBar;
	}

}
