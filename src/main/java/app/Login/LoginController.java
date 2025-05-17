package app.Login;

import app.Login.Factory.AppUser;
import app.Login.Factory.UserFactory;
import app.usermanagment.Createuser.SecureAES;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import app.Classes.DataBaseConnection;
import app.Classes.Authentication;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;
import app.Index.IndexApplication;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LoginController {



    public LoginController(){
    }
    @FXML
    protected Label errorMsg;
    @FXML
    protected TextField username;


    @FXML
    protected PasswordField password;

    @FXML
    protected Button loginBtn;
    @FXML
    protected void LoginEvent() {

          String dbPath1 = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";

        DataBaseConnection dataBaseConnectionss=new DataBaseConnection(dbPath1);

        String user = (String) this.username.getText();
        String pass = (String) this.password.getText();
        String mytype = "SELECT type FROM users WHERE username = '" + user + "'";
        List<Map<String, String>> result = dataBaseConnectionss.select(mytype);
        String userType = result.getFirst().get("type");

        AppUser appUser = UserFactory.createUser(userType);
        pass = appUser.processPassword(pass);




        String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";




        DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
        Authentication authentication = new Authentication(user,pass,dataBaseConnection);

        if (authentication.check()){

            String pre_query = "SELECT is_active FROM users WHERE username = '" + user + "';";
            boolean pre_result = dataBaseConnection.execute(pre_query);
            String myresult=dataBaseConnectionss.select(pre_query).getFirst().get("is_active");

            if(myresult.equals("true")|| myresult.equals("1")){
                errorMsg.setText("User Already Logged In");
                errorMsg.setTextFill(Paint.valueOf("red"));
                return;
            }
            boolean username = true; // Replace "name" with the actual username

            String query = "UPDATE users SET is_active = '" + username + "' WHERE username = '" + user + "';";


            IndexApplication indexApplication = new IndexApplication();

            Stage stage = (Stage) errorMsg.getScene().getWindow();
            stage.setResizable(true);
//                Stage stage = new Stage();


            try {
                indexApplication.start(stage);


            }catch (IOException e){
                System.out.println(e.getMessage());
            }



        }else {

            errorMsg.setText("Wrong User Or Password");
            errorMsg.setTextFill(Paint.valueOf("red"));
        }


    }
}