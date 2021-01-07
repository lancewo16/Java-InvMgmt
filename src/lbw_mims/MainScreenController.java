/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lbw_mims;

import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ButtonType;

/**
 *
 * @author theworthens
 */
public class MainScreenController implements Initializable {

    private static Part partModified;
    private static Product productModified;
    
    //These are all the buttons on the main screen 
    @FXML private Button exitButton;
    @FXML private Button partDeleteButton;
    @FXML private Button partModifyButton;
    @FXML private Button productDeleteButton;
    @FXML private Button productModifyButton;
    
    
    //These are the TextFields for searching products or parts
    @FXML private TextField partSearchTextField;
    @FXML private TextField productSearchTextField;

   //These are for the Product TableView
    @FXML private TableView<Product> productSearchTableView;
    @FXML private TableColumn<Product, Integer> colProductID;
    @FXML private TableColumn<Product, String> colProductName;
    @FXML private TableColumn<Product, Integer> colProductInventory;
    @FXML private TableColumn<Product, Double> colProductPrice;

    
    //These are for the Part TableView
    @FXML private TableView<Part> partSearchTableView;
    @FXML private TableColumn<Part, Integer> colPartID;
    @FXML private TableColumn<Part, String> colPartName;
    @FXML private TableColumn<Part, Integer> colPartInventory;
    @FXML private TableColumn<Part, Double> colPartCost;    

     public MainScreenController()

    {
        
    } 

    public static Part getPartModified()
    {
        return partModified;
    }

    public void setPartModified(Part partModified)
    {
        MainScreenController.partModified = partModified;
    }

    public static Product getProductModified()

    {
        return productModified;
    }

    public void setProductModified(Product productModified)
    {
        MainScreenController.productModified = productModified;
    }

    
    
    public void userClicksOnPartSearchButton(ActionEvent event) throws IOException
    {
            String searchText;
        searchText = partSearchTextField.getText().trim();
        ObservableList<Part> searchResults = FXCollections.observableArrayList();
        
        // Regex pattern for matches from Stack Overflow user tokhi
        // URL to source: https://stackoverflow.com/a/39531087
        Boolean isNumber = searchText.matches("^[0-9]*$");
        
        if(searchText.length() == 0){
            partSearchTextField.clear();
            searchResults = Inventory.getAllParts();
        } else if(isNumber) { // If searchText is a number, search parts by index
            int partId = Integer.parseInt(partSearchTextField.getText());
            
            // Ensure partId is not greater than length of parts list
            if(partId <= Inventory.getAllParts().size()){

                Part foundPart = Inventory.lookupPart(partId);
                searchResults.add(foundPart);
                
                
                System.out.println(foundPart);
            } else {                
                searchResults.clear(); // ensure search results are clear for invalid ID

            }
        } else { // if searchText is not a numbger, search parts list with string
            ObservableList foundParts = Inventory.lookupPart(searchText);
            searchResults.setAll(foundParts);
        }
        
        partSearchTableView.setItems(searchResults);
    
    }



     
    public void userClicksOnPartAddButton(ActionEvent event) throws IOException

    {
        
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("AddPartScreen.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
        
    }

    public void userClicksOnPartModifyButton(ActionEvent event) throws IOException

    { 
        partModified = partSearchTableView.getSelectionModel().getSelectedItem();   
        setPartModified(partModified);
       
        if (partSearchTableView.getSelectionModel().isEmpty())
    
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Oops!");
            alert.setHeaderText(null);
            alert.setContentText("Please choose a part to Modify!");
            alert.showAndWait();
    
    }else{
        
        Parent loader = FXMLLoader.load(getClass().getResource("ModifyPartScreen.fxml"));
        Scene scene = new Scene(loader);
        
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        }  
   
}
        public void userClicksOnPartDeleteButton(ActionEvent event) throws IOException
        {
            if(partSearchTableView.getSelectionModel().isEmpty())
            {
               Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Oops!");
            alert.setHeaderText(null);
            alert.setContentText("Please choose a part to Delete!");
            alert.showAndWait(); 
          
                
        
            }else{
        
         Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Add Product");
        alert.setContentText("Are you sure you want to delete this record?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
        
        ObservableList<Part> selectedPartRows, allParts;
        allParts = partSearchTableView.getItems();
        selectedPartRows = partSearchTableView.getSelectionModel().getSelectedItems();

        selectedPartRows.forEach((part) -> {
            allParts.remove(part);
        });

        }
    }
        }
        
