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
public class ModifyPartScreenController implements Initializable {
    
    //These are for the Part TextFields, Label, and Radio Buttons
    @FXML private TextField partIDTextField;
    @FXML private TextField partNameTextField;
    @FXML private TextField partInventoryTextField;
    @FXML private TextField partCostTextField;
    @FXML private TextField partMaximumTextField;
    @FXML private TextField partMinimumTextField;
    @FXML private TextField partCompanyMachineTextField;
    @FXML private Label partCompanyMachineLabel;
    @FXML private RadioButton inHouseRadioButton;
    @FXML private RadioButton outsourcedRadioButton;

    private ToggleGroup partModifyToggleGroup;
    private boolean clickedInHouse;
    private final Part partModify;
    
    
    public ModifyPartScreenController()
    {
        this.partModify = getPartModified();
    }    

    public void userClicksOnRadioButton()
    {
        if(this.partModifyToggleGroup.getSelectedToggle().equals(inHouseRadioButton))
        {
            this.partCompanyMachineLabel.setText("Machine ID");
            clickedInHouse = true;
        }
        
        if(this.partModifyToggleGroup.getSelectedToggle().equals(outsourcedRadioButton))
        {
            this.partCompanyMachineLabel.setText("Company Name");
            clickedInHouse = false;
        }
    }
   

    public void userClicksOnSaveButton(ActionEvent event) throws IOException

    {
        String partIDString = partIDTextField.getText();
        String partName = partNameTextField.getText();
        String partInventory = partInventoryTextField.getText();
        String partPrice = partCostTextField.getText();
        String partMin = partMinimumTextField.getText();
        String partMax = partMaximumTextField.getText();
        String partCompanyMachine = partCompanyMachineTextField.getText();

        if (partInventory == null)
        {
            partInventory = "0";
        }
        
        if(partName.equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);    
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

            partModified.setPartID(Integer.parseInt(partIDString));
            partModified.setName(partName);
            partModified.setPrice(Double.parseDouble(partPrice));
            partModified.setStock(Integer.parseInt(partInventory));
            partModified.setMin(Integer.parseInt(partMin));
            partModified.setMax(Integer.parseInt(partMax));
            partModified.setMachineID(Integer.parseInt(partCompanyMachine));
            
            //try {

                //modifiedPart.isValid();
      
                if(partModify == null) {
                    partModified.setPartID(Inventory.getPartsCount());
                    Inventory.addPart(partModified);

                } else {
                    int partID = partModify.getPartID();
                    partModified.setPartID(partID);
                    Inventory.updatePart(partModified);

                } 

                Parent loader = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(loader);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();

            //} catch (InventoryException e) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("InventoryError");
                alert.setHeaderText("Part not valid");
                alert.setContentText("Part modifications invalid.");
                alert.showAndWait();

            //}

        } else {


            Outsourced partModified = new Outsourced();
            partModified.setName(partName);
            partModified.setPrice(Double.parseDouble(partPrice));
            partModified.setStock(Integer.parseInt(partInventory));
            partModified.setMin(Integer.parseInt(partMin));
            partModified.setMax(Integer.parseInt(partMax));
            partModified.setCompanyName(partCompanyMachine);

            if(partModify == null) {

                partModified.setPartID(Inventory.getPartsCount());
                Inventory.addPart(partModified);

            } else {

                int partID = partModify.getPartID();
                partModified.setPartID(partID);
                Inventory.updatePart(partModified);
            }

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
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partIDTextField.setDisable(true);
        partModifyToggleGroup = new ToggleGroup();
        this.inHouseRadioButton.setToggleGroup(partModifyToggleGroup);
        this.outsourcedRadioButton.setToggleGroup(partModifyToggleGroup);
        this.outsourcedRadioButton.setSelected(true);

        partIDTextField.setText(Integer.toString(partModify.getPartID()));
        partNameTextField.setText(partModify.getName());
        partInventoryTextField.setText(Integer.toString(partModify.getStock()));
        partCostTextField.setText(Double.toString(partModify.getPrice()));
        partMinimumTextField.setText(Integer.toString(partModify.getMin()));
        partMaximumTextField.setText(Integer.toString(partModify.getMax()));

        if(partModify instanceof InHouse)

        {
            partCompanyMachineTextField.setText(Integer.toString(((InHouse) partModify).getMachineID()));
            this.inHouseRadioButton.setSelected(true);
            this.partCompanyMachineLabel.setText("Machine ID");
            
            
        } else {

            partCompanyMachineTextField.setText(((Outsourced) partModify).getCompanyName());
            this.outsourcedRadioButton.setSelected(true);
            this.partCompanyMachineLabel.setText("Company Name");
        }

    }     
    
}
