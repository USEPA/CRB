package com.epa.erb.chapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Activity;
import com.epa.erb.Constants;
import com.epa.erb.engagement_action.EngagementActionController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class ChapterLandingController implements Initializable {

	@FXML
	VBox mainPanel;
	@FXML
	VBox aboutPanel;
	@FXML
	Label headingLabel;
	@FXML
	HBox headingLabelHBox;
	@FXML
	TextArea aboutTextArea;
	@FXML
	ListView<Activity> activitiesListView;
	
	private Chapter chapter;
	private EngagementActionController engagementActionController;
	public ChapterLandingController(Chapter chapter, EngagementActionController engagementActionController) {
		this.chapter = chapter;
		this.engagementActionController = engagementActionController;
	}
	
	private ArrayList<Chapter> listOfAllChapters;
	public ChapterLandingController(ArrayList<Chapter> listOfAllChapters, EngagementActionController engagementActionController) {
		this.listOfAllChapters = listOfAllChapters;
		this.engagementActionController = engagementActionController;
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(ChapterLandingController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleControls();
		setAboutTextAreaText(getAboutText());
		setHeadingLabelText(getHeadingText());
		fillActivitiesListView(getActivitiesForListView());
	}
	
	private void handleControls() {
		headingLabelHBox.setStyle("-fx-background-color: " + constants.getAllChaptersColor() + ";");
	}
	
	public void setHeadingLabelText(String text) {
		headingLabel.setText(text);
	}
	
	String getHeadingText() {
		if (chapter != null) {
			return "Welcome to " + chapter.getStringName();
		} else {
			return "Equitable Resilience Builder";
		}
	}
	
	public void setAboutTextAreaText(Text text) {
		text.setFont(Font.font(15));
		aboutTextArea.setText(text.getText());
	}
	
	Text getAboutText() {
		if (chapter != null) {
			addAboutPanel();
			return new Text(chapter.getDescriptionName());
		} else {
			removeAboutPanel();
			return new Text("The Equitable Resilience Builder (ERB) is an application that assists communities with resilience planning. ERB engages communities in a guided process to inclusively assess their vulnerability and resilience to disasters and climate change, then use the results to prioritize actions to build resilience in an equitable way.");
		}
	}

	public void fillActivitiesListView(ArrayList<Activity> listOfActivities) {
		cleanActivitiesListView();
		for(Activity activity: listOfActivities) {
			activitiesListView.getItems().add(activity);
		}
		setActivityListViewCellFactory();
	}
	
	private ArrayList<Activity> getActivitiesForListView() {
		ArrayList<Activity> activitiesForListView = new ArrayList<Activity>();
		if (chapter != null) { // landing for a single chapter
			for (Activity activity : chapter.getAssignedActivities()) {
				activitiesForListView.add(activity);
			}
		} else { // landing for erb showing all chapters
			for (Chapter chapter : listOfAllChapters) {
				for (Activity activity : chapter.getAssignedActivities()) {
					activitiesForListView.add(activity);
				}
			}
		}
		return activitiesForListView;
	}
	
	private void setActivityListViewCellFactory() {
		activitiesListView.setCellFactory(new Callback<ListView<Activity>, ListCell<Activity>>() {
			@Override
			public ListCell<Activity> call(ListView<Activity> param) {
				ListCell<Activity> cell = new ListCell<Activity>() {
					@Override
					protected void updateItem(Activity item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText("Chapter " + item.getChapterAssignment() + ": " + item.getLongName());
							setFont(new Font(14.0));
						}
					}
				};
				cell.setOnMouseClicked(e -> handleActivitySelectedInList());
				return cell;
			}
		});
	}
	
	private void handleActivitySelectedInList() {
		Activity selectedActivity = activitiesListView.getSelectionModel().getSelectedItem();
		HashMap<TreeItem<String>, String> treeMap = engagementActionController.getTreeMap();
		for (TreeItem<String> treeItem : treeMap.keySet()) {
			String treeItemValue = treeItem.getValue();
			if (treeItemValue.contentEquals(selectedActivity.getLongName())) { // if tree item value matches activity name
				String treeItemActivityID = treeMap.get(treeItem);
				if (treeItemActivityID.contentEquals(selectedActivity.getActivityID())) { // if tree item GUID matches
					Chapter treeItemChapter = engagementActionController.getChapter(treeItem.getParent().getValue());
					if (String.valueOf(treeItemChapter.getChapterNum()).contentEquals(selectedActivity.getChapterAssignment())) {
						engagementActionController.getTreeView().getSelectionModel().select(treeItem); // select tree item
						engagementActionController.treeViewClicked(); // handle tree item selected
					}
				}
			}
		}
	}
	
	private void removeAboutPanel() {
		if(mainPanel.getChildren().contains(aboutPanel)) {
			mainPanel.getChildren().remove(aboutPanel);
		}
	}
	
	private void addAboutPanel() {
		if(!mainPanel.getChildren().contains(aboutPanel)) {
			mainPanel.getChildren().add(0,aboutPanel);
		}
	}
	
	private void cleanActivitiesListView() {
		activitiesListView.getItems().clear();
	}

	public Label getHeadingLabel() {
		return headingLabel;
	}

	public HBox getHeadingLabelHBox() {
		return headingLabelHBox;
	}

	public TextArea getAboutTextArea() {
		return aboutTextArea;
	}

	public ListView<Activity> getActivitiesListView() {
		return activitiesListView;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public EngagementActionController getEngagementActionController() {
		return engagementActionController;
	}

	public void setEngagementActionController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}

	public ArrayList<Chapter> getListOfAllChapters() {
		return listOfAllChapters;
	}

	public void setListOfAllChapters(ArrayList<Chapter> listOfAllChapters) {
		this.listOfAllChapters = listOfAllChapters;
	}

}
