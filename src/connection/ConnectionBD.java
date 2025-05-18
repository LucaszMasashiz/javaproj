/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Masashi
 */
public class ConnectionBD {

    private static ConnectionBD instance;
    private Connection connection;

    private static final String URL = "jdbc:postgresql://pgadmin.panel.javaworld.com.br:5433/lucas_ic7p28";
    private static final String USER = "lucas_ic7p28";
    private static final String PASSWORD = "masashi_zneG3K";

    private ConnectionBD() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Driver do PostgreSQL não encontrado.", ex);
        }
    }


    public static ConnectionBD getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new ConnectionBD();
        }
        return instance;
    }


    public Connection getConnection() {
        return connection;
    }


    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}

