package be.reaktika.vives.service.sales.ports;


public interface InventoryPort {

    /**
     *
     * @param itemsToClaim
     * @return true if the product has been reserved in the requested quantity
     */
    boolean claimItems(InventoryItemClaim itemsToClaim);
}
