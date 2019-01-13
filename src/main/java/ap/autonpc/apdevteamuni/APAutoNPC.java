package ap.autonpc.apdevteamuni;

import ap.autonpc.apdevteamuni.command.AutoNPCCommand;
import ap.autonpc.apdevteamuni.configuration.NPC;
import ap.autonpc.apdevteamuni.configuration.NPCManager;
import ap.autonpc.apdevteamuni.configuration.RegionType;
import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;


public class APAutoNPC extends JavaPlugin {

    private NPCManager npcManager = new NPCManager();
    private static APAutoNPC INSTANCE;

	public void onEnable() {
	    INSTANCE = this;
	    readConfig();
        this.getCommand("AutoNPC").setExecutor(new AutoNPCCommand());
	}
	
	public void onDisable() {
		Plugin plugin = null;
	}

	public NPCManager getNpcManager() {
	    return npcManager;
    }

    public static APAutoNPC getInstance() {
        return INSTANCE;
    }

	private void readConfig() {
        this.saveDefaultConfig();

        ConfigurationSection configurationSection = getConfig().getConfigurationSection("npcs");
        int i = 0;

        for(String npc : configurationSection.getKeys(false)) {
            int id = getConfig().getInt("npcs." + npc + ".id");
            int price = getConfig().getInt("npcs." + npc + ".price");
            String permNode = getConfig().getString("npcs." + npc + ".permnode");
            String region = getConfig().getString("npcs." + npc + ".regionType");
            RegionType regionType = (region.equalsIgnoreCase("airspace")) ? RegionType.AIRSPACE : (region.equalsIgnoreCase("claim") ? RegionType.CLAIM : RegionType.NONE);
            //RegionType regionType = RegionType.AIRSPACE;

            npcManager.registerNPC(new NPC(npc,id,price,permNode,regionType));

            i++;
        }
        Bukkit.getLogger().info("Loaded " + i + " NPCs!");
    }
}
