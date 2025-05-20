package app.Suppliers.Strategy;

import java.util.List;
import java.util.Map;
public class SupplierSearchContext {
    private SupplierSearchStrategy strategy;

    public void setStrategy(SupplierSearchStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Map<String, String>> executeSearch(String keyword) {
        return strategy.search(keyword);
    }
}

