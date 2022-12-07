package be.reaktika.vives.service.sales;

import be.reaktika.vives.service.sales.ports.InventoryClaimResult;
import be.reaktika.vives.service.sales.ports.InventoryItemClaim;
import be.reaktika.vives.service.sales.ports.InventoryPort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Service
public class InventoryService {

    private final InventoryPort inventoryPort;

    public InventoryService(InventoryPort inventoryPort) {
        this.inventoryPort = inventoryPort;
    }

    public InventoryClaimResult claimItems(Collection<InventoryItemClaim> claims) {
        //verify if the claimed items are still available
        var allTrue = claims.stream()
                                        .map(inventoryPort::claimItems)
                                        .reduce(Boolean.TRUE, Boolean::logicalAnd);
        return new InventoryClaimResult(claims, allTrue);
    }

    public CompletableFuture<InventoryClaimResult> claimItemsAsync(Collection<InventoryItemClaim> claims) {
        return CompletableFuture.supplyAsync(() -> {
            return claimItems(claims);
        });
    }
}
