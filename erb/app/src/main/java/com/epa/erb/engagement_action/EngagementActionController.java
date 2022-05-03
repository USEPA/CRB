package com.epa.erb.engagement_action;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Activity;
import com.epa.erb.Chapter;
import com.epa.erb.Constants;
import com.epa.erb.XMLManager;
import com.epa.erb.chapter.ChapterLandingController;
import com.epa.erb.chapter.PlanController;
import com.epa.erb.chapter.ReflectController;
import com.epa.erb.noteboard.NoteBoardContentController;
import com.epa.erb.worksheet.WorksheetContentController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class EngagementActionController implements Initializable{

	@FXML
	HBox headingHBox;
	@FXML
	HBox pathwayHBox;
	@FXML
	HBox mainHBox;
	@FXML
	HBox statusHBox;
	@FXML
	HBox attributeCollapseHBox;
	@FXML
	Label chapterLabel;
	@FXML
	Label erbPathwayLabel;
	@FXML
	Label attributePanelCollapseLabel;
	@FXML
	TreeView<String> treeView;
	@FXML
	VBox contentVBox;
	@FXML
	VBox controlsVBox;
	@FXML
	VBox attributeVBox;
	@FXML
	VBox keyVBox;
	@FXML
	Button startButton;
	@FXML
	Button completeButton;
	@FXML
	Button saveButton;
	@FXML
	Button previousButton;
	@FXML
	Button skipButton;
	@FXML
	Button nextButton;
	@FXML
	Pane materialKeyPane;
	@FXML
	Pane descriptionKeyPane;
	@FXML
	Pane whoKeyPane;
	@FXML
	Pane completeKeyPane;
	@FXML
	Pane readyKeyPane;
	@FXML
	Pane skippedKeyPane;
	@FXML
	Pane inProgressKeyPane;
	@FXML
	Pane timeKeyPane;
	@FXML
	ScrollPane attributeScrollPane;
	
	private File projectDirectory;
	private File dataFile;
	public EngagementActionController(File projectDirectory, File dataFile) {
		this.projectDirectory = projectDirectory;
		this.dataFile = dataFile;
	}
	
	private Activity currentActivity = null; //Tracks the current user selected chapter
	private Constants constants = new Constants();
	private ArrayList<Chapter> dataChapters = new ArrayList<Chapter>(); //List of chapter objects parsed from .xml file 
	private Logger logger = LogManager.getLogger(EngagementActionController.class);
	private HashMap<TreeItem<String>, String> treeMap = new HashMap<TreeItem<String>, String>(); //Holds the tree items mapped to a chapter name or activity ID
	private ArrayList<AttributePanelController> listOfAttributePanelControllers = new ArrayList<AttributePanelController>(); //Holds all of the attribute panel that are loaded
	private ArrayList<ERBPathwayDiagramController> listOfPathwayDiagramControllers = new ArrayList<ERBPathwayDiagramController>();
	
	//private String pathToERBFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\").replace("\\", "\\\\");
	private String pathToERBFolder = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		parseDataFromSetup();
		fillTreeView();
		handleNavigationButtonsShown(null, null);
		handleControls();
		loadChapterERBPathway();
		initializeStyle();
		initializeTreeViewSelection();
	}
	
	private void initializeTreeViewSelection() {
		String activityGUID = "0";
		for (TreeItem<String> treeItem : treeMap.keySet()) {
			if (treeMap.get(treeItem) == activityGUID) {
				getTreeView().getSelectionModel().select(treeItem);
				treeViewClicked();
			}
		}
	}
	
	private void handleControls() {
		treeView.setOnMouseClicked(e-> treeViewClicked());
	}
	
	private void initializeStyle() {
		materialKeyPane.setStyle("-fx-background-color: " + constants.getMaterialsColor() + ";");
		descriptionKeyPane.setStyle("-fx-background-color: " + constants.getDescriptionColor() + ";");
		whoKeyPane.setStyle("-fx-background-color: " + constants.getWhoColor() + ";");
		completeKeyPane.setStyle("-fx-background-color: " + constants.getCompleteStatusColor() + ";");
		readyKeyPane.setStyle("-fx-background-color: " + constants.getReadyStatusColor() + ";");
		skippedKeyPane.setStyle("-fx-background-color: " + constants.getSkippedStatusColor() + ";");
		inProgressKeyPane.setStyle("-fx-background-color: " + constants.getInProgressStatusColor() + ";");
		timeKeyPane.setStyle("-fx-background-color: " + constants.getTimeColor() + ";");
	}
	
	private void loadERBLandingContent() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/ChapterLanding.fxml"));
			ChapterLandingController chapterLandingController = new ChapterLandingController(dataChapters, this);
			fxmlLoader.setController(chapterLandingController);
			Parent root = fxmlLoader.load();
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
			Parent root = fxmlLoader.load();
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
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void loadSampleNB() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/NoteBoardContent.fxml"));
			NoteBoardContentController noteBoardContentController = new NoteBoardContentController();
			fxmlLoader.setController(noteBoardContentController);
			Parent root = fxmlLoader.load();
			contentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void loadChapterPlan(Chapter chapter) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/Plan.fxml"));
			PlanController planController = new PlanController(chapter);
			fxmlLoader.setController(planController);
			Parent root = fxmlLoader.load();
			contentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void loadChapterReflect(Chapter chapter) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chapter/Reflect.fxml"));
			ReflectController reflectController = new ReflectController(chapter);
			fxmlLoader.setController(reflectController);
			Parent root = fxmlLoader.load();
			contentVBox.getChildren().add(root);
			VBox.setVgrow(root, Priority.ALWAYS);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	void loadAttributeInfo(String attributeLabel, String attributeText, String attributeColor) {
		if (attributeText.trim().length() > 0 && !containsAttribute(attributeLabel)) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/AttributePane.fxml"));
				AttributePanelController attributePanelController = new AttributePanelController(this);
				fxmlLoader.setController(attributePanelController);
				Parent root = fxmlLoader.load();
				attributePanelController.setAttributeFields(attributeText,attributeLabel, attributeColor);
				attributeVBox.getChildren().add(root);
				VBox.setVgrow(root, Priority.ALWAYS);
				listOfAttributePanelControllers.add(attributePanelController);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	@FXML
	public void startButtonAction() {
		TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		String GUID = treeMap.get(selectedTreeItem);
		Activity activity = getActivity(GUID);
		activity.setStatus("in progress"); //set status of activity
		
		ERBPathwayDiagramController erbPathwayDiagramController = getErbPathwayDiagramController(GUID);
		if(erbPathwayDiagramController != null) {
			erbPathwayDiagramController.updateStatus(); //set status of erb diagram
		}
	}
	
	@FXML
	public void completeButtonAction() {
		TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		String GUID = treeMap.get(selectedTreeItem);
		Activity activity = getActivity(GUID);
		activity.setStatus("complete"); //set status of activity
		
		ERBPathwayDiagramController erbPathwayDiagramController = getErbPathwayDiagramController(GUID);
		if(erbPathwayDiagramController != null) {
			erbPathwayDiagramController.updateStatus(); //set status of erb diagram
		}
	}
	
	@FXML
	public void previousButtonAction() {
		TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		TreeItem<String> parentTreeItem = selectedTreeItem.getParent();
		if (selectedTreeItem != null && parentTreeItem != null) {
			int currentIndex = parentTreeItem.getChildren().indexOf(selectedTreeItem);
			TreeItem<String> itemToSelect = parentTreeItem.getChildren().get(currentIndex - 1);
			treeView.getSelectionModel().select(itemToSelect);
			treeViewClicked();
		}
	}
	
	@FXML
	public void skipButtonAction() {
		
	}
	
	@FXML
	public void attributePanelCollapseClicked() {
		String attributePanelCollapseString = attributePanelCollapseLabel.getText();
		if(attributePanelCollapseString.contentEquals(">")) {
			collapseAttributes();
			attributeScrollPane.setMinWidth(0.0);
			attributePanelCollapseLabel.setText("<");

		} else if (attributePanelCollapseString.contentEquals("<")) {
			unCollapseAttributes();
			attributeScrollPane.setMinWidth(200.0);
			attributePanelCollapseLabel.setText(">");
		}
	}
	
	private void collapseAttributes() {
		if(attributeCollapseHBox.getChildren().contains(attributeVBox)) {
			attributeCollapseHBox.getChildren().remove(attributeVBox);
		}
	}
	
	private void unCollapseAttributes() {
		if(!attributeCollapseHBox.getChildren().contains(attributeVBox)) {
			attributeCollapseHBox.getChildren().add(1, attributeVBox);
		}
	}
	
	@FXML
	public void nextButtonAction() {
		TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		TreeItem<String> parentTreeItem = selectedTreeItem.getParent();
		if (selectedTreeItem != null && parentTreeItem != null) {
			if (parentTreeItem.getValue().contains("ERB")) {
				TreeItem<String> itemToSelect = selectedTreeItem.getChildren().get(0);
				treeView.getSelectionModel().select(itemToSelect);
			} else {
				int currentIndex = parentTreeItem.getChildren().indexOf(selectedTreeItem);
				TreeItem<String> itemToSelect = parentTreeItem.getChildren().get(currentIndex + 1);
				treeView.getSelectionModel().select(itemToSelect);
			}
			treeViewClicked();
		}
	}
	
	@FXML
	public void saveButtonAction() {
		File dataFile = new File(pathToERBFolder + "\\EngagementActionTool\\" + projectDirectory.getName() + "\\Data.xml");
		XMLManager xmlManager = new XMLManager();
		xmlManager.writeDataXML(dataFile, dataChapters);
	}
	
	public void treeViewClicked() {
		TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
		if (selectedTreeItem != null) {
			TreeItem<String> parentTreeItem = selectedTreeItem.getParent();
			if (parentTreeItem != null) {
				String parentTreeItemValue = parentTreeItem.getValue().trim();
				String selectedTreeItemValue = selectedTreeItem.getValue().trim();
				if (selectedTreeItemValue.length() > 0) {
					if (parentTreeItemValue.contains("ERB")) { // Is Chapter
						Chapter currentChapter = getChapter(selectedTreeItemValue);
						currentActivity = null;
						cleanContentVBox();
						cleanAttributeVBox();
						addColorKey(1);
						removeAttributePane();
						loadChapterLandingContent(currentChapter);
						handleNavigationButtonsShown(null, null);
						loadActivityERBPathway(currentChapter);
						removeStatusPanel();
					} else { // Is Activity
						String activityGUID = treeMap.get(selectedTreeItem);
						Activity selectedActivity = getActivity(activityGUID);
						if(currentActivity != selectedActivity) { //If a new activity is selected
							Chapter currentChapter = getChapter(parentTreeItemValue);
							addAttributePanel(1);
							addColorKey(1);
							cleanContentVBox();
							cleanAttributeVBox();
							loadActivityContentPanel(selectedTreeItem);
							loadActivityERBPathway(currentChapter);
							highlightSelectedActivityDiagram(selectedTreeItem);
							currentActivity = selectedActivity;
							addStatusPanel();
						}
						handleNavigationButtonsShown(selectedTreeItem, parentTreeItem);
					}
				} else {
					currentActivity = null;
				}
			} else { //ERB Pathway 
				if (selectedTreeItem != null) {
					currentActivity = null;
					cleanContentVBox();
					cleanAttributeVBox();
					removeAttributePane();
					removeColorKey();
					loadChapterERBPathway();
					loadERBLandingContent();
					handleNavigationButtonsShown(null, null);
					removeStatusPanel();
				}
			}
		} else {
			currentActivity = null;
			handleNavigationButtonsShown(null, null);
		}
	}
	
	private void highlightSelectedActivityDiagram(TreeItem<String> selectedTreeItem) {
		String activityGUID = treeMap.get(selectedTreeItem);
		Activity selectedActivity = getActivity(activityGUID);
		for(ERBPathwayDiagramController erbPathwayDiagramController: listOfPathwayDiagramControllers) {
			if(erbPathwayDiagramController.getActivity() == selectedActivity) {
				erbPathwayDiagramController.highlightDiagram();
			} else {
				erbPathwayDiagramController.unHighlightDiagram();
			}
		}
	}
	
	private void loadActivityContentPanel(TreeItem<String> selectedTreeItem) {
		String activityGUID = treeMap.get(selectedTreeItem);
		Activity selectedActivity = getActivity(activityGUID);
		if (selectedActivity.getActivityType().getDescription().contentEquals("worksheet")) {
			if (!selectedActivity.getLongName().contentEquals("Plan") && !selectedActivity.getLongName().contentEquals("Reflect")) {
				loadAttributeInfo("Objective", selectedActivity.getObjectives(), constants.getObjectivesColor());
				loadAttributeInfo("Linked Activities", selectedActivity.getLinksString(), constants.getLinksColor());
				loadAttributeInfo("Instructions", selectedActivity.getDirections(), constants.getInstructionsColor());
				loadSampleWK(selectedActivity);
			} else if (selectedActivity.getLongName().contentEquals("Plan")) {
				removeAttributePane();
				loadChapterPlan(getChapter(selectedActivity));
			} else if (selectedActivity.getLongName().contentEquals("Reflect")) {
				removeAttributePane();
				loadChapterReflect(getChapter(selectedActivity));
			}
		} else if (selectedActivity.getActivityType().getDescription().contentEquals("noteboard")) {
			loadAttributeInfo("Objective", selectedActivity.getObjectives(), constants.getObjectivesColor());
			loadAttributeInfo("Linked Activities", selectedActivity.getLinksString(), constants.getLinksColor());
			loadAttributeInfo("Instructions",
					"1. On a white board, list the hazards of concern to your community in a column down the left side, and draw horizontal lines between them. (see example)"
							+ "\n"
							+ "2. Reflect on the stories and data that were shared at the beginning of the workshop. Who were the groups of people that were mentioned who experienced impacts from hazards and disasters? What were the impacts they experienced?"
							+ "\n"
							+ "3. As you go, add additional who's and what's for current or potential future impacts"
							+ "\n"
							+ "4. After about 30 minutes or when the discussion is at a lull, begin discussing why these impacts happen. Give everyone a few minutes to think and write some \"why's\" on pink post-it notes. Then have people place the pink notes near the blue and yellow notes, and share their thoughts with the group"
							+ "\n"
							+ "5. After about 15 minutes, introduce the phases of disaster mitigation-response-recovery. Use colored dots to label each of the \"why's\" with one or more phases",
					constants.getInstructionsColor());
			loadSampleNB();
		}
	}
	
	private void loadChapterERBPathway() {
		cleanPathwayHBox();
		setChapterLabelText("Chapters");
		int count = 0;
		for (Chapter chapter : dataChapters) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/ERBChapterDiagram.fxml"));
				ERBChapterDiagramController erbChapterDiagramController = new ERBChapterDiagramController(chapter,this);
				fxmlLoader.setController(erbChapterDiagramController);
				Parent root = fxmlLoader.load();
				handleChapterDiagramLines(count, erbChapterDiagramController);
				pathwayHBox.getChildren().add(root);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			count++;
		}
	}
	
	private void handleChapterDiagramLines(int count, ERBChapterDiagramController erbChapterDiagramController) {
		if(dataChapters.size() == 1 && count == 0) {
			erbChapterDiagramController.hideLeftLeadingLine();
			erbChapterDiagramController.hideRightLeadingLine();
		} else if(count == dataChapters.size()-1) {
			erbChapterDiagramController.hideRightLeadingLine();
		} else if(count ==0) {
			erbChapterDiagramController.hideLeftLeadingLine();
		}
	}


	private void loadActivityERBPathway(Chapter chapter) {
		if (chapter != null) {
			if (chapterLabel.getText() == null || !chapterLabel.getText().contentEquals(chapter.getStringName())) { // If a new chapter is selected
				cleanPathwayHBox();
				cleanListOfActivityDiagrams();
				setChapterLabelText(chapter.getStringName() + " Activities");
				int count = 0;
				for (Activity activity : chapter.getUserSelectedActivities()) {
					try {
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/ERBPathwayDiagram.fxml"));
						ERBPathwayDiagramController erbPathwayDiagramController = new ERBPathwayDiagramController(activity, this);
						fxmlLoader.setController(erbPathwayDiagramController);
						Parent root = fxmlLoader.load();
						handleActivityDiagramLines(chapter, count, erbPathwayDiagramController);
						pathwayHBox.getChildren().add(root);
						listOfPathwayDiagramControllers.add(erbPathwayDiagramController);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
					count++;
				}
			}
		}
	}
	
	private void handleActivityDiagramLines(Chapter chapter, int count,  ERBPathwayDiagramController erbPathwayDiagramController) {
		if (chapter.getNumberOfUserSelectedActivities() == 1) {
			erbPathwayDiagramController.hideLeftLeadingLine();
			erbPathwayDiagramController.hideRightLeadingLine();
		} else if (count == chapter.getNumberOfUserSelectedActivities() - 1) {
			erbPathwayDiagramController.hideRightLeadingLine();
		} else if (count == 0) {
			erbPathwayDiagramController.hideLeftLeadingLine();
		}
	}
	
	private void fillTreeView() {
		TreeItem<String> rootTreeItem = new TreeItem<String>("ERB Pathway");
		rootTreeItem.setExpanded(true);
		treeView.setRoot(rootTreeItem);
		treeMap.put(rootTreeItem, "0");
		for (Chapter chapter : dataChapters) {
			TreeItem<String> chapterTreeItem = new TreeItem<String>(chapter.getStringName());
			rootTreeItem.getChildren().add(chapterTreeItem);
			treeMap.put(chapterTreeItem, chapter.getNumericName());
			for (Activity activity : chapter.getUserSelectedActivities()) {
				TreeItem<String> activityTreeItem = new TreeItem<String>(activity.getLongName());
				chapterTreeItem.getChildren().add(activityTreeItem);
				treeMap.put(activityTreeItem, activity.getGUID());
			}
		}
	}
	
	private void handleNavigationButtonsShown(TreeItem<String> selectedTreeItem, TreeItem<String> parentTreeItem) {
		if (selectedTreeItem != null) {
			if (parentTreeItem == null) {
				previousButton.setDisable(true);
				skipButton.setDisable(true);
				nextButton.setDisable(false);
			} else {
				int numberOfChildrenInParent = parentTreeItem.getChildren().size();
				if (numberOfChildrenInParent > 1) {
					int indexOfSelectedItem = parentTreeItem.getChildren().indexOf(selectedTreeItem);
					if (indexOfSelectedItem == 0) {
						previousButton.setDisable(true);
						skipButton.setDisable(true); //true
						nextButton.setDisable(false);
					} else if (indexOfSelectedItem == parentTreeItem.getChildren().size() - 1) {
						previousButton.setDisable(false);
						skipButton.setDisable(true);
						nextButton.setDisable(true);
					} else {
						previousButton.setDisable(false);
						skipButton.setDisable(true); //true
						nextButton.setDisable(false);
					}
				} else {
					previousButton.setDisable(true);
					skipButton.setDisable(true);
					nextButton.setDisable(true);
				}
			}
		} else {
			previousButton.setDisable(true);
			skipButton.setDisable(true);
			nextButton.setDisable(true);
		}
	}
	
	/**
	 * Parses the chapter data that was create by the user in pt. 1 of the tool
	 */
	private void parseDataFromSetup() {
		//File dataFile = new File(pathToERBFolder + "\\EngagementSetupTool\\" + projectDirectory.getName() + "\\Data.xml");
		XMLManager xmlManager = new XMLManager();
		dataChapters = xmlManager.parseDataXML(dataFile);
	}
		
	private void cleanAttributeVBox() {
		attributeVBox.getChildren().clear();
		listOfAttributePanelControllers.clear();
	}
	
	private void cleanPathwayHBox() {
		pathwayHBox.getChildren().clear();
	}

	private void cleanContentVBox() {
		contentVBox.getChildren().clear();
	}
	
	private void cleanListOfActivityDiagrams() {
		listOfPathwayDiagramControllers.clear();
	}
	
	private void addColorKey(int index) {
		if(!headingHBox.getChildren().contains(keyVBox)) {
			headingHBox.getChildren().add(index, keyVBox);
		}
	}
	
	private void removeColorKey() {
		headingHBox.getChildren().remove(keyVBox);
	}
	
	private void addAttributePanel(int index) {
		if(!mainHBox.getChildren().contains(attributeScrollPane)) {
			mainHBox.getChildren().add(index, attributeScrollPane);
		}
	}
	
	private void removeAttributePane() {
		mainHBox.getChildren().remove(attributeScrollPane);
	}
	
	private void removeStatusPanel() {
		if(controlsVBox.getChildren().contains(statusHBox)) {
			controlsVBox.getChildren().remove(statusHBox);
		}
	}
	
	private void addStatusPanel() {
		if(!controlsVBox.getChildren().contains(statusHBox)) {
			controlsVBox.getChildren().add(0, statusHBox);
		}
	}
	
	void removeAttributePanelController(AttributePanelController attributePanelController) {
		listOfAttributePanelControllers.remove(attributePanelController);
	}
	
	private boolean containsAttribute(String attributeLabel) {
		for(AttributePanelController attributePanelController : listOfAttributePanelControllers) {
			if(attributePanelController.getAttributeLabelText().contentEquals(attributeLabel)) {
				return true;
			}
		}
		return false;
	}
	
	private Chapter getChapter(String chapterName) {
		for(Chapter chapter : dataChapters) {
			if(chapter.getStringName().contentEquals(chapterName)) {
				return chapter;
			}
		}
		logger.debug("Chapter returned is null");
		return null;
	}
	
	public Chapter getChapter(Activity activity) {
		for(Chapter chapter : dataChapters) {
			for(Activity chapterActivity: chapter.getUserSelectedActivities()) {
				if(chapterActivity.getGUID().contentEquals(activity.getGUID())) {
					return chapter;
				}
			}
		}
		logger.debug("Chapter returned is null");
		return null;
	}
	
	private Activity getActivity(String guid) {
		for(Chapter chapter: dataChapters) {
			for(Activity activity : chapter.getUserSelectedActivities()) {
				if(activity.getGUID().contentEquals(guid)) {
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
			return treeMap.get(treeItem);
		} else {
			logger.debug("Selected Activity GUID is null");
			return null;
		}
	}
	
	public ERBPathwayDiagramController getErbPathwayDiagramController(String GUID) {
		for(ERBPathwayDiagramController erbPathwayDiagramController : listOfPathwayDiagramControllers) {
			if(erbPathwayDiagramController.getActivity().getGUID().contentEquals(GUID)) {
				return erbPathwayDiagramController;
			}
		}
		return null;
	}
		
	private void setChapterLabelText(String chapterLabelText) {
		chapterLabel.setText(chapterLabelText);
	}
		
	public TreeView<String> getTreeView() {
		return treeView;
	}
	
	public HashMap<TreeItem<String>, String> getTreeMap() {
		return treeMap;
	}

	public ScrollPane getAttributeScrollPane() {
		return attributeScrollPane;
	}
	
	public ArrayList<Chapter> getDataChapters(){
		return dataChapters;
	}
		
}
