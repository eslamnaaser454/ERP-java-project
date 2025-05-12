package app.usermanagment.Createuser;

import app.Classes.DataBaseConnection;
import app.Classes.Logging;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;//java new

import java.net.URL;
import java.util.*;

public class CreateuserConroller implements Initializable {
String selected="User";
String Department_name="HR";
    @FXML
    private ComboBox<String> Department;

    @FXML
    private RadioButton HR;

    @FXML
    private Button add;

    @FXML
    private RadioButton admin;

    @FXML
    private Label error;
    @FXML
    TextField tusername;
    @FXML
    private Label lemail;

    @FXML
    private Label lpassword;

    @FXML
    private Label lphone;

    @FXML
    private Label lssn;

    @FXML
    private Label lusername;

    @FXML
    private TextField temail;

    @FXML
    private TextField tpassword;

    @FXML
    private TextField tphone;

    @FXML
    private TextField tssn;


    @FXML
    private RadioButton user;

    @FXML
    private ToggleGroup user_type;

    private Map<String, String> element;
    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
    @FXML
    public void add() {
        if(selected.equals("HR")){
            Department_name=null;

        }
        else {
            Department_name=Department.getSelectionModel().getSelectedItem();
        }
        System.out.println(Department_name+ "\t"+selected);
        String type = "";
        String username = tusername.getText();
        String password = tpassword.getText();
        String phone = tphone.getText();
        String email = temail.getText();
        String ssn =   tssn.getText();
        String usernameT = tusername.getText();
        boolean admins=selected.equals("HR");;


        String emaill ="select email from users";
        String usernamee="select username from users";
        String ssnn="select ssn from users";
        String phonee="select phone from users";
        DataBaseConnection dataBaseConnectionss=new DataBaseConnection(dbPath);
        List <Map<String,String>>maps= dataBaseConnectionss.select(emaill);
        List <Map<String,String>>maps1p= dataBaseConnectionss.select(usernamee);
        List <Map<String,String>>maps2p= dataBaseConnectionss.select(ssnn);
        List <Map<String,String>>maps3p= dataBaseConnectionss.select(phonee);


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

        for (Map<String,String> map:maps2p){

            String ssn1=map.get("SSN");

            if (ssn.equals(ssn1)){
                error.setText("SSN is exist");
                error.setTextFill(Paint.valueOf("red"));
                tssn.setBorder(Border.stroke(Paint.valueOf("red")));
                return;
            }
        }
        for (Map<String,String> map:maps3p){

            String phone1=map.get("phone");

            if (phone.equals(phone1)){
                error.setText("Phone is exist");
                error.setTextFill(Paint.valueOf("red"));
                tphone.setBorder(Border.stroke(Paint.valueOf("red")));
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
            tssn.setBorder(Border.stroke(Paint.valueOf("red")));

            return;
        }
        if (ssn.length()!=14) {
            error.setText("ssn Field must be  14 digits");
            error.setTextFill(Paint.valueOf("red"));
            tssn.setBorder(Border.stroke(Paint.valueOf("red")));
            return;
        }
        if(selected.equals("HR")){

            try {

                password= SecureAES.encrypt(password);
                System.out.println("Encrypted: " + password);

                String decrypted = SecureAES.decrypt(password);
                System.out.println("Decrypted: " + decrypted);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
       else if(Department_name==null){

            error.setText("You must select department");
            error.setTextFill(Paint.valueOf("red"));
            Department.setBorder(Border.stroke(Paint.valueOf("red")));
            return;
        }

        DataBaseConnection dataBaseConnections = new DataBaseConnection(dbPath);
        List<Map<String,String>> SSN= dataBaseConnections.select("SELECT * FROM users where SSN ='"+ssn+"' ");
        if (SSN==null||SSN.isEmpty()){
            error.setText("ssn is null");
            error.setTextFill(Paint.valueOf("red"));
        }



        String query = "INSERT INTO users(username, password, phone, email, SSN, is_super_user, type, Department,is_active) " +
                "VALUES ('" + username + "', '" + password + "', '" + phone + "', '" + email + "', '" +
                ssn + "', '" + admins + "', '" + selected + "', '" + Department_name +"', '"+false+ "')";


        try {
            DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
            boolean result = dataBaseConnection.execute(query);

            if (result) {

                System.out.println(admins);
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
        Logging logging = new Logging() ;
        logging.addLog("User with name "+usernameT+" has been Added to the system");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Department_names departmentDAO = new Department_names();

        List<String> departmentNames = departmentDAO.getDepartmentNames();

        Department.setItems(FXCollections.observableArrayList(departmentNames));


     //   Department.getSelectionModel().selectFirst();
        System.out.println(Department.getSelectionModel().getSelectedItem());

        user_type.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton selectedRadioButton = (RadioButton) newValue;
                System.out.println("Selected: " + selectedRadioButton.getText());
            selected=selectedRadioButton.getText();
                Department.setVisible(!selected.equals("HR"));
           }
        });
        if(selected.equals("HR")){
           Department_name="HR";
Department.getSelectionModel().select(null);
        }
        else {
            Department_name=Department.getSelectionModel().getSelectedItem();
        }

        try {
            DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
            List<Map<String, String>> users = dataBaseConnection.select("SELECT * FROM users");
            error.setText("");
            if (users == null || users.isEmpty()) {
                System.err.println("sql databse is null");
                return;
            }

        } catch (Exception e) {
            System.err.println("SQL Error: " + e.getMessage());
        }
    }
}
