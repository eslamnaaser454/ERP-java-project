package app.Index;

import app.Classes.Authentication;
import app.Classes.DataBaseConnection;
import app.Login.LoginApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
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


public class HrIndexController implements Initializable {

    private Authentication authentication;

    @FXML
    private Label First;

    @FXML
    private Label First_count;

    @FXML
    private ImageView First_ico;

    @FXML
    private Label Fourth;

    @FXML
    private Label Fourth_cont;

    @FXML
    private ImageView Fourth_ico;

    @FXML
    private Label Second;

    @FXML
    private Label Second_count;

    @FXML
    private ImageView Second_ico;

    @FXML
    private Label Third;

    @FXML
    private Label Third_cont;

    @FXML
    private ImageView Third_ico;
    private DataBaseConnection dataBaseConnection;

    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";


    private void updateEmployeeCount() {
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String, String>> result = dataBaseConnection.select("SELECT COUNT(*) AS hr_count FROM users WHERE type = 'HR'");

        if (!result.isEmpty()) {
            String countStr = result.getFirst().get("hr_count");
            int count = Integer.parseInt(countStr);
            System.out.println("HR count = " + count);
            First_count.setText(String.valueOf(count));
        }
    }
    private void updatesale() {
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String, String>> result = dataBaseConnection.select("SELECT COUNT(*) AS Employees FROM staff ");
        if (result != null) {
            String countStr = result.getFirst().get("Employees");
            int count = Integer.parseInt(countStr);
            System.out.println("Employees count = " + count);
            Second_count.setText(String.valueOf(count));
        }
    }
    private void updatestock() {
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String, String>> result = dataBaseConnection.select("SELECT COUNT(*) AS Department1 FROM department ");
        if (result != null) {
            String countStr = result.getFirst().get("Department1");
            int count = Integer.parseInt(countStr);
            System.out.println("Department count = " + count);
            Third_cont.setText(String.valueOf(count));        }
    }
    private void updateproducts() {
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String, String>> result = dataBaseConnection.select("SELECT COUNT(*) AS Users FROM users WHERE type != 'HR'");
        if (result != null) {
            String countStr = result.getFirst().get("Users");
            int count = Integer.parseInt(countStr);
            System.out.println("Users count = " + count);
            Fourth_cont.setText(String.valueOf(count));
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