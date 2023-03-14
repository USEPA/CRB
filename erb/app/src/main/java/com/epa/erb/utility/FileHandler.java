package com.epa.erb.utility;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epa.erb.ERBContentItem;
import com.epa.erb.goal.Goal;
import com.epa.erb.project.Project;

public class FileHandler {
	
	public FileHandler() {
		
	}
	
	private Constants constants = new Constants();
	private Logger logger = LogManager.getLogger(FileHandler.class);
	
	//--------------------------------------------------------------------------
	
	public File getProjectsDirectory() {
		File projectsDirectory = new File(constants.getPathToERBProjectsFolder());
		return projectsDirectory;
	}
	
	public File getProjectDirectory(Project project) {
		if(project != null) {
			File projectDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName());
			return projectDirectory;
		} else {
			logger.error("Cannot getProjectDirectory. project = " + project);
			return null;
		}
	}
	
	public File getGoalsDirectory(Project project) {
		if(project != null) {
			File goalsDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals");
			return goalsDirectory;
		} else {
			logger.error("Cannot getGoalsDirectory. project = " + project);
			return null;
		}
	}
	
	public File getGoalDirectory(Project project, Goal goal) {
		if(project != null && goal != null) {
			File goalDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName());
			return goalDirectory;
		} else {
			logger.error("Cannot getGoalDirectory. project = " + project + " goal = " + goal);
			return null;
		}
	}
	
	public File getActivitiesDirectory(Project project, Goal goal) {
		if (project != null && goal != null) {
			File activitiesDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\Activities_XML");
			return activitiesDirectory;
		} else {
			logger.error("Cannot getActivitiesDirectory. project = " + project + " goal = " + goal);
			return null;
		}
	}
	
	public File getStepsDirectory(Project project, Goal goal) {
		if (project != null && goal != null) {
			File stepsDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\Steps_XML");
			return stepsDirectory;
		} else {
			logger.error("Cannot getStepsDirectory. project = " + project + " goal = " + goal);
			return null;
		}
	}
	
	public File getSupportingDOCDirectory(Project project, Goal goal) {
		if(project != null && goal != null) {
			File supportingDOCDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\Supporting_DOC");
			return supportingDOCDirectory;
		} else {
			logger.error("Cannot getSupportingDOCDirectory. project = " + project + " goal = " + goal);
			return null;
		}
	}
	
	public File getGUIDDataDirectory(Project project, Goal goal) {
		if(project != null && goal != null) {
			File guidsDirectory = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\GUID_Data");
			return guidsDirectory;
		} else {
			logger.error("Cannot getGUIDDataDirectory. project = " + project + " goal = " + goal);
			return null;
		}
	}
	
	//--------------------------------------------------------------------------
	
	public File getProjectMetaXMLFile(Project project) {
		if(project != null) {
			File projectXMLMetaXMLFile = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Meta.xml");
			return projectXMLMetaXMLFile;
		} else {
			logger.error("Cannot getProjectMetaXMLFile. project = null.");
			return null;
		}
	}
	
	public File getGoalMetaXMLFile(Project project, Goal goal) {
		if(project != null && goal != null) {
			File goalMetaXMLFile = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\Meta.xml");
			return goalMetaXMLFile;
		} else {
			logger.error("Cannot getGoalMetaXMLFile. project = " + project + " goal = " + goal);
			return null;
		}
	}
	
	public File getDataXMLFile(Project project, Goal goal, ERBContentItem erbContentItem) {
		if(project != null && goal != null && erbContentItem != null) {
			File dataXMLFile = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\GUID_Data\\" + erbContentItem.getGuid() + "\\Data.xml");
			return dataXMLFile;
		} else {
			logger.error("Cannot getDataXMLFile. project = " + project + " goal = " + goal + " ERBContentItem = " + erbContentItem);
			return null;
		}
	}
	
	public File getDynamicActivityDataXMLFile(Project project, Goal goal, String guid) {
		if(project != null && goal != null && guid != null) {
			File dynamicActivityDataXMLFile = new File(constants.getPathToERBProjectsFolder() + "\\" + project.getProjectCleanedName() + "\\Goals\\" + goal.getGoalCleanedName() + "\\GUID_Data\\" + guid + "\\Data.xml");
			return dynamicActivityDataXMLFile;
		} else {
			logger.error("Cannot getDynamicActivityDataXMLFile. project = " + project + " goal = " + goal + " guid = " + guid);
			return null;
		}
	}
	
	public File getJSONPWordCloudFileForInteractiveActivity(Project project, Goal goal, String guid, String webViewId) {
		File jsonpFile = new File(getGUIDDataDirectory(project, goal) + "\\" + guid + "\\" + webViewId + "\\wordcloud.jsonp");
		return jsonpFile;
	}
	
	//--------------------------------------------------------------------------
	
	public File getStaticSupportingDOCDirectory() {
		File supportingDOCDirectory = new File(constants.getPathToERBStaticDataFolder() + "\\Activities\\Supporting_DOC");
		return supportingDOCDirectory;
	}
	
	public File getStaticAvailableActivitiesXMLFile() {
		File availableActivitiesFile = new File(constants.getPathToERBStaticDataFolder()+ "\\Activities\\Available_Activities.xml");
		return availableActivitiesFile;
	}
	
	public File getStaticAvailableStepsXMLFile() {
		File availableStepsFile = new File(constants.getPathToERBStaticDataFolder() + "\\Activities\\Available_Steps.xml");
		return availableStepsFile;
	}
	
	public File getStaticAvailableContentXMLFile() {
		File availableContentXMLFile = new File(constants.getPathToERBFolder() + "\\Static_Data_2.0\\Available_Content.xml");
		return availableContentXMLFile;
	}
	
	public File getStaticAvailableInteractiveActivitiesXMLFile() {
		File availableInterativeActivitiesFile = new File(constants.getPathToERBStaticDataFolder() + "\\Activities\\Available_Interactive_Activities.xml");
		return availableInterativeActivitiesFile;
	}
	
	public File getStaticChaptersXMLFile() {
		File chaptersFile = new File(constants.getPathToERBStaticDataFolder() + "\\Chapters\\Chapters.xml");
		return chaptersFile;
	}
	
	public File getStaticGoalCategoriesXMLFile() {
		File goalCategoriesFile = new File(constants.getPathToERBFolder() + "\\Static_Data_2.0\\Goal_Categories.xml");
		return goalCategoriesFile;
	}
	
	public File getStaticAvailableIntroXMLFile() {
		File availableIntroFile = new File(constants.getPathToERBStaticDataFolder() + "\\Intro\\Available_Intro.xml");
		return availableIntroFile;
	}
	
	public File getStaticFormContentXML(String id) {
		if(id != null) {
			File formContentFile = new File(constants.getPathToERBFolder() + "\\Static_Data_2.0\\ContentXMLs\\" + id + "\\form_text.xml");
			return formContentFile;
		} else {
			logger.error("Cannot getStaticFormContentXML. id = " + id);
			return null;
		}
	}
	
	public File getStaticAvailableResourcesXMLFile() {
		File availableResourcesFile = new File(constants.getPathToERBStaticDataFolder() + "\\Resources\\Available_Resources.xml");
		return availableResourcesFile;
	}
	
	public File getStaticWordCloudDirectory() {
		File wordCloudDirectory = new File(constants.getPathToERBFolder() + "\\JavaScript\\WordCloud");
		return wordCloudDirectory;
	}
	
	public File getStaticIconImageFile(String id) {
		File iconFile = new File(constants.getPathToERBStaticDataFolder() + "\\Icons\\Icons\\" + id + "\\icon.png");
		return iconFile;
	}
	
	//-------------------------------------------------------------------------
	
	public void openFileOnDesktop(File fileToToOpen) {
		try {
			if (fileToToOpen != null && fileToToOpen.exists() && Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(fileToToOpen);
			} else {
				logger.error(fileToToOpen.getPath() + " either does not exist or desktop is not supported.");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public void deleteDirectory(File directory) {
		if (directory != null) {
			File[] allContents = directory.listFiles();
			if (allContents != null) {
				for (File file : allContents) {
					deleteDirectory(file);
				}
			}
			directory.delete();
		} else {
			logger.error("Cannot deleteDirectory. directory = null.");
		}
	}

	public void copyFile(File source, File dest) {
		if (source != null && source.exists() && dest != null) {
			try {
				InputStream is = new FileInputStream(source);
				OutputStream os = new FileOutputStream(dest);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
				is.close();
				os.close();
			} catch (Exception e) {
				logger.error("Cannot copyFile. " + e.getMessage());
			}
		} else {
			logger.error("Cannot copyFile. source = " + source + " dest = " + dest);
		}
	}
	
	//------------------------------------------------------------------------
	
	public void createGUIDDataDirectory(Project project, Goal goal) {
		File GUIDDataDirectory = getGUIDDataDirectory(project, goal);
		if (!GUIDDataDirectory.exists()) {
			GUIDDataDirectory.mkdir();
		}
	}
	
	public void createGUIDDirectoriesForGoal2(Project project, Goal goal, ArrayList<ERBContentItem> uniqueERBContentItems) {
		createGUIDDataDirectory(project, goal);
		for(ERBContentItem erbContentItem: uniqueERBContentItems) {
			createDirectory(project, goal, erbContentItem);
		}
	}
	
	private void createDirectory(Project p, Goal g, ERBContentItem e) {
		File guidDir = new File (getGUIDDataDirectory(p, g) + "\\" + e.getGuid());
		if(!guidDir.exists()) guidDir.mkdir();
		
		if(e.getChildERBContentItems().size() > 0) {
			for(ERBContentItem child: e.getChildERBContentItems()) {
				createDirectory(p, g, child);
			}
		}
	}
	
	
	 
}
