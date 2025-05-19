package app.PublicControllers;

import app.Classes.Authentication;
import app.Classes.DataBaseConnection;
import app.HR.index.HRIndexApplication;
import app.Index.IndexApplication;
import app.Login.LoginApplication;
import app.Market.MarketApplication;
import app.Sales.SalesApplication;
import app.Suppliers.SuppliersApplication;
import app.usermanagment.usermanagmentapp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import app.Stores.Index.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import app.Stores.Index.StoreIndexApplication;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

public class SideBarController implements Initializable {
    @FXML
    private Label name;
    @FXML
    private HBox HR;

    @FXML
    HBox OverView;

    @FXML
    HBox UsersNav;
    @FXML
    HBox SalesHbox;
    @FXML
    HBox MarketHBox;
    @FXML
    HBox SuppliersHBox;
    @FXML
    HBox StoresHBox;
    @FXML
    private ImageView Profile_Picture;

    @FXML
    public void GoToOverView(){
        Stage stage = (Stage) OverView.getScene().getWindow();
        IndexApplication nextPage = new IndexApplication();
        try {
            nextPage.start(stage);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void GoToMarket(){
        MarketApplication marketApplication = new MarketApplication();
        Stage stage = (Stage) OverView.getScene().getWindow();
        try {
            marketApplication.start(stage);
        }catch (IOException e){
            System.out.println("exception: "+e.getMessage());
        }
    }

    @FXML
    public void GoToSuppliers(){
        Stage stage = (Stage) OverView.getScene().getWindow();
        SuppliersApplication nextPage = new SuppliersApplication();
        try {
            nextPage.start(stage);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void GoToStoresIndex(){
        StoreIndexApplication storeIndexApplication = new StoreIndexApplication();
        Stage stage = (Stage) OverView.getScene().getWindow();
        try {
            storeIndexApplication.start(stage);
        }catch (IOException e){
            System.out.println(e.getCause());
        }
    }

    @FXML
    public void GotoHR(){
        HRIndexApplication HRIndexApplication = new HRIndexApplication();
        Stage stage = (Stage) OverView.getScene().getWindow();
        stage.setResizable(true);
        try {
            HRIndexApplication.start(stage);
        }catch (IOException e){
            System.out.println(e.getCause());
        }
    }

    @FXML
    private void GoToSales(){
        SalesApplication salesApplication = new SalesApplication();
        Stage stage = (Stage) OverView.getScene().getWindow();
        try {
            salesApplication.start(stage);
        }catch (IOException e){
            System.out.println(e.getCause());
        }
    }

    @FXML
    private void GoToUserManagment(){
        usermanagmentapp Usermanagmentapp = new usermanagmentapp();
        Stage stage = (Stage) OverView.getScene().getWindow();
        try {
            Usermanagmentapp.start(stage);
        }catch (IOException e){
            System.out.println();
        }
    }

    @FXML
    private void GoToLogin(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Are you sure you want to logout?");
        try {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                String dbPath1 = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
                Authentication authentication = new Authentication();

                DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath1);
                String user=authentication.getUser().get("username");
                boolean username = false; // Replace "name" with the actual username
                System.out.println("USERNAME NOW = "  + user);
                String query = "UPDATE users SET is_active = '" + username + "' WHERE username = '" + user + "';";
                boolean result1 = dataBaseConnection.execute(query);
                System.out.println(result1);

                LoginApplication loginApplication = new LoginApplication();
                Stage stage = (Stage) OverView.getScene().getWindow();
                loginApplication.start(stage);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Authentication authentication = new Authentication();
            System.out.println("TYPE =  "+authentication.getUser().get("type"));
            name.setText("Hello, "+authentication.getUser().get("username"));
            String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
            String url = "jdbc:sqlite:" + dbPath;

            String query = "SELECT Image_Path FROM users WHERE username = ?";
            try (Connection conn = DriverManager.getConnection(url);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, authentication.getUser().get("username"));

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String imagePath = rs.getString("Image_Path");

                    // التحقق من أن imagePath ليست null وليست فارغة
                    if (imagePath != null && !imagePath.trim().isEmpty()) {
                        File imageFile = new File(imagePath);

                        if (imageFile.exists()) {
                            Image image = new Image(imageFile.toURI().toString());
                            Profile_Picture.setImage(image);

// اضبط أبعاد الـ ImageView (ممكن تكون أكبر من الدائرة للظهور بشكل أفضل)
                            Profile_Picture.setFitWidth(80);  // ممكن تعدل حسب الحاجة
                            Profile_Picture.setFitHeight(80);
                            Profile_Picture.setPreserveRatio(true);

// أنشئ دائرة clip بالأبعاد المطلوبة (radius = نصف العرض/الارتفاع)
                            double clipRadiusX = 39 / 2.0;
                            double clipRadiusY = 37 / 2.0;

// لأن Circle تأخذ نصف قطر واحد فقط، نستخدم أقل نصف قطر لضمان تناسق الشكل الدائري (تقريبياً)
                            double clipRadius = Math.min(clipRadiusX, clipRadiusY);

                            Circle clip = new Circle(Profile_Picture.getFitWidth() / 2, Profile_Picture.getFitHeight() / 2, clipRadius);
                            Profile_Picture.setClip(clip);

// تخزين الحجم الأصلي للقطر حتى نستخدمه في hover
                            final double originalRadius = clipRadius;
                            final double hoverRadius = originalRadius * 1.5;  // مثلا 1.5 مرة أكبر

// حدث عند المرور على الصورة (hover)
                            Profile_Picture.setOnMouseEntered(e -> {
                                clip.setRadius(hoverRadius);
                            });

// حدث عند الخروج من الصورة (hover out)
                            Profile_Picture.setOnMouseExited(e -> {
                                clip.setRadius(originalRadius);
                            });} else {
                            System.out.println("Image file not found: " + imagePath);
                        }
                    } else {
                        System.out.println("No image path set for user.");
                    }
                }
            // Add null check for UsersNav
            if (UsersNav != null && authentication.getUser().get("is_super_user").equals("false")) {
                System.out.println("Equal");
                UsersNav.setVisible(false);
            } else {
                System.out.println("Not Equal "+authentication.getUser().get("is_super_user"));
                // If UsersNav is null, log it
                if (UsersNav == null) {
                    System.out.println("UsersNav ");
                }
            }
        } }
        catch (Exception e) {
            System.err.println("Error in SideBarController initialize: " + e.getMessage());
            e.printStackTrace();
        }

}}