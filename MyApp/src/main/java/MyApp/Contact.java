package MyApp;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "contacts")
public class Contact {
    final private StringProperty name;
    final private StringProperty number;

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId(){
        return id;
    }

    public Contact() {
        this.name = new SimpleStringProperty();
        this.number = new SimpleStringProperty();
    }

    public Contact(String name, String number) {
        this.name = new SimpleStringProperty(name);
        this.number = new SimpleStringProperty(number);
        this.id = -1;
    }

    public Contact(String name, String number, int id){
        this(name, number);
        this.id = id;
    }

    @Column
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Column
    public String getNumber() {
        return number.get();
    }

    public StringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    @Override
    public String toString(){
        return "Name: " + getName() + " Number: " + getNumber();
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null) return false;
        if(obj.getClass() != getClass()) {
            return false;
        }
        Contact other = (Contact) obj;
        boolean numbers = getNumber().equals(other.getNumber());
        boolean names = getName().equals(other.getName());

        return names && numbers;
    }

    @Override
    public int hashCode(){
        return Objects.hash(getName(), getNumber());
    }

    public enum Fields {
        Name, Number
    }
}
