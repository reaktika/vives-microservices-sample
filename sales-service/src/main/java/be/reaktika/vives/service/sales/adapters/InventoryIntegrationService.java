package be.reaktika.vives.service.sales.adapters;

import be.reaktika.vives.service.sales.ports.InventoryItemClaim;
import be.reaktika.vives.service.sales.ports.InventoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InventoryIntegrationService implements InventoryPort {

    private final Logger logger = LoggerFactory.getLogger(InventoryIntegrationService.class);

    @Override
    public boolean claimItems(InventoryItemClaim itemsToClaim) {
        logger.info(String.format("claiming item %s with quantity %d", itemsToClaim.productId(), itemsToClaim.quantity()));
        return true;
    }
}
