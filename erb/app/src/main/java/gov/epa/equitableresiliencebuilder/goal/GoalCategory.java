package gov.epa.equitableresiliencebuilder.goal;

import java.util.ArrayList;

import gov.epa.equitableresiliencebuilder.ERBContentItem;
public class GoalCategory {
	
	private String categoryName;
	private ArrayList<ERBContentItem> erbContentItems;
	public GoalCategory(String categoryName, ArrayList<ERBContentItem> erbContentItems) {
		this.categoryName = categoryName;
		this.erbContentItems = erbContentItems;
	}

	public String getCategoryName() {
		return categoryName;
	}
	
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public ArrayList<ERBContentItem> getErbContentItems() {
		return erbContentItems;
	}

	public void setErbContentItems(ArrayList<ERBContentItem> erbContentItems) {
		this.erbContentItems = erbContentItems;
	}
}
