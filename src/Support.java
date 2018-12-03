import com.palmergames.bukkit.towny.object.PlayerCache;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class Support {

    public static boolean inTerritory(Player player) {
        if(townyLoaded()) {
            if(TownySupport.inTerritory(player)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFriendly(Entity P, Entity O) {
        if(P instanceof Player && O instanceof Player) {
            Player player = (Player) P;
            Player other = (Player) O;
            if(townyLoaded()) {
                if(TownySupport.isFriendly(player, other)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean townyLoaded() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Towny");
        return plugin != null;
    }

    public static boolean inWorldEditRegion(final Player player) {
        if (worldeditLoaded() && worldguardLoaded()) {
            final WorldGuard worldGuard;
            final ApplicableRegionSet regions;
            final WorldGuardPlatform worldGuardPlatform;
            final RegionContainer regionContainer;
            final World world;
            final RegionManager regionManager;
            final Location playerLocation;
            boolean access;
            worldGuard = WorldGuard.getInstance();
            worldGuardPlatform = worldGuard.getPlatform();
            world = worldGuardPlatform.getWorldByName(player.getWorld().getName());
            regionContainer = worldGuardPlatform.getRegionContainer();
            regionManager = regionContainer.get(world);
            playerLocation = player.getLocation();
            regions = regionManager.getApplicableRegions(new Vector(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ()));
            access = false;
            outer:
            for (final ProtectedRegion region : regions) {
                final LocalPlayer localPlayer;
                final WorldGuardPlugin worldEditPlugin;
                worldEditPlugin = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
                localPlayer = worldEditPlugin.wrapPlayer(player);
                if (region.isMember(localPlayer)) {
                    access = true;
                    break outer;
                }
            }
            return access;
        }
        return false;
    }

    private static boolean worldeditLoaded() {
        return Bukkit.getPluginManager().getPlugin("WorldEdit") != null;
    }

    private static boolean worldguardLoaded() {
        return Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
    }

}
