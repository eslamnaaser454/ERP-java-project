package app.Suppliers.Proxy;

import java.util.List;
import java.util.Map;

public interface SupplierService {
    List<Map<String, String>> getSuppliers();
    List<Map<String, String>> searchSuppliers(String keyword);

    // Add this method for company search
    List<Map<String, String>> searchSuppliersByCompany(String companyName);
    List<Map<String, String>> searchSuppliersByEmail(String keyword);

}
