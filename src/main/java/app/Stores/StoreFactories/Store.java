package app.Stores.StoreFactories;

public interface Store {
    String getId();
    String getName();
    String getLocation();
    void saveToDatabase();
    void deleteFromDatabase();
}