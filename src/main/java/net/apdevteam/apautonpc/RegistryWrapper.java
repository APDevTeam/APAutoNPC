package net.apdevteam.apautonpc;

import javax.annotation.Nullable;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.CitizensPlugin;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.api.npc.NPCRegistry;

public class RegistryWrapper implements Listener {
    private static @Nullable RegistryWrapper instance;

    public static RegistryWrapper getInstance() {
        if (instance == null) {
            instance = new RegistryWrapper();
        }
        return instance;
    }

    private @Nullable NPCRegistry registry;

    public @Nullable NPCRegistry getRegistry() {
        if (registry == null) {
            updateRegistry();
        }
        return registry;
    }

    private void updateRegistry() {
        try {
            Plugin test = CitizensAPI.getPlugin();
            if (!(test instanceof CitizensPlugin)) {
                APAutoNPC.getInstance().getLogger().info("Failed to get plugin");
                return;
            }
            registry = ((CitizensPlugin) test).getNPCRegistry();
            if (registry == null) {
                APAutoNPC.getInstance().getLogger().info("Registry is null: " + CitizensAPI.hasImplementation());
            } else {
                APAutoNPC.getInstance().getLogger().info("Updated registry");
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            APAutoNPC.getInstance().getLogger().info("Failed to get registry: " + CitizensAPI.hasImplementation());
        }
    }

    @EventHandler
    public void onEnable(CitizensEnableEvent e) {
        updateRegistry();
    }
}