    public void userClicksOnProductSearchButton(ActionEvent event) throws IOException
    {
        String searchText;
        searchText = productSearchTextField.getText().trim();
        ObservableList<Product> searchResults = FXCollections.observableArrayList();
        Product foundProduct = null;
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();
        
        
        if(searchText.length() == 0){
            productSearchTextField.clear();
            searchResults = Inventory.getAllProducts();
        }else{
            // Regex pattern for matches from Stack Overflow user tokhi
            // URL to source: https://stackoverflow.com/a/39531087
            
            Boolean isNumber = searchText.matches("^[0-9]*$");
            
            // If searchText is a number, search parts by index
            if(isNumber){
                int productId = Integer.parseInt(searchText);

                // Ensure partId is not greater than length of parts list
                if(productId <= Inventory.getAllProducts().size()){
                    foundProduct = Inventory.lookupProduct(productId);

                    // insert found product to Search results
                    searchResults.add(foundProduct);

                } else {  // notify user to enter valid part ID

                    searchResults.clear();  // ensure search results is empty if non-valid ID entered

                    System.out.println("Dialogue for invalid part ID");
                }
            } else { // if searchText is not a numbger, search parts list with string
                foundProducts = Inventory.lookupProduct(searchText);
                searchResults.setAll(foundProducts);
                System.out.println(productSearchTextField.getText());
            }
        }
        // display search results
        productSearchTableView.setItems(searchResults);
    }

            
               
    public void userClicksOnProductAddButton(ActionEvent event) throws IOException

    {

        Parent tableViewParent = FXMLLoader.load(getClass().getResource("AddProductScreen.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();

    }

    
    public void userClicksOnProductModifyButton(ActionEvent event) throws IOException

       { 
        productModified = productSearchTableView.getSelectionModel().getSelectedItem();   
        setProductModified(productModified);
       
        if (productSearchTableView.getSelectionModel().isEmpty())
    
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Oops!");
            alert.setHeaderText(null);
            alert.setContentText("Please choose a product to Modify!");
            alert.showAndWait();
    
    }else{
        
        Parent loader = FXMLLoader.load(getClass().getResource("ModifyProductScreen.fxml"));
        Scene scene = new Scene(loader);
        
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        }  
   
}
             
    public void userClicksOnPartSearchTableView()
    {

        if(partSearchTableView.getSelectionModel().isEmpty()) 
            return;
        
        this.partModifyButton.setDisable(false);
        this.partDeleteButton.setDisable(false);
        this.productDeleteButton.setDisable(true);
        this.productModifyButton.setDisable(true);
    }

    public void userClicksOnProductSearchTableView()
    {
        if(productSearchTableView.getSelectionModel().isEmpty()) 
            return;
        
        this.productModifyButton.setDisable(false);
        this.productDeleteButton.setDisable(false);
        this.partDeleteButton.setDisable(true);
        this.partModifyButton.setDisable(true);
    }

    public void userClicksOnProductDeleteButton()
      {
            if(productSearchTableView.getSelectionModel().isEmpty())
            {
               Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Oops!");
            alert.setHeaderText(null);
            alert.setContentText("Please choose a product to Delete!");
            alert.showAndWait(); 
          
                  
            }else{
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Add Product");
        alert.setContentText("Are you sure you want to delete this record?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
        
        ObservableList<Product> selectedProductRows, allProducts;
        allProducts = productSearchTableView.getItems();
        selectedProductRows = productSearchTableView.getSelectionModel().getSelectedItems();
        
        selectedProductRows.forEach((product) -> {
            allProducts.remove(product);
        });
    }

    }
      }
    
    public void userClicksOnExitButton(ActionEvent event)
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to Exit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
        
            Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        colPartID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartID()).asObject());
        colPartName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colPartInventory.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        colPartCost.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        colProductID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProductID()).asObject());
        colProductName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colProductInventory.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        colProductPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        
        this.productDeleteButton.setDisable(false);
        this.partDeleteButton.setDisable(false);
        this.partModifyButton.setDisable(false);
        this.productModifyButton.setDisable(false);
        
        
        productSearchTableView.setItems(Inventory.getAllProducts());
        partSearchTableView.setItems(Inventory.getAllParts());
        
    }

    
}
