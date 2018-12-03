import net.minecraft.server.v1_13_R2.IRegistry;
import net.minecraft.server.v1_13_R2.Item;
import net.minecraft.server.v1_13_R2.MinecraftKey;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DropConfigParser {

    private FileConfiguration config;
    private int parseMode;

    public static final int PARSE_MODE_BLOCKS = 1;
    public static final int PARSE_MODE_ENTITY = 2;

    public DropConfigParser(final FileConfiguration config) {
        super();
        this.config = config;
        parseMode = 0;
    }

    public void setParseMode(final int parseMode) {
        this.parseMode = parseMode;
    }

    public CustomItemDrops parseConfig(final String path) {
        final CustomItemDrops customItemDrops;
        final ConfigurationSection blockSection;
        final Set<String> keys;
        blockSection = config.getConfigurationSection(path);
        keys = blockSection.getKeys(false);
        customItemDrops = new CustomItemDrops();
        for (final String key : keys) {
            final CustomDropSet customDropSet;
            final ConfigurationSection section;
            section = blockSection.getConfigurationSection(key);
            customDropSet = extractDropSet(section);
            customItemDrops.addCustomDrop(customDropSet);
        }
        return customItemDrops;
    }

    private CustomDropSet extractDropSet(final ConfigurationSection section) {
        final String lore;
        final ConfigurationSection drops;
        final CustomDropSet customDropSet;
        final Set<String> keys;
        lore = ChatColor.translateAlternateColorCodes('&', section.getString("lore"));
        drops = section.getConfigurationSection("drops");
        keys = drops.getKeys(false);
        customDropSet = new CustomDropSet(lore);
        for (final String key : keys) {
            final ConfigurationSection dropSection;
            final CustomDrop customDrop;
            dropSection = drops.getConfigurationSection(key);
            customDrop = extractDrop(dropSection);
            customDropSet.addCustomDrop(customDrop);
        }
        return customDropSet;
    }

    private CustomDrop extractDrop(final ConfigurationSection section) {
        Object target;
        final DropDistribution insteadDropDistribution;
        final DropDistribution additionalDropDistribution;
        final CustomDrop customDrop;
        final ConfigurationSection insteadSection;
        final ConfigurationSection additionalSection;
        target = null;
        if (parseMode == PARSE_MODE_BLOCKS) {
            target = materialFromId(section.getName());
        } else if (parseMode == PARSE_MODE_ENTITY) {
            target = entityTypeFromId(section.getName());
            if (target == null) {
                target = entityTypeFromId(section.getName());
            }
            if (target == null) {
                System.out.println("INVALID ENTITY ID CHOSEN");
            }
        }
        customDrop = new CustomDrop(target);
        insteadSection = section.getConfigurationSection("instead");
        if (insteadSection != null) {
            insteadDropDistribution = extractDistribution(insteadSection);
            customDrop.setInstead(insteadDropDistribution);
        } else {
            customDrop.setInstead(null);
            customDrop.setVanillaInstead(true);
        }
        additionalSection = section.getConfigurationSection("additional");
        additionalDropDistribution = extractDistribution(additionalSection);
        customDrop.setAdditional(additionalDropDistribution);
        return customDrop;
    }
/**
    private EntityType getEntityByName(String name) {
        for (EntityType type : EntityType.values()) {
            if(type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
**/
    private DropDistribution extractDistribution(final ConfigurationSection section) {
        final DropDistribution dropDistribution;
        final Set<String> distribution;
        dropDistribution = new DropDistribution();
        distribution = section.getKeys(false);
        for (final String items : distribution) {
            final ConfigurationSection itemDetails;
            final DropDistributionItem distributionItem;
            itemDetails = section.getConfigurationSection(items);
            distributionItem = extractDistributionItem(itemDetails);
            dropDistribution.addDropDistributionItem(distributionItem);
        }
        return dropDistribution;
    }

    private DropDistributionItem extractDistributionItem(final ConfigurationSection section) {
        final ConfigurationSection item;
        final double probability;
        final DropDistributionItem distributionItem;
        final ItemStack itemStack;
        item = section.getConfigurationSection("item");
        itemStack = extractItemStack(item);
        probability = section.getDouble("probability");
        distributionItem = new DropDistributionItem(itemStack, probability);
        return distributionItem;
    }

    private ItemStack extractItemStack(final ConfigurationSection section) {
        final ItemStack itemStack;
        final ItemMeta itemMeta;
        final String id;
        String name;
        String lore;
        final List<String> fullLore;
        final int amount;
        final String damage;
        Material material;
        id = section.getString("id");
        if (id.equals("*nothing*")) {
            itemStack = null;
        } else {
            material = materialFromId(id);
            if (material == null) {
                System.out.println("INVALID ITEM ID ENTERED");
            }
            name = section.getString("name");
            lore = section.getString("lore");
            damage = section.getString("damage");
            amount = section.getInt("amount");
            itemStack = new ItemStack(material, amount);
            itemMeta = itemStack.getItemMeta();
            if (name != null) {
                name = "" + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', name);
                itemMeta.setDisplayName(name);
            }
            if (lore != null) {
                String currentLine;
                lore = "" + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', lore);
                fullLore = new ArrayList<>();
                currentLine = "";
                for (final char c : lore.toCharArray()) {
                    if (c == '|') {
                        fullLore.add(currentLine);
                        currentLine = "";
                    } else {
                        currentLine += c;
                    }
                }
                fullLore.add(currentLine);
                itemMeta.setLore(fullLore);
            }
            itemStack.setItemMeta(itemMeta);
            if (damage != null) {
                itemStack.setDurability((short) Integer.parseInt(damage));
            }
        }
        return itemStack;
    }

    private Material materialFromId(final String id) {
        final MinecraftKey minecraftKey;
        final Item item;
        String name;
        minecraftKey = new MinecraftKey(id);
        item = IRegistry.ITEM.get(minecraftKey);
        if (item != null) {
            final Material material;
            name = item.getName();
            name = name.substring(name.lastIndexOf('.') + 1).toUpperCase();
            material = Material.getMaterial(name);
            return material;
        }
        return null;
    }

    private EntityType entityTypeFromId(final String id) {
        return EntityType.valueOf(id.toUpperCase());
    }

}
