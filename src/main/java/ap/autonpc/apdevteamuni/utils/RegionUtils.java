package ap.autonpc.apdevteamuni.utils;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class RegionUtils {

    public static LinkedList<String> getPlayerRegions(Player player) {
        Location location = player.getLocation();
        RegionManager regionManager = ((WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard")).getRegionManager(location.getWorld());
        ApplicableRegionSet set = regionManager.getApplicableRegions(location);
        LinkedList<String> parentNames = new LinkedList<String>();
        LinkedList<String> regions = new LinkedList<String>();

        for(ProtectedRegion region : set){
            String id = region.getId();
            regions.add(id);
            ProtectedRegion parent = region.getParent();
            while(parent != null){
                parentNames.add(parent.getId());
                parent = parent.getParent();
            }
        }
        regions.removeAll(parentNames);
        return regions;
    }
    public static boolean isPlayerInRegion(Player player) {
        return getPlayerRegions(player).get(0) != null;
    }

    public static boolean isPlayerInAirspace(Player player) {
        for(String name : getPlayerRegions(player)){
            if(name.toLowerCase().contains("airspace")){
                return true;
            }
        }
        return false;
    }
}
