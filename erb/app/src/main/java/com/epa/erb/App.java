/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.epa.erb;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.engagement_action.EngagementActionController;
import com.epa.erb.goal.Goal;
import com.epa.erb.goal.GoalCategory;
import com.epa.erb.project.Project;
import com.epa.erb.utility.Constants;
import com.epa.erb.utility.FileHandler;
import com.epa.erb.utility.MainPanelHandler;
import com.epa.erb.utility.XMLManager;

public class App extends Application {

	private int prefWidth;
	private int prefHeight;
	private XMLManager xmlManager;
	private Project selectedProject;
	private FileHandler fileHandler = new FileHandler();
	private Logger logger = LogManager.getLogger(App.class);
	private ERBContainerController erbContainerController;
	private EngagementActionController engagementActionController;

	private ArrayList<Project> projects;
	private ArrayList<IndicatorCard> indicatorCards;
	private ArrayList<GoalCategory> availableGoalCategories;	
	private ArrayList<ERBContentItem> availableERBContentItems;

	@Override
	public void start(Stage primaryStage) throws Exception {
		xmlManager = new XMLManager(this);
		setScreenResolutionPreferences(getScreenResolution());
		setScreenSizePreferences(getScreenSize());
		readAndStoreData();
		showERBToolMain();
	}

	public static void main(String[] args) {
		try {
			Application.launch(args);
		} catch (Exception e) {
		}
	}

	private void showERBToolMain() {
		Parent erbContainerRoot = loadERBContainer();
		launchERBLanding();
		showERBContainer(erbContainerRoot);
	}

	private int getScreenResolution() {
		return java.awt.Toolkit.getDefaultToolkit().getScreenResolution();
	}
	
	private Dimension getScreenSize() {
		return java.awt.Toolkit.getDefaultToolkit().getScreenSize();

	}

	private void setScreenResolutionPreferences(int resolution) {
		Constants constants = new Constants();
		if (resolution > 0 && resolution <= 96) {
			prefWidth = constants.getPrefWidthForScale100();
			prefHeight = constants.getPrefHeightForScale100();
		} else if (resolution > 96 && resolution <= 125) {
			prefWidth = constants.getPrefWidthForScale125();
			prefHeight = constants.getPrefHeightForScale125();
		} else if (resolution > 125 && resolution <= 144) {
			prefWidth = constants.getPrefWidthForScale150();
			prefHeight = constants.getPrefHeightForScale150();
		} else {
			prefWidth = constants.getPrefWidthForScale175();
			prefHeight = constants.getPrefHeightForScale175();
		}
	}
	
	private void setScreenSizePreferences(Dimension size) {
		System.out.println(size);
		double width = size.getWidth();
		double height = size.getHeight();
		
		Constants constants = new Constants();
		if(width >= 1680) {
			prefWidth = constants.getPrefWidthForScale100();
			prefHeight = constants.getPrefHeightForScale100();
		} else if (width >= 1280) {
			prefWidth = constants.getPrefWidthForScale125();
			prefHeight = constants.getPrefHeightForScale125();
		} else if (width >= 1024) {
			prefWidth = constants.getPrefWidthForScale150();
			prefHeight = constants.getPrefHeightForScale150();
		}
	}
	

	public void launchERBLanding() {
		MainPanelHandler mainPanelHandler = new MainPanelHandler();
		Parent erbLandingRoot = mainPanelHandler.loadERBLanding(this);
		loadNodeToERBContainer(erbLandingRoot);
	}

	private Parent loadERBContainer() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/erb/ERBContainer.fxml"));
			erbContainerController = new ERBContainerController(this);
			fxmlLoader.setController(erbContainerController);
			ScrollPane root = fxmlLoader.load();
			root.setPrefWidth(getPrefWidth());
			root.setPrefHeight(getPrefHeight());
			return root;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private Stage erbContainerStage = null;
	private void showERBContainer(Parent erbContainerRoot) {
		if (erbContainerRoot != null) {
			erbContainerStage = new Stage();
			Scene scene = new Scene(erbContainerRoot);
			erbContainerStage.setScene(scene);
			erbContainerStage.setOnCloseRequest(e -> erbCloseRequested());
			erbContainerStage.setTitle("ERB: Equitable Resilience Builder");
			erbContainerStage.show();
		} else {
			logger.error("Cannot show ERBContainer. erbContainerRoot is null.");
		}
	}

