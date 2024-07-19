/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.Models;

/**
 *
 * @author user
 */



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private int transaction_ID;
    private int product_ID;
    private int store_ID;
    private int quantity;
    private Timestamp transaction_date;
    private int employee_ID;
    private int previous_quantity;
}
