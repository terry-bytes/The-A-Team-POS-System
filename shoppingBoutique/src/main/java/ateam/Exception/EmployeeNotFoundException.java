/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.Exception;

/**
 *
 * @author user
 */
public class EmployeeNotFoundException extends Exception {
    
     public EmployeeNotFoundException(String msg) {
         super(msg);
    }

    public EmployeeNotFoundException() {
        this("User not found");
    }
    
    
    
    
}