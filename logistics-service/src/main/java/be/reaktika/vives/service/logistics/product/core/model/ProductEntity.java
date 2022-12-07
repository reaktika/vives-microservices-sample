package be.reaktika.vives.service.logistics.product.core.model;

import java.net.URI;

public record ProductEntity(Long id, String name, URI picture, Double unitPrice) {
}
