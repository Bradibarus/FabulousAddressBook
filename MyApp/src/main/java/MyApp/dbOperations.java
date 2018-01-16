package MyApp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class dbOperations {
    private SessionFactory sessionFactory;
    private Session session;

    dbOperations() throws SQLException, IOException {
        this.sessionFactory = new Configuration()
                .configure()
                .addAnnotatedClass(Contact.class)
                .buildSessionFactory();
    }

    protected void loadFromDB(Collection<Contact> contacts) {
        session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            contacts.addAll(session.createQuery("from Contact").list());
            tx.commit();
        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }



    protected void deleteContactDB(Contact contact) {
        session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            session.delete(contact);
            tx.commit();
        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }

    protected void addContactDB(Contact contact) {
        session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            session.save(contact);
            tx.commit();
        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }

    protected void changeContactDB(Contact oldContact, Contact newContact) throws SQLException {
        session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        try{
            Contact ContactToEdit = session.get(Contact.class, oldContact.getId());
            ContactToEdit.setNumber(newContact.getNumber());
            ContactToEdit.setName(newContact.getName());
            tx.commit();
        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }
}
