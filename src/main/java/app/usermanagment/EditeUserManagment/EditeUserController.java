package app.usermanagment.EditeUserManagment;

import app.Classes.Authentication;
import app.Classes.DataBaseConnection;
import app.usermanagment.Createuser.SecureAES;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class EditeUserController implements Initializable {
    private  String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
    private String originalUsername;
    private String originalPassword;
    private String originalPhone;
    private String originalEmail;
    private String originalSSN;
    private boolean originalIsAdmin;
    private double originalWidth;
    private double originalHeight;
    private String currentImagePath;

    @FXML
    private ImageView Image_View;

    @FXML
    private Button Upload;
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

    public void edit() throws Exception {
        String username= tusername.getText();
        String password=tpassword.getText();
        String phone =tphone.getText();
        String email=temail.getText();
        String ssn=tssn.getText();

        String emaill ="select email from users";
        String usernamee="select username from users";
        String ssnn="select ssn from users";
        String phonee="select phone from users";
        DataBaseConnection dataBaseConnectionss=new DataBaseConnection(dbPath);
        List <Map<String,String>>maps= dataBaseConnectionss.select(emaill);
        List <Map<String,String>>maps1p= dataBaseConnectionss.select(usernamee);
        List <Map<String,String>>maps2p= dataBaseConnectionss.select(ssnn);
        List <Map<String,String>>maps3p= dataBaseConnectionss.select(phonee);

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
        if (password.length()<8) {
            error.setText("Password Field should be at least 8");
            error.setTextFill(Paint.valueOf("red"));
            tpassword.setBorder(Border.stroke(Paint.valueOf("red")));
            return;
        }
        if(admin.isSelected()){
            password= SecureAES.encrypt(password);
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
        if (ssn.length()!= 14) {
            error.setText("ssn Field must be gretar than 14 ");
            error.setTextFill(Paint.valueOf("red"));
            tssn.setBorder(Border.stroke(Paint.valueOf("red")));
            return;
        }
        for (Map<String ,String> map : maps){

            String existingEmail = map.get("email");

            if (email.equals(existingEmail)&&!email.equals(originalEmail)){
                error.setText("Email already exists");
                error.setTextFill(Paint.valueOf("red"));
                temail.setBorder(Border.stroke(Paint.valueOf("red")));
                return;
            }
        }
        for (Map<String,String> map:maps1p){

            String user=map.get("username");

            if (username.equals(user)&&!username.equals(originalUsername)){
                error.setText("user name is exist");
                error.setTextFill(Paint.valueOf("red"));
                tusername.setBorder(Border.stroke(Paint.valueOf("red")));
                return;
            }
        }

        for (Map<String,String> map:maps2p){

            String ssn1=map.get("SSN");

            if (ssn.equals(ssn1)&&!ssn.equals(originalSSN)){
                error.setText("SSN is exist");
                error.setTextFill(Paint.valueOf("red"));
                tssn.setBorder(Border.stroke(Paint.valueOf("red")));
                return;
            }
        }
        for (Map<String,String> map:maps3p){

            String phone1=map.get("phone");

            if (phone.equals(phone1)&&!phone.equals(originalPhone)){
                error.setText("Phone is exist");
                error.setTextFill(Paint.valueOf("red"));
                tphone.setBorder(Border.stroke(Paint.valueOf("red")));
                return;
            }
        }

        DataBaseConnection dataBaseConnections = new DataBaseConnection(dbPath);
        List<Map<String,String>> SSN= dataBaseConnections.select("SELECT * FROM users where SSN ='"+ssn+"' ");
        if (SSN==null||SSN.isEmpty()){
            error.setText("ssn is null");
            error.setTextFill(Paint.valueOf("red"));
        }
        String query = "update users set username = '" +  username+ "', password = '" +password+ "', phone = '" +phone+ "', email= '" +email+ "', SSN = '" +ssn+ "'  where id = " + id + ";";
        DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
        if(currentImagePath!=null) {
            String username1 = tusername.getText();// عدل حسب معرف المستخدم الحالي
            saveImagePathToDatabase(currentImagePath, username1);

        }

        boolean result = dataBaseConnection.execute(query);

        if (result==true) {
            error.setText("User Updated Successfully");
            error.setTextFill(Paint.valueOf("green"));
        } else {
            error.setText("Failed to update User");
            error.setTextFill(Paint.valueOf("red"));
        }
    }

    private void saveImagePathToDatabase(String imagePath, String username) {
        String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";

        String url = "jdbc:sqlite:" + dbPath;

        String updateSQL = "UPDATE users SET Image_Path = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            pstmt.setString(1, imagePath);
            pstmt.setString(2, username);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Image path saved successfully!");
            } else {
                System.out.println("No user updated. Check user ID.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


//            load_data();

    }
    public void load_data() throws Exception {
        DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String, String>> user = dataBaseConnection.select("select * from users where id = " + id + ";");

        if (user == null || user.isEmpty()) {
            System.err.println("No user found for ID: " + id);
            return;
        }

        Map<String, String> users = user.getFirst();

        tusername.setText(users.get("username"));
        tpassword.setText(users.get("password"));
        temail.setText(users.get("email"));
        tphone.setText(users.get("phone"));
        tssn.setText(users.get("SSN"));
        // Store original values
        originalUsername = users.get("username");
        originalPassword = users.get("password");
        originalEmail = users.get("email");
        originalPhone = users.get("phone");
        originalSSN = users.get("SSN");
        String imagePath = users.get("Image_Path");
        if (imagePath != null && !imagePath.trim().isEmpty()) {
            File imageFile = new File(imagePath);

            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                Image_View.setImage(image);
        originalWidth = Image_View.getFitWidth();
        originalHeight = Image_View.getFitHeight();
        Image_View.setOnMouseEntered(e1 -> {
            Image_View.setFitWidth(originalWidth * 5);
            Image_View.setFitHeight(originalHeight * 5);
        });
        Image_View.setOnMouseExited(e1 -> {
            Image_View.setFitWidth(originalWidth);
            Image_View.setFitHeight(originalHeight);
        });

            }
        }
        Upload.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose a Profile Picture");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );

            File selectedFile = fileChooser.showOpenDialog(Upload.getScene().getWindow());
            if (selectedFile != null) {
                try {
                    // عرض الصورة
                    Image image = new Image(selectedFile.toURI().toString());

                    Image_View.setImage(image);
                    originalWidth = Image_View.getFitWidth();
                    originalHeight = Image_View.getFitHeight();
                    Image_View.setOnMouseEntered(e1 -> {
                        Image_View.setFitWidth(originalWidth * 5);
                        Image_View.setFitHeight(originalHeight * 5);
                    });

                    Image_View.setOnMouseExited(e1 -> {
                        Image_View.setFitWidth(originalWidth);
                        Image_View.setFitHeight(originalHeight);
                    });
                    // حفظ المسار في قاعدة البيانات

                    currentImagePath = selectedFile.getAbsolutePath();


                } catch (Exception ex) {
                    System.out.println("Error loading image: " + ex.getMessage());
                }

            }
        });

        // Handle admin checkbox and password field
        String isAdmin = users.get("type");
        if(isAdmin.equals("HR")){
            isAdmin = "true";}
        admin.setSelected(isAdmin.equalsIgnoreCase("true") || isAdmin.equals("1"));
        // This is the correct field
        Authentication authentication = new Authentication();
        System.out.println("Username FROM EDIT : "+ authentication.getUsername());
        System.out.println("Username FROM EDIT : "+ tusername.getText());
        System.out.println(authentication.getUsername() +"   NEXT    "+ tusername.getText());
        if (("1".equals(isAdmin) || "true".equalsIgnoreCase(isAdmin))) {




            tpassword.setDisable(true);
            if(authentication.getUsername().equals(tusername.getText())){
                tpassword.setDisable(false);

                try {
                    tpassword.setText(SecureAES.decrypt(tpassword.getText()));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

            System.out.println("MASTER IS CALL");
        }
    }



}