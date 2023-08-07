package com.example.ProductService.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ProductService.entity.Product;
import com.example.ProductService.exception.ProductServiceCustomException;
import com.example.ProductService.model.ProductRequest;
import com.example.ProductService.model.ProductResponse;
import com.example.ProductService.repository.ProductRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ProductService {


    @Autowired
    private ProductRepository productRepository;



    public long addProduct(ProductRequest productRequest) {
log.info("Adding Product");
  

 Product product=Product.builder()
.productName(productRequest.getProductName())
.price(productRequest.getPrice())
.quantity(productRequest.getQuantity())
.build();

productRepository.save(product);
log.info("Product Created....");
        return product.getProductId();
    }



    public ProductResponse getProductById(Long productId)  {
        log.info("Getting the product :{}",productId);
      Product product= productRepository.findById(productId)
      .orElseThrow(()->new ProductServiceCustomException("Product with given id is not found","PRODUCT_NOT_FOUND"));
ProductResponse productResponse=new ProductResponse();
BeanUtils.copyProperties(product, productResponse);
      
        return productResponse;
    }



    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce Qunatity {} for id:{}",quantity,productId);

        Product product=productRepository.findById(productId).orElseThrow(
            ()->new ProductServiceCustomException("Product with given id is not found","PRODUCT_NOT_FOUND"));

            if(product.getQuantity() < quantity){
                throw new ProductServiceCustomException("Product does not have sufficient quantity","INSUFFICIENT_QUANTITY");
            }

            product.setQuantity((product.getQuantity()-quantity));
            productRepository.save(product);
            log.info("Product Quantity Updated successfully");
            
    }
    
}
