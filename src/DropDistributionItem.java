import org.bukkit.inventory.ItemStack;

public class DropDistributionItem {

    private ItemStack itemStack;
    private double probability;

    public DropDistributionItem(final ItemStack itemStack, final double probability) {
        super();
        this.itemStack = itemStack;
        this.probability = probability;
    }

    public ItemStack getItemStack() {
        if (itemStack != null) {
            final ItemStack itemClone;
            itemClone = itemStack.clone();
            itemClone.setDurability(itemStack.getDurability());
            return itemClone;
        }
        return null;
    }

    public double getProbability() {
        return probability;
    }

}
