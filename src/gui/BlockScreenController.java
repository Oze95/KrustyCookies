package gui;

import java.io.IOException;
import java.sql.SQLException;

import db.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BlockScreenController{
	
	@FXML
	private TextField nameField;
	
	@FXML
	private TextField timeField;
	
	@FXML 
	private Button backBtn;
	
	@FXML 
	private Text messageText;
	
	public void back() throws IOException {
		Stage stage = (Stage) backBtn.getScene().getWindow();
		Parent root = FXMLLoader.load(Main.class.getResource("welcomeScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
	}
	
	public void block() {
		try {
			Database.getInstance().blockPallet(timeField.getText(), nameField.getText());
			messageText.setText("Success!");
		} catch (SQLException e) {
			messageText.setText("Something went wrong :(");
			e.printStackTrace();
		}
	}
	

}
