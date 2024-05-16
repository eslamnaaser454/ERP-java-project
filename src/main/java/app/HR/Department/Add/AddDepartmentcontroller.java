package app.HR.Department.Add;

import app.Classes.DataBaseConnection;
import app.Classes.Logging;
import app.HR.Department.Departmentcontroller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;



public class AddDepartmentcontroller implements Initializable {
    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
    DataBaseConnection dataBaseConnection;
    @FXML
    TextField DepartmentName;

    Departmentcontroller departmentcontroller;

    public void setDepartmentcontroller(Departmentcontroller departmentcontroller) {
        this.departmentcontroller = departmentcontroller;
    }

    @FXML
    Label ErrMsg;
    public void submit(ActionEvent actionEvent) {

        String departmentName = DepartmentName.getText();
        if (departmentName.isEmpty()) {
            ErrMsg.setText("Please enter department name");
            return;
        }
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String, String> >map = dataBaseConnection.select("select * from department where name = '" + departmentName + "';");
        if (!map.isEmpty()) {
            ErrMsg.setText("Department already exists");
            return;
        }
        dataBaseConnection.insert("insert into department (name) values ('" + departmentName + "');");
        ErrMsg.setText("Department added successfully");
        ErrMsg.setTextFill(Paint.valueOf("green"));
        DepartmentName.setText("");
        departmentcontroller.LoadCards();

        Logging logging = new Logging();
        logging.addLog("Department with Name \"" + departmentName + "\" added successfully");
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}