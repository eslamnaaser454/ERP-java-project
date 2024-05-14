package app.Classes;



public class Logging {

    DataBaseConnection dataBaseConnection;
    Authentication authentication;



    public void addLog(String Log){
        authentication = new Authentication();
        dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
        dataBaseConnection.excute("insert into log(user,content) values ('"+authentication.getUser().get("username")+"','"+Log+"');");
    }



}
