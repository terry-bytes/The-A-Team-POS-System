/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.Exception;

/**
 *
 * @author user
 */
public class InvalidPasswordException extends Exception {

    public InvalidPasswordException(String msg) {
        
        super(msg);
    }
    
    public InvalidPasswordException() {
        
        this("Error, wrong password inserted");
    }
    
    
}