package com.epa.erb.chapter;

import java.net.URL;
import java.util.ResourceBundle;
import com.epa.erb.Activity;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ReflectNotesController implements Initializable{

	@FXML
	Label titleLabel;
	@FXML
	TextArea notesTextArea;
	
	private Activity activity;
	private ReflectController reflectController;
	public ReflectNotesController(Activity activity, ReflectController reflectController) {
		this.activity = activity;
		this.reflectController = reflectController;
	}
	
	private Chapter chapter;
	public ReflectNotesController(Chapter chapter, ReflectController reflectController) {
		this.chapter = chapter;
		this.reflectController = reflectController;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 if(activity !=null) {
			 checkForExistingActivityNotes();
			 titleLabel.setText("Notes for " + activity.getLongName());
		 }
		 if(chapter !=null) {
			 checkForExistingChapterNotes();
			 titleLabel.setText("Notes for " + chapter.getStringName());
		 }
	}
	
	private void checkForExistingActivityNotes() {
		if(activity.getNotes() != null && activity.getNotes().length() > 0) {
			notesTextArea.setText(activity.getNotes());
		}
	}
	
	private void checkForExistingChapterNotes() {
		if(chapter.getNotes() != null && chapter.getNotes().length() > 0) {
			notesTextArea.setText(chapter.getNotes());
		}
	}
	
	@FXML
	public void saveNotesAction() {
		if(activity != null) activity.setNotes(notesTextArea.getText());
		if(chapter != null) chapter.setNotes(notesTextArea.getText());
		reflectController.closeReflectNotesStage();
	}

}
