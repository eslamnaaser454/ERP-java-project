package app.Index;

import app.Classes.Authentication;
import app.Classes.DataBaseConnection;
import app.Login.LoginApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import app.Stores.Index.StoreIndexApplication;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


public class IndexController implements Initializable {

    private Authentication authentication;
    @FXML
    private HBox sideBar;
//    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
//    private DataBaseConnection dataBaseConnection;
//    public void updateEmployeeCount() {
//        dataBaseConnection = new DataBaseConnection(dbPath);
//        List<Map<String, String>> result = dataBaseConnection.select("SELECT * from staff;");
//        if (!result.isEmpty()) {
//            String count = (result.size()+1)+"";
//            empCount.setText(count);
//        }
//    }

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


    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
        sideBar = null;
    }
}