package com.epa.erb.engagement_setup;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.epa.erb.Activity;
import com.epa.erb.ActivityType;
import com.epa.erb.Chapter;
import com.epa.erb.ERBMainController;
import com.epa.erb.engagement_action.EngagementActionController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class EngagementSetupController implements Initializable {

	@FXML
	HBox erbPathwayHBox;
	@FXML
	ListView<ActivityType> activitityTypeListView;
	@FXML
	TextArea activityTypeDescriptionTextField;
	@FXML
	Button addChapterButton;
	@FXML
	VBox selectedActivitesVBox;
	@FXML
	ListView<Activity> customizedActivitiesListView;
	@FXML
	TextArea shortNameTextField;
	@FXML
	TextArea longNameTextField;
	@FXML
	TextArea descriptionTextField;
	@FXML
	TextArea directionsTextField;
	@FXML
	TextArea objectivesTextField;
	@FXML
	Hyperlink fileNameHyperlink;
	@FXML
	Button assignButton;
	@FXML
	Button doEngagementButton;
	@FXML
	Button saveDataButton;
	
	private ERBMainController erbMainController;
	private File projectDirectory;
	public EngagementSetupController(ERBMainController erbMainController, File projectDirectory) {
		this.erbMainController = erbMainController;
		this.projectDirectory = projectDirectory;
	}
	
	private String selectedChapter = null;	//Tracks the current user selected chapter they are adding activities to
	private ArrayList<Chapter> chaptersCreated = new ArrayList<Chapter>();	//List of Chapter objects created by the user
	private ArrayList<Activity> customizedActivities = new ArrayList<Activity>();	//List of Activities parsed from .xml file
	private ArrayList<ActivityType> activityTypes = new ArrayList<ActivityType>();	//List of ActivityTypes parsed from .xml file
	private Logger logger = LogManager.getLogger(EngagementSetupController.class);
	private ArrayList<ChapterTitledPaneController> chapterTitledPaneControllers = new ArrayList<ChapterTitledPaneController>(); //List of ChapterTitledPaneControllers for all chapters created by the user

	//private String pathToERBFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\").replace("\\", "\\\\");
	private String pathToERBFolder = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		parseActivityTypes();
		parseAvailableActivities();
		handleControls();
		fillActivitiesListView();
		setActivityTypeListViewCellFactory();
		setActivityTypeListViewDrag(activitityTypeListView);
		if(projectDirectoryHasDataFile(projectDirectory)) {
			loadExisitingProjectData();
		}
	}
	
	private void handleControls() {
		fileNameHyperlink.setOnMouseClicked(e-> fileNameHyperlinkClicked());
		activitityTypeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateActivityTypeDescriptionTextArea());
		customizedActivitiesListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateCustomizedActivityInfo());
	}
	
	private void fillActivitiesListView() {
		for (ActivityType activityType : activityTypes) {
			activitityTypeListView.getItems().add(activityType);
		}
	}
	
	private void loadExisitingProjectData() {
		System.out.println("LOAD");
		File dataFile = new File(projectDirectory.getPath() + "\\Data.xml");
		if (dataFile.exists() && dataFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(dataFile);
				doc.getDocumentElement().normalize();
				NodeList chapterNodeList = doc.getElementsByTagName("chapter");
				for (int i = 0; i < chapterNodeList.getLength(); i++) {
					Node chapterNode = chapterNodeList.item(i);
					// Chapter
					if (chapterNode.getNodeType() == Node.ELEMENT_NODE) {
						Element chapterElement = (Element) chapterNode;
						int chapterNum = Integer.parseInt(chapterElement.getAttribute("chapterNum"));
						String chapterNumericName = chapterElement.getAttribute("numericName");
						String chapterStringName = chapterElement.getAttribute("stringName");
						String chapterDescription = chapterElement.getAttribute("description");
						Chapter chapter = new Chapter(chapterNum, chapterNumericName, chapterStringName,chapterDescription);
						//CODE TO ADD CHAPTERS
						chaptersCreated.add(chapter);
						ChapterTitledPaneController chapterTitledPaneController = loadChapterTitledPaneController(chapter);
						if (chapterTitledPaneController != null) {
							handleTitledPaneListViewAccess(chapterTitledPaneController);
							storeTitledPaneController(chapterTitledPaneController);
						}
						
						NodeList activityNodeList = chapterNode.getChildNodes();
						for (int j = 0; j < activityNodeList.getLength(); j++) {
							Node activityNode = activityNodeList.item(j);
							// Activity
							if (activityNode.getNodeType() == Node.ELEMENT_NODE) {
								Element activityElement = (Element) activityNode;
								String activityStatus = activityElement.getAttribute("status");
								String activityShortName = activityElement.getAttribute("shortName");
								String activityLongName = activityElement.getAttribute("longName");
								String activityFileName = activityElement.getAttribute("fileName");
								String activityDirections = activityElement.getAttribute("directions");
								String activityObjectives = activityElement.getAttribute("objectives");
								String activityDescription = activityElement.getAttribute("description");
								String activityMaterials= activityElement.getAttribute("materials");
								String activityTime = activityElement.getAttribute("time");
								String activityWho = activityElement.getAttribute("who");
								String activityID = activityElement.getAttribute("activityID");
								String activityGUID = activityElement.getAttribute("guid");
								
								//Create new activity
								Activity activity = new Activity();
								activity.setStatus(activityStatus);
								activity.setShortName(activityShortName);
								activity.setLongName(activityLongName);
								activity.setFileName(activityFileName);
								activity.setDirections(activityDirections);
								activity.setObjectives(activityObjectives);
								activity.setDescription(activityDescription);
								activity.setMaterials(activityMaterials);
								activity.setTime(activityTime);
								activity.setWho(activityWho);
								activity.setActivityID(activityID);
								activity.setGUID(activityGUID);
								
								//Add activity type element to activity
								NodeList activityTypeNodeList = activityElement.getElementsByTagName("activityType");
								for (int k = 0; k < activityTypeNodeList.getLength(); k++) {
									Node activityTypeNode = activityTypeNodeList.item(k);
									// ActivityType
									if (activityTypeNode.getNodeType() == Node.ELEMENT_NODE) {
										Element activityTypeElement = (Element) activityTypeNode;
										String activityTypeLongName = activityTypeElement.getAttribute("longName");
										String activityTypeShortName = activityTypeElement.getAttribute("shortName");
										String activityTypeDescription = activityTypeElement.getAttribute("description");
										String activityTypeFileExt = activityTypeElement.getAttribute("fileExt");
										ActivityType activityType = new ActivityType(activityTypeLongName,activityTypeShortName, activityTypeDescription, activityTypeFileExt);
										activity.setActivityType(activityType);
									}
								}
								if (!activity.getLongName().contentEquals("Plan") && !activity.getLongName().contentEquals("Reflect")) {
									// CODE TO LOAD ACTIVITIES
									ActivityType activityType = activity.getActivityType();
									SelectedActivity selectedActivity = new SelectedActivity(activityType.getLongName(), activityType.getLongName());
									chapterTitledPaneController.getTitledPaneListView().getItems().add(selectedActivity);
									selectedActivity.setActivityID(activity.getActivityID());
									selectedActivity.setShowName(activity.getLongName());
									setTitledPaneListViewCellFactory(chapterTitledPaneController.getTitledPaneListView());
								}
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(dataFile.getPath() + " either does not exist or cannot be read");
		}
		//Create chapters
		//Add activities to chapters
	}
	
	private boolean projectDirectoryHasDataFile(File projectDirectory) {
		File [] projectFiles = projectDirectory.listFiles();
		if(projectFiles != null && projectFiles.length > 0) {
			for(File projectFile : projectFiles) {
				if(projectFile.getName().contentEquals("Data.xml")) {
					return true;
				}
			}
		}
		return false;
	}

	private void setActivityTypeListViewCellFactory() {
		activitityTypeListView.setCellFactory(new Callback<ListView<ActivityType>, ListCell<ActivityType>>() {
			@Override
			public ListCell<ActivityType> call(ListView<ActivityType> param) {
				ListCell<ActivityType> cell = new ListCell<ActivityType>() {
					@Override
					protected void updateItem(ActivityType item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getLongName());
						}
					}
				};
				return cell;
			}
		});
	}
	
	private void fillCustomizedActivitiesListView(String activityTypeName) {
		clearCustomizedActivityInfo();
		cleanCustomizedActivitiesListView();
		for (Activity customActivity : customizedActivities) {
			if (customActivity.getActivityType().getLongName().contentEquals(activityTypeName) && !customActivity.getShortName().contentEquals("Plan") && !customActivity.getShortName().contentEquals("Reflect")) {
				if(!customizedActivitiesListView.getItems().contains(customActivity)) {
					customizedActivitiesListView.getItems().add(customActivity);
				}
			}
		}
	}
	
	private void setCustomizedActivityListViewCellFactory() {
		customizedActivitiesListView.setCellFactory(new Callback<ListView<Activity>, ListCell<Activity>>() {
			@Override
			public ListCell<Activity> call(ListView<Activity> param) {
				ListCell<Activity> cell = new ListCell<Activity>() {
					@Override
					protected void updateItem(Activity item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getLongName());
						}
					}
				};
				return cell;
			}
		});
	}
	
	private void setTitledPaneListViewCellFactory(ListView<SelectedActivity> titledPaneListView) {
		titledPaneListView.setCellFactory(new Callback<ListView<SelectedActivity>, ListCell<SelectedActivity>>() {
			@Override
			public ListCell<SelectedActivity> call(ListView<SelectedActivity> param) {
				ListCell<SelectedActivity> cell = new ListCell<SelectedActivity>() {
					@Override
					protected void updateItem(SelectedActivity item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getShowName());
							setContextMenu(createContextMenu());
						}
					}
				};
				return cell;
			}
		});
	}
	
	private ContextMenu createContextMenu() {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem removeMenuItem = new MenuItem("Remove");
		removeMenuItem.setOnAction(e-> removeMenuItemAction());
		contextMenu.getItems().add(removeMenuItem);
		return contextMenu;
	}
	
	private void removeMenuItemAction() {
		removeChapterTitledPane();
		cleanCustomizedActivitiesListView();
	}
					
	@FXML
	public void addChapterButtonAction() {
		Chapter chapter = createChapter();
		chaptersCreated.add(chapter);
		ChapterTitledPaneController chapterTitledPaneController = loadChapterTitledPaneController(chapter);
		if (chapterTitledPaneController != null) {
			handleTitledPaneListViewAccess(chapterTitledPaneController);
			storeTitledPaneController(chapterTitledPaneController);
		}
	}
	
	private Chapter createChapter() {
		//Create and store a new Chapter object to represent the user added chapter
		int chapterNum = chaptersCreated.size() + 1;
		Chapter chapter = new Chapter(chapterNum, chapterNum + ".0", "Chapter " + chapterNum, "");
		return chapter;
	}
	
	private ChapterTitledPaneController loadChapterTitledPaneController(Chapter chapter) {
		// Load and add the TitledPane to the selected activities vbox
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_setup/ChapterTitledPane.fxml"));
			ChapterTitledPaneController chapterTitledPaneController = new ChapterTitledPaneController(chapter);
			fxmlLoader.setController(chapterTitledPaneController);
			TitledPane pane = fxmlLoader.load();
			VBox.setVgrow(pane, Priority.ALWAYS);
			selectedActivitesVBox.getChildren().add(pane);
			return chapterTitledPaneController;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	private void handleTitledPaneListViewAccess(ChapterTitledPaneController chapterTitledPaneController) {
		//Handle access to the TitledPaneListView in this class
		ListView<SelectedActivity> titledPaneListView = chapterTitledPaneController.getTitledPaneListView();
		setTitledPaneListViewCellFactory(titledPaneListView);
		setSelectedActivityListViewDrag(titledPaneListView);
		titledPaneListView.setOnMouseClicked(e -> selectedActivityListViewClicked(e, titledPaneListView, chapterTitledPaneController.getPaneTitle()));
	}
	
	private void storeTitledPaneController(ChapterTitledPaneController chapterTitledPaneController) {
		//Store the ChapterTitledPaneController for the user created chapter
		chapterTitledPaneControllers.add(chapterTitledPaneController);
	}

	/**
	 * Handles users assigning a customized activity to a selected activity. Updates the activityID and the show name to reflect the assigned customized activity.
	 */
	@FXML
	public void assignButtonAction() {
		Activity selectedCustomizedActivity = customizedActivitiesListView.getSelectionModel().getSelectedItem();
		for (ChapterTitledPaneController chapterTitledPaneController : chapterTitledPaneControllers) {
			if (chapterTitledPaneController.getPaneTitle().contentEquals(selectedChapter)) {
				SelectedActivity selectedActivity = chapterTitledPaneController.getTitledPaneListView().getSelectionModel().getSelectedItem();
				selectedActivity.setActivityID(selectedCustomizedActivity.getActivityID());
				selectedActivity.setShowName(selectedCustomizedActivity.getLongName());
				setTitledPaneListViewCellFactory(chapterTitledPaneController.getTitledPaneListView());
			}
		}
	}
	
	@FXML
	public void doEngagementButtonAction() {
		erbMainController.closeSetupStage();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/engagement_action/EngagementAction.fxml"));
			EngagementActionController engagementActionController = new EngagementActionController(projectDirectory);
			fxmlLoader.setController(engagementActionController);
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("ERB: Equitable Resilience Builder");
			stage.show();
		} catch (Exception e) {
			logger.fatal(e.getMessage());
		}
	}
	
	@FXML
	public void saveDataButtonAction() {
		addFinalSelectedActivitiesToChapters();
		storeFinalSelectedActivitiesAndChapters();
	}
	
	/**
	 * Writes the chapter data to an xml file to save for pt 2 of the tool
	 */
	private void storeFinalSelectedActivitiesAndChapters() {
		try {
			File dataFile = new File(pathToERBFolder + "\\EngagementSetupTool\\" + projectDirectory.getName() + "\\Data.xml");
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			Element rootElement = document.createElement("chapters");
			document.appendChild(rootElement);
			for(Chapter chapter: chaptersCreated) {
				Element chapterElement = document.createElement("chapter");
				chapterElement.setAttribute("chapterNum", Integer.toString(chapter.getChapterNum()));
				chapterElement.setAttribute("numericName", chapter.getNumericName());
				chapterElement.setAttribute("stringName", chapter.getStringName());
				chapterElement.setAttribute("description", chapter.getDescriptionName());
				for(Activity activity: chapter.getUserSelectedActivities()) {
					Element activityElement = document.createElement("activity");
					activityElement.setAttribute("status", activity.getStatus());
					activityElement.setAttribute("shortName", activity.getShortName());
					activityElement.setAttribute("longName", activity.getLongName());
					activityElement.setAttribute("fileName", activity.getFileName());
					activityElement.setAttribute("directions", activity.getDirections());
					activityElement.setAttribute("objectives", activity.getObjectives());
					activityElement.setAttribute("description", activity.getDescription());
					activityElement.setAttribute("materials", activity.getMaterials());
					activityElement.setAttribute("time", activity.getTime());
					activityElement.setAttribute("who", activity.getWho());
					activityElement.setAttribute("activityID", activity.getActivityID());
					activityElement.setAttribute("guid", generateGUID());
					
					Element activityTypeElement = document.createElement("activityType");
					activityTypeElement.setAttribute("longName", activity.getActivityType().getLongName());
					activityTypeElement.setAttribute("shortName", activity.getActivityType().getShortName());
					activityTypeElement.setAttribute("description",activity.getActivityType().getDescription());
					activityTypeElement.setAttribute("fileExt", activity.getActivityType().getFileExt());
					
					Element linkedIDSElement = document.createElement("linkedActivityIDS");
					for(Activity linkedActivity : activity.getListOfLinkedActivities()) {
						Element linkElement = document.createElement("link");
						linkElement.setAttribute("activityID", linkedActivity.getActivityID());
						linkedIDSElement.appendChild(linkElement);
					}
					
					activityElement.appendChild(activityTypeElement);
					activityElement.appendChild(linkedIDSElement);
					chapterElement.appendChild(activityElement);
				}
				rootElement.appendChild(chapterElement);
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			
			StreamResult file = new StreamResult(dataFile);
			transformer.transform(domSource, file);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * Add all user selected activities to the user created chapters. Also adds the Plan activity and Reflect activity
	 */
	private void addFinalSelectedActivitiesToChapters() {
		for (ChapterTitledPaneController chapterTitledPaneController : chapterTitledPaneControllers) {
			Chapter chapter = getChapter(chapterTitledPaneController.getPaneTitle());
			ListView<SelectedActivity> listView = chapterTitledPaneController.getTitledPaneListView();
			chapter.addUserSelectedActivity(getCustomizedPlanActivity());
			ObservableList<SelectedActivity> selectedActivities = listView.getItems();
			for (SelectedActivity selectedActivity : selectedActivities) {
				chapter.addUserSelectedActivity(getCustomizedActivity(selectedActivity.getActivityID()));
			}
			chapter.addUserSelectedActivity(getCustomizedReflectActivity());
		}
	}
	
	private String generateGUID() {
		UUID uuid = UUID.randomUUID();
		return String.valueOf(uuid);
	}
	
	/**
	 * Handles users clicking a selected activity. Calls a method to update the list of customized activities based on the type of selected activity the user has clicked. 
	 * 
	 * @param titledPaneListView
	 * @param paneTitle
	 */
	private void selectedActivityListViewClicked(MouseEvent mouseEvent, ListView<SelectedActivity> titledPaneListView, String paneTitle) {
		SelectedActivity selectedActivity = titledPaneListView.getSelectionModel().getSelectedItem();
		if (selectedActivity != null) {
			selectedChapter = paneTitle;
			fillCustomizedActivitiesListView(selectedActivity.getActivityType());
			setCustomizedActivityListViewCellFactory();
		}
	}
	
	/**
	 * Handles users clicked the file hyperlink. Opens the hyper linked document.
	 */
	private void fileNameHyperlinkClicked() {
		try {
			String fileName = fileNameHyperlink.getAccessibleText().trim();
			File file = new File(pathToERBFolder + "\\Activities\\ChapterActivities\\" + fileName);
			if (file.exists() && Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(file);
			} else {
				logger.error(file.getPath() + " either does not exist or desktop is not supported.");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * Updates the activity type description according to the selected activity type.
	 */
	private void updateActivityTypeDescriptionTextArea() {
		ActivityType selectedActivityType = activitityTypeListView.getSelectionModel().getSelectedItem();
		for(ActivityType activityType: activityTypes) {
			if(activityType.getLongName().contentEquals(selectedActivityType.getLongName())) {
				activityTypeDescriptionTextField.setText(activityType.getDescription());
			}
		}
	}
	
	/**
	 * Updates the customized activity fields according to the selected customized activity information.
	 */
	private void updateCustomizedActivityInfo() {
		Activity selectedCustomizedActivity = customizedActivitiesListView.getSelectionModel().getSelectedItem();
		if (selectedCustomizedActivity != null) {
			for (Activity customActivity : customizedActivities) {
				if (customActivity.getLongName().contentEquals(selectedCustomizedActivity.getLongName())) {
					shortNameTextField.setText(customActivity.getShortName().replace("\r", "\n"));
					longNameTextField.setText(customActivity.getLongName().replace("\r", "\n"));
					descriptionTextField.setText(customActivity.getDescription().replace("\r", "\n"));
					directionsTextField.setText(customActivity.getDirections().replace("\r", "\n"));
					objectivesTextField.setText(customActivity.getObjectives().replace("\r", "\n"));
					fileNameHyperlink.setText(customActivity.getFileName().substring(2, customActivity.getFileName().length()));
					fileNameHyperlink.setAccessibleText(customActivity.getFileName());
				}
			}
		}
	}
	
	private void removeChapterTitledPane() {
		for (ChapterTitledPaneController chapterTitledPaneController : chapterTitledPaneControllers) {
			if (chapterTitledPaneController.getPaneTitle().contentEquals(selectedChapter)) {
				SelectedActivity selectedActivity = chapterTitledPaneController.getTitledPaneListView().getSelectionModel().getSelectedItem();
				chapterTitledPaneController.getTitledPaneListView().getItems().remove(selectedActivity);
				setTitledPaneListViewCellFactory(chapterTitledPaneController.getTitledPaneListView());
			}
		}
	}
	
	private void clearCustomizedActivityInfo() {
		shortNameTextField.setText(null);
		longNameTextField.setText(null);
		descriptionTextField.setText(null);
		directionsTextField.setText(null);
		objectivesTextField.setText(null);
		fileNameHyperlink.setText(null);
	}
	
	private void setActivityTypeListViewDrag(ListView<ActivityType> listView) {
		String TAB_DRAG_KEY = "listView";
		ObjectProperty<ListView<ActivityType>> draggingTab = new SimpleObjectProperty<ListView<ActivityType>>();
		listView.setOnDragDetected(event-> {
			Dragboard dragboard = listView.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent clipboardContent = new ClipboardContent();
			clipboardContent.putString(TAB_DRAG_KEY);
			dragboard.setContent(clipboardContent);
			draggingTab.set(listView);
			event.consume();
		});
	}
	
	private void setSelectedActivityListViewDrag(ListView<SelectedActivity> draggedListView) {
		String TAB_DRAG_KEY = "listView";
		ObjectProperty<ListView<SelectedActivity>> draggingTab = new SimpleObjectProperty<ListView<SelectedActivity>>();
		draggedListView.setOnDragOver(event-> {
			event.acceptTransferModes(TransferMode.MOVE);
			event.consume();
		});
		draggedListView.setOnDragDropped(event-> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if(db.hasString()) {			
				Object source = event.getGestureSource();				
				boolean equals = false;
				if(source == draggedListView) equals = true;
				
				if(equals == true) { //Would handle drag and drop within titled pane
				}else {
					@SuppressWarnings("unchecked")
					ListView<ActivityType> sourceListView = (ListView<ActivityType>) source;
					ActivityType selectedActivityType = (ActivityType) sourceListView.getSelectionModel().getSelectedItem();
					SelectedActivity selectedActivity = new SelectedActivity(selectedActivityType.getLongName(), selectedActivityType.getLongName());
					draggedListView.getItems().add(selectedActivity);
				}
				success = true;
			}
			event.setDropCompleted(success);
			event.consume();
		});
		draggedListView.setOnDragDetected(event-> {
			Dragboard dragboard = draggedListView.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent clipboardContent = new ClipboardContent();
			clipboardContent.putString(TAB_DRAG_KEY);
			dragboard.setContent(clipboardContent);
			draggingTab.set(draggedListView);
			event.consume();
		});
	}
	
	private void parseActivityTypes() {
		activityTypes.clear();
		File activityTypesFile = new File(pathToERBFolder + "\\Activities\\Activity_Types.xml");
		if (activityTypesFile.exists() && activityTypesFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(activityTypesFile);
				doc.getDocumentElement().normalize();
				NodeList activityTypeNodeList = doc.getElementsByTagName("activityType");
				for (int i = 0; i < activityTypeNodeList.getLength(); i++) {
					Node activityTypeNode = activityTypeNodeList.item(i);
					// Activity Type
					if (activityTypeNode.getNodeType() == Node.ELEMENT_NODE) {
						Element activityTypeElement = (Element) activityTypeNode;
						String longName = activityTypeElement.getAttribute("longName");
						String shortName = activityTypeElement.getAttribute("shortName");
						String description = activityTypeElement.getAttribute("description");
						String fileExt = activityTypeElement.getAttribute("fileExt");
						ActivityType activityType = new ActivityType(longName, shortName, description, fileExt);
						activityTypes.add(activityType);
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(activityTypesFile.getPath() + " either does not exist or cannot be read");
		}
	}

	private void parseAvailableActivities() {
		customizedActivities.clear();
		File activitesFile = new File(pathToERBFolder + "\\Activities\\Available_Activites.xml");
		if (activitesFile.exists() && activitesFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(activitesFile);
				doc.getDocumentElement().normalize();
				NodeList activitiesNodeList = doc.getElementsByTagName("activity");
				for (int i = 0; i < activitiesNodeList.getLength(); i++) {
					Node activityNode = activitiesNodeList.item(i);
					// Activity
					if (activityNode.getNodeType() == Node.ELEMENT_NODE) {
						Element activityElement = (Element) activityNode;
						String activityTypeName = activityElement.getAttribute("activityType");
						ActivityType activityType = getActivityType(activityTypeName);
						String status = activityElement.getAttribute("status");
						String shortName = activityElement.getAttribute("shortName");
						String longName = activityElement.getAttribute("longName");
						String fileName = activityElement.getAttribute("fileName");
						String directions = activityElement.getAttribute("directions");
						String objectives = activityElement.getAttribute("objectives");
						String description = activityElement.getAttribute("description");
						String materials = activityElement.getAttribute("materials");
						String time = activityElement.getAttribute("time");
						String who = activityElement.getAttribute("who");
						String activityID = activityElement.getAttribute("activityID");
						Activity activity = new Activity(activityType, status, shortName, longName, fileName, directions,
								objectives, description, materials, time, who, activityID);
						customizedActivities.add(activity);
						NodeList linkedActivityIDNodeList = activityElement.getElementsByTagName("linkedActivityIDS");
						for(int j =0; j < linkedActivityIDNodeList.getLength(); j++) {
							Node linkedActivityIDNode = linkedActivityIDNodeList.item(j);
							//Linked ActivityIDS
							if(linkedActivityIDNode.getNodeType() == Node.ELEMENT_NODE) {
								Element linkedActivityIDElement = (Element) linkedActivityIDNode;
								NodeList linksNodeList = linkedActivityIDElement.getElementsByTagName("link");
								for(int k=0; k < linksNodeList.getLength(); k++) {
									Node linkNode = linksNodeList.item(k);
									//Link
									if(linkNode.getNodeType() == Node.ELEMENT_NODE) {
										Element linkElement = (Element) linkNode;
										String linkID = linkElement.getAttribute("activityID");
										activity.addLinkedActivityID(linkID);
									}
								}
							}
						}
					}
				}
				setLinkActivities();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(activitesFile.getPath() + " either does not exist or cannot be read");
		}
	}
	
	private void setLinkActivities() {
		for (Activity activity : customizedActivities) {
			if (activity.getListOfLinkedActivityIDS().size() > 0) {
				for (String activityID : activity.getListOfLinkedActivityIDS()) {
					Activity linkedActivity = getCustomizedActivity(activityID);
					activity.addLinkedActivity(linkedActivity);
				}
			}
		}
	}
	
	private void cleanCustomizedActivitiesListView() {
		customizedActivitiesListView.getItems().clear();
	}
	
	private Chapter getChapter(String chapterName) {
		for(Chapter chapter: chaptersCreated) {
			if(chapter.getStringName().contentEquals(chapterName)) {
				return chapter;
			}
		}
		logger.debug("Chapter returned is null");
		return null;
	}
	
	private ActivityType getActivityType(String activityTypeName) {
		for(ActivityType activityType: activityTypes) {
			if(activityType.getLongName().contentEquals(activityTypeName)) {
				return activityType;
			}
		}
		logger.debug("ActivityType returned is null.");
		return null;
	}
	
	private Activity getCustomizedActivity(String activityID) {
		for(Activity activity: customizedActivities) {
			if(activity.getActivityID().contentEquals(activityID)) {
				return activity;
			}
		}
		logger.debug("Customized Activity returned is null.");
		return null;
	}
		
	private Activity getCustomizedPlanActivity() {
		for(Activity activity: customizedActivities) {
			if(activity.getShortName().contentEquals("Plan")) {
				return activity;
			}
		}
		logger.debug("Customized Plan Activity returned is null.");
		return null;
	}
	
	private Activity getCustomizedReflectActivity() {
		for(Activity activity: customizedActivities) {
			if(activity.getShortName().contentEquals("Reflect")) {
				return activity;
			}
		}
		logger.debug("Customized Reflect Activity returned is null.");
		return null;
	}
	
}
