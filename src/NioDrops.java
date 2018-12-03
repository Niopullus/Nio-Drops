import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NioDrops extends JavaPlugin {

    private BlockDropListener listener1;
    private EntityDropListener listener2;

    public void onEnable() {
        final Server server;
        final PluginManager pluginManager;
        final FileConfiguration config;
        final PlayerDeathListener playerDeathListener;
        final ArmorEquipListener armorEquipListener;
        final RightClickListener rightClickListener;
        final Wings wings;
        config = getConfig();
        listener1 = new BlockDropListener(config, "blocks");
        listener2 = new EntityDropListener(config, "entity");
        rightClickListener = new RightClickListener();
        wings = new Wings();
        armorEquipListener = new ArmorEquipListener();
        playerDeathListener = new PlayerDeathListener();
        server = getServer();
        pluginManager = server.getPluginManager();
        pluginManager.registerEvents(listener1, this);
        pluginManager.registerEvents(listener2, this);
        pluginManager.registerEvents(playerDeathListener, this);
        pluginManager.registerEvents(wings, this);
        pluginManager.registerEvents(armorEquipListener, this);
        pluginManager.registerEvents(rightClickListener, this);
    }

    private void configTest() {
        final FileConfiguration config;
        ConfigurationSection blocks, fire_blast, drops, stone, instead, additional, outcome1, outcome2, item1, item2;
        config = getConfig();
        blocks = config.createSection("blocks");
        fire_blast = blocks.createSection("fire blast");
        fire_blast.set("lore", "fire blast I");
        drops = fire_blast.createSection("drops");
        stone = drops.createSection("stone");
        instead = stone.createSection("instead");
        additional = stone.createSection("additional");

        outcome1 = instead.createSection("outcome1");
        outcome1.set("probability", 50);
        item1 = outcome1.createSection("item");
        item1.set("id", "stone");
        item1.set("name", "rough stone");
        item1.set("lore", "stone from the ground");
        item1.set("amount", 1);

        outcome2 = instead.createSection("outcome2");
        outcome2.set("probability", 50);
        item2 = outcome1.createSection("item");
        item2.set("id", "glass");
        item2.set("name", "smooth glass");
        item2.set("lore", "made by hand");
        item2.set("amount", 1);

        outcome1 = additional.createSection("outcome1");
        outcome1.set("probability", 50);
        item1 = outcome1.createSection("item");
        item1.set("id", "cookie");
        item1.set("name", "warm cookie");
        item1.set("lore", "straight from the oven");
        item1.set("amount", 1);

        outcome2 = additional.createSection("outcome2");
        outcome2.set("probability", 50);
        item2 = outcome1.createSection("item");
        item2.set("id", "potato");
        item2.set("name", "yummy potato");
        item2.set("lore", "very starchy");
        item2.set("amount", 1);

        saveConfig();
    }

    public void onDisable() {

    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, String[] args) {
        if (command.getName().equalsIgnoreCase("reloadNioDrops")) {
            final FileConfiguration config;
            reloadConfig();
            config = getConfig();
            listener1.reload(config);
            listener2.reload(config);
            sender.sendMessage("Successfully reloaded NioDrops.");
            return true;
        }
        return false;
    }

}
