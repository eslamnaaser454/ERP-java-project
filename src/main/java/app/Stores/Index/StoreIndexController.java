package app.Stores.Index;


import app.Index.IndexApplication;
import app.Stores.Manage.StoreManageApplication;
import app.Stores.Products.ProductsApplication;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class StoreIndexController {
    @FXML
    public VBox manage;



    @FXML
    public void GotoManage(){
        StoreManageApplication storeManageApplication = new StoreManageApplication();
        Stage stage = (Stage) manage.getScene().getWindow();
        stage.setResizable(true);
        try {
            storeManageApplication.start(stage);

        }catch (IOException e){
            System.out.println(e.getCause());
        }
        //
    }

    @FXML
    public void GotoProduct(){
        ProductsApplication productsApplication = new ProductsApplication();
        Stage stage = (Stage) manage.getScene().getWindow();
        stage.setResizable(true);
        try {
            productsApplication.start(stage);

        }catch (IOException e){
            System.out.println(e.getCause());
        }
        //
    }


}
