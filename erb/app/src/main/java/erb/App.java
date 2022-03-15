/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package erb;

import erb.App;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App extends Application {

	private Logger logger = LogManager.getLogger(App.class);
	
	public String getGreeting() {
		return "Launching ERB";
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		logger.info(getGreeting());
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EngagementSetup.fxml"));
			EngagementSetupController engagementSetupController = new EngagementSetupController();
			fxmlLoader.setController(engagementSetupController);
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			logger.fatal(e.getMessage());
		}
	}
}