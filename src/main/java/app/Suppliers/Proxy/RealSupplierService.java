package app.Suppliers.Proxy;

import app.Classes.DataBaseConnection;

import java.util.List;
import java.util.Map;

public class RealSupplierService implements SupplierService {
    private final DataBaseConnection db;

    public RealSupplierService(String dbPath) {
        this.db = new DataBaseConnection(dbPath);
    }

    @Override
    public List<Map<String, String>> getSuppliers() {
        return db.select("SELECT * FROM supplier;");
    }

    @Override
    public List<Map<String, String>> searchSuppliers(String keyword) {
        return db.select("SELECT * FROM supplier WHERE name LIKE '" + keyword + "%';");
    }
}



