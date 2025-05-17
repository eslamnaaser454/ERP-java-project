package app.Index;

import app.Classes.Authentication;
import app.Classes.DataBaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class IndexApplication extends Application {

    public IndexApplication(){
    }

    public static boolean isMaximized = false;

    public void start(Stage stage) throws IOException {
        Authentication auth = new Authentication();
        FXMLLoader fxmlLoader;

        try {
            if(!auth.getUser().get("type").equals("HR")) {
                // Regular user path
                fxmlLoader = new FXMLLoader(getClass().getResource("Index-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1288, 579);
                stage.setTitle("ERP System");
                stage.setResizable(true);
                stage.setScene(scene);
            } else {
                // HR user path
                fxmlLoader = new FXMLLoader(getClass().getResource("indexHr.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1288, 579);
                stage.setTitle("ERP HR System");
                stage.setResizable(true);
                stage.setScene(scene);
            }

            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw so the calling code knows something went wrong
        }

        Authentication authentication = new Authentication();
        System.out.println("Username : "+ authentication.getUsername());

        stage.setOnCloseRequest(event -> {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Exit");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to close the application?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Allow the application to close
                System.out.println("Application is closing...");
                String user = authentication.getUsername();
                boolean username = false; // This represents the is_active status (false)
                String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
                DataBaseConnection dataBaseConnection = new DataBaseConnection(dbPath);

                String query = "UPDATE users SET is_active = '" + username + "' WHERE username = '" + user + "';";
                boolean result1 = dataBaseConnection.execute(query);

                System.out.println(result1);
            } else {
                // Consume the event to prevent closing
                event.consume();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}