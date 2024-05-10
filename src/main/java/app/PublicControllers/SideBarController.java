package app.PublicControllers;

import app.HR.index.HRIndexApplication;
import app.Index.IndexApplication;
import app.Suppliers.SuppliersApplication;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import app.Stores.Index.*;
import javafx.stage.Stage;
import app.Stores.Index.StoreIndexApplication;
import app.HR.Employees.EmployeesAplication;

import java.io.IOException;

public class SideBarController {

    @FXML
    HBox OverView;

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



}
