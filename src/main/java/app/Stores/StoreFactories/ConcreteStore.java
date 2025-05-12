package app.Stores.StoreFactories;

import app.Classes.DataBaseConnection;

public class ConcreteStore implements Store {
    private final String id;
    private final String name;
    private final String location;
    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";

    public ConcreteStore(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    @Override public String getId() { return id; }
    @Override public String getName() { return name; }
    @Override public String getLocation() { return location; }

    @Override
    public void saveToDatabase() {
        DataBaseConnection db = new DataBaseConnection(dbPath);
        if (id == null) {
            db.execute("INSERT INTO stock(name, location) VALUES(?, ?)", name, location);
        } else {
            db.execute("UPDATE stock SET name=?, location=? WHERE id=?", name, location, id);
        }
    }

    @Override
    public void deleteFromDatabase() {
        if (id != null) {
            new DataBaseConnection(dbPath).execute("DELETE FROM stock WHERE id=?", id);
        }
    }
}