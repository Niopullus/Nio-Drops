import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CustomItemDrops implements Iterable<CustomDropSet> {

    private List<CustomDropSet> drops;

    public CustomItemDrops() {
        super();
        drops = new ArrayList<>();
    }

    public void addCustomDrop(final CustomDropSet customDrop) {
        drops.add(customDrop);
    }

    public Iterator<CustomDropSet> iterator() {
        return new CustomDropIterator(drops);
    }

    public void scanDrops(final List<String> lore, final BlockBreakEvent event) {
        if (lore != null) {
            for (final String loreLine : lore) {
                for (final CustomDropSet customDrop : drops) {
                    final String loreLine2;
                    loreLine2 = customDrop.getLore();
                    if (loreLine.equals(loreLine2)) {
                        customDrop.applyDistribution(event);
                        break;
                    }
                }
            }
        }
    }

    public void scanDrops(final List<String> lore, final EntityDeathEvent event, final Player player) {
        if (lore != null) {
            for (final String loreLine : lore) {
                for (final CustomDropSet customDrop : drops) {
                    final String loreLine2;
                    loreLine2 = customDrop.getLore();
                    if (loreLine.equals(loreLine2)) {
                        customDrop.applyDistribution(event, player);
                        break;
                    }
                }
            }
        }
    }

    public class CustomDropIterator implements Iterator<CustomDropSet> {

        private int index;
        private List<CustomDropSet> drops;

        public CustomDropIterator(List<CustomDropSet> drops) {
            super();
            this.drops = drops;
            index = 0;
        }

        public boolean hasNext() {
            return index < drops.size();
        }

        public CustomDropSet next() {
            final CustomDropSet drop;
            drop = drops.get(index);
            index += 1;
            return drop;
        }

    }

}
