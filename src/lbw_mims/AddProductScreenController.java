/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lbw_mims;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static lbw_mims.MainScreenController.getProductModified;


/**
 * FXML Controller class
 *
 * @author theworthens
 */
public class AddProductScreenController implements Initializable {

    //These are for the Product/Part TextFields
    @FXML private TextField productIDTextField;
    @FXML private TextField productNameTextField;
    @FXML private TextField productInventoryTextField;
    @FXML private TextField partSearchTextField;
    @FXML private TextField productPriceTextField;
    @FXML private TextField productMinimumTextField;
    @FXML private TextField productMaximumTextField;

   //These are for the PartDelete TableView
    @FXML private TableView<Part> partDeleteTableView;
    @FXML private TableColumn<Part, Integer> colPartDeleteID;
    @FXML private TableColumn<Part, String> colPartDeleteName;
    @FXML private TableColumn<Part, Integer> colPartDeleteInventory;
    @FXML private TableColumn<Part, Double> colPartDeleteCost;
    
    //These are for the PartSearch TableView
    @FXML private TableView<Part> partSearchTableView;
    @FXML private TableColumn<Part, Integer> colPartSearchID;
    @FXML private TableColumn<Part, String> colPartSearchName;
    @FXML private TableColumn<Part, Integer> colPartSearchInventory;
    @FXML private TableColumn<Part, Double> colPartSearchCost;    

  
    private final Product productModified;
    private final ObservableList<Part> productParts = FXCollections.observableArrayList();

    

    public AddProductScreenController()
    {
        this.productModified = getProductModified();
    }

    public void userClicksOnSearchButton(ActionEvent event) throws IOException 
    
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
    
    public void userClicksOnAddButton()       
    {
        if(partSearchTableView.getSelectionModel().isEmpty())
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Oops!");
            alert.setHeaderText(null);
            alert.setContentText("Please choose a part to add!");
            alert.showAndWait();
    
    }else{
        
        Part part = partSearchTableView.getSelectionModel().getSelectedItem();
        productParts.add(part);
        populatePartDeleteTableView();   
    }    
  
    } 
        
    public void userClicksOnDeleteButton()
    {
    
        if(productParts.size() > 1)
        {
            Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete these parts?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
        
            Part part = partDeleteTableView.getSelectionModel().getSelectedItem();
            productParts.remove(part);
        }
        } else {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(" The minimum part requirements are not met.");
            alert.setContentText("At least one part must be associated with the product.");
            alert.showAndWait();
        }
    }


    public void userClicksOnSaveButton(ActionEvent event) throws IOException
     {

        String productName = productNameTextField.getText();
        String productInventory = productInventoryTextField.getText();
        String productCost = productPriceTextField.getText();
        String productMin = productMinimumTextField.getText();
        String productMax = productMaximumTextField.getText();
        

        Product newProduct = new Product();
        newProduct.setName(productName);
        newProduct.setPrice(Double.parseDouble(productCost));
        newProduct.setStock(Integer.parseInt(productInventory));
        newProduct.setMin(Integer.parseInt(productMin));
        newProduct.setMax(Integer.parseInt(productMax));

        

        for(Part p : productParts)

        {
            newProduct.addAssociatedPart(p);
        }  
         
        try {
            
            if(productModified == null) 
            {
               
            }
            if(newProduct.isValid())

            {
             newProduct.setProductID(Inventory.getProductsCount());            
             Inventory.addProduct(newProduct);  
           
            Parent loader = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(loader);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
            }

        } catch (InventoryException e) {

            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inventory Error");
            alert.setHeaderText("Product is not valid");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


        
      
    
    public void userClicksOnCancelButton(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to Cancel?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }   
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        int productAutoID = Inventory.getProductsCount();
        
        productIDTextField.setText("" + productAutoID);
        productIDTextField.setDisable(true);
        colPartSearchID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartID()).asObject());    
        colPartSearchName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colPartSearchInventory.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        colPartSearchCost.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        colPartDeleteID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartID()).asObject());
        colPartDeleteName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colPartDeleteInventory.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        colPartDeleteCost.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        populatePartSearchTableView();
        populatePartDeleteTableView();
    } 
    

       public void populatePartSearchTableView() 
    {
        partSearchTableView.setItems(Inventory.getAllParts());
    }

    
    public void populatePartDeleteTableView()
    {
        partDeleteTableView.setItems(productParts);
    }    
    
}
