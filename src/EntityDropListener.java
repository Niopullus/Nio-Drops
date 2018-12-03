import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.ProjectileSource;

import java.util.List;

public class EntityDropListener implements Listener {

    private CustomItemDrops drops;
    private FileConfiguration config;
    private String path;
    private int parseMode;

    public EntityDropListener(final FileConfiguration config, final String path) {
        super();
        this.config = config;
        this.path = path;
        this.parseMode = DropConfigParser.PARSE_MODE_ENTITY;
        drops = parseDrops();
    }

    private CustomItemDrops parseDrops() {
        final DropConfigParser parser;
        parser = new DropConfigParser(config);
        parser.setParseMode(parseMode);
        return parser.parseConfig(path);
    }

    public void reload(final FileConfiguration config) {
        this.config = config;
        drops = parseDrops();
    }

    public CustomItemDrops getDrops() {
        return drops;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMobDeath(final EntityDeathEvent event) {
        final Entity entity;
        entity = event.getEntity();
        if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent entityDamageEvent;
            entityDamageEvent = (EntityDamageByEntityEvent) entity.getLastDamageCause();
            if (entityDamageEvent.getDamager() instanceof Player) {
                final Player player;
                final CustomItemDrops customItemDrops;
                final EntityEquipment equipment;
                final ItemStack item;
                final ItemMeta itemMeta;
                final List<String> lore;
                player = (Player) entityDamageEvent.getDamager();
                customItemDrops = getDrops();
                equipment = player.getEquipment();
                item = equipment.getItemInMainHand();
                itemMeta = item.getItemMeta();
                if (itemMeta != null) {
                    lore = itemMeta.getLore();
                    customItemDrops.scanDrops(lore, event, player);
                }
            } else if (entityDamageEvent.getDamager() instanceof Arrow) {
                final ProjectileSource projectileSource;
                final Arrow arrow;
                arrow = (Arrow) entityDamageEvent.getDamager();
                projectileSource = arrow.getShooter();;
                if (projectileSource instanceof Player) {
                    final Player player;
                    final CustomItemDrops customItemDrops;
                    final EntityEquipment equipment;
                    final ItemStack item;
                    final ItemMeta itemMeta;
                    final List<String> lore;
                    player = (Player) projectileSource;
                    customItemDrops = getDrops();
                    equipment = player.getEquipment();
                    item = equipment.getItemInMainHand();
                    itemMeta = item.getItemMeta();
                    if (itemMeta != null) {
                        lore = itemMeta.getLore();
                        customItemDrops.scanDrops(lore, event, player);
                    }
                }
            }
        }
    }

}
