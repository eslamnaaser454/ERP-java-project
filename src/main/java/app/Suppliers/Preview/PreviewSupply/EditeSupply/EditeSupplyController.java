package app.Suppliers.Preview.PreviewSupply.EditeSupply;

import app.Classes.DataBaseConnection;
import app.Suppliers.Preview.PreviewSupply.PreviewSupplyController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class EditeSupplyController implements Initializable {
    DataBaseConnection dataBaseConnection;
    private String id;
    private PreviewSupplyController previewSupplyController;

    public void setId(String id) {
        this.id = id;
    }

    public void setPreviewSupplyController(PreviewSupplyController previewSupplyController) {
        this.previewSupplyController = previewSupplyController;
    }
    @FXML
    private TextField nameFiled;
    @FXML
    private TextField qntFiled;
    @FXML
    private TextField priceFiled;
    @FXML
    private TextField feesFiled;
    @FXML
    private TextField sellPriceFiled;
    @FXML
    private ComboBox<String> stockCombo;

    @FXML
    Label ErrMsg;



    private Map<String,String> getSupply(){
        dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
        List<Map<String ,String>> list = dataBaseConnection.select("select * from supply where id="+id+";");
        if (!list.isEmpty()){
            Map<String,String> map = list.getFirst();
            return map;
        }else
            return null;
    }
    public void setData(){
        dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
        String stockName = null;

        List<Map<String ,String>> list = dataBaseConnection.select("select * from supply where id="+id+";");
        if (!list.isEmpty()){
            Map<String,String> map = list.getFirst();
            List<Map<String,String>> stocks = dataBaseConnection.select("select * from stock;");
            for (Map<String ,String> stock:stocks){
                stockCombo.getItems().add(stock.get("name"));
                if (stock.get("id").equals(map.get("stock_id")))
                    stockName = stock.get("name");

            }
            nameFiled.setText(map.get("name"));
            qntFiled.setText(map.get("qnt"));
            priceFiled.setText(map.get("unite_price"));
            feesFiled.setText(map.get("additional_fees"));
            sellPriceFiled.setText(map.get("sell_price"));
            stockCombo.setValue(stockName);
        }
    }

    public void submit(){
        String name = (String) nameFiled.getText();

        String qnt = qntFiled.getText();
        String price = priceFiled.getText();
        String sellPrice = sellPriceFiled.getText();
        String fees = feesFiled.getText();
        String stock = stockCombo.getValue();

        String stockId;
        dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);

        List<Map<String,String>> getStockes = dataBaseConnection.select("select * from stock where name='"+stock+"'");




        if (name.isEmpty()){
            nameFiled.setBorder(Border.stroke(Paint.valueOf("red")));
            ErrMsg.setText("Name Field Is Empty");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            return;

        }else if (qnt.isEmpty()){
            qntFiled.setBorder(Border.stroke(Paint.valueOf("red")));
            ErrMsg.setText("Quantity Field Is Empty");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            return;
        }else if(!qnt.isEmpty()){
            try {
                double test = Double.parseDouble(qnt);
            }catch (Exception e){
                qntFiled.setBorder(Border.stroke(Paint.valueOf("red")));
                ErrMsg.setText("Quantity Field Must Be Number");
                ErrMsg.setTextFill(Paint.valueOf("red"));
                return;
            }
        }

        if(price.isEmpty()){
            priceFiled.setBorder(Border.stroke(Paint.valueOf("red")));
            ErrMsg.setText("Unit price Field Is Empty");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            return;
        }else if (!price.isEmpty()) {
            try {
                double test = Double.parseDouble(price);
            } catch (Exception e) {
                priceFiled.setBorder(Border.stroke(Paint.valueOf("red")));
                ErrMsg.setText("Unite Price Field Must Be Number");
                ErrMsg.setTextFill(Paint.valueOf("red"));
                return;
            }
        }


        if(sellPrice.isEmpty()){
            sellPriceFiled.setBorder(Border.stroke(Paint.valueOf("red")));
            ErrMsg.setText("Sell price Field Is Empty");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            return;
        }else if (!sellPrice.isEmpty()) {
            try {
                double test = Double.parseDouble(sellPrice);
            } catch (Exception e) {
                sellPriceFiled.setBorder(Border.stroke(Paint.valueOf("red")));
                ErrMsg.setText("Sell Price Field Must Be Number");
                ErrMsg.setTextFill(Paint.valueOf("red"));
                return;
            }
        }

        if (!fees.isEmpty()) {
            try {
                double test = Double.parseDouble(fees);
            } catch (Exception e) {
                feesFiled.setBorder(Border.stroke(Paint.valueOf("red")));
                ErrMsg.setText("Additional Fees Field Must Be Number");
                ErrMsg.setTextFill(Paint.valueOf("red"));
                return;
            }
        }else{
            fees = "0";
        }

        if(stock == null){
            stockCombo.setBorder(Border.stroke(Paint.valueOf("red")));
            ErrMsg.setText("You Should Choose Stock ");
            ErrMsg.setTextFill(Paint.valueOf("red"));
            return;
        }

        if (getStockes.isEmpty() ){
            System.out.println("Stock Not Found");
            return;
        }else {
            stockId = getStockes.getFirst().get("id");
        }
        dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
        String NoneImagePath = "\\src\\main\\resources\\images\\defulteImages\\NoneImage.jpg";
        String query = "update supply set  name = '"+name +"' , qnt = "+qnt+" , unite_price = "+price+" , sell_price = "+sellPrice+", additional_fees = "+fees+", stock_id = "+stockId+" where id = "+id+";";
        System.out.println("Query is : "+query);
        dataBaseConnection.excute(query);
        ErrMsg.setText("Supply Edited SuccessFuly");
        ErrMsg.setTextFill(Paint.valueOf("green"));
        setData();
        this.previewSupplyController.setData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setData();
    }
}
