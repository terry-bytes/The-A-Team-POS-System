/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.DAOIMPL;

import ateam.DAO.CategoryDAO;
import ateam.Models.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Train 01
 */
public class CategoryDaoImpl implements CategoryDAO{
    private Connection connection;

    public CategoryDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean addCategory(Category category) {
        if(connection != null){
            String sql = "INSERT INTO categories (category_name) VALUES (?)";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, category.getCategory_name());
                
                if(preparedStatement.executeUpdate() > 0) return true;
            } catch (SQLException ex) {
                Logger.getLogger(CategoryDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    @Override
    public Category getCategoryById(int category_ID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Category> getAllCategories() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateCategory(Category ctgr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteCategory(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

  
    
}
