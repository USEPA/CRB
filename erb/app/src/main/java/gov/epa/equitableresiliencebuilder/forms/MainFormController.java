package gov.epa.equitableresiliencebuilder.forms;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import gov.epa.equitableresiliencebuilder.App;
import gov.epa.equitableresiliencebuilder.engagement_action.EngagementActionController;
import gov.epa.equitableresiliencebuilder.utility.XMLManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainFormController extends FormController implements Initializable{
	
	private App app;
	private File xmlContentFileToParse;
	private EngagementActionController engagementActionController;
	public MainFormController(App app, File xmlContentFileToParse, EngagementActionController engagementActionController) {
		super(app, xmlContentFileToParse, engagementActionController);
		this.app = app;
		this.xmlContentFileToParse = xmlContentFileToParse;
		this.engagementActionController = engagementActionController;
	}
	
	@FXML
	HBox hBox;
	@FXML
	Pane lP, tP, rP, bP;
	@FXML
	Separator separator;
	@FXML
	VBox nodeVBox, formVBox;
	@FXML
	VBox rightPanelVBox, topPanelVBox, bottomPanelVBox;
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		XMLManager xmlManager = new XMLManager(app);
		HashMap<String, ArrayList<HBox>> formContentHashMap = xmlManager.parseMainFormContentXML(xmlContentFileToParse, this);
		addContent(formContentHashMap);
		hideEmptyControls();
		setColor(tP);

	}
	
	public void addContent(HashMap<String, ArrayList<HBox>> formContentHashMap) {
		if(formContentHashMap != null) {
			ArrayList<HBox> formContentTextFlows = formContentHashMap.get("formVBox");
			addTextFlowsToVBox(formVBox, formContentTextFlows);
			ArrayList<HBox> topPanelTextFlows = formContentHashMap.get("topPanelVBox");
			addTextFlowsToVBox(topPanelVBox, topPanelTextFlows);
			ArrayList<HBox> bottomPanelTextFlows = formContentHashMap.get("bottomPanelVBox");
			addTextFlowsToVBox(bottomPanelVBox, bottomPanelTextFlows);
			
			if(topPanelTextFlows.size() > 0) {
				String expands = topPanelTextFlows.get(0).getId();
				if(expands.contentEquals("false")) {
					rightPanelVBox.setMinWidth(250.0);
					rightPanelVBox.setMaxWidth(250.0);
					formVBox.setMinWidth(450.0);
				} else if (expands.length() == 0) {
					rightPanelVBox.setMinWidth(250.0);
					rightPanelVBox.setMaxWidth(250.0);
					formVBox.setMinWidth(450.0);
				}
			}
		}
	}
	
	public void addTextFlowsToVBox(VBox vBox, ArrayList<HBox> textFlows) {
		if (textFlows != null && vBox != null) {
			for (HBox textFlow : textFlows) {
				vBox.getChildren().add(textFlow);
			}
		}
	}
	
	private void hideEmptyControls() {
		if(topPanelVBox.getChildren().size() == 0 && bottomPanelVBox.getChildren().size() == 0) {
			hBox.getChildren().remove(rightPanelVBox);
			separator.setVisible(false);
		}
		if(topPanelVBox.getChildren().size() ==0) {
			if(rightPanelVBox.getChildren().contains(topPanelVBox)) rightPanelVBox.getChildren().remove(topPanelVBox);
			separator.setVisible(false);
		}
		if(bottomPanelVBox.getChildren().size() ==0) {
			if(rightPanelVBox.getChildren().contains(bottomPanelVBox)) rightPanelVBox.getChildren().remove(bottomPanelVBox);
			separator.setVisible(false);
		}
	}
	
	public void clearContent() {
		clearFormVBox();
		clearTopVBox();
		clearBottomVBox();
	}
	
	private void clearFormVBox() {
		formVBox.getChildren().clear();
	}
	
	private void clearTopVBox() {
		topPanelVBox.getChildren().clear();
	}
	
	private void clearBottomVBox() {
		bottomPanelVBox.getChildren().clear();
	}

	public EngagementActionController getEngagementActionController() {
		return engagementActionController;
	}

	public void setEngagementActionController(EngagementActionController engagementActionController) {
		this.engagementActionController = engagementActionController;
	}

}
