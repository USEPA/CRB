module gov.epa.equitableresiliencebuilder {
	requires java.base;
	requires javafx.fxml;
	requires javafx.base;
	requires java.desktop;
	requires jdk.compiler;
	requires jdk.jsobject;
	requires javafx.swing;
	requires org.apache.poi.poi;
	requires org.apache.xmlbeans;
	requires org.apache.commons.io;
	requires org.controlsfx.controls;
	requires org.apache.poi.ooxml.schemas;
	requires transitive java.xml;
	requires transitive javafx.web;
	requires transitive java.logging;
	requires transitive javafx.graphics;
	requires transitive javafx.controls;
	requires transitive org.apache.poi.ooxml;
	opens gov.epa.equitableresiliencebuilder to javafx.fxml;
	opens gov.epa.equitableresiliencebuilder.goal to javafx.fxml;
	opens gov.epa.equitableresiliencebuilder.excel to javafx.fxml;
	opens gov.epa.equitableresiliencebuilder.forms to javafx.fxml;
	opens gov.epa.equitableresiliencebuilder.print to javafx.fxml;
	opens gov.epa.equitableresiliencebuilder.project to javafx.fxml;
	opens gov.epa.equitableresiliencebuilder.noteboard to javafx.fxml;
	opens gov.epa.equitableresiliencebuilder.indicators to javafx.fxml;
	opens gov.epa.equitableresiliencebuilder.finalReport to javafx.fxml;
	opens gov.epa.equitableresiliencebuilder.wordcloud to javafx.fxml, javafx.base;
	opens gov.epa.equitableresiliencebuilder.engagement_action to javafx.fxml, javafx.base;
	exports gov.epa.equitableresiliencebuilder;
	exports gov.epa.equitableresiliencebuilder.goal;
	exports gov.epa.equitableresiliencebuilder.excel;
	exports gov.epa.equitableresiliencebuilder.forms;
	exports gov.epa.equitableresiliencebuilder.print;
	exports gov.epa.equitableresiliencebuilder.project;
	exports gov.epa.equitableresiliencebuilder.utility;
	exports gov.epa.equitableresiliencebuilder.wordcloud;
	exports gov.epa.equitableresiliencebuilder.noteboard;
	exports gov.epa.equitableresiliencebuilder.indicators;
	exports gov.epa.equitableresiliencebuilder.finalReport;
	exports gov.epa.equitableresiliencebuilder.engagement_action;
}