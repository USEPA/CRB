package com.epa.erb.activity_progress_tracker;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epa.erb.chapter.Chapter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ProgressTrackerController implements Initializable{

	@FXML
	HBox progressContentHBox;
	
	private ArrayList<Chapter> listOfAllChapters;
	public ProgressTrackerController(ArrayList<Chapter> listOfAllChapters) {
		this.listOfAllChapters = listOfAllChapters;
	}
	
	private Logger logger = LogManager.getLogger(ProgressTrackerController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addProgressColumns();
	}
	
	private void addProgressColumns() {
		for(Chapter chapter: listOfAllChapters) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/activity_progress_tracker/ProgressColumn.fxml"));
				ProgressColumnController progressColumnController = new ProgressColumnController(chapter);
				fxmlLoader.setController(progressColumnController);
				Parent root = fxmlLoader.load();
				progressContentHBox.getChildren().add(root);
				HBox.setHgrow(root, Priority.ALWAYS);
			}catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	public HBox getProgressContentHBox() {
		return progressContentHBox;
	}

	public ArrayList<Chapter> getListOfAllChapters() {
		return listOfAllChapters;
	}

	public void setListOfAllChapters(ArrayList<Chapter> listOfAllChapters) {
		this.listOfAllChapters = listOfAllChapters;
	}

}
