package app.usermanagment.Createuser;

import app.Classes.DataBaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;
import java.net.URL;
import java.util.*;

public class CreateuserConroller implements Initializable {

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
    private Button add;
    @FXML
    private Label error;
    private Map<String, String> element;
    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
    @FXML
    public void add() {
        String username = tusername.getText();
        String password = tpassword.getText();
        String phone = tphone.getText();
        String email = temail.getText();
        String ssn =   tssn.getText();
        boolean admins=admin.isSelected();
        String emaill ="select email from users";
        String usernamee="select username from users";
        DataBaseConnection dataBaseConnectionss=new DataBaseConnection(dbPath);
        List <Map<String,String>>maps= dataBaseConnectionss.select(emaill);
        List <Map<String,String>>maps1p= dataBaseConnectionss.select(usernamee);


        if (username == null || username.isEmpty()) {
            error.setText("Username Field Is Empty");
            error.setTextFill(Paint.valueOf("red"));
            tusername.setBorder(Border.stroke(Paint.valueOf("red")));
            return;
        }
        for (Map<String,String> map:maps1p){
            String user=map.get("username");
            if (username.equals(user)){
                error.setText("user name is exist");
                error.setTextFill(Paint.valueOf("red"));
                tusername.setBorder(Border.stroke(Paint.valueOf("red")));
                return;
            }
        }


        if (password == null || password.isEmpty()) {
            error.setText("Password Field Is Empty");
            error.setTextFill(Paint.valueOf("red"));
            tpassword.setBorder(Border.stroke(Paint.valueOf("red")));
            return;
        }
        if (password.length()<8|| password.length()>16) {
            error.setText("Password Field should be between 8 to 16 ");
            error.setTextFill(Paint.valueOf("red"));
            tpassword.setBorder(Border.stroke(Paint.valueOf("red")));
            return;
        }

        if (phone == null || phone.isEmpty()) {
            error.setText("Phone Field Is Empty");
            error.setTextFill(Paint.valueOf("red"));
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

        if (email == null || email.isEmpty()) {
            error.setText("Email Field Is Empty");
            error.setTextFill(Paint.valueOf("red"));
            temail.setBorder(Border.stroke(Paint.valueOf("red")));
            return;
        }
        for (Map<String ,String> map : maps){
            String existingEmail = map.get("email");
            if (email.equals(existingEmail)){
                error.setText("Email already exists");
                error.setTextFill(Paint.valueOf("red"));
                temail.setBorder(Border.stroke(Paint.valueOf("red")));
                return;
            }
        }

        if (ssn == null || ssn.isEmpty()) {
            error.setText("ssn Field Is Empty");
            error.setTextFill(Paint.valueOf("red"));
            tusername.setBorder(Border.stroke(Paint.valueOf("red")));
            return;
        }
        if (ssn.length()!=16) {
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

        String query = "insert into users(username, password, phone, email, SSN, is_active) values ('" + username + "', '" + password + "', '" + phone + "', '" + email + "', '" + ssn + "','" + admins + "')";


        try {
            DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
            boolean result = dataBaseConnection.excute(query);

            if (result) {
                error.setText("user add successfully");
                error.setTextFill(Paint.valueOf("green"));
            } else {
                error.setText("Failed to add User");
                error.setTextFill(Paint.valueOf("red"));
            }
        } catch (Exception e) {
            error.setText("SQL Error: " + e.getMessage());
            error.setTextFill(Paint.valueOf("red"));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
            List<Map<String, String>> users = dataBaseConnection.select("SELECT * FROM users");

            if (users == null || users.isEmpty()) {
                System.err.println("sql databse is null");
                return;
            }

        } catch (Exception e) {
            System.err.println("SQL Error: " + e.getMessage());
        }
    }
}
