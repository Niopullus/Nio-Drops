import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DropDistribution {

    private Random random;
    private List<DropDistributionItem> items;

    public DropDistribution() {
        super();
        items = new ArrayList<>();
        random = new Random();
    }

    public void addDropDistributionItem(final DropDistributionItem dropDistributionItem) {
        items.add(dropDistributionItem);
    }

    public ItemStack getDrop() {
        DropDistributionItem item;
        int index;
        int i;
        item = null;
        index = Math.abs(random.nextInt(100));
        i = 0;
        while (i < items.size()) {
            item = items.get(i);
            index -= item.getProbability();
            if (index <= 0) {
                break;
            }
            i += 1;
        }
        return item.getItemStack();
    }

}
