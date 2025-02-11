package gov.epa.equitableresiliencebuilder.indicators;

import java.util.ArrayList;

public class IndicatorCard {
	
	private final String socialEnvironmentSystemColor = "#A6D0E2"; //Blue
	private final String builtEnvironmentSystemColor = "#F0C485"; //Orange
	private final String naturalEnvironmentSystemColor = "#CBE2A6"; //Green
	
	private String id;
	private String system;
	private String indicator;
	private String definition;
	private String dataPoints;
	private String thresholds;
	private String equityValue;
	private String localConcern;
	private String resilienceValue;
	private String dataCollection_y_n;
	private String additionalInformation;
	private String rawDataCollectionNotes;
	private String quantitativeDataSources;
	private ArrayList<String> dataQuestionsToAnswer;
	private String qualitativeDataCollectionProcess;
	private String quantitativeDataCollectionProcess;
	
	public IndicatorCard(String id, String s, String i, String d, String rV, String eV, String lC, ArrayList<String> q, String qnDS, String qnDC, String qlDC, String aI, String rdColl, String dP, String t, String dataCollection_y_n ) {
		this.id = id;
		this.system = s;
		this.indicator = i;
		this.definition = d;
		this.thresholds = t;
		this.dataPoints = dP;
		this.equityValue = eV;
		this.localConcern =lC;
		this.resilienceValue = rV;
		this.dataQuestionsToAnswer = q;
		this.additionalInformation = aI;
		this.quantitativeDataSources = qnDS;
		this.rawDataCollectionNotes = rdColl;
		this.quantitativeDataCollectionProcess = qnDC;
		this.qualitativeDataCollectionProcess = qlDC;
		this.dataCollection_y_n = dataCollection_y_n;
	}

	public String getSocialEnvironmentSystemColor() {
		return socialEnvironmentSystemColor;
	}
	public String getBuiltEnvironmentSystemColor() {
		return builtEnvironmentSystemColor;
	}
	public String getNaturalEnvironmentSystemColor() {
		return naturalEnvironmentSystemColor;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getIndicator() {
		return indicator;
	}
	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public String getResilienceValue() {
		return resilienceValue;
	}
	public void setResilienceValue(String resilienceValue) {
		this.resilienceValue = resilienceValue;
	}
	public String getEquityValue() {
		return equityValue;
	}
	public void setEquityValue(String equityValue) {
		this.equityValue = equityValue;
	}
	public String getLocalConcern() {
		return localConcern;
	}
	public void setLocalConcern(String localConcern) {
		this.localConcern = localConcern;
	}
	public String getQuantitativeDataSources() {
		return quantitativeDataSources;
	}
	public void setQuantitativeDataSources(String quantitativeDataSources) {
		this.quantitativeDataSources = quantitativeDataSources;
	}
	public String getQuantitativeDataCollProcess() {
		return quantitativeDataCollectionProcess;
	}
	public void setQuantitativeDataCollProcess(String quantitativeDataCollProcess) {
		this.quantitativeDataCollectionProcess = quantitativeDataCollProcess;
	}
	public String getQualitativeDataCollProcess() {
		return qualitativeDataCollectionProcess;
	}
	public void setQualitativeDataCollProcess(String qualitativeDataCollProcess) {
		this.qualitativeDataCollectionProcess = qualitativeDataCollProcess;
	}
	public String getAdditionalInformation() {
		return additionalInformation;
	}
	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
	public String getRawDataCollectionNotes() {
		return rawDataCollectionNotes;
	}
	public void setRawDataCollectionNotes(String rawDataCollectionNotes) {
		this.rawDataCollectionNotes = rawDataCollectionNotes;
	}
	public String getDataPoints() {
		return dataPoints;
	}
	public void setDataPoints(String dataPoints) {
		this.dataPoints = dataPoints;
	}
	public String getThresholds() {
		return thresholds;
	}
	public void setThresholds(String thresholds) {
		this.thresholds = thresholds;
	}
	public ArrayList<String> getQuestionsToAnswer() {
		return dataQuestionsToAnswer;
	}
	public void setQuestionsToAnswer(ArrayList<String> questionsToAnswer) {
		this.dataQuestionsToAnswer = questionsToAnswer;
	}
	public ArrayList<String> getDataQuestionsToAnswer() {
		return dataQuestionsToAnswer;
	}
	public void setDataQuestionsToAnswer(ArrayList<String> dataQuestionsToAnswer) {
		this.dataQuestionsToAnswer = dataQuestionsToAnswer;
	}
	public String getQuantitativeDataCollectionProcess() {
		return quantitativeDataCollectionProcess;
	}
	public void setQuantitativeDataCollectionProcess(String quantitativeDataCollectionProcess) {
		this.quantitativeDataCollectionProcess = quantitativeDataCollectionProcess;
	}
	public String getQualitativeDataCollectionProcess() {
		return qualitativeDataCollectionProcess;
	}
	public void setQualitativeDataCollectionProcess(String qualitativeDataCollectionProcess) {
		this.qualitativeDataCollectionProcess = qualitativeDataCollectionProcess;
	}
	public String getDataCollection_y_n() {
		return dataCollection_y_n;
	}
	public void setDataCollection_y_n(String dataCollection_y_n) {
		this.dataCollection_y_n = dataCollection_y_n;
	}
	public String getDataQuestionsToAnswerAsString() {
		StringBuilder stringBuilder = new StringBuilder();
		for(String q: dataQuestionsToAnswer) {
			stringBuilder.append(q + "\n");
		}
		return stringBuilder.toString();
	}
}
