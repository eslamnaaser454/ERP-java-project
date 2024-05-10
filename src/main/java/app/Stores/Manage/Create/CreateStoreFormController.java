package app.Stores.Manage.Create;

import app.Classes.DataBaseConnection;
import app.Stores.Manage.StoreManageController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;

import java.util.List;
import java.util.Map;

public class CreateStoreFormController {
    private String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";



    @FXML
    TextField NameField;

    @FXML
    TextArea LocationField;

    @FXML
    Button SubmitBtn;
    @FXML
    Label ErrorMsg;


    @FXML
    public void submit(){
        String name = (String) NameField.getText();
        String location = (String) LocationField.getText();
        if ((name.isEmpty() || name == null) ){
           ErrorMsg.setTextFill(Paint.valueOf("red"));
           ErrorMsg.setText("Name Field Is Empty");
           NameField.setBorder(Border.stroke(Paint.valueOf("red")));

           return;
        }
        if ((location.isEmpty() || location == null)){
            ErrorMsg.setTextFill(Paint.valueOf("red"));
            ErrorMsg.setText("Location Field Is Empty");
            LocationField.setBorder(Border.stroke(Paint.valueOf("red")));

            return;

        }

        String query = "insert into stock(name,location) values('"+name+"','"+location+"');";
        DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
        boolean result = dataBaseConnection.excute(query);
        if (result){
            ErrorMsg.setText("Store Created Successfully");
            ErrorMsg.setTextFill(Paint.valueOf("green"));
            NameField.setText("");
            LocationField.setText("");
        }

    }
}
