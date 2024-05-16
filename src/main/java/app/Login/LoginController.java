package app.Login;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import app.Classes.DataBaseConnection;
import app.Classes.Authentication;
import javafx.scene.paint.Paint;
import app.Index.IndexApplication;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {



    public LoginController(){
        System.out.println("Logincontroller Runder");
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
        String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
          String user = (String) this.username.getText();
        String pass = (String) this.password.getText();
        System.out.println("login data");


        System.out.println(dbPath);
        DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
       Authentication authentication = new Authentication(user,pass,dataBaseConnection);
        System.out.println("here");
        if (authentication.check()){
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