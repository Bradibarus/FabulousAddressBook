package MyApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
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
            contacts = FXCollections.observableList(new ArrayList<Contact>());
        try {
            dbOperations = new dbOperations();
            dbOperations.loadFromDB(contacts);
            contacts.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (contacts.removeAll(contactToDelete)) {
            dbOperations.deleteContactDB(contactToDelete);
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


    public void changeContact(Contact oldContact, Contact newContact) throws Exception {
        dbOperations.changeContactDB(oldContact, newContact);
        oldContact.setNumber(newContact.getNumber());
        oldContact.setName(newContact.getName());
    }
}
