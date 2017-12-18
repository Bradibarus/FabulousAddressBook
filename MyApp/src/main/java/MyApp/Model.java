package MyApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class Model {
    private ObservableList<Contact> contacts;
    private MyApp.dbOperations dbOperations;

    public Model() throws SQLException {
        try {
            dbOperations = new dbOperations();
            contacts = FXCollections.observableList(new ArrayList<Contact>());
            if(!dbOperations.tableExists("Contacts")) {
                Logger.getLogger("MyApp").info("Table does not exist \n Creating table...");
                dbOperations.createContactsTable();
                Logger.getLogger("MyApp").info("Table successfully created");
            }else{
                Logger.getLogger("MyApp").info("Loading data...");
                dbOperations.loadFromDB(contacts);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } catch (NullPointerException e){
            e.printStackTrace();
        };
    }

    public int size(){
        return contacts.size();
    }

    public void add(Contact contact) throws Exception {
        try {
            if(contacts.contains(contact)) throw new Exception("Contact already exists");
            this.contacts.add(contact);
            dbOperations.addContactDB(contact);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Contact contactToDelete) {
        try {
            if (contacts.removeAll(contactToDelete)) {
                dbOperations.deleteContactDB(contactToDelete);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Contact> getAll(){
        return contacts;
    }

    /*
    Well, I found this method great for some reflection magic testing
    May be it is now a bit slower  : )
    But so flexible and fabulous (Unlike all the JDBC stuff)
     */
    public List<Contact> searchFor(Contact.Fields type, String searchString){
        List<Contact> searchResult = new ArrayList<>(contacts.size());
        Class cl = Contact.class;
        String methodName = "get"+type.toString();
        Method method = null;
        try {
            method = cl.getMethod(methodName);
            for(Contact contact: contacts){
                if(((String)method.invoke(contact)).equals(searchString)){
                    searchResult.add(contact);
                }
            }
            if(searchResult.size() > 0) return searchResult;
            else {
                for(Contact contact: contacts){
                    if(((String)method.invoke(contact)).contains(searchString)){
                        searchResult.add(contact);
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    public void changeContact(Contact oldContact, Contact.Fields type, String changeValue){
        Class cl = Contact.class;
        String methodName = "set"+type.toString();
        Method method = null;
        try {
            method = cl.getMethod(methodName, String.class);
            for(Contact contact: contacts){
                if (contact.equals(oldContact)) {
                    dbOperations.changeContactDB(oldContact, type, changeValue);
                    method.invoke(contact, changeValue);
                    break;
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeContact(Contact oldContact, Contact newContact) throws Exception {
        try {
            for(Contact contact: contacts){
                if (contact.equals(oldContact)) {
                    if(contacts.contains(newContact)) throw new Exception("Contact with such fields already exists");
                    for(Contact.Fields field: Contact.Fields.values()){
                        String setMethodName = "set" + field.toString() ;
                        String getMethodName = "get" + field.toString();
                        Method setMethod = Contact.class.getMethod(setMethodName, String.class);
                        Method getMethod = Contact.class.getMethod(getMethodName);
                        String changeValue = (String) getMethod.invoke(newContact);
                        dbOperations.changeContactDB(oldContact, field, changeValue);
                        setMethod.invoke(contact, changeValue);

                    }
                }
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
