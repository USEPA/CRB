package gov.epa.equitableresiliencebuilder.engagement_action;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gov.epa.equitableresiliencebuilder.App;
import gov.epa.equitableresiliencebuilder.goal.Goal;
import gov.epa.equitableresiliencebuilder.project.Project;
import gov.epa.equitableresiliencebuilder.utility.FileHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;

public class WorksheetIndexController implements Initializable{

	private FileHandler fileHandler;
	
	private App app;
	private Goal goal;
	private Project project;
	public WorksheetIndexController(App app, Project project, Goal goal) {
		this.app = app;
		this.goal = goal;
		this.project = project;
		
		fileHandler = new FileHandler(app);
	}
	
	@FXML
	ListView<Hyperlink> worksheetListView;
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ArrayList<File> worksheets = getWorksheets();
		addWorksheetsToList(worksheets);

	}
	
	private void addWorksheetsToList(ArrayList<File> worksheets) {
		for(File worksheet: worksheets) {
			Hyperlink hyperlink = new Hyperlink(worksheet.getName());
			hyperlink.setOnAction(e-> hyperlinkClicked(worksheet));
			worksheetListView.getItems().add(hyperlink);
		}
	}
	
	private void hyperlinkClicked(File file) {
		if(file != null && file.exists()) {
			fileHandler.openFileOnDesktop(file);
		}
	}
	
	private ArrayList<File> getWorksheets(){
		File supportingDocDir = fileHandler.getSupportingDOCDirectory(project, goal);
		ArrayList<File> files = new ArrayList<File>();
		if(supportingDocDir != null && supportingDocDir.exists()) {
			for(File worksheet: supportingDocDir.listFiles()) {
				files.add(worksheet);
			}
		}
		return files;
	}

	public App getApp() {
		return app;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}
}
