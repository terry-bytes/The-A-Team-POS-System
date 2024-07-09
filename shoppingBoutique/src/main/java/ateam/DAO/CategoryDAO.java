package ateam.DAO;

import ateam.Models.Category;
import java.util.List;

public interface CategoryDAO {

    void addCategory(Category category);

    Category getCategoryById(int category_ID); 

    List<Category> getAllCategories();

    void updateCategory(Category category);

    void deleteCategory(int category_ID); 
}
