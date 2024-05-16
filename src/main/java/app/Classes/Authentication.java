package app.Classes;
import app.Classes.DataBaseConnection;

import java.util.List;
import java.util.Map;

public class Authentication {
    DataBaseConnection dataBaseConnection;
    private String username;
    private String password;

    public Authentication(Authentication authentication){
        setDataBaseConnection(authentication.getDataBaseConnection());
        setUsername(authentication.getPassword());
        setPassword(authentication.getPassword());
        clearSesions();
        createSession();

    }

    public Authentication(String username,String password,DataBaseConnection dataBaseConnection){
        setDatabaseConnection(dataBaseConnection);
        setUsername(username);
        setPassword(password);
        clearSesions();
        createSession();
    }

    public Authentication(){
    getSession();
    }


    public Map<String,String> getSession(){
        dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
        List<Map<String,String>> list = dataBaseConnection.select("select * from session;");
        if (list == null || list.isEmpty()){
            return null;
        }else {
            username = list.getFirst().get("username");
            password = list.getFirst().get("password");
            System.out.println("session geted with values("+username+" , "+password+")");
            return list.getFirst();
        }
    }

    public void createSession(){
        dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
        List<Map<String,String>> list = dataBaseConnection.select("select * from session;");
        if (list == null || list.isEmpty()){
            dataBaseConnection.excute("insert into session(username,password ) values ('"+getUsername()+"','"+getPassword()+"');");
            System.out.println("Created a new session");
        }else {
            Map<String,String> map = list.getFirst();
            dataBaseConnection.excute("update session set username='"+getUsername()+"',password='"+getPassword()+"', where id = "+map.get("id")+";");
            System.out.println("Updated a session");

        }
    }

    public void clearSesions(){
        dataBaseConnection = new DataBaseConnection(DataBaseConnection.dbPath);
        dataBaseConnection.excute("delete from session;");
        System.out.println("sessions cleared");
    }


    public DataBaseConnection getDataBaseConnection() {
        return dataBaseConnection;
    }

    public void setDataBaseConnection(DataBaseConnection dataBaseConnection) {
        this.dataBaseConnection = dataBaseConnection;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setDatabaseConnection(DataBaseConnection dataBaseConnection){
        this.dataBaseConnection = dataBaseConnection;
    }








    public boolean check(){
        String query = "select * from users where username='"+getUsername()+"' and password='"+getPassword()+"';";
        List<Map<String,String >> list = dataBaseConnection.select(query);
        if (list != null) {
            System.out.println("check opration done without exption");

            return !list.isEmpty();
        }else {
            System.out.println("check opration done with exption");

            return false;

        }
        }


    public Map<String,String> getUser(){
        String query = "select * from users where username='"+getUsername()+"' and password='"+getPassword()+"';";
        List<Map<String,String >> list = dataBaseConnection.select(query);
        if (!list.isEmpty() ){

            return list.getFirst();
        }else
            return null;

    }


}
