package app.Classes;


import java.sql.*;
import java.util.*;

public class DataBaseConnection {
    public static final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private String host;

    private String db;
    private String user;
    private String password;

    private String jar;

    public DataBaseConnection(String host, String db, String user, String password) {

        setHost(host);
        setDb("jdbc:sqlite:"+db);
        setUser(user);
        setPassword(password);
        this.jar = "com.mysql.cj.jdbc.Driver";

    }

    public DataBaseConnection(String db) {

        setHost("");
        setDb("jdbc:sqlite:"+db);
        setUser("");
        setPassword("");
        this.jar = "org.sqlite.JDBC";

    }


    public boolean excute(String query){
        try {

            Class.forName(this.jar);
        }catch (ClassNotFoundException e){
            System.out.println("Class Not found exption");

            return false;
        }

        try {

            if (jar.equals("org.sqlite.JDBC")){
                connection = DriverManager.getConnection(getDb());

            }else {
                connection = DriverManager.getConnection(getHost()+getDb(),getUser(),getPassword());

            }

            //  connection = DriverManager.getConnection(getHost(),getUser(),getPassword());
            statement = connection.createStatement();
            statement.execute(query);
            connection.close();
            return true;
        }catch (SQLException e){
            System.out.println("sql exption");
            System.out.println("Database Src : "+getDb());
            System.out.println("Driver : "+this.jar);
            System.out.println(e.toString());

            return false;
        }

    }

    public List<Map<String,String>> select(String query){
        try {

            Class.forName(this.jar);
        }catch (ClassNotFoundException e){
            System.out.println("Class Not found exption");
            return null;
        }

        try {
            connection = DriverManager.getConnection(getDb());
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            String value;
            List<Map<String,String>> list = new ArrayList<>();
            while (resultSet.next()){
                Map<String,String> map = new HashMap<>();
                String coumnName ;
                for (int i = 1; i <= columnCount; i++) {
                    coumnName = metaData.getColumnName(i);
                    value = resultSet.getString(i);
                    map.put(coumnName,value);
                }
                list.add(map);
            }

            connection.close();
            return list;

        }catch (SQLException e){
            System.out.println("sql exption");
            System.out.println(e.getMessage());
            System.out.println(getDb());


            return null;
        }

    }

    public int insert(String query) {
        int generatedId = -1;
        try {
            Class.forName(this.jar);
            connection = DriverManager.getConnection(getDb());
            statement = connection.createStatement();
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            }
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }



    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}