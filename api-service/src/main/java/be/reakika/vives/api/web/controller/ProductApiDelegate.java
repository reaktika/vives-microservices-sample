package be.reakika.vives.api.web.controller;

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


    @Override
    public ResponseEntity<List<Product>> productsGet() {
        logger.info("get products");
        return ProductsApiDelegate.super.productsGet();
    }

    @Override
    public ResponseEntity<Product> productsIdGet(String id) {
        return ProductsApiDelegate.super.productsIdGet(id);
    }

    @Override
    public ResponseEntity<Product> productsPost(Product product) {
        return ProductsApiDelegate.super.productsPost(product);
    }

    @Override
    public ResponseEntity<Product> productsPut(Product product) {
        return ProductsApiDelegate.super.productsPut(product);
    }
}
