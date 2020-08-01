package net.apdevteam.apautonpc;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Owner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SellNPCCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!cmd.getName().equalsIgnoreCase("SellNPC")) {
            return false;
        }

        if(!(sender instanceof Player)){
            sender.sendMessage(APAutoNPC.PREFIX + "You must be a player to use this command!");
            return true;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("apautonpc.sell")) {
            sender.sendMessage(APAutoNPC.PREFIX + "You do not have permission for this command!");
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


        NPC selected = APAutoNPC.getInstance().getSelected(player);
        if(selected == null) {
            sender.sendMessage(APAutoNPC.PREFIX + "Please select an NPC to use that command.");
            return true;
        }

        Owner ownedTrait = selected.getTrait(Owner.class);
        if(ownedTrait.getOwnerId() != player.getUniqueId()) {
            sender.sendMessage(APAutoNPC.PREFIX + "You must own the selected NPC to use that command.");
            return true;
        }


        // Finally, sell NPC and notify player
        selected.destroy();
        APAutoNPC.getInstance().giveBalance(player, 1000000);
        sender.sendMessage(APAutoNPC.PREFIX + "Your NPC has been sold.");
        APAutoNPC.getInstance().getLogger().info(player.getName() + " has sold NPC ID: " + selected.getId());
        return true;
    }
}