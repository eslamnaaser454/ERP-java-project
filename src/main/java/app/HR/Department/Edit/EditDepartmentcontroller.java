package app.HR.Department.Edit;

import app.HR.Department.Departmentcontroller;
import javafx.fxml.Initializable;

import app.HR.Manage.ManageEmplyessController;
import app.Stores.Manage.Edite.EditeStoreFormController;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import app.Classes.DataBaseConnection;
import app.Stores.Manage.StoreManageController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class EditDepartmentcontroller implements Initializable {
    private String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
    private String id;
    @FXML
    TextField DepartmentName;
    @FXML
    Label ErrMsg;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setId(String id) {
        this.id = id;
    }
    private Map<String, String> element;
    public void setData() {
        DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String, String>> department = dataBaseConnection.select("select * from department where id=" + id + ";");
        System.out.println(id);
        if (department.isEmpty()) {
            ErrMsg.setText("Department not found");
            return;
        }

        element = department.getFirst();
        DepartmentName.setText(element.get("name"));

    }


public void submit() {
    String name = DepartmentName.getText();
    DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
    if (name.isEmpty() || id.isEmpty()) {
        ErrMsg.setText("Please fill all fields");
        return;
    }
    String query = "update department set name='" + name + "' where id=" + id + ";";
    dataBaseConnection= new DataBaseConnection(dbPath);
    dataBaseConnection.excute(query);
    ErrMsg.setText("Department updated successfully");
    ErrMsg.setTextFill(Paint.valueOf("green"));
        departmentcontroller.refresh();

}
    private Departmentcontroller departmentcontroller;
    public void setDepartmentcontroller(Departmentcontroller departmentcontroller) {
        this.departmentcontroller = departmentcontroller;
    }
}
