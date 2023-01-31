/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.epa.erb;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.goal.Goal;
import com.epa.erb.goal.GoalCategory;
import com.epa.erb.project.Project;
import com.epa.erb.utility.Constants;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.MainPanelHandler;
import com.epa.erb.utility.XMLManager;

public class App extends Application {

	private int prefWidth;
	private int prefHeight;
	private Project selectedProject;
	private ERBContainerController erbContainerController;
	private EngagementActionController engagementActionController;

	private ArrayList<Project> projects;
	private ArrayList<Chapter> availableChapters;
	private ArrayList<ContentPanel> availableContentPanels;
	private ArrayList<GoalCategory> availableGoalCategories;

	private FileHandler fileHandler = new FileHandler();
	private XMLManager xmlManager = new XMLManager(this);
	private Logger logger = LogManager.getLogger(App.class);

	@Override
	public void start(Stage primaryStage) throws Exception {
		setScreenSizePreferences(getScreenScale());
		readAndStoreData();
		showERBToolMain();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	private void showERBToolMain() {
		Parent erbContainerRoot = loadERBContainer();
		launchERBLanding();
		showERBContainer(erbContainerRoot);
	}

	private int getScreenScale() {
		return java.awt.Toolkit.getDefaultToolkit().getScreenResolution();
	}

	private void setScreenSizePreferences(int scale) {
		Constants constants = new Constants();
		if (scale > 0 && scale <= 96) {
			prefWidth = constants.getPrefWidthForScale100();
			prefHeight = constants.getPrefHeightForScale100();
		} else if (scale > 96 && scale <= 125) {
			prefWidth = constants.getPrefWidthForScale125();
			prefHeight = constants.getPrefHeightForScale125();
		} else if (scale > 125 && scale <= 144) {
			prefWidth = constants.getPrefWidthForScale150();
			prefHeight = constants.getPrefHeightForScale150();
		} else {
			prefWidth = constants.getPrefWidthForScale175();
			prefHeight = constants.getPrefHeightForScale175();
		}
	}

	public void launchERBLanding() {
		MainPanelHandler mainPanelHandler = new MainPanelHandler();
		Parent erbLandingRoot = mainPanelHandler.loadERBLanding(this);
		loadNodeToERBContainer(erbLandingRoot);
	}

	private Parent loadERBContainer() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/ERBContainer.fxml"));
			erbContainerController = new ERBContainerController(this);
			fxmlLoader.setController(erbContainerController);
			ScrollPane root = fxmlLoader.load();
			root.setPrefWidth(getPrefWidth());
			root.setPrefHeight(getPrefHeight());
			return root;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	private Stage erbContainerStage = null;

	private void showERBContainer(Parent erbContainerRoot) {
		if (erbContainerRoot != null) {
			erbContainerStage = new Stage();
			Scene scene = new Scene(erbContainerRoot);
			erbContainerStage.setScene(scene);
			erbContainerStage.setOnCloseRequest(e -> erbCloseRequested());
			erbContainerStage.setTitle("ERB: Equitable Resilience Builder");
			erbContainerStage.show();
		} else {
			logger.error("Cannot show ERBContainer. erbContainerRoot is null.");
		}
	}

	private void erbCloseRequested() {
		if (engagementActionController != null) {
			Project project = engagementActionController.getProject();
			Goal goal = engagementActionController.getCurrentGoal();
			ArrayList<Chapter> listOfUniqueChapters = engagementActionController.getListOfUniqueChapters();
			xmlManager.writeGoalMetaXML(fileHandler.getGoalMetaXMLFile(project, goal), listOfUniqueChapters);
			fileHandler.createGUIDDirectoriesForGoal(project, goal, listOfUniqueChapters);
		}
	}

	private void readAndStoreData() {
		readAndStoreAvailableContent();
		readAndStoreAvailableChapters();
		readAndStoreAvailableGoalCategories();
		readAndStoreProjects();
	}

	private void readAndStoreAvailableContent() {
		File contentFile = fileHandler.getStaticAvailableContentXMLFile();
		availableContentPanels = xmlManager.parseAvailableContentXML(contentFile);
	}

	private void readAndStoreAvailableChapters() {
		File chaptersFile = fileHandler.getStaticChaptersXMLFile();
		availableChapters = xmlManager.parseChaptersXML(chaptersFile);
	}

	private void readAndStoreAvailableGoalCategories() {
		File goalCategoriesFile = fileHandler.getStaticGoalCategoriesXMLFile();
		availableGoalCategories = xmlManager.parseGoalCategoriesXML(goalCategoriesFile);
	}

	private void readAndStoreProjects() {
		File projectsDirectory = fileHandler.getProjectsDirectory();
		projects = xmlManager.parseAllProjects(projectsDirectory);
	}

	public Project getExploreProject() {
		for (Project project : projects) {
			if (project.getProjectName().contentEquals("Explore")) {
				return project;
			}
		}
		return null;
	}

	//TODO:Move to utility
	public void loadNodeToERBContainer(Node node) {
		if (node != null) {
			setNodeGrowPriority(node, Priority.ALWAYS);
			erbContainerController.getErbContainer().getChildren().clear();
			erbContainerController.getErbContainer().getChildren().add(node);
		} else {
			logger.error("Cannot load node to ERBContainer. node is null.");
		}
	}

	//TODO:Move to utility
	public void setNodeGrowPriority(Node node, Priority priority) {
		if (node != null) {
			VBox.setVgrow(node, priority);
			HBox.setHgrow(node, priority);
		}
	}

	public void updateAvailableProjectsList() {
		readAndStoreProjects();
	}

	//TODO:Move to utility
	public String generateGUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public int getPrefWidth() {
		return prefWidth;
	}

	public void setPrefWidth(int prefWidth) {
		this.prefWidth = prefWidth;
	}

	public int getPrefHeight() {
		return prefHeight;
	}

	public void setPrefHeight(int prefHeight) {
		this.prefHeight = prefHeight;
	}

	public Project getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(Project selectedProject) {
		this.selectedProject = selectedProject;
	}

	public ArrayList<Project> getProjects() {
		return projects;
	}

	public void setProjects(ArrayList<Project> projects) {
		this.projects = projects;
	}

	public ArrayList<Chapter> getAvailableChapters() {
		return availableChapters;
	}

	public void setAvailableChapters(ArrayList<Chapter> chapters) {
		this.availableChapters = chapters;
	}

	public ArrayList<GoalCategory> getAvailableGoalCategories() {
		return availableGoalCategories;
	}

	public ArrayList<ContentPanel> getAvailableContentPanels() {
		return availableContentPanels;
	}

	public void setAvailableGoalCategories(ArrayList<GoalCategory> goalCategories) {
		this.availableGoalCategories = goalCategories;
	}

	public ERBContainerController getErbContainerController() {
		return erbContainerController;
	}

	public void setErbContainerController(ERBContainerController erbContainerController) {
		this.erbContainerController = erbContainerController;
	}

	public Stage getErbContainerStage() {
		return erbContainerStage;
	}

	public void setErbContainerStage(Stage erbContainerStage) {
		this.erbContainerStage = erbContainerStage;
	}

	public EngagementActionController getEngagementActionController() {
		return engagementActionController;
	}

	public void setEngagementActionController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}

}