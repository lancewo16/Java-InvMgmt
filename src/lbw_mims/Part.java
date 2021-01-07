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
public abstract class Part {

  private int partID, stock, min, max;
  private String name;
  private double price;

   
    public void setPartID(int partID)   
    {
        this.partID = partID;
    }

    
    public int getStock()  
    {
        return stock;
    }

   public int getPartID() 
    {
        return partID;
    }
    
    public void setStock(int stock) 
    {
        this.stock = stock;
    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
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

    
    public boolean isValid() throws InventoryException 
    {
        if(getName().equals("")) 
        {

            throw new InventoryException("Error: Part name required.");
        }
        
        if(getPrice() < 0)

        {
            throw new InventoryException("Error: The price must be more than zero.");
        }

        if(getStock() < getMin() || getStock() > getMax())
        {
            throw new InventoryException("Error: The quantity in-stock must be between the minimum and maximum inventory.");
        }
        
        if(getMin() < 0) 
        {
            throw new InventoryException("Error: The minimum inventory must be more than zero.");
        }

        
        if(getMin() > getMax())
        {
            throw new InventoryException("Error: The minimum inventory cannot be greater than the maximum inventory");
        }

        if(getStock() < getMin() || getStock() > getMax())
        {
            throw new InventoryException("Error: The quantity in-stock must be between the minimum and maximum inventory.");
        }
        return true;
        
    }
}


