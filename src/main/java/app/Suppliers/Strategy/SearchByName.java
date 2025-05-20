package app.Suppliers.Strategy;

import app.Suppliers.Proxy.SupplierService;

import java.util.List;
import java.util.Map;

public class SearchByName implements SupplierSearchStrategy {
    private final SupplierService supplierService;

    public SearchByName(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Override
    public List<Map<String, String>> search(String keyword) {
        return supplierService.searchSuppliers(keyword);
    }
}

