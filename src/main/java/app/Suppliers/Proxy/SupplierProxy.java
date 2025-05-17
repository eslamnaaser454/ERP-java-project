package app.Suppliers.Proxy;

import java.util.List;
import java.util.Map;

public class SupplierProxy implements SupplierService {
    private RealSupplierService realService;
    private final String dbPath;

    public SupplierProxy(String dbPath) {
        this.dbPath = dbPath;
    }

    private RealSupplierService getRealService() {
        if (realService == null) {
            realService = new RealSupplierService(dbPath);
        }
        return realService;
    }

    @Override
    public List<Map<String, String>> getSuppliers() {
        System.out.println("Proxy: Loading all suppliers...");
        return getRealService().getSuppliers();
    }

    @Override
    public List<Map<String, String>> searchSuppliers(String keyword) {
        System.out.println("Proxy: Searching suppliers for keyword: " + keyword);
        return getRealService().searchSuppliers(keyword);
    }
}



