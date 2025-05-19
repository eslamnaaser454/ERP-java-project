//بسم الله
/*package app.Index;

import app.Classes.Authentication;
import app.Classes.DataBaseConnection;
import app.Login.LoginApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import app.Stores.Index.StoreIndexApplication;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.control.Label;


public class IndexController implements Initializable {

    private Authentication authentication;
    @FXML
    private HBox sideBar;
    @FXML
    private Label empCount;
    @FXML
    private Label salescont;
    @FXML
    private Label stockscont;
    @FXML
    private Label productscont;

    private DataBaseConnection dataBaseConnection;

    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";


    private void updateEmployeeCount() {
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String, String>> result = dataBaseConnection.select("SELECT * FROM staff;");
        if (result != null) {
            empCount.setText(String.valueOf(result.size()));
        }
    }
    private void updatesale() {
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String, String>> result = dataBaseConnection.select("SELECT * FROM sale;");
        if (result != null) {
            salescont.setText(String.valueOf(result.size()));
        }
    }
    private void updatestock() {
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String, String>> result = dataBaseConnection.select("SELECT * FROM stock;");
        if (result != null) {
            stockscont.setText(String.valueOf(result.size()));
        }
    }
    private void updateproducts() {
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String, String>> result = dataBaseConnection.select("SELECT * FROM supply;");
        if (result != null) {
            productscont.setText(String.valueOf(result.size()));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateEmployeeCount();
        updatesale();
        updatestock();
        updateproducts();

    }



}*/
package app.Index;

import app.Classes.Authentication;
import app.Classes.DataBaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import java.net.URL;
import java.util.*;

// Observer Interface
interface DataObserver {
    void update(Map<String, Integer> data);
}

// Subject Interface
interface DataSource {
    void registerObserver(DataObserver observer);
    void unregisterObserver(DataObserver observer);
    void notifyObservers();
    Map<String, Integer> fetchData();
}

// Concrete Subject (Data Source)
class DatabaseDataSource implements DataSource {
    private final String dbPath;
    private final List<DataObserver> observers = new ArrayList<>();
    private final DataBaseConnection dataBaseConnection;

    public DatabaseDataSource(String dbPath) {
        this.dbPath = dbPath;
        this.dataBaseConnection = new DataBaseConnection(dbPath);
    }

    @Override
    public void registerObserver(DataObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void unregisterObserver(DataObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        Map<String, Integer> data = fetchData();
        for (DataObserver observer : this.observers) {
            observer.update(data);
        }
    }

    @Override
    public Map<String, Integer> fetchData() {
        Map<String, Integer> data = new HashMap<>();
        data.put("employeeCount", fetchCount("SELECT * FROM staff;"));
        data.put("salesCount", fetchCount("SELECT * FROM sale;"));
        data.put("stockCount", fetchCount("SELECT * FROM stock;"));
        data.put("productsCount", fetchCount("SELECT * FROM supply;"));
        return data;
    }

    private int fetchCount(String query) {
        List<Map<String, String>> result = dataBaseConnection.select(query);
        return (result != null) ? result.size() : 0;
    }

    // Simulate data updates (in a real application, this would be triggered by database changes)
    public void simulateDataChange() {
        notifyObservers();
    }
}

public class IndexController implements Initializable, DataObserver {

    private Authentication authentication;
    @FXML
    private HBox sideBar;
    @FXML
    private Label empCount;
    @FXML
    private Label salescont;
    @FXML
    private Label stockscont;
    @FXML
    private Label productscont;

    private DatabaseDataSource dataSource;
    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataSource = new DatabaseDataSource(dbPath);
        dataSource.registerObserver(this);
        // Initial data load
        update(dataSource.fetchData());

        // In a real application, you would have a mechanism to trigger data updates
        // For demonstration purposes, let's simulate an update after a delay
        // (Remove this in a production environment and implement real-time updates)

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                dataSource.simulateDataChange();
            }
        }, 5000, 5000); // Update every 5 seconds

    }

    @Override
    public void update(Map<String, Integer> data) {
        if (empCount != null) {
            empCount.setText(String.valueOf(data.get("employeeCount")));
        }
        if (salescont != null) {
            salescont.setText(String.valueOf(data.get("salesCount")));
        }
        if (stockscont != null) {
            stockscont.setText(String.valueOf(data.get("stockCount")));
        }
        if (productscont != null) {
            productscont.setText(String.valueOf(data.get("productsCount")));
        }
    }
}