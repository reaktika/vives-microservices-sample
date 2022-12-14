package be.reaktika.vives.api.web.controller;

import be.reaktika.vives.api.ports.LogisticsPort;
import be.reaktika.vives.furniture.api.model.product.Product;
import be.reaktika.vives.furniture.api.product.ProductsApiController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class ProductController extends ProductsApiController {

    private final LogisticsPort logistics;
    public ProductController(NativeWebRequest request, LogisticsPort logistics) {
        super(request);
        this.logistics = logistics;
    }

    @Override
    public CompletableFuture<ResponseEntity<List<Product>>> getProducts() {
        return logistics.getProducts().toFuture().thenApply(ResponseEntity::ok);
    }
}
