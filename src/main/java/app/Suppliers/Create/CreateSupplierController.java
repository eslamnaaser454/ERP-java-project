package app.Suppliers.Create;

import app.Classes.DataBaseConnection;
import app.Classes.Logging;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;

public class CreateSupplierController {
    private String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";



    @FXML
    private Label ErrMsg;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField companyField;



    @FXML
    public void submit(){
        String name = (String) nameField.getText();
        String phone = (String) phoneField.getText();
        String email = (String) emailField.getText();
        String company = (String) companyField.getText();

        if (name.isEmpty()){
            nameField.setBorder(Border.stroke(Paint.valueOf("red")));
            ErrMsg.setText("Name Field Is Empty");
            ErrMsg.setTextFill(Paint.valueOf("red"));
        } else if (phone.isEmpty()) {
            phoneField.setBorder(Border.stroke(Paint.valueOf("red")));
            ErrMsg.setText("Phone Field Is Empty");
            ErrMsg.setTextFill(Paint.valueOf("red"));
        } else if (email.isEmpty()) {
            emailField.setBorder(Border.stroke(Paint.valueOf("red")));
            ErrMsg.setText("E-mail Field Is Empty");
            ErrMsg.setTextFill(Paint.valueOf("red"));
        } else if (company.isEmpty()) {
            companyField.setBorder(Border.stroke(Paint.valueOf("red")));
            ErrMsg.setText("Company Field Is Empty");
            ErrMsg.setTextFill(Paint.valueOf("red"));
        }else {
            DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
            dataBaseConnection.excute("insert into supplier(name,phone,email,company,rate) values('"+ name +"','"+ phone +"','"+email+"','"+company+"',0.0);");
            ErrMsg.setText("Supplier Added Successfuly");
            ErrMsg.setTextFill(Paint.valueOf("green"));
            nameField.setText("");
            phoneField.setText("");
            emailField.setText("");
            companyField.setText("");
            Logging logging = new Logging();
            logging.addLog("new Supplier with name "+name+" Added ");
        }
    }
}
