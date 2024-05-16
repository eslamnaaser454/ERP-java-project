package app.Suppliers.Preview;

import app.Classes.DataBaseConnection;
import app.Suppliers.Preview.CreateSupply.CreateSupplyApplication;
import app.Suppliers.Preview.PreviewSupply.PreviewSupplyApplication;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class PreviewSupplierController implements Initializable {
    private String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";


    private DataBaseConnection dataBaseConnection;
    private String id;

    @FXML
    private Label nameLable;

    @FXML
    private Label companyLable;

    @FXML
    private Label emailLable;

    @FXML
    private Label phoneLable;

    @FXML
    private Label totalTradesValue;

    @FXML
    private VBox supplyContainer;

    @FXML
    private Label totalGain;

    public void setId(String id) {
        this.id = id;
    }


    public Map<String,String> getSupplier(){
        Map<String,String> map = new HashMap<>();
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String,String>> list = dataBaseConnection.select("select * from supplier where id ="+id+";");
        if (!list.isEmpty() ){
            System.out.println("here 1");
            return list.getFirst();
        }else {
            System.out.println("here 2 id = "+id);

            return map;
        }
    }

    public void setData(){
        Map<String,String> map = getSupplier();
        nameLable.setText(map.get("name"));
        companyLable.setText(map.get("company"));
        phoneLable.setText(map.get("phone"));
        emailLable.setText(map.get("email"));
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String,String>> list = dataBaseConnection.select("select * from supply where supplier_id="+map.get("id")+";");
        int totalTrades = list.size();
        totalTradesValue.setText("" + totalTrades);
        loadSupplies();
    }



    @FXML
    public void createSupply(){
        CreateSupplyApplication createSupplyApplication = new CreateSupplyApplication(id);
        Stage stage = new Stage();
        stage.setResizable(false);
        try {
            createSupplyApplication.start(stage);
        }catch (IOException e){
            System.out.println("Error While Going to Create Supply");
        }
    }

    public void loadSupplies(){
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String,String>> supplies = dataBaseConnection.select("select * from supply where supplier_id="+id+";");
        supplyContainer.getChildren().clear();
        for (Map<String,String> map:supplies){
            File file = new File(System.getProperty("user.dir")+map.get("image"));
            if (!file.exists()){
                file = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\app\\images\\defulteImages\\NoneImage.jpg");
            }
            ImageView imageView = null;
            try {
                imageView = new ImageView(new Image(file.toURI().toURL().toString()));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);
            List<Map<String,String>> list = dataBaseConnection.select("select * from supplier where id="+map.get("supplier_id")+";");
            Map<String,String> supplier = list.getFirst();

             list = dataBaseConnection.select("select * from stock  where id="+map.get("stock_id")+";");
             Map<String,String> stock = list.getFirst();

            // Labels
            Label productNameLabel = new Label("Product Name : ");
            Label productNameValueLabel = new Label(map.get("name"));
            HBox nameHbox = new HBox(productNameLabel,productNameValueLabel);

            Label quantityLabel = new Label("Qnt : ");
            Label quantityValueLabel = new Label(map.get("qnt"));
            HBox qntHbox = new HBox(quantityLabel,quantityValueLabel);

            Label unitPriceLabel = new Label("Unite Price : ");
            Label unitPriceValueLabel = new Label(map.get("unite_price"));
            HBox unitPriceHbox = new HBox(unitPriceLabel,unitPriceValueLabel);

            Label additionalFeesLabel = new Label("Additional Fees : ");
            Label additionalFeesValueLabel = new Label(map.get("additional_fees"));
            HBox additionalFeesHbox = new HBox(additionalFeesLabel,additionalFeesValueLabel);


            Label sellingPriceLabel = new Label("Selling Price : ");
            Label sellingPriceValueLabel = new Label(map.get("sell_price"));
            HBox sellingPriceHbox = new HBox(sellingPriceLabel,sellingPriceValueLabel);


            Label supplierLabel = new Label("Supplier : ");
            Hyperlink supplierHyperlink = new Hyperlink(supplier.get("name"));
            HBox supplierHbox = new HBox(supplierLabel,supplierHyperlink);

            Label stockLabel = new Label("Stock : ");
            Hyperlink stockHyperlink = new Hyperlink(stock.get("name"));
            HBox stockHbox = new HBox(stockLabel,stockHyperlink);
            // Buttons
            Button previewButton = new Button("Preview");
            previewButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
            previewButton.setPadding(new Insets(5,15,5,15));

            previewButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    PreviewSupplyApplication previewSupplyApplication = new PreviewSupplyApplication(map.get("id"));
                    Stage stage = (Stage) supplyContainer.getScene().getWindow();
                    try {
                        previewSupplyApplication.start(stage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            Button removeButton = new Button("Remove");
            removeButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            removeButton.setPadding(new Insets(5,15,5,15));
            removeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    ButtonType okButton = new ButtonType("Delete");
                    ButtonType cancleButton = new ButtonType("Cancel");

                    Alert alert = new Alert(Alert.AlertType.WARNING,"Are you sure you want to delete that Element",okButton,cancleButton);
                    alert.setHeaderText(null);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == okButton) {
                        String query = "delete from supply  where id=" + map.get("id") + ";";
                        System.out.println(query);
                        dataBaseConnection.excute(query);
                        setData();

                    }
                }
            });


            // HBoxes
            HBox imageBox = new HBox(imageView);
            imageBox.setAlignment(Pos.CENTER_LEFT);
            VBox detilesVbox =  new VBox(
                    nameHbox,qntHbox,unitPriceHbox,additionalFeesHbox,sellingPriceHbox,supplierHbox,stockHbox
            );
            detilesVbox.setSpacing(5);

            for (Node node :detilesVbox.getChildren()){
                if (node instanceof HBox){
                    for (Node l : ((HBox) node).getChildren()){
                        if (l instanceof Label){

                            ((Label) l).setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD,12));
                        }
                    }
                }
            }







            VBox buttonsBox = new VBox(previewButton, removeButton);
            buttonsBox.setAlignment(Pos.CENTER);
            buttonsBox.setSpacing(10);

            // Main HBox
            HBox mainBox = new HBox(imageBox, detilesVbox,buttonsBox);
            mainBox.setHgrow(detilesVbox,Priority.ALWAYS);
            mainBox.setPadding(new Insets(10));
            mainBox.setSpacing(10);

            mainBox.setAlignment(Pos.CENTER_LEFT);
            mainBox.setStyle("-fx-background-color: rgba(203,203,203,0.4);");
            supplyContainer.getChildren().add(mainBox);

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }


}
