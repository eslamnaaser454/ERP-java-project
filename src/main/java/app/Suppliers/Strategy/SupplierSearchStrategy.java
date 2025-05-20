package app.Suppliers.Strategy;

import java.util.List;
import java.util.Map;

public interface SupplierSearchStrategy {
    List<Map<String, String>> search(String keyword);
}
