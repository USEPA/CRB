package gov.epa.equitableresiliencebuilder.project;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gov.epa.equitableresiliencebuilder.App;
import gov.epa.equitableresiliencebuilder.utility.MainPanelHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;

public class ProjectSelectionController implements Initializable{

	private App app;
	public ProjectSelectionController(App app) {
		this.app = app;
	}
	
	@FXML
	VBox vBox;
	@FXML
	TextArea descriptionTextArea;
	@FXML
	TextField projectNameTextField;
	@FXML
	ListView<Project> projectsListView;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setProjectsListViewCellFactory();
		app.updateAvailableProjectsList();
		fillProjectsListView();
	}
	
	public void loadEngagementActionToContainer(Project project) {
		MainPanelHandler mainPanelHandler = new MainPanelHandler(app);
		Parent engagementActionRoot = mainPanelHandler.loadEngagementActionRoot(app, project);
		app.addNodeToERBContainer(engagementActionRoot);
	}
	
	private void fillProjectsListView() {
		ArrayList<Project> projects = app.getProjects();
		for (Project project : projects) {
			if(!project.getProjectCleanedName().contentEquals("Explore")) {
				projectsListView.getItems().add(project);
			}
		}
	}
	
	private void setProjectsListViewCellFactory() {
		projectsListView.setCellFactory(new Callback<ListView<Project>, ListCell<Project>>() {
			@Override
			public ListCell<Project> call(ListView<Project> param) {
				ListCell<Project> cell = new ListCell<Project>() {
					@Override
					protected void updateItem(Project item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getProjectName());
							setFont(new Font(14.0));
						}
					}
				};
				cell.setOnMouseClicked(e-> mouseClickedProject(e));
				return cell;
			}
		});
	}
	
	private void mouseClickedProject(MouseEvent mouseEvent) {
		if (mouseEvent == null || mouseEvent.getClickCount() == 2) {
			Project selectedProject = projectsListView.getSelectionModel().getSelectedItem();
			loadProject(selectedProject);
		} else if (mouseEvent.getClickCount() == 1 ) {
			Project selectedProject = projectsListView.getSelectionModel().getSelectedItem();
			descriptionTextArea.setText(selectedProject.getProjectDescription());
		}
	}
	
	public void loadProject(Project project) {
		if (project != null) {
			MainPanelHandler mainPanelHandler = new MainPanelHandler(app);
			app.setSelectedProject(project);
			app.getErbContainerController().getMyBreadCrumbBar().setProject(project);
			app.getErbContainerController().getMyBreadCrumbBar().addBreadCrumb(project.getProjectName() + " Project", mainPanelHandler.getMainPanelIdHashMap().get("Engagement"));
			loadEngagementActionToContainer(project);
		}
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public TextField getProjectNameTextField() {
		return projectNameTextField;
	}

	public ListView<Project> getProjectsListView() {
		return projectsListView;
	}
	
}
