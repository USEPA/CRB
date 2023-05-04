package com.epa.erb.utility;

import java.io.File;

public class Constants {
		
	//Scaling Sizes
	private int prefHeightForScale100 = 950;
	private int prefWidthForScale100 = 1350;
	private int prefHeightForScale125 = 750;
	private int prefWidthForScale125 = 1150;
	private int prefHeightForScale150 = 600;
	private int prefWidthForScale150 = 1000;
	private int prefHeightForScale175 = 500;
	private int prefWidthForScale175 = 900;
	
//	1920
//	1680
//	1280
	
	//Activity
	private String activityTypeColor = "";
		//Status
	private String readyStatusColor = "#E3E3E3"; 				//Gray
	private String inProgressStatusColor = "#EDF7B2"; 			//Yellow
	private String completeStatusColor = "#B2F7D1"; 			//Mint Green
		//Attributes
	private String shortNameColor = "";
	private String longNameColor = "";
	private String fileNameColor = "";
	private String instructionsColor = "#86E596"; 				//Light Green
	private String objectivesColor = "#6B8BFF"; 				//Blue
	private String descriptionColor = "#F7B2E0"; 				//Pink	
	private String materialsColor = "#B895EB";					//Purple
	private String timeColor = "#F7DBB2";						//Orange
	private String whoColor = "#EB8787"; 						//Red
	private String activityIDColor = "";
	private String linksColor = "#E88AFF";						//Magenta

	//Chapters
	//private String allChaptersColor	= "#C3C3C3"; 		    //Grey
	private String allChaptersColor	= "#A7BFE8";				//Blue
	
	private String chapter1Color = "#F1A858";					//Orange
	private String chapter2Color = "#6FE658";					//Light Green
	private String chapter3Color = "#35AF69";					//Green
	private String chapter4Color = "#42C2D7";					//Light Blue
	private String chapter5Color = "#427ED7";					//Blue


	//Post It Note Stack
	private String layer1ColorString = "#F7B2E0";				//Pink
	private String layer2ColorString = "#B895EB";				//Purple
	private String layer3ColorString = "#EB8787";				//Red
	private String layer4ColorString = "#B2F7D1";				//Mint Green
	private String layer5ColorString = "#B2C1F7";				//Light Blue
	
	//Post-It Note Single
	private String postItNoteColor = "#FFFFFF"; 				//White
			
	//Paths
	private String pathToERBFolder;
	private String pathToERBProjectsFolder;
	private String pathToERBStaticDataFolder;
	
	public Constants() {
		setFilePathsStatic(); //For running tool locally
		//setFilePathsDynamic(); //For packaging tool 
	}
	
	private void setFilePathsStatic() {
		File userDir = new File(System.getProperty("user.dir"));
		File codeResourcesFile = new File(userDir.getParentFile().getParentFile() + "//erb_supporting_docs//Code_Resources");
		
		pathToERBFolder = codeResourcesFile.getPath() +  "\\ERB";
		pathToERBProjectsFolder = codeResourcesFile.getPath() + "\\ERB\\Projects";
		pathToERBStaticDataFolder = codeResourcesFile.getPath() +  "\\ERB\\Static_Data";
	}
	
	private void setFilePathsDynamic() {
		pathToERBFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\").replace("\\", "\\\\"); //Dynamic
		pathToERBProjectsFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\Projects\\").replace("\\", "\\\\"); //Dynamic
		pathToERBStaticDataFolder = (System.getProperty("user.dir")+"\\lib\\ERB\\Static_Data\\").replace("\\", "\\\\"); //Dynamic
	}
	
	//-------------------------------------------------------------------------
	
	public int getPrefHeightForScale100() {
		return prefHeightForScale100;
	}
	public int getPrefWidthForScale100() {
		return prefWidthForScale100;
	}
	public int getPrefHeightForScale125() {
		return prefHeightForScale125;
	}
	public int getPrefWidthForScale125() {
		return prefWidthForScale125;
	}
	public int getPrefHeightForScale150() {
		return prefHeightForScale150;
	}
	public int getPrefWidthForScale150() {
		return prefWidthForScale150;
	}
	public int getPrefHeightForScale175() {
		return prefHeightForScale175;
	}
	public int getPrefWidthForScale175() {
		return prefWidthForScale175;
	}
	public String getActivityTypeColor() {
		return activityTypeColor;
	}
	public String getReadyStatusColor() {
		return readyStatusColor;
	}
	public String getInProgressStatusColor() {
		return inProgressStatusColor;
	}
	public String getCompleteStatusColor() {
		return completeStatusColor;
	}
	public String getShortNameColor() {
		return shortNameColor;
	}
	public String getLongNameColor() {
		return longNameColor;
	}
	public String getFileNameColor() {
		return fileNameColor;
	}
	public String getInstructionsColor() {
		return instructionsColor;
	}
	public String getObjectivesColor() {
		return objectivesColor;
	}
	public String getDescriptionColor() {
		return descriptionColor;
	}
	public String getMaterialsColor() {
		return materialsColor;
	}
	public String getTimeColor() {
		return timeColor;
	}
	public String getWhoColor() {
		return whoColor;
	}
	public String getActivityIDColor() {
		return activityIDColor;
	}
	public String getLinksColor() {
		return linksColor;
	}
	public String getAllChaptersColor() {
		return allChaptersColor;
	}	
	public String getChapter1Color() {
		return chapter1Color;
	}

	public String getChapter2Color() {
		return chapter2Color;
	}

	public String getChapter3Color() {
		return chapter3Color;
	}

	public String getChapter4Color() {
		return chapter4Color;
	}

	public String getChapter5Color() {
		return chapter5Color;
	}

	public String getLayer1ColorString() {
		return layer1ColorString;
	}
	public String getLayer2ColorString() {
		return layer2ColorString;
	}
	public String getLayer3ColorString() {
		return layer3ColorString;
	}
	public String getLayer4ColorString() {
		return layer4ColorString;
	}
	public String getLayer5ColorString() {
		return layer5ColorString;
	}
	public String getPostItNoteColor() {
		return postItNoteColor;
	}
	public String getPathToERBFolder() {
		return pathToERBFolder;
	}
	public String getPathToERBProjectsFolder() {
		return pathToERBProjectsFolder;
	}
	public String getPathToERBStaticDataFolder() {
		return pathToERBStaticDataFolder;
	}

}
