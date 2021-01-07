/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lbw_mims;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author theworthens
 */
public class Product {

    private int productID, stock, min, max;
    private double price;
    private String name;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    
    public int getProductID() 
    {
        return productID;
    }

    
    public void setProductID(int productID) 
    {
        this.productID = productID;
    }

    
    public int getStock() 
    {
        return stock;
    }

    
    public void setStock(int stock) 
    {
        this.stock = stock;
    }

    public int getMin() 
    {
        return min;
    }

    
    public void setMin(int min) 
    {
        this.min = min;
    }


    public int getMax()
    {
        return max;
    }


    public void setMax(int max) 
    {
        this.max = max;
    }

    
    public double getPrice() 
    {
        return price;
    }


    public void setPrice(double price) 
    {
        this.price = price;
    }


    public String getName() 
    {
        return name;
    }


    public void setName(String name) 
    {
        this.name = name;
    }

    
    void addAssociatedPart(Part part) 
    {
        this.associatedParts.add(part);
    }


    public boolean deleteAssociatedPart(int partID) 
    {
        return this.associatedParts.remove(lookupRelatedPart(partID));
    }

    
    private Part lookupRelatedPart(int partID)
    {
        for(Part p : associatedParts)
        { 
            if(p.getPartID() == partID) 
            {
                return p;
            }
        }
        return null;
    }

    
    ObservableList<Part> getAllAssociatedParts()
    {
        return associatedParts;
    }


    public int getAssociatedPartCount()
    {
        return associatedParts.size();
    }

    
    public boolean isValid() throws InventoryException
    {
        double partsPrice = 0.00;

        partsPrice = getAllAssociatedParts().stream().map((p) -> p.getPrice()).reduce(partsPrice, 
        (accumulator, _item) -> accumulator + _item);

        
        if(getName().equals("")) {

            throw new InventoryException("Please enter a product name.");
        }

        
        if(getPrice() <= 0)
        {
            throw new InventoryException("Error: The price must be greater than zero.");
        }

        
        if(getMin() < 0) 
        {
            throw new InventoryException("Error: The minimum inventory must be greater than zero.");
        }

        
        if(getMin() > getMax())
        {
            throw new InventoryException("Error: The minimum inventory must be less than the maximum inventory");
        }

        

        if(getStock() <= 0)
        {
            throw new InventoryException("Error: The current stock cannot be negative.");
        }


        if(getAssociatedPartCount() < 1)
        {
            throw new InventoryException("Error: At least one part must be associated with the product.");
        }

        
        if(getPrice() <= partsPrice)
        {
            throw new InventoryException("Error: The product price must be greater than the cost of its associated parts.");
        }

        
        if(getStock() < getMin() || getStock() > getMax()) 
        {
            throw new InventoryException("Error: The current inventory must be between the product's minimum and maximum stock levels.");
        }
        
        return true;
    }


    public void purgeAssociatedParts()
    {
        associatedParts = FXCollections.observableArrayList();
    }

}
