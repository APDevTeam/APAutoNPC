package net.apdevteam.apautonpc;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.apdevteam.apautonpc.Config.Config;
import net.apdevteam.apautonpc.Config.Merchant;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Owner;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SellNPCCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("SellNPC")) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(APAutoNPC.PREFIX + "You must be a player to use this command!");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("apautonpc.sell")) {
            sender.sendMessage(APAutoNPC.PREFIX + "You do not have permission for this command!");
            return true;
        }

        ProtectedRegion airspace = APAutoNPC.getInstance().isInAirspace(player.getLocation());
        if (airspace == null) {
            sender.sendMessage(APAutoNPC.PREFIX + "You must be in an airspace to use that command.");
            return true;
        } else if (!APAutoNPC.getInstance().isOwner(airspace, player)) {
            sender.sendMessage(APAutoNPC.PREFIX + "You must be an airspace owner to use that command.");
            return true;
        }


        NPC selected = APAutoNPC.getInstance().getSelected(player);
        if (selected == null) {
            sender.sendMessage(APAutoNPC.PREFIX + "Please select an NPC to use that command.");
            return true;
        }

        Owner ownedTrait = selected.getTraitNullable(Owner.class);
        if (ownedTrait == null || !ownedTrait.isOwnedBy(player)) {
            sender.sendMessage(APAutoNPC.PREFIX + "You must own the selected NPC to use that command.");
            return true;
        }

        Merchant type = Config.Merchants.get(selected.getName().replace(' ', '-').toUpperCase());
        if(type == null) {
            sender.sendMessage(APAutoNPC.PREFIX + "Unable to determine merchant type, please contact an administrator.");
            return true;
        }
        APAutoNPC.getInstance().giveBalance(player, type.cost);
        APAutoNPC.getInstance().getLogger().info(player.getName() + " has sold NPC ID: " + selected.getId() + " for: " + type.cost);

        // Finally, destroy the NPC and notify player
        selected.destroy();
        sender.sendMessage(APAutoNPC.PREFIX + "Your NPC has been sold.");
        return true;
    }
}