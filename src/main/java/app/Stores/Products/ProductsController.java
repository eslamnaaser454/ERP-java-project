package app.Stores.Products;

import app.Classes.DataBaseConnection;
import app.Classes.ExcelSheet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ProductsController implements Initializable {

    @FXML
    private TableView<Product> table;
    @FXML
    private TableColumn<Product,String> idCol;
    @FXML
    private TableColumn<Product,String> nameCol;
    @FXML
    private TableColumn<Product,String> qntCol;
    @FXML
    private TableColumn<Product,String> priceCol;
    @FXML
    private TableColumn<Product,String> sellCol;
    @FXML
    private TableColumn<Product,String> stockCol;
    @FXML
    private TextField searchField;


    public ObservableList<Product> observableList(){
        DataBaseConnection dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
        ObservableList<Product> observableList = FXCollections.observableArrayList();
        List<Map<String ,String >> list = dataBaseConnection.select("select * from supply;");
        for (Map<String ,String > map:list){
            Product product = new Product(map.get("id"),map.get("name"),map.get("qnt"),map.get("unite_price"),map.get("sell_price"),map.get("stock_id"));
            observableList.add(product);
        }
        return observableList;
    }

    public ObservableList<Product> observableList(String value){
        DataBaseConnection dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
        ObservableList<Product> observableList = FXCollections.observableArrayList();
        List<Map<String ,String >> list = dataBaseConnection.select("select * from supply where name like '"+value+"%';");
        for (Map<String ,String > map:list){
            Product product = new Product(map.get("id"),map.get("name"),map.get("qnt"),map.get("unite_price"),map.get("sell_price"),map.get("stock_id"));
            observableList.add(product);
        }
        return observableList;
    }


    public void reFresh(){
        table.getItems().clear();
        table.setItems(observableList());
    }

    public void search(){
        String value = searchField.getText();
        table.getItems().clear();
        table.setItems(observableList(value));
    }

    public void Report(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        // Set the title for the directory chooser dialog
        directoryChooser.setTitle("Choose Directory");
        // Set the initial directory (optional)
        // directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if (selectedDirectory != null) {
            // Do something with the chosen directory
            String chosenDirectoryPath = selectedDirectory.getAbsolutePath();
            Date date = new Date();

//            System.out.println("Chosen directory: " + chosenDirectoryPath+"\\Invoice-Report-("+date.toString()+").xlsx");
            ExcelSheet excelSheet = new ExcelSheet(chosenDirectoryPath,"Supplies-Report");

            DataBaseConnection dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
            List<Map<String,String>> list = dataBaseConnection.select("select * from supply;");
            List<Map<Integer,String>> list1 = new ArrayList<>();
            Map<Integer,String> header = new HashMap<>();
            header.put(0,"ID");
            header.put(1,"Name");
            header.put(2,"Quantity");
            header.put(3,"Unite Price");
            header.put(4,"Sell Price");
            header.put(5,"Stock");

            list1.add(header);
            for (Map<String,String> map:list){
                Map<Integer,String> supply = new HashMap<>();
                supply.put(0, map.get("id"));
                supply.put(1, map.get("name"));
                supply.put(2, map.get("qnt"));
                supply.put(3, map.get("unite_price"));
                supply.put(4, map.get("sell_price"));
                List<Map<String,String>> stock = dataBaseConnection.select("select * from stock where id = "+map.get("stock_id")+";");
                supply.put(5, stock.getFirst().get("name"));

                list1.add(supply);
            }
            try {
                excelSheet.write(list1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        } else {
            // User canceled the directory chooser dialog
            System.out.println("No directory selected.");
        }
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        qntCol.setCellValueFactory(new PropertyValueFactory<>("qnt"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        sellCol.setCellValueFactory(new PropertyValueFactory<>("sell"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        reFresh();
    }


    public class Product{

        private String id;
        private String name;
        private String qnt;
        private String price;
        private String sell;
        private String stock;

        public Product(String id, String name, String qnt, String price, String sell, String stock) {
            this.id = id;
            this.name = name;
            this.qnt = qnt;
            this.price = price;
            this.sell = sell;
            DataBaseConnection dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
            this.stock = dataBaseConnection.select("select * from stock where id = "+stock+";").getFirst().get("name");

        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getQnt() {
            return qnt;
        }

        public String getPrice() {
            return price;
        }

        public String getSell() {
            return sell;
        }

        public String getStock() {
            return stock;
        }
    }
}
