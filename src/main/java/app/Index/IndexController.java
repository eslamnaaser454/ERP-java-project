package app.Index;

import app.Classes.Authentication;
import app.Classes.DataBaseConnection;
import app.Login.LoginApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import app.Stores.Index.StoreIndexApplication;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.control.Label;


public class IndexController implements Initializable {

    private Authentication authentication;
    @FXML
    private HBox sideBar;
    @FXML
    private Label empCount;
    @FXML
    private Label salescont;
    @FXML
    private Label stockscont;
    @FXML
    private Label productscont;

    private DataBaseConnection dataBaseConnection;

    private void updateEmployeeCount() {
        dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
        List<Map<String, String>> result = dataBaseConnection.select("SELECT * FROM staff;");
        if (result != null) {
            empCount.setText(String.valueOf(result.size()));
        }
    }
    private void updatesale() {
        dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
        List<Map<String, String>> result = dataBaseConnection.select("SELECT * FROM sale;");
        if (result != null) {
            salescont.setText(String.valueOf(result.size()));
        }
    }
    private void updatestock() {
        dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
        List<Map<String, String>> result = dataBaseConnection.select("SELECT * FROM stock;");
        if (result != null) {
            stockscont.setText(String.valueOf(result.size()));
        }
    }
    private void updateproducts() {
        dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
        List<Map<String, String>> result = dataBaseConnection.select("SELECT * FROM supply;");
        if (result != null) {
            productscont.setText(String.valueOf(result.size()));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateEmployeeCount();
        updatesale();
        updatestock();
        updateproducts();

    }


  
}