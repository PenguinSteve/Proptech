package tdtu.Proptech.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tdtu.SpringCommerce.model.Category;
import tdtu.SpringCommerce.model.Product;
import tdtu.SpringCommerce.repository.CategoryRepository;
import tdtu.SpringCommerce.repository.ProductRepository;
import tdtu.SpringCommerce.request.AddProductRequest;
import tdtu.SpringCommerce.request.UpdateProductRequest;
import tdtu.SpringCommerce.service.image.ImageService;

import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageService imageService;

    @Override
    public Product addProduct(AddProductRequest request) {
        Category category = categoryRepository.findByName(request.getCategory())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Product product = createProduct(request, category);
        return productRepository.save(product);
    }

    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getPrice(),
                request.getBrand(),
                request.getColor(),
                request.getDescription(),
                imageService.uploadImg(request.getImageURL()),
                category);
    }
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public List<Product> getProductByNameContaining(String name) {
        return productRepository.findByNameContaining(name);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        () -> {throw new RuntimeException("Product not found!");});
    }

    @Override
    public Product updateProduct(Long id, UpdateProductRequest request) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        Product updatedProduct = updateExistingProduct(existingProduct, request);
        return productRepository.save(updatedProduct);
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setColor(request.getColor());
        existingProduct.setDescription(request.getDescription());
        Category category = categoryRepository.findByName(request.getCategory()).orElseThrow(() -> new RuntimeException("Category not found"));
        existingProduct.setCategory(category);
        if (request.getImageURL() != null && !request.getImageURL().isEmpty()) {
            existingProduct.setImageURL(imageService.uploadImg(request.getImageURL()));
        }
        return existingProduct;
    }

    @Override
    public List<Product> getProductsByCriteria(String category, String brand, String color, String sortOrder) {
        if ("asc".equalsIgnoreCase(sortOrder)) {
            return productRepository.findByCriteriaOrderByPriceAsc(category, brand, color);
        } else if ("desc".equalsIgnoreCase(sortOrder)) {
            return productRepository.findByCriteriaOrderByPriceDesc(category, brand, color);
        }
        return productRepository.findByCriteria(category, brand, color);
    }


}