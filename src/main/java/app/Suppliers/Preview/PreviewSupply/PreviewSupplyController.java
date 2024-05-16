package app.Suppliers.Preview.PreviewSupply;

import app.Classes.DataBaseConnection;
import app.Suppliers.Preview.PreviewSupplierApplication;
import app.Suppliers.Preview.PreviewSupply.EditeSupply.EditeSupplyApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class PreviewSupplyController implements Initializable {
    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";

    private DataBaseConnection dataBaseConnection;
    private String id;
    @FXML
    private ImageView image;
    @FXML
    private Label nameLable;
    @FXML
    private Label priceLable;
    @FXML
    private Label feesLable;
    @FXML
    private Label sellLable;
    @FXML
    private Label qntLable;
    @FXML
    private Label supplierLable;
    @FXML
    private Label stockLable;

    @FXML
    private TextField customerNameField;
    @FXML
    private TextField customerPhoneField;
    @FXML
    private TextField qntField;

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
    private TableColumn<Sale,String> qntCol;

    @FXML
    private TableColumn<Sale,String> priceCol;





    @FXML
    private TableColumn<Sale,String> totalCol;

    @FXML
    private ImageView backIcon;





    public void setId(String id) {
        this.id = id;
    }

    @FXML
    private void back(){
        dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
        Map<String,String> map = dataBaseConnection.select("select * from supply where id = "+id+";").getFirst();
        PreviewSupplierApplication page = new PreviewSupplierApplication(map.get("supplier_id"));
        Stage stage = (Stage) table.getScene().getWindow();
        try {
            page.start(stage);
        }catch (IOException e){
            System.out.println(e.getCause());
        }
    }

    public void setData(){
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String,String>> list = dataBaseConnection.select("select * from supply where id="+id+";");
        if (!list.isEmpty()){
            Map<String,String> map = list.getFirst();
            nameLable.setText(map.get("name"));
            priceLable.setText(map.get("unite_price"));
            feesLable.setText(map.get("additional_fees"));
            qntLable.setText(map.get("qnt"));
            sellLable.setText(map.get("sell_price"));
            File file = new File(System.getProperty("user.dir")+map.get("image"));
            if (!file.exists()){
                file = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\app\\images\\defulteImages\\NoneImage.jpg");
            }

            try {
                image.setImage(new Image(file.toURI().toURL().toString()));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }



        }

        table.getItems().clear();
        table.setItems(observableList());

    }



    @FXML
    public void addSales(){
        String customerName = customerNameField.getText();
        String customerContact = customerPhoneField.getText();
        String qnt = qntField.getText();
        int d;
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        if (customerName.isEmpty()){
            alert.setContentText("Customer Name Field Is Empty");
            alert.show();
            return;
        }
        if (customerContact.isEmpty()){
            alert.setContentText("Customer Contact Field Is Empty");
            alert.show();
            return;

        }else if (customerContact.length() < 11){
            alert.setContentText("Invalid Contact Number");
            alert.show();
            return;
        }
        if (qnt.isEmpty()){
            alert.setContentText("Quantity Field Is Empty");
            alert.show();
            return;
        }else if(Integer.parseInt(qnt) < 1){
            alert.setContentText("Quantity Field Can Be lower than One");
            alert.show();
            return;

        }else {

            try {
                d = Integer.parseInt(qnt);
            } catch (Exception e) {
                alert.setContentText("Quantity Field Must Be Number");
                alert.show();
                return;

            }
        }
        dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);

        Map<String,String> map = dataBaseConnection.select("select * from supply where id="+id+";").getFirst();

        if (d > Integer.parseInt(map.get("qnt")) || map.get("qnt").equals("0")){
            alert.setContentText("This Product doesn't has that much");
            alert.show();
            return;
        }
            String query = "insert into invoice(customer_name ,customer_phone ) values ('"+customerName+"','"+customerContact+"');";
            int invoiceID = dataBaseConnection.insert(query);

            query = "insert into sale(product_name ,unitePrice ,qnt ,invoice_id ,supply_id ) values ('"+map.get("name")+"',"+map.get("sell_price")+","+qnt+","+invoiceID+","+map.get("id")+")";
            dataBaseConnection.excute(query);
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText("Sale Added Successfully");
            alert.show();
            customerNameField.setText("");
            customerPhoneField.setText("");
            qntField.setText("");
            int updatedQnt = Integer.parseInt(map.get("qnt")) - d;
            dataBaseConnection.excute("update supply set qnt="+updatedQnt+" where id="+map.get("id")+";");
            setData();

    }


    ObservableList<Sale> observableList(){
        dataBaseConnection = new DataBaseConnection(dbPath);
            List<Map<String,String>> list = dataBaseConnection.select("select * from sale where supply_id ="+id+";");
            ObservableList<Sale> observableList = FXCollections.observableArrayList();
        for (Map<String,String> map:list){
            Sale sale = new Sale(map.get("id"),map.get("qnt"),map.get("unitePrice"),map.get("invoice_id"));
            System.out.println(map.get("id")+" "+map.get("qnt")+" "+map.get("unitePrice")+" "+map.get("invoice_id"));

            observableList.add(sale);
        }
        return  observableList;
    }



    ObservableList<Sale> observableList(String search){
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String,String>> list = dataBaseConnection.select("select * from sale where supply_id="+id+" ;");
        ObservableList<Sale> observableList = FXCollections.observableArrayList();
        for (Map<String,String> map:list){
//        public Sale(String id, String qnt, String price,String invoice_id) {

            Sale sale = new Sale(map.get("id"),map.get("qnt"),map.get("unitePrice"),map.get("invoice_id"));
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

    public void goToEdit(){
        EditeSupplyApplication editeSupplyApplication = new EditeSupplyApplication(id,this);
        Stage stage = new Stage();
        try {
            editeSupplyApplication.start(stage);
        } catch (IOException e) {
            System.out.println("Error While Open Edite Supply Form");
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        qntCol.setCellValueFactory(new PropertyValueFactory<>("qnt"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("unitePrice"));

        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));


    }

  public class Sale{
        private String  id;
      private String customer ="eslam";
      private String contact = "01145954371";
      private String qnt;
      private String unitePrice ;
      private String total ;
      private String invoice_id;

      public Sale(String id, String qnt, String unitePrice, String invoice_id) {
          this.id = id;
          this.invoice_id = invoice_id;

          dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
          Map<String,String> map = dataBaseConnection.select("select * from invoice where id = "+invoice_id+";").getFirst();
          this.customer = map.get("customer_name");
          this.contact = map.get("customer_phone");
          this.qnt = qnt;
          this.unitePrice = unitePrice;
          this.total = ""+ Double.parseDouble(unitePrice)*Double.parseDouble(qnt);

      }


      public String getId() {
          return id;
      }

      public void setId(String id) {
          this.id = id;
      }

      public String getCustomer() {
          return customer;
      }

      public void setCustomer(String customer) {
          this.customer = customer;
      }

      public String getContact() {
          return contact;
      }

      public void setContact(String contact) {
          this.contact = contact;
      }

      public String getQnt() {
          return qnt;
      }

      public void setQnt(String qnt) {
          this.qnt = qnt;
      }

      public String getUnitePrice() {
          return unitePrice;
      }

      public void setUnitePrice(String unitePrice) {
          this.unitePrice = unitePrice;
      }

      public String getTotal() {
          return total;
      }

      public void setTotal(String total) {
          this.total = total;
      }

      public String getInvoice_id() {
          return invoice_id;
      }

      public void setInvoice_id(String invoice_id) {
          this.invoice_id = invoice_id;
      }
  }

}
