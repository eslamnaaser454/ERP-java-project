package app.Suppliers;

import app.Suppliers.Create.CreateSupplierApplication;
import app.Suppliers.Preview.PreviewSupplierApplication;
import app.Suppliers.Proxy.SupplierProxy;
import app.Suppliers.Proxy.SupplierService;
import app.Suppliers.Strategy.SearchByCompany;
import app.Suppliers.Strategy.SearchByEmail;
import app.Suppliers.Strategy.SearchByName;
import app.Suppliers.Strategy.SupplierSearchContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SuppliersController implements Initializable {

    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";

    private final SupplierService supplierService = new SupplierProxy(dbPath);
    private app.Suppliers.Strategy.SupplierSearchStrategy searchStrategy;

    @FXML private ComboBox<String> strategySelector;

    @FXML
    private FlowPane cardsContainer;

    @FXML
    private TextField searchField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        strategySelector.setValue("Name"); // default option selected
        loadCards();
    }


    @FXML
    public void search() {
        String keyword = searchField.getText();
        if (keyword.isEmpty()) return;

        SupplierSearchContext context = new SupplierSearchContext();

        String selectedStrategy = strategySelector.getValue();

        switch (selectedStrategy) {
            case "Company":
                context.setStrategy(new SearchByCompany(supplierService));
                break;
            case "Email":
                context.setStrategy(new SearchByEmail(supplierService));
                break;
            case "Name":
            default:
                context.setStrategy(new SearchByName(supplierService));
                break;
        }

        List<Map<String, String>> results = context.executeSearch(keyword);
        renderCards(results);
    }






    @FXML
    public void reFresh() {
        searchField.setText("");
        loadCards();
    }

    @FXML
    public void GoToCreate() {
        try {
            Stage stage = new Stage();
            stage.setResizable(false);
            new CreateSupplierApplication().start(stage);
        } catch (IOException e) {
            System.out.println("Error loading Add Supplier page: " + e.getMessage());
        }
    }

    private void loadCards() {
        List<Map<String, String>> suppliers = supplierService.getSuppliers();
        displayCards(suppliers);
    }

    private void loadCards(String keyword) {
        List<Map<String, String>> suppliers = supplierService.searchSuppliers(keyword);
        displayCards(suppliers);
    }
    private void renderCards(List<Map<String, String>> list) {
        cardsContainer.getChildren().clear();
        for (Map<String, String> map : list) {
            VBox vbox = new VBox();
            vbox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
            vbox.setPrefWidth(188.0);
            vbox.setStyle("-fx-padding: 5; -fx-background-color: #d7e2fc;-fx-background-radius: 5");

            Label nameLabel = new Label(map.get("name"));
            nameLabel.setMaxWidth(Double.MAX_VALUE);
            nameLabel.setFont(Font.font("System Bold", 12.0));
            nameLabel.setPadding(new Insets(5, 10, 5, 10));

            Label phoneLabel = new Label(map.get("phone"));
            phoneLabel.setMaxWidth(Double.MAX_VALUE);
            phoneLabel.setFont(Font.font("System Bold", 12.0));
            phoneLabel.setPadding(new Insets(5, 10, 5, 10));

            Label emailLabel = new Label(map.get("email"));
            emailLabel.setMaxWidth(Double.MAX_VALUE);
            emailLabel.setFont(Font.font("System Bold", 12.0));
            emailLabel.setPadding(new Insets(5, 10, 5, 10));

            Label companyLabel = new Label(map.get("company"));
            companyLabel.setMaxWidth(Double.MAX_VALUE);
            companyLabel.setFont(Font.font("System Bold", 12.0));
            companyLabel.setPadding(new Insets(5, 10, 5, 10));

            Button previewBtn = new Button("Preview");
            previewBtn.setMaxWidth(Double.MAX_VALUE);
            previewBtn.setPrefHeight(25);
            previewBtn.setStyle("-fx-background-color: #00A1FF;-fx-font-weight: bold");
            previewBtn.setFont(Font.font("System Bold", 12.0));
            previewBtn.setTextFill(Paint.valueOf("white"));
            previewBtn.setOnAction(event -> {
                PreviewSupplierApplication page = new PreviewSupplierApplication(map.get("id"));
                Stage stage = (Stage) cardsContainer.getScene().getWindow();
                try {
                    page.start(stage);
                } catch (IOException e) {
                    System.out.println(e.getCause());
                }
            });

            VBox.setMargin(previewBtn, new Insets(10, 0, 0, 0));
            vbox.getChildren().addAll(nameLabel, phoneLabel, emailLabel, companyLabel, previewBtn);
            cardsContainer.getChildren().add(vbox);
        }
    }


    private void displayCards(List<Map<String, String>> suppliers) {
        cardsContainer.getChildren().clear();

        for (Map<String, String> supplier : suppliers) {
            VBox card = createSupplierCard(supplier);
            cardsContainer.getChildren().add(card);
        }
    }

    private VBox createSupplierCard(Map<String, String> data) {
        VBox vbox = new VBox();
        vbox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        vbox.setPrefWidth(188.0);
        vbox.setStyle("-fx-padding: 5; -fx-background-color: #d7e2fc; -fx-background-radius: 5;");

        Label nameLabel = createLabel(data.get("name"));
        Label phoneLabel = createLabel(data.get("phone"));
        Label emailLabel = createLabel(data.get("email"));
        Label companyLabel = createLabel(data.get("company"));

        Button previewBtn = new Button("Preview");
        previewBtn.setMaxWidth(Double.MAX_VALUE);
        previewBtn.setPrefHeight(25.0);
        previewBtn.setStyle("-fx-background-color: #00A1FF; -fx-font-weight: bold");
        previewBtn.setFont(Font.font("System Bold", 12.0));
        previewBtn.setTextFill(Paint.valueOf("white"));
        previewBtn.setOnAction((ActionEvent event) -> {
            try {
                Stage stage = (Stage) cardsContainer.getScene().getWindow();
                new PreviewSupplierApplication(data.get("id")).start(stage);
            } catch (IOException e) {
                System.out.println("Error previewing supplier: " + e.getMessage());
            }
        });

        VBox.setMargin(previewBtn, new Insets(10, 0, 0, 0));
        vbox.getChildren().addAll(nameLabel, phoneLabel, emailLabel, companyLabel, previewBtn);
        return vbox;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setFont(Font.font("System Bold", 12.0));
        label.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));
        return label;
    }

}
