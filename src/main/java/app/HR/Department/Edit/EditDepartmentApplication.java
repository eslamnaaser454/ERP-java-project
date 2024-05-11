package app.HR.Department.Edit;
import app.HR.Department.Departmentcontroller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EditDepartmentApplication extends Application {
    private String id;
    public void setId(String id) {
        this.id = id;
    }
    public EditDepartmentApplication(String id) {
        this.id = id;
    }
    public static void main(String[] args) {
        launch(args);
    }

@Override
public void start(Stage primaryStage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(EditDepartmentApplication.class.getResource("DepartmentEdit.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    EditDepartmentcontroller editDepartmentcontroller = fxmlLoader.getController();
    editDepartmentcontroller.setDepartmentcontroller(departmentcontroller);
    editDepartmentcontroller.setId(id);
    editDepartmentcontroller.setData();
    System.out.println("from app"+id);
    primaryStage.setScene(scene);
    primaryStage.show();
}
    private Departmentcontroller departmentcontroller;
    public void setDepartmentcontroller(Departmentcontroller departmentcontroller) {
        this.departmentcontroller = departmentcontroller;
    }

}
