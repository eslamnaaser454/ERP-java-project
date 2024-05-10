package app.Suppliers;

import app.Classes.DataBaseConnection;
import app.Suppliers.Create.CreateSupplierApplication;
import app.Suppliers.Preview.PreviewSupplierApplication;
import app.Suppliers.Preview.PreviewSupplierController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class SuppliersController implements Initializable {
    private DataBaseConnection dataBaseConnection;
    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
    @FXML
    private FlowPane cardsContainer;

    @FXML
    private TextField searchField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       LoadCards();

    }
    @FXML
    public void search(){
        String text = (String) searchField.getText();
        if (!text.isEmpty()){
            LoadCards(text);
        }
    }
    @FXML
    public void reFresh(){
        searchField.setText("");
        LoadCards();
    }

    @FXML
    public void GoToCreate(){
        Stage stage = new Stage();
        stage.setResizable(false);
        CreateSupplierApplication nextPage = new CreateSupplierApplication();
        try{
            nextPage.start(stage);
        }catch (IOException e){
            System.out.println("Error At going to Add supplier Page");
        }
    }

    private void LoadCards(){
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String,String>> list = dataBaseConnection.select("select * from supplier;");
        if (list != null){
            cardsContainer.getChildren().clear();
            for (Map<String,String> map:list){
                VBox vbox = new VBox();
                vbox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
                vbox.setPrefWidth(188.0);
                vbox.setStyle("-fx-padding: 5; -fx-background-color: #d7e2fc;");

                Label nameLabel = new Label();
                nameLabel.setMaxWidth(Double.MAX_VALUE);
                nameLabel.setFont(Font.font("System Bold", 12.0));
                nameLabel.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));
                nameLabel.setOpaqueInsets(new Insets(5.0, 10.0, 5.0, 10.0));

                Label phoneLabel = new Label();
                phoneLabel.setMaxWidth(Double.MAX_VALUE);
                phoneLabel.setFont(Font.font("System Bold", 12.0));
                phoneLabel.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));
                phoneLabel.setOpaqueInsets(new Insets(5.0, 10.0, 5.0, 10.0));

                Label emailLabel = new Label();
                emailLabel.setMaxWidth(Double.MAX_VALUE);
                emailLabel.setFont(Font.font("System Bold", 12.0));
                emailLabel.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));
                emailLabel.setOpaqueInsets(new Insets(5.0, 10.0, 5.0, 10.0));

                Label companyLabel = new Label();
                companyLabel.setMaxWidth(Double.MAX_VALUE);
                companyLabel.setFont(Font.font("System Bold", 12.0));
                companyLabel.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));
                companyLabel.setOpaqueInsets(new Insets(5.0, 10.0, 5.0, 10.0));

                Button previewBtn = new Button("Preview");
                previewBtn.setMaxWidth(Double.MAX_VALUE);
                previewBtn.setPrefHeight(25.0);
                previewBtn.setStyle("-fx-background-color: blue;");
                previewBtn.setFont(Font.font("System Bold", 12.0));
                previewBtn.setTextFill(Paint.valueOf("white"));
                previewBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        PreviewSupplierApplication page = new PreviewSupplierApplication(map.get("id"));
                        Stage stage = (Stage) cardsContainer.getScene().getWindow();
                        try {
                            page.start(stage);
                        }catch (IOException e){
                            System.out.println(e.getCause());
                        }
                    }
                });
                VBox.setMargin(previewBtn, new Insets(10.0, 0, 0, 0));

                vbox.getChildren().addAll(nameLabel, phoneLabel, emailLabel, companyLabel, previewBtn);
                nameLabel.setText(map.get("name"));
                phoneLabel.setText(map.get("phone"));
                emailLabel.setText(map.get("email"));
                companyLabel.setText(map.get("company"));
                cardsContainer.getChildren().add(vbox);

            }

        }
    }

    private void LoadCards(String search){
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String,String>> list = dataBaseConnection.select("select * from supplier where name like '"+search+"%';");
        if (list != null){
            cardsContainer.getChildren().clear();
            for (Map<String,String> map:list){
                VBox vbox = new VBox();
                vbox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
                vbox.setPrefWidth(188.0);
                vbox.setStyle("-fx-padding: 5; -fx-background-color: #d7e2fc;");

                Label nameLabel = new Label();
                nameLabel.setMaxWidth(Double.MAX_VALUE);
                nameLabel.setFont(Font.font("System Bold", 12.0));
                nameLabel.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));
                nameLabel.setOpaqueInsets(new Insets(5.0, 10.0, 5.0, 10.0));

                Label phoneLabel = new Label();
                phoneLabel.setMaxWidth(Double.MAX_VALUE);
                phoneLabel.setFont(Font.font("System Bold", 12.0));
                phoneLabel.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));
                phoneLabel.setOpaqueInsets(new Insets(5.0, 10.0, 5.0, 10.0));

                Label emailLabel = new Label();
                emailLabel.setMaxWidth(Double.MAX_VALUE);
                emailLabel.setFont(Font.font("System Bold", 12.0));
                emailLabel.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));
                emailLabel.setOpaqueInsets(new Insets(5.0, 10.0, 5.0, 10.0));

                Label companyLabel = new Label();
                companyLabel.setMaxWidth(Double.MAX_VALUE);
                companyLabel.setFont(Font.font("System Bold", 12.0));
                companyLabel.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));
                companyLabel.setOpaqueInsets(new Insets(5.0, 10.0, 5.0, 10.0));

                Button previewBtn = new Button("Preview");
                previewBtn.setMaxWidth(Double.MAX_VALUE);
                previewBtn.setPrefHeight(25.0);
                previewBtn.setStyle("-fx-background-color: blue;");
                previewBtn.setFont(Font.font("System Bold", 12.0));
                previewBtn.setTextFill(Paint.valueOf("white"));
                previewBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        PreviewSupplierApplication page = new PreviewSupplierApplication(map.get("id"));
                        Stage stage = (Stage) cardsContainer.getScene().getWindow();
                        try {
                            page.start(stage);
                        }catch (IOException e){
                            System.out.println(e.getCause());
                        }
                    }
                });

                VBox.setMargin(previewBtn, new Insets(10.0, 0, 0, 0));

                vbox.getChildren().addAll(nameLabel, phoneLabel, emailLabel, companyLabel, previewBtn);
                nameLabel.setText(map.get("name"));
                phoneLabel.setText(map.get("phone"));
                emailLabel.setText(map.get("email"));
                companyLabel.setText(map.get("company"));
                cardsContainer.getChildren().add(vbox);

            }

        }
    }

}
