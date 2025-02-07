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
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class AlternativeFormController extends FormController implements Initializable{
	
	private App app;
	private File xmlContentFileToParse;
	public AlternativeFormController(App app, File xmlContentFileToParse, EngagementActionController engagementActionController) {
		super(app, xmlContentFileToParse, engagementActionController);
		this.app = app;
		this.xmlContentFileToParse = xmlContentFileToParse;
	}
	
	@FXML
	HBox hBox;
	@FXML
	Pane lP, tP, rP, bP;
	@FXML
	VBox leftHeaderVBox, leftVBox, centerVBox, rightVBox;
	@FXML
	VBox containerVBox, nodeVBox, titleVBox;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		XMLManager xmlManager = new XMLManager(app);
		HashMap<String, ArrayList<HBox>> formContentHashMap = xmlManager.parseAlternativeFormContentXML(xmlContentFileToParse, this);
		addContent(formContentHashMap);
		hideEmptyControls();
		setColor(tP);
	}
	
	public void addContent(HashMap<String, ArrayList<HBox>> formContentHashMap) {
		if(formContentHashMap != null) {
			ArrayList<HBox> titleContentTextFlows = formContentHashMap.get("titleVBox");
			addTextFlowsToVBox(titleVBox, titleContentTextFlows);
			ArrayList<HBox> formHeaderTextFlows = formContentHashMap.get("leftHeaderVBox");
			addTextFlowsToVBox(leftHeaderVBox, formHeaderTextFlows);
			ArrayList<HBox> formContentTextFlows = formContentHashMap.get("leftVBox");
			addTextFlowsToVBox(leftVBox, formContentTextFlows);
			ArrayList<HBox> topPanelTextFlows = formContentHashMap.get("centerVBox");
			addTextFlowsToVBox(centerVBox, topPanelTextFlows);
			ArrayList<HBox> bottomPanelTextFlows = formContentHashMap.get("rightVBox");
			addTextFlowsToVBox(rightVBox, bottomPanelTextFlows);
		}
	}
	
	public void addTextFlowsToVBox(VBox vBox, ArrayList<HBox> textFlows) {
		if (textFlows != null && vBox != null) {
			for (HBox textFlow : textFlows) {
				vBox.getChildren().add(textFlow);
				if(xmlContentFileToParse.getName().contains("251")) {
					vBox.setAlignment(Pos.TOP_CENTER);
				}
			}
		}
	}
	
	private void hideEmptyControls() {
		if(rightVBox.getChildren().size() == 0) {
			hBox.getChildren().remove(rightVBox);
		}
		if(centerVBox.getChildren().size() ==0) {
			hBox.getChildren().remove(centerVBox);
		}
		if(leftVBox.getChildren().size() ==0) {
			hBox.getChildren().remove(leftVBox);
		}
		if(leftHeaderVBox.getChildren().size() ==0) {
			hBox.getChildren().remove(leftHeaderVBox);
		}
		if(xmlContentFileToParse.getName().contains("251")) {
			hBox.setAlignment(Pos.CENTER_LEFT);
		}
	}
}
