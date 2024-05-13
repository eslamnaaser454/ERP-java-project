package app.Suppliers.Preview.CreateSupply;

import app.Classes.DataBaseConnection;
import app.Classes.Image;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class CreateSupplyController implements Initializable {
    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";

    private final String resourcesPath = System.getProperty("user.dir") + "\\src\\main\\resources";


    DataBaseConnection dataBaseConnection;

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    @FXML
    TextField nameFiled;
    @FXML
    TextField imageFiled;
    @FXML
    TextField qntFiled;
    @FXML
    TextField priceFiled;
    @FXML
    TextField sellPriceFiled;
    @FXML
    TextField feesFiled;
    @FXML
    ComboBox<String> stockCombo ;

    @FXML
    Button imageBtn;

    @FXML
    Label ErrMsg;

    @FXML
    public void chooseImage(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(new Stage());
        imageFiled.setText(file.getAbsolutePath());
    }

    @FXML
    public void submit(){
        String name = (String) nameFiled.getText();
        String imagePath = imageFiled.getText();
        String qnt = qntFiled.getText();
        String price = priceFiled.getText();
        String sellPrice = sellPriceFiled.getText();
        String fees = feesFiled.getText();
        String stock = stockCombo.getValue();
        File image = new File(imagePath);
        String stockId;
        dataBaseConnection = new DataBaseConnection(dbPath);

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
        if (!imagePath.isEmpty()){
            if (!image.exists()){
                imageFiled.setBorder(Border.stroke(Paint.valueOf("red")));
                ErrMsg.setText("Image Dosen't Exist");
                ErrMsg.setTextFill(Paint.valueOf("red"));
                return;
            }

            System.out.println(image.toPath());

        }else {
            image = null;
        }
        if (getStockes.isEmpty() ){
            System.out.println("Stock Not Found");
            return;
        }else {
            stockId = getStockes.getFirst().get("id");
        }
        dataBaseConnection = new DataBaseConnection(dbPath);
        String NoneImagePath = "\\src\\main\\resources\\images\\defulteImages\\NoneImage.jpg";
        String query = "insert into supply (name,image,qnt,unite_price,sell_price,additional_fees,supplier_id,stock_id) values('"+name +"','"+NoneImagePath+"',"+qnt+","+price+","+sellPrice+","+fees+","+id+","+stockId+");";
        System.out.println("Query is : "+query);
        int supplyId =dataBaseConnection.insert(query);
        ErrMsg.setText("Supply Created SuccessFuly");
        nameFiled.setText("");
        imageFiled.setText("");
        qntFiled.setText("");
        priceFiled.setText("");
        feesFiled.setText("");

        ErrMsg.setTextFill(Paint.valueOf("green"));




        if (image != null){
            Image SavedImage = new Image(imagePath);
            String newPath = "\\src\\main\\resources\\app\\DataBaseImages\\SuppliesImages\\"+supplyId;
            SavedImage.SaveAt(System.getProperty("user.dir")+newPath+"\\");
            dataBaseConnection.excute("update supply set image='"+newPath+"\\"+image.getName()+"' where id="+supplyId+";");
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//      imageFiled.setDisable(true);
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String,String>> list = dataBaseConnection.select("select * from stock;");
        if (!list.isEmpty()){
            System.out.println("Not Empty");
            for (Map<String,String> map:list){
                stockCombo.getItems().add(map.get("name"));
            }
            }else {
            System.out.println(" Empty");

        }
    }
}
