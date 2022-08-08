package com.epa.erb.goal;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Activity;
import com.epa.erb.App;
import com.epa.erb.Constants;
import com.epa.erb.XMLManager;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.project.Project;
import com.epa.erb.project.ProjectSelectionController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;

public class GoalCreationController implements Initializable{

	@FXML
	VBox goalsVBox;
	@FXML
	TextField goalNameTextField;
	@FXML
	TextArea goalDescriptionTextArea;
	@FXML
	ListView<Goal> goalsListView;

	private App app;
	private Project project;
	private ProjectSelectionController projectSelectionController;
	public GoalCreationController(App app, Project project, ProjectSelectionController projectSelectionController) {
		this.app = app;
		this.project = project;
		this.projectSelectionController = projectSelectionController;
	}
	
	private Constants constants = new Constants();
	private XMLManager xmlManager = new XMLManager(app);
	private Logger logger = LogManager.getLogger(GoalCreationController.class);
	private String pathToERBProjectsFolder = constants.getPathToERBProjectsFolder();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		populateGoalCategoryCheckBoxes();
		setGoalsListViewCellFactory();
	}
	
	private void handleControls() {
		app.getErbContainerController().setTitleLabelText("ERB: Goal Creation");
	}

	@FXML
	public void addButtonAction() {
		if(hasGoalUserInput()) {
			ArrayList<CheckBox> selectedGoalCheckBoxes = getSelectedGoalCategoryCheckBoxes();
			if(selectedGoalCheckBoxes.size() > 0) {
				String goalName = getGoalName();
				String goalCleanedName = cleanStringForWindows(goalName);
				String goalDescription = getGoalDescription();
				ArrayList<GoalCategory> selectedGoalCategories = getSelectedGoalCategories();
				Goal goal = new Goal(app,goalName, goalCleanedName, goalDescription, selectedGoalCategories);
				goal.setChapters(app.getActivities(), project);
				addGoalToGoalsListView(goal);
				cleanGoalUserInputFields();
				uncheckAllGoalCategoryCheckBoxes();
			}
		} else {
			showGoalUserInputNeededAlert();
		}
	}
		
	@FXML
	public void doneButtonAction() {
		Optional<ButtonType> result = showDoneAlert();
		if(result.get() == ButtonType.OK) {
			ArrayList<Goal> createdGoals = getCreatedGoals();
			project.setProjectGoals(createdGoals);
			writeProjectMetaData(project);
			writeGoalsMetaData(createdGoals);
			projectSelectionController.loadEngagementActionToContainer(project);
		}
	}
	
	private void populateGoalCategoryCheckBoxes() {
		ArrayList<GoalCategory> goalCategories = app.getGoalCategories();
		for(GoalCategory goalCategory : goalCategories) {
			CheckBox goalCategoryCheckBox = createGoalCategoryCheckBox(goalCategory);
			goalsVBox.getChildren().add(goalCategoryCheckBox);
		}
	}
	
	private void uncheckAllGoalCategoryCheckBoxes() {
		for (int i = 0; i < goalsVBox.getChildren().size(); i++) {
			CheckBox goalCheckBox = (CheckBox) goalsVBox.getChildren().get(i);
			goalCheckBox.setWrapText(true);
			goalCheckBox.setPrefHeight(Control.USE_COMPUTED_SIZE);
			goalCheckBox.setPrefWidth(Control.USE_COMPUTED_SIZE);
			goalCheckBox.setSelected(false);
		}
	}
	
	private void addGoalToGoalsListView(Goal goal) {
		if(!goalsListView.getItems().contains(goal)) {
			goalsListView.getItems().add(goal);
		}
		setGoalsListViewCellFactory();
	}
	
	private void setGoalsListViewCellFactory() {
		goalsListView.setCellFactory(new Callback<ListView<Goal>, ListCell<Goal>>() {
			@Override
			public ListCell<Goal> call(ListView<Goal> param) {
				ListCell<Goal> cell = new ListCell<Goal>() {
					@Override
					protected void updateItem(Goal item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getGoalName());
							setContextMenu(createGoalContextMenu());
						}
					}
				};
				return cell;
			}
		});
	}
	
	public String cleanStringForWindows(String string) {
		return string.replaceAll("[^A-Za-z0-9]", "_");
	}

	private boolean hasGoalUserInput() {
		String goalName = goalNameTextField.getText();
		String goalDescription = goalDescriptionTextArea.getText();
		if(goalName != null && goalDescription != null) {
			if(goalName.trim().length() > 0 && goalDescription.trim().length() > 0) {
				return true;
			}
		}
		return false;
	}
	
	private void removeSelectedGoal(Event e) {
		Goal goalToRemove = getGoalListViewSelectedGoal();
		if(goalToRemove != null) goalsListView.getItems().remove(goalToRemove);
		setGoalsListViewCellFactory();
	}
	
	private void cleanGoalUserInputFields() {
		goalNameTextField.setText(null);
		goalDescriptionTextArea.setText(null);
	}

	private void showGoalUserInputNeededAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText("Please enter a valid goal name and description.");
		alert.setTitle("Alert");
		alert.showAndWait();
	}
	
	private Optional<ButtonType> showDoneAlert() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setContentText("Creating goals can only be done once. Are you sure you'd like to continue?");
		alert.setTitle("Alert");
		Optional<ButtonType> result = alert .showAndWait();
		return result;
	}
	
	public void writeProjectMetaData(Project project) {
		File projectMetaFile = constants.getProjectMetaXMLFile(project);
		xmlManager.writeProjectMetaXML(projectMetaFile, project);
	}
	
	public void writeGoalsMetaData(ArrayList<Goal> goals) {
		if (goals != null) {
			createGoalsDirectory();
			for (Goal goal : goals) {
				File goalDirectory = createGoalDirectory(goal);
				File goalMetaFile = new File(goalDirectory.getPath() + "\\Meta.xml");
				ArrayList<Chapter> chapters = goal.getChapters();
				xmlManager.writeGoalMetaXML(goalMetaFile, chapters);
				File activitesDirectory = createActivitiesDirectory(goalDirectory);
				createActivityDirectory(activitesDirectory, goal);
			}
		} else {
			logger.error("Cannot writeGoalsMetaData. goals is null.");
		}
	}
	
	private CheckBox createGoalCategoryCheckBox(GoalCategory goalCategory) {
		if (goalCategory != null) {
			CheckBox checkBox = new CheckBox(goalCategory.getCategoryName());
			checkBox.setWrapText(true);
			checkBox.setFont(new Font(15.0));
			return checkBox;
		} else {
			logger.error("Cannot createGoalCategoryCheckBox. goalCategory is null.");
			return null;
		}
	}
	
	private ContextMenu createGoalContextMenu() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem removeMenuItem = new MenuItem("Remove");
		removeMenuItem.setOnAction(e-> removeSelectedGoal(e));
		contextMenu.getItems().add(removeMenuItem);
		return contextMenu;
	}
	
	private void createActivityDirectory(File activitiesDirectory, Goal goal) {
		if (activitiesDirectory != null && goal != null) {
			for (Chapter chapter : goal.getChapters()) {
				for (Activity activity : chapter.getAssignedActivities()) {
					File activityDirectory = new File(activitiesDirectory.getPath() + "\\" + activity.getActivityID());
					if (!activityDirectory.exists()) {
						activityDirectory.mkdir();
					}
				}
			}
		} else {
			logger.error("Cannot createActivityDirectory. activitiesDirectory or goal is null.");
		}
	}
	
	private File createActivitiesDirectory(File goalDirectory) {
		if (goalDirectory != null) {
			File activitiesDirectory = new File(goalDirectory.getPath() + "\\Activities");
			if (!activitiesDirectory.exists()) {
				activitiesDirectory.mkdir();
			}
			return activitiesDirectory;
		} else {
			logger.error("Cannot createActivitiesDirectory. goalDirectory is null.");
			return null;
		}
	}
	
	private File createGoalDirectory(Goal goal) {
		if (goal != null) {
			File goalDirectory = new File(
					pathToERBProjectsFolder + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName());
			if (!goalDirectory.exists()) {
				goalDirectory.mkdir();
			}
			return goalDirectory;
		} else {
			logger.error("Cannot createGoalDirectory. goal is null.");
			return null;
		}
	}
	
	private void createGoalsDirectory() {
		File goalDirectory = new File(pathToERBProjectsFolder +  "\\" + project.getProjectCleanedName() + "\\Goals");
		if(!goalDirectory.exists()) {
			goalDirectory.mkdir();
		}
	}
		
	private ArrayList<Goal> getCreatedGoals(){
		ArrayList<Goal> createdGoals = new ArrayList<Goal>();
		for(Goal goal: goalsListView.getItems()) {
			createdGoals.add(goal);
		}
		return createdGoals;
	}
		
	public GoalCategory getGoalCategory(String goalCategoryName) {
		if (goalCategoryName != null) {
			ArrayList<GoalCategory> goalCategories = app.getGoalCategories();
			for (GoalCategory goalCategory : goalCategories) {
				if (goalCategory.getCategoryName().contentEquals(goalCategoryName)) {
					return goalCategory;
				}
			}
			logger.debug("Goal Category returned is null.");
			return null;
		} else {
			logger.error("Cannot getGoalCategory. goalCategoryName is null.");
			return null;
		}
	}
	
	private ArrayList<GoalCategory> getSelectedGoalCategories() {
		ArrayList<GoalCategory> selectedGoalCategories = new ArrayList<GoalCategory>();
		ArrayList<CheckBox> selectedGoalCheckBoxes = getSelectedGoalCategoryCheckBoxes();
		for (CheckBox goalCategoryCheckBox : selectedGoalCheckBoxes) {
			String goalCategoryName = goalCategoryCheckBox.getText();
			GoalCategory goalCategory = getGoalCategory(goalCategoryName);
			if(goalCategory != null) selectedGoalCategories.add(goalCategory);
		}
		return selectedGoalCategories;
	}
	
	private ArrayList<CheckBox> getSelectedGoalCategoryCheckBoxes() {
		ArrayList<CheckBox> selectedGoalCheckBoxes = new ArrayList<CheckBox>();
		for(int i =0; i < goalsVBox.getChildren().size(); i++) {
			CheckBox goalCheckBox = (CheckBox) goalsVBox.getChildren().get(i);
			if(goalCheckBox.isSelected()) {
				selectedGoalCheckBoxes.add(goalCheckBox);
			}
		}
		return selectedGoalCheckBoxes;
	}
	
	private String getGoalName() {
		String goalName = goalNameTextField.getText().trim();
		return goalName;
	}
	
	private String getGoalDescription() {
		String goalDescription = goalDescriptionTextArea.getText();
		return goalDescription;
	}
	
	private Goal getGoalListViewSelectedGoal() {
		return goalsListView.getSelectionModel().getSelectedItem();
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

	public void setProject(Project project) {
		this.project = project;
	}
	
	public ProjectSelectionController getProjectSelectionController() {
		return projectSelectionController;
	}

	public void setProjectSelectionController(ProjectSelectionController projectSelectionController) {
		this.projectSelectionController = projectSelectionController;
	}

	public VBox getGoalsVBox() {
		return goalsVBox;
	}

	public TextField getGoalNameTextField() {
		return goalNameTextField;
	}

	public TextArea getGoalDescriptionTextArea() {
		return goalDescriptionTextArea;
	}

	public ListView<Goal> getGoalsListView() {
		return goalsListView;
	}

}
