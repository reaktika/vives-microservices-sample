package be.reaktika.vives.service.logistics.product.core;

import be.reaktika.vives.service.logistics.product.core.model.ProductEntity;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.List;

@Repository
public class ProductRepository {
    public List<ProductEntity> getAllProducts() {
        return List.of(new ProductEntity(1L, "Ergonomic deskchair", URI.create("https://m.media-amazon.com/images/I/81jsk+89WVL._AC_SL1500_.jpg"), 150. ),
                new ProductEntity(2L, "Relax chair", URI.create("https://www.swiss-relax.be/websites/1/uploads/images/31-7-2020_16_02_57_medicare-bruin-1.jpg"), 300. ));
    }
}
