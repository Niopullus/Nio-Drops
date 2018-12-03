import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDeathListener implements Listener {

    private Map<String, List<ItemStack>> items;

    public PlayerDeathListener() {
        super();
        items = new HashMap<>();
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        final Player player;
        final List<ItemStack> itemStacks;
        final List<ItemStack> toKeep;
        player = event.getEntity();
        itemStacks = event.getDrops();
        toKeep = new ArrayList<>();
        for (final ItemStack item : itemStacks) {
            final ItemMeta itemMeta;
            final List<String> lore;
            if (item != null) {
                itemMeta = item.getItemMeta();
                if (itemMeta != null) {
                    lore = itemMeta.getLore();
                    if (lore != null) {
                        for (final String loreLine : lore) {
                            if (loreLine.equals(ChatColor.translateAlternateColorCodes('&', "&7Bind I")) ||
                                    loreLine.equals(ChatColor.translateAlternateColorCodes('&', "&r&7Bind I"))    ) {
                                toKeep.add(item);
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (toKeep.size() > 0) {
            items.put(player.getUniqueId().toString(), toKeep);
            for (final ItemStack itemStack : toKeep) {
                itemStacks.remove(itemStack);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        final Player player;
        player = event.getPlayer();
        if (items.containsKey(player.getUniqueId().toString())) {
            final List<ItemStack> toGive;
            final PlayerInventory playerInventory;
            toGive = items.get(player.getUniqueId().toString());
            playerInventory = player.getInventory();
            for (final ItemStack item : toGive) {
                playerInventory.addItem(item);
            }
            items.remove(player.getUniqueId().toString());
        }
    }

}
