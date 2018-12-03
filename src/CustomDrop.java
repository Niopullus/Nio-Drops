import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomDrop {

    private Object target;
    private DropDistribution instead;
    private DropDistribution additional;
    private boolean vanillaInstead;

    public CustomDrop(final Object target) {
        super();
        this.target = target;
        instead = new DropDistribution();
        additional = new DropDistribution();
        vanillaInstead = false;
    }

    public Material getMaterialTarget() {
        return (Material) target;
    }

    public EntityType getEntityTarget() {
        return (EntityType) target;
    }

    public boolean isVanillaInstead() {
        return vanillaInstead;
    }

    public void setVanillaInstead(boolean vanillaInstead) {
        this.vanillaInstead = vanillaInstead;
    }

    public void applyDistribution(final BlockBreakEvent event, final boolean replace) {
        final Block block;
        ItemStack insteadDrop;
        ItemStack additionalDrop;
        final World world;
        Location location;
        final Player player;
        final ItemStack item;
        final EntityEquipment equipment;
        block = event.getBlock();
        player = event.getPlayer();
        equipment = player.getEquipment();
        item = equipment.getItemInMainHand();
        additionalDrop = additional.getDrop();
        world = block.getWorld();
        location = block.getLocation();
        location = new Location(location.getWorld(), location.getX() + 0.5, location.getY() + 0.5, location.getZ() + 0.5);
        if (replace) {
            insteadDrop = instead.getDrop();
            block.setType(Material.AIR);
            if (insteadDrop != null) {
                final ItemMeta itemMeta;
                final List<String> lore;
                final List<String> replacedLore;
                itemMeta = insteadDrop.getItemMeta();
                if (itemMeta != null) {
                    String displayName;
                    displayName = itemMeta.getDisplayName();
                    if (displayName != null) {
                        displayName = displayName.replace("*player*", player.getName());
                        itemMeta.setDisplayName(displayName);
                    }
                    lore = itemMeta.getLore();
                    if (lore != null) {
                        replacedLore = new ArrayList<>();
                        for (final String loreLine : lore) {
                            replacedLore.add(loreLine.replace("*player*", player.getName()));
                        }
                        itemMeta.setLore(replacedLore);
                    }
                    insteadDrop.setItemMeta(itemMeta);
                }
                world.dropItemNaturally(location, insteadDrop);
            }
        }
        if (additionalDrop != null) {
            final ItemMeta itemMeta;
            final List<String> lore;
            final List<String> replacedLore;
            itemMeta = additionalDrop.getItemMeta();
            if (itemMeta != null) {
                String displayName;
                displayName = itemMeta.getDisplayName();
                if (displayName != null) {
                    displayName = displayName.replace("*player*", player.getName());
                    itemMeta.setDisplayName(displayName);
                }
                lore = itemMeta.getLore();
                if (lore != null) {
                    replacedLore = new ArrayList<>();
                    for (final String loreLine : lore) {
                        replacedLore.add(loreLine.replace("*player*", player.getName()));
                    }
                    itemMeta.setLore(replacedLore);
                }
                additionalDrop.setItemMeta(itemMeta);
            }
            world.dropItemNaturally(location, additionalDrop);
        }
        item.setDurability((short) (item.getDurability() + 1));
        if (item.getDurability() > item.getType().getMaxDurability()) {
            equipment.setItemInMainHand(null);
        }
    }

    public void applyDistribution(final EntityDeathEvent event, final Player player, final boolean replace) {
        final List<ItemStack> drops;
        final ItemStack item1;
        final ItemStack item2;
        drops = event.getDrops();
        if (replace) {
            drops.clear();
            item1 = instead.getDrop();
            if (item1 != null) {
                final ItemMeta itemMeta;
                final List<String> lore;
                final List<String> replacedLore;
                itemMeta = item1.getItemMeta();
                if (itemMeta != null) {
                    String displayName;
                    displayName = itemMeta.getDisplayName();
                    if (displayName != null) {
                        displayName = displayName.replace("*player*", player.getName());
                        itemMeta.setDisplayName(displayName);
                    }
                    lore = itemMeta.getLore();
                    if (lore != null) {
                        replacedLore = new ArrayList<>();
                        for (final String loreLine : lore) {
                            replacedLore.add(loreLine.replace("*player*", player.getName()));
                        }
                        itemMeta.setLore(replacedLore);
                    }
                    item1.setItemMeta(itemMeta);
                }
                drops.add(item1);
            }
        }
        item2 = additional.getDrop();
        if (item2 != null) {
            final ItemMeta itemMeta;
            final List<String> lore;
            final List<String> replacedLore;
            itemMeta = item2.getItemMeta();
            if (itemMeta != null) {
                String displayName;
                displayName = itemMeta.getDisplayName();
                if (displayName != null) {
                    displayName = displayName.replace("*player*", player.getName());
                    itemMeta.setDisplayName(displayName);
                }
                lore = itemMeta.getLore();
                if (lore != null) {
                    replacedLore = new ArrayList<>();
                    for (final String loreLine : lore) {
                        replacedLore.add(loreLine.replace("*player*", player.getName()));
                    }
                    itemMeta.setLore(replacedLore);
                }
                item2.setItemMeta(itemMeta);
            }
            drops.add(item2);
        }
    }

    public void addInsteadItem(final DropDistributionItem item) {
        instead.addDropDistributionItem(item);
    }

    public void addAdditionItem(final DropDistributionItem item) {
        additional.addDropDistributionItem(item);
    }

    public void setInstead(final DropDistribution instead) {
        this.instead = instead;
    }

    public void setAdditional(final DropDistribution additional) {
        this.additional = additional;
    }

}
