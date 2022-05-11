package com.epa.erb;

public class Activity {
	
	private ActivityType activityType;
	private String chapterAssignment; 
	private String status;
	private String shortName;
	private String longName;
	private String fileName;
	private String directions;
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
		this.directions = directions;
		this.objectives = objectives;
		this.description = description;
		this.materials = materials;
		this.time = time;
		this.who = who;
		this.activityID = activityID;
		this.notes = notes;
		this.rating = rating;
	}
		
	public Activity() {
		
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

	public String getDirections() {
		return directions;
	}

	public void setDirections(String directions) {
		this.directions = directions;
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

	public String toString() {
		return  "--ActivityType-- " + "\n" + activityType.toString() +  "\n" + "----" + "\n" +
				"ShortName: " + shortName + "\n" + 
				"LongName: " + longName + "\n" + 
				"FileName: " + fileName + "\n" + 
				"Directions: " + directions + "\n" + 
				"Objectives: " + objectives + "\n" + 
				"Description: " + description + "\n" +
				"Materials: " + materials + "\n" + 
				"Time: " + time + "\n" + 
				"Who: " + who + "\n" +
				"ActivityID: " + activityID + "\n";
	}
	
}
