module com.epa.erb {
	requires java.xml;
	requires java.desktop;
	requires transitive javafx.graphics;
	requires javafx.fxml;
	requires transitive javafx.controls;
	requires org.apache.logging.log4j;
	requires javafx.web;
	requires javafx.base;
	opens com.epa.erb to javafx.fxml;
	exports com.epa.erb;
}