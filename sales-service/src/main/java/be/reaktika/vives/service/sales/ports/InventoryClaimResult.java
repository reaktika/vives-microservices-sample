package be.reaktika.vives.service.sales.ports;

import java.util.Collection;

public record InventoryClaimResult(Collection<InventoryItemClaim> claimedItems, boolean isSuccess) {
}
