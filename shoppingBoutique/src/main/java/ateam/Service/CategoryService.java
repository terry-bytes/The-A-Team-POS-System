/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ateam.Service;

import ateam.Models.Category;
import java.util.List;

/**
 *
 * @author user
 */
public interface CategoryService {
    
    boolean addTheCategory(Category category);

    Category getTheCategoryById(int category_ID); 

    List<Category> getAllTheCategories();

    boolean updateTheCategory(Category category);

    boolean deleteACategory(int category_ID); 
    
}
