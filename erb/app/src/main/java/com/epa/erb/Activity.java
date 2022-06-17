package com.epa.erb;

import java.util.ArrayList;
import com.epa.erb.chapter.PlanController;
import com.epa.erb.chapter.ReflectController;
import com.epa.erb.noteboard.NoteBoardContentController;
import com.epa.erb.worksheet.WorksheetContentController;

public class Activity {
	
	private ActivityType activityType;
	private String chapterAssignment; 
	private String status;
	private String shortName;
	private String longName;
	private String fileName;
	private String instructions;
	private String objectives;
	private String description;
	private String materials;
	private String time;
	private String who;
	private String activityID;
	private String notes;
	private String rating;
	public Activity(ActivityType activityType, String chapterAssignment, String status, String shortName, String longName, String fileName, String directions, String objectives, String description, String materials, String time, String who, String activityID, String notes, String rating) {
		this.activityType = activityType;
		this.chapterAssignment = chapterAssignment;
		this.status = status;
		this.shortName = shortName;
		this.longName = longName;
		this.fileName = fileName;
		this.instructions = directions;
		this.objectives = objectives;
		this.description = description;
		this.materials = materials;
		this.time = time;
		this.who = who;
		this.activityID = activityID;
		this.notes = notes;
		this.rating = rating;
	}
	
	private boolean isSaved = true;
	private PlanController planController;
	private ReflectController reflectController;
	private WorksheetContentController worksheetContentController;
	private NoteBoardContentController noteBoardContentController;
		
	public Activity() {
		
	}
	
	public ArrayList<String> getSplitMaterials(){
		ArrayList<String> listOfMaterials = new ArrayList<String>();
		String [] splitStrings = materials.split("\n");
		for(String s: splitStrings) {
			listOfMaterials.add(s);
		}
		return listOfMaterials;
	}
	
	public Activity cloneActivity() {
		Activity clonedActivity = new Activity(activityType, chapterAssignment, status, shortName, longName, fileName, instructions, objectives, description, materials, time, who, activityID, notes, rating);
		return clonedActivity;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public String getChapterAssignment() {
		return chapterAssignment;
	}

	public void setChapterAssignment(String chapterAssignment) {
		this.chapterAssignment = chapterAssignment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getObjectives() {
		return objectives;
	}

	public void setObjectives(String objectives) {
		this.objectives = objectives;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMaterials() {
		return materials;
	}

	public void setMaterials(String materials) {
		this.materials = materials;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}

	public String getActivityID() {
		return activityID;
	}

	public void setActivityID(String activityID) {
		this.activityID = activityID;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public boolean isSaved() {
		return isSaved;
	}

	public void setSaved(boolean isSaved) {
		this.isSaved = isSaved;
	}

	public PlanController getPlanController() {
		return planController;
	}

	public void setPlanController(PlanController planController) {
		this.planController = planController;
	}

	public ReflectController getReflectController() {
		return reflectController;
	}

	public void setReflectController(ReflectController reflectController) {
		this.reflectController = reflectController;
	}

	public WorksheetContentController getWorksheetContentController() {
		return worksheetContentController;
	}

	public void setWorksheetContentController(WorksheetContentController worksheetContentController) {
		this.worksheetContentController = worksheetContentController;
	}

	public NoteBoardContentController getNoteBoardContentController() {
		return noteBoardContentController;
	}

	public void setNoteBoardContentController(NoteBoardContentController noteBoardContentController) {
		this.noteBoardContentController = noteBoardContentController;
	}

}
