package app.HR.index;

import app.HR.Employees.EmployeesAplication;
import app.HR.Manage.ManageEmplyessApplication;
import app.Stores.Manage.StoreManageApplication;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HRIndexController {
    @FXML
    public VBox manage;
    @FXML
    public void GotoManageemployes(){
        ManageEmplyessApplication ManageEmplyessApplication = new ManageEmplyessApplication();
        Stage stage = (Stage) manage.getScene().getWindow();
        stage.setResizable(true);
        try {
            ManageEmplyessApplication.start(stage);

        }catch (IOException e){
            System.out.println(e.getCause());
        }
    }
}
