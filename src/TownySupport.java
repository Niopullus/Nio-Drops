import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.*;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import com.palmergames.bukkit.util.BukkitTools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TownySupport {

    public static Boolean inTerritory(Player player) {
        try {
            TownBlock block = WorldCoord.parseWorldCoord(player).getTownBlock();
            Block mcBlock;
            Location location = player.getLocation();
            mcBlock = player.getWorld().getBlockAt(location);
            if (mcBlock != null) {
                boolean result = PlayerCacheUtil.getCachePermission(player, location, BukkitTools.getTypeId(mcBlock), BukkitTools.getData(mcBlock), TownyPermission.ActionType.BUILD);
                boolean result2 = false;
                final Resident resident;
                resident = TownyUniverse.getDataSource().getResident(player.getName());
                if (resident.hasTown()) {
                    result2 = resident.getTown().hasTownBlock(block);
                }
                return (result || result2) && block.getTown() != null;
            }
        }catch(NotRegisteredException e) {
        }
        return false;
    }

    public static Boolean isFriendly(Player player, Player other) {
        try {
            Resident playerUser = TownyUniverse.getDataSource().getResident(player.getName());
            Resident otherUser = TownyUniverse.getDataSource().getResident(other.getName());
            if(playerUser.getTown().getName().equalsIgnoreCase(otherUser.getTown().getName())) {
                return true;
            }
        }catch(NotRegisteredException e) {
        }
        return false;
    }

}
