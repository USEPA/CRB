package com.epa.erb;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Activity extends ERBItem{
	
	private String id;
	private String guid;
	private String longName;
	private String shortName;

	private String status;
	private String notes;
	private String rating;
	
	public Activity(String id, String guid, String longName, String shortName, String status, String notes, String rating) {
		super(id, guid, longName, shortName, null);
		this.status = status;
		this.notes = notes;
		this.rating = rating;
	}
	
	private ArrayList<Step> assignedSteps = new ArrayList<Step>();
	private ArrayList<InteractiveActivity> assignedDynamicActivities = new ArrayList<InteractiveActivity>();
	private Logger logger = LogManager.getLogger(Activity.class);
	
	public Activity cloneActivity() {
		Activity clonedActivity = new Activity(id, guid, longName, shortName, status, notes, rating);
		clonedActivity.setAssignedSteps(assignedSteps);
		clonedActivity.setAssignedDynamicActivities(assignedDynamicActivities);
		return clonedActivity;
	}

	public void addStep(Step step) {
		if (step != null) {
			assignedSteps.add(step);
		} else {
			logger.error("Cannot addStep. step is null.");
		}
	}
	
	public void removeStep(Step step) {
		if (step != null) {
			assignedSteps.remove(step);
		} else {
			logger.error("Cannot removeStep. step is null.");
		}
	}
	
	public void addDynamicActivity(InteractiveActivity dynamicActivity) {
		if (dynamicActivity != null) {
			assignedDynamicActivities.add(dynamicActivity);
		} else {
			logger.error("Cannot addDynamicActivity. dynamicActivity is null.");
		}
	}
	
	public void removeDynamicActivity(InteractiveActivity dynamicActivity) {
		if (dynamicActivity != null) {
			assignedDynamicActivities.remove(dynamicActivity);
		} else {
			logger.error("Cannot removeDynamicActivity. dynamicActivity is null.");
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public ArrayList<Step> getAssignedSteps() {
		return assignedSteps;
	}

	public void setAssignedSteps(ArrayList<Step> assignedSteps) {
		this.assignedSteps = assignedSteps;
	}

	public ArrayList<InteractiveActivity> getAssignedDynamicActivities() {
		return assignedDynamicActivities;
	}

	public void setAssignedDynamicActivities(ArrayList<InteractiveActivity> assignedDynamicActivities) {
		this.assignedDynamicActivities = assignedDynamicActivities;
	}

}
