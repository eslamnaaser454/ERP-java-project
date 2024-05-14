package app.Stores.Manage;

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

public class StoreManageController implements Initializable {

    private String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
    private String TableName = "stock";

    @FXML
    private TableView<store> table;
    @FXML
    private TableColumn<store, String> idCol;
    @FXML
    private TableColumn<store, String> nameCol;
    @FXML
    private TableColumn<store, String> locationCol;
    @FXML
    TableColumn<store,HBox> actionCol;

    @FXML
    private TextField searchField;

    @FXML Button refreshBtn;

//    @FXML
//    private TableColumn<store, String> editCol;

    public StoreManageController() {
    }

    public ObservableList<store> storeObservableList() {
        ObservableList<store> observableList = FXCollections.observableArrayList();
        DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String,String>> list = dataBaseConnection.select("select * from stock;");

        for (Map<String,String> map:list){
            observableList.add(new store(map.get("id"),map.get("name"),map.get("location"),this));
        }
        return observableList;
    }

    public ObservableList<store> storeObservableList(String search ) {
        ObservableList<store> observableList = FXCollections.observableArrayList();
        DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
        String query = "select * from stock";
        if (search != null && !search.isEmpty()){
            query+= " where name like '"+search+"%'";
        }
        query+=";";
        List<Map<String,String>> list = dataBaseConnection.select(query);
       // System.out.println(query);

        for (Map<String,String > map:list){
          //  System.out.println(map.get("name"));
            observableList.add(new store(map.get("id"),map.get("name"),map.get("location"),this));
        }

        return observableList;
    }


    @FXML
    public void GotoCreateForm() {
        Stage stage = new Stage();
        stage.setResizable(false);
        CreateStoreFormApplication createStoreFormApplication = new CreateStoreFormApplication();
        try {
            createStoreFormApplication.start(stage);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("idCol"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nameCol"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("locationCol"));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("actions"));


//        table.setItems(storeObservableList());
    }

    @FXML
    public void refreshTable(){
        table.getItems().clear();
        table.setItems(storeObservableList());
        System.out.printf("Table Refreshed\n");
    }
@FXML
    public void search(){

        String value = (String) searchField.getText();


        table.getItems().clear();
        table.setItems(storeObservableList(value));
    }

    // Inner class for store model
    public  class store {
        public String idCol;
        public String nameCol;
        public String locationCol;

        public Button deleteBtn;



        public Button editBtn;

        public HBox actions;


        private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";






        public String x;
        private StoreManageController storeManageController;
        public store(String idCol, String nameCol, String locationCol,StoreManageController storeManageController) {
            this.idCol = idCol;
            this.nameCol = nameCol;
            this.locationCol = locationCol;
            this.storeManageController = storeManageController;

            editBtn = new Button("Edite");
            editBtn.setBackground(Background.fill(Paint.valueOf("blue")));
            editBtn.setStyle("-fx-background-color: blue; -fx-background-radius: 15;");
            editBtn.setTextFill(Paint.valueOf("white"));
            editBtn.setUserData(idCol);
            editBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    EditeStoreFormApplication editeStoreFormApplication = new EditeStoreFormApplication(idCol);
                    editeStoreFormApplication.setStoreManageController(storeManageController);
                    Stage stage = new Stage();
                    stage.setResizable(false);
                    try {
                        editeStoreFormApplication.start(stage);

                    }catch (IOException e){
                        System.out.println("here "+ e.toString());
                    }

                }
            });


            deleteBtn = new Button("Delete");
            deleteBtn.setBackground(Background.fill(Paint.valueOf("red")));
            deleteBtn.setStyle("-fx-background-color: red; -fx-background-radius: 15;");
            deleteBtn.setTextFill(Paint.valueOf("white"));
            deleteBtn.setUserData(idCol);
            deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
                    ButtonType okButton = new ButtonType("ok");
                    ButtonType cancleButton = new ButtonType("Cancel");

                    Alert alert = new Alert(Alert.AlertType.WARNING,"Are you sure you want to delete that Element",okButton,cancleButton);
                    alert.setHeaderText(null);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == okButton) {
                        String query = "delete from stock where id=" + idCol + ";";
                        System.out.println(query);
                        dataBaseConnection.excute(query);
                        storeManageController.refreshTable();
                    }

                }
            });
            actions = new HBox();
            actions.setSpacing(40);
            actions.setPrefHeight(20);
            actions.setAlignment(javafx.geometry.Pos.CENTER);
            actions.getChildren().addAll(editBtn,deleteBtn);
        }

        public Button getDeleteBtn() {
            return deleteBtn;
        }

        public String getIdCol() {
            return idCol;
        }

        public void setIdCol(String idCol) {
            this.idCol = idCol;
        }

        public String getNameCol() {
            return nameCol;
        }
        public Button getEditBtn() {
            return editBtn;
        }

        public void setNameCol(String nameCol) {
            this.nameCol = nameCol;
        }

        public String getLocationCol() {
            return locationCol;
        }

        public void setLocationCol(String locationCol) {
            this.locationCol = locationCol;
        }

        public HBox getActions() {
            return actions;
        }
    }


}
