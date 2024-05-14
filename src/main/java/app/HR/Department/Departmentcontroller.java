package app.HR.Department;

import app.HR.Department.Add.AddDepartmentApplication;
import app.HR.Department.Edit.EditDepartmentApplication;
import app.HR.Manage.Edite.EditeEmployessApplication;
import app.HR.Manage.add.AddEmployessApplication;
import app.Stores.Manage.StoreManageController;
import app.Suppliers.Preview.PreviewSupplierApplication;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import app.Classes.DataBaseConnection;
import app.Stores.Manage.Create.CreateStoreFormApplication;
import app.Stores.Manage.Edite.EditeStoreFormApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import java.net.URL;
import java.util.ResourceBundle;

public class Departmentcontroller implements Initializable {
    private DataBaseConnection dataBaseConnection;
    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
    @FXML
    private FlowPane cardsContainer;
    @FXML
    private TextField searchField;

    public void LoadCards() {
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String, String>> list = dataBaseConnection.select("select * from department;");
        if (list != null) {
            cardsContainer.getChildren().clear();
            for (Map<String, String> map : list) {
                VBox vbox = new VBox();
                vbox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
                vbox.setPrefWidth(200.0);
                vbox.setStyle("-fx-padding: 10; -fx-background-color: #e0e0e0; -fx-border-color: #c0c0c0; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5;");

                Label nameLabel = new Label();
                nameLabel.setMaxWidth(Double.MAX_VALUE);
                nameLabel.setAlignment(javafx.geometry.Pos.CENTER);
                nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14.0));
                nameLabel.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));

                Label id = new Label();
                id.setMaxWidth(Double.MAX_VALUE);
                id.setAlignment(javafx.geometry.Pos.CENTER);
                id.setFont(Font.font("Arial", 12.0));
                id.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));

                Button Edit = new Button("Edit");
                Edit.setMaxWidth(Double.MAX_VALUE);
                Edit.setPrefHeight(30.0);
                Edit.setStyle("-fx-background-color: #4a86e8; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
                Edit.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));
                Edit.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        EditDepartmentApplication page = new EditDepartmentApplication(map.get("id"));
                        System.out.println(map.get("id"));
                        Stage stage = new Stage();
                        try {
                            page.start(stage);
                        } catch (IOException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Dialog");
                            alert.setHeaderText("An Error Occurred");
                            alert.setContentText(e.getMessage());
                            alert.showAndWait();
                        }
                    }
                });
                VBox.setMargin(Edit, new Insets(10.0, 0, 0, 0));

                vbox.getChildren().addAll(nameLabel, id, Edit);
                nameLabel.setText(map.get("name"));
                id.setText("id: " + map.get("id"));

                cardsContainer.getChildren().add(vbox);

            }

        }
    }

    private void LoadCards(String search) {
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String, String>> list = dataBaseConnection.select("select * from department where name like '" + search + "%';");

        if (list != null) {
            cardsContainer.getChildren().clear();
            for (Map<String, String> map : list) {
                VBox vbox = new VBox();
                vbox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
                vbox.setPrefWidth(200.0);
                vbox.setStyle("-fx-padding: 10; -fx-background-color: #e0e0e0; -fx-border-color: #c0c0c0; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5;");

                Label nameLabel = new Label();
                nameLabel.setMaxWidth(Double.MAX_VALUE);
                nameLabel.setAlignment(javafx.geometry.Pos.CENTER);
                nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14.0));
                nameLabel.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));

                Label id = new Label();
                id.setMaxWidth(Double.MAX_VALUE);
                id.setAlignment(javafx.geometry.Pos.CENTER);
                id.setFont(Font.font("Arial", 12.0));
                id.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));

                Button Edit = new Button("Edit");
                Edit.setMaxWidth(Double.MAX_VALUE);
                Edit.setPrefHeight(30.0);
                Edit.setStyle("-fx-background-color: #4a86e8; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
                Edit.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));
                Edit.setUserData(map.get("id"));
                Edit.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        EditDepartmentApplication page = new EditDepartmentApplication(map.get("id"));
                        page.setDepartmentcontroller(Departmentcontroller.this);
                        Stage stage = (Stage) cardsContainer.getScene().getWindow();
                        try {
                            page.start(stage);
                        }catch (IOException e){
                            System.out.println(e.getCause());
                        }
                    }
                });
                VBox.setMargin(Edit, new Insets(10.0, 0, 0, 0));

                vbox.getChildren().addAll(nameLabel, id, Edit);
                nameLabel.setText(map.get("name"));
                id.setText("id: " + map.get("id"));

                cardsContainer.getChildren().add(vbox);

            }

        }
    }

    public void search(ActionEvent event) {
        String text = (String) searchField.getText();
        if (!text.isEmpty()) {
            LoadCards(text);
        }
    }
    //dcsaca

    @FXML
    public void reFresh(ActionEvent event) {
        searchField.setText("");
        System.out.println("refresh");
        LoadCards();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LoadCards();

    }

    public void GoToCreate(ActionEvent actionEvent) {
        AddDepartmentApplication page = new AddDepartmentApplication(this);
        Stage stage = new Stage();
        try {
            page.start(stage);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("An Error Occurred");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void refresh() {
        LoadCards();
    }
}
