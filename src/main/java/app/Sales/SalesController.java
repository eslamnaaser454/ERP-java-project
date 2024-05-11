package app.Sales;


import app.Classes.DataBaseConnection;
import app.Sales.Invoice.InvoiceApplication;
import app.Suppliers.Preview.PreviewSupply.PreviewSupplyController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class SalesController implements Initializable {

    private DataBaseConnection dataBaseConnection;

    @FXML
    private TextField searchField;
    @FXML
    private TableView<Sale> table;
    @FXML
    private TableColumn<Sale,String> idCol;

    @FXML
    private TableColumn<Sale,String> customerCol;

    @FXML
    private TableColumn<Sale,String> contactCol;



    @FXML
    private TableColumn<Sale,String> dateCol;


    @FXML
    private TableColumn<Sale,String> totalCol;

    @FXML
    private TableColumn<Sale,Button> printCol;






    ObservableList<Sale> observableList(){
        dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
        List<Map<String,String>> list = dataBaseConnection.select("select * from invoice ;");
        ObservableList<Sale> observableList = FXCollections.observableArrayList();
        for (Map<String,String> map:list){
            Sale sale = new Sale(map.get("id"),map.get("customer_name"),map.get("customer_phone"),map.get("date"));
            observableList.add(sale);
        }
        return  observableList;
    }

    ObservableList<Sale> observableList(String search){
        dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
        List<Map<String,String>> list = new ArrayList<>();

        try {
            int id = Integer.parseInt(search);
            list = dataBaseConnection.select("select * from invoice where id = "+id+";");

        }catch (Exception e){
            list = dataBaseConnection.select("select * from invoice where customer_name like '"+search+"%';");

        }
        ObservableList<Sale> observableList = FXCollections.observableArrayList();
        for (Map<String,String> map:list){
            Sale sale = new Sale(map.get("id"),map.get("customer_name"),map.get("customer_phone"),map.get("date"));
            observableList.add(sale);
        }
        return  observableList;
    }


    public void search(){
        String value = searchField.getText();
        table.getItems().clear();
        table.setItems(observableList(value));
    }

    public void refreash(){
        table.getItems().clear();
        table.setItems(observableList());
    }














    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
//        qntCol.setCellValueFactory(new PropertyValueFactory<>("qnt"));
//        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
//        productCol.setCellValueFactory(new PropertyValueFactory<>("product"));
        printCol.setCellValueFactory(new PropertyValueFactory<>("printBtn"));
        refreash();


    }


    public class Sale{
        private String id;
        private String customer;
        private String contact;

        private String date;
        private String total;


        private Button printBtn;

        public Sale(String id, String customer, String contact, String date) {
            this.id = id;
            this.customer = customer;
            this.contact = contact;

            dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
            List<Map<String ,String>> sales = dataBaseConnection.select("select * from sale where invoice_id = "+this.id+";");
            double Total = 0;
            for (Map<String,String >map:sales){

                for (int i = 1;i<=Integer.parseInt(map.get("qnt"));i++){
                    Total += Double.parseDouble(map.get("unitePrice"));
                }

            }
            this.total = Total+"";
            this.date = date;

            printBtn = new Button("Detiles");
            printBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    InvoiceApplication invoiceApplication = new InvoiceApplication(id);
                    Stage stage = new Stage();
//                    stage.setResizable(false);
                    try {
                        invoiceApplication.start(stage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
            printBtn.setBackground(Background.fill(Paint.valueOf("brown")));
            printBtn.setTextFill(Paint.valueOf("white"));
        }

        public String getId() {
            return id;
        }

        public String getCustomer() {
            return customer;
        }

        public String getContact() {
            return contact;
        }



        public String getDate() {
            return date;
        }

        public String getTotal() {
            return total;
        }




        public Button getPrintBtn() {
            return printBtn;
        }
    }
}
