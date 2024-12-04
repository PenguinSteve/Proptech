package tdtu.Proptech.controller.RestAPIController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tdtu.SpringCommerce.response.ApiResponse;
import tdtu.SpringCommerce.service.category.CategoryService;

@RestController
@RequestMapping("/api/category")
public class ApiCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAllCategories(){
        try{
            return ResponseEntity.ok(new ApiResponse("Categories found", categoryService.getAllCategories()));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(new ApiResponse("Category found", categoryService.getCategoryById(id)));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Category not found", null));
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
        try{
            return ResponseEntity.ok(new ApiResponse("Category found", categoryService.getCategoryByName(name)));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Category not found", null));
        }
    }
}