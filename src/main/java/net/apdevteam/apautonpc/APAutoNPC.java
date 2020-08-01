package net.apdevteam.apautonpc;

import java.util.HashMap;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.citizensnpcs.api.npc.NPC;
import com.degitise.minevid.dtlTraders.utils.citizens.TraderTrait;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.CitizensPlugin;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;


public class APAutoNPC extends JavaPlugin {
    public static final String PREFIX = ChatColor.DARK_BLUE + "[" + ChatColor.AQUA + "APAutoNPC" + ChatColor.DARK_BLUE + "] " + ChatColor.GRAY;
    public static final int MERCHANT_COST = 1000000;

    private static APAutoNPC instance;
    private Economy economy;
    private NPCRegistry registry;
    private WorldGuardPlugin worldGuard;
    HashMap<String, Integer> merchIDs = new HashMap<>();

    public static APAutoNPC getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;

        economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        if(!(economy instanceof Economy)) {
            getLogger().severe("Failed to load economy!");
            return;
        }
        Plugin test = CitizensAPI.getPlugin();
        if(!(test instanceof CitizensPlugin)) {
            getLogger().severe("Failed to load Citizens!");
            return;
        }
        registry = ((CitizensPlugin) test).getNPCRegistry();
        if(!(registry instanceof NPCRegistry)) {
            getLogger().severe("Failed to load NPC registry!");
            return;
        }
        test = getServer().getPluginManager().getPlugin("WorldGuard");
        if(!(test instanceof WorldGuardPlugin)) {
            getLogger().severe("Failed to load WorldGuard!");
            return;
        }
        worldGuard = (WorldGuardPlugin) test;


        // TODO: Configure this somehow
        merchIDs.put("minerals", 3209);

        this.getCommand("buynpc").setExecutor(new BuyNPCCommand());
        this.getCommand("sellnpc").setExecutor(new SellNPCCommand());
    }

    public void onDisable() {

    }

    public int getID(String type) {
        return merchIDs.getOrDefault(type, -1);
    }

    public ProtectedRegion isInAirspace(Location loc) {
        RegionManager regionManager = worldGuard.getRegionManager(loc.getWorld());
        for(ProtectedRegion r : regionManager.getApplicableRegions(loc)) {
            if(r.getId().toLowerCase().contains("airspace")
                    && r.getFlag(DefaultFlag.PVP) == StateFlag.State.DENY
                    && r.getFlag(DefaultFlag.TNT) == StateFlag.State.DENY) {
                return r;
            }
        }
        return null;
    }

    public boolean isOwner(ProtectedRegion region, Player player) {
        return region.getOwners().contains(player.getUniqueId()) || region.getOwners().contains(player.getName());
    }

    public boolean takeBalance(Player player, int balance) {
        if(economy.getBalance(player) < balance)
            return false;

        economy.withdrawPlayer(player, balance);
        return true;
    }

    public void giveBalance(Player player, int balance) {
        economy.depositPlayer(player, balance);
    }

    public NPC cloneNPC(int id, Location loc) {
        NPC oldNPC = registry.getById(id);

        NPC newNPC = oldNPC.clone();
        newNPC.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);

        String shopName = oldNPC.getTrait(TraderTrait.class).getGUIName();
        newNPC.getTrait(TraderTrait.class).setGUIName(shopName);

        return newNPC;
    }

    public NPC getSelected(Player player) {
        return CitizensAPI.getDefaultNPCSelector().getSelected(player);
    }
}