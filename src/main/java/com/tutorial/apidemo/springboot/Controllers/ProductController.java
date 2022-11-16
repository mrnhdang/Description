package com.tutorial.apidemo.springboot.Controllers;

import com.tutorial.apidemo.springboot.Models.Product;
import com.tutorial.apidemo.springboot.Models.ResponseObject;
import com.tutorial.apidemo.springboot.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/Products")
public class ProductController {
    //DI = Dependency injection
    @Autowired
    private ProductRepository repository;

    @GetMapping("")
    // the request id: http://localhost:8080/api/v1/Products
    List<Product> getAllProducts(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id){
        Optional<Product> foundProduct =repository.findById(id);
        return foundProduct.isPresent() ?
            ResponseEntity.status(HttpStatus.MULTI_STATUS.OK).body(
                    new ResponseObject("ok","Query product successfully",foundProduct)
            ):
             ResponseEntity.status(HttpStatus.MULTI_STATUS.NOT_FOUND).body(
                    new ResponseObject("failed","Cannot find product with id = "+id,"")
            );
        }
    // ínsert product
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct){
        //2 products must not have the same name !
        List<Product> foundProducts =repository.findByProductName(newProduct.getProductName().trim());
        if(foundProducts.size()>0){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failes", "Product name already taken", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert successfully",repository.save(newProduct))
        );
    }
    //update, upsert = update if found, otherwise insert
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product newProduct , @PathVariable Long id){
        Product updatedProduct =  repository.findById(id)
                .map(product -> {
                    product.setProductName(newProduct.getProductName());
                    product.setPrice(newProduct.getPrice());
                    product.setYear(newProduct.getYear());
                    product.setUrl(newProduct.getUrl());
                    return repository.save(product);
                }).orElseGet(() -> {
                    newProduct.setId(id);
                    return repository.save(newProduct);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update product successfully", updatedProduct)
        );
    }
    //Delete product
    @DeleteMapping("{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id){
        boolean exists =repository.existsById(id);
        if(exists){
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok","Delete product successfully","")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed","Cannot find product to delete","")
        );
    }
}
