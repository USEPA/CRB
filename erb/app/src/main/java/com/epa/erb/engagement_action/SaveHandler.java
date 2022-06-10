package com.epa.erb.engagement_action;

import java.util.ArrayList;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Activity;
import com.epa.erb.App;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SaveHandler {
	
	private App app;
	private String saveOrigin;
	private Activity activity;
	private Chapter chapter;
	private Goal goal;
	private Project project;
	private ArrayList<Project> projects;
	public SaveHandler(App app, String saveOrigin, Activity activity, Chapter chapter, Goal goal, Project project, ArrayList<Project> projects) {
		this.app = app;
		this.saveOrigin = saveOrigin;
		this.activity = activity;
		this.chapter = chapter;
		this.goal = goal;
		this.project = project;
		this.projects = projects;
	}
	
	private Logger logger = LogManager.getLogger(SaveHandler.class);

	public void beginSave() {
			String saveType = handleSavePrompt();
			if(!saveType.contentEquals("NO")) {
				loadSavePopup(activity, chapter, goal, project, projects, saveType);
		}
	}
	
	private String handleSavePrompt() {
		if (saveOrigin.contentEquals("close")) {
			if (isAProjectNotSaved(projects)) {
				return showToolSaveNeeded();
			}
		} else if (saveOrigin.contentEquals("projectChange")) {
			if (!project.isSaved()) {
				return showProjectSaveNeeded();
			}
		} else if (saveOrigin.contentEquals("saveButton")) {
			if(!project.isSaved()) {
				return "PROJECT";
			}
		} else if (saveOrigin.contentEquals("goalChange")) {
			if (!goal.isSaved()) {
				return showGoalSaveNeeded();
			}
		} else if (saveOrigin.contentEquals("chapterChange")) {
			if (!chapter.isSaved()) {
				String resultString = showChapterSaveNeeded();
				if(resultString.contentEquals("NO")) {
					chapter.setSaved(true);
				}
				return resultString;
			}
		} else if (saveOrigin.contentEquals("activityChange")) {
			if (!activity.isSaved()) {
				String resultString = showActivitySaveNeeded();
				if(resultString.contentEquals("NO")) {
					activity.setSaved(true);
				}
				return resultString;
			}
		}
		return "NO";
	}
	
	private Stage savePopupStage = null; //Save prompt on app close
	public void loadSavePopup(Activity activity, Chapter chapter, Goal goal, Project project,ArrayList<Project> projects, String saveType) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/SavePopup.fxml"));
			SavePopupController savePopupController = new SavePopupController(activity, chapter, goal, project, app, saveType, projects, this);
			fxmlLoader.setController(savePopupController);
			Parent root = fxmlLoader.load();
			savePopupStage = new Stage();
			Scene scene = new Scene(root);
			savePopupStage.setScene(scene);
			savePopupStage.setTitle("Saving...");
			savePopupStage.show();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}	
	
	private boolean isAProjectNotSaved(ArrayList<Project> projects) {
		for (Project project : projects) {
			if (!project.isSaved()) {
				return true;
			}
		}
		return false;
	}
	
	private String showActivitySaveNeeded(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Save");
		alert.setContentText("Would you like to save the changes you've made to this activity?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) return "ACTIVITY";
		return "NO" ;
	}
	
	private String showChapterSaveNeeded(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Save");
		alert.setContentText("Would you like to save the changes you've made to this chapter?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) return "CHAPTER";
		return "NO" ;
	}
	
	
	private String showGoalSaveNeeded(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Save");
		alert.setContentText("Would you like to save the changes you've made to this goal?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) return "GOAL";
		return "NO" ;
	}
	
	private String showProjectSaveNeeded(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Save");
		alert.setContentText("Would you like to save the changes you've made to this project?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) return "PROJECT";
		return "NO" ;
	}
	
	private String showToolSaveNeeded(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Save");
		alert.setContentText("Would you like to save the changes you've made to this tool?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) return "ALL";
		return "NO" ;
	}
	
	public void closeSavePopupStage() {
		if (savePopupStage != null) {
			savePopupStage.close();
		}
	}

}
