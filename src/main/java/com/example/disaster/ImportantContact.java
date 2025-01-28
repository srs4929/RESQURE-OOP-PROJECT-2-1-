package com.example.disaster;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
public class ImportantContact implements Initializable {

     @FXML
      private TableView<ImportantContactSearch>table;
      @FXML
      private TableColumn<ImportantContactSearch,String>id;
      @FXML
      private TableColumn<ImportantContactSearch,String>name;
      @FXML
       private TableColumn<ImportantContactSearch,String>role;
      @FXML
       private TableColumn<ImportantContactSearch,String>phone;
      @FXML
      private TableColumn<ImportantContactSearch,String>location;
      ObservableList<ImportantContactSearch>importantcontactlist= FXCollections.observableArrayList();

      @FXML
      private Button home;
      @FXML
      private TextField keyword;
      public void initialize(URL url, ResourceBundle resource) {
          Database connectnow = new Database();
          Connection connectDB = connectnow.getConnection();
          String query = "SELECT id,Name,Role,Contact,Location FROM contact";
          try {
              // Create statement and execute query
              Statement statement = connectDB.createStatement();
              ResultSet resultSet = statement.executeQuery(query);

              // Clear the list to avoid duplicates if called multiple times
              importantcontactlist.clear();

              // Populate the list from the ResultSet
              while (resultSet.next()) {
                  Integer queryID=resultSet.getInt("id");
                  String queryName = resultSet.getString("Name");
                  String queryRole = resultSet.getString("Role");
                  String queryContact = resultSet.getString("Contact");
                  String queryLocation = resultSet.getString("Location");

                  importantcontactlist.add(new ImportantContactSearch(queryID,queryName, queryRole, queryContact, queryLocation));
              }

              // Bind columns (only do this once, not in the loop)

              name.setCellValueFactory(new PropertyValueFactory<>("name"));
              role.setCellValueFactory(new PropertyValueFactory<>("role"));
              phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
              location.setCellValueFactory(new PropertyValueFactory<>("location"));

              // Set the data to the table
              table.setItems(importantcontactlist);

              // Filter the elements (optional: apply filters to the table)
              FilteredList<ImportantContactSearch> filteredData = new FilteredList<>(importantcontactlist, b -> true);
              keyword.textProperty().addListener((observable,oldValue,newValue)->{

                  filteredData.setPredicate(ImportantContactSearch->{

                      //if no keyword found show what was before
                      if(newValue.isEmpty()||newValue.isBlank()||newValue==null)
                      {
                          return true;
                      }
                      String searchkeyword=newValue.toLowerCase();
                      if(ImportantContactSearch.getLocation().toLowerCase().indexOf(searchkeyword)>-1){

                          return true;
                      }
                      else if(ImportantContactSearch.getName().toLowerCase().indexOf(searchkeyword)>-1)
                      {
                          return true;
                      }
                      else if(ImportantContactSearch.getRole().toLowerCase().indexOf(searchkeyword)>-1)
                      {
                          return true;
                      }
                      else if(ImportantContactSearch.getPhone().toLowerCase().indexOf(searchkeyword)>-1)
                      {
                          return true;
                      }
                      else return false;

                  });
              });
              SortedList<ImportantContactSearch>sortedData=new SortedList<>(filteredData);
              sortedData.comparatorProperty().bind(table.comparatorProperty());
              table.setItems(sortedData);



          } catch (SQLException e) {
              Logger.getLogger(ImportantContact.class.getName()).log(Level.SEVERE, null, e);
              e.printStackTrace();
          }


      }

    public void menu(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Client.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException event) {
            event.printStackTrace();
        }
    }



}

