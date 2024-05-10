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
    }

    public Authentication(String username,String password,DataBaseConnection dataBaseConnection){
        setDatabaseConnection(dataBaseConnection);
        setUsername(username);
        setPassword(password);
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
