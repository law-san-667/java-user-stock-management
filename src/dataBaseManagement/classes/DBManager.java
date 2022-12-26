package dataBaseManagement.classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    public static Connection getConnection () throws Exception {
        try {
// Chargement du driver jdbc mysql
            Class.forName ("com.mysql.cj.jdbc.Driver");
// Ouverture de la connexion
            return DriverManager.getConnection("jdbc:mysql://localhost/app_user_products_manager", "root", "");
        } catch (ClassNotFoundException e) {
            throw new Exception ("Driver Class not found : '" + e.getMessage () + "' ");
        } catch (SQLException e) {
            throw new Exception ("Error : Unable to open connection with database !");
        }
    }

}
