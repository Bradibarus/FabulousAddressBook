package MyApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class dbOperations {
    private Connection dbConnection;

    dbOperations() throws SQLException {
        this.dbConnection = MyApp.dbConnection.getConnection();
    }

    protected void loadFromDB(Collection<Contact> contacts) throws SQLException {
        String selectAllSQL =
                "SELECT *\n" +
                        "FROM contacts;";
        PreparedStatement selectAllStatement = dbConnection.prepareStatement(selectAllSQL);
        ResultSet resultSet = selectAllStatement.executeQuery();
        while (resultSet.next()){
            Contact contact = new Contact(
                    resultSet.getString(Contact.Fields.Name.toString().toLowerCase()),
                    resultSet.getString(Contact.Fields.Number.toString().toLowerCase()),
                    resultSet.getInt("id"));
            contacts.add(contact);
        }
    }



    protected void deleteContactDB(Contact contact) throws SQLException {
        String deleteContactSQL =
                "DELETE FROM contacts\n" +
                        "    WHERE contacts.id = ?";
        PreparedStatement deleteContactStatement = dbConnection.prepareStatement(deleteContactSQL);
        deleteContactStatement.setInt(1, contact.getId());
        deleteContactStatement.execute();
    }

    protected void addContactDB(Contact contact) throws SQLException {
        String addContactSQL =
                "INSERT INTO contacts(name, number)\n" +
                        "    VALUES (?, ?);";
        PreparedStatement addContactStatement = dbConnection.prepareStatement(addContactSQL);
        addContactStatement.setString(1, contact.getName());
        addContactStatement.setString(2, contact.getNumber());
        addContactStatement.execute();
        String getIdSQL = "SELECT LAST_INSERT_ID() as id;";
        PreparedStatement getIdStatement = dbConnection.prepareStatement(getIdSQL);
        ResultSet resultSet = getIdStatement.executeQuery();
        int id;
        if(resultSet.next()) id = resultSet.getInt(1);
        else throw new SQLException();
        contact.setId(id);
    }

    protected void changeContactDB(Contact contact, Contact.Fields type, String value) throws SQLException {
        String changeSQL =
                "UPDATE contacts " +
                "SET "+ type.toString() + " = ? " +
                "WHERE id = ?;";
        PreparedStatement changeStatement = dbConnection.prepareStatement(changeSQL);
        changeStatement.setString(1,value);
        changeStatement.setInt(2, contact.getId());
        changeStatement.executeUpdate();
    }

    protected void createContactsTable() throws SQLException {
        String createTableString =
                "CREATE TABLE contacts (\n" +
                        "  id int auto_increment,\n" +
                        "  name VARCHAR(20),\n" +
                        "  number VARCHAR(15),\n" +
                        "  PRIMARY KEY (id)\n" +
                        ");";
        PreparedStatement createTableStatement = dbConnection.prepareStatement(createTableString);
        createTableStatement.execute();
    }

    protected boolean tableExists(String tableName) throws SQLException {
        String checkTable = "SHOW TABLES LIKE ?";
        PreparedStatement statement = dbConnection.prepareStatement(checkTable);
        statement.setString(1,tableName);
        ResultSet checkResult = statement.executeQuery();
        if(checkResult.next()) return true;
        return false;
    }
}
