/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.ServiceImpl;

import ateam.DAO.CategoryDAO;
import ateam.DAOIMPL.CategoryDaoImpl;
import ateam.Models.Category;
import ateam.Service.CategoryService;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author user
 */
public class CategoryServiceIMPL  implements CategoryService{

    
    private CategoryDAO categoryDao;
    private Connection connection;
    
    public CategoryServiceIMPL(CategoryDAO categoryDao) {
        this.categoryDao = categoryDao;
        categoryDao = new CategoryDaoImpl(connection);
    }
    
    
    

    public boolean addTheCategory(Category category) {
        
     return categoryDao.addCategory(category);
    }

    public Category getTheCategoryById(int category_ID) {
        
     return categoryDao.getCategoryById(category_ID);
    }

    public List<Category> getAllTheCategories() {
        
     return categoryDao.getAllCategories();
    }

    public boolean updateTheCategory(Category category) {
        
     return categoryDao.updateCategory(category);
    }

    public boolean deleteACategory(int category_ID) {
        
    return categoryDao.deleteCategory(category_ID);
    }
    
}
