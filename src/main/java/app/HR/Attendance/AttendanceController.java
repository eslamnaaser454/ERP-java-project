package app.HR.Attendance;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import app.Classes.DataBaseConnection;
import app.Stores.Manage.Create.CreateStoreFormApplication;
import app.Stores.Manage.Edite.EditeStoreFormApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;


public class AttendanceController implements Initializable {
@FXML
    private String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
    private String TableName = "Attendance";


    @FXML
    private TableView<Attendance> table;
    @FXML
    private TableColumn<Attendance, String> idCol;
    @FXML
    private TableColumn<Attendance, String> fullnamecol;
    @FXML
    TableColumn<Attendance, HBox> AttendanceRateCol;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button refreshBtn;
    @FXML
    private Button createBtn;


    public AttendanceController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public class Attendance{

    }



    public void initialize() {
    }

    public void GotoaddForm(ActionEvent actionEvent) {
    }

    public void refreshTable(ActionEvent actionEvent) {
    }

    public void search(ActionEvent actionEvent) {
    }

    public void GotoCreateForm(ActionEvent actionEvent) {
    }

}