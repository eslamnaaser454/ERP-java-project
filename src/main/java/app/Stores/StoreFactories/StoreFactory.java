package app.Stores.StoreFactories;

public class StoreFactory {
    public static Store createStore(String id, String name, String location) {
        return new ConcreteStore(id, name, location);
    }

    public static Store createStore(String name, String location) {
        return createStore(null, name, location);
    }
}