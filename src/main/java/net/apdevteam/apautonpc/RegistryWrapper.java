package net.apdevteam.apautonpc;

import javax.annotation.Nullable;

import org.bukkit.plugin.Plugin;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.CitizensPlugin;
import net.citizensnpcs.api.npc.NPCRegistry;

public class RegistryWrapper {
    private static @Nullable NPCRegistry registry;

    public static @Nullable NPCRegistry getRegistry() {
        if (registry == null) {
            updateRegistry();
        }
        return registry;
    }

    private static void updateRegistry() {
        try {
            Plugin test = CitizensAPI.getPlugin();
            if (!(test instanceof CitizensPlugin)) {
                APAutoNPC.getInstance().getLogger().info("Failed to get plugin");
                return;
            }
            registry = ((CitizensPlugin) test).getNPCRegistry();
        } catch (IllegalStateException e) {
            APAutoNPC.getInstance().getLogger().info("Failed to get registry: " + CitizensAPI.hasImplementation());
        }
    }
}
