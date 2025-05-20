package app.Suppliers.Strategy;

import app.Suppliers.Proxy.SupplierService;
import java.util.List;
import java.util.Map;

public class SearchByEmail implements SupplierSearchStrategy {

    private final SupplierService supplierService;

    public SearchByEmail(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Override
    public List<Map<String, String>> search(String keyword) {
        return supplierService.searchSuppliersByEmail(keyword);
    }
}
