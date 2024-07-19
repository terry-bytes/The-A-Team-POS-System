/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ateam.DAO;

import ateam.Models.Transaction;
import java.sql.SQLException;

/**
 *
 * @author user
 */
public interface TransactionDAO {
    void insertTransaction(Transaction transaction) throws SQLException;
    
}
