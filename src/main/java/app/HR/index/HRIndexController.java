package app.HR.index;

import app.HR.Department.DepartmentApplication;
import app.HR.Manage.ManageEmplyessApplication;
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
    @FXML
    public void GotoDepartment(){
        DepartmentApplication DepartmentApplication = new DepartmentApplication();
        Stage stage = (Stage) manage.getScene().getWindow();
        stage.setResizable(true);
        try {
            DepartmentApplication.start(stage);

        }catch (IOException e){
            System.out.println(e.getCause());
        }
    }
}
