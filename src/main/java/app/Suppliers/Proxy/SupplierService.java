package app.Suppliers.Proxy;

import java.util.List;
import java.util.Map;

public interface SupplierService {
    List<Map<String, String>> getSuppliers();
    List<Map<String, String>> searchSuppliers(String keyword);
}



