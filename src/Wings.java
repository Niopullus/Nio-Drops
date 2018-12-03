import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Wings implements Listener {


    private static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Nio-Drops");

    public static ArrayList<Player> Flying = new ArrayList<Player>();

    @EventHandler
    public void onEquip(ArmorEquipEvent e) {
        Player player = e.getPlayer();
        ItemStack NewItem = e.getNewArmorPiece();
        ItemStack OldItem = e.getOldArmorPiece();
        if(hasWingsBoots(NewItem)) {
            if(wingsEnabled()) {
                if(Support.inTerritory(player) || Support.inWorldEditRegion(player)) {
                    if(player.getGameMode() != GameMode.CREATIVE) {
                        if(player.getGameMode() != GameMode.ADVENTURE) {
                            player.setAllowFlight(true);
                        }
                    }
                }
            }
        }
        if(hasWingsBoots(OldItem)) {
            if(wingsEnabled()) {
                if(player.getGameMode() != GameMode.CREATIVE) {
                    if(player.getGameMode() != GameMode.ADVENTURE) {
                        player.setAllowFlight(false);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent e) {
        Player player = e.getPlayer();
        ItemStack boots = player.getEquipment().getBoots();
        if(hasWingsBoots(boots)) {
            if(wingsEnabled()) {
                if(Support.inTerritory(player) || Support.inWorldEditRegion(player)) {
                    if(e.isFlying()) {
                        if(player.getAllowFlight()) {
                            e.setCancelled(true);
                            player.setFlying(true);
                            Flying.add(player);
                        }
                    }else {
                        Flying.remove(player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        ItemStack boots = player.getEquipment().getBoots();
        if(hasWingsBoots(boots)) {
            if(wingsEnabled()) {
                if(Support.inTerritory(player) || Support.inWorldEditRegion(player)) {
                    if(!player.getAllowFlight()) {
                        player.setAllowFlight(true);
                    }
                }else {
                    if(player.isFlying()) {
                        if(player.getGameMode() != GameMode.CREATIVE) {
                            if(player.getGameMode() != GameMode.ADVENTURE) {
                                player.setFlying(false);
                                player.setAllowFlight(false);
                                Flying.remove(player);
                            }
                        }
                    }
                }
                if(player.isFlying()) {
                    Flying.add(player);
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        ItemStack boots = player.getEquipment().getBoots();
        if(hasWingsBoots(boots)) {
            if(wingsEnabled()) {
                if(Support.inTerritory(player) || Support.inWorldEditRegion(player)) {
                    player.setAllowFlight(true);
                    Flying.add(player);
                }
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        ItemStack boots = player.getEquipment().getBoots();
        if(hasWingsBoots(boots)) {
            if(wingsEnabled()) {
                player.setFlying(false);
                player.setAllowFlight(false);
                Flying.remove(player);
            }
        }
    }
/**
    public static void onStart() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if(Flying.contains(player)) {
                        Location l = player.getLocation().subtract(0, .25, 0);
                        if(player.isFlying()) {
                            ItemStack boots = player.getEquipment().getBoots();
                            if(boots != null) {
                                if(hasWingsBoots(boots)) {
                                    if(wingsEnabled())) {
                                        ParticleEffect.CLOUD.display((float) .25, (float) 0, (float) .25, 0, 10, l, 100);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 1, 1);
    }
**/
    private static boolean wingsEnabled() {
        final FileConfiguration config;
        config = Bukkit.getPluginManager().getPlugin("Nio-Drops").getConfig();
        if (config.contains("enableWings")) {
            return config.getBoolean("enableWings");
        }
        return true;
    }

    private static boolean hasWingsBoots(final ItemStack item) {
        final ItemMeta itemMeta;
        final List<String> lore;
        if (item != null) {
            itemMeta = item.getItemMeta();
            if (itemMeta != null) {
                lore = itemMeta.getLore();
                if (lore != null) {
                    for (final String line : lore) {
                        if (line.equals(ChatColor.translateAlternateColorCodes('&', "&7Wings I"))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
