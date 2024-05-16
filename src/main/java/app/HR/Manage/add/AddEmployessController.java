package app.HR.Manage.add;


import app.Classes.Logging;
import app.Stores.Manage.StoreManageController;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import app.Classes.DataBaseConnection;
import app.Stores.Manage.Create.CreateStoreFormApplication;
import app.Stores.Manage.Edite.EditeStoreFormApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import java.net.URL;
import java.util.ResourceBundle;

public class AddEmployessController implements Initializable {
    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
    private String id;
    DataBaseConnection dataBaseConnection;

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


    TextField jobField;

    @FXML
    private void submit(ActionEvent event) {
        String fname = fnameField.getText();
        String lname = lnameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = AddressField.getText();
        String salary = salaryField.getText();
        String ssn = SSNField.getText();
        boolean isMaleSelected = Malerad.isSelected();
        boolean isFemaleSelected = Femalerad.isSelected();

        String department = Departmentco.getValue();
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String, String>> getdepartment = dataBaseConnection.select("select * from department where name = '" + department + "';");

//        if (getdepartment.isEmpty()) {
//            ErrMsg.setText("Please Select Department");
//            ErrMsg.setTextFill(Paint.valueOf("red"));
//            return;
//        } else {
//            String departmentId = getdepartment.getFirst().get("id");
//
//        }
        if (fname.isEmpty()) {
            ErrMsg.setText("Please Enter First Name");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            fnameField.setBorder(Border.stroke(Paint.valueOf("red")));
            return;

        } else if (lname.isEmpty()) {
            ErrMsg.setText("Please Enter Last Name");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            lnameField.setBorder(Border.stroke(Paint.valueOf("red")));
            return;

        } else if (phone.isEmpty()) {
            ErrMsg.setText("Please Enter Phone");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            phoneField.setBorder(Border.stroke(Paint.valueOf("red")));
            return;

        } else if (email.isEmpty()) {
            ErrMsg.setText("Please Enter Email");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            emailField.setBorder(Border.stroke(Paint.valueOf("red")));
            return;
        }
        else if (salary.isEmpty()) {
            ErrMsg.setText("Please Enter Salary");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            salaryField.setBorder(Border.stroke(Paint.valueOf("red")));
            return;

        }
        else if (ssn.isEmpty()) {
            ErrMsg.setText("Please Enter SSN");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            SSNField.setBorder(Border.stroke(Paint.valueOf("red")));
            return;

        }else if (address.isEmpty()) {

            ErrMsg.setText("Please Enter Address");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            AddressField.setBorder(Border.stroke(Paint.valueOf("red")));
            return;

        } else if (department.isEmpty()) {
            ErrMsg.setText("Please Select Department");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            Departmentco.setBorder(Border.stroke(Paint.valueOf("red")));
            return;

        } else if (!isMaleSelected && !isFemaleSelected) {
            ErrMsg.setText("Please Select gender");
            Malerad.setBorder(Border.stroke(Paint.valueOf("red")));
            Femalerad.setBorder(Border.stroke(Paint.valueOf("red")));
            return;

        }
        Map<String, String> departmentV = getdepartment.getFirst();
        String departmentId = departmentV.get("id");

        dataBaseConnection = new DataBaseConnection(dbPath);
        String query = "insert into staff (first_name,last_name,phone,email,salary,ssn,department_id,mail,femail,address) values ('" + fname + "','" + lname + "','" + phone + "','" + email + "','" + salary + "','" + ssn + "','" + departmentId + "','" + isMaleSelected + "','" + isFemaleSelected + "','" + address + "');";
        System.out.printf("Query is" + query);
        dataBaseConnection.excute(query);
        ErrMsg.setText("Employee Added Successfully");
        ErrMsg.setTextFill(Paint.valueOf("green"));
        fnameField.setText("");
        lnameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        AddressField.setText("");
        salaryField.setText("");
        SSNField.setText("");
        Departmentco.setValue("");
        Malerad.setSelected(false);
        Femalerad.setSelected(false);
        Logging logging = new Logging() ;
        logging.addLog("Employee with name "+fname+" "+lname+" has been Adeed to the system");


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup group = new ToggleGroup();

        RadioButton Malerad = new RadioButton("male");
        Malerad.setUserData("male");
        Malerad.setToggleGroup(group);
        Malerad.setSelected(true);


        RadioButton Femalerad = new RadioButton("female");
        Femalerad.setUserData("female");
        Femalerad.setToggleGroup(group);
        Malerad.setSelected(true);


        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String, String>> list = dataBaseConnection.select("select * from department;");
        if (!list.isEmpty()) {
            System.out.println("Not Empty");
            for (Map<String, String> map : list) {
                Departmentco.getItems().add(map.get("name"));
            }
        } else {
            System.out.println(" Empty");

        }
    }
}




