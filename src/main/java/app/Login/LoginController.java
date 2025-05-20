package app.Login;

import app.Login.Factory.AppUser;
import app.Login.Factory.UserFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import app.Classes.DataBaseConnection;
import app.Classes.Authentication;
import app.Index.IndexApplication;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LoginController {

    @FXML
    private Label errorMsg;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginBtn;

    @FXML
    protected void LoginEvent() {
        String userInput = username.getText();
        String passInput = password.getText();

        String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
        DataBaseConnection dbConnection = new DataBaseConnection(dbPath);

        // Step 1: Validate username exists
        String userCheckQuery = "SELECT * FROM users WHERE username = '" + userInput + "'";
        List<Map<String, String>> userResult = dbConnection.select(userCheckQuery);

        if (userResult == null || userResult.isEmpty()) {
            showError("Username not found");
            return;
        }

        // Step 2: Get user type
        String userTypeQuery = "SELECT type FROM users WHERE username = '" + userInput + "'";
        String userType = dbConnection.select(userTypeQuery).getFirst().get("type");

        // Step 3: Process password via Factory
        AppUser user = UserFactory.createUser(userType);
        String processedPassword = user.processPassword(passInput);

        // Step 4: Authenticate
        Authentication auth = new Authentication(userInput, processedPassword, dbConnection);

        if (auth.check()) {
            // Step 5: Check if already logged in
            String activeCheckQuery = "SELECT is_active FROM users WHERE username = '" + userInput + "'";
            String isActive = dbConnection.select(activeCheckQuery).getFirst().get("is_active");

            if (isActive.equals("true") || isActive.equals("1")) {
                showError("User already logged in");
                return;
            }

            // Step 6: Mark user as active
            String activateUserQuery = "UPDATE users SET is_active = 1 WHERE username = '" + userInput + "'";
            dbConnection.execute(activateUserQuery);

            // Step 7: Load main application
            openHomePage();

        } else {
            showError("Wrong username or password");
        }
    }

    private void showError(String message) {
        errorMsg.setText(message);
        errorMsg.setTextFill(Paint.valueOf("red"));
    }

    private void openHomePage() {
        try {
            IndexApplication app = new IndexApplication();
            Stage stage = (Stage) errorMsg.getScene().getWindow();
            stage.setResizable(true);
            app.start(stage);
        } catch (IOException e) {
            showError("Failed to open home page");
        }
    }
}
