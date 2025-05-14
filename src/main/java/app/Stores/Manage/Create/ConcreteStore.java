package app.Stores.Manage.Create;
import app.Classes.DataBaseConnection;
public class ConcreteStore implements Store {
    private String dbPath;

    public ConcreteStore(String dbPath) {
        this.dbPath = dbPath;
    }

    @Override
    public DataBaseConnection createConnection() {
        return new DataBaseConnection(dbPath);
    }
}
