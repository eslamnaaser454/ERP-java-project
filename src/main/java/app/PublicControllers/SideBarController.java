package app.PublicControllers;

import app.Classes.Authentication;
import app.HR.index.HRIndexApplication;
import app.Index.IndexApplication;
import app.Login.LoginApplication;
import app.Sales.SalesApplication;
import app.Suppliers.SuppliersApplication;
import app.usermanagment.usermanagmentapp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import app.Stores.Index.*;
import javafx.stage.Stage;
import app.Stores.Index.StoreIndexApplication;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SideBarController implements Initializable {

    @FXML
    HBox OverView;

    @FXML
    HBox UsersNav;

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
    public void GoToSuppliers(){
        Stage stage = (Stage) OverView.getScene().getWindow();
        SuppliersApplication nextPage = new SuppliersApplication();
        try {
            nextPage.start(stage);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }

    @FXML
    private void GoToStoresIndex(){
        StoreIndexApplication storeIndexApplication = new StoreIndexApplication();
        Stage stage = (Stage) OverView.getScene().getWindow();
        try {
            storeIndexApplication.start(stage);

        }catch (IOException e){
            System.out.println(e.getCause());
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
    private void GoToSales(){
        SalesApplication salesApplication = new SalesApplication();
        Stage stage = (Stage) OverView.getScene().getWindow();
        try {
            salesApplication.start(stage);

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

    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK){
        LoginApplication loginApplication = new LoginApplication();
        Stage stage = (Stage) OverView.getScene().getWindow();
        try {
            loginApplication.start(stage);
        }
        catch (IOException e){
            System.out.println(e.getCause());
        }
    }
}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Authentication authentication = new Authentication();

        if (!authentication.getUser().get("is_super_user").equals("1")){
            System.out.println("Equal");
            UsersNav.setVisible(false);
        }else {
            System.out.println("Not Equal "+authentication.getUser().get("is_super_user"));

        }

    }
}
