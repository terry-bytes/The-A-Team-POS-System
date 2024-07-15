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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        Category category = new Category();
        if(connection != null){
            String sql = "SELECT category_name FROM categories WHERE category_ID = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1, category_ID);
                
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    if(resultSet.next()){
                        category.setCategory_ID(category_ID);
                        category.setCategory_name(resultSet.getString("category_name"));
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(CategoryDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return category;
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList();
        if(connection != null){
            String sql = "SELECT category_ID, category_name FROM categories";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
               
                
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    if(resultSet.next()){
                        Category category = new Category();
                        category.setCategory_ID(resultSet.getInt("category_ID"));
                        category.setCategory_name(resultSet.getString("category_name"));
                        categories.add(category);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(CategoryDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return categories;
    }

    @Override
    public boolean updateCategory(Category ctgr) {
        if(connection != null){
            String sql = "UPDATE categories SET category_name=? WHERE category_ID = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1, ctgr.getCategory_ID());
                
                if(preparedStatement.executeUpdate() > 0) return true;
            } catch (SQLException ex) {
                Logger.getLogger(CategoryDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    @Override
    public boolean deleteCategory(int i) {
        if(connection != null){
            String sql = "DELETE FROM categories WHERE category_ID = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1, i);
                
                if(preparedStatement.executeUpdate() > 0) return true;
            } catch (SQLException ex) {
                Logger.getLogger(CategoryDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

  
    
}
