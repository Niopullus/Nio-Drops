import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ArmorEquipListener implements Listener {

    private Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Nio-Drops");

    public ArmorEquipListener() {
        super();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public final void onInventoryClick(final InventoryClickEvent e) {
        boolean shift = false, numberkey = false;
        Player player = (Player) e.getWhoClicked();
        if(e.isCancelled()) return;
        if(e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)) {
            shift = true;
        }
        if(e.getClick().equals(ClickType.NUMBER_KEY)) {
            numberkey = true;
        }
        if((e.getSlotType() != InventoryType.SlotType.ARMOR || e.getSlotType() != InventoryType.SlotType.QUICKBAR) && !(e.getInventory().getType().equals(InventoryType.CRAFTING) || e.getInventory().getType().equals(InventoryType.PLAYER))) {
            return;
        }
        if(!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        if(e.getCurrentItem() == null) {
            return;
        }
        ArmorType newArmorType = ArmorType.matchType(shift ? e.getCurrentItem() : e.getCursor());
        if(!shift && newArmorType != null && e.getRawSlot() != newArmorType.getSlot()) {
            // Used for drag and drop checking to make sure you aren't trying to place a helmet in the boots place.
            return;
        }

        if(e.getInventory().getType() == InventoryType.CRAFTING) {
            // Stops the activation when a player shift clicks from their small crafting option.
            if(e.getRawSlot() >= 0 && e.getRawSlot() <= 4) {
                return;
            }
        }
        if(shift) {
            newArmorType = ArmorType.matchType(e.getCurrentItem());
            if(newArmorType != null) {
                boolean equipping = true;
                if(e.getRawSlot() == newArmorType.getSlot()) {
                    equipping = false;
                }
                if(newArmorType.equals(ArmorType.HELMET) && (equipping ? e.getWhoClicked().getInventory().getHelmet() == null : e.getWhoClicked().getInventory().getHelmet() != null) || newArmorType.equals(ArmorType.CHESTPLATE) && (equipping ? e.getWhoClicked().getInventory().getChestplate() == null : e.getWhoClicked().getInventory().getChestplate() != null) || newArmorType.equals(ArmorType.LEGGINGS) && (equipping ? e.getWhoClicked().getInventory().getLeggings() == null : e.getWhoClicked().getInventory().getLeggings() != null) || newArmorType.equals(ArmorType.BOOTS) && (equipping ? e.getWhoClicked().getInventory().getBoots() == null : e.getWhoClicked().getInventory().getBoots() != null)) {
                    ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.SHIFT_CLICK, newArmorType, equipping ? null : e.getCurrentItem(), equipping ? e.getCurrentItem() : null);
                    Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                    if(armorEquipEvent.isCancelled()) {
                        e.setCancelled(true);
                    }
                }
            }
        }else {
            ItemStack newArmorPiece = e.getCursor();
            if(newArmorPiece == null) {
                newArmorPiece = new ItemStack(Material.AIR);
            }
            ItemStack oldArmorPiece = e.getCurrentItem();
            if(oldArmorPiece == null) {
                oldArmorPiece = new ItemStack(Material.AIR);
            }
            if(numberkey) {
                if(e.getInventory().getType().equals(InventoryType.PLAYER)) {// Prevents shit in the 2by2 crafting
                    // e.getClickedInventory() == The players inventory
                    // e.getHotBarButton() == key people are pressing to equip or unequip the item to or from.
                    // e.getRawSlot() == The slot the item is going to.
                    // e.getSlot() == Armor slot, can't use e.getRawSlot() as that gives a hotbar slot ;-;
                    ItemStack hotbarItem = e.getInventory().getItem(e.getHotbarButton());
                    if(hotbarItem != null) {// Equipping
                        newArmorType = ArmorType.matchType(hotbarItem);
                        newArmorPiece = hotbarItem;
                        oldArmorPiece = e.getInventory().getItem(e.getSlot());
                    }else {// Unequipping
                        newArmorType = ArmorType.matchType(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR ? e.getCurrentItem() : e.getCursor());
                    }
                }
            }else {
                // e.getCurrentItem() == Unequip
                // e.getCursor() == Equip
                newArmorType = ArmorType.matchType(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR ? e.getCurrentItem() : e.getCursor());
            }
            if(newArmorType != null && e.getRawSlot() == newArmorType.getSlot()) {
                ArmorEquipEvent.EquipMethod method = ArmorEquipEvent.EquipMethod.DRAG;
                if(e.getAction().equals(InventoryAction.HOTBAR_SWAP) || numberkey) {
                    method = ArmorEquipEvent.EquipMethod.HOTBAR_SWAP;
                }
                final ItemStack It = newArmorPiece.clone();
                final ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, method, newArmorType, oldArmorPiece, newArmorPiece);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    ItemStack I = e.getWhoClicked().getInventory().getItem(e.getSlot());
                    if(e.getInventory().getType().equals(InventoryType.PLAYER)) {
                        if(e.getSlot() == ArmorType.HELMET.getSlot()) {
                            I = e.getWhoClicked().getEquipment().getHelmet();
                        }
                        if(e.getSlot() == ArmorType.CHESTPLATE.getSlot()) {
                            I = e.getWhoClicked().getEquipment().getChestplate();
                        }
                        if(e.getSlot() == ArmorType.LEGGINGS.getSlot()) {
                            I = e.getWhoClicked().getEquipment().getLeggings();
                        }
                        if(e.getSlot() == ArmorType.BOOTS.getSlot()) {
                            I = e.getWhoClicked().getEquipment().getBoots();
                        }
                    }
                    if(I == null) {
                        if(e.getInventory().getType().equals(InventoryType.CRAFTING)) {
                            I = new ItemStack(Material.AIR, 0);
                        }
                        if(e.getInventory().getType().equals(InventoryType.PLAYER)) {
                            I = new ItemStack(Material.AIR, 1);
                        }
                    }
                    // I == the old item
                    // It == the new item
                    if(I.isSimilar(It) || (I.getType() == Material.AIR && It.getType() == Material.AIR)) {
                        Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                        if(armorEquipEvent.isCancelled()) {
                            e.setCancelled(true);
                        }
                    }
                }, 0);
            }else {
                if(e.getHotbarButton() >= 0) {
                    newArmorPiece = e.getWhoClicked().getInventory().getItem(e.getHotbarButton());
                    if(oldArmorPiece != null) {
                        if(ArmorType.matchType(oldArmorPiece) != null || oldArmorPiece.getType() == Material.AIR) {
                            if(ArmorType.matchType(newArmorPiece) != null || newArmorPiece == null) {
                                if(ArmorType.matchType(oldArmorPiece) != null) {
                                    if(e.getRawSlot() != ArmorType.matchType(oldArmorPiece).getSlot()) {
                                        return;
                                    }
                                }
                                if(ArmorType.matchType(newArmorPiece) != null) {
                                    if(e.getRawSlot() != ArmorType.matchType(newArmorPiece).getSlot()) {
                                        return;
                                    }
                                }
                                ArmorEquipEvent.EquipMethod method = ArmorEquipEvent.EquipMethod.DRAG;
                                if(e.getAction().equals(InventoryAction.HOTBAR_SWAP) || numberkey) {
                                    method = ArmorEquipEvent.EquipMethod.HOTBAR_SWAP;
                                }
                                ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, method, newArmorType, oldArmorPiece, newArmorPiece);
                                Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                                if(armorEquipEvent.isCancelled()) {
                                    e.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
