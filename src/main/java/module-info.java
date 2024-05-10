module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens app.Login to javafx.fxml;
    opens app.Index to javafx.fxml;
    opens app.Stores.Index to javafx.fxml;
    opens app.Stores.Manage to javafx.fxml;
    opens app.Stores.Manage.Create to javafx.fxml;
    opens app.Stores.Manage.Edite to javafx.fxml;
    opens app.HR.Employees to javafx.fxml;
    opens app.Suppliers to javafx.fxml;
    opens app.Suppliers.Create to javafx.fxml;
    opens app.Suppliers.Preview to javafx.fxml;
    opens app.Suppliers.Preview.CreateSupply to javafx.fxml;
    opens app.HR.index to javafx.graphics;
    opens app.PublicControllers to javafx.fxml;
    opens app.HR.Manage to javafx.fxml;
    opens app.HR.Manage.add to javafx.fxml;
    opens app.HR.Manage.Edite to javafx.fxml;
    opens app.HR.Attendance to javafx.fxml;


    exports app.Index;
    exports app.Login;
    exports app.Stores.Index;
    exports app.Stores.Manage;
    exports app.Stores.Manage.Create;
    exports app.Stores.Manage.Edite;
    exports app.HR.Employees;
    exports app.HR.index;
    exports app.HR.Manage;
    exports app.PublicControllers;
    exports app.Suppliers;
    exports app.Suppliers.Create;
    exports app.Suppliers.Preview;
    exports app.Suppliers.Preview.CreateSupply;
    exports app.HR.Manage.add;
    exports app.HR.Manage.Edite;
    exports app.HR.Attendance;

}