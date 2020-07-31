package net.apdevteam.apautonpc;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.CitizensPlugin;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Owner;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class NPCCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!cmd.getName().equalsIgnoreCase("AutoNPC")) {
            return false;
        }

        if(!(sender instanceof Player)){
            sender.sendMessage(APAutoNPC.PREFIX + "You must be a player to use this command");
            return true;
        }
        Player player = (Player) sender;

        if(args.length != 1) {
            sender.sendMessage(APAutoNPC.PREFIX + "Invalid usage, please type /AutoNPC help");
            return true;
        }


        if(args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(APAutoNPC.PREFIX + "Use /AutoNPC <type> to get a merchant.");
            sender.sendMessage(APAutoNPC.PREFIX + "Valid types:");
            for(String s : APAutoNPC.getInstance().merchIDs.keySet()) {
                sender.sendMessage(APAutoNPC.PREFIX + "  - " + s.toUpperCase());
            }
            return true;
        }


        int merchID = APAutoNPC.getInstance().getID(args[0]);
        if(merchID == -1) {
            sender.sendMessage(APAutoNPC.PREFIX + "That is not a valid NPC type, please type /AutoNPC help");
            return true;
        }

        ProtectedRegion airspace = APAutoNPC.getInstance().isInAirspace(player.getLocation());
        if(airspace == null) {
            sender.sendMessage(APAutoNPC.PREFIX + "You must be in an airspace to use that command.");
            return true;
        }
        else if(!APAutoNPC.getInstance().isOwner(airspace, player)) {
            sender.sendMessage(APAutoNPC.PREFIX + "You must be an airspace owner to use that command.");
            return true;
        }

        if(!APAutoNPC.getInstance().takeBalance(player, 1000000)) {
            sender.sendMessage(APAutoNPC.PREFIX + "You cannot afford this!");
            return true;
        }

        // Finally, clone NPC and notify player
        NPC npc = APAutoNPC.getInstance().cloneNPC(merchID, player.getLocation());

        npc.getTrait(Owner.class).setOwner(player);
        sender.sendMessage(APAutoNPC.PREFIX + "Congratulations on your new NPC!");
        return true;
    }
}