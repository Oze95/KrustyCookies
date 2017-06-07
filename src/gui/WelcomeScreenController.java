package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class WelcomeScreenController implements Initializable {
	@FXML
	private Button blockMenuBtn;
	@FXML
	private Button searchMenuBtn;
	@FXML
	private Button produceMenuBtn;
	
	public void toBlockScreen(ActionEvent event) throws IOException {
		Stage stage = (Stage) blockMenuBtn.getScene().getWindow();
		Parent root = FXMLLoader.load(Main.class.getResource("blockScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
	}
	
	public void toSearchScreen() throws IOException {
		Stage stage = (Stage) blockMenuBtn.getScene().getWindow();
		Parent root = FXMLLoader.load(Main.class.getResource("searchScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
	}
	
	public void toProduceScreen() throws IOException {
		Stage stage = (Stage) produceMenuBtn.getScene().getWindow();
		Parent root = FXMLLoader.load(Main.class.getResource("produceScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}
