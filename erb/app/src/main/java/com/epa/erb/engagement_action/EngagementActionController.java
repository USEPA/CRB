package com.epa.erb.engagement_action;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Activity;
import com.epa.erb.App;
import com.epa.erb.Constants;
import com.epa.erb.Progress;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.chapter.ChapterLandingController;
import com.epa.erb.chapter.PlanController;
import com.epa.erb.chapter.ReflectController;
import com.epa.erb.goal.GlobalGoalTrackerController;
import com.epa.erb.goal.Goal;
import com.epa.erb.noteboard.NoteBoardContentController;
import com.epa.erb.project.Project;
import com.epa.erb.worksheet.WorksheetContentController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EngagementActionController implements Initializable{

	@FXML
	ComboBox<Goal> goalComboBox;
	@FXML
	HBox headingHBox;
	@FXML
	TitledPane pathwayTitledPane;
	@FXML
	HBox erbPathwayDiagramHBox;
	@FXML
	VBox erbKeyVBox;
	@FXML
	Pane materialKeyPane;
	@FXML
	Pane descriptionKeyPane;
	@FXML
	Pane whoKeyPane;
	@FXML
	Pane timeKeyPane;
	@FXML
	Pane completeKeyPane;
	@FXML
	Pane readyKeyPane;
	@FXML
	Pane skippedKeyPane;
	@FXML
	Pane inProgressKeyPane;
	@FXML
	VBox treeViewVBox;
	@FXML
	TreeView<String> treeView;
	@FXML
	VBox localProgressVBox;
	@FXML
	VBox mainVBox;
	@FXML
	HBox statusHBox;
	@FXML
	HBox body2HBox;
	@FXML
	VBox contentVBox;
	@FXML
	HBox attributePanelHBox;
	@FXML
	Label attributePanelCollapseLabel;
	@FXML
	VBox attributePanelContentVBox;	
	@FXML
	Label goalProgressPercentLabel;
	@FXML
	Label goalConfidencePercentLabel;
	@FXML
	VBox goalProgressVBox;
	@FXML
	VBox goalProgressBar;
	@FXML
	VBox goalConfidenceVBox;
	@FXML
	VBox goalConfidenceBar;
	@FXML
	ProgressIndicator chapterProgressIndicator;
	@FXML
	Button previousButton;
	@FXML
	Button skipButton;
	@FXML
	Button nextButton;
	@FXML
	ToggleGroup activityStatusToggleGroup;
	@FXML
	RadioButton readyRadioButton;
	@FXML
	RadioButton inProgressRadioButton;
	@FXML
	RadioButton completeRadioButton;
	
	private App app;
	private Project project;
	public EngagementActionController(App app, Project project) {
		this.app = app;
		this.project = project;
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(EngagementActionController.class);
	private Activity currentSelectedActivity = null; //tracks the current user selected activity
	private HashMap<TreeItem<String>, String> treeItemActivityIdTreeMap = new HashMap<TreeItem<String>, String>(); //holds the tree items mapped to a chapter or activity GUID
	private ArrayList<AttributePanelController> listOfAttributePanelControllers = new ArrayList<AttributePanelController>(); //holds all of the attribute panels
	private ArrayList<ERBPathwayDiagramController> listOfPathwayDiagramControllers = new ArrayList<ERBPathwayDiagramController>(); //holds all of the pathway controllers
	private ArrayList<ERBChapterDiagramController> listOfChapterDiagramControllers = new ArrayList<ERBChapterDiagramController>(); //holds all of the chapter controllers
	private ArrayList<WorksheetContentController> listOfAllWorksheetContentControllers = new ArrayList<WorksheetContentController>();
	private ArrayList<NoteBoardContentController> listOfAllNoteBoardContentControllers = new ArrayList<NoteBoardContentController>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillGoalComboBox();
		goalComboBox.setCellFactory(lv-> createGoalCell());
		goalComboBox.setButtonCell(createGoalCell());
		initializeGoalSelection();
		handleControls();
		setNavigationButtonsDisability(null, null);
	}
	
	private void handleControls() {
		treeView.setOnMouseClicked(e-> treeViewClicked(e));
		initializeStyle();
		addProgressListeners();
		addGoalChangeListener();
	}
	
	private void initializeStyle() {
		pathwayTitledPane.setStyle("-fx-box-border: transparent;");
		materialKeyPane.setStyle("-fx-background-color: " + constants.getMaterialsColor() + ";");
		descriptionKeyPane.setStyle("-fx-background-color: " + constants.getDescriptionColor() + ";");
		whoKeyPane.setStyle("-fx-background-color: " + constants.getWhoColor() + ";");
		completeKeyPane.setStyle("-fx-background-color: " + constants.getCompleteStatusColor() + ";");
		readyKeyPane.setStyle("-fx-background-color: " + constants.getReadyStatusColor() + ";");
		skippedKeyPane.setStyle("-fx-background-color: " + constants.getSkippedStatusColor() + ";");
		inProgressKeyPane.setStyle("-fx-background-color: " + constants.getInProgressStatusColor() + ";");
		timeKeyPane.setStyle("-fx-background-color: " + constants.getTimeColor() + ";");
		chapterProgressIndicator.setStyle("-fx-progress-color: " + constants.getAllChaptersColor() + ";");
	}
	
	private void addProgressListeners() {
		localProgressVBox.heightProperty().addListener(e-> handleProgressBarsAfterPaneSizeChange());
		localProgressVBox.widthProperty().addListener(e-> handleProgressBarsAfterPaneSizeChange());
	}
	
	private void addGoalChangeListener() {
		goalComboBox.valueProperty().addListener(new ChangeListener<Goal>() {
			@Override
			public void changed(ObservableValue ov, Goal origG, Goal newG) {
				goalChanged(origG, newG);
			}
		});
	}
	
	private void handleProgressBarsAfterPaneSizeChange() {
		if(currentSelectedActivity != null) {
			String currentChapterName = "Chapter " + currentSelectedActivity.getChapterAssignment();
			updateLocalProgress(getChapter(currentChapterName), getCurrentGoal().getChapters());
		} else {
			TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
			if(selectedTreeItem.getValue().contains("Chapter")) {
				updateLocalProgress(getChapter(selectedTreeItem.getValue().trim()), getCurrentGoal().getChapters());
			}
		}
	}
	
	private void initializeGoalSelection() {
		goalComboBox.getSelectionModel().select(0);
		goalSelected(project.getProjectGoals().get(0));
	}
	
	private void initializeTreeViewSelection() {
		for (TreeItem<String> treeItem : treeItemActivityIdTreeMap.keySet()) {
			if (treeItemActivityIdTreeMap.get(treeItem) == "0") {
				getTreeView().getSelectionModel().select(treeItem);
				treeViewClicked(null);
			}
		}
	}
			
	private void loadERBLandingContent(ArrayList<Chapter> chapters) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/ChapterLanding.fxml"));
			ChapterLandingController chapterLandingController = new ChapterLandingController(chapters, this);
			fxmlLoader.setController(chapterLandingController);
			VBox root = fxmlLoader.load();
			root.setPrefWidth(app.getPrefWidth());
			root.setPrefHeight(app.getPrefHeight());
			contentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void loadChapterLandingContent(Chapter chapter) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/ChapterLanding.fxml"));
			ChapterLandingController chapterLandingController = new ChapterLandingController(chapter, this);
			fxmlLoader.setController(chapterLandingController);
			VBox root = fxmlLoader.load();
			root.setPrefWidth(app.getPrefWidth());
			root.setPrefHeight(app.getPrefHeight());
			contentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void loadSampleWK(Activity activity) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/worksheet/WorksheetContent.fxml"));
			WorksheetContentController worksheetContentController = new WorksheetContentController(activity);
			fxmlLoader.setController(worksheetContentController);
			Parent root = fxmlLoader.load();
			contentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
			activity.setWorksheetContentController(worksheetContentController);
			listOfAllWorksheetContentControllers.add(worksheetContentController);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void loadSampleNB(Activity activity) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardContent.fxml"));
			NoteBoardContentController noteBoardContentController = new NoteBoardContentController(app, project, getCurrentGoal(), activity);
			fxmlLoader.setController(noteBoardContentController);
			Parent root = fxmlLoader.load();
			contentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
			activity.setNoteBoardContentController(noteBoardContentController);
			listOfAllNoteBoardContentControllers.add(noteBoardContentController);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private Parent loadAttributePanel(String attributeTitle, String attributeContent, String attributeColor) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/AttributePane.fxml"));
			AttributePanelController attributePanelController = new AttributePanelController(attributeTitle, attributeContent, attributeColor, this);
			listOfAttributePanelControllers.add(attributePanelController);
			fxmlLoader.setController(attributePanelController);
			return fxmlLoader.load();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	private void loadChapterPlan(Chapter chapter, Activity activity) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/Plan.fxml"));
			PlanController planController = new PlanController();
			fxmlLoader.setController(planController);
			VBox root = fxmlLoader.load();
			root.setPrefWidth(app.getPrefWidth());
			root.setPrefHeight(app.getPrefHeight());
			contentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
			activity.setPlanController(planController);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void loadChapterReflect(Chapter chapter, Activity activity) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/Reflect.fxml"));
			ReflectController reflectController = new ReflectController(getCurrentGoal(), chapter, this);
			fxmlLoader.setController(reflectController);
			Parent root = fxmlLoader.load();
			reflectController.initProgress(getCurrentGoal(), chapter);
			contentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
			activity.setReflectController(reflectController);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private Parent loadChapterERBPathwayDiagram(Chapter chapter, ArrayList<Chapter> chapters)  {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/ERBChapterDiagram.fxml"));
			ERBChapterDiagramController erbChapterDiagramController = new ERBChapterDiagramController(chapter, chapters, this);
			listOfChapterDiagramControllers.add(erbChapterDiagramController);
			fxmlLoader.setController(erbChapterDiagramController);
			return fxmlLoader.load();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	private Parent loadERBPathwayDiagram(Activity activity, Chapter chapter) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/ERBPathwayDiagram.fxml"));
			ERBPathwayDiagramController erbPathwayDiagramController = new ERBPathwayDiagramController(activity, chapter, this);
			listOfPathwayDiagramControllers.add(erbPathwayDiagramController);
			fxmlLoader.setController(erbPathwayDiagramController);
			return fxmlLoader.load();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	private Stage globalGoalTrackerStage = null;
	private void loadGlobalGoalTracker() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/goal/GlobalGoalTracker.fxml"));
			GlobalGoalTrackerController globalGoalTrackerController = new GlobalGoalTrackerController(app, project);
			fxmlLoader.setController(globalGoalTrackerController);
			Parent root = fxmlLoader.load();
			globalGoalTrackerStage = new Stage();
			Scene scene = new Scene(root);
			globalGoalTrackerStage.setScene(scene);
			globalGoalTrackerStage.setTitle("ERB: Goal Tracker");
			globalGoalTrackerStage.show();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void goalChanged(Goal origGoal, Goal newGoal) {
		if(origGoal != null) {
			app.initSaveHandler(null, null, origGoal, project, null, "goalChange");
			goalSelected(newGoal);
		}
	}
	
	@FXML
	public void activityStatusRadioButtonAction () {
		RadioButton radioButton = (RadioButton) activityStatusToggleGroup.getSelectedToggle();
		changeActivityStatus(currentSelectedActivity, radioButton);
		ERBPathwayDiagramController erbPathwayDiagramController = getErbPathwayDiagramController(currentSelectedActivity.getActivityID());
		ERBChapterDiagramController erbChapterDiagramController = getErbChapterDiagramController(currentSelectedActivity.getChapterAssignment());
		if(erbChapterDiagramController != null) erbChapterDiagramController.fillChapterCircleBasedOnStatus();
		if(erbPathwayDiagramController != null) erbPathwayDiagramController.updateStatus(); //set status of erb diagram
		updateLocalProgress(getChapterForActivity(currentSelectedActivity), getCurrentGoal().getChapters());
		currentSelectedActivity.setSaved(false);
	}
	
	private void changeActivityStatus(Activity activity, RadioButton radioButton) {
		if(radioButton.getText().contentEquals("Ready")) {
			activity.setStatus("ready");
		} else if(radioButton.getText().contentEquals("In Progress")) {
			activity.setStatus("in progress"); 
		} else if(radioButton.getText().contentEquals("Complete")) {
			activity.setStatus("complete");
		} 
	}
	
	@FXML
	public void previousButtonAction() {
		TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		TreeItem<String> parentTreeItem = selectedTreeItem.getParent();
		if (selectedTreeItem != null && parentTreeItem != null) {
			int currentIndex = parentTreeItem.getChildren().indexOf(selectedTreeItem);
			treeView.getSelectionModel().select(parentTreeItem.getChildren().get(currentIndex - 1));
			treeViewClicked(null);
		}
	}
	
	@FXML
	public void skipButtonAction() {
		
	}
	
	@FXML
	public void nextButtonAction() {
		TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		TreeItem<String> parentTreeItem = selectedTreeItem.getParent();
		if (selectedTreeItem != null && parentTreeItem != null) {
			if (parentTreeItem.getValue().contains("ERB")) {
				treeView.getSelectionModel().select(selectedTreeItem.getChildren().get(0));
			} else {
				int currentIndex = parentTreeItem.getChildren().indexOf(selectedTreeItem);
				treeView.getSelectionModel().select(parentTreeItem.getChildren().get( currentIndex+ 1));
			}
			treeViewClicked(null);
		}
	}
	
	@FXML
	public void saveButtonAction() {
		app.initSaveHandler(null, null, null, project, null, "saveButton");
	}
	
	@FXML
	public void goalLabelClicked() {
		loadGlobalGoalTracker();
	}
		
	@FXML
	public void attributePanelCollapseClicked(MouseEvent mouseEvent) {
		if (mouseEvent.getSource().toString().contains("VBox")) { //Don't allow for label
			String attributePanelCollapseString = attributePanelCollapseLabel.getText();
			if (attributePanelCollapseString.contentEquals(">") && !attributePanelCollapsed()) {
				collapseAttributePanel();
				attributePanelHBox.setMinWidth(0.0);
				attributePanelCollapseLabel.setText("<");
			} else if (attributePanelCollapseString.contentEquals("<") && attributePanelCollapsed()) {
				unCollapseAttributePanel();
				attributePanelHBox.setMinWidth(200.0);
				attributePanelCollapseLabel.setText(">");
			}
		}
	}
	
	public void treeViewClicked(Event e) {
		if (e == null || (!e.getTarget().toString().contains("Group@"))) {
			TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
			if (selectedTreeItem != null) {
				TreeItem<String> parentTreeItem = selectedTreeItem.getParent();
				if (parentTreeItem != null) {
					String parentTreeItemValue = parentTreeItem.getValue().trim();
					String selectedTreeItemValue = selectedTreeItem.getValue().trim();
					if (selectedTreeItemValue.length() > 0) {
						if (parentTreeItemValue.contains("ERB")) { // Is Chapter						
							chapterSelectedInTree(selectedTreeItemValue);
							addLocalProgressVBox(1);
							updateLocalProgress(getChapter(selectedTreeItemValue), getCurrentGoal().getChapters());
						} else { // Is Activity
							activitySelectedInTree(selectedTreeItem, parentTreeItem);
						}
					} else {
						currentSelectedActivity = null;
					}
				} else { // ERB Pathway
					erbPathwaySelectedInTree(selectedTreeItem);
					removeLocalProgressVBox();
				}
			} else {
				currentSelectedActivity = null;
				setNavigationButtonsDisability(null, null);
			}
		}
	}
	
	private void fillAndStoreTreeViewData(ArrayList<Chapter> chapters) {
		treeItemActivityIdTreeMap.clear();
		TreeItem<String> rootTreeItem = new TreeItem<String>("ERB Pathway");
		rootTreeItem.setExpanded(true);
		treeView.setRoot(rootTreeItem);
		treeItemActivityIdTreeMap.put(rootTreeItem, "0");
		for (Chapter chapter : chapters) {
			TreeItem<String> chapterTreeItem = new TreeItem<String>(chapter.getStringName());
			rootTreeItem.getChildren().add(chapterTreeItem);
			treeItemActivityIdTreeMap.put(chapterTreeItem, chapter.getNumericName());
			for (Activity activity : chapter.getAssignedActivities()) {
				TreeItem<String> activityTreeItem = new TreeItem<String>(activity.getLongName());
				chapterTreeItem.getChildren().add(activityTreeItem);
				treeItemActivityIdTreeMap.put(activityTreeItem, activity.getActivityID());
			}
		}
	}
	
	private void fillGoalComboBox() {
		for(Goal goal: project.getProjectGoals()) {
			goalComboBox.getItems().add(goal);
		}
	}
	
	private ListCell<Goal> createGoalCell() {
		return new ListCell<Goal>() {
			@Override
			protected void updateItem(Goal item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.getGoalName());
				}
			}
		};
	}
	
	void generateAttributePanel(String attributeTitle, String attributeContent, String attributeColor) {
		if (attributeContent.trim().length() > 0 && !attributePanelContainsAttribute(attributeTitle)) {
			Parent root = loadAttributePanel(attributeTitle, attributeContent, attributeColor);
			attributePanelContentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
		}
	}
	
	private void goalSelected(Goal goal) {
		ArrayList<Chapter> listOfChapters = goal.getChapters();
		if (listOfChapters != null) {
			fillAndStoreTreeViewData(listOfChapters);
			initializeTreeViewSelection();
			generateChapterERBPathway();
		}
	}
	
	public void updateLocalProgress(Chapter chapter, ArrayList<Chapter> chapters) {
		Progress progress = new Progress();
		if (chapter != null) {
			int chapterProgress = progress.getChapterPercentDone(chapter);
			setChapterProgress(chapterProgress);
		}
		if(chapters != null) {
			int goalProgress = progress.getGoalPercentDone(chapters);
			setGoalProgress(goalProgress);
			int goalConfidence = progress.getGoalConfidencePercent(chapters);
			setGoalConfidence(goalConfidence);
		}
	}
	
	private void setChapterProgress(int chapterPercent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				chapterProgressIndicator.setProgress(chapterPercent/100.0);
			}
		});
	}
	
	private void setGoalProgress(int goalPercent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				double progressBarHeight = goalProgressVBox.getHeight();
				goalProgressBar.setMaxHeight(progressBarHeight);
				goalProgressBar.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
				double fixedCapacity = 100;
				double progress = goalPercent / fixedCapacity;
				goalProgressBar.setPrefHeight(progressBarHeight * progress);
				goalProgressPercentLabel.setText(goalPercent + "%");
			}
		});
	}
	
	private void setGoalConfidence(int goalConfidence) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				double progressBarHeight = goalConfidenceVBox.getHeight();
				goalConfidenceBar.setMaxHeight(progressBarHeight);
				goalConfidenceBar.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
				double fixedCapacity = 100;
				double progress = goalConfidence / fixedCapacity;
				goalConfidenceBar.setPrefHeight(progressBarHeight * progress);
				goalConfidencePercentLabel.setText(goalConfidence + "%");
			}
		});
	}
	
	private void loadActivityContent(TreeItem<String> selectedTreeItem, Chapter chapter) {
		String activityID = treeItemActivityIdTreeMap.get(selectedTreeItem);
		Activity selectedActivity = getActivity(activityID, chapter.getChapterNum());
		if (selectedActivity.getActivityType().getDescription().contentEquals("worksheet")) {
			if (!selectedActivity.getLongName().contentEquals("Plan") && !selectedActivity.getLongName().contentEquals("Reflect")) {
				generateAttributePanel("Objective", selectedActivity.getObjectives(), constants.getObjectivesColor());
				generateAttributePanel("Instructions", selectedActivity.getInstructions(), constants.getInstructionsColor());
				loadSampleWK(selectedActivity);
			} else if (selectedActivity.getLongName().contentEquals("Plan")) {
				removeAttributeScrollPane();
				loadChapterPlan(getChapterForActivity(selectedActivity), selectedActivity);
			} else if (selectedActivity.getLongName().contentEquals("Reflect")) {
				removeAttributeScrollPane();
				loadChapterReflect(getChapterForActivity(selectedActivity), selectedActivity);
			}
		} else if (selectedActivity.getActivityType().getDescription().contentEquals("noteboard")) {
			generateAttributePanel("Objective", selectedActivity.getObjectives(), constants.getObjectivesColor());
			generateAttributePanel("Instructions", selectedActivity.getInstructions(), constants.getInstructionsColor());
			loadSampleNB(selectedActivity);
		}
	}
	
	private void highlightSelectedActivityDiagram(TreeItem<String> selectedTreeItem, Chapter chapter) {
		String activityID = treeItemActivityIdTreeMap.get(selectedTreeItem);
		Activity selectedActivity = getActivity(activityID, chapter.getChapterNum());
		for(ERBPathwayDiagramController erbPathwayDiagramController: listOfPathwayDiagramControllers) {
			if(erbPathwayDiagramController.getActivity() == selectedActivity) {
				erbPathwayDiagramController.highlightDiagram();
			} else {
				erbPathwayDiagramController.unHighlightDiagram();
			}
		}
	}
	
	private void erbPathwaySelectedInTree(TreeItem<String> selectedTreeItem) {
		if (selectedTreeItem != null) {
			currentSelectedActivity = null;
			cleanContentVBox();
			cleanAttributePanelContentVBox();
			cleanListOfAttributePanelControllers();
			removeAttributeScrollPane();
			removeERBKeyVBox();
			generateChapterERBPathway();
			loadERBLandingContent(getCurrentGoal().getChapters());
			setNavigationButtonsDisability(null, null);
			removeStatusHBox();
		}
	}
	
	private void chapterSelectedInTree(String selectedTreeItemValue) {
		if(currentSelectedActivity != null) app.initSaveHandler(null, getChapterForActivity(currentSelectedActivity), getCurrentGoal(), project, null, "chapterChange");
		Chapter currentChapter = getChapter(selectedTreeItemValue);
		currentSelectedActivity = null;
		cleanContentVBox();
		cleanAttributePanelContentVBox();
		cleanListOfAttributePanelControllers();
		addERBKeyVBox(1);
		removeAttributeScrollPane();
		loadChapterLandingContent(currentChapter);
		setNavigationButtonsDisability(null, null);
		generateActivityERBPathway(currentChapter);
		removeStatusHBox();
	}
	
	private void activitySelectedInTree(TreeItem<String> selectedTreeItem, TreeItem<String> parentTreeItem) {
		String activityGUID = treeItemActivityIdTreeMap.get(selectedTreeItem);
		Chapter currentChapter = getChapter(parentTreeItem.getValue());
		Activity selectedActivity = getActivity(activityGUID, currentChapter.getChapterNum());
		if(!selectedActivity.getActivityID().contentEquals("26")) {
			addLocalProgressVBox(1);
			updateLocalProgress(currentChapter, getCurrentGoal().getChapters());
		} else {
			removeLocalProgressVBox();
		}
		if(!currentChapter.getStringName().contentEquals(selectedActivity.getChapterAssignment()) || currentSelectedActivity != selectedActivity) { //If a new activity is selected
			if(currentSelectedActivity != null) app.initSaveHandler(currentSelectedActivity, null, getCurrentGoal(), project, null, "activityChange");
			addAttributeScrollPane(1);
			addERBKeyVBox(1);
			cleanContentVBox();
			cleanAttributePanelContentVBox();
			cleanListOfAttributePanelControllers();
			loadActivityContent(selectedTreeItem, currentChapter);
			generateActivityERBPathway(currentChapter);
			highlightSelectedActivityDiagram(selectedTreeItem, currentChapter);
			setActivityStatusRadioButton(selectedActivity);
			currentSelectedActivity = selectedActivity;
			addStatusHBox();
			if(selectedActivity.getActivityID().contentEquals("25") || selectedActivity.getActivityID().contentEquals("26")) removeStatusHBox();
		}
		setNavigationButtonsDisability(selectedTreeItem, parentTreeItem);
	}
	
	private void setActivityStatusRadioButton(Activity activity) {
		if(activity.getStatus().contentEquals("ready")) {
			readyRadioButton.setSelected(true);
		} else if (activity.getStatus().contentEquals("in progress")) {
			inProgressRadioButton.setSelected(true);
		} else if (activity.getStatus().contentEquals("complete")) {
			completeRadioButton.setSelected(true);
		}
	}
	
	private void generateChapterERBPathway() {
		cleanERBPathwayDiagramHBox();
		cleanListOfChapterDiagramControllers();
		for (Chapter chapter : getCurrentGoal().getChapters()) {
			Parent root = loadChapterERBPathwayDiagram(chapter, getCurrentGoal().getChapters());
			pathwayTitledPane.setText(getCurrentGoal().getGoalName() + " Pathway");
			if(root != null) erbPathwayDiagramHBox.getChildren().add(root);
		}
	}
	
	private void generateActivityERBPathway(Chapter chapter) {
		if (chapter != null) {
				cleanERBPathwayDiagramHBox();
				cleanListOfActivityDiagramControllers();
				for (Activity activity : chapter.getAssignedActivities()) {
					Parent root = loadERBPathwayDiagram(activity, chapter);
					pathwayTitledPane.setText(chapter.getStringName() + " Pathway");
					if(root != null) erbPathwayDiagramHBox.getChildren().add(root);
				}
			}
	}
	
	private void setNavigationButtonsDisability(TreeItem<String> selectedTreeItem, TreeItem<String> parentTreeItem) {
		if (selectedTreeItem != null) { //if activity is not null
			if (parentTreeItem == null) { //if erb pathway
				previousButton.setDisable(true);
				skipButton.setDisable(true);
				nextButton.setDisable(false);
			} else {
				int numberOfChildrenInParent = parentTreeItem.getChildren().size();
				if (numberOfChildrenInParent > 1) { //if more than 1 activity
					int indexOfSelectedItem = parentTreeItem.getChildren().indexOf(selectedTreeItem);
					if (indexOfSelectedItem == 0) { //if first activity
						previousButton.setDisable(true);
						skipButton.setDisable(true); //true
						nextButton.setDisable(false);
					} else if (indexOfSelectedItem == parentTreeItem.getChildren().size() - 1) { //if last activity
						previousButton.setDisable(false);
						skipButton.setDisable(true);
						nextButton.setDisable(true);
					} else {
						previousButton.setDisable(false);
						skipButton.setDisable(true);
						nextButton.setDisable(false);
					}
				} else { //if only 1 activity
					previousButton.setDisable(true);
					skipButton.setDisable(true);
					nextButton.setDisable(true);
				}
			}
		} else { //if activity is null
			previousButton.setDisable(true);
			skipButton.setDisable(true);
			nextButton.setDisable(true);
		}
	}
	
	private boolean attributePanelContainsAttribute(String attributeLabel) {
		for(AttributePanelController attributePanelController : listOfAttributePanelControllers) {
			if(attributePanelController.getAttributeLabelText().contentEquals(attributeLabel)) {
				return true;
			}
		}
		return false;
	}
	
	private void collapseAttributePanel() {
		if(attributePanelHBox.getChildren().contains(attributePanelContentVBox)) {
			attributePanelHBox.getChildren().remove(attributePanelContentVBox);
		}
	}
	
	private void unCollapseAttributePanel() {
		if(!attributePanelHBox.getChildren().contains(attributePanelContentVBox)) {
			attributePanelHBox.getChildren().add(1, attributePanelContentVBox);
		}
	}
	
	private boolean attributePanelCollapsed() {
		if(!attributePanelHBox.getChildren().contains(attributePanelContentVBox)) {
			return true;
		} else {
			return false;
		}
	}
		
	private void cleanAttributePanelContentVBox() {
		attributePanelContentVBox.getChildren().clear();
	}
	
	private void cleanERBPathwayDiagramHBox() {
		erbPathwayDiagramHBox.getChildren().clear();
	}

	private void cleanContentVBox() {
		contentVBox.getChildren().clear();
	}
	
	private void cleanListOfActivityDiagramControllers() {
		listOfPathwayDiagramControllers.clear();
	}
	
	private void cleanListOfChapterDiagramControllers() {
		listOfChapterDiagramControllers.clear();
	}
	
	private void cleanListOfAttributePanelControllers() {
		listOfAttributePanelControllers.clear();
	}
	
	void removeAttributePanelController(AttributePanelController attributePanelController) {
		listOfAttributePanelControllers.remove(attributePanelController);
	}
	
	private void addLocalProgressVBox(int index) {
		if(!treeViewVBox.getChildren().contains(localProgressVBox)) {
			treeViewVBox.getChildren().add(index, localProgressVBox);
		}
	}
	
	private void removeLocalProgressVBox() {
		if(treeViewVBox.getChildren().contains(localProgressVBox)) {
			treeViewVBox.getChildren().remove(localProgressVBox);
		}
	}
	
	private void addERBKeyVBox(int index) {
		if(!headingHBox.getChildren().contains(erbKeyVBox)) {
			headingHBox.getChildren().add(index, erbKeyVBox);
		}
	}
	
	private void removeERBKeyVBox() {
		headingHBox.getChildren().remove(erbKeyVBox);
	}
	
	private void addAttributeScrollPane(int index) {
		if(!body2HBox.getChildren().contains(attributePanelHBox)) {
			body2HBox.getChildren().add(index, attributePanelHBox);
		}
	}
	
	private void removeAttributeScrollPane() {
		body2HBox.getChildren().remove(attributePanelHBox);
	}
	
	private void removeStatusHBox() {
		if(mainVBox.getChildren().contains(statusHBox)) {
			mainVBox.getChildren().remove(statusHBox);
		}
	}
	
	private void addStatusHBox() {
		if(!mainVBox.getChildren().contains(statusHBox)) {
			mainVBox.getChildren().add(0, statusHBox);
		}
	}
	
	public void closeGlobalGoalTrackerStage() {
		if(globalGoalTrackerStage!=null) {
			globalGoalTrackerStage.close();
		}
	}
			
	public Chapter getChapter(String chapterName) {
		for(Chapter chapter : getCurrentGoal().getChapters()) {
			if(chapter.getStringName().contentEquals(chapterName)) {
				return chapter;
			}
		}
		logger.debug("Chapter returned is null");
		return null;
	}
	
	public Chapter getChapterForActivity(Activity activity) {
		String chapterName = "Chapter " + activity.getChapterAssignment();
		Chapter chapter = getChapter(chapterName);
		if(chapter != null) return chapter;
		logger.debug("Chapter returned is null");
		return null;
	}
	
	private Activity getActivity(String activityID, int chapterNum) {
		for(Chapter chapter: getCurrentGoal().getChapters()) {
			for(Activity activity : chapter.getAssignedActivities()) {
				if(activity.getActivityID().contentEquals(activityID) && activity.getChapterAssignment().contentEquals(String.valueOf(chapterNum))) {
					return activity;
				}
			}
		}
		logger.debug("Activity returned is null");
		return null;
	}
	
	String getSelectedActivityGUID() {
		TreeItem<String> treeItem = treeView.getSelectionModel().getSelectedItem();
		if (treeItem != null) {
			return treeItemActivityIdTreeMap.get(treeItem);
		} else {
			logger.debug("Selected Activity GUID returned is null");
			return null;
		}
	}
	
	public ERBPathwayDiagramController getErbPathwayDiagramController(String GUID) {
		for(ERBPathwayDiagramController erbPathwayDiagramController : listOfPathwayDiagramControllers) {
			if(erbPathwayDiagramController.getActivity().getActivityID().contentEquals(GUID)) {
				return erbPathwayDiagramController;
			}
		}
		logger.debug("ERB Pathway Diagram Controller returned is null");
		return null;
	}
	
	public ERBChapterDiagramController getErbChapterDiagramController(String selectedChapter) {
		for(ERBChapterDiagramController erbChapterDiagramController : listOfChapterDiagramControllers) {
			if(String.valueOf(erbChapterDiagramController.getChapter().getChapterNum()).contentEquals(selectedChapter)){
				return erbChapterDiagramController;
			}
		}
		logger.debug("ERB Chapter Diagram Controller returned is null");
		return null;
	}
	
	public Goal getCurrentGoal() {
		return goalComboBox.getSelectionModel().getSelectedItem();
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

	public Activity getCurrentSelectedActivity() {
		return currentSelectedActivity;
	}

	public void setCurrentSelectedActivity(Activity currentSelectedActivity) {
		this.currentSelectedActivity = currentSelectedActivity;
	}

	public HashMap<TreeItem<String>, String> getTreeItemActivityIdTreeMap() {
		return treeItemActivityIdTreeMap;
	}

	public void setTreeItemActivityIdTreeMap(HashMap<TreeItem<String>, String> treeItemActivityIdTreeMap) {
		this.treeItemActivityIdTreeMap = treeItemActivityIdTreeMap;
	}

	public ArrayList<AttributePanelController> getListOfAttributePanelControllers() {
		return listOfAttributePanelControllers;
	}

	public void setListOfAttributePanelControllers(ArrayList<AttributePanelController> listOfAttributePanelControllers) {
		this.listOfAttributePanelControllers = listOfAttributePanelControllers;
	}

	public ArrayList<ERBPathwayDiagramController> getListOfPathwayDiagramControllers() {
		return listOfPathwayDiagramControllers;
	}

	public void setListOfPathwayDiagramControllers(ArrayList<ERBPathwayDiagramController> listOfPathwayDiagramControllers) {
		this.listOfPathwayDiagramControllers = listOfPathwayDiagramControllers;
	}

	public ArrayList<WorksheetContentController> getListOfAllWorksheetContentControllers() {
		return listOfAllWorksheetContentControllers;
	}

	public void setListOfAllWorksheetContentControllers(
			ArrayList<WorksheetContentController> listOfAllWorksheetContentControllers) {
		this.listOfAllWorksheetContentControllers = listOfAllWorksheetContentControllers;
	}

	public ArrayList<NoteBoardContentController> getListOfAllNoteBoardContentControllers() {
		return listOfAllNoteBoardContentControllers;
	}

	public void setListOfAllNoteBoardContentControllers(
			ArrayList<NoteBoardContentController> listOfAllNoteBoardContentControllers) {
		this.listOfAllNoteBoardContentControllers = listOfAllNoteBoardContentControllers;
	}

	public Stage getGlobalGoalTrackerStage() {
		return globalGoalTrackerStage;
	}

	public void setGlobalGoalTrackerStage(Stage globalGoalTrackerStage) {
		this.globalGoalTrackerStage = globalGoalTrackerStage;
	}

	public ComboBox<Goal> getGoalComboBox() {
		return goalComboBox;
	}

	public HBox getHeadingHBox() {
		return headingHBox;
	}
	
	public HBox getErbPathwayDiagramHBox() {
		return erbPathwayDiagramHBox;
	}

	public VBox getErbKeyVBox() {
		return erbKeyVBox;
	}

	public Pane getMaterialKeyPane() {
		return materialKeyPane;
	}

	public Pane getDescriptionKeyPane() {
		return descriptionKeyPane;
	}

	public Pane getWhoKeyPane() {
		return whoKeyPane;
	}

	public Pane getTimeKeyPane() {
		return timeKeyPane;
	}

	public Pane getCompleteKeyPane() {
		return completeKeyPane;
	}

	public Pane getReadyKeyPane() {
		return readyKeyPane;
	}

	public Pane getSkippedKeyPane() {
		return skippedKeyPane;
	}

	public Pane getInProgressKeyPane() {
		return inProgressKeyPane;
	}

	public VBox getTreeViewVBox() {
		return treeViewVBox;
	}

	public TreeView<String> getTreeView() {
		return treeView;
	}

	public VBox getLocalProgressVBox() {
		return localProgressVBox;
	}

	public VBox getMainVBox() {
		return mainVBox;
	}

	public HBox getStatusHBox() {
		return statusHBox;
	}

	public HBox getBody2HBox() {
		return body2HBox;
	}

	public VBox getContentVBox() {
		return contentVBox;
	}

	public HBox getAttributePanelHBox() {
		return attributePanelHBox;
	}

	public Label getAttributePanelCollapseLabel() {
		return attributePanelCollapseLabel;
	}

	public VBox getAttributePanelContentVBox() {
		return attributePanelContentVBox;
	}

	public Label getGoalProgressPercentLabel() {
		return goalProgressPercentLabel;
	}

	public Label getGoalConfidencePercentLabel() {
		return goalConfidencePercentLabel;
	}

	public VBox getGoalProgressVBox() {
		return goalProgressVBox;
	}

	public VBox getGoalProgressBar() {
		return goalProgressBar;
	}

	public VBox getGoalConfidenceVBox() {
		return goalConfidenceVBox;
	}

	public VBox getGoalConfidenceBar() {
		return goalConfidenceBar;
	}

	public ProgressIndicator getChapterProgressIndicator() {
		return chapterProgressIndicator;
	}

	public Button getPreviousButton() {
		return previousButton;
	}

	public Button getSkipButton() {
		return skipButton;
	}

	public Button getNextButton() {
		return nextButton;
	}

	public ToggleGroup getActivityStatusToggleGroup() {
		return activityStatusToggleGroup;
	}

	public RadioButton getReadyRadioButton() {
		return readyRadioButton;
	}

	public RadioButton getInProgressRadioButton() {
		return inProgressRadioButton;
	}

	public RadioButton getCompleteRadioButton() {
		return completeRadioButton;
	}

}
