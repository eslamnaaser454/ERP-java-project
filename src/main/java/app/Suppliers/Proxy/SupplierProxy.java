package app.Suppliers.Proxy;

import app.Classes.DataBaseConnection;
import java.util.List;
import java.util.Map;

public class SupplierProxy implements SupplierService {
    private final DataBaseConnection db;

    public SupplierProxy(String dbPath) {
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

    @Override
    public List<Map<String, String>> searchSuppliersByCompany(String companyName) {
        return db.select("SELECT * FROM supplier WHERE company LIKE '" + companyName + "%';");
    }

    @Override
    public List<Map<String, String>> searchSuppliersByEmail(String keyword) {
        return db.select("SELECT * FROM supplier WHERE email LIKE '" + keyword + "%';");
    }

    public List<Map<String, String>> searchSuppliersByPhone(String keyword) {
        return db.select("SELECT * FROM supplier WHERE phone LIKE '" + keyword + "%';");
    }
}
