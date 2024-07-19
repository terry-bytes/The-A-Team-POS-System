/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.DAOIMPL;

/**
 *
 * @author user
 */


import ateam.BDconnection.Connect;
import ateam.DAO.TransactionDAO;
import ateam.Models.Transaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionDAOIMPL implements TransactionDAO{
    private final Connection connection;

    public TransactionDAOIMPL() {
        Connect connect = new Connect();
        this.connection = connect.connectToDB();
    }

    

    @Override
    public void insertTransaction(Transaction transaction) throws SQLException {
        String query = "INSERT INTO transactions (product_ID, store_ID, quantity, transaction_date, employee_ID, previous_quantity) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, transaction.getProduct_ID());
            stmt.setInt(2, transaction.getStore_ID());
            stmt.setInt(3, transaction.getQuantity());
            stmt.setTimestamp(4, transaction.getTransaction_date());
            stmt.setInt(5, transaction.getEmployee_ID());
            stmt.setInt(6, transaction.getPrevious_quantity());
            stmt.executeUpdate();
        }
    }
}

