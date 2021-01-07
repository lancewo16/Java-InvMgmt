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
public class Inventory {
    
    private final static ObservableList<Product> allProducts = FXCollections.observableArrayList();   
    private final static ObservableList<Part> allParts = FXCollections.observableArrayList();

    
    public static void addProduct(Product product)
    {
        allProducts.add(product);
    }

    /**
     *
     * @param productID
     * @return
     */
    public static boolean deleteProduct(int productID)
    {
        for(Product p : allProducts)
        {
            if(p.getProductID() == productID)
            {
                allProducts.remove(p);
                return true;
            }
        }
        return false;
    }

    
    public static void updateProduct(Product product)
    {
        allProducts.set(product.getProductID(), product);
    }


    static void addPart(Part part)
    {
        allParts.add(part);
    }

    
    public static boolean deletePart(int partID)
    {
        for(Part p : allParts)
        {
            if(p.getPartID() == partID)
            {
                allParts.remove(p);
                return true;
            }
        }
        return false;
    }

    
     
    public static Part lookupPart(int partId){ // TODO: change void to Part
        ObservableList<Part> parts = Inventory.getAllParts();
        Part foundPart = null;
        for(Part part : parts){
            if(part.getPartID() == partId){
                foundPart = part;
            }
        }
        
        return foundPart;  
        
    }
        
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> searchResults = FXCollections.observableArrayList();
        ObservableList<Part> parts = Inventory.getAllParts();
        for(Part part : parts){
            if(part.getName().contains(partName)){
                searchResults.add(part);
            }
        }
        
        return searchResults;
      
    }
    
     public static Product lookupProduct(int productID){
        ObservableList<Product> products = Inventory.getAllProducts();
        Product foundProduct = null;
        for(Product product : products){
            if(product.getProductID() == productID){
                foundProduct = product;
            }
        }
        
        return foundProduct;    
    }
    
    public static ObservableList<Product> lookupProduct(String productName){ // TODO: change void to ObservableList<Product>
        ObservableList<Product> searchResults = FXCollections.observableArrayList();
        ObservableList<Product> products = Inventory.getAllProducts();
        for(Product product : products){
            if(product.getName().contains(productName)){
                searchResults.add(product);
            }
        }
        
        return searchResults;
        
    }
    
    static void updatePart(Part part)
    {
        allParts.set(part.getPartID(), part);
    }

    
    static ObservableList<Part> getAllParts()
    {
        return allParts;
    }

    
    public static int getPartsCount()
    {
        return allParts.size();
    }

    
    public static int getProductsCount()
    {
        return allProducts.size();
    }

    public static ObservableList<Product> getAllProducts()
    {
        return allProducts;
    }   
}

