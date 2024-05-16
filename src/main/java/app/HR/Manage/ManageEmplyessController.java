package app.HR.Manage;

import app.Classes.ExcelSheet;
import app.Classes.Logging;
import app.HR.Manage.Edite.EditeEmployessApplication;
import app.HR.Manage.add.AddEmployessApplication;
import app.Stores.Manage.StoreManageController;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageEmplyessController implements Initializable {
    private String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
    private String TableName = "Employees";
    DataBaseConnection dataBaseConnection;

    @FXML
    public void GotoaddForm() {
        Stage stage = new Stage();
        stage.setResizable(false);
        AddEmployessApplication nextPage = new AddEmployessApplication();
        try {
            nextPage.start(stage);
        } catch (IOException e) {
            System.out.println("Error At going to Add supplier Page");
        }
    }


    @FXML
    private TableView<hr> table;


    @FXML
    private TableColumn<hr, String> idCol;

    @FXML
    private TableColumn<hr, String> firstNameCol;


    @FXML
    private TableColumn<hr, String> lastNameCol;

    @FXML
    private Button refreshBtn;
    @FXML
    private Button addBtn;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchBtn;
    @FXML
    private TableColumn<hr, String> emailCol;
    @FXML
    private TableColumn<hr, String> phoneCol;
    @FXML
    private TableColumn<hr, String> addressCol;
    @FXML
    private TableColumn<hr, String> departmentCol;
    @FXML
    private TableColumn<hr, String> salaryCol;
    @FXML
    private TableColumn<hr, String> genderCol;
    @FXML
    private TableColumn<hr, String> ssnCol;

    @FXML
    TableColumn<hr, HBox> actionCol;


    public void initialize() {
        NumberBinding tableWidth = table.widthProperty().subtract(2);
        idCol.prefWidthProperty().bind(tableWidth.multiply(0.1));
        firstNameCol.prefWidthProperty().bind(tableWidth.multiply(0.1));
        lastNameCol.prefWidthProperty().bind(tableWidth.multiply(0.1));

    }

    @FXML
    public void refreshTable() {
        table.getItems().clear();
        table.setItems(hrObservableList());
    }

    @FXML
    public void search() {

        String value = (String) searchField.getText();


        table.getItems().clear();
        table.setItems(hrObservableList(value));
    }

    public void Report(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        // Set the title for the directory chooser dialog
        directoryChooser.setTitle("Choose Directory");
        // Set the initial directory (optional)
        // directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if (selectedDirectory != null) {
            // Do something with the chosen directory
            String chosenDirectoryPath = selectedDirectory.getAbsolutePath();
            Date date = new Date();

//            System.out.println("Chosen directory: " + chosenDirectoryPath+"\\Invoice-Report-("+date.toString()+").xlsx");
            ExcelSheet excelSheet = new ExcelSheet(chosenDirectoryPath,"Staff-report");

            dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
            List<Map<String,String>> list = dataBaseConnection.select("select * from staff;");
            List<Map<Integer,String>> list1 = new ArrayList<>();
            Map<Integer,String> header = new HashMap<>();
            header.put(0,"ID");
            header.put(1,"First Name");
            header.put(2,"Last Name");
            header.put(3,"Phone");
            header.put(4,"Email");
            header.put(5,"Salary");
            header.put(6,"SSN");
            header.put(7,"Department");
            header.put(8,"Gender");
            header.put(9,"Address");
            list1.add(header);
            for (Map<String,String> map:list){
                Map<Integer,String> staff = new HashMap<>();
//                observableList.add(new ManageEmplyessController.hr(map.get("id"), map.get("first_name"), map.get("last_name"), map.get("phone"), map.get("email"), map.get("address"), map.get("department_id"), map.get("salary"), map.get("mail"), map.get("ssn"), this));

                staff.put(0, map.get("id"));
                staff.put(1, map.get("first_name"));
                staff.put(2, map.get("last_name"));
                staff.put(3, map.get("phone"));
                staff.put(4, map.get("email"));
                staff.put(5, map.get("salary"));
                staff.put(6, map.get("ssn"));
                List<Map<String,String>> departmentGet = dataBaseConnection.select("select * from department where id = "+map.get("department_id")+";");
                if (!departmentGet.isEmpty() || departmentGet != null){
                    staff.put(7, departmentGet.getFirst().get("name"));
                }else{
                    staff.put(7, "None");

                }

                if (map.get("mail").equals("1") || map.get("mail").equals("true")){
                    staff.put(8, "male");

                }else{
                    staff.put(8, "female");
                }
                staff.put(9, map.get("address"));


                list1.add(staff);
            }
            try {
                excelSheet.write(list1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        } else {
            // User canceled the directory chooser dialog
            System.out.println("No directory selected.");
        }
    }




    public static class hr {

        public String idCol;
        public String firstNameCol;
        public String lastNameCol;
        public String emailCol;
        public String phoneCol;
        public String addressCol;
        public String departmentCol;
        public String salaryCol;
        public String genderCol;
        public String ssnCol;
        public Button editBtn;
        public Button deleteBtn;

        public HBox actions;

        private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";

        public String getIdCol() {
            return idCol;
        }

        public void setIdCol(String idCol) {
            this.idCol = idCol;
        }

        public String getFirstNameCol() {
            return firstNameCol;
        }

        public void setFirstNameCol(String firstNameCol) {
            this.firstNameCol = firstNameCol;
        }

        public String getLastNameCol() {
            return lastNameCol;
        }

        public void setLastNameCol(String lastNameCol) {
            this.lastNameCol = lastNameCol;
        }

        public String getEmailCol() {
            return emailCol;
        }

        public void setEmailCol(String emailCol) {
            this.emailCol = emailCol;
        }

        public String getPhoneCol() {
            return phoneCol;
        }

        public void setPhoneCol(String phoneCol) {
            this.phoneCol = phoneCol;
        }

        public String getAddressCol() {
            return addressCol;
        }

        public void setAddressCol(String addressCol) {
            this.addressCol = addressCol;
        }

        public String getDepartmentCol() {
            return departmentCol;
        }

        public void setDepartmentCol(String departmentCol) {
            this.departmentCol = departmentCol;
        }

        public String getSalaryCol() {
            return salaryCol;
        }

        public void setSalaryCol(String salaryCol) {
            this.salaryCol = salaryCol;
        }

        public String getGenderCol() {
            return genderCol;
        }

        public void setGenderCol(String genderCol) {
            this.genderCol = genderCol;
        }

        public String getSsnCol() {
            return ssnCol;
        }

        public void setSsnCol(String ssnCol) {
            this.ssnCol = ssnCol;
        }

        public String getDbPath() {
            return dbPath;
        }

        public HBox getActions() {
            return actions;
        }

        private ManageEmplyessController ManageEmplyessController;

        public hr(String idCol, String firstNameCol, String lastNameCol, String emailCol, String phoneCol, String addressCol, String departmentCol, String salaryCol, String genderCol, String ssnCol, ManageEmplyessController manageEmplyessController) {
            this.idCol = idCol;
            this.firstNameCol = firstNameCol;
            this.lastNameCol = lastNameCol;
            this.emailCol = emailCol;
            this.phoneCol = phoneCol;
            this.addressCol = addressCol;
            this.departmentCol = departmentCol;
            this.salaryCol = salaryCol;
            this.genderCol = genderCol;
            this.ssnCol = ssnCol;


            deleteBtn = new Button("Delete");
            deleteBtn.setBackground(Background.fill(Paint.valueOf("red")));
            deleteBtn.setTextFill(Paint.valueOf("white"));
            deleteBtn.setStyle("-fx-background-color: red; -fx-background-radius: 15;");
            deleteBtn.setUserData(idCol);

            deleteBtn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
                    ButtonType okButton = new ButtonType("ok");
                    ButtonType cancleButton = new ButtonType("Cancel");

                    Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete that Employees", okButton, cancleButton);
                    alert.setHeaderText(null);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == okButton) {
                        String query = "delete from staff where id=" + idCol + ";";
                        System.out.println(query);
                        dataBaseConnection.excute(query);
                        ManageEmplyessController.refreshTable();
                        Logging logging = new Logging() ;
                        logging.addLog("Employee with name "+firstNameCol+" "+lastNameCol+" has been deleted");


                    }

                }
            });


            this.ManageEmplyessController = manageEmplyessController;
            editBtn = new Button("Edite");
            editBtn.setBackground(Background.fill(Paint.valueOf("blue")));
            editBtn.setTextFill(Paint.valueOf("white"));
            editBtn.setStyle("-fx-background-color: blue; -fx-background-radius: 15;");            editBtn.setUserData(idCol);
            actions = new HBox();
            actions.setSpacing(40);
            actions.setPrefHeight(20);
            actions.setAlignment(javafx.geometry.Pos.CENTER);
            actions.getChildren().addAll(editBtn, deleteBtn);
            editBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Stage stage = new Stage();
                    stage.setResizable(false);
                    EditeEmployessApplication nextPage = new EditeEmployessApplication(idCol);
                    nextPage.setManageEmplyessController(ManageEmplyessController);
                    try {
                        nextPage.start(stage);
                    } catch (IOException e) {
                        System.out.println("Error At going to Edite Employees Page");
                    }
                }
            });
            if (genderCol.equals("true") || genderCol.equals("1")) {
                this.genderCol = "male";

            } else {
                this.genderCol = "female";

            }
            DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
            List<Map<String, String>> list = dataBaseConnection.select("select * from department where id=" + departmentCol + ";");
            this.departmentCol = list.getFirst().get("name");


        }
    }

    @FXML
    private void refreshTable(ActionEvent event) {
    }

    @FXML
    private void GotoCreateForm(ActionEvent event) {
    }

    @FXML
    private void search(ActionEvent event) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("idCol"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstNameCol"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastNameCol"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("emailCol"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneCol"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("addressCol"));
        departmentCol.setCellValueFactory(new PropertyValueFactory<>("departmentCol"));
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salaryCol"));
        ssnCol.setCellValueFactory(new PropertyValueFactory<>("ssnCol"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("genderCol"));
        table.setItems(hrObservableList());
        actionCol.setCellValueFactory(new PropertyValueFactory<>("actions"));
        table.setItems(hrObservableList());
        refreshBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                table.getItems().clear();
                table.setItems(hrObservableList());
                System.out.printf("refreshed");
            }
        });
        searchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                search();
            }
        });

    }


    public ObservableList<hr> hrObservableList() {
        ObservableList<ManageEmplyessController.hr> observableList = FXCollections.observableArrayList();
        DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String, String>> list = dataBaseConnection.select("select * from staff;");

        for (Map<String, String> map : list) {
            observableList.add(new ManageEmplyessController.hr(map.get("id"), map.get("first_name"), map.get("last_name"), map.get("phone"), map.get("email"), map.get("address"), map.get("department_id"), map.get("salary"), map.get("mail"), map.get("ssn"), this));
        }
        return observableList;
    }


    public ObservableList<ManageEmplyessController.hr> hrObservableList(String search) {
        ObservableList<ManageEmplyessController.hr> observableList = FXCollections.observableArrayList();
        DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
        String query = "select * from staff";
        if (search != null && !search.isEmpty()) {
            query += " where first_name like '" + search + "%'";
        }
        query += ";";
        List<Map<String, String>> list = dataBaseConnection.select(query);

        for (Map<String, String> map : list) {
            observableList.add(new hr(map.get("id"), map.get("first_name"), map.get("last_name"), map.get("phone"), map.get("email"), map.get("address"), map.get("department_id"), map.get("salary"), map.get("mail"), map.get("ssn"), this));
        }

        return observableList;
    }
}