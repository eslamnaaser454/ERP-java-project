package app.usermanagment.EditeUserManagment;

import app.Classes.DataBaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class EditeUserController implements Initializable {
    private  String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
@FXML
private Label error;
@FXML
private TextField tusername;
@FXML
private TextField tpassword;
@FXML
private TextField tphone;
@FXML
private TextField temail;
@FXML
private TextField tssn;
@FXML
private CheckBox admin;
@FXML
private Button edit;
String id;
public void setId(String id) {
        this.id = id;
    }

   public void edit(){
       String username= tusername.getText();
       String password=tpassword.getText();
       String phone =tphone.getText();
       String email=temail.getText();
       String ssn=tssn.getText();
       boolean admins=admin.isSelected();
       String emaill ="select email from users";
       String usernamee="select username from users";
       DataBaseConnection dataBaseConnectionss=new DataBaseConnection(dbPath);
       List <Map<String,String>>maps= dataBaseConnectionss.select(emaill);
       List <Map<String,String>>maps1p= dataBaseConnectionss.select(usernamee);

       if (username==null||username.isEmpty()){
           error.setText("username is empty ");
           error .setTextFill(Paint.valueOf("red"));
           tusername.setBorder(Border.stroke(Paint.valueOf("red")));
           return;
       }
       if (password==null||password.isEmpty()){
           error.setText("password is empty ");
           error .setTextFill(Paint.valueOf("red"));
           tpassword.setBorder(Border.stroke(Paint.valueOf("red")));
           return;
       }
       if (password.length()<8|| password.length()>16) {
           error.setText("Password Field should be between 8 to 16 ");
           error.setTextFill(Paint.valueOf("red"));
           tpassword.setBorder(Border.stroke(Paint.valueOf("red")));
           return;
       }
       if (phone==null||phone.isEmpty()){
           error.setText("Phone is empty ");
           error .setTextFill(Paint.valueOf("red"));
           tphone.setBorder(Border.stroke(Paint.valueOf("red")));
           return;
       }
       if (phone.length() <11 ) {
           error.setText("Phone Field Is greater 11 ");
           error.setTextFill(Paint.valueOf("red"));
           tphone.setBorder(Border.stroke(Paint.valueOf("red")));
           return;
       }
       if (!phone.matches("\\d+")){
           error.setText("Phone Field Contains Non-Digit Characters");
           error.setTextFill(Paint.valueOf("red"));
           tphone.setBorder(Border.stroke(Paint.valueOf("red")));
           return;

       }
       if (email==null||email.isEmpty()){
           error.setText("Email is empty ");
           error .setTextFill(Paint.valueOf("red"));
           temail.setBorder(Border.stroke(Paint.valueOf("red")));
           return;
       }
       if (ssn==null||ssn.isEmpty()){
           error.setText("Ssn is empty ");
           error .setTextFill(Paint.valueOf("red"));
           tssn.setBorder(Border.stroke(Paint.valueOf("red")));
           return;
       }
       if (ssn.length()!= 16) {
           error.setText("ssn Field must be gretar than 16 ");
           error.setTextFill(Paint.valueOf("red"));
           tusername.setBorder(Border.stroke(Paint.valueOf("red")));
           return;
       }
       DataBaseConnection dataBaseConnections = new DataBaseConnection(dbPath);
       List<Map<String,String>> SSN= dataBaseConnections.select("SELECT * FROM users where SSN ='"+ssn+"' ");
       if (SSN==null||SSN.isEmpty()){
           error.setText("ssn is null");
           error.setTextFill(Paint.valueOf("red"));
       }
       String query = "update users set username = '" +  username+ "', password = '" +password+ "', phone = '" +phone+ "', email= '" +email+ "', SSN = '" +ssn+ "'  where id = " + id + ";";
       DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
       boolean result = dataBaseConnection.excute(query);

       if (result==true) {
           error.setText("User Updated Successfully");
           error.setTextFill(Paint.valueOf("green"));
       } else {
           error.setText("Failed to update User");
           error.setTextFill(Paint.valueOf("red"));
       }
   }
   @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


//            load_data();

    }
    public void load_data(){
    DataBaseConnection dataBaseConnection=new DataBaseConnection(dbPath);
        List<Map<String, String>> user = dataBaseConnection.select("select * from users where id = " + id + ";");
        if (user==null||user.isEmpty()){
            System.err.println("No user found for ID: " + id);
            return;
        }
        Map<String,String> users=user.getFirst();
        tusername.setText(users.get("username"));
        tpassword.setText(users.get("password"));
        temail.setText(users.get("email"));
        tphone.setText(users.get("phone"));
        tssn.setText(users.get("SSN"));
        admin.setText(users.get("admins"));

    }


}
