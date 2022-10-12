package com.epa.erb.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.epa.erb.Activity;
import com.epa.erb.App;
import com.epa.erb.DynamicActivity;
import com.epa.erb.Step;
import com.epa.erb.chapter.Chapter;
import com.epa.erb.forms.MainFormController;
import com.epa.erb.goal.Goal;
import com.epa.erb.goal.GoalCategory;
import com.epa.erb.noteboard.CategorySectionController;
import com.epa.erb.noteboard.PostItNoteController;
import com.epa.erb.project.Project;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class XMLManager {

	private App app;
	public XMLManager(App app) {
		this.app = app;
	}

	private Logger logger = LogManager.getLogger(XMLManager.class);
	
	//Parse Chapters
	public ArrayList<Chapter> parseChaptersXML(File xmlFile) {
		if (xmlFile!= null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				ArrayList<Chapter> chapters = new ArrayList<Chapter>();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList chapterNodeList = doc.getElementsByTagName("chapter");
				for (int i = 0; i < chapterNodeList.getLength(); i++) {
					Node chapterNode = chapterNodeList.item(i);
					// Chapter
					if (chapterNode.getNodeType() == Node.ELEMENT_NODE) {
						Element chapterElement = (Element) chapterNode;
						String chapterNum = chapterElement.getAttribute("chapterNum");
						String description = chapterElement.getAttribute("description");
						String notes = chapterElement.getAttribute("notes");
						String numericName = chapterElement.getAttribute("numericName");
						String stringName = chapterElement.getAttribute("stringName");
						Chapter chapter = new Chapter(Integer.parseInt(chapterNum), numericName, stringName, description, notes);
						chapter.assignStepIds(app.getAvailableSteps());
						chapter.assignActivityIds(app.getAvailableActivities());
						chapters.add(chapter);
					}
				}
				return chapters;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	//Parse AvailableActivities
	public ArrayList<Activity> parseAvailableActivitiesXML(File xmlFile){
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				ArrayList<Activity> availableActivities = new ArrayList<Activity>();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList activitiesNodeList = doc.getElementsByTagName("activity");
				for (int i = 0; i < activitiesNodeList.getLength(); i++) {
					Node activityNode = activitiesNodeList.item(i);
					// Activity
					if (activityNode.getNodeType() == Node.ELEMENT_NODE) {
						Element activityElement = (Element) activityNode;

						String activityID = activityElement.getAttribute("activityID");
						String chapterAssignment = activityElement.getAttribute("chapterAssignment");
						String status = activityElement.getAttribute("status");
						String shortName = activityElement.getAttribute("shortName");
						String longName = activityElement.getAttribute("longName");
						String fileName = activityElement.getAttribute("fileName");
						String notes = activityElement.getAttribute("notes");
						String rating = activityElement.getAttribute("rating");
						
						ArrayList<String> dynamicActivityIds = new ArrayList<String>();
						NodeList dynamicActivityIdsNodeList = activityElement.getElementsByTagName("dynamicActivityId");
						for (int j = 0; j < dynamicActivityIdsNodeList.getLength(); j++) {
							Node dynamicActivityIdNode = dynamicActivityIdsNodeList.item(j);
							if (dynamicActivityIdNode.getNodeType() == Node.ELEMENT_NODE) {
								Element dynamicActivityElement = (Element) dynamicActivityIdNode;
								String id = dynamicActivityElement.getAttribute("id");
								dynamicActivityIds.add(id);
							}
						}
						ArrayList<String> stepIds = new ArrayList<String>();
						NodeList stepIdsNodeList = activityElement.getElementsByTagName("stepId");
						for (int j = 0; j < stepIdsNodeList.getLength(); j++) {
							Node stepIdNode = stepIdsNodeList.item(j);
							if (stepIdNode.getNodeType() == Node.ELEMENT_NODE) {
								Element stepElement = (Element) stepIdNode;
								String id = stepElement.getAttribute("id");
								stepIds.add(id);
							}
						}
						Activity activity = new Activity(chapterAssignment, status, shortName, longName, fileName, activityID, notes, rating, dynamicActivityIds, stepIds);
						availableActivities.add(activity);
					}
				}
				return availableActivities;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	// Parse AvailableSteps
	public ArrayList<Step> parseAvailableStepsXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				ArrayList<Step> availableSteps = new ArrayList<Step>();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList stepsNodeList = doc.getElementsByTagName("step");
				for (int i = 0; i < stepsNodeList.getLength(); i++) {
					Node stepNode = stepsNodeList.item(i);
					// Step
					if (stepNode.getNodeType() == Node.ELEMENT_NODE) {
						Element stepElement = (Element) stepNode;
						String stepType = stepElement.getAttribute("stepType");
						String stepID = stepElement.getAttribute("stepID");
						String activityAssignment = stepElement.getAttribute("activityAssignment");
						String chapterAssignment = stepElement.getAttribute("chapterAssignment");
						String status = stepElement.getAttribute("status");
						String shortName = stepElement.getAttribute("shortName");
						String longName = stepElement.getAttribute("longName");
						String fileName = stepElement.getAttribute("fileName");
						String notes = stepElement.getAttribute("notes");
						String rating = stepElement.getAttribute("rating");

						ArrayList<String> dynamicActivityIds = new ArrayList<String>();
						NodeList dynamicActivityIdsNodeList = stepElement.getElementsByTagName("dynamicActivityId");
						for (int j = 0; j < dynamicActivityIdsNodeList.getLength(); j++) {
							Node dynamicActivityIdNode = dynamicActivityIdsNodeList.item(j);
							if (dynamicActivityIdNode.getNodeType() == Node.ELEMENT_NODE) {
								Element dynamicActivityElement = (Element) dynamicActivityIdNode;
								String id = dynamicActivityElement.getAttribute("id");
								dynamicActivityIds.add(id);
							}
						}
						
						Step step = new Step(stepType, activityAssignment, chapterAssignment, status, shortName, longName,fileName, stepID, notes, rating, dynamicActivityIds);
						availableSteps.add(step);
					}
				}
				return availableSteps ;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	// Parse AvailableDynamicActivities
	public ArrayList<DynamicActivity> parseAvailableDynamicActivitiesXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				ArrayList<DynamicActivity> availableDynamicActivities = new ArrayList<DynamicActivity>();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList dynamicActivitiesNodeList = doc.getElementsByTagName("dynamic_activity");
				for (int i = 0; i < dynamicActivitiesNodeList.getLength(); i++) {
					Node dynamicActivityNode = dynamicActivitiesNodeList.item(i);
					// Dynamic Activity
					if (dynamicActivityNode.getNodeType() == Node.ELEMENT_NODE) {
						Element dynamicActivityElement = (Element) dynamicActivityNode;
						String dynamicActivityID = dynamicActivityElement.getAttribute("id");
						String dynamicActivityName = dynamicActivityElement.getAttribute("name");
						String dynamicActivityStatus = dynamicActivityElement.getAttribute("status");
						DynamicActivity dynamicActivity = new DynamicActivity(dynamicActivityID, dynamicActivityName, dynamicActivityStatus);
						availableDynamicActivities.add(dynamicActivity);
					}
				}
				return availableDynamicActivities;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	// Parse AvailableResources
	public HashMap<String, String> parseAvailableResourcesXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				HashMap<String, String> availableResources = new HashMap<String, String>();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList resourceNodeList = doc.getElementsByTagName("resource");
				for (int i = 0; i < resourceNodeList.getLength(); i++) {
					Node resourceNode = resourceNodeList.item(i);
					// Resource
					if (resourceNode.getNodeType() == Node.ELEMENT_NODE) {
						Element resourceElement = (Element) resourceNode;
						String id = resourceElement.getAttribute("id");
						String name = resourceElement.getAttribute("name");

						availableResources.put(id, name);
					}
				}
				return availableResources;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	// Parse AvailableIntro
	public HashMap<String, String> parseAvailableIntroXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				HashMap<String, String> availableIntroPanels = new HashMap<String, String>();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList intoNodeList = doc.getElementsByTagName("introPanel");
				for (int i = 0; i < intoNodeList.getLength(); i++) {
					Node introNode = intoNodeList.item(i);
					// Intro
					if (introNode.getNodeType() == Node.ELEMENT_NODE) {
						Element introElement = (Element) introNode;
						String id = introElement.getAttribute("id");
						String name = introElement.getAttribute("name");

						availableIntroPanels.put(id, name);
					}
				}
				return availableIntroPanels;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}

	//Parse GoalCategories
	public ArrayList<GoalCategory> parseGoalCategoriesXML(File xmlFile){
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				ArrayList<GoalCategory> goalCategories = new ArrayList<GoalCategory>();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList goalCategoryNodeList = doc.getElementsByTagName("goalCategory");
				for (int i = 0; i < goalCategoryNodeList.getLength(); i++) {
					Node goalCategoryNode = goalCategoryNodeList.item(i);
					//Goal Category
					if(goalCategoryNode.getNodeType() == Node.ELEMENT_NODE) {
						Element goalCategoryElement = (Element) goalCategoryNode;
						String categoryName = goalCategoryElement.getAttribute("categoryName");
						NodeList assignedActivityNodeList = goalCategoryElement.getElementsByTagName("assignedActivity");
						ArrayList<String> listOfActivityIDs = new ArrayList<String>();
						for(int j =0; j < assignedActivityNodeList.getLength(); j++) {
							Node assignedActivityNode = assignedActivityNodeList.item(j);
							//Assigned Activity
							if(assignedActivityNode.getNodeType() == Node.ELEMENT_NODE) {
								Element assignedActivityElement = (Element) assignedActivityNode;
								String activityID = assignedActivityElement.getAttribute("activityID");
								listOfActivityIDs.add(activityID);
							}
						}
						GoalCategory goalCategory = new GoalCategory(categoryName, listOfActivityIDs);
						goalCategories.add(goalCategory);
					}
				}
				return goalCategories;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	//Parse all projects
	public ArrayList<Project> parseAllProjects(File ERBProjectDirectory, ArrayList<Activity> activities){
		ArrayList<Project> projects = new ArrayList<Project>();
		if(ERBProjectDirectory != null && ERBProjectDirectory.exists()) {
			File [] projectDirectories = ERBProjectDirectory.listFiles();
			for(File projectDirectory : projectDirectories) {
				if(projectDirectory.isDirectory()) {
					File projectMetaFile = new File(projectDirectory.getPath() + "\\Meta.xml");
					if(projectMetaFile.exists()) {
						Project project = parseProjectXML(projectMetaFile, activities);
						projects.add(project);
					}
				}
			}
		}
		return projects;
	}
	
	//Parse Project
	public Project parseProjectXML(File xmlFile, ArrayList<Activity> activities){
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList projectNodeList = doc.getElementsByTagName("project");
				for(int i = 0; i < projectNodeList.getLength(); i++) {
					Node projectNode = projectNodeList.item(i);
					//Project
					if(projectNode.getNodeType() == Node.ELEMENT_NODE) {
						Element projectElement = (Element) projectNode;
						String projectName = projectElement.getAttribute("projectName");
						String projectType = projectElement.getAttribute("projectType");
						String projectCleanedName = projectElement.getAttribute("projectCleanedName");
						ArrayList<Goal> listOfGoals = new ArrayList<Goal>();
						NodeList goalsNodeList = projectElement.getElementsByTagName("goal");
						for(int j =0; j < goalsNodeList.getLength(); j++) {
							Node goalNode = goalsNodeList.item(j);
							//Goal
							if(goalNode.getNodeType() == Node.ELEMENT_NODE) {
								Element goalElement = (Element) goalNode;
								String goalName = goalElement.getAttribute("goalName");
								String goalCleanedName = goalElement.getAttribute("goalCleanedName");
								ArrayList<GoalCategory> listOfSelectedGoalCategories = new ArrayList<GoalCategory>();
								String goalDescription = goalElement.getAttribute("goalDescription");
								NodeList goalCategoryNodeList = goalElement.getElementsByTagName("goalCategory");
								for (int k = 0; k < goalCategoryNodeList.getLength(); k++) {
									Node goalCategoryNode = goalCategoryNodeList.item(k);
									//Goal Category
									if(goalCategoryNode.getNodeType() == Node.ELEMENT_NODE) {
										Element goalCategoryElement = (Element) goalCategoryNode;
										String categoryName = goalCategoryElement.getAttribute("categoryName");
										ArrayList<String> listOfActivityIDs = new ArrayList<String>();
										NodeList assignedActivityNodeList = goalCategoryElement.getElementsByTagName("assignedActivity");
										for(int l =0; l < assignedActivityNodeList.getLength(); l++) {
											Node assignedActivityNode = assignedActivityNodeList.item(l);
											//Assigned Activity
											if(assignedActivityNode.getNodeType() == Node.ELEMENT_NODE) {
												Element assignedActivityElement = (Element) assignedActivityNode;
												String activityID = assignedActivityElement.getAttribute("activityID");
												listOfActivityIDs.add(activityID);
											}
										}
										GoalCategory goalCategory = new GoalCategory(categoryName, listOfActivityIDs);
										listOfSelectedGoalCategories.add(goalCategory);
									}
								}
								Goal goal = new Goal(app, goalName, goalCleanedName, goalDescription, listOfSelectedGoalCategories);
								listOfGoals.add(goal);
							}
						}
						Project project = new Project(projectName, projectType, projectCleanedName, listOfGoals);
						for(Goal goal: listOfGoals) {
							goal.setChapters(activities, project);
						}
						return project;
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;	
	}
	
	//Parse Goal
	public ArrayList<Chapter> parseGoalXML(File xmlFile, ArrayList<Activity> activities){
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				
				ArrayList<Chapter> chapters = new ArrayList<Chapter>();
				NodeList chapterNodeList = doc.getElementsByTagName("Chapter");
				for(int i = 0; i < chapterNodeList.getLength(); i++) {
					Node chapterNode = chapterNodeList.item(i);
					//CHAPTER
					if(chapterNode.getNodeType() == Node.ELEMENT_NODE) {
						Element chapterElement = (Element) chapterNode;
						int chapterNum = Integer.parseInt(chapterElement.getAttribute("chapterNum"));
						String numericName = chapterElement.getAttribute("numericName");
						String stringName = chapterElement.getAttribute("stringName");
						String chapterDescription = chapterElement.getAttribute("description");
						String chapterNotes = chapterElement.getAttribute("notes");
						String chapterGUID = chapterElement.getAttribute("GUID");						
						
						Chapter chapter = new Chapter(chapterNum, numericName, stringName, chapterDescription, chapterNotes);
						chapter.setGUID(chapterGUID);
						chapter.assignActivityIds(app.getAvailableActivities());
						chapter.assignStepIds(app.getAvailableSteps());
						
						NodeList cActivityNodeList = chapterElement.getElementsByTagName("ChapterActivity");
						ArrayList<Activity> listOfChapterActivities = new ArrayList<Activity>();
						for(int j =0; j<cActivityNodeList.getLength(); j++) {
							Node cActivityNode = cActivityNodeList.item(j);
							//CHAPTER -> ACTIVITY
							if(cActivityNode.getNodeType() == Node.ELEMENT_NODE) {
								Element cActivityElement = (Element) cActivityNode;
								String activityGUID = cActivityElement.getAttribute("GUID");
								String activityID = cActivityElement.getAttribute("activityID");
								String activityNotes = cActivityElement.getAttribute("notes");
								String activityRating = cActivityElement.getAttribute("rating");
								String activityStatus = cActivityElement.getAttribute("status");

								Activity activity = app.getActivityByID(activityID, app.getAvailableActivities());
								if(activity != null) {
									activity = activity.cloneActivity();
									activity.setChapterAssignment(String.valueOf(chapter.getChapterNum()));
									activity.setStatus(activityStatus);
									activity.setNotes(activityNotes);
									activity.setRating(activityRating);
									activity.setGUID(activityGUID);
									listOfChapterActivities.add(activity);
								}
								
								NodeList caStepsNodeList = cActivityElement.getElementsByTagName("ChapterActivityStep");
								ArrayList<Step> listOfChapterActivitySteps = new ArrayList<Step>();
								for(int k =0; k<caStepsNodeList.getLength();k++) {
									Node caStepNode = caStepsNodeList.item(k);
									//CHAPTER -> ACTIVITY -> STEP
									if(caStepNode.getNodeType() == Node.ELEMENT_NODE) {
										Element caStepElement = (Element) caStepNode;
										String stepGUID = caStepElement.getAttribute("GUID");
										String stepNotes = caStepElement.getAttribute("notes");
										String stepRating = caStepElement.getAttribute("rating");
										String stepStatus = caStepElement.getAttribute("status");
										String stepID = caStepElement.getAttribute("stepID");
										
										Step step = app.getStepByID(stepID, app.getAvailableSteps());
										if(step != null) {
											step = step.cloneStep();
											step.setGUID(stepGUID);
											step.setNotes(stepNotes);
											step.setRating(stepRating);
											step.setStatus(stepStatus);
											listOfChapterActivitySteps.add(step);
										}
										
										NodeList casDynamicActivitiesNodeList = caStepElement.getElementsByTagName("ChapterActivityStepDynamicActivity");
										ArrayList<DynamicActivity> listOfChapterActivityStepDynamicActivities = new ArrayList<DynamicActivity>();
										for(int l =0; l < casDynamicActivitiesNodeList.getLength(); l++) {
											Node casDynamicActivityNode = casDynamicActivitiesNodeList.item(l);
											//CHAPTER -> ACTIVITY -> STEP -> DYNAMIC ACTIVITY
											if(casDynamicActivityNode.getNodeType() == Node.ELEMENT_NODE) {
												Element casDynamicActivityElement = (Element) casDynamicActivityNode;
												String dynamicActivityGUID = casDynamicActivityElement.getAttribute("GUID");
												String dynamicActivityID = casDynamicActivityElement.getAttribute("id");
												String dynamicActivityStatus = casDynamicActivityElement.getAttribute("status");
												
												DynamicActivity dynamicActivity = app.getDynamicActivityById(dynamicActivityID, app.getAvailableDynamicActivities());
												if(dynamicActivity != null) {
													dynamicActivity = dynamicActivity.cloneDynamicActivity();
													dynamicActivity.setGUID(dynamicActivityGUID);
													dynamicActivity.setStatus(dynamicActivityStatus);
													listOfChapterActivityStepDynamicActivities.add(dynamicActivity);
												}
											}
										}
										step.setAssignedDynamicActivities(listOfChapterActivityStepDynamicActivities);
									}
								}
								
								NodeList caDynamicActivitesNodeList = cActivityElement.getElementsByTagName("ChapterActivityDynamicActivity");
								ArrayList<DynamicActivity> listOfChapterActivityDynamicActivities = new ArrayList<DynamicActivity>();
								for(int m=0; m < caDynamicActivitesNodeList.getLength(); m++) {
									Node caDynamicActivityNode = caDynamicActivitesNodeList.item(m);
									//CHAPTER -> ACTIVITY -> DYNAMIC ACTIVITY
									if(caDynamicActivityNode.getNodeType() == Node.ELEMENT_NODE) {
										Element caDynamicActivityElement = (Element) caDynamicActivityNode;
										String dynamicActivityGUID = caDynamicActivityElement.getAttribute("GUID");
										String dynamicActivityID = caDynamicActivityElement.getAttribute("id");
										String dynamicActivityStatus = caDynamicActivityElement.getAttribute("status");
										
										DynamicActivity dynamicActivity = app.getDynamicActivityById(dynamicActivityID, app.getAvailableDynamicActivities());
										if(dynamicActivity != null) {
											dynamicActivity = dynamicActivity.cloneDynamicActivity();
											dynamicActivity.setGUID(dynamicActivityGUID);
											dynamicActivity.setStatus(dynamicActivityStatus);
											listOfChapterActivityDynamicActivities.add(dynamicActivity);
										}
									}
								}
								activity.setAssignedDynamicActivities(listOfChapterActivityDynamicActivities);
								activity.setAssignedSteps(listOfChapterActivitySteps);
							}
						}
						
						NodeList cStepNodeList = chapterElement.getElementsByTagName("ChapterStep");
						ArrayList<Step> listOfChapterSteps = new ArrayList<Step>();
						for(int n=0; n < cStepNodeList.getLength(); n++) {
							Node cStepNode = cStepNodeList.item(n);
							//CHAPTER -> STEP
							if(cStepNode.getNodeType() == Node.ELEMENT_NODE) {
								Element cStepElement = (Element) cStepNode;
								String stepGUID = cStepElement.getAttribute("GUID");
								String stepNotes = cStepElement.getAttribute("notes");
								String stepRating = cStepElement.getAttribute("rating");
								String stepStatus = cStepElement.getAttribute("status");
								String stepID = cStepElement.getAttribute("stepID");
								
								Step step = app.getStepByID(stepID, app.getAvailableSteps());
								if(step != null) {
									step = step.cloneStep();
									step.setGUID(stepGUID);
									step.setNotes(stepNotes);
									step.setRating(stepRating);
									step.setStatus(stepStatus);
									listOfChapterSteps.add(step);
								}
								
								NodeList csDynamicActivitesNodeList = cStepElement.getElementsByTagName("ChapterStepDynamicActivity");
								ArrayList<DynamicActivity> listOfChapterStepDynamicActivities = new ArrayList<DynamicActivity>();
								for(int o=0; o<csDynamicActivitesNodeList.getLength();o++) {
									Node csDynamicActivityNode = csDynamicActivitesNodeList.item(o);
									//CHAPTER -> STEP -> DYNAMIC ACTIVITY
									if(csDynamicActivityNode.getNodeType() == Node.ELEMENT_NODE) {
										Element csDynamicActivityElement = (Element) csDynamicActivityNode;
										String dynamicActivityGUID = csDynamicActivityElement.getAttribute("GUID");
										String dynamicActivityID = csDynamicActivityElement.getAttribute("id");
										String dynamicActivityStatus = csDynamicActivityElement.getAttribute("status");
										
										DynamicActivity dynamicActivity = app.getDynamicActivityById(dynamicActivityID, app.getAvailableDynamicActivities());
										if(dynamicActivity != null) {
											dynamicActivity = dynamicActivity.cloneDynamicActivity();
											dynamicActivity.setGUID(dynamicActivityGUID);
											dynamicActivity.setStatus(dynamicActivityStatus);
											listOfChapterStepDynamicActivities.add(dynamicActivity);
										}
									}
								}
								step.setAssignedDynamicActivities(listOfChapterStepDynamicActivities);
							}
						}
						chapter.setAssignedSteps(listOfChapterSteps);
						chapter.setAssignedActivities(listOfChapterActivities);
						chapters.add(chapter);
					}
				}
				return chapters;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;	
	}
	
	public void writeProjectMetaXML(File xmlFile, Project project) {
		if (xmlFile != null && project != null) {
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();
				Element rootElement = document.createElement("project");
				document.appendChild(rootElement);
				rootElement.setAttribute("projectName", project.getProjectName());
				rootElement.setAttribute("projectType", project.getProjectType());
				rootElement.setAttribute("projectCleanedName", project.getProjectCleanedName());
				Element goalsElement = document.createElement("goals");
				for (Goal goal : project.getProjectGoals()) {
					Element goalElement = document.createElement("goal");
					goalElement.setAttribute("goalName", goal.getGoalName());
					goalElement.setAttribute("goalCleanedName", goal.getGoalCleanedName());
					goalElement.setAttribute("goalDescription", goal.getGoalDescription());
					Element goalsCategoryElement = document.createElement("goalsCategories");
					for (GoalCategory goalCategory : goal.getListOfSelectedGoalCategories()) {
						Element goalCategoryElement = document.createElement("goalCategory");
						goalCategoryElement.setAttribute("categoryName", goalCategory.getCategoryName());
						Element assignedActivitesElement = document.createElement("assignedActivities");
						for (String activityID : goalCategory.getListOfAssignedActivityIDs()) {
							Element assignedActivityElement = document.createElement("assignedActivity");
							assignedActivityElement.setAttribute("activityID", activityID);
							assignedActivitesElement.appendChild(assignedActivityElement);
						}
						goalCategoryElement.appendChild(assignedActivitesElement);
						goalsCategoryElement.appendChild(goalCategoryElement);
					}
					goalElement.appendChild(goalsCategoryElement);
					goalsElement.appendChild(goalElement);
				}
				rootElement.appendChild(goalsElement);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource domSource = new DOMSource(document);

				StreamResult file = new StreamResult(xmlFile);
				transformer.transform(domSource, file);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot writeProjectMetaXML. xmlFile or project is null.");
		}
	}
	
	public void writeGoalMetaXML(File xmlFile, ArrayList<Chapter> chapters) {
		if (xmlFile != null && chapters != null) {
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();
				Element rootElement = document.createElement("chapters");
				document.appendChild(rootElement);
				//CHAPTER
				for (Chapter chapter : chapters) {
					Element chapterElement = document.createElement("Chapter");
					chapterElement.setAttribute("chapterNum", String.valueOf(chapter.getChapterNum()));
					chapterElement.setAttribute("description", chapter.getDescriptionName());
					chapterElement.setAttribute("notes", chapter.getNotes());
					chapterElement.setAttribute("numericName", chapter.getNumericName());
					chapterElement.setAttribute("stringName", chapter.getStringName());
					chapterElement.setAttribute("GUID", chapter.getGUID());
					Element assignedActivitiesElement = document.createElement("assignedActivities");
					//CHAPTER -> ACTIVITY
					for (Activity activity : chapter.getAssignedActivities()) {
						Element assignedActivityElement = document.createElement("ChapterActivity");
						assignedActivityElement.setAttribute("status", activity.getStatus());
						assignedActivityElement.setAttribute("activityID", activity.getActivityID());
						assignedActivityElement.setAttribute("notes", activity.getNotes());
						assignedActivityElement.setAttribute("rating", activity.getRating());
						assignedActivityElement.setAttribute("GUID", activity.getGUID());
						assignedActivitiesElement.appendChild(assignedActivityElement);
						
						Element assignedStepsElement = document.createElement("assignedSteps");
						//CHAPTER -> ACTIVITY -> STEP
						for(Step step: activity.getAssignedSteps()) {
							Element assignedActivityStepElement = document.createElement("ChapterActivityStep");
							assignedActivityStepElement.setAttribute("stepType", step.getStepType());
							assignedActivityStepElement.setAttribute("status", step.getStatus());
							assignedActivityStepElement.setAttribute("stepID", step.getStepID());
							assignedActivityStepElement.setAttribute("notes", step.getNotes());
							assignedActivityStepElement.setAttribute("rating", step.getRating());
							assignedActivityStepElement.setAttribute("GUID", step.getGUID());
							assignedStepsElement.appendChild(assignedActivityStepElement);
							
							Element assignedDynamicActivitiesElement = document.createElement("assignedDynamicActivities");
							//CHAPTER -> ACTIVITY -> STEP -> DYNAMIC ACTIVITY
							for(DynamicActivity dynamicActivity : step.getAssignedDynamicActivities()) {
								Element assignedActivityDynamicActivityElement = document.createElement("ChapterActivityStepDynamicActivity");
								assignedActivityDynamicActivityElement.setAttribute("id", dynamicActivity.getId());
								assignedActivityDynamicActivityElement.setAttribute("name", dynamicActivity.getName());
								assignedActivityDynamicActivityElement.setAttribute("status", dynamicActivity.getStatus());
								assignedActivityDynamicActivityElement.setAttribute("GUID", dynamicActivity.getGUID());
								assignedDynamicActivitiesElement.appendChild(assignedActivityDynamicActivityElement);
							}
							assignedActivityStepElement.appendChild(assignedDynamicActivitiesElement);
						}
						assignedActivityElement.appendChild(assignedStepsElement);
						
						Element assignedDynamicActivitiesElement = document.createElement("assignedDynamicActivities");
						//CHAPTER -> ACTIVITY -> DYNAMIC ACTIVITY
						for(DynamicActivity dynamicActivity : activity.getAssignedDynamicActivities()) {
							Element assignedActivityDynamicActivityElement = document.createElement("ChapterActivityDynamicActivity");
							assignedActivityDynamicActivityElement.setAttribute("id", dynamicActivity.getId());
							assignedActivityDynamicActivityElement.setAttribute("name", dynamicActivity.getName());
							assignedActivityDynamicActivityElement.setAttribute("status", dynamicActivity.getStatus());
							assignedActivityDynamicActivityElement.setAttribute("GUID", dynamicActivity.getGUID());
							assignedDynamicActivitiesElement.appendChild(assignedActivityDynamicActivityElement);
						}
						assignedActivityElement.appendChild(assignedDynamicActivitiesElement);
						
						
					}
					Element assignedStepsElement = document.createElement("assignedSteps");
					//CHAPTER -> STEP
					for(Step step: chapter.getAssignedSteps()) {
						Element assignedStepElement = document.createElement("ChapterStep");
						assignedStepElement.setAttribute("stepType", step.getStepType());
						assignedStepElement.setAttribute("status", step.getStatus());
						assignedStepElement.setAttribute("stepID", step.getStepID());
						assignedStepElement.setAttribute("notes", step.getNotes());
						assignedStepElement.setAttribute("rating", step.getRating());
						assignedStepElement.setAttribute("GUID", step.getGUID());
						assignedStepsElement.appendChild(assignedStepElement);
						
						Element assignedDynamicActivitiesElement = document.createElement("assignedDynamicActivities");
						//CHAPTER -> STEP -> DYNAMIC ACTIVITY
						for(DynamicActivity dynamicActivity : step.getAssignedDynamicActivities()) {
							Element assignedStepDynamicActivityElement = document.createElement("ChapterStepDynamicActivity");
							assignedStepDynamicActivityElement.setAttribute("id", dynamicActivity.getId());
							assignedStepDynamicActivityElement.setAttribute("name", dynamicActivity.getName());
							assignedStepDynamicActivityElement.setAttribute("status", dynamicActivity.getStatus());
							assignedStepDynamicActivityElement.setAttribute("GUID", dynamicActivity.getGUID());
							assignedDynamicActivitiesElement.appendChild(assignedStepDynamicActivityElement);
						}
						assignedStepElement.appendChild(assignedDynamicActivitiesElement);
					}
				
					chapterElement.appendChild(assignedActivitiesElement);
					chapterElement.appendChild(assignedStepsElement);
					rootElement.appendChild(chapterElement);
				}

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource domSource = new DOMSource(document);

				StreamResult file = new StreamResult(xmlFile);
				transformer.transform(domSource, file);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot writeGoalMetaXML. xmlFile or chapters is null.");
		}
	}
	
	public void writeNoteboardDataXML(File xmlFile, ArrayList<CategorySectionController> categories) {
		if (xmlFile != null && categories != null) {
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();
				Element rootElement = document.createElement("categories");
				document.appendChild(rootElement);
				for (CategorySectionController category : categories) {
					Element categoryElement = document.createElement("category");
					categoryElement.setAttribute("name", category.getCategoryName());
					Element notesElement = document.createElement("notes");
					for (PostItNoteController postItNoteController : category.getListOfPostItNoteControllers()) {
						Element noteElement = document.createElement("note");
						noteElement.setAttribute("content", postItNoteController.getPostItNoteText());
						noteElement.setAttribute("color", postItNoteController.getPostItNoteColor());
						noteElement.setAttribute("likes", postItNoteController.getNumberLabel().getText());
						noteElement.setAttribute("position",
								String.valueOf(postItNoteController.getPostItNoteIndex(category)));
						notesElement.appendChild(noteElement);
					}
					categoryElement.appendChild(notesElement);
					rootElement.appendChild(categoryElement);
				}

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource domSource = new DOMSource(document);

				StreamResult file = new StreamResult(xmlFile);
				transformer.transform(domSource, file);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot writeNoteboardDataXML. xmlFile or categories is null.");
		}
	}
	
	public ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> parseNoteboardDataXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				
				NodeList categoryNodeList = doc.getElementsByTagName("category");
				ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> listOfCategoryHashMaps = new ArrayList<HashMap<String,ArrayList<HashMap<String,String>>>>();
				for(int i = 0; i < categoryNodeList.getLength(); i++) {
					Node categoryNode = categoryNodeList.item(i);
					//Category
					if(categoryNode.getNodeType() == Node.ELEMENT_NODE) {
						Element categoryElement = (Element) categoryNode;
						String categoryName = categoryElement.getAttribute("name");
						HashMap<String, ArrayList<HashMap<String, String>>> categoryHashMaps = new HashMap<String, ArrayList<HashMap<String,String>>>();
						NodeList noteNodeList = categoryElement.getElementsByTagName("note");
						ArrayList<HashMap<String, String>> listOfNotesHashMaps = new ArrayList<HashMap<String,String>>();
						for(int j =0; j<noteNodeList.getLength(); j++) {
							Node noteNode = noteNodeList.item(j);
							//Note
							if(noteNode.getNodeType() == Node.ELEMENT_NODE) {
								HashMap<String, String> noteHashMap = new HashMap<String, String>();
								Element nodeElement = (Element) noteNode;
								String color = nodeElement.getAttribute("color");
								noteHashMap.put("color", color);
								String content = nodeElement.getAttribute("content");
								noteHashMap.put("content", content);
								String likes = nodeElement.getAttribute("likes");
								noteHashMap.put("likes", likes);
								String position = nodeElement.getAttribute("position");
								noteHashMap.put("position", position);
								listOfNotesHashMaps.add(noteHashMap);
							}
						}
						categoryHashMaps.put(categoryName, listOfNotesHashMaps);
						listOfCategoryHashMaps.add(categoryHashMaps);
					}
				}
				return listOfCategoryHashMaps;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	public HashMap<String, ArrayList<TextFlow>> parseMainFormContentXML(File xmlFile, MainFormController formContentController) {
		if (xmlFile != null && xmlFile.exists()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();

				HashMap<String, ArrayList<TextFlow>> contentHashMap = new HashMap<String, ArrayList<TextFlow>>();

				NodeList containerNodeList = doc.getElementsByTagName("container");
				for (int i = 0; i < containerNodeList.getLength(); i++) {
					Node containerNode = containerNodeList.item(i);
					if (containerNode.getNodeType() == Node.ELEMENT_NODE) {
						// Container
						Element containerElement = (Element) containerNode;
						String containerId = containerElement.getAttribute("id");
						ArrayList<TextFlow> textBlocks = new ArrayList<TextFlow>();
						NodeList textBlockNodeList = containerElement.getElementsByTagName("textBlock");
						for (int j = 0; j < textBlockNodeList.getLength(); j++) {
							Node textBlockNode = textBlockNodeList.item(j);
							// TextBlock
							if (textBlockNode.getNodeType() == Node.ELEMENT_NODE) {
								TextFlow textFlow = new TextFlow();
								Element textBlockElement = (Element) textBlockNode;
								NodeList textNodeList = textBlockElement.getElementsByTagName("text");
								for (int k = 0; k < textNodeList.getLength(); k++) {
									Node textNode = textNodeList.item(k);
									// Text
									if (textNode.getNodeType() == Node.ELEMENT_NODE) {
										Element textElement = (Element) textNode;
										double size = Double.parseDouble(textElement.getAttribute("size"));
										String fontFamily = textElement.getAttribute("fontFamily");
										String fontStyle = textElement.getAttribute("fontStyle");
										String text = textElement.getAttribute("text");
										Text t = new Text(text);
										if (fontStyle.contentEquals("Hyperlink")) {
											String linkType = textElement.getAttribute("linkType");
											String link = textElement.getAttribute("link");
											t.setStyle("-fx-fill:#4d90bc");
											t.setFont(Font.font(fontFamily, FontWeight.NORMAL, size));
											t.setOnMouseEntered(e -> t.setUnderline(true));
											t.setOnMouseExited(e -> t.setUnderline(false));
											t.setOnMouseClicked(e-> formContentController.handleHyperlink(t, linkType, link));
										} else {
											if (fontStyle.contentEquals("Bold")) {
												t.setFont(Font.font(fontFamily, FontWeight.BOLD, size));
											} else if (fontStyle.contentEquals("Underlined")) {
												t.setUnderline(true);
												t.setFont(Font.font(fontFamily, size));
											} else {
												t.setFont(Font.font(fontFamily, FontWeight.NORMAL, size));
											}
										}
										textFlow.getChildren().add(t);
									}
								}
								textBlocks.add(textFlow);
							}
						}
						contentHashMap.put(containerId, textBlocks);
					}
				}
				return contentHashMap;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot parseFormContentXML. xmlFile = " + xmlFile);
		}
		return null;
	}
	
	public HashMap<ArrayList<Text>, String> parseOutputFormContentXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();

				HashMap<ArrayList<Text>, String> contentList = new LinkedHashMap<ArrayList<Text>, String>();

				NodeList containerNodeList = doc.getElementsByTagName("container");
				for (int i = 0; i < containerNodeList.getLength(); i++) {
					Node containerNode = containerNodeList.item(i);
					if (containerNode.getNodeType() == Node.ELEMENT_NODE) {
						// Container
						Element containerElement = (Element) containerNode;
						NodeList textBlockNodeList = containerElement.getElementsByTagName("textBlock");
						for (int j = 0; j < textBlockNodeList.getLength(); j++) {
							Node textBlockNode = textBlockNodeList.item(j);
							// TextBlock
							if (textBlockNode.getNodeType() == Node.ELEMENT_NODE) {
								Element textBlockElement = (Element) textBlockNode;
								String textBlockType = textBlockElement.getAttribute("type");
								ArrayList<Text> textList = new ArrayList<Text>();
								NodeList textNodeList = textBlockElement.getElementsByTagName("text");
								for (int k = 0; k < textNodeList.getLength(); k++) {
									Node textNode = textNodeList.item(k);
									// Text
									if (textNode.getNodeType() == Node.ELEMENT_NODE) {
										Element textElement = (Element) textNode;
										double size = Double.parseDouble(textElement.getAttribute("size"));
										String fontFamily = textElement.getAttribute("fontFamily");
										String fontStyle = textElement.getAttribute("fontStyle");
										String text = textElement.getAttribute("text");
										
										Text t = new Text(text);
										t.setFont(Font.font(fontFamily, size));
										t.setId(fontStyle);
										textList.add(t);
									}
								}
								contentList.put(textList, textBlockType);
							}
						}
					}
				}
				return contentList;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot parseFormContentXML. xmlFile = " + xmlFile);
		}
		return null;
	}
	
	public void writeOutputFormDataXML(File xmlFile, ArrayList<javafx.scene.Node> outputFormNodes) {
		if (xmlFile != null && outputFormNodes != null) {
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();
				Element rootElement = document.createElement("textBlocks");
				document.appendChild(rootElement);
				Element containerElement = document.createElement("container");
				containerElement.setAttribute("id", "formVBox");
				for(javafx.scene.Node node: outputFormNodes) {
					Element textBlockElement = document.createElement("textBlock");
					textBlockElement.setAttribute("type", node.getId());
					
					if(node.toString().contains("TextFlow")) {
						TextFlow textFlow = (TextFlow) node;
						for(int i =0; i < textFlow.getChildren().size(); i++) {
							Text text = (Text) textFlow.getChildren().get(i);
							Element textElement = document.createElement("text");
							textElement.setAttribute("size", String.valueOf(text.getFont().getSize()));
							textElement.setAttribute("fontFamily", text.getFont().getFamily());
							textElement.setAttribute("fontStyle", text.getId());
							textElement.setAttribute("text", text.getText());
							textBlockElement.appendChild(textElement);
						}
					} else if (node.toString().contains("TextArea")) {
						TextArea textArea = (TextArea) node;
						Element textElement = document.createElement("text");
						textElement.setAttribute("size", "16.0");
						textElement.setAttribute("fontFamily", "System");
						if(textArea.getText() != null && textArea.getText().length() > 0) {
							textElement.setAttribute("text", textArea.getText());
							textElement.setAttribute("fontStyle", "Regular");
						} else {
							textElement.setAttribute("text", textArea.getPromptText());
							textElement.setAttribute("fontStyle", "Prompt");
						}
						textBlockElement.appendChild(textElement);
					}
					containerElement.appendChild(textBlockElement);
				}
				rootElement.appendChild(containerElement);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource domSource = new DOMSource(document);

				StreamResult file = new StreamResult(xmlFile);
				transformer.transform(domSource, file);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot writeOutputFormDataXML. xmlFile or outputFormNodes is null.");
		}
	}

}
