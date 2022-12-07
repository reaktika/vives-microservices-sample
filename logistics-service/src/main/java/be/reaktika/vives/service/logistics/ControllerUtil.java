package be.reaktika.vives.service.logistics;

import be.reaktika.vives.furniture.api.model.product.Product;
import be.reaktika.vives.service.logistics.product.core.model.ProductEntity;

public class ControllerUtil {

    public static Product modelToApi(ProductEntity model) {
        Product apiProduct = new Product();
        apiProduct.setId(model.id());
        apiProduct.setName(model.name());
        apiProduct.setPicture(model.picture().toString());
        apiProduct.setUnitPrice(model.unitPrice().floatValue());
        apiProduct.setType(getType(model));
        return apiProduct;
    }

    private static Product.TypeEnum getType(ProductEntity model) {
        return Product.TypeEnum.CHAIR;
    }
}
