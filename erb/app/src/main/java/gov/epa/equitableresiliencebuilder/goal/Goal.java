package gov.epa.equitableresiliencebuilder.goal;

import java.io.File;
import java.util.ArrayList;

import gov.epa.equitableresiliencebuilder.App;
import gov.epa.equitableresiliencebuilder.ERBContentItem;
import gov.epa.equitableresiliencebuilder.project.Project;
import gov.epa.equitableresiliencebuilder.utility.FileHandler;
import gov.epa.equitableresiliencebuilder.utility.XMLManager;

public class Goal {
	
	private FileHandler fileHandler;
	private ArrayList<ERBContentItem> listOfContentItemsInGoal = new ArrayList<ERBContentItem>();
	
	private App app;
	private String goalName;
	private String goalCleanedName;
	private String goalDescription;
	private ArrayList<GoalCategory> listOfSelectedGoalCategories;
	public Goal(App app, String goalName, String goalCleanedName, String goalDescription, ArrayList<GoalCategory> listOfSelectedGoalCategories ) {
		this.app = app;
		this.goalName = goalName;
		this.goalCleanedName = goalCleanedName;
		this.goalDescription = goalDescription;
		this.listOfSelectedGoalCategories = listOfSelectedGoalCategories;
		
		fileHandler = new FileHandler(app);
	}
	
	public void setContentItems(Project project) {
		XMLManager xmlManager= new XMLManager(app);
		File goalMetaXML = fileHandler.getGoalMetaXMLFile(project, this);
		if(goalMetaXML.exists()) {
			listOfContentItemsInGoal = xmlManager.parseGoalXML(goalMetaXML);
		}else {
			for(GoalCategory gC: listOfSelectedGoalCategories) {
				listOfContentItemsInGoal.addAll(gC.getErbContentItems());
			}
		}
	}
	
	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public String getGoalName() {
		return goalName;
	}

	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}

	public String getGoalCleanedName() {
		return goalCleanedName;
	}

	public void setGoalCleanedName(String goalCleanedName) {
		this.goalCleanedName = goalCleanedName;
	}

	public String getGoalDescription() {
		return goalDescription;
	}

	public void setGoalDescription(String goalDescription) {
		this.goalDescription = goalDescription;
	}

	public ArrayList<GoalCategory> getListOfSelectedGoalCategories() {
		return listOfSelectedGoalCategories;
	}

	public void setListOfSelectedGoalCategories(ArrayList<GoalCategory> listOfSelectedGoalCategories) {
		this.listOfSelectedGoalCategories = listOfSelectedGoalCategories;
	}

	public ArrayList<ERBContentItem> getListOfContentItemsInGoal() {
		return listOfContentItemsInGoal;
	}
}
