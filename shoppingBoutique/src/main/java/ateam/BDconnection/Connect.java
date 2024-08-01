package ateam.BDconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Train 01
 */
public class Connect {

    public Connect() {
    }
    private static final String URL = "jdbc:mysql://localhost:3306/carolsboutique?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "@Mysql23";


    
    private Connection connection;
    
    public Connection connectToDB(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return connection;
    }

}