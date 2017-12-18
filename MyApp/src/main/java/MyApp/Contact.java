package MyApp;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class Contact {
    final private StringProperty name;
    final private StringProperty number;
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
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

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

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
