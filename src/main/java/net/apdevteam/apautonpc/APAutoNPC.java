package net.apdevteam.apautonpc;

import java.util.List;
import java.util.Map;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.apdevteam.apautonpc.Config.Config;
import net.apdevteam.apautonpc.Config.Merchant;
import net.citizensnpcs.api.npc.NPC;
import com.degitise.minevid.dtlTraders.utils.citizens.TraderTrait;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.CitizensPlugin;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

import javax.annotation.Nullable;


public class APAutoNPC extends JavaPlugin {
    public static final String PREFIX = ChatColor.DARK_BLUE + "[" + ChatColor.AQUA + "APAutoNPC" + ChatColor.DARK_BLUE + "] " + ChatColor.GRAY;

    private static APAutoNPC instance;
    private Economy economy;
    private NPCRegistry registry;
    private WorldGuardPlugin worldGuard;

    public static APAutoNPC getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        MemorySection merchants = (MemorySection) getConfig().get("Merchants");
        for(String str : merchants.getKeys(false)) {
            Merchant m = new Merchant();
            m.id = merchants.getConfigurationSection(str).getInt("id", -1);
            m.cost = merchants.getConfigurationSection(str).getDouble("cost", 1000000);

            str = str.toUpperCase();
            Config.Merchants.put(str, m);
            getLogger().info("Loaded " + str);
        }

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

        getCommand("buynpc").setExecutor(new BuyNPCCommand());
        getCommand("sellnpc").setExecutor(new SellNPCCommand());

        getLogger().info("APAutoNPC " + getDescription().getVersion() + " has been enabled.");
    }

    public void onDisable() {

    }

    @Nullable
    public Merchant getMerchant(String type) {
        return Config.Merchants.getOrDefault(type, null);
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

    public boolean takeBalance(Player player, double balance) {
        if(economy.getBalance(player) < balance)
            return false;

        economy.withdrawPlayer(player, balance);
        return true;
    }

    public void giveBalance(Player player, double balance) {
        economy.depositPlayer(player, balance);
    }

    public NPC cloneNPC(int id, Location loc) {
        NPC oldNPC = registry.getById(id);
        if(oldNPC == null) {
            return null;
        }

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