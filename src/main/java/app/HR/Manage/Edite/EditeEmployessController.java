package app.HR.Manage.Edite;

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

public class EditeEmployessController implements Initializable {
    private String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";

    public void setId(String id) {
        this.id = id;
    }

    @FXML
    TextField fnameField;
    @FXML
    TextField lnameField;
    @FXML
    TextField emailField;
    @FXML
    TextField phoneField;
    @FXML
    TextField AddressField;
    @FXML
    TextField salaryField;
    @FXML
    TextField SSNField;
    @FXML
    ChoiceBox<String> Departmentco;
    @FXML
    RadioButton Malerad;
    @FXML
    RadioButton Femalerad;
    @FXML
    Label ErrMsg;

    private String id;
    private Map<String, String> element;

    public void setData() {
        DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String, String>> staff = dataBaseConnection.select("select * from staff where id=" + id + ";");
        List<Map<String, String>> departments = dataBaseConnection.select("select * from department;");

        if (staff.isEmpty() || staff == null) {
            ErrMsg.setText(id);
        } else {
            Map<String, String> employee = staff.getFirst();
            element = employee;
            fnameField.setText(element.get("first_name"));
            lnameField.setText(element.get("last_name"));
            emailField.setText(element.get("email"));
            phoneField.setText(element.get("phone"));
            AddressField.setText(element.get("address"));
            salaryField.setText(element.get("salary"));
            SSNField.setText(element.get("ssn"));
            ToggleGroup toggleGroup = new ToggleGroup();
            Malerad.setToggleGroup(toggleGroup);
            Femalerad.setToggleGroup(toggleGroup);

            // Add all departments to the ChoiceBox
            for (Map<String, String> department : departments) {
                Departmentco.getItems().add(department.get("name"));
            }

            // Set the value of the ChoiceBox to the department of the employee
            String employeeDepartmentId = element.get("department_id");
            for (Map<String, String> department : departments) {
                if (department.get("id").equals(employeeDepartmentId)) {
                    Departmentco.setValue(department.get("name"));
                    break;
                }
            }

            // Set the value of the radio button to the gender of the employee
            String ismail = element.get("mail");
            String isfemail = element.get("femail");
            if (ismail.equals("1") || ismail.equals("true")) {
                Malerad.setSelected(true);
            } else if (isfemail.equals("1") || isfemail.equals("true")) {
                Femalerad.setSelected(true);
            }
        }
    }

    @FXML
    public void submit() {
        String fname = fnameField.getText();
        String lname = lnameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = AddressField.getText();
        String salary = salaryField.getText();
        String ssn = SSNField.getText();
        String department = Departmentco.getValue();
        String department_id = "";
        DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);



        if (fname.isEmpty()|| fname == null) {
            ErrMsg.setText("Please Enter First Name");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            return;
        }
        if (lname.isEmpty()|| lname ==null) {
            ErrMsg.setText("Please Enter Last Name");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            return;
        }
        if (email.isEmpty() || email==null) {
            ErrMsg.setText("Please Enter Email");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            return;
        }
        if (!email.contains("@")) {
            ErrMsg.setText("Email must contain @ symbol");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            return;
        }
        if (phone.isEmpty()||phone==null) {
            ErrMsg.setText("Please Enter Phone");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            return;
        }
        if (phone.length()>11||phone.length()<11){
            ErrMsg.setText("the number must be 11 ");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            return;
        }
        if (!phone.matches("\\d+")){
            ErrMsg.setText("Phone Field Contains Non-Digit Characters");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            return;

        }
        if (address.isEmpty()||address==null) {
            ErrMsg.setText("Please Enter Address");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            return;
        }
        if (salary.isEmpty()||salary==null) {
            ErrMsg.setText("Please Enter Salary");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            return;
        }
//        if (!salary.matches("\\d+")){
//            ErrMsg.setText("salary Field Contains Non-Digit Characters");
//            ErrMsg.setTextFill(Paint.valueOf("red"));
//            return;
//
//        }
        if (ssn.isEmpty()||ssn ==null) {
            ErrMsg.setText("Please Enter SSN");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            return;
        }
        if (ssn.length()!=16) {
            ErrMsg.setText(" SSN must be 16 integer");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            return;
        }
        if (department.isEmpty()||department==null) {
            ErrMsg.setText("Please Enter Department");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            return;
        }
//        if (!(Malerad.isSelected() ^ Femalerad.isSelected())) {
//            ErrMsg.setText("Please Select one gender");
//            ErrMsg.setTextFill(Paint.valueOf("red"));
//            return;
//        }



        List<Map<String, String>> departments = dataBaseConnection.select("select * from department;");
        for (Map<String, String> departmentc : departments) {
            if (departmentc.get("name").equals(department)) {
                department_id = departmentc.get("id");
                break;
            }
        }
        String query = "update staff set first_name='" + fname + "',last_name='" + lname + "',email='" + email + "',phone='" + phone + "',address='" + address + "',salary='" + salary + "',ssn='" + ssn + "',department_id='" + department_id + "',mail='" + Malerad.isSelected() + "',femail='" + Femalerad.isSelected() + "' where id=" + id + ";";
        ErrMsg.setText("Employee Added Successfully");
        boolean result = new DataBaseConnection(dbPath).excute(query);
        if (result) {
            Stage stage = (Stage) ErrMsg.getScene().getWindow();
            stage.close();
        } else {
            ErrMsg.setText("Error while updating");
        }
        manageEmplyessController.refreshTable();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private ManageEmplyessController manageEmplyessController;
    public void setManageEmplyessController(ManageEmplyessController manageEmplyessController) {
        this.manageEmplyessController = manageEmplyessController;
}

}