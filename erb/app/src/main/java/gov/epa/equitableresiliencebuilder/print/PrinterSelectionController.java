package gov.epa.equitableresiliencebuilder.print;

import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import java.net.URL;
import java.util.ResourceBundle;

import gov.epa.equitableresiliencebuilder.indicators.DataSelection_InPersonController;
import gov.epa.equitableresiliencebuilder.indicators.IndicatorsPrintViewController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class PrinterSelectionController implements Initializable{

	private IndicatorsPrintViewController iPWC;
	private DataSelection_InPersonController dSIPC;
	public PrinterSelectionController(IndicatorsPrintViewController iPWC, DataSelection_InPersonController dSIPC) {
		this.iPWC = iPWC;
		this.dSIPC = dSIPC;
	}
	
	@FXML
	Label label;
	@FXML
	ListView<Printer> printerListView;
			
	public void fillPrinterListView() {
		for(Printer printer: Printer.getAllPrinters()) {
			printerListView.getItems().add(printer);
		}
	}
	
	private void setListViewCellFactory() {
		Callback<ListView<Printer>, ListCell<Printer>> cellFactory = new Callback<ListView<Printer>, ListCell<Printer>>() {
		    @Override
		    public ListCell<Printer> call(ListView<Printer> l) {
		        return new ListCell<Printer>() {

		            @Override
		            protected void updateItem(Printer item, boolean empty) {
		                super.updateItem(item, empty);
		                if (item == null || empty) {
		                    setGraphic(null);
		                } else {
		                    setText(item.getName());
		                }
		            }
		        } ;
		    }
		};
		printerListView.setCellFactory(cellFactory);

	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setListViewCellFactory();
		fillPrinterListView();
		setLabelText();
	}
	
	private void setLabelText() {
		label.setText(iPWC.getListOfCardHBoxes().size() + " pages to be printed.");
	}
	
	@FXML
	public void okButtonAction() {
		Printer selectedPrinter = printerListView.getSelectionModel().getSelectedItem();
		if(selectedPrinter!= null) {
			for(HBox cardHBox : iPWC.getListOfCardHBoxes()) {			
				PageLayout pageLayout = selectedPrinter.createPageLayout(Paper.NA_LETTER, PageOrientation.LANDSCAPE, 5, 5, 5, 5);
				PrinterJob job = PrinterJob.createPrinterJob();
				job.setPrinter(selectedPrinter);
				job.getJobSettings().setPageLayout(pageLayout);
				if(job != null) {
					boolean success = job.printPage(cardHBox);
					if(success) {
						job.endJob();
					}
				}	
			}
		}
		dSIPC.getPrinterSelectionStage().close();
		showPrintedAlert();
	}
	
	private void showPrintedAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText("Printing is complete!");
		alert.setTitle("Printing");
		alert.showAndWait();
	}

}
