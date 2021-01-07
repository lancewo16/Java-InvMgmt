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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import static lbw_mims.MainScreenController.getPartModified;


/**
 * FXML Controller class
 *
 * @author theworthens
 */
public class AddPartScreenController implements Initializable {
    
    
    @FXML private TextField partIDTextField;
    @FXML private TextField partNameTextField;
    @FXML private TextField partCostTextField;
    @FXML private TextField partMinimumTextField;
    @FXML private TextField partMaximumTextField;
    @FXML private TextField partCompanyMachineTextField;
    @FXML private TextField partInventoryTextField;
    @FXML private Label partCompanyMachineLabel;
    @FXML private RadioButton inHouseRadioButton;
    @FXML private RadioButton outsourcedRadioButton;
    
    private boolean clickedInHouse;
    private ToggleGroup partAddToggleGroup;

    
    public void userClicksOnSaveButton(ActionEvent event) throws IOException
    {
        String partName = partNameTextField.getText();
        String partPrice = partCostTextField.getText();
        String partMin = partMinimumTextField.getText();
        String partMax = partMaximumTextField.getText();
        String partInventory = partInventoryTextField.getText();
        String partCompanyMachine = partCompanyMachineTextField.getText();
    

        if (partInventory == null)
        {
            partInventory = "0";
        }
        
         if(partName.equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Missing Part Name");    
            alert.setContentText("Part name required.");
            alert.showAndWait();
            return;
        }
        
        if(Integer.parseInt(partInventory) > Integer.parseInt(partMax) 
                || Integer.parseInt(partInventory) < Integer.parseInt(partMin))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid inventory quantity");    
            alert.setContentText("Current inventory must be between minimum and maximum.");
            alert.showAndWait();
            return;

        }   
        if(Double.parseDouble(partPrice) <= 0)
            {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Dollar Amount");    
            alert.setContentText("The price must be more than zero.");
            alert.showAndWait();
            return;
            
        } 
        if(partCompanyMachine.equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Missing Field");    
            alert.setContentText("Company Name/Machine ID required.");
            alert.showAndWait();
            return;
        } 
        
        
        if (clickedInHouse) {

            InHouse partModified = new InHouse();
            partModified.setName(partName);
            partModified.setMachineID(Integer.parseInt(partCompanyMachine));
            partModified.setPrice(Double.parseDouble(partPrice));
            partModified.setStock(Integer.parseInt(partInventory));
            partModified.setMin(Integer.parseInt(partMin));
            partModified.setMax(Integer.parseInt(partMax));

            try {

                partModified.isValid();
                partModified.setPartID(Inventory.getPartsCount());
                Inventory.addPart(partModified);
                
                Parent loader = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(loader);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();

            } catch (InventoryException e) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("This part is not valid");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }

        } else {
            
            Outsourced partModified = new Outsourced();
            partModified.setName(partName);
            partModified.setPrice(Double.parseDouble(partPrice));
            partModified.setStock(Integer.parseInt(partInventory));
            partModified.setMin(Integer.parseInt(partMin));
            partModified.setMax(Integer.parseInt(partMax));
            partModified.setCompanyName(partCompanyMachine);
            partModified.setPartID(Inventory.getPartsCount());
            Inventory.addPart(partModified);
            
            Parent loader = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(loader);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
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
    
      public void userChangesRadioButton()
    {
        if(this.partAddToggleGroup.getSelectedToggle().equals(inHouseRadioButton))
        {
            this.partCompanyMachineLabel.setText("Machine ID Number");
            this.partCompanyMachineTextField.setPromptText("Machine ID Number");
            clickedInHouse = true;
        }

        if(this.partAddToggleGroup.getSelectedToggle().equals(outsourcedRadioButton))
        {
            this.partCompanyMachineLabel.setText("Company Name");
            this.partCompanyMachineTextField.setPromptText("Company Name");
            clickedInHouse = false;
        }
    }

    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        partIDTextField.setText("" + Inventory.getPartsCount());
        partIDTextField.setDisable(true);
        clickedInHouse = false;
        partAddToggleGroup = new ToggleGroup();
        
        this.inHouseRadioButton.setToggleGroup(partAddToggleGroup);
        this.outsourcedRadioButton.setToggleGroup(partAddToggleGroup);        
        this.outsourcedRadioButton.setSelected(true);
    }    
    
}
