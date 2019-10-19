import javafx.fxml.FXML;
import java.net.URL;
import java.util.*;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.*;
import java.io.File;
import javafx.event.*;

public class EventController implements Initializable {
	@FXML
	private Label filenameLabel;
	@FXML
	private Button browseButton;
	@FXML
	private TitledPane pane;
	@FXML
	private ChoiceBox<String> box1;
	@FXML
	private ChoiceBox<String> box2;
	@FXML
	private ChoiceBox<String> box3;
	@FXML
	private ChoiceBox<String> methodBox;
	@FXML
	private Label targetLabel;
	@FXML
	private Label relatedLabel;

	private String csvFilename;

	private List<ChoiceBox<String>> box = new ArrayList<ChoiceBox<String>>();

	private ArrayList<String> target = new ArrayList<String>();
	private ArrayList<String> related = new ArrayList<String>();
	private String method;

	private ArrayList<CheckBox> checkboxes = new ArrayList<CheckBox>();

	HeaderReader headerReader;
	Configurator configurator;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pane.setExpanded(false);
		box.add(box1);
		box.add(box2);
		box.add(box3);

		for(ChoiceBox<String> b : box) {
			b.getItems().clear();
		}
		methodBox.getItems().clear();
		methodBox.getItems().add("rootsquare");
		methodBox.getItems().add("polynomial");
		methodBox.getItems().add("locationmap");
		methodBox.getItems().add("others");
	}

	public void browse() {
		FileChooser chooser = new FileChooser();
		//chooser.setInitialDirectory(new File(System.getProperty("user.home")));                 
		chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));
		chooser.setTitle("Choose a CSV File");
		File csvFile = chooser.showOpenDialog(new Stage());
		if(csvFile != null) {
			System.out.println(csvFile.getName());
			csvFilename = csvFile.getName();
			filenameLabel.setText(csvFilename);
			pane.setText(csvFilename);
			pane.setStyle("-fx-text-fill: green;");
			headerReader = new HeaderReader(csvFile);
			updateChoiceBox();
		} else {
			System.out.println("null");
		}
	}

	void updateChoiceBox() {
		for(ChoiceBox<String> b : box) {
			b.getItems().clear();
			for(String optionValue : headerReader.getHeaders()) {
				b.getItems().add(optionValue);
			}
		}
	}

	public void selectTarget() {
		checkboxes.clear();
		try {
			Stage stage = new Stage();
			stage.setTitle("Select Target Headers");
			GridPane gt = new GridPane();
			for(int i = 0; i < headerReader.getHeaders().size(); i++) {
				CheckBox cb = new CheckBox(headerReader.getHeaders().get(i));
				cb.setStyle("-fx-label-padding: 8 8 8 8;");
				checkboxes.add(cb);
				gt.add(cb, (i < headerReader.getHeaders().size()/2)? 0 : 1,
				(i < headerReader.getHeaders().size()/2)? i : headerReader.getHeaders().size() - 1 - i);
			}
			Button button = new Button("Finish");
			gt.add(button, 0, headerReader.getHeaders().size());
			gt.setAlignment(Pos.CENTER);
			Scene scene = new Scene(gt, 450, 20 * headerReader.getHeaders().size() +50);
			stage.setScene(scene);
			button.setOnAction((ActionEvent e) -> {
				String targetString = "";
				target.clear();
				for(CheckBox cb : checkboxes) {
					if(cb.isSelected()) {
						target.add(cb.getText());
						targetString += "[" + cb.getText() + "]" + "\n";
						System.out.println(cb.getText());
					}
				}
				targetLabel.setText(targetString);
				stage.close();
			});
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void selectRelated() {
		checkboxes.clear();
		try {
			Stage stage = new Stage();
			stage.setTitle("Select Related Headers");
			GridPane gr = new GridPane();
			for(int i = 0; i < headerReader.getHeaders().size(); i++) {
				CheckBox cb = new CheckBox(headerReader.getHeaders().get(i));
				cb.setStyle("-fx-label-padding: 8 8 8 8;");
				checkboxes.add(cb);
				gr.add(cb, (i < headerReader.getHeaders().size()/2)? 0 : 1,
				(i < headerReader.getHeaders().size()/2)? i : headerReader.getHeaders().size() - 1 - i);
			}
			Button button = new Button("Finish");
			gr.add(button, 0, headerReader.getHeaders().size());
			gr.setAlignment(Pos.CENTER);
			Scene scene = new Scene(gr, 450, 20 * headerReader.getHeaders().size() + 50);
			stage.setScene(scene);
			button.setOnAction((ActionEvent e) -> {
				String relatedString = "";
				related.clear();
				for(CheckBox cb : checkboxes) {
					if(cb.isSelected()) {
						related.add(cb.getText());
						relatedString += "[" + cb.getText() + "]" + "\n";
						System.out.println(cb.getText());
					}
				}
				relatedLabel.setText(relatedString);
				stage.close();
			});
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void startSweeping() {
		//lat lon time
		if(csvFilename == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Form Error");
			alert.setHeaderText(null);
			alert.setContentText("Plese select a CSV file!");
			alert.initOwner(pane.getScene().getWindow());
			alert.show();
            return;
		} else if(target.size() == 0 ^ related.size() == 0) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Form Error");
			alert.setHeaderText(null);
			alert.setContentText("Target headers or related haders are lost!");
			alert.initOwner(pane.getScene().getWindow());
			alert.show();
            return;
		}
		configurator = new Configurator();
		String value1 = (String)box1.getValue();
		System.out.println(value1);
		configurator.setLatitude(value1);
		String value2 = (String)box2.getValue();
		System.out.println(value2);
		configurator.setLongitude(value2);
		String value3 = (String)box3.getValue();
		System.out.println(value3);
		configurator.setTime(value3);
		method = (String)methodBox.getValue(); 
		configurator.setRelatedData(target, related, method);
		configurator.generateJsonFile();
		/**
		 * run python code here
		 */
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Info");
		alert.setHeaderText(null);
		alert.setContentText("Sweep Successfully!");
		alert.initOwner(pane.getScene().getWindow());
		alert.show();
    	return;
	}
}