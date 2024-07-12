/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.Models;

/**
 *
 * @author Train 09
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    private String sender;
    private String receiver;
    private String message;
    private String password;
    private String subject;

    
    public Email(String receiver, String message, String subject) {
    this.receiver = receiver;
    this.message = message;
    this.subject = subject;
}
 public Email(String sender, String password) {
        this.sender = sender;
        this.password = password;
    }    


}
