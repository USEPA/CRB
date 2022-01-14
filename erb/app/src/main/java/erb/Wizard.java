package erb;

import java.util.ArrayList;
import java.util.HashMap;
import erb_chapter1.Chap1Step1Controller;
import erb_chapter1.Chap1Step2Controller;
import erb_chapter1.Chap1Step3Controller;
import erb_chapter1.Chap1Step4Controller;
import erb_chapter1.Chap1Step5Controller;
import erb_chapter2.Chap2Step1Controller;
import erb_chapter2.Chap2Step2Controller;
import erb_chapter2.Chap2Step3Controller;
import erb_chapter2.Chap2Step4Controller;
import erb_chapter2.Chap2Step5Controller;
import erb_chapter3.Chap3Step1Controller;
import erb_chapter3.Chap3Step2Controller;
import erb_chapter3.Chap3Step3Controller;
import erb_chapter3.Chap3Step4Controller;
import erb_chapter3.Chap3Step5Controller;
import erb_chapter3.Chap3Step6Controller;
import erb_chapter4.Chap4Step1Controller;
import erb_chapter4.Chap4Step2Controller;
import erb_chapter4.Chap4Step3Controller;
import erb_chapter4.Chap4Step4Controller;
import erb_chapter5.Chap5Step1Controller;
import erb_chapter5.Chap5Step2Controller;
import erb_chapter5.Chap5Step3Controller;
import erb_chapter5.Chap5Step4Controller;
import erb_chapter5.Chap5Step5Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class Wizard {
	

	public Wizard () {
		
	}
	
	WizardContainerController wizardContainerController;
	ArrayList<Node> listOfPanelsInWizard = new ArrayList<Node>();
	ArrayList<Node> listOfChapter1PanelsInWizard = new ArrayList<Node>();
	ArrayList<Node> listOfChapter2PanelsInWizard = new ArrayList<Node>();
	ArrayList<Node> listOfChapter3PanelsInWizard = new ArrayList<Node>();
	ArrayList<Node> listOfChapter4PanelsInWizard = new ArrayList<Node>();
	ArrayList<Node> listOfChapter5PanelsInWizard = new ArrayList<Node>();
	HashMap<Integer, Node> mapOfPanelsInWizard = new HashMap<Integer, Node>();
	
	public void addPanel(Node node, int chapterNumber) {
		if(node != null) {
			listOfPanelsInWizard.add(node);
			int numPanels = mapOfPanelsInWizard.keySet().size();
			mapOfPanelsInWizard.put(numPanels, node);
			addPanelToChapterList(node, chapterNumber);
		}
	}
	
	public void addPanelToChapterList(Node node, int chapterNumber) {
		if(chapterNumber == 1) {
			listOfChapter1PanelsInWizard.add(node);
		}else if (chapterNumber ==2) {
			listOfChapter2PanelsInWizard.add(node);
		}else if (chapterNumber ==3) {
			listOfChapter3PanelsInWizard.add(node);
		}else if (chapterNumber ==4) {
			listOfChapter4PanelsInWizard.add(node);
		}else if (chapterNumber ==5) {
			listOfChapter5PanelsInWizard.add(node);
		}
	}
	
	public void removePanel(Node node) {
		if(node != null) {
			int index = listOfPanelsInWizard.indexOf(node);
			listOfPanelsInWizard.remove(node);
			mapOfPanelsInWizard.remove(index);
		}
	}
	
	public void setPanelsFromPreviousProject(Wizard wizard, PreviousProject previousProject) {
		String projectType = previousProject.getProjectType();
		if (projectType.contains("customOption")) {
			if (projectType.contains("1")) {
				wizard.setOnRampOption1Panels();
			} else if (projectType.contains("2")) {
				wizard.setOnRampOption2Panels();
			} else if (projectType.contains("3")) {
				wizard.setOnRampOption3Panels();
			}
		} else if (projectType.contains("sequential")) {
			wizard.setSequentialPanels();
		} else if (projectType.contains("freestyle")) {
		}
	}
	
	public void setSequentialPanels() {
		addChapter1PanelsSequentially();
		addChapter2PanelsSequentially();
		addChapter3PanelsSequentially();
		addChapter4PanelsSequentially();
		addChapter5PanelsSequentially();
		addFinal();
	}
	
	public void setOnRampOption1Panels() {
		addChapter3PanelsSequentially();
		addChapter4PanelsSequentially();
		addFinal();
	}
	
	public void setOnRampOption2Panels() {
		setSequentialPanels();
	}
	
	public void setOnRampOption3Panels() {
		addChapter3Step3();
		addChapter3Step4();
		addChapter3Step5();
		addFinal();
	}
	
	private void addChapter1PanelsSequentially() {
		addChapter1Step1();
		addChapter1Step2();
		addChapter1Step3();
		addChapter1Step4();
		addChapter1Step5();
	}
	
	private void addChapter2PanelsSequentially() {
		addChapter2Step1();
		addChapter2Step2();
		addChapter2Step3();
		addChapter2Step4();
		addChapter2Step5();
	}
	
	private void addChapter3PanelsSequentially() {
		addChapter3Step1();
		addChapter3Step2();
		addChapter3Step3();
		addChapter3Step4();
		addChapter3Step5();
		addChapter3Step6();		
	}
	
	private void addChapter4PanelsSequentially() {
		addChapter4Step1();
		addChapter4Step2();
		addChapter4Step3();
		addChapter4Step4();
	}
	
	private void addChapter5PanelsSequentially() {
		addChapter5Step1();
		addChapter5Step2();
		addChapter5Step3();
		addChapter5Step4();
		addChapter5Step5();
	}
	
	public Node getPanel(int index) {
		if (mapOfPanelsInWizard != null && mapOfPanelsInWizard.size() > 0) {
			return mapOfPanelsInWizard.get(index);
		} else {
			return null;
		}
	}
	
	public Node getPanel(String panelAccessibleText) {
		if (listOfPanelsInWizard != null && listOfPanelsInWizard.size() > 0) {
			for (Node node : listOfPanelsInWizard) {
				if (node.getAccessibleText().contentEquals(panelAccessibleText)) {
					return node;
				}
			}
		}
		return null;
	}
	
	public int getPanelIndex(Node panel) {
		if (mapOfPanelsInWizard != null && mapOfPanelsInWizard.size() > 0) {
			for (int key : mapOfPanelsInWizard.keySet()) {
				if (mapOfPanelsInWizard.get(key) == panel) {
					return key;
				}
			}
		}
		return -1;
	}
	
	public int getPanelIndex(String panelAccessibleText) {
		if (listOfPanelsInWizard != null && listOfPanelsInWizard.size() > 0) {
			for (Node node : listOfPanelsInWizard) {
				if (node.getAccessibleText().contentEquals(panelAccessibleText)) {
					return getPanelIndex(node);
				}
			}
		}
		return -1;
	}
	
	public String getPanelAccessibleText(Node panel) {
		if (mapOfPanelsInWizard != null && mapOfPanelsInWizard.size() > 0) {
			for (int key : mapOfPanelsInWizard.keySet()) {
				if (mapOfPanelsInWizard.get(key) == panel) {
					return mapOfPanelsInWizard.get(key).getAccessibleText();
				}
			}
		}
		return null;
	}
	
	public String getPanelAccessibleText(int index) {
		if (mapOfPanelsInWizard != null && mapOfPanelsInWizard.size() > 0) {
			return mapOfPanelsInWizard.get(index).getAccessibleText();
		} else {
			return null;
		}
	}
	
	public int getMaxWizardPanelIndex() {
		if(mapOfPanelsInWizard != null && mapOfPanelsInWizard.size() > 0) {
			return mapOfPanelsInWizard.size() -1;
		}else {
			return -1;
		}	
	}
	
	public ArrayList<Node> getListOfPanelsInWizard() {
		return listOfPanelsInWizard;
	}
	
	public ArrayList<Node> getListOfChapter1PanelsInWizard() {
		return listOfChapter1PanelsInWizard;
	}

	public ArrayList<Node> getListOfChapter2PanelsInWizard() {
		return listOfChapter2PanelsInWizard;
	}

	public ArrayList<Node> getListOfChapter3PanelsInWizard() {
		return listOfChapter3PanelsInWizard;
	}

	public ArrayList<Node> getListOfChapter4PanelsInWizard() {
		return listOfChapter4PanelsInWizard;
	}

	public ArrayList<Node> getListOfChapter5PanelsInWizard() {
		return listOfChapter5PanelsInWizard;
	}

	public HashMap<Integer, Node> getMapOfPanelsInWizard() {
		return mapOfPanelsInWizard;
	}

	public WizardContainerController getWizardContainerController() {
		return wizardContainerController;
	}

	public void setWizardContainerController(WizardContainerController wizardContainerController) {
		this.wizardContainerController = wizardContainerController;
	}
	
	public void addChapter1Step1() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap1Step1.fxml"));
			Chap1Step1Controller chap1Step1Controller = new Chap1Step1Controller(wizardContainerController, this);
			fxmlLoader.setController(chap1Step1Controller);
			addPanel(fxmlLoader.load(), 1);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter1Step2() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap1Step2.fxml"));
			Chap1Step2Controller chap1Step2Controller = new Chap1Step2Controller(wizardContainerController, this);
			fxmlLoader.setController(chap1Step2Controller);
			addPanel(fxmlLoader.load(), 1);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter1Step3() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap1Step3.fxml"));
			Chap1Step3Controller chap1Step3Controller = new Chap1Step3Controller(wizardContainerController, this);
			fxmlLoader.setController(chap1Step3Controller);
			addPanel(fxmlLoader.load(), 1);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter1Step4() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap1Step4.fxml"));
			Chap1Step4Controller chap1Step4Controller = new Chap1Step4Controller(wizardContainerController, this);
			fxmlLoader.setController(chap1Step4Controller);
			addPanel(fxmlLoader.load(), 1);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter1Step5() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap1Step5.fxml"));
			Chap1Step5Controller chap1Step5Controller = new Chap1Step5Controller(wizardContainerController, this);
			fxmlLoader.setController(chap1Step5Controller);
			addPanel(fxmlLoader.load(), 1);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter2Step1() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap2Step1.fxml"));
			Chap2Step1Controller chap2Step1Controller = new Chap2Step1Controller(wizardContainerController, this);
			fxmlLoader.setController(chap2Step1Controller);
			addPanel(fxmlLoader.load(), 2);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter2Step2() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap2Step2.fxml"));
			Chap2Step2Controller chap2Step2Controller = new Chap2Step2Controller(wizardContainerController, this);
			fxmlLoader.setController(chap2Step2Controller);
			addPanel(fxmlLoader.load(), 2);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter2Step3() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap2Step3.fxml"));
			Chap2Step3Controller chap2Step3Controller = new Chap2Step3Controller(wizardContainerController, this);
			fxmlLoader.setController(chap2Step3Controller);
			addPanel(fxmlLoader.load(), 2);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter2Step4() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap2Step4.fxml"));
			Chap2Step4Controller chap2Step4Controller = new Chap2Step4Controller(wizardContainerController, this);
			fxmlLoader.setController(chap2Step4Controller);
			addPanel(fxmlLoader.load(), 2);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter2Step5() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap2Step5.fxml"));
			Chap2Step5Controller chap2Step5Controller = new Chap2Step5Controller(wizardContainerController, this);
			fxmlLoader.setController(chap2Step5Controller);
			addPanel(fxmlLoader.load(), 2);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter3Step1() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap3Step1.fxml"));
			Chap3Step1Controller chap3Step1Controller = new Chap3Step1Controller(wizardContainerController, this);
			fxmlLoader.setController(chap3Step1Controller);
			addPanel(fxmlLoader.load(), 3);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter3Step2() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap3Step2.fxml"));
			Chap3Step2Controller chap3Step2Controller = new Chap3Step2Controller(wizardContainerController, this);
			fxmlLoader.setController(chap3Step2Controller);
			addPanel(fxmlLoader.load(), 3);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addChapter3Step3() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap3Step3.fxml"));
			Chap3Step3Controller chap3Step3Controller = new Chap3Step3Controller(wizardContainerController, this);
			fxmlLoader.setController(chap3Step3Controller);
			addPanel(fxmlLoader.load(), 3);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter3Step4() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap3Step4.fxml"));
			Chap3Step4Controller chap3Step4Controller = new Chap3Step4Controller(wizardContainerController, this);
			fxmlLoader.setController(chap3Step4Controller);
			addPanel(fxmlLoader.load(), 3);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter3Step5() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap3Step5.fxml"));
			Chap3Step5Controller chap3Step5Controller = new Chap3Step5Controller(wizardContainerController, this);
			fxmlLoader.setController(chap3Step5Controller);
			addPanel(fxmlLoader.load(), 3);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter3Step6() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap3Step6.fxml"));
			Chap3Step6Controller chap3Step6Controller = new Chap3Step6Controller(wizardContainerController, this);
			fxmlLoader.setController(chap3Step6Controller);
			addPanel(fxmlLoader.load(), 3);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter4Step1() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap4Step1.fxml"));
			Chap4Step1Controller chap4Step1Controller = new Chap4Step1Controller(wizardContainerController, this);
			fxmlLoader.setController(chap4Step1Controller);
			addPanel(fxmlLoader.load(), 4);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter4Step2() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap4Step2.fxml"));
			Chap4Step2Controller chap4Step2Controller = new Chap4Step2Controller(wizardContainerController, this);
			fxmlLoader.setController(chap4Step2Controller);
			addPanel(fxmlLoader.load(), 4);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter4Step3() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap4Step3.fxml"));
			Chap4Step3Controller chap4Step3Controller = new Chap4Step3Controller(wizardContainerController, this);
			fxmlLoader.setController(chap4Step3Controller);
			addPanel(fxmlLoader.load(), 4);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter4Step4() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap4Step4.fxml"));
			Chap4Step4Controller chap4Step4Controller = new Chap4Step4Controller(wizardContainerController, this);
			fxmlLoader.setController(chap4Step4Controller);
			addPanel(fxmlLoader.load(), 4);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter5Step1() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap5Step1.fxml"));
			Chap5Step1Controller chap5Step1Controller = new Chap5Step1Controller(wizardContainerController, this);
			fxmlLoader.setController(chap5Step1Controller);
			addPanel(fxmlLoader.load(), 5);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter5Step2() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap5Step2.fxml"));
			Chap5Step2Controller chap5Step2Controller = new Chap5Step2Controller(wizardContainerController, this);
			fxmlLoader.setController(chap5Step2Controller);
			addPanel(fxmlLoader.load(), 5);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter5Step3() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap5Step3.fxml"));
			Chap5Step3Controller chap5Step3Controller = new Chap5Step3Controller(wizardContainerController, this);
			fxmlLoader.setController(chap5Step3Controller);
			addPanel(fxmlLoader.load(), 5);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter5Step4() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap5Step4.fxml"));
			Chap5Step4Controller chap5Step4Controller = new Chap5Step4Controller(wizardContainerController, this);
			fxmlLoader.setController(chap5Step4Controller);
			addPanel(fxmlLoader.load(), 5);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChapter5Step5() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chap5Step5.fxml"));
			Chap5Step5Controller chap5Step5Controller = new Chap5Step5Controller(wizardContainerController, this);
			fxmlLoader.setController(chap5Step5Controller);
			addPanel(fxmlLoader.load(), 5);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addFinal() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Final.fxml"));
			FinalController finalController = new FinalController();
			fxmlLoader.setController(finalController);
			addPanel(fxmlLoader.load(), -1);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
