package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import db.Database;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SearchScreenController implements Initializable{
	@FXML
	private TextField enterField1;
	@FXML
	private TextField enterField2;
	@FXML
	private Text productionDateText;
	@FXML
	private Text cookieNameText;
	@FXML
	private Text deliveredText;
	@FXML
	private Text locationText;
	@FXML
	private Text blockedText;
	@FXML
	private Button backBtn;
	@FXML
	private Text messageText;
	@FXML
	private RadioButton searchIDRadio;
	@FXML
	private RadioButton searchCookieRadio;
	@FXML
	private RadioButton searchTimeRadio;
	@FXML
	private ListView<String> resultList = new ListView<String>();

	//States of radio buttons
	private final static int ID_RADIO = 0;
	private final static int TIME_RADIO = 1;
	private final static int COOKIE_RADIO = 2;
	private int searchToggle = 0;
	final ToggleGroup searchGroup = new ToggleGroup();


	public void searchByID() {
		enterField1.setText("");
		searchToggle = ID_RADIO;
		searchIDRadio.setSelected(true);
		enterField2.setDisable(true);
	}

	public void searchByTime() {
		enterField1.setText("");
		searchToggle = TIME_RADIO;
		searchTimeRadio.setSelected(true);
		enterField2.setDisable(false);
	}


	public void searchByCookie() {
		enterField1.setText("");
		searchToggle = COOKIE_RADIO;
		searchCookieRadio.setSelected(true);
		enterField2.setDisable(true);
	}

	public void search() {
		resultList.getSelectionModel().clearSelection();
		List<String> palletStrings;
		ObservableList<String> cookieItems;
		try {
			switch(searchToggle) {

			case COOKIE_RADIO:
				palletStrings = Database.getInstance().getPallets(enterField1.getText());
				cookieItems = FXCollections.observableArrayList(palletStrings);
				resultList.setItems(cookieItems);
				break;
			case ID_RADIO: 
				palletStrings = Database.getInstance().getPallet(Integer.parseInt(enterField1.getText()));
				setText(palletStrings);
				break;
			case TIME_RADIO: 
				palletStrings = Database.getInstance().getPallets(enterField1.getText(), enterField2.getText());
				cookieItems = FXCollections.observableArrayList(palletStrings);
				resultList.setItems(cookieItems);
				break;
			}
		} catch (NumberFormatException | SQLException e) {
			messageText.setText("Something went wrong :(");
			e.printStackTrace();
		}
	}

	public void back() throws IOException {
		Stage stage = (Stage) backBtn.getScene().getWindow();
		Parent root = FXMLLoader.load(Main.class.getResource("welcomeScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		searchIDRadio.setToggleGroup(searchGroup);
		searchTimeRadio.setToggleGroup(searchGroup);
		searchCookieRadio.setToggleGroup(searchGroup);
		searchByID();
		setList();
	}

	private void setText(List<String> palletStrings) {
		cookieNameText.setText(palletStrings.get(0));
		deliveredText.setText(palletStrings.get(1));
		productionDateText.setText(palletStrings.get(2));
		locationText.setText(palletStrings.get(3));
		blockedText.setText(palletStrings.get(4));
	}

	private void setList() {
		resultList.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<String>() {
					public void changed(ObservableValue<? extends String> ov, 
							String old_val, String new_val) {
						List<String> palletStrings;
						try {
							if (!(new_val == null)) {
								palletStrings = Database.getInstance().getPallet(Integer.parseInt(new_val));
								setText(palletStrings);

							}
						} catch (NumberFormatException | SQLException e) {
							messageText.setText("Something went wrong :(");
							e.printStackTrace();
						}
					}
				});
	}
}
