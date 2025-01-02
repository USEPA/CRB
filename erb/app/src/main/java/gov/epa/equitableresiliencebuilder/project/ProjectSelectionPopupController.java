package gov.epa.equitableresiliencebuilder.project;

import java.net.URL;
import java.util.ResourceBundle;

import gov.epa.equitableresiliencebuilder.App;
import gov.epa.equitableresiliencebuilder.forms.MainFormController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ProjectSelectionPopupController implements Initializable{

	private App app;
	private MainFormController formContentController;
	public ProjectSelectionPopupController(App app, MainFormController formContentController) {
		this.app = app;
		this.formContentController = formContentController;
	}
	
	@FXML
	ComboBox<Project> projectComboBox;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillProjectChoiceBox();
		setChoiceBoxCellFactory();
		projectComboBox.getSelectionModel().select(0);
	}
	
	private void fillProjectChoiceBox() {
		app.updateAvailableProjectsList();
		projectComboBox.getItems().addAll(app.getProjects());
	}
	
	@FXML
	public void okButtonAction() {
		projectComboBox.getSelectionModel().getSelectedItem();
		formContentController.closeProjectPopupStage();
	}
	
	private void setChoiceBoxCellFactory() {
		Callback<ListView<Project>, ListCell<Project>> cellFactory = new Callback<ListView<Project>, ListCell<Project>>() {
		    @Override
		    public ListCell<Project> call(ListView<Project> l) {
		        return new ListCell<Project>() {

		            @Override
		            protected void updateItem(Project item, boolean empty) {
		                super.updateItem(item, empty);
		                if (item == null || empty) {
		                    setGraphic(null);
		                } else {
		                    setText(item.getProjectName());
		                }
		            }
		        } ;
		    }
		};
		projectComboBox.setCellFactory(cellFactory);
		projectComboBox.setButtonCell(cellFactory.call(null));
	}
	
}
