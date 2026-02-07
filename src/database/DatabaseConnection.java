/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Disath Damsutha
 */
public class DatabaseConnection {

    public static Connection getConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/foodcity_db",
                    "root",
                    "Disath06@#");

            System.out.println("Success! DB connected to Docker Cnotaienr.");
            return conn;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
            return null;

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL Driver Not Found!");
            return null;
        }
    }

    public static void main(String[] args) {
        getConnection();
    }
}
