package ap.autonpc.apdevteamuni.command;


import ap.autonpc.apdevteamuni.APAutoNPC;
import ap.autonpc.apdevteamuni.configuration.NPC;

import ap.autonpc.apdevteamuni.configuration.NPCManager;
import ap.autonpc.apdevteamuni.configuration.RegionType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.citizensnpcs.api.CitizensAPI;

import net.citizensnpcs.api.npc.NPCRegistry;

import net.milkbowl.vault.economy.Economy;

import static ap.autonpc.apdevteamuni.utils.RegionUtils.isPlayerInAirspace;
import static ap.autonpc.apdevteamuni.utils.RegionUtils.isPlayerInRegion;

public class AutoNPCCommand implements CommandExecutor {

    private static Economy economy;
    private static NPCRegistry registry;

    private NPCManager npcManager;

    public AutoNPCCommand() {
        economy = Bukkit.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        registry = CitizensAPI.getNPCRegistry();
        npcManager = APAutoNPC.getInstance().getNpcManager();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage("You must be a player to perform this command!");
            return true;
        }

        final Player player = (Player)sender;

        if(cmd.getName().equalsIgnoreCase("AutoNPC")) {

            if(args.length <= 0) {
                return false;
            }

            if(npcManager.getNPC(args[0]) == null) {
                sender.sendMessage("Could not find that type of NPC");
                return true;
            }

            NPC npc = npcManager.getNPC(args[0]);

            if(npc.getRegionType() == RegionType.AIRSPACE && !isPlayerInAirspace(player)){
                sender.sendMessage("You need to be in an airspace");
                return true;
            }

            if(npc.getRegionType() == RegionType.CLAIM && !isPlayerInRegion(player)) {
                sender.sendMessage("You need to be in a claim");
                return true;
            }

            if(npcManager.getNPC(args[0]) != null) {
                if(economy.getBalance(player) >= npc.getPrice()) {

                    if(registry.getById(npc.getID()) == null) {
                        sender.sendMessage("Configuration Error! Could not find NPC with this ID");
                        return true;
                    }

                    if(!player.hasPermission(npc.getPermNode())) {
                        sender.sendMessage("You do not have permissions to spawn this NPC");
                        return true;
                    }

                    net.citizensnpcs.api.npc.NPC clonedNpc = registry.getById(npc.getID()).clone();
                    clonedNpc.teleport(player.getLocation(), null);
                    economy.withdrawPlayer(player, npc.getPrice());

                    sender.sendMessage(npc.getName() + " has been placed!");
                    return true;
                }else {
                    sender.sendMessage("Insufficient funds");
                }
            }
        }
        return false;
    }

}
