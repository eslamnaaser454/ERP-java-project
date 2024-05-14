package app.Index;

import app.Classes.Authentication;
import app.Login.LoginApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import app.Stores.Index.StoreIndexApplication;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class IndexController implements Initializable {

    private Authentication authentication;
    @FXML
    private VBox sideBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        if (!authentication.check()){
//            Stage stage = (Stage) sideBar.getScene().getWindow();
//            LoginApplication loginApplication = new LoginApplication();
//            stage.setResizable(false);
//            try {
//                loginApplication.start(stage);
//
//            }catch (IOException e){
//                System.out.println(e.toString());
//            }
//        }
    }


  
}