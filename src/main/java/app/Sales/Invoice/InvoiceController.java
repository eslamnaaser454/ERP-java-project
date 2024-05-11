package app.Sales.Invoice;

import app.Classes.DataBaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class InvoiceController implements Initializable {
    DataBaseConnection dataBaseConnection;
    private String id;
    @FXML
    private Label nameLable;
    @FXML
    private Label contactLable;
    @FXML
    private Label purchLable;

    @FXML
    private TableView<Sale> table;

    @FXML
    private TableColumn<Sale,String> idCol;

    @FXML
    private TableColumn<Sale,String> productCol;

    @FXML
    private TableColumn<Sale,String> qntCol;

    @FXML
    private TableColumn<Sale,String> priceCol;

    @FXML
    private TableColumn<Sale,String> totalCol;

    public void setId(String id) {
        this.id = id;
    }

    private ObservableList<Sale> observableList(){
        ObservableList<Sale> observableList = FXCollections.observableArrayList();
        dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
        List<Map<String,String>> list = dataBaseConnection.select("select * from sale where invoice_id = "+id+";");
        for (Map<String,String> map:list){
            int total = 0;
            for (int i = 1;i<=Double.parseDouble(map.get("qnt"));i++){
                total+=Double.parseDouble(map.get("unitePrice"));
            }
            Sale sale = new Sale(map.get("id"),map.get("product_name"),map.get("qnt"),map.get("unitePrice"),""+total);
            observableList.add(sale);


        }
        return observableList;
    }


    public void setData(){
        dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
        List<Map<String,String>> list = dataBaseConnection.select("select * from invoice where id = "+id+";");
        if (!list.isEmpty()){
            Map<String,String> map = list.getFirst();
            nameLable.setText(map.get("customer_name"));
            contactLable.setText(map.get("customer_phone"));
            int purch = 0;
            list.clear();
            list = dataBaseConnection.select("select * from sale where invoice_id = "+id+";");
            for (Map<String,String> sale:list){
                int total = 0;
                for (int i = 1;i<=Double.parseDouble(sale.get("qnt"));i++){
                    total+=Double.parseDouble(sale.get("unitePrice"));
                }

                purch+=total;
                purchLable.setText(""+purch);
            }

            table.getItems().clear();
            table.setItems(observableList());

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productCol.setCellValueFactory(new PropertyValueFactory<>("product"));
        qntCol.setCellValueFactory(new PropertyValueFactory<>("qnt"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("unitePrice"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));

    }


    public class Sale{
        private String id;
        private String product;
        private String qnt;
        private String unitePrice;
        private String total;

        public Sale(String id, String product, String qnt, String unitePrice, String total) {
            this.id = id;
            this.product = product;
            this.qnt = qnt;
            this.unitePrice = unitePrice;
            this.total = total;
        }

        public String getId() {
            return id;
        }

        public String getProduct() {
            return product;
        }

        public String getQnt() {
            return qnt;
        }

        public String getUnitePrice() {
            return unitePrice;
        }

        public String getTotal() {
            return total;
        }
    }
}
