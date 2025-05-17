package app.PublicControllers;

import app.Classes.Authentication;
import app.Classes.DataBaseConnection;
import app.HR.index.HRIndexApplication;
import app.Index.IndexApplication;
import app.Login.LoginApplication;
import app.Market.MarketApplication;
import app.Sales.SalesApplication;
import app.Suppliers.SuppliersApplication;
import app.usermanagment.usermanagmentapp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import app.Stores.Index.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import app.Stores.Index.StoreIndexApplication;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class HrSidebarController implements Initializable {
    @FXML
    private Label name;
    @FXML
    private HBox HR;

    @FXML
    HBox OverView;

    @FXML
    HBox UsersNav;
    @FXML
    HBox SalesHbox;
    @FXML
    HBox MarketHBox;
    @FXML
    HBox SuppliersHBox;
    @FXML
    HBox  StoresHBox;







    @FXML
    public void GoToOverView(){
        Stage stage = (Stage) OverView.getScene().getWindow();
        IndexApplication nextPage = new IndexApplication();
        try {
            nextPage.start(stage);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }

    @FXML
    public void GotoHR(){
        HRIndexApplication HRIndexApplication = new HRIndexApplication();
        Stage stage = (Stage) OverView.getScene().getWindow();
        stage.setResizable(true);
        try {
            HRIndexApplication.start(stage);

        }catch (IOException e){
            System.out.println(e.getCause());
        }
    }

    @FXML
    private void GoToUserManagment(){

        usermanagmentapp Usermanagmentapp = new usermanagmentapp();
        Stage stage = (Stage) OverView.getScene().getWindow();
        try {
            Usermanagmentapp.start(stage);

        }catch (IOException e){
            System.out.println();
        }
    }
    @FXML
    private void GoToLogin(){


        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Are you sure you want to logout?");
        try {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                String dbPath1 = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
                Authentication authentication = new Authentication();

                DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath1);
                String user=authentication.getUser().get("username");
                boolean username = false; // Replace "name" with the actual username
                System.out.println("USERNAME NOW = "  + user);
                String query = "UPDATE users SET is_active = '" + username + "' WHERE username = '" + user + "';";
                boolean result1 = dataBaseConnection.execute(query);
                System.out.println(result1);

                LoginApplication loginApplication = new LoginApplication();
                Stage stage = (Stage) OverView.getScene().getWindow();

                loginApplication.start(stage);

            }

        }
        catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Authentication authentication = new Authentication();

        System.out.println("TYPE =  "+authentication.getUser().get("type"));
        name.setText("Hello, "+authentication.getUser().get("username"));
        if (authentication.getUser().get("is_super_user").equals("false")){
            System.out.println("Equal");
            UsersNav.setVisible(false);
        }else {
            System.out.println("Not Equal "+authentication.getUser().get("is_super_user"));

        }

    }
}