	private void erbCloseRequested() {
		if (engagementActionController != null) {
			Project project = engagementActionController.getProject();
			Goal goal = engagementActionController.getCurrentGoal();
			xmlManager.writeGoalMetaXML(fileHandler.getGoalMetaXMLFile(project, goal),engagementActionController.getListOfUniqueERBContentItems());
			fileHandler.createGUIDDirectoriesForGoal2(project, goal, engagementActionController.getListOfUniqueERBContentItems());
		}
	}

	private void readAndStoreData() {
		readAndStoreIndicatorCards();
		readAndStoreAvailableContent();
		readAndStoreAvailableGoalCategories();
		readAndStoreProjects();
	}
	
	private void readAndStoreIndicatorCards() {
		File contentFile = fileHandler.getStaticIndicatorCardsXMLFile();
		indicatorCards = xmlManager.parseIndicatorCardsXML(contentFile);
	}
	
	private void readAndStoreAvailableContent() {
		File contentFile = fileHandler.getStaticAvailableContentXMLFile();
		availableERBContentItems = xmlManager.parseContentXML(contentFile);
	}
	
	public ERBContentItem findERBContentItemForId(String id) {
		if (id != null) {
			for (ERBContentItem erbContentItem : availableERBContentItems) {
				if (erbContentItem.getId().contentEquals(id)) {
					return erbContentItem;
				}
			}
		}
		return null;
	}
	
	public IndicatorCard findIndicatorItemForId(String id) {
		if(id != null) {
			for(IndicatorCard indicatorCard: indicatorCards) {
				if(indicatorCard.getId().contentEquals(id)) {
					return indicatorCard;
				}
			}
		}
		return null;
	}

	private void readAndStoreAvailableGoalCategories() {
		File goalCategoriesFile = fileHandler.getStaticGoalCategoriesXMLFile();
		availableGoalCategories = xmlManager.parseGoalCategoriesXML(goalCategoriesFile);
	}

	private void readAndStoreProjects() {
		File projectsDirectory = fileHandler.getProjectsDirectory();
		projects = xmlManager.parseAllProjects(projectsDirectory );
	}
	
	public Project getExploreProject() {
		for(Project project: projects) {
			if(project.getProjectName().contentEquals("Explore")) {
				return project;
			}
		}
		return null;
	}

	public void loadNodeToERBContainer(Node node) {
		if (node != null) {
			setNodeGrowPriority(node, Priority.ALWAYS);
			erbContainerController.getErbContainer().getChildren().clear();
			erbContainerController.getErbContainer().getChildren().add(node);
		} else {
			logger.error("Cannot load node to ERBContainer. node is null.");
		}
	}

	public void setNodeGrowPriority(Node node, Priority priority) {
		if (node != null) {
			VBox.setVgrow(node, priority);
			HBox.setHgrow(node, priority);
		}
	}

	public void updateAvailableProjectsList() {
		readAndStoreProjects();
	}

	public String generateGUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public int getPrefWidth() {
		return prefWidth;
	}

	public void setPrefWidth(int prefWidth) {
		this.prefWidth = prefWidth;
	}

	public int getPrefHeight() {
		return prefHeight;
	}

	public void setPrefHeight(int prefHeight) {
		this.prefHeight = prefHeight;
	}

	public Project getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(Project selectedProject) {
		this.selectedProject = selectedProject;
	}

	public ArrayList<Project> getProjects() {
		return projects;
	}

	public void setProjects(ArrayList<Project> projects) {
		this.projects = projects;
	}

	public ArrayList<GoalCategory> getAvailableGoalCategories() {
		return availableGoalCategories;
	}

	public void setAvailableGoalCategories(ArrayList<GoalCategory> goalCategories) {
		this.availableGoalCategories = goalCategories;
	}

	public ERBContainerController getErbContainerController() {
		return erbContainerController;
	}

	public void setErbContainerController(ERBContainerController erbContainerController) {
		this.erbContainerController = erbContainerController;
	}

	public Stage getErbContainerStage() {
		return erbContainerStage;
	}

	public void setErbContainerStage(Stage erbContainerStage) {
		this.erbContainerStage = erbContainerStage;
	}

	public EngagementActionController getEngagementActionController() {
		return engagementActionController;
	}

	public void setEngagementActionController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}

	public ArrayList<ERBContentItem> getAvailableERBContentItems() {
		return availableERBContentItems;
	}

	public ArrayList<IndicatorCard> getIndicatorCards() {
		return indicatorCards;
	}
	

}