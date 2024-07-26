/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.Models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 *
 * @author user
 */
public class InventorySummary {
    private int storeID;
    private int productID;
    private String productName;
    private String productSKU;
    private String color;
    private String size;
    private int totalQuantity;
    private String productImagePath;

    // Getters and setters for each field
}

