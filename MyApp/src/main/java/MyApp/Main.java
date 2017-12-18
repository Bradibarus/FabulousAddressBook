package MyApp;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class Main extends Application{
    private static final Logger logger = Logger.getLogger("MyApp");
    private Model model;
    private Button addButton;
    private Button deleteButton;
    private Button editButton;
    private AddView addView;
    private EditView editView;
    private ObservableList<Contact> selected;
    TableView<Contact> table;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try{
            model = new Model();
        }catch (SQLException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Something went wrong");
            alert.setContentText("Ooops, unable to connect to database");

            alert.showAndWait();
        }

        primaryStage.setTitle("MyApp");

        TableColumn<Contact, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("name"));

        TableColumn<Contact, String> numberColumn = new TableColumn<>("Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("number"));

        table = new TableView<>();
        table.setItems(this.model.getAll());
        table.getColumns().addAll(nameColumn, numberColumn);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        selected = table.getSelectionModel().getSelectedItems();
        selected.addListener(new ListChangeListener<Contact>() {
            @Override
            public void onChanged(Change<? extends Contact> c) {
                if(selected.size()==1) editButton.setDisable(false);
                else editButton.setDisable(true);
            }
        });

        addButton = new Button();
        addButton.setText("Add Entry");
        addButton.setOnAction(e->{
            if(addView == null) addView = new AddView();
            addView.display();
        });

        deleteButton = new Button();
        deleteButton.setText("Delete Entry");
        deleteButton.setOnAction(e->{
            List selectedContact =  table.getSelectionModel().getSelectedItems();
            selectedContact.forEach(c -> model.delete((Contact)c));
        });

        editButton = new Button();
        editButton.setText("Edit Entry");
        editButton.setDisable(true);
        editButton.setOnAction(e->{
            if(editView == null) editView = new EditView();
            editView.display();
        });

        HBox buttons = new HBox(addButton, deleteButton, editButton){{setSpacing(10);}};
        VBox layout = new VBox(table, buttons);
        layout.setSpacing(10);
        layout.setPadding(new Insets(20,20,20,20));

        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public class AddView {
        public void display(){
            Stage window = new Stage();
            window.setTitle("Add entry");
            window.initModality(Modality.APPLICATION_MODAL);

            TextField nameField = new TextField();
            nameField.setPromptText("Enter the name");

            TextField numberField = new TextField();
            numberField.setPromptText("Enter the number");

            Button okButton = new Button("Add");
            okButton.setOnAction(e->{
                Contact newContact = new Contact(nameField.getText(), numberField.getText());
                try {
                    model.add(newContact);
                } catch (Exception e1) {
                    new Alert(Alert.AlertType.ERROR){{
                        setTitle("Error");
                        setHeaderText("Something went wrong");
                        setContentText(e1.getMessage());
                    }}.showAndWait();
                }
                window.close();
            });

            VBox layout = new VBox(nameField, numberField, okButton);
            layout.setPadding(new Insets(20,20,20,20));
            layout.setSpacing(10);
            Scene scene = new Scene(layout);
            window.setScene(scene);
            window.show();
        }
    }

    public class EditView {
        public void display(){
            Stage window = new Stage();
            window.setTitle("Edit entry");
            window.initModality(Modality.APPLICATION_MODAL);

            Contact selectedContact = table.getSelectionModel().getSelectedItem();

            TextField nameField = new TextField();
            nameField.setText(selectedContact.getName());

            TextField numberField = new TextField();
            numberField.setText(selectedContact.getNumber());

            Button okButton = new Button("Save");
            okButton.setOnAction(e->{
                Contact newContact = new Contact(nameField.getText(), numberField.getText());
                try {
                    model.changeContact(selectedContact, newContact);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    new Alert(Alert.AlertType.ERROR){{
                        setTitle("Error");
                        setHeaderText("Something went wrong");
                        setContentText(e1.getMessage());
                    }}.showAndWait();
                }
                window.close();
            });

            VBox layout = new VBox(nameField, numberField, okButton);
            layout.setPadding(new Insets(20,20,20,20));
            layout.setSpacing(10);
            Scene scene = new Scene(layout);
            window.setScene(scene);
            window.show();
        }
    }
}
