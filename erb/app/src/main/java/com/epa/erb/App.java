/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.epa.erb;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

public class App extends Application {

	private ArrayList<Project> projects;
	private ArrayList<Chapter> chapters;
	private ArrayList<Activity> activities;
	private Constants constants = new Constants();
	private ArrayList<ActivityType> activityTypes;
	private ArrayList<GoalCategory> goalCategories;
	private ERBContainerController erbContainerController;
	private Logger logger = LogManager.getLogger(App.class);
	private String pathToERBFolder = constants.getPathToLocalERBFolder();
	
	private Project selectedProject;
	
	private String getGreeting() {
		return "Launching ERB";
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//System.out.println(java.awt.Toolkit.getDefaultToolkit().getScreenResolution());
		logger.info(getGreeting());
		readAndStoreData();
		loadERBContainer();
		loadERBLandingToContainer();
	}
	
	private void loadERBLandingToContainer() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/ERBLanding.fxml"));
			ERBLandingController erbLandingController = new ERBLandingController(this);
			fxmlLoader.setController(erbLandingController);
			Parent root = fxmlLoader.load();
			loadContent(root);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private Stage erbContainerStage = null;
	private void loadERBContainer() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/ERBContainer.fxml"));
			erbContainerController = new ERBContainerController(this);
			fxmlLoader.setController(erbContainerController);
			erbContainerStage = new Stage();
			Scene scene = new Scene(fxmlLoader.load());
			erbContainerStage.setScene(scene);
			erbContainerStage.setOnCloseRequest(e-> callToCloseERB());
			erbContainerStage.setTitle("ERB: Equitable Resilience Builder");
			erbContainerStage.show();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void callToCloseERB() {
		loadSavePopup(null, null, null, null, getProjects(), "close");
	}
	
	public void loadSavePopup(Activity activity, Chapter chapter, Goal goal, Project project,  ArrayList<Project> projects, String saveOrigin) {
		try {
			SaveHandler saveHandler = new SaveHandler(this, saveOrigin, activity, chapter, goal, project, projects);
			saveHandler.beginSave();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}	
	
	private void readAndStoreData() {
		XMLManager xmlManager = new XMLManager(this);
		
		File chaptersFile = getChaptersFileToParse();
		if(chaptersFile != null) chapters = xmlManager.parseChaptersXML(chaptersFile);
		
		File activityTypesFile = getActivityTypesFileToParse();
		if(activityTypesFile != null) activityTypes = xmlManager.parseActivityTypesXML(activityTypesFile);
		
		File availableActivitiesFile = getAvailableActivitiesFileToParse();
		if(availableActivitiesFile != null) activities = xmlManager.parseAvailableActivitiesXML(availableActivitiesFile, activityTypes);
		
		File goalCategoriesFile = getGoalCategoriesFileToParse();
		if(goalCategoriesFile != null) goalCategories = xmlManager.parseGoalCategoriesXML(goalCategoriesFile, activities);
		
		File erbProjectDirectory = getERBProjectDirectory();
		if(erbProjectDirectory != null) projects = xmlManager.parseAllProjects(erbProjectDirectory, activities);
	}
	
	public void loadContent(Node contentNode) {
		erbContainerController.getErbContainer().getChildren().clear();
		VBox.setVgrow(contentNode, Priority.ALWAYS);
		HBox.setHgrow(contentNode, Priority.ALWAYS);
		erbContainerController.getErbContainer().getChildren().add(contentNode);
	}
	
	private File getChaptersFileToParse() {
		File chaptersFile = new File(pathToERBFolder + "\\Static_Data\\Chapters\\Chapters.xml");
		if(chaptersFile.exists()) {
			return chaptersFile;
		} else {
			return null;
		}
	}
	
	private File getActivityTypesFileToParse() {
		File activityTypesFile = new File(pathToERBFolder + "\\Static_Data\\Activities\\Activity_Types.xml");
		if(activityTypesFile.exists()) {
			return activityTypesFile;
		} else {
			return null;
		}
	}
	
	private File getAvailableActivitiesFileToParse() {
		File availableActivitiesFile = new File(pathToERBFolder + "\\Static_Data\\Activities\\Available_Activities.xml");
		if(availableActivitiesFile.exists()) {
			return availableActivitiesFile;
		} else {
			return null;
		}
	}
	
	private File getGoalCategoriesFileToParse() {
		File goalCategoriesFile = new File(pathToERBFolder + "\\Static_Data\\Goals\\Goal_Categories.xml");
		if(goalCategoriesFile.exists()) {
			return goalCategoriesFile;
		} else {
			return null;
		}
	}
	
	private File getERBProjectDirectory() {
		File erbProjectDirectory = new File(pathToERBFolder + "\\Projects");
		if(erbProjectDirectory.exists()) {
			return erbProjectDirectory;
		} else {
			return null;
		}
	}
	
	public Activity getActivity(String activityID) {
		for (Activity activity : activities) {
			if (activity.getActivityID().contentEquals(activityID)) {
				return activity;
			}
		}
		logger.debug("Activity returned is null");
		return null;
	}
	
	public ArrayList<Project> getProjects(){
		return projects;
	}
		
	public ArrayList<Chapter> getChapters() {
		return chapters;
	}

	public ArrayList<Activity> getActivities() {
		return activities;
	}

	public ArrayList<ActivityType> getActivityTypes() {
		return activityTypes;
	}

	public ArrayList<GoalCategory> getGoalCategories() {
		return goalCategories;
	}

	public Project getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(Project selectedProject) {
		this.selectedProject = selectedProject;
	}
	
}