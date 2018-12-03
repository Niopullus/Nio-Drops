import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class RightClickListener implements Listener {

    public RightClickListener() {
        super();
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player;
        final PlayerInventory playerInventory;
        final ItemStack itemStack;
        final ItemMeta itemMeta;
        final Location playerLocation;
        player = event.getPlayer();
        playerLocation = player.getLocation();
        playerInventory = player.getInventory();
        itemStack = playerInventory.getItemInMainHand();
        itemMeta = itemStack.getItemMeta();
        Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("Nio-Drops"), new Runnable() {
            @Override
            public void run() {
                if (itemMeta.getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&4&lImportant and Valuable Brick"))) {
                    for (int counter = 0; counter < 14; counter++) {
                        for (int i = playerLocation.getBlockX() - counter; i <= playerLocation.getBlockX() + counter; i++) {
                            for (int j = playerLocation.getBlockY() - counter; j <= playerLocation.getBlockY() + counter; j++) {
                                for (int k = playerLocation.getBlockZ() - counter; k <= playerLocation.getBlockZ() + counter; k++) {
                                    final Block block;
                                    block = player.getWorld().getBlockAt(i, j, k);
                                    if (block != null) {
                                        final double x = (double) i;
                                        final double y = (double) j;
                                        final double z = (double) k;
                                        Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("Nio-Drops"), new Runnable() {
                                            @Override
                                            public void run() {
                                                int random = (int) (Math.random() * 400);
                                                if (random < 2) {
                                                    block.setType(Material.LAVA);
                                                } else if (random < 4) {
                                                    player.getWorld().spawnEntity(new Location(player.getWorld(), x, y, z), EntityType.PRIMED_TNT);
                                                } else {
                                                    block.setType(Material.FIRE);
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

}
