package gov.epa.equitableresiliencebuilder.indicators;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

public class IndicatorSelector_InPersonController implements Initializable{

	public IndicatorSelector_InPersonController() {
		
	}
	
	@FXML
	HBox indicatorHBox;
	@FXML
	CheckBox indicatorCheckBox;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	public CheckBox getIndicatorCheckBox() {
		return indicatorCheckBox;
	}

}
