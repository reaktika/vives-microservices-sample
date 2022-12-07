package be.reaktika.vives.service.logistics.product;

import be.reaktika.vives.furniture.api.model.product.Product;
import be.reaktika.vives.service.logistics.ControllerUtil;
import be.reaktika.vives.service.logistics.product.core.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("logistics")
public class InternalProductController {

    private final Logger logger = LoggerFactory.getLogger(InternalProductController.class);
    private final ProductRepository productRepository;

    public InternalProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping(value = "products")
    public ResponseEntity<List<Product>> getProducts() {
        logger.info("processing getProducts request");
        var products =  productRepository.getAllProducts()
                .stream()
                .map(ControllerUtil::modelToApi)
                .toList();
        return ResponseEntity.ok(products);
    }
}
