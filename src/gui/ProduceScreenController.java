package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import db.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ProduceScreenController implements Initializable{
	@FXML
	private ListView<String> cookiesList;
	@FXML
	private Button backBtn, produceBtn;
	@FXML
	private TextField amountField;
	@FXML
	private Text messageText;
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			fillCookies();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void fillCookies() throws SQLException {
		ObservableList<String> cookiesItems = FXCollections.observableArrayList (Database.getInstance().getAllCookies());
		cookiesList.setItems(cookiesItems);
	}
	
	public void back() throws IOException {
		Stage stage = (Stage) backBtn.getScene().getWindow();
		Parent root = FXMLLoader.load(Main.class.getResource("welcomeScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
	}
	
	public void produce() {
		try {
			Database.getInstance().produceCookies(cookiesList.getSelectionModel().getSelectedItem());
			messageText.setText("Success!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
}
