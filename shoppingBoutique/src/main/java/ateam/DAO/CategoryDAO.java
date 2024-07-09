package ateam.DAO;

import ateam.Models.Category;
import java.util.List;

public interface CategoryDAO {

    boolean addCategory(Category category);

    Category getCategoryById(int category_ID); 

    List<Category> getAllCategories();

    boolean updateCategory(Category category);

    boolean deleteCategory(int category_ID); 
}
