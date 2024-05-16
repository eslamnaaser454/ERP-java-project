package app.Log;

import app.Classes.DataBaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class LogController {
    DataBaseConnection dataBaseConnection;
    @FXML
    private VBox cardsContainer;
    @FXML
    private DatePicker dateField;





    public VBox GetLogCart(Map<String,String> log) {

            VBox vbox = new VBox();
            vbox.setPrefWidth(100.0);
            vbox.setStyle("-fx-border-color: rgb(203,203,203);");
            HBox hboxUser = new HBox();
            hboxUser.setStyle("-fx-padding: 5;");
            Label userLabel = new Label("User : ");

            userLabel.setFont(Font.font("System Bold", FontWeight.BOLD, 11));
            Label userNameLabel = new Label(log.get("user"));
            userNameLabel.setTextFill(javafx.scene.paint.Paint.valueOf("#828282"));
            userNameLabel.setFont(Font.font("System Bold", FontWeight.BOLD, 11));
            hboxUser.getChildren().addAll(userLabel, userNameLabel);

            HBox hboxDate = new HBox();
            hboxDate.setStyle("-fx-padding: 5;");
            Label dateLabel = new Label("Date : ");
            dateLabel.setTextFill(Paint.valueOf("#828282"));
            dateLabel.setFont(Font.font("System Bold", FontWeight.BOLD, 11));
            Label dateValueLabel = new Label(log.get("date"));
            dateValueLabel.setTextFill(Paint.valueOf("#828282"));
            dateValueLabel.setFont(Font.font("System Bold", FontWeight.BOLD, 11));
            hboxDate.getChildren().addAll(dateLabel, dateValueLabel);

            HBox hboxAction = new HBox();
            hboxAction.setStyle("-fx-padding: 5;");
            Label actionLabel = new Label("Action : ");
            actionLabel.setTextFill(Paint.valueOf("#828282"));
            actionLabel.setFont(Font.font("System Bold", FontWeight.BOLD, 11));
            Label actionValueLabel = new Label(log.get("content"));
            actionValueLabel.setTextFill(Paint.valueOf("#828282"));
            actionValueLabel.setFont(Font.font("System Bold", FontWeight.BOLD, 11));
            hboxAction.getChildren().addAll(actionLabel, actionValueLabel);

            vbox.getChildren().addAll(hboxUser, hboxDate, hboxAction);

            return vbox;

    }


    public void load_cards(){
        cardsContainer.getChildren().clear();
        dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
        List<Map<String,String>> list = dataBaseConnection.select("select * from log ORDER BY date DESC;");
        for (Map<String,String> log:list){
            cardsContainer.getChildren().add(GetLogCart(log));
        }
    }

  @FXML
public void filter(){
    cardsContainer.getChildren().clear();
    dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
    LocalDate localDate = dateField.getValue();
    if (localDate != null) {
        String formattedDate = localDate.toString(); // This already formats to YYYY-MM-DD

        // Correct the SQL query to properly format the date and compare
        String query = "SELECT * FROM log WHERE strftime('%Y-%m-%d', date) = '" + formattedDate + "';";
        List<Map<String,String>> list = dataBaseConnection.select(query);
        for (Map<String,String> log : list){
            cardsContainer.getChildren().add(GetLogCart(log));
        }
    }
}


}
