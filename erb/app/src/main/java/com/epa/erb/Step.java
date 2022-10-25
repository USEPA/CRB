package com.epa.erb;

import java.util.ArrayList;

public class Step {
	
	private String stepType;
	private String status;
	private String shortName;
	private String longName;
	private String stepID;
	private String notes;
	private String rating;
	public Step(String stepType, String status, String shortName, String longName, String stepID, String notes, String rating) {
		this.stepType = stepType;
		this.status = status;
		this.shortName = shortName;
		this.longName = longName;
		this.stepID = stepID;
		this.notes = notes;
		this.rating = rating;
	}
	
	private ArrayList<DynamicActivity> assignedDynamicActivities = new ArrayList<DynamicActivity>();
	
	private String GUID;
	
	public Step cloneStep() {
		Step clonedStep = new Step(stepType, status, shortName, longName, stepID, notes, rating);
		clonedStep.setGUID(GUID);
		clonedStep.setAssignedDynamicActivities(assignedDynamicActivities);
		return clonedStep;
	}

	public void resetAssignments() {
		assignedDynamicActivities.clear();
	}
	
	public void addDynamicActivity(DynamicActivity dynamicActivity) {
		if (dynamicActivity != null) {
			assignedDynamicActivities.add(dynamicActivity);
		}
	}
	
	public void removeDynamicActivity(DynamicActivity dynamicActivity) {
		if (dynamicActivity != null) {
			assignedDynamicActivities.remove(dynamicActivity);
		}
	}

	public String getStepType() {
		return stepType;
	}

	public void setStepType(String stepType) {
		this.stepType = stepType;
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

	public String getStepID() {
		return stepID;
	}

	public void setStepID(String activityID) {
		this.stepID = activityID;
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

	public ArrayList<DynamicActivity> getAssignedDynamicActivities() {
		return assignedDynamicActivities;
	}

	public void setAssignedDynamicActivities(ArrayList<DynamicActivity> assignedDynamicActivities) {
		this.assignedDynamicActivities = assignedDynamicActivities;
	}

	public String getGUID() {
		return GUID;
	}

	public void setGUID(String gUID) {
		GUID = gUID;
	}	
	
}
