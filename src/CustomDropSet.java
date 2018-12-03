import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.List;

public class CustomDropSet {

    private List<CustomDrop> customDrops;
    private String lore;

    public CustomDropSet(final String lore) {
        super();
        this.lore = lore;
        customDrops = new ArrayList<>();
    }

    public String getLore() {
        return lore;
    }

    public void addCustomDrop(final CustomDrop drop) {
        customDrops.add(drop);
    }

    public void applyDistribution(final BlockBreakEvent event) {
        final Material material;
        final Block block;
        block = event.getBlock();
        material = block.getType();
        for (final CustomDrop customDrop : customDrops) {
            final Material target;
            target = customDrop.getMaterialTarget();
            if (material.equals(target)) {
                customDrop.applyDistribution(event, !customDrop.isVanillaInstead());
                break;
            }
        }
    }

    public void applyDistribution(final EntityDeathEvent event, final Player player) {
        final Entity entity;
        final EntityType entityType;
        entity = event.getEntity();
        entityType = entity.getType();
        for (final CustomDrop customDrop : customDrops) {
            final EntityType target;
            target = customDrop.getEntityTarget();
            if (entityType.equals(target)) {
                customDrop.applyDistribution(event, player, !customDrop.isVanillaInstead());
                break;
            }
        }
    }

    public int dropCount() {
        return customDrops.size();
    }

}
