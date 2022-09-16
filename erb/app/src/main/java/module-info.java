module com.epa.erb {
	requires java.xml;
	requires javafx.fxml;
	requires javafx.web;
	requires javafx.base;
	requires java.base;
	requires java.desktop;
	requires jdk.compiler;
	requires org.apache.commons.io;
	requires org.apache.logging.log4j;
	requires transitive javafx.graphics;
	requires transitive javafx.controls;
	requires jdk.jsobject;
	requires com.aspose.words;
	requires org.slf4j;
	requires org.docx4j.core;
	requires jakarta.xml.bind;
	opens com.epa.erb to javafx.fxml;
	opens com.epa.erb.goal to javafx.fxml;
	opens com.epa.erb.chapter to javafx.fxml;
	opens com.epa.erb.project to javafx.fxml;
	opens com.epa.erb.noteboard to javafx.fxml;
	opens com.epa.erb.worksheet to javafx.fxml, javafx.web;
	opens com.epa.erb.engagement_action to javafx.fxml;
	opens com.epa.erb.form_activities to javafx.fxml;
	opens com.epa.erb.intro_panels to javafx.fxml;
	exports com.epa.erb;
}