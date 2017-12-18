package MyApp;

import com.mysql.fabric.jdbc.FabricMySQLDriver;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class dbConnection {
    static public Connection getConnection() throws SQLException {
        String url, username, password;
        Connection connection = null;
        Properties props = new Properties();
        try {
            InputStream propsIn = dbConnection.class.getClassLoader().getResourceAsStream("database.properties");
            props.load(propsIn);
            url = props.getProperty("jdbc.url");
            username = props.getProperty("jdbc.username");
            password = props.getProperty("jdbc.password");
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
