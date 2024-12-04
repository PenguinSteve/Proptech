package tdtu.Proptech.controller.RestAPIController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tdtu.SpringCommerce.model.Product;
import tdtu.SpringCommerce.request.AddProductRequest;
import tdtu.SpringCommerce.request.UpdateProductRequest;
import tdtu.SpringCommerce.response.ApiResponse;
import tdtu.SpringCommerce.service.product.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ApiProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new ApiResponse("Product found", productService.getProductById(id)));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found", null));
        }
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAllProducts(){
        return ResponseEntity.ok(new ApiResponse("Products found", productService.getAllProducts()));
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> createProduct(@RequestParam("name") String name,
                                                     @RequestParam("price") Double price,
                                                     @RequestParam("brand") String brand,
                                                     @RequestParam("color") String color,
                                                     @RequestParam("description") String description,
                                                     @RequestParam("category") String category,
                                                     @RequestPart("image") MultipartFile imageURL){
        try{
            AddProductRequest request = new AddProductRequest();
            request.setName(name);
            request.setPrice(price);
            request.setBrand(brand);
            request.setColor(color);
            request.setDescription(description);
            request.setCategory(category);
            request.setImageURL(imageURL);
            Product createdProduct = productService.addProduct(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Product created successfully", createdProduct));
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id,
                                                     @RequestParam("name") String name,
                                                     @RequestParam("price") Double price,
                                                     @RequestParam("brand") String brand,
                                                     @RequestParam("color") String color,
                                                     @RequestParam("description") String description,
                                                     @RequestParam("category") String category,
                                                     @RequestPart("image") MultipartFile imageURL) {
        try {
            UpdateProductRequest request = new UpdateProductRequest();
            request.setName(name);
            request.setPrice(price);
            request.setBrand(brand);
            request.setColor(color);
            request.setDescription(description);
            request.setCategory(category);
            request.setImageURL(imageURL);
            Product updatedProduct = productService.updateProduct(id, request);
            return ResponseEntity.ok(new ApiResponse("Product updated successfully", updatedProduct));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id){
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("Product deleted successfully", id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchProductsByCriteria(@RequestParam(value = "category", required = false) String category,
                                                                @RequestParam(value = "brand", required = false) String brand,
                                                                @RequestParam(value = "color", required = false) String color,
                                                                @RequestParam(value = "sortOrder", required = false) String sortOrder) {
        try {
            List<Product> products = productService.getProductsByCriteria(category, brand, color, sortOrder);
            return ResponseEntity.ok(new ApiResponse("Products found", products));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/searchByName")
    public ResponseEntity<ApiResponse> searchProductsByName(@RequestParam("name") String name) {
        try {
            List<Product> products = productService.getProductByNameContaining(name);
            return ResponseEntity.ok(new ApiResponse("Products found", products));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
