package app.usermanagment.Createuser;

import app.Classes.DataBaseConnection;
import app.Classes.Logging;
import app.usermanagment.Createuser.UserBuilder.User;
import app.usermanagment.service.UserService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class CreateuserConroller implements Initializable {
    String selected = "User";
    String Department_name = "HR";
    private double originalWidth;
    private double originalHeight;
    private String currentImagePath;
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
    @FXML
    private Button Upload;
    @FXML
    private ImageView Image_View;

    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
    private UserService userService;

    @FXML
    public void add() {

        if (selected.equals("HR")) {
            Department_name = null;
        } else {
            Department_name = Department.getSelectionModel().getSelectedItem();
        }
        System.out.println(Department_name + "\t" + selected);
        String username = tusername.getText();
        String password = tpassword.getText();
        String phone = tphone.getText();
        String email = temail.getText();
        String ssn = tssn.getText();
        String usernameT = tusername.getText();
        boolean isAdmin = selected.equals("HR");

        // Validation checks
        if (username == null || username.isEmpty()) {
            error.setText("Username Field Is Empty");
            error.setTextFill(Paint.valueOf("red"));
            tusername.setBorder(Border.stroke(Paint.valueOf("red")));
            return;
        }
        String usernamee = "select username from users";
        DataBaseConnection dataBaseConnectionssUsername = new DataBaseConnection(dbPath);
        List<Map<String, String>> maps1p = dataBaseConnectionssUsername.select(usernamee);
        for (Map<String, String> map : maps1p) {
            String user = map.get("username");
            if (username.equals(user)) {
                error.setText("user name is exist");
                error.setTextFill(Paint.valueOf("red"));
                tusername.setBorder(Border.stroke(Paint.valueOf("red")));
                return;
            }
        }

        String ssnn = "select ssn from users";
        DataBaseConnection dataBaseConnectionssSSN = new DataBaseConnection(dbPath);
        List<Map<String, String>> maps2p = dataBaseConnectionssSSN.select(ssnn);
        for (Map<String, String> map : maps2p) {
            String ssn1 = map.get("SSN");
            if (ssn.equals(ssn1)) {
                error.setText("SSN is exist");
                error.setTextFill(Paint.valueOf("red"));
                tssn.setBorder(Border.stroke(Paint.valueOf("red")));
                return;
            }
        }
        String phonee = "select phone from users";
        DataBaseConnection dataBaseConnectionssPhone = new DataBaseConnection(dbPath);
        List<Map<String, String>> maps3p = dataBaseConnectionssPhone.select(phonee);
        for (Map<String, String> map : maps3p) {
            String phone1 = map.get("phone");
            if (phone.equals(phone1)) {
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
        if (password.length() < 8 || password.length() > 16) {
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
        if (phone.length() < 11) {
            error.setText("Phone Field Is greater 11 ");
            error.setTextFill(Paint.valueOf("red"));
            tphone.setBorder(Border.stroke(Paint.valueOf("red")));
            return;
        }
        if (!phone.matches("\\d+")) {
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
        String emaill = "select email from users";
        DataBaseConnection dataBaseConnectionssEmail = new DataBaseConnection(dbPath);
        List<Map<String, String>> maps = dataBaseConnectionssEmail.select(emaill);
        for (Map<String, String> map : maps) {
            String existingEmail = map.get("email");
            if (email.equals(existingEmail)) {
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
        if (ssn.length() != 14) {
            error.setText("ssn Field must be  14 digits");
            error.setTextFill(Paint.valueOf("red"));
            tssn.setBorder(Border.stroke(Paint.valueOf("red")));
            return;
        }

        // Create the User object using the Builder
        User.UserBuilder userBuilder = new User.UserBuilder(username, password, phone, email, ssn)
                .isSuperUser(isAdmin)
                .type(selected);

        if (selected.equals("User")) {
            if (Department_name == null) {
                error.setText("You must select department");
                error.setTextFill(Paint.valueOf("red"));
                Department.setBorder(Border.stroke(Paint.valueOf("red")));
                return;
            }
            userBuilder.department(Department_name);
        }

        User newUser = userBuilder.build();

        if (userService.addUser(newUser)) {
if(currentImagePath!=null) {
    String username1 = tusername.getText();// عدل حسب معرف المستخدم الحالي
    saveImagePathToDatabase(currentImagePath, username1);

}
            error.setText("user add successfully");
            error.setTextFill(Paint.valueOf("green"));
            Logging logging = new Logging();
            logging.addLog("User with name " + newUser.getUsername() + " has been Added to the system");
            // Optionally clear the form fields here
            tusername.clear();
            tpassword.clear();
            tphone.clear();
            temail.clear();
            tssn.clear();
            Department.setValue("Department");

            Image_View.setImage(null);
            currentImagePath = null;
        } else {
            error.setText("Failed to add User (Username, Email, Phone, or SSN might already exist)");
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
    public void initialize(URL location, ResourceBundle resources) {
        Department.setPromptText("Department");

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
                    ex.printStackTrace();
                }
            }
        });
        userService = new UserService(dbPath);
        Department_names departmentDAO = new Department_names();
        List<String> departmentNames = departmentDAO.getDepartmentNames();
        Department.setItems(FXCollections.observableArrayList(departmentNames));

        user_type.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton selectedRadioButton = (RadioButton) newValue;
                selected = selectedRadioButton.getText();
                Department.setVisible(!selected.equals("HR"));
                if (selected.equals("HR")) {
                    Department_name = null;
                    Department.getSelectionModel().select(null);
                } else {
                    Department_name = Department.getSelectionModel().getSelectedItem();
                }
            }
        });

        if (selected.equals("HR")) {
            Department_name = "HR";
            Department.getSelectionModel().select(null);
        } else {
            Department_name = Department.getSelectionModel().getSelectedItem();
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