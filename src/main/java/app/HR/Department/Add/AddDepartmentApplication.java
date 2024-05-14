package app.HR.Department.Add;

import app.HR.Department.Departmentcontroller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddDepartmentApplication extends Application {
    Departmentcontroller departmentcontroller;

    public AddDepartmentApplication(Departmentcontroller departmentcontroller){
        this.departmentcontroller = departmentcontroller;
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AddDepartmentApplication.class.getResource("Adddepartment.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        AddDepartmentcontroller addDepartmentcontroller = fxmlLoader.getController();
        addDepartmentcontroller.setDepartmentcontroller(departmentcontroller);
        primaryStage.show();
    }
}

