package app.usermanagment;

import app.Classes.DataBaseConnection;
import app.Log.LogApplication;
import app.usermanagment.Createuser.CreateuserApplication;
import app.usermanagment.EditeUserManagment.EditeUserApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class usermangmentcontroller implements Initializable
{
    ///
    private String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";

    @FXML
    private TableView<user> tabel;
    @FXML
    private TableColumn<user, String> userCol;
    @FXML
    private TableColumn<user, String> emailCol;
    @FXML
    private TableColumn<user, String> phoneCol;
    @FXML
    private TableColumn<user, String> ssnCol;
    @FXML
    private TableColumn<user, String> activeCol;
    @FXML
    private TableColumn<user, String> typeCol;
    @FXML
    private TableColumn<user, HBox> action;
    @FXML
    private Button Add;
    @FXML
    private Button search;
    @FXML
    private Button refresh;
    @FXML
    private TextField tsearch;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        ssnCol.setCellValueFactory(new PropertyValueFactory<>("ssn"));
        activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        action.setCellValueFactory(new PropertyValueFactory<>("action"));


        tabel.setItems(observableList());
    }

    private ObservableList<user> observableList()
    {
        ObservableList<user> userObservableList = FXCollections.observableArrayList();
        DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String, String>> list = dataBaseConnection.select("select * from users;");

        if (list == null)
        {
            System.out.println("Database connection failed or query returned null");
            return userObservableList;
        }

        for (Map<String, String> map : list)
        {
            userObservableList.add(new user(
                    map.get("username"),
                    map.get("email"),
                    map.get("phone"),
                    map.get("SSN"),
                    map.get("is_active"),
                    map.get("is_super_user"),
                    map.get("id"),
                    this
            ));
        }

        return userObservableList;
    }

    @FXML
    public void Logg(){
        LogApplication logApplication = new LogApplication();
        Stage stage =(Stage) tabel.getScene().getWindow();
        try {
            logApplication.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class user
    {
        private String username;
        private String email;
        private String phone;
        private String ssn;
        private String active;
        private String type;
        private  HBox action ;
        private String id ;



        public user(String username, String email, String phone, String ssn, String active, String type,String id ,usermangmentcontroller usermangmentcontroller)
        {
            this.username = username;
            this.email = email;
            this.phone = phone;
            this.ssn = ssn;
            this.active = active;
            this.type = type;
            this.id =id;
            Button edit=new Button("edit");
            edit.setBackground(Background.fill(Paint.valueOf("blue")));
            edit.setTextFill(Paint.valueOf("white"));
            edit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        EditeUserApplication editeUserApplication=new EditeUserApplication(id);
                        Stage stage=new Stage();
                        stage.setTitle("edit");
                        stage.setResizable(true);
                        editeUserApplication.start(stage);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            Button delet=new Button("delet");
            delet.setBackground(Background.fill(Paint.valueOf("red")));
            delet.setTextFill(Paint.valueOf("white"));
            delet.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try{
                    ButtonType buttonType=new ButtonType("cancel");
                    ButtonType buttonType1=new ButtonType("delete");
                    Alert alert =new Alert(Alert.AlertType.WARNING,"Are you want to delete this item ",buttonType,buttonType1);
                    alert.setHeaderText(null);
                    Optional<ButtonType> result = alert.showAndWait();//return the value of alert
                        //////////////////////////////////////////////
                    if (result.isPresent()&&result.get()==buttonType1){
                        String query = "delete from users where id = " + id + ";";
                        if (query ==null){
                            System.out.println( "here1");
                        }
                        System.out.println("Executing query: " + query);
                        DataBaseConnection dataBaseConnection=new DataBaseConnection(usermangmentcontroller.dbPath);
                        dataBaseConnection.excute(query);
                        usermangmentcontroller.refresh();
                    }

                }
                catch (Exception e){
                       throw new  RuntimeException(e);
                }
                }

           });
            HBox hBox=new HBox();
            hBox.getChildren().addAll(edit,delet);
            setAction(hBox);

        }


        public void setAction(HBox action) {
            this.action = action;
        }

        public HBox getAction() {
            return action;
        }

        public String getUsername()
        {
            return username;
        }

        public String getEmail()
        {
            return email;
        }

        public String getPhone()
        {
            return phone;
        }

        public String getSsn()
        {
            return ssn;
        }

        public String getActive()
        {
            return active;
        }

        public String getType()
        {
            return type;
        }

        public void setUsername(String username)
        {
            this.username = username;
        }

        public void setEmail(String email)
        {
            this.email = email;
        }

        public void setPhone(String phone)
        {
            this.phone = phone;
        }

        public void setSsn(String ssn)
        {
            this.ssn = ssn;
        }

        public void setActive(String active)
        {
            this.active = active;
        }

        public void setType(String type)
        {this.type = type;}
    }

    @FXML
    public void adduser()
    {
        try
        {
            CreateuserApplication CreateuserApplication = new CreateuserApplication();
            Stage stage = new Stage();
            stage.setTitle("Create User");
            stage.setResizable(false);
            CreateuserApplication.start(stage);
        }
        catch (IOException e)
        {
            System.out.println("here");;
        }
    }
    @FXML
    public void refresh()
    {
        tabel.getItems().clear();
        tabel.setItems(observableList());
    }
    private ObservableList<user> search_observableList(String search) {
        ObservableList<user> userObservableList = FXCollections.observableArrayList();
        DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);


        String query = "select * from users";


        if (search != null && !search.isEmpty()) {
            query += " where username like '" + search + "%'";
        }

        query += ";";


        List<Map<String, String>> list = dataBaseConnection.select(query);

        if (list == null) {
            System.out.println("Database connection failed or query returned null");
            return userObservableList;
        }

                for (Map<String, String> map : list) {
            userObservableList.add(new user(
                    map.get("username"),
                    map.get("email"),
                    map.get("phone"),
                    map.get("SSN"),
                    map.get("is_active"),
                    map.get("is_super_user"),
                    map.get("id"),
                    this
            ));
        }

        return userObservableList;
    }


@FXML
    public void Search()
    {
        String search=(String)tsearch.getText();
        tabel.getItems().clear();
        tabel.setItems(search_observableList(search));
    }


}
