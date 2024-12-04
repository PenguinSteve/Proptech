package tdtu.Proptech.service.category;

import tdtu.SpringCommerce.model.Category;

import java.util.List;

public interface CategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
}
