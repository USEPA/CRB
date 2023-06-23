package com.epa.erb.indicators;

import java.net.URL;
import java.util.ResourceBundle;

import com.epa.erb.App;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

public class IndicatorSelector_VirtualController implements Initializable{

	@FXML
	HBox indicatorHBox;
	@FXML
	CheckBox indicatorCheckBox;
	
	public IndicatorSelector_VirtualController() {
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	public CheckBox getIndicatorCheckBox() {
		return indicatorCheckBox;
	}

}
