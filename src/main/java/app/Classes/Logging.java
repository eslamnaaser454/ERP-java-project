package app.Classes;



public class Logging {

    DataBaseConnection dataBaseConnection;
    Authentication authentication;
    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";




    public void addLog(String Log){
        authentication = new Authentication();
        dataBaseConnection = new DataBaseConnection(dbPath);
        dataBaseConnection.execute("insert into log(user,content) values ('"+authentication.getUser().get("username")+"','"+Log+"');");
    }



}
