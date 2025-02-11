package gov.epa.equitableresiliencebuilder.indicators;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import gov.epa.equitableresiliencebuilder.App;
import gov.epa.equitableresiliencebuilder.utility.FileHandler;

public class IndicatorSaveDataParser {
	
	private Logger logger;
	private FileHandler fileHandler;

	private App app;
	public IndicatorSaveDataParser(App app) {
		this.app = app;
		
		logger = app.getLogger();
		fileHandler = new FileHandler(app);
	}

	public ArrayList<String> getSavedSelectedIndicatorIds_InPerson() {
		File inPersonCardSelectionFile = new File(getIndicatorsDir() + File.separator + "CardSelection_InPerson.txt");
		if (inPersonCardSelectionFile.exists()) {
			ArrayList<String> indicatorIds = new ArrayList<String>();
			try {
				Scanner scanner = new Scanner(inPersonCardSelectionFile);
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					indicatorIds.add(line.trim());
				}
				scanner.close();
				return indicatorIds;
			} catch (FileNotFoundException e) {
				logger.log(Level.SEVERE, "Failed to save indicator ids: " + e.getMessage());
			}
		}
		return null;
	}

	public ArrayList<IndicatorCard> getSavedSelectedIndicatorCards_InPerson() {
		File indicatorWorkbookFile = new File(fileHandler.getSupportingDOCDirectory(app.getSelectedProject(),
				app.getEngagementActionController().getCurrentGoal()) + File.separator + "Indicators_List.xlsx");
		IndicatorWorkbookParser iWP = new IndicatorWorkbookParser(app,indicatorWorkbookFile);
		ArrayList<IndicatorCard> cards = iWP.parseForIndicatorCards();

		ArrayList<String> ids = getSavedSelectedIndicatorIds_InPerson();
		ArrayList<IndicatorCard> savedCards = new ArrayList<IndicatorCard>();

		for (IndicatorCard iC : cards) {
			if (ids.contains(iC.getId())) {
				savedCards.add(iC);
			}
		}
		return savedCards;
	}

	public ArrayList<String> getSavedSelectedData_InPerson() {
		File inPersonDataSelectionFile = new File(getIndicatorsDir() + File.separator + "DataSelection_InPerson.txt");
		if (inPersonDataSelectionFile.exists()) {
			ArrayList<String> data = new ArrayList<String>();
			try {
				Scanner scanner = new Scanner(inPersonDataSelectionFile);
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					data.add(line.trim());
				}
				scanner.close();
				return data;
			} catch (FileNotFoundException e) {
				logger.log(Level.SEVERE, "Failed to get saved data: " + e.getMessage());
			}
		}
		return null;
	}

	public ArrayList<String> getSavedSelectedIndicatorIds_Virtual() {
		File virtualCardSelectionFile = new File(getIndicatorsDir() + File.separator + "CardSelection_Virtual.txt");
		if (virtualCardSelectionFile.exists()) {
			ArrayList<String> indicatorIds = new ArrayList<String>();
			try {
				Scanner scanner = new Scanner(virtualCardSelectionFile);
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					indicatorIds.add(line.trim());
				}
				scanner.close();
				return indicatorIds;
			} catch (FileNotFoundException e) {
				logger.log(Level.SEVERE, "Failed to get saved indicator ids: " + e.getMessage());
			}
		}
		return null;
	}

	public ArrayList<IndicatorCard> getSavedSelectedIndicatorCards_Virtual() {
		File indicatorWorkbookFile = new File(fileHandler.getSupportingDOCDirectory(app.getSelectedProject(),
				app.getEngagementActionController().getCurrentGoal()) + File.separator + "Indicators_List.xlsx");
		IndicatorWorkbookParser iWP = new IndicatorWorkbookParser(app,indicatorWorkbookFile);
		ArrayList<IndicatorCard> cards = iWP.parseForIndicatorCards();

		ArrayList<String> ids = getSavedSelectedIndicatorIds_Virtual();
		ArrayList<IndicatorCard> savedCards = new ArrayList<IndicatorCard>();

		for (IndicatorCard iC : cards) {
			if (ids.contains(iC.getId())) {
				savedCards.add(iC);
			}
		}
		return savedCards;
	}
	
	private File getIndicatorsDir() {
		return fileHandler.getIndicatorsDirectory(app.getSelectedProject(),app.getEngagementActionController().getCurrentGoal());
	}
}
