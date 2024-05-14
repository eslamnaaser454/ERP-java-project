module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.apache.poi.ooxml;
    requires org.apache.xmlbeans;

    opens app.Login to javafx.fxml;
    opens app.Index to javafx.fxml;
    opens app.Stores.Index to javafx.fxml;
    opens app.Stores.Manage to javafx.fxml;
    opens app.Stores.Manage.Create to javafx.fxml;
    opens app.Stores.Manage.Edite to javafx.fxml;
    opens app.Suppliers to javafx.fxml;
    opens app.Suppliers.Create to javafx.fxml;
    opens app.Suppliers.Preview to javafx.fxml;
    opens app.Suppliers.Preview.CreateSupply to javafx.fxml;
    opens app.Suppliers.Preview.PreviewSupply to javafx.fxml;
    opens app.Suppliers.Preview.PreviewSupply.EditeSupply to javafx.fxml;
    opens app.HR.index to javafx.graphics;
    opens app.PublicControllers to javafx.fxml;
    opens app.HR.Manage to javafx.fxml;
    opens app.HR.Manage.add to javafx.fxml;
    opens app.HR.Manage.Edite to javafx.fxml;
    opens app.HR.Department to javafx.fxml;
    opens app.HR.Department.Add to javafx.fxml;

    opens app.Sales to javafx.fxml;
    opens app.Sales.Invoice to javafx.fxml;
    opens app.HR.Department.Edit to javafx.fxml;
    opens app.Stores.Products to javafx.fxml;
//    opens app.Stores.Products to javafx.fxml;
    opens app.usermanagment to javafx.fxml;
    opens app.usermanagment.Createuser to javafx.fxml;
    opens app.usermanagment.EditeUserManagment to javafx.fxml;
    opens app.Log to javafx.fxml;


    exports app.Index;
    exports app.Login;
    exports app.Stores.Index;
    exports app.Stores.Manage;
    exports app.Stores.Manage.Create;
    exports app.Stores.Manage.Edite;
    exports app.HR.index;
    exports app.HR.Manage;
    exports app.PublicControllers;
    exports app.Suppliers;
    exports app.Suppliers.Create;
    exports app.Suppliers.Preview;
    exports app.Suppliers.Preview.CreateSupply;
    exports app.Sales ;
    exports app.Sales.Invoice ;
    exports app.HR.Department;
    exports app.Suppliers.Preview.PreviewSupply;
    exports app.Suppliers.Preview.PreviewSupply.EditeSupply ;
    exports app.HR.Manage.add;
    exports app.HR.Manage.Edite;
    exports app.HR.Department.Edit;
    exports app.Stores.Products;
    exports app.HR.Department.Add;
    exports app.usermanagment;
    exports app.usermanagment.Createuser;
    exports app.usermanagment.EditeUserManagment;
    exports app.Log;

}