package be.reakika.vives.api.web.controller;

import be.reaktika.vives.furniture.api.product.ProductsApiController;
import be.reaktika.vives.furniture.api.product.ProductsApiDelegate;
import org.springframework.stereotype.Component;

@Component
public class ProductController extends ProductsApiController {

    public ProductController(ProductsApiDelegate delegate) {
        super(delegate);
    }
}
