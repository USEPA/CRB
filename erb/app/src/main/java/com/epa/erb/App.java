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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.engagement_action.SaveHandler;
import com.epa.erb.goal.Goal;
import com.epa.erb.goal.GoalCategory;
import com.epa.erb.project.Project;
import com.epa.erb.utility.Constants;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.XMLManager;

public class App extends Application {

	private int prefWidth;
	private int prefHeight;
	private Project selectedProject;
	private ArrayList<Project> projects;
	private ArrayList<Chapter> chapters;
	private ArrayList<Activity> activities;
	private Constants constants = new Constants();
	private FileHandler fileHandler = new FileHandler();
	private ArrayList<ActivityType> activityTypes;
	private ArrayList<GoalCategory> goalCategories;
	private XMLManager xmlManager = new XMLManager(this);
	private ERBContainerController erbContainerController;
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
		launchERBLandingNew2();
		showERBContainer(erbContainerRoot);
	}
	
	private int getScreenScale() {
		return java.awt.Toolkit.getDefaultToolkit().getScreenResolution();
	}
	
	private void setScreenSizePreferences(int scale) {
		if(scale > 0 && scale <=96) {
			prefWidth = constants.getPrefWidthForScale100();
			prefHeight = constants.getPrefHeightForScale100();
		} else if (scale > 96 && scale <=125) {
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
	
	public void launchERBLandingNew2() {
		Parent erbLandingNew2Root = loadERBLandingNew2();
		loadNodeToERBContainer(erbLandingNew2Root);
	}
	
	private Parent loadERBLandingNew2() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/ERBLandingNew2.fxml"));
			ERBLandingNew2Controller erbLandingNew2Controller = new ERBLandingNew2Controller(this);
			fxmlLoader.setController(erbLandingNew2Controller);
			return fxmlLoader.load();
		} catch (Exception e) {
			return null;
		}
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
		initSaveHandler(null, null, null, null, getProjects(), "close");
		if(areEmptyProjects()) {
			deleteEmptyProjectDirectories();
		}
	}
	
	private void deleteEmptyProjectDirectories() {
		File projectsDirectory = fileHandler.getProjectsDirectory();
		if (projectsDirectory != null && projectsDirectory.exists()) {
			File[] projectDirectories = projectsDirectory.listFiles();
			for (File projDir : projectDirectories) {
				if (projDir.isDirectory()) {
					File projectMetaFile = fileHandler.getProjectMetaXMLFile(selectedProject);
					if (projectMetaFile == null || !projectMetaFile.exists()) {
						fileHandler.deleteDirectory(projDir);
 					}
				}
			}
		}
	}
	
	private boolean areEmptyProjects() {
		File projectsDirectory = fileHandler.getProjectsDirectory();
		if (projectsDirectory != null && projectsDirectory.exists()) {
			File[] projectDirectories = projectsDirectory.listFiles();
			for (File projDir : projectDirectories) {
				if (projDir.isDirectory()) {
					File projectMetaFile = fileHandler.getProjectMetaXMLFile(selectedProject);
					if (projectMetaFile == null || !projectMetaFile.exists()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void initSaveHandler(Activity activity, Chapter chapter, Goal goal, Project project, ArrayList<Project> projects, String saveOrigin) {
		SaveHandler saveHandler = new SaveHandler(this, saveOrigin, activity, chapter, goal, project, projects);
		saveHandler.beginSave();
	}	
	
	private void readAndStoreData() {	
		readAndStoreChapters();
		readAndStoreActivityTypes();
		readAndStoreAvailableActivities();
		readAndStoreGoalCategories();
		readAndStoreProjects();
	}
	
	private void readAndStoreChapters() {
		File chaptersFile = fileHandler.getChaptersFileToParse();
		chapters = xmlManager.parseChaptersXML(chaptersFile);
	}
	
	private void readAndStoreActivityTypes() {
		File activityTypesFile = fileHandler.getActivityTypesFileToParse();
		activityTypes = xmlManager.parseActivityTypesXML(activityTypesFile);
	}
	
	private void readAndStoreAvailableActivities() {
		File availableActivitiesFile = fileHandler.getAvailableActivitiesFileToParse();
		activities = xmlManager.parseAvailableActivitiesXML(availableActivitiesFile, activityTypes);
	}
	
	private void readAndStoreGoalCategories() {
		File goalCategoriesFile = fileHandler.getGoalCategoriesFileToParse();
		goalCategories = xmlManager.parseGoalCategoriesXML(goalCategoriesFile);
	}
	
	private void readAndStoreProjects() {
		File projectsDirectory = fileHandler.getProjectsDirectory();
		projects = xmlManager.parseAllProjects(projectsDirectory, activities);
	}
	
	public void loadNodeToERBContainer(Node node) {
		if (node != null) {
			setNodeGrowPriority(node, Priority.ALWAYS);
			erbContainerController.getErbContainer().getChildren().clear();
			erbContainerController.getErbContainer().getChildren().add(node);
		} else {
			logger.error("Cannot load node to ERBContainer. node is null.");
		}
	}
	
	public void setNodeGrowPriority(Node node, Priority priority) {
		if (node != null) {
			VBox.setVgrow(node, priority);
			HBox.setHgrow(node, priority);
		}
	}
	
	public void updateAvailableProjectsList() {
		readAndStoreProjects();
	}
	
	public Activity getActivity(String activityID, ArrayList<Activity> activities) {
		if (activityID != null && activities != null) {
			for (Activity activity : activities) {
				if (activity.getActivityID().contentEquals(activityID)) {
					return activity;
				}
			}
		} else {
			logger.error("Cannot get activity. activityID or activities is null.");
			return null;
		}
		logger.debug("Cannot get activity. Activity returned is null");
		return null;

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

	public ArrayList<Chapter> getChapters() {
		return chapters;
	}

	public void setChapters(ArrayList<Chapter> chapters) {
		this.chapters = chapters;
	}

	public ArrayList<Activity> getActivities() {
		return activities;
	}

	public void setActivities(ArrayList<Activity> activities) {
		this.activities = activities;
	}

	public ArrayList<ActivityType> getActivityTypes() {
		return activityTypes;
	}

	public void setActivityTypes(ArrayList<ActivityType> activityTypes) {
		this.activityTypes = activityTypes;
	}

	public ArrayList<GoalCategory> getGoalCategories() {
		return goalCategories;
	}

	public void setGoalCategories(ArrayList<GoalCategory> goalCategories) {
		this.goalCategories = goalCategories;
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

}