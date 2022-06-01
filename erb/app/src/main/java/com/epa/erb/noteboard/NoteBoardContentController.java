package com.epa.erb.noteboard;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.Activity;
import com.epa.erb.Constants;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

public class NoteBoardContentController implements Initializable{

	@FXML
	Label activityNameLabel;
	@FXML
	VBox mainVBox;
	@FXML
	Pane layer1Pane;
	@FXML
	Pane layer2Pane;
	@FXML
	Pane layer3Pane;
	@FXML
	Pane layer4Pane;
	@FXML
	Pane note; //layer 5
	
	private Activity activity;
	public NoteBoardContentController(Activity activity) {
		this.activity = activity;
	}
	
	private Constants constants = new Constants();
	private ArrayList<String> categories = new ArrayList<String>();
	private Logger logger = LogManager.getLogger(NoteBoardContentController.class);
	private ArrayList<PostItNoteController> postItNoteControllers = new ArrayList<PostItNoteController>();
	private ArrayList<CategorySectionController> categorySectionControllers = new ArrayList<CategorySectionController>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillCategories();
		createCategoryRows();
		handleControls();
		setActivityNameLabelText(activity.getLongName());
	}
	
	private void handleControls() {
		layer1Pane.setStyle("-fx-background-color: " + constants.getLayer1ColorString() + ";");
		layer2Pane.setStyle("-fx-background-color: " + constants.getLayer2ColorString() + ";");
		layer3Pane.setStyle("-fx-background-color: " + constants.getLayer3ColorString() + ";");
		layer4Pane.setStyle("-fx-background-color: " + constants.getLayer4ColorString() + ";");
		note.setStyle("-fx-background-color: " + constants.getLayer5ColorString() + ";");
		setDrag(note, null);
	}
	
	private void fillCategories() {
		categories.add("Flood");
		categories.add("Heat");
		categories.add("Radiological Disease");
		categories.add("Pandemic");
	}
	
	@FXML
	public void noteClicked() {
		
	}
	
	private void createCategoryRows() {
		for (String category : categories) {
			Parent root = loadCategorySection(category);
			VBox.setVgrow(root, Priority.ALWAYS);
			mainVBox.getChildren().add(root);
		}
	}
	
	private Parent loadCategorySection(String category) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/CategorySection.fxml"));
			CategorySectionController categorySectionController = new CategorySectionController((category));
			fxmlLoader.setController(categorySectionController);
			HBox catHBox = fxmlLoader.load();
			setDrag(categorySectionController.getPostItHBox(), categorySectionController);
			categorySectionControllers.add(categorySectionController);
			return catHBox;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	private int indexToMove = -1;
	private int sourcePaneHashCode = -1;
	private static final String TAB_DRAG_KEY = "pane";
	private ObjectProperty<Pane> draggingTab = new SimpleObjectProperty<Pane>();
	private void setDrag(Pane p, CategorySectionController categorySectionController) {
		p.setOnDragOver(event-> {
			event.acceptTransferModes(TransferMode.MOVE);
			Pane sourceNote = (Pane) event.getSource();
			Pane sourcePane = (Pane) sourceNote.getParent();
			Pane targetPane = (Pane) event.getTarget();
			if (targetPane != null) {
				if(targetPane.toString().contains("TextFlow")) {
					VBox sourceNoteVBox = (VBox) sourceNote;
					if(sourceNoteVBox.getId().contentEquals("postedNote")) {
						sourcePaneHashCode = sourcePane.hashCode();
						indexToMove = sourcePane.getChildren().indexOf(sourceNoteVBox);
					}
				}
			}
			event.consume();
		});
		p.setOnDragDropped(event-> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if(db.hasString()) {
				Pane targetPane = p;
				Pane sourceNote = (Pane) event.getGestureSource();
				//Adding a new post it
				if(sourceNote.getId() != null && sourceNote.getId().contentEquals("note") && targetPane.getId() != null && targetPane.getId().contentEquals("postItHBox")) {
					try {
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/noteboard/PostItNote.fxml"));
						PostItNoteController postItNoteController = new PostItNoteController(this);
						fxmlLoader.setController(postItNoteController);
						Pane postedPane = fxmlLoader.load();	
						setDrag(postedPane, categorySectionController);
						targetPane.getChildren().add(postedPane);
						postItNoteControllers.add(postItNoteController);
					}catch (Exception e) {
						logger.error(e.getMessage());
					}
					// Moving a post it
				} else if (sourceNote.getId() != null && sourceNote.getId().contentEquals("postedNote") && targetPane.getId() != null && targetPane.getId().contentEquals("postItHBox")) {
					if(sourcePaneHashCode == targetPane.hashCode()) {
						if (indexToMove > -1) {
							if (targetPane.getChildren().contains(sourceNote)) targetPane.getChildren().remove(sourceNote);
							if(!targetPane.getChildren().contains(sourceNote)) targetPane.getChildren().add(indexToMove, sourceNote);
						}
					} else {
						int numChildren = targetPane.getChildren().size();
						if (targetPane.getChildren().contains(sourceNote)) targetPane.getChildren().remove(sourceNote);
						if(!targetPane.getChildren().contains(sourceNote)) targetPane.getChildren().add(numChildren, sourceNote);
					}
				//Moving a post it
				} else if(sourceNote.getId() != null && sourceNote.getId().contentEquals("postedNote") && targetPane.getId() != null && targetPane.getId().contentEquals("postedNote")) {
					Pane sourceParentPane = (Pane) sourceNote.getParent();
					TextFlow textFlow = (TextFlow) event.getTarget();
					VBox noteVBox = (VBox) textFlow.getParent().getParent().getParent().getParent().getParent();
					int targetIndex = sourceParentPane.getChildren().indexOf(noteVBox);
					sourceParentPane.getChildren().remove(sourceNote);
					sourceParentPane.getChildren().add(targetIndex, sourceNote);
				}
				success = true;
			}
			event.setDropCompleted(success);
			event.consume();
		});
		p.setOnDragDetected(event-> {
			Dragboard dragboard = p.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent clipboardContent = new ClipboardContent();
			clipboardContent.putString(TAB_DRAG_KEY);
			dragboard.setContent(clipboardContent);
			draggingTab.set(p);
			event.consume();
		});
	}
	
	public void setCategoryPostIts() {
		for(CategorySectionController categorySectionController: categorySectionControllers) {
			ArrayList<PostItNoteController> assignedPostItNoteControllers = new ArrayList<PostItNoteController>();
			for(PostItNoteController postItNoteController: postItNoteControllers) {
				if(postItNoteController.getPostItNotePane().getParent().hashCode() == categorySectionController.getPostItHBox().hashCode()) {
					assignedPostItNoteControllers.add(postItNoteController);
				}
			}
			categorySectionController.setListOfPostItNoteControllers(assignedPostItNoteControllers);
		}
	}
	
	void removePostItNoteController(PostItNoteController postItNoteController) {
		postItNoteControllers.remove(postItNoteController);
	}
	
	private void setActivityNameLabelText(String text) {
		activityNameLabel.setText(text);
	}

	public Label getActivityNameLabel() {
		return activityNameLabel;
	}

	public VBox getMainVBox() {
		return mainVBox;
	}

	public Pane getLayer1Pane() {
		return layer1Pane;
	}

	public Pane getLayer2Pane() {
		return layer2Pane;
	}

	public Pane getLayer3Pane() {
		return layer3Pane;
	}

	public Pane getLayer4Pane() {
		return layer4Pane;
	}

	public Pane getNote() {
		return note;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public ArrayList<CategorySectionController> getCategorySectionControllers() {
		return categorySectionControllers;
	}
	
}
