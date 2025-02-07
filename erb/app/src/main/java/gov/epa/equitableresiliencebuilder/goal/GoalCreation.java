package gov.epa.equitableresiliencebuilder.goal;

import java.io.File;
import java.util.ArrayList;

import gov.epa.equitableresiliencebuilder.App;
import gov.epa.equitableresiliencebuilder.project.Project;
import gov.epa.equitableresiliencebuilder.utility.FileHandler;
import gov.epa.equitableresiliencebuilder.utility.XMLManager;

public class GoalCreation{
	
	private XMLManager xmlManager;
	private FileHandler fileHandler;

	private App app;
	private Project project;
	public GoalCreation(App app, Project project) {
		this.app = app;
		this.project = project;
		
		xmlManager = new XMLManager(app);
		fileHandler = new FileHandler(app);
	}
	
	public String cleanStringForWindows(String string) {
		return string.replaceAll("[^A-Za-z0-9]", "_");
	}
	
	public void writeProjectMetaData(Project project) {
		File projectMetaFile = fileHandler.getProjectMetaXMLFile(project);
		xmlManager.writeProjectMetaXML(projectMetaFile, project);
	}
	
	public void writeGoalsMetaData(ArrayList<Goal> goals, String projectName) {
		if (goals != null) {
			createGoalsDirectory();
			for (Goal goal : goals) {
				createGoalDirectory(goal);
				createSupportingDocDirectory(goal);
				File supportingDOCDirectory = fileHandler.getSupportingDOCDirectory(project, goal);
				if(supportingDOCDirectory != null && supportingDOCDirectory.exists()) {
					if(projectName.contentEquals("Explore")) {
						copyGlobalSupportingDocsToGoalSupportingDocs(supportingDOCDirectory, true);
					}else {
						copyGlobalSupportingDocsToGoalSupportingDocs(supportingDOCDirectory,false);
					}
				}
			}
		} else {
		}
	}
	
	private void copyGlobalSupportingDocsToGoalSupportingDocs(File goalSupportingDOCDirectory, boolean readOnly) {
		if(goalSupportingDOCDirectory != null) {
			ArrayList<String> worksheetNames = xmlManager.parseWorksheetsXML(fileHandler.getWorksheetsFileFromResources());			
			for(String worksheetName: worksheetNames) {
				File sourceFile = fileHandler.getSupportingDocFromResources(worksheetName);
				File destFile = new File(goalSupportingDOCDirectory + File.separator + sourceFile.getName());
				fileHandler.copyFile(sourceFile, destFile);
				if(readOnly) destFile.setReadOnly();
			}
		}
	}
	
	private void createSupportingDocDirectory(Goal goal) {
		if (goal != null) {
			File supportingDOCDirectory = fileHandler.getSupportingDOCDirectory(project, goal);
			if (supportingDOCDirectory != null && !supportingDOCDirectory.exists()) {
				supportingDOCDirectory.mkdir();
			}
		} else {
		}
	}
	
	private void createGoalDirectory(Goal goal) {
		if (goal != null) {
			File goalDirectory = fileHandler.getGoalDirectory(project, goal);
			if (goalDirectory !=null && !goalDirectory.exists()) {
				goalDirectory.mkdir();
			}
		} else {
		}
	}
	
	private void createGoalsDirectory() {
		File goalDirectory = fileHandler.getGoalsDirectory(project);
		if(goalDirectory != null && !goalDirectory.exists()) {
			goalDirectory.mkdir();
		}
	}
		
	public GoalCategory getGoalCategory(String goalCategoryName) {
		if (goalCategoryName != null) {
			ArrayList<GoalCategory> goalCategories = app.getAvailableGoalCategories();
			for (GoalCategory goalCategory : goalCategories) {
				if (goalCategory.getCategoryName().contentEquals(goalCategoryName)) {
					return goalCategory;
				}
			}
			return null;
		} else {
			return null;
		}
	}
	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public Project getProject() {
		return project;
	}


}
