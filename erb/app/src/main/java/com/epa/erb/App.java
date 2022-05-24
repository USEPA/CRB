/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.epa.erb;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.goal.GoalCategory;
import com.epa.erb.project.Project;

public class App extends Application {

	private ArrayList<Project> projects;
	private ArrayList<Activity> activities;
	private ArrayList<ActivityType> activityTypes;
	private ArrayList<GoalCategory> goalCategories;
	private Logger logger = LogManager.getLogger(App.class);
	private Constants constants = new Constants();
	private String pathToERBFolder = constants.getPathToLocalERBFolder();
	
	private String getGreeting() {
		return "Launching ERB";
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		logger.info(getGreeting());
		readAndStoreData();
		loadERBLanding();
	}
	
	private Stage erbLandingStage = null;
	private void loadERBLanding() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/ERBLanding.fxml"));
			ERBLandingController erbLandingController = new ERBLandingController(this);
			fxmlLoader.setController(erbLandingController);
			erbLandingStage = new Stage();
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			erbLandingStage.setScene(scene);
			erbLandingStage.setTitle("ERB");
			erbLandingStage.show();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void readAndStoreData() {
		XMLManager xmlManager = new XMLManager(this);
		File activityTypesFile = getActivityTypesFileToParse();
		if(activityTypesFile != null) activityTypes = xmlManager.parseActivityTypesXML(activityTypesFile);
		
		File availableActivitiesFile = getAvailableActivitiesFileToParse();
		if(availableActivitiesFile != null) activities = xmlManager.parseAvailableActivitiesXML(availableActivitiesFile, activityTypes);
		
		File goalCategoriesFile = getGoalCategoriesFileToParse();
		if(goalCategoriesFile != null) goalCategories = xmlManager.parseGoalCategoriesXML(goalCategoriesFile, activities);
		
		File erbProjectDirectory = getERBProjectDirectory();
		if(erbProjectDirectory != null) projects = xmlManager.parseAllProjects(erbProjectDirectory, activities);
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
	
	public void closeERBLandingStage() {
		if(erbLandingStage != null) {
			erbLandingStage.close();
		}
	}
	
	public ArrayList<Project> getProjects(){
		return projects;
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
	
}