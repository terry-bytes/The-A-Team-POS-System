/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.Models;

import java.sql.Timestamp;

/**
 *
 * @author user
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Return {
    private int return_ID;
    private int sales_ID;
    private int product_ID;
    private int quantity;
    private Timestamp return_date;
    private String email;
    private String reason;
}
