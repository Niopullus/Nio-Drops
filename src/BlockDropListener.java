import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.List;

public class BlockDropListener implements Listener {

    private CustomItemDrops drops;
    private FileConfiguration config;
    private String path;
    private int parseMode;

    public BlockDropListener(final FileConfiguration config, final String path) {
        super();
        this.config = config;
        this.path = path;
        this.parseMode = DropConfigParser.PARSE_MODE_BLOCKS;
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
    public void onBlockBreak(final BlockBreakEvent event) {
        final Player player;
        final EntityEquipment equipment;
        final ItemStack item;
        final ItemMeta itemMeta;
        final List<String> lore;
        final CustomItemDrops customItemDrops;
        if (event.isCancelled()) {
            return;
        }
        player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        customItemDrops = getDrops();
        equipment = player.getEquipment();
        item = equipment.getItemInMainHand();
        itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            lore = itemMeta.getLore();
            customItemDrops.scanDrops(lore, event);
        }
    }

}
