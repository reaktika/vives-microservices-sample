package be.reaktika.vives.api.web.controller;

import be.reaktika.vives.api.ports.LogisticsPort;
import be.reaktika.vives.furniture.api.model.product.Product;
import be.reaktika.vives.furniture.api.product.ProductsApiDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductApiDelegate implements ProductsApiDelegate {
    private final Logger logger = LoggerFactory.getLogger(ProductApiDelegate.class);

    private final LogisticsPort logistics;

    public ProductApiDelegate(LogisticsPort logistics) {
        this.logistics = logistics;
    }

    @Override
    public ResponseEntity<List<Product>> getProducts() {
        logger.info("get products from logisticsService");
        return ResponseEntity.ok(logistics.getProducts().block());
    }

    @Override
    public ResponseEntity<Product> createProduct(Product product) {
        return ProductsApiDelegate.super.createProduct(product);
    }

    @Override
    public ResponseEntity<Product> getProduct(String id) {
        return ProductsApiDelegate.super.getProduct(id);
    }



    @Override
    public ResponseEntity<Product> updateProduct(Product product) {
        return ProductsApiDelegate.super.updateProduct(product);
    }

}
