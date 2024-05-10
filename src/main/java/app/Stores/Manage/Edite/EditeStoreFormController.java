package app.Stores.Manage.Edite;

import app.Classes.DataBaseConnection;
import app.Stores.Manage.StoreManageController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class EditeStoreFormController implements Initializable {
    private String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";


    public void setId(String id) {
        this.id = id;
    }



    @FXML
    private VBox container;
    @FXML
    TextField NameField;

    @FXML
    TextArea LocationField;

    @FXML
    Button SubmitBtn;
    @FXML
    Label ErrorMsg;

    private String id;
    private Map<String,String> element;


    public void setData(){
        DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String ,String>> stock = dataBaseConnection.select("select * from stock where id="+id+";");

        if(stock.isEmpty() || stock == null){
            ErrorMsg.setText(id);
        }else{
            Map<String,String> map = stock.getFirst();
            element = map;
            NameField.setText(element.get("name"));
            LocationField.setText(element.get("location"));
        }
    }
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

        String query = "update  stock set name='"+name+"' , location='"+location+"'  where id="+id+";";
        DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
        boolean result = dataBaseConnection.excute(query);
        if (result){
            ErrorMsg.setText("Store Updated Successfully");
            ErrorMsg.setTextFill(Paint.valueOf("green"));

        }
        storeManageController.refreshTable();




    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {




    }
    private StoreManageController storeManageController;
    public void setStoreManageController(StoreManageController storeManageController) {
        this.storeManageController = storeManageController;

    }
}
