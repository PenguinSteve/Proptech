package tdtu.Proptech.service.product;

import tdtu.SpringCommerce.model.Product;
import tdtu.SpringCommerce.request.AddProductRequest;
import tdtu.SpringCommerce.request.UpdateProductRequest;

import java.util.List;

public interface ProductService {
    Product addProduct(AddProductRequest request);
    Product getProductById(Long id);
    List<Product> getProductByNameContaining(String name);
    void deleteProductById(Long id);
    Product updateProduct(Long id, UpdateProductRequest request);
    List<Product> getAllProducts();
    List<Product> getProductsByCriteria(String category, String brand, String color, String sortOrder);
}